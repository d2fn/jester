package com.d2fn.jester.rewrite;

import org.apache.http.client.HttpClient;

import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

public class TwitterImageRewriter extends AbstractImageRewriter {
    public static Pattern LINK_PATTERN = compile("(https?://)?(www\\.)?twitter\\.com.*photo?[^\\s]+");
    //<img src="https://pbs.twimg.com/media/A5cSQWVCMAEbhqo.png" alt="Embedded image permalink"
    public static Pattern EMBEDDED_LINK_PATTERN = compile("img src=\"(https?://[^\"]+)\".*Embedded image", Pattern.MULTILINE);

    public TwitterImageRewriter(HttpClient httpClient) {
        super(LINK_PATTERN, EMBEDDED_LINK_PATTERN, httpClient);
    }

}
