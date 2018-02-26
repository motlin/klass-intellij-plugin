package com.klass.intellij.reference;

import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.completion.util.ParenthesesInsertHandler;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.icons.AllIcons;
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

public class KlassAssociationEndTypeReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference
{
    private final String className;

    public KlassAssociationEndTypeReference(@NotNull PsiElement element, String className)
    {
        super(element, new TextRange(0, className.length()));
        this.className = className;
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode)
    {
        Project project = this.myElement.getProject();
        return KlassUtil.findClasses(project)
                .stream()
                .filter(klassClass -> klassClass.getName().equals(this.className))
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
        BracketsInsertHandler insertHandler = new BracketsInsertHandler();
        for (KlassClass klassClass : klassClasses)
        {
            if (klassClass.getName() != null && !klassClass.getName().isEmpty())
            {
                LookupElementBuilder lookupElementBuilder = LookupElementBuilder.create(klassClass.getName())
                        .withIcon(AllIcons.Nodes.Class)
                        .withTypeText(klassClass.getContainingFile().getName())
                        .withInsertHandler(insertHandler);
                variants.add(lookupElementBuilder);
            }
        }
        return variants.toArray();
    }

    private static class BracketsInsertHandler extends ParenthesesInsertHandler<LookupElement>
    {
        private BracketsInsertHandler()
        {
            super(false, false, true, false, '[', ']');
        }

        @Override
        protected boolean placeCaretInsideParentheses(InsertionContext context, LookupElement item)
        {
            return true;
        }
    }
}
