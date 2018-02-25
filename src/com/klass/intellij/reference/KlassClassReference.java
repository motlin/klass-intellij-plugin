package com.klass.intellij.reference;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.klass.intellij.KlassIcons;
import com.klass.intellij.KlassUtil;
import com.klass.intellij.psi.KlassClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class KlassClassReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference
{
    private final String className;

    public KlassClassReference(@NotNull PsiElement element, String className)
    {
        super(element, new TextRange(0, className.length() + 1));
        this.className = className;
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode)
    {
        Project project = this.myElement.getProject();
        return KlassUtil.findClasses(project)
                .stream()
                .map(KlassClass::getClassName)
                .filter(klassClassName -> klassClassName.getText().equals(this.className))
                .map(PsiElementResolveResult::new)
                .toArray(ResolveResult[]::new);
    }

    @Nullable
    @Override
    public PsiElement resolve()
    {
        ResolveResult[] resolveResults = multiResolve(false);
        return resolveResults.length == 1 ? resolveResults[0].getElement() : null;
    }

    @NotNull
    @Override
    public Object[] getVariants()
    {
        Project project = this.myElement.getProject();
        List<KlassClass> klassClasses = KlassUtil.findClasses(project);
        List<LookupElement> variants = new ArrayList<>();
        for (KlassClass klassClass : klassClasses)
        {
            if (klassClass.getName() != null && !klassClass.getName().isEmpty())
            {
                variants.add(LookupElementBuilder.create(klassClass.getClassName())
                        .withIcon(KlassIcons.FILE)
                        .withTypeText(klassClass.getContainingFile().getName())
                );
            }
        }
        return variants.toArray();
    }
}
