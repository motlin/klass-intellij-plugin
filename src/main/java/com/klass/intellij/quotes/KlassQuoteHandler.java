package com.klass.intellij.quotes;

import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler;
import com.intellij.psi.tree.TokenSet;
import com.klass.intellij.psi.KlassTypes;

public class KlassQuoteHandler extends SimpleTokenSetQuoteHandler {
  public KlassQuoteHandler() {
    super(TokenSet.create(KlassTypes.STRING_LITERAL));
  }
}
