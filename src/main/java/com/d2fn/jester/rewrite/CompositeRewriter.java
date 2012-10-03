package com.d2fn.jester.rewrite;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A rewriter which delegates to other rewriters
 */
public class CompositeRewriter implements Rewriter {
    private final Collection<Rewriter> delegates;

    public CompositeRewriter(Collection<Rewriter> delegates) {
        this.delegates = checkNotNull(delegates);
    }

    @Override
    public Optional<String> rewrite(String input) throws Exception {
        final Set<String> known = Sets.newHashSet();
        String current = input;
        known.add(input);
        while (true) {
            boolean rewritten = false;
            for (Rewriter delegate : delegates) {
                final Optional<String> optional = delegate.rewrite(current);
                if (optional.isPresent()) {
                    if (known.contains(optional.get())) {
                        break;
                    }
                    current = optional.get();
                    known.add(current);
                    rewritten = true;
                    break;
                }
            }
            if (!rewritten) {
                break;
            }
        }
        if (current.equals(input)) {
            // there was no change.
            return Optional.absent();
        }
        return Optional.of(current);
    }
}
