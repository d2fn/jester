package com.d2fn.jester.plugin.image;

import com.d2fn.jester.rewrite.CloudappRewriter;
import com.d2fn.jester.rewrite.InstagramRewriter;
import com.d2fn.jester.rewrite.TwitterImageRewriter;
import org.junit.Test;

import java.util.regex.Matcher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RegexTest {

    @Test
    public void testCloudappLinkFinder() throws Exception {
        final String text = "Here is a link http://cl.ly/image/151G1S12290Z ok.";
        final Matcher matcher = CloudappRewriter.LINK_PATTERN.matcher(text);
        assertTrue(matcher.find());
        assertEquals("http://cl.ly/image/151G1S12290Z", matcher.group());
    }

    @Test
    public void testInstagramLinkFinder() throws Exception {
        final String text = "Here is a link http://instagram.com/p/QLwqJxLT4f/ ok.";
        final Matcher matcher = InstagramRewriter.LINK_PATTERN.matcher(text);
        assertTrue(matcher.find());
        assertEquals("http://instagram.com/p/QLwqJxLT4f/", matcher.group());
    }

    @Test
    public void testTwitterImageLinkFinder() throws Exception {
        final String text = "Here is a link http://twitter.com/williamsjoe/status/258695575673909249/photo/1 ok.";
        final Matcher matcher = TwitterImageRewriter.LINK_PATTERN.matcher(text);
        assertTrue(matcher.find());
        assertEquals("http://twitter.com/williamsjoe/status/258695575673909249/photo/1", matcher.group());

    }
}
