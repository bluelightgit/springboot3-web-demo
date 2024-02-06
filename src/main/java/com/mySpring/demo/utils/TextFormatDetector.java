package com.mySpring.demo.utils;

public class TextFormatDetector {

    public static boolean isIpAddress(String text) {
        return text.matches("^(\\d{1,3}\\.){3}\\d{1,3}$");
    }

    public static boolean isUrl(String text) {
        return text.matches("^(http|https)://.*$");
    }
}
