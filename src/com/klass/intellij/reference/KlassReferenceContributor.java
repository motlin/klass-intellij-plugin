package com.klass.intellij.reference;

import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.*;
import com.intellij.util.ProcessingContext;
import com.klass.intellij.psi.KlassAssociationEndType;
import org.jetbrains.annotations.NotNull;

public class KlassReferenceContributor extends PsiReferenceContributor
{
    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar)
    {
        registrar.registerReferenceProvider(PlatformPatterns.psiElement(KlassAssociationEndType.class),
                new PsiReferenceProvider()
                {
                    @NotNull
                    @Override
                    public PsiReference[] getReferencesByElement(
                            @NotNull PsiElement element,
                            @NotNull ProcessingContext context)
                    {
                        KlassAssociationEndType klassAssociationEndType = (KlassAssociationEndType) element;

                        String className = klassAssociationEndType.getText();
                        if (className == null)
                        {
                            return PsiReference.EMPTY_ARRAY;
                        }

                        return new PsiReference[]{new KlassAssociationEndTypeReference(element, className)};
                    }
                });
    }
}
