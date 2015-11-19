package com.testfairy.uploader;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

class TestFairyService {
	private final String serverAddress;
	private final ProxyInfo proxyInfo;
	private Request request;

	TestFairyService(String serverAddress, ProxyInfo proxyInfo) {
		this.serverAddress = serverAddress;
		this.proxyInfo = proxyInfo;
	}

	Request newRequest() {
		request = new Request(serverAddress, proxyInfo);
		return request;
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
		private final ProxyInfo proxyInfo;

		public Request(String address, ProxyInfo proxyInfo) {
			this.serverAddress = address;
			this.proxyInfo = proxyInfo;
			this.entity = new MultipartEntity();
		}

		public Request addString(String key, String value) {
			addEntry(entity, key, value);
			return this;
		}

		public Request addFile(String key, File file) {
			entity.addPart(key, new FileBody(file));
			return this;
		}

		public void upload() {
			try {
				DefaultHttpClient httpClient = new DefaultHttpClient();
				proxyInfo.apply(httpClient);

				HttpPost post = new HttpPost(getUploadUrl());

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

				System.out.println("post finished " + responseString);
			} catch (Exception exception) {
				throw new RuntimeException(exception);
			}
		}


		private String getUploadUrl() {
			return String.format("%s%s", serverAddress, "/api/upload");
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
