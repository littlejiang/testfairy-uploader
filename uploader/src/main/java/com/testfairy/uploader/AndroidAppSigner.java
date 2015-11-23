package com.testfairy.uploader;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;

class AndroidAppSigner {
    private final String apiKey;
    private final String apkPath;
    private final JarSigner jarSigner;
    private final ZipAligner aligner;
    private final String tempDirectory;
    private final TestFairyService service;

    AndroidAppSigner(
        String apiKey,
        String apkPath,
        TestFairyService service,
        JarSigner jarSigner,
        ZipAligner aligner
    ) {
        this.service = service;
        this.apiKey = apiKey;
        this.apkPath = apkPath;
        this.jarSigner = jarSigner;
        this.aligner = aligner;
        this.tempDirectory = System.getProperty("java.io.tmpdir");
    }

    File resign(Build build) {
        String baseName = FilenameUtils.getBaseName(apkPath);

        File download = service.download(
            String.format("%s?api_key=%s", build.instrumentedUrl(), apiKey),
            FilenameUtils.normalize(String.format("%s/testfairy-%s.apk", tempDirectory, baseName))
        );

        File temporaryPath = new File(String.format("%s/testfairy-%s.temp.apk", tempDirectory, baseName));
        removeSignature(download.getAbsolutePath(), temporaryPath.getAbsolutePath());
        jarSigner.sign(temporaryPath);
        aligner.align(temporaryPath, download.getAbsolutePath());
        jarSigner.verify(download);
        temporaryPath.delete();

        return download;
    }

    void removeSignature(String apkFilename, String outFilename) {
        ZipArchiveInputStream zais = null;
        ZipArchiveOutputStream zaos = null;

        try {
            zais = new ZipArchiveInputStream(new FileInputStream(apkFilename));
            zaos = new ZipArchiveOutputStream(new FileOutputStream(outFilename));
            while (true) {
                ZipArchiveEntry entry = zais.getNextZipEntry();
                if (entry == null) {
                    break;
                }

                // skip META-INF files
                if (entry.getName().startsWith("META-INF/")) {
                    continue;
                }

                ZipArchiveEntry zipEntry = new ZipArchiveEntry(entry.getName());
                if (entry.getMethod() == ZipEntry.STORED) {
                    // when storing files, we need to copy the size and crc ourselves
                    zipEntry.setSize(entry.getSize());
                    zipEntry.setCrc(entry.getCrc());
                }

                zaos.setMethod(entry.getMethod());
                zaos.putArchiveEntry(zipEntry);
                IOUtils.copy(zais, zaos);
                zaos.closeArchiveEntry();
            }
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        } finally {
            IOUtils.closeQuietly(zais);
            IOUtils.closeQuietly(zaos);
        }
    }

    @Override
    public String toString() {
        return "AndroidAppSigner{" +
            "apiKey='" + apiKey + '\'' +
            ", apkPath='" + apkPath + '\'' +
            ", jarSigner=" + jarSigner +
            ", aligner=" + aligner +
            ", tempDirectory='" + tempDirectory + '\'' +
            ", service=" + service +
            '}';
    }
}
