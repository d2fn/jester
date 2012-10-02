package com.d2fn.jester.plugin.image;

import org.junit.Assert;
import org.junit.Test;

import java.util.regex.Matcher;

import static com.d2fn.jester.plugin.image.ImagePlugin.CLOUDAPP_LINK;
import static com.d2fn.jester.plugin.image.ImagePlugin.INSTAGRAM_LINK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RegexTest {

    @Test
    public void testCloudappLinkFinder() throws Exception {
        final String text = "Here is a link http://cl.ly/image/151G1S12290Z ok.";
        final Matcher matcher = CLOUDAPP_LINK.matcher(text);
        assertTrue(matcher.find());
        assertEquals("http://cl.ly/image/151G1S12290Z", matcher.group());
    }

    @Test
    public void testInstagramLinkFinder() throws Exception {
        final String text = "Here is a link http://instagram.com/p/QLwqJxLT4f/ ok.";
        final Matcher matcher = INSTAGRAM_LINK.matcher(text);
        assertTrue(matcher.find());
        assertEquals("http://instagram.com/p/QLwqJxLT4f/", matcher.group());
    }
}
