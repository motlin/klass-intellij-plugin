package com.klass.intellij.psi;

import java.util.List;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public interface AbstractKlassExpressionMemberName extends PsiElement
{
    @NotNull
    List<KlassAssociationEndName> getAssociationEndNameList();

    @NotNull
    KlassMemberName getMemberName();
}
