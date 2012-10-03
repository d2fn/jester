package com.d2fn.jester.rewrite;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

public class SentenceRewriter implements Rewriter {
    private final Rewriter delegate;

    public SentenceRewriter(Rewriter delegate) {
        this.delegate = checkNotNull(delegate);
    }

    @Override
    public Optional<String> rewrite(String input) throws Exception {
        final String[] pieces = input.split(" ");
        for (int idx=0; idx < pieces.length; idx ++) {
            final Optional<String> optional = delegate.rewrite(pieces[idx]);
            if (optional.isPresent()) {
                pieces[idx] = optional.get();
            }
        }
        return Optional.of(Joiner.on(" ").join(pieces));
    }
}
