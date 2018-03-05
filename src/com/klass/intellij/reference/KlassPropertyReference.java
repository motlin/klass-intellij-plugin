package com.klass.intellij.reference;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.klass.intellij.KlassUtil;
import com.klass.intellij.psi.KlassKlass;
import com.klass.intellij.psi.KlassKlassName;
import com.klass.intellij.psi.KlassProjection;
import com.klass.intellij.psi.KlassProjectionInnerNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class KlassPropertyReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference
{
    private final String propertyName;

    public KlassPropertyReference(@NotNull PsiElement element, String propertyName)
    {
        super(element, new TextRange(0, propertyName.length()));
        this.propertyName = propertyName;
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode)
    {
        PsiElement parent = this.myElement.getParent();
        PsiElement grandparent = parent.getParent();
        String parentText = parent.getText();
        String grandparentText = grandparent.getText();
        if (grandparent instanceof KlassProjectionInnerNode)
        {
            PsiReference associationEndReference = grandparent.getReference();

            PsiElement psiElement = ((KlassProjectionInnerNode) grandparent).getAssociationEndName();
            // PsiReference klassReference = (PsiReference) psiElement;

        }
        else if (grandparent instanceof KlassProjection)
        {
            KlassKlassName klassName = ((KlassProjection) grandparent).getKlassName();
            PsiReference klassReference = klassName.getReference();

            KlassKlass klassKlass = (KlassKlass) klassReference.resolve();
            return klassKlass.getPropertyList()
                    .stream()
                    .filter(klassProperty -> klassProperty.getName().equals(this.propertyName))
                    .map(PsiElementResolveResult::new)
                    .toArray(ResolveResult[]::new);
        }

        return new ResolveResult[]{};
    }

    @Nullable
    @Override
    public PsiElement resolve()
    {
        ResolveResult[] resolveResults = this.multiResolve(false);
        return resolveResults.length == 1 ? resolveResults[0].getElement() : null;
    }

    @NotNull
    @Override
    public Object[] getVariants()
    {
        Project project = this.myElement.getProject();
        List<KlassKlass> klassKlasses = KlassUtil.findClasses(project);
        List<LookupElement> variants = new ArrayList<>();
        for (KlassKlass klassKlass : klassKlasses)
        {
            if (klassKlass.getName() != null && !klassKlass.getName().isEmpty())
            {
                LookupElementBuilder lookupElementBuilder = LookupElementBuilder.create(klassKlass.getName())
                        .withIcon(AllIcons.Nodes.Class)
                        .withTypeText(klassKlass.getContainingFile().getName());
                variants.add(lookupElementBuilder);
            }
        }
        return variants.toArray();
    }
}
