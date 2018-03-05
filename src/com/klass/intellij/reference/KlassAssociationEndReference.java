package com.klass.intellij.reference;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.klass.intellij.KlassUtil;
import com.klass.intellij.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class KlassAssociationEndReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference
{
    private final String associationEndName;

    public KlassAssociationEndReference(@NotNull PsiElement element, String associationEndName)
    {
        super(element, new TextRange(0, associationEndName.length()));
        this.associationEndName = associationEndName;
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode)
    {
        PsiElement innerNode = this.myElement.getParent();
        KlassTypedElement klassTypedElement = (KlassTypedElement) innerNode.getParent();
        PsiElement type = klassTypedElement.getType();
        PsiReference reference = type.getReference();
        KlassKlass klassKlass = (KlassKlass) reference.resolve();
        String typeName = klassKlass.getName();

        List<KlassAssociation> associations = KlassUtil.findAssociations(klassKlass.getProject());
        for (KlassAssociation association : associations)
        {
            List<KlassAssociationEnd> associationEndList = association.getAssociationEndList();
            KlassAssociationEnd sourceEnd = associationEndList.get(0);
            KlassAssociationEnd targetEnd = associationEndList.get(1);

            String sourceName = sourceEnd.getName();
            String targetName = targetEnd.getName();

            KlassKlassName sourceTypeName = sourceEnd.getKlassName();
            KlassKlassName targetTypeName = targetEnd.getKlassName();

            KlassKlass sourceKlass = (KlassKlass) sourceTypeName.getReference().resolve();
            KlassKlass targetKlass = (KlassKlass) targetTypeName.getReference().resolve();

            String sourceType = sourceKlass.getName();
            String targetType = targetKlass.getName();

            System.out.println("sourceName = " + sourceName);
            System.out.println("targetName = " + targetName);
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
