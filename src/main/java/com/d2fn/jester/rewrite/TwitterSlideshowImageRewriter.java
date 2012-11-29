package com.d2fn.jester.rewrite;

import org.apache.http.client.HttpClient;

import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

public class TwitterSlideshowImageRewriter extends AbstractImageRewriter {
    public static Pattern LINK_PATTERN = compile("(https?://)?(www\\.)?twitter\\.com.*photo?[^\\s]+");
    public static Pattern EMBEDDED_LINK_PATTERN = compile("img.*media-slideshow-image.*src=\"(https?://[^\"]+):.*\".*", Pattern.MULTILINE);

    public TwitterSlideshowImageRewriter(HttpClient httpClient) {
        super(LINK_PATTERN, EMBEDDED_LINK_PATTERN, httpClient);
    }

}
