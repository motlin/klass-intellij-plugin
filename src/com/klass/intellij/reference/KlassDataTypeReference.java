package com.klass.intellij.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.ResolveResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class KlassDataTypeReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference
{
    public static final String[] VARIANTS = {
            "Boolean",
            "Integer",
            "Long",
            "Double",
            "Float",
            "String",
            "Instant",
            "LocalDate",
    };

    private final String substring;

    public KlassDataTypeReference(@NotNull PsiElement element, String substring)
    {
        super(element, new TextRange(0, substring.length()));
        this.substring = substring;
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode)
    {
        return new ResolveResult[]{};
    }

    @Nullable
    @Override
    public PsiElement resolve()
    {
        return null;
    }

    @NotNull
    @Override
    public Object[] getVariants()
    {
        return VARIANTS;
    }
}
