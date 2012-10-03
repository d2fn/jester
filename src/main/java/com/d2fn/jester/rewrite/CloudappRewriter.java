package com.d2fn.jester.rewrite;

import org.apache.http.client.HttpClient;

import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

public class CloudappRewriter extends AbstractImageRewriter {
    public static final Pattern LINK_PATTERN = compile("(http://)?(www\\.)?cl\\.ly[^\\s]+");
    public static final Pattern EMBEDDED_LINK_PATTERN = compile("a class=\"embed\".*(http://cl\\.ly[^\"]+)", Pattern.MULTILINE);

    public CloudappRewriter(HttpClient httpClient) {
        super(LINK_PATTERN, EMBEDDED_LINK_PATTERN, httpClient);
    }
}
