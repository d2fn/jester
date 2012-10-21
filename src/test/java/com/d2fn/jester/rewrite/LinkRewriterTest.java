package com.d2fn.jester.rewrite;

import com.google.common.base.Optional;
import junit.framework.TestCase;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LinkRewriterTest extends TestCase {

    RedirectFinder redirectFinder;
    LinkRewriter rewriter;

    @Override
    public void setUp() throws Exception {
        redirectFinder = mock(RedirectFinder.class);
        rewriter = new LinkRewriter(redirectFinder);
    }

    public void testFindsTcoLinks() throws Exception {
        when(redirectFinder.findRedirect("http://t.co/CtsPkPgp")).thenReturn("foo");
        assertEquals(Optional.of("foo"), rewriter.rewrite("http://t.co/CtsPkPgp"));
    }

    public void testFindsHttpsTcoLinks() throws Exception {
        when(redirectFinder.findRedirect("https://t.co/CtsPkPgp")).thenReturn("foo");
        assertEquals(Optional.of("foo"), rewriter.rewrite("https://t.co/CtsPkPgp"));
    }

    public void testFindsTcoLinksWithoutProtocol() throws Exception {
        when(redirectFinder.findRedirect("t.co/CtsPkPgp")).thenReturn("foo");
        assertEquals(Optional.of("foo"), rewriter.rewrite("t.co/CtsPkPgp"));
    }

    public void testFindsBitlyLinks() throws Exception {
        when(redirectFinder.findRedirect("http://bit.ly/TKeX2Z")).thenReturn("foo");
        assertEquals(Optional.of("foo"), rewriter.rewrite("http://bit.ly/TKeX2Z"));
    }

    public void testFindsHttpsBitlyLinks() throws Exception {
        when(redirectFinder.findRedirect("https://bit.ly/TKeX2Z")).thenReturn("foo");
        assertEquals(Optional.of("foo"), rewriter.rewrite("https://bit.ly/TKeX2Z"));
    }

    public void testFindsBitlyLinksWithoutProtocol() throws Exception {
        when(redirectFinder.findRedirect("bit.ly/TKeX2Z")).thenReturn("foo");
        assertEquals(Optional.of("foo"), rewriter.rewrite("bit.ly/TKeX2Z"));
    }
}
