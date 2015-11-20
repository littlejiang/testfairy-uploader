package com.testfairy.uploader;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class JarSigner {
    private final String jarSignerPath;
    private final String keyStorePath;
    private final String keyStoreAlias;
    private final String storePassword;
    private final String keyPassword;
    private final ProcessExecutor executor;

    public JarSigner(
        String jarSignerPath,
        String keyStorePath,
        String keyStoreAlias,
        String storePassword,
        String keyPassword,
        ProcessExecutor executor
    ) {
        this.jarSignerPath = jarSignerPath;
        this.keyStorePath = keyStorePath;
        this.keyStoreAlias = keyStoreAlias;
        this.storePassword = storePassword;
        this.keyPassword = keyPassword;
        this.executor = executor;
    }

    void sign(File apkPath) {
        try {
            List<String> commands = isJks(new File(keyStoreAlias)) ? jks(apkPath) : pkcs12(apkPath);
            check(executor.execute(commands));
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    void verify(File apkPath) {
        try {
            List<String> commands = Arrays.asList(jarSignerPath, "-verify", apkPath.getAbsolutePath());
            executor.execute(commands);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private void check(String output) {
        if (Strings.isEmpty(output))
            return;

        if (output.contains("error") || output.contains("unsigned")) {
            throw new RuntimeException(output);
        }
    }

    private List<String> jks(File apkPath) {
        ArrayList<String> command = new ArrayList<String>(8);
        command.add(jarSignerPath);
        command.add("-keystore");
        command.add(keyStorePath);
        command.add("-storepass");
        command.add(storePassword);
        if (! Strings.isEmpty(keyPassword)) {
            command.add("-keypass");
            command.add(keyPassword);
        }
        command.add("-digestalg");
        command.add("SHA1");
        command.add("-sigalg");
        command.add("MD5withRSA");
        command.add(apkPath.getAbsolutePath());
        command.add(keyStoreAlias);
        return command;
    }

    private List<String> pkcs12(File apkPath) {
        ArrayList<String> command = new ArrayList<String>(8);
        command.add(jarSignerPath);
        command.add("-keystore");
        command.add(keyStorePath);
        command.add("-storepass");
        command.add(storePassword);
        command.add("-storetype");
        command.add("pkcs12");
        command.add(apkPath.getAbsolutePath());
        command.add(keyStoreAlias);

        return command;
    }

    private static boolean isJks(File f) throws Exception {
        KeyStore ks = null;
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(f);
            ks = KeyStore.getInstance("jks");
            ks.load(fis, null);
            return true;
        } catch (IOException e) {
            IOUtils.closeQuietly(fis);
            fis = new FileInputStream(f);
            ks = KeyStore.getInstance("pkcs12");
            ks.load(fis, null);
            return false;
        } finally {
            IOUtils.closeQuietly(fis);
        }
    }
}
