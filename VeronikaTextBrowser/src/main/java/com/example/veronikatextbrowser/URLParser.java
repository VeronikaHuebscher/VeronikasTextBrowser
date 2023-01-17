package com.example.veronikatextbrowser;

public class URLParser {

    private static final String HTTP_PREFIX = "http://";
    private static final String HTTPS_PREFIX = "https://";

    public static String parseAddress(String url) {
        if (url.startsWith(HTTP_PREFIX)) {
            url = url.substring(HTTP_PREFIX.length());
        } else if (url.startsWith(HTTPS_PREFIX)) {
            url = url.substring(HTTPS_PREFIX.length());
        }

        int index = url.indexOf("/");
        if (index == -1) {
            return url;
        } else {
            String address = url.substring(0, index);
            return address;

        }


    }

    public static String parsePath(String url) {
        if (url.startsWith(HTTP_PREFIX)) {
            url = url.substring(HTTP_PREFIX.length());
        } else if (url.startsWith(HTTPS_PREFIX)) {
            url = url.substring(HTTPS_PREFIX.length());
        }

        int index = url.indexOf("/");
        if (index == -1) {
            return "/";
        } else {
            String path = url.substring(index);
            return path;
        }


    }


}
