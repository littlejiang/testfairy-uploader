package com.testfairy.uploader.cli;

import com.testfairy.uploader.*;
import com.testfairy.uploader.cli.options.*;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    private static final int EXIT_SUCCESS = 0;
    private static final int EXIT_NO_API_KEY = -1;
    private static final int EXIT_INVALID_API_KEY = -2;
    private static final int EXIT_NO_ARTIFACT_FILE = -3;
    private static final int EXIT_INVALID_ARTIFACT_FILE = -4;
    private static final int EXIT_INVALID_ARTIFACT_FILE_TYPE = -5;
    private static final int EXIT_UNEXPECTED_EXCEPTION = -5;

    private final List<OptionsArg> OPTION_ARGS = Arrays.asList(
        new AnonymousOption(),
        new AutoUpdateOption(),
        new CommentOption(),
        new FramesPerSecondOption(),
        new MaxDurationOption(),
        new MetricsOption(),
        new NotifyOption(),
        new TestersOption(),
        new VideoRecordingOption(),
        new VideoQualityOption(),
        new WatermarkOption()
    );

    public static void main(String[] args) {
        System.exit(new Main().execute(args));
    }

    int execute(String[] args) {
        try {
            OptionParser parser = new OptionParser();
            for (OptionsArg optionsArg : OPTION_ARGS) {
                optionsArg.configure(parser);
            }

            OptionSpec<File> mappingArg = parser.acceptsAll(Arrays.asList("proguard-mapping", "symbols-file")).withRequiredArg().ofType(File.class);
            OptionSpec<String> apiKeyArg = parser.accepts("api-key", "Your API application key. See https://app.testfairy.com/settings for details").withRequiredArg();
            OptionSpec<File> inputArg = parser.nonOptions("APK or IPA file data").ofType(File.class);
            OptionSpec<Void> help = parser.acceptsAll(Arrays.asList("h", "?", "help"), "Show TestFairy uploader usage").forHelp();

            OptionSet arguments = parser.parse(args);

            if (arguments.has(help)) {
                parser.printHelpOn(System.out);
                return EXIT_SUCCESS;
            }

            Options.Builder options = null;
            for (OptionsArg optionsArg : OPTION_ARGS) {
                options = optionsArg.apply(arguments, options);
            }

            if (!arguments.has(apiKeyArg)) {
                System.err.println("Argument --api-key is required");
                parser.printHelpOn(System.err);
                return EXIT_NO_API_KEY;
            }

            String api = arguments.valueOf(apiKeyArg);
            if (StringUtils.isEmpty(api)) {
                System.err.println("Argument --api-key cannot be empty");
                parser.printHelpOn(System.err);
                return EXIT_INVALID_API_KEY;
            }

            if (!arguments.has(inputArg)) {
                System.err.println("Input APK or IPA file required");
                parser.printHelpOn(System.err);
                return EXIT_NO_ARTIFACT_FILE;
            }

            File input = arguments.valueOf(inputArg);
            if (input == null) {
                System.err.println("Input APK or IPA file required");
                parser.printHelpOn(System.err);
                return EXIT_NO_ARTIFACT_FILE;
            }

            if (!input.exists()) {
                System.err.println("No APK or IPA file found at " + input.getPath());
                parser.printHelpOn(System.err);
                return EXIT_INVALID_ARTIFACT_FILE;
            }

            File mapping = null;
            if (arguments.has(mappingArg)) mapping = arguments.valueOf(mappingArg);

            List<String> files = fileList(input);

            Uploader uploader = null;
            if (AppUtils.isAndroidAPK(files)) {
                uploader = new AndroidUploader.Builder(api)
                    .setApkPath(input.getPath())
                    .setProguardMapPath(mapping == null ? null : mapping.getPath())
                    .setOptions(options == null ? null : options.build())
                    .build();
            } else if (AppUtils.isAppleIPA(files)) {
                uploader = new IOSUploader.Builder(api)
                    .setIpaPath(input.getPath())
                    .setSymbolsPath(mapping == null ? null : mapping.getPath())
                    .setOptions(options == null ? null : options.build())
                    .build();
            }

            if (uploader == null) {
                System.err.println(String.format("File found at [%s] is neither an Android or iOS archive", input.getAbsolutePath()));
                return EXIT_INVALID_ARTIFACT_FILE_TYPE;
            }

            final int [] result = {EXIT_SUCCESS};
            uploader.upload(new Listener() {
                @Override
                public void onUploadStarted() {

                }

                @Override
                public void onUploadComplete(Build build) {
                    System.out.println("Upload successful: " + build.buildUrl());
                }

                @Override
                public void onUploadFailed(Throwable throwable) {
                    System.err.println("Upload failed");
                    System.err.println(throwable.getMessage());
                    result[0] = EXIT_UNEXPECTED_EXCEPTION;
                }

                @Override
                public void onProgress(float p) {

                }
            });

            return result[0];
        } catch (Exception exception) {
            System.err.println("Unexpected Failure");
            System.err.println(exception.getMessage());
            return EXIT_UNEXPECTED_EXCEPTION;
        }
    }

    private static List<String> fileList(File file) throws Exception {
        ArrayList<String> files = new ArrayList<String>();
        ZipArchiveInputStream zais = null;
        try {
            zais = new ZipArchiveInputStream(new FileInputStream(file));
            while (true) {
                ZipArchiveEntry entry = zais.getNextZipEntry();
                if (entry == null) {
                    break;
                }

                files.add(entry.getName());
            }

            return files;
        } catch (Exception exception) {
            throw exception;
        } finally {
            IOUtils.closeQuietly(zais);
        }
    }
}
