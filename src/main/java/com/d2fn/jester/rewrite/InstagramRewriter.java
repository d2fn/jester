package com.d2fn.jester.rewrite;

import org.apache.http.client.HttpClient;

import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

public class InstagramRewriter extends AbstractImageRewriter {
    public static Pattern LINK_PATTERN = compile("(http://)?(www\\.)?instagr.?am[^\\s]+");
    public static Pattern EMBEDDED_LINK_PATTERN = compile("img class=\"photo\".*(http://[^\"]+)", Pattern.MULTILINE);

    public InstagramRewriter(HttpClient httpClient) {
        super(LINK_PATTERN, EMBEDDED_LINK_PATTERN, httpClient);
    }
}
