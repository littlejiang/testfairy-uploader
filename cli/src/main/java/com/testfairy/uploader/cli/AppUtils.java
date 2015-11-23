package com.testfairy.uploader.cli;

import java.io.IOException;
import java.util.List;

class AppUtils {
    /**
     * Checks if the file is an Android APK. Done by checking if AndroidManifest.xml
     * and classes.dex are found in the zip file.
     *
     * @return boolean
     */
    public static boolean isAndroidAPK(List<String> files) throws IOException {
        return (files.contains("AndroidManifest.xml") && files.contains("classes.dex"));
    }

    /**
     * Checks if the file is an iOS IPA file. Done by checking if Payload directory
     * contains Payload/*.app/
     *
     * @param files
     * @return boolean
     */
    public static boolean isAppleIPA(List<String> files) throws IOException {

        for (String file: files) {
            if (file.startsWith("Payload/") && file.contains(".app/")) {
                return true;
            }
        }
        return false;
    }

    public static boolean isMacOSXApp(List<String> files) {

        for (String file: files) {
            if (file.startsWith("Applications/") && file.contains(".app/") && file.endsWith("Info.plist")) {
                return true;
            }
        }
        return false;
    }
}