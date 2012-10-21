package com.d2fn.jester.rewrite;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkRewriter implements Rewriter {
    private static final List<Pattern> PATTERNS = ImmutableList.<Pattern>builder()
            .add(Pattern.compile("(https?://)?(www\\.)?t\\.co/[^\\s]+"))
            .add(Pattern.compile("(https?://)?(www\\.)?bit\\.ly/[^\\s]+"))
            .add(Pattern.compile("(https?://)?(www\\.)?j\\.mp/[^\\s]+"))
            .add(Pattern.compile("(https?://)?(www\\.)?tcrn\\.ch/[^\\s]+"))
            .build();
    private final RedirectFinder redirectFinder;

    public LinkRewriter(RedirectFinder redirectFinder) {
        this.redirectFinder = redirectFinder;
    }

    @Override
    public Optional<String> rewrite(String input) throws Exception {
        for (Pattern pattern : PATTERNS) {
            final Matcher matcher = pattern.matcher(input);
            if (matcher.matches()) {
                final String link = redirectFinder.findRedirect(matcher.group());
                return Optional.fromNullable(link);
            }
        }
        return Optional.absent();
    }


}
