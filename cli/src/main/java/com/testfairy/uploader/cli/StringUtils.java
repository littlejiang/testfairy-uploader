package com.testfairy.uploader.cli;

class StringUtils {
    public static boolean isEmpty(String value) {
        return value == null || value.trim().length() == 0;
    }
}
