package com.klass.intellij.reference;

import java.util.ArrayList;
import java.util.List;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.icons.AllIcons;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiPolyVariantReferenceBase;
import com.intellij.psi.PsiReference;
import com.intellij.psi.ResolveResult;
import com.klass.intellij.KlassUtil;
import com.klass.intellij.psi.KlassElementFactory;
import com.klass.intellij.psi.KlassKlass;
import com.klass.intellij.psi.KlassParameterizedProperty;
import com.klass.intellij.psi.KlassParameterizedPropertyName;
import com.klass.intellij.psi.KlassTypedElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class KlassParameterizedPropertyReference extends PsiPolyVariantReferenceBase<PsiElement>
{
    private final String parameterizedPropertyName;

    public KlassParameterizedPropertyReference(@NotNull PsiElement element, String parameterizedPropertyName)
    {
        super(element, new TextRange(0, parameterizedPropertyName.length()));
        this.parameterizedPropertyName = parameterizedPropertyName;
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode)
    {
        PsiElement innerNode = this.myElement.getParent();
        KlassTypedElement klassTypedElement = (KlassTypedElement) innerNode.getParent().getParent().getParent();
        PsiElement type = klassTypedElement.getType();
        PsiReference reference = type.getReference();
        KlassKlass klassKlass = (KlassKlass) reference.resolve();

        if (klassKlass == null)
        {
            return new ResolveResult[]{};
        }

        ResolveResult[] resolveResults = klassKlass
                .getClassBlock()
                .getClassBody()
                .getMemberList()
                .stream()
                .filter(KlassParameterizedProperty.class::isInstance)
                .map(KlassParameterizedProperty.class::cast)
                .filter(klassParameterizedProperty -> klassParameterizedProperty.getName().equals(this.parameterizedPropertyName))
                .map(PsiElementResolveResult::new)
                .toArray(ResolveResult[]::new);
        return resolveResults;
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

    @Override
    public PsiElement handleElementRename(String newElementName)
    {
        ASTNode node = this.myElement.getNode();
        if (node != null)
        {
            KlassParameterizedPropertyName parameterizedPropertyName = KlassElementFactory.createParameterizedPropertyName(
                    this.myElement.getProject(),
                    newElementName);

            ASTNode newNode = parameterizedPropertyName.getNode();
            node.getTreeParent().replaceChild(node, newNode);
        }
        return this.myElement;
    }
}
