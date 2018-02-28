package com.klass.intellij.reference;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.klass.intellij.KlassUtil;
import com.klass.intellij.psi.KlassEnumeration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class KlassEnumerationReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference
{
    private final String enumerationName;

    public KlassEnumerationReference(@NotNull PsiElement element, String enumerationName)
    {
        super(element, new TextRange(0, enumerationName.length()));
        this.enumerationName = enumerationName;
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode)
    {
        Project project = this.myElement.getProject();
        return KlassUtil.findEnumerations(project)
                .stream()
                .filter(klassEnumeration -> klassEnumeration.getName().equals(this.enumerationName))
                .map(PsiElementResolveResult::new)
                .toArray(ResolveResult[]::new);
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
        List<KlassEnumeration> klassEnumerations = KlassUtil.findEnumerations(project);
        List<LookupElement> variants = new ArrayList<>();

        for (KlassEnumeration klassEnumeration : klassEnumerations)
        {
            if (klassEnumeration.getName() != null && !klassEnumeration.getName().isEmpty())
            {
                LookupElementBuilder lookupElementBuilder = LookupElementBuilder.create(klassEnumeration.getName())
                        .withIcon(AllIcons.Nodes.Enum)
                        .withTypeText(klassEnumeration.getContainingFile().getName());
                variants.add(lookupElementBuilder);
            }
        }
        return variants.toArray();
    }
}
