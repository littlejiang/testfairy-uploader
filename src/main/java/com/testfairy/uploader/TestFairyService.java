package com.testfairy.uploader;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.*;
import java.util.Scanner;

class TestFairyService {
    private final String serverAddress;
    private final String userAgent;
    private final ProxyInfo proxyInfo;

    TestFairyService(
        String serverAddress,
        String userAgent,
        ProxyInfo proxyInfo
    ) {
        this.serverAddress = serverAddress;
        this.userAgent = userAgent;
        this.proxyInfo = proxyInfo;
    }

    Request newRequest() {
        return new Request(serverAddress, userAgent, proxyInfo);
    }

    File download(String url, String localFilename) {
        FileOutputStream fis = null;
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            proxyInfo.apply(httpClient);

            HttpGet httpget = new HttpGet(url);
            HttpResponse response = httpClient.execute(httpget);
            HttpEntity entity = response.getEntity();

            fis = new FileOutputStream(localFilename);
            IOUtils.copy(entity.getContent(), fis);
            return new File(localFilename);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        } finally {
            IOUtils.closeQuietly(fis);
        }
    }

    static class ProxyInfo {
        private final String host;
        private final int port;
        private final String user;
        private final String password;

        ProxyInfo(String host, int port, String user, String password) {
            this.host = host;
            this.port = port;
            this.user = user;
            this.password = password;
        }

        void apply(DefaultHttpClient client) {
            if (Strings.isEmpty(host))
                return;

            HttpHost proxy = new HttpHost(host, port);
            if (!Strings.isEmpty(user)) {
                AuthScope authScope = new AuthScope(user, port);
                Credentials credentials = new UsernamePasswordCredentials(user, password);
                client.getCredentialsProvider().setCredentials(authScope, credentials);
            }

            client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        }
    }

    static class Request {
        private final MultipartEntity entity;
        private final String serverAddress;
        private final String userAgent;
        private final ProxyInfo proxyInfo;
        private final Gson deserializer;

        public Request(
            String address,
            String userAgent,
            ProxyInfo proxyInfo
        ) {
            this.serverAddress = address;
            this.userAgent = userAgent;
            this.proxyInfo = proxyInfo;
            this.entity = new MultipartEntity();
            this.deserializer = new Gson();
        }

        public Request addString(String key, String value) {
            addEntry(entity, key, value);
            return this;
        }

        public Request addFile(String key, File file) {
            if (! file.exists())
                throw new IllegalArgumentException("No file was found at: " + file.getAbsolutePath());

            entity.addPart(key, new FileBody(file));
            return this;
        }

        public Build upload() {
            return uploadTo(getUploadUrl());
        }

        public Build uploadSigned() {
            return uploadTo(getSignedUploadUrl());
        }

        private Build uploadTo(String endpoint) {
            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                proxyInfo.apply(httpClient);

                HttpPost post = new HttpPost(endpoint);
                post.addHeader("User-Agent", userAgent);
                post.setEntity(entity);

                HttpResponse response = httpClient.execute(post);
                InputStream is = response.getEntity().getContent();

                // Improved error handling.
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != 200) {
                    String responseBody = new Scanner(is).useDelimiter("\\A").next();
                    throw new Exception(responseBody);
                }

                StringWriter writer = new StringWriter();
                IOUtils.copy(is, writer, "UTF-8");
                String responseString = writer.toString();

                Build fromJson = this.deserializer.fromJson(responseString, Build.class);
                if ("ok".equals(fromJson.status())) return fromJson;

                FailedUploadResponse failed = deserializer.fromJson(responseString, FailedUploadResponse.class);
                throw new RuntimeException(failed.message);
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }

        private String getUploadUrl() {
            return String.format("%s%s", serverAddress, "/api/upload");
        }

        private String getSignedUploadUrl() {
            return String.format("%s%s", serverAddress, "/api/upload-signed");
        }

        private static void addEntry(MultipartEntity entity, String key, String value) {
            try {
                if (Strings.isEmpty(value))
                    return;

                entity.addPart(key, new StringBody(value));
            } catch (UnsupportedEncodingException exception) {
                throw new IllegalArgumentException(exception);
            }
        }
    }
}
