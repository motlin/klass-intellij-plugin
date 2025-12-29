package com.klass.intellij.braces;

import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.klass.intellij.psi.KlassTypes;
import org.jetbrains.annotations.NotNull;

public class KlassBraceMatcher implements PairedBraceMatcher {
  private static final BracePair[] PAIRS = {
    new BracePair(KlassTypes.LBRACE, KlassTypes.RBRACE, true),
    new BracePair(KlassTypes.LBRACKET, KlassTypes.RBRACKET, false),
    new BracePair(KlassTypes.LPAREN, KlassTypes.RPAREN, false),
  };

  @NotNull @Override
  public BracePair[] getPairs() {
    return PAIRS;
  }

  @Override
  public boolean isPairedBracesAllowedBeforeType(
      @NotNull IElementType lbraceType, IElementType contextType) {
    return true;
  }

  @Override
  public int getCodeConstructStart(PsiFile file, int openingBraceOffset) {
    return openingBraceOffset;
  }
}
