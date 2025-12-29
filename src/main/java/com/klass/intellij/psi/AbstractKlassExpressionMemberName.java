package com.klass.intellij.psi;

import com.intellij.psi.PsiElement;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public interface AbstractKlassExpressionMemberName extends PsiElement {
  @NotNull List<KlassAssociationEndName> getAssociationEndNameList();

  @NotNull KlassMemberName getMemberName();
}
