package com.klass.intellij.braces;

import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.klass.intellij.psi.KlassTypes;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class KlassBraceMatcher implements PairedBraceMatcher
{
    private static final BracePair[] PAIRS = {
            new BracePair(KlassTypes.LBRACE, KlassTypes.RBRACE, true),
            new BracePair(KlassTypes.LBRACKET, KlassTypes.RBRACKET, false),
    };

    @NotNull
    @Override
    public BracePair[] getPairs()
    {
        return PAIRS;
    }

    @Override
    public boolean isPairedBracesAllowedBeforeType(@NotNull IElementType lbraceType, @Nullable IElementType contextType)
    {
        return true;
    }

    @Override
    public int getCodeConstructStart(PsiFile file, int openingBraceOffset)
    {
        return openingBraceOffset;
    }
}
