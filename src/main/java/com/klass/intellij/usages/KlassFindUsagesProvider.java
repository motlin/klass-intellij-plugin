package com.klass.intellij.usages;

import com.intellij.lang.cacheBuilder.DefaultWordsScanner;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.tree.TokenSet;
import com.klass.intellij.lexer.KlassLexerAdapter;
import com.klass.intellij.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class KlassFindUsagesProvider implements FindUsagesProvider {
  @Nullable @Override
  public WordsScanner getWordsScanner() {
    return new DefaultWordsScanner(
        new KlassLexerAdapter(),
        TokenSet.create(KlassTypes.NOMBRE),
        TokenSet.create(KlassTokenType.C_STYLE_COMMENT, KlassTokenType.END_OF_LINE_COMMENT),
        TokenSet.EMPTY);
  }

  @Override
  public boolean canFindUsagesFor(@NotNull PsiElement psiElement) {
    return psiElement instanceof PsiNamedElement;
  }

  @Nullable @Override
  public String getHelpId(@NotNull PsiElement psiElement) {
    return null;
  }

  @NotNull @Override
  public String getType(@NotNull PsiElement element) {
    if (element instanceof KlassKlass) {
      return "class";
    }
    if (element instanceof KlassAssociation) {
      return "association";
    }
    if (element instanceof KlassAssociationEnd) {
      return "association end";
    }
    if (element instanceof KlassEnumeration) {
      return "enumeration";
    }
    return "";
  }

  @NotNull @Override
  public String getDescriptiveName(@NotNull PsiElement element) {
    if (element instanceof KlassNamedElement) {
      return ((KlassNamedElement) element).getName();
    }
    return "";
  }

  @NotNull @Override
  public String getNodeText(@NotNull PsiElement element, boolean useFullName) {
    // TODO: Better implementation
    return this.getDescriptiveName(element);
  }
}
