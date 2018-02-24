package com.klass.intellij;

import com.intellij.lexer.FlexAdapter;

public class KlassLexerAdapter extends FlexAdapter
{
    public KlassLexerAdapter()
    {
        super(new KlassLexer(null));
    }
}
