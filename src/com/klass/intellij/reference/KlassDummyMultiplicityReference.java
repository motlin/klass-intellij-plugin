package com.klass.intellij.reference;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.ResolveResult;
import com.klass.intellij.psi.KlassDummyMultiplicity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class KlassDummyMultiplicityReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference
{
    public KlassDummyMultiplicityReference(KlassDummyMultiplicity klassDummyMultiplicity)
    {
        super(klassDummyMultiplicity);
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
        return new Object[]{"0..1", "1..1", "0..*", "1..*"};
    }
}
