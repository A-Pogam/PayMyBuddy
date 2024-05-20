package org.PayMyBuddy.constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UriToIgnore {

    public static final String errorURI = "/error";
    public static final String faviconURI = "/favicon.ico";
    public static final List<String> uriToIgnore = new ArrayList<>(Arrays.asList(
            errorURI,
            faviconURI
    ));
}