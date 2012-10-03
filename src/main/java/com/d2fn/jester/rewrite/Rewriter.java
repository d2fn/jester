package com.d2fn.jester.rewrite;

import com.google.common.base.Optional;

public interface Rewriter {

    /**
     * Rewrites a particular string. If it could not be rewritten, returns {@link Optional.Absent()}
     * @param input the string to rewrite
     * @return a set Option if it was rewritten, otherwise absent
     * @throws Exception if something explodes
     */
    Optional<String> rewrite(String input) throws Exception;

}
