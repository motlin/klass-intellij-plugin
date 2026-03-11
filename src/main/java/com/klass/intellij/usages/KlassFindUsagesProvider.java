package com.klass.intellij.usages;

import com.intellij.lang.cacheBuilder.DefaultWordsScanner;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.tree.TokenSet;
import com.klass.intellij.lexer.KlassLexerAdapter;
import com.klass.intellij.psi.KlassAssociation;
import com.klass.intellij.psi.KlassAssociationEnd;
import com.klass.intellij.psi.KlassEnumeration;
import com.klass.intellij.psi.KlassEnumerationLiteral;
import com.klass.intellij.psi.KlassInterface;
import com.klass.intellij.psi.KlassKlass;
import com.klass.intellij.psi.KlassMember;
import com.klass.intellij.psi.KlassNamedElement;
import com.klass.intellij.psi.KlassParameterDeclaration;
import com.klass.intellij.psi.KlassProjection;
import com.klass.intellij.psi.KlassTokenType;
import com.klass.intellij.psi.KlassTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class KlassFindUsagesProvider implements FindUsagesProvider {
  @Nullable @Override
  public WordsScanner getWordsScanner() {
    return new DefaultWordsScanner(
        new KlassLexerAdapter(),
        TokenSet.create(KlassTypes.IDENTIFIER),
        TokenSet.create(KlassTokenType.C_STYLE_COMMENT, KlassTokenType.END_OF_LINE_COMMENT),
        TokenSet.create(KlassTypes.STRING_LITERAL));
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
    if (element instanceof KlassInterface) {
      return "interface";
    }
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
    if (element instanceof KlassProjection) {
      return "projection";
    }
    if (element instanceof KlassMember) {
      return "property";
    }
    if (element instanceof KlassEnumerationLiteral) {
      return "enumeration literal";
    }
    if (element instanceof KlassParameterDeclaration) {
      return "parameter";
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
