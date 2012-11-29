package com.d2fn.jester.rewrite;

import junit.framework.TestCase;
import org.junit.Assert;

import java.util.regex.Matcher;

public class TwitterSlideshowImageRewriterTest extends TestCase {

    public void testLinkPattern() throws Exception {
        String url = "http://twitter.com/IEatInTheShower/status/274253008802553856/photo/1";
        final Matcher matcher = TwitterSlideshowImageRewriter.LINK_PATTERN.matcher(url);
        Assert.assertTrue(matcher.matches());
    }

    public void testFindsTheEmbeddedImage() throws Exception {
        String content = "<img class=\"large media-slideshow-image\" alt=\"\" src=\"https://pbs.twimg.com/media/A85XqJCCIAAZkxA.jpg:large\" height=\"1198\" width=\"957\"";
        final Matcher matcher = TwitterSlideshowImageRewriter.EMBEDDED_LINK_PATTERN.matcher(content);
        Assert.assertTrue(matcher.find());
        Assert.assertEquals("https://pbs.twimg.com/media/A85XqJCCIAAZkxA.jpg", matcher.group(1));
    }
}
