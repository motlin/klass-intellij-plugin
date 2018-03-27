package com.klass.intellij.reference;

import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.completion.util.ParenthesesInsertHandler;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.klass.intellij.KlassUtil;
import com.klass.intellij.psi.KlassEnumeration;
import com.klass.intellij.psi.KlassKlass;
import com.klass.intellij.psi.KlassNamedElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class KlassKlassReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference
{
    private final String name;

    private KlassNamedElement namedElement;

    public KlassKlassReference(@NotNull PsiElement element, String name)
    {
        super(element, new TextRange(0, name.length()));
        this.name = name;
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode)
    {
        if (this.namedElement != null)
        {
            return new PsiElementResolveResult[]{new PsiElementResolveResult(this.namedElement)};
        }

        Project project = this.myElement.getProject();
        List<KlassKlass> klasses = KlassUtil.findClasses(project)
                .stream()
                .filter(klass -> klass.getName().equals(this.name))
                .collect(Collectors.toList());
        if (klasses.size() == 2)
        {
            throw new AssertionError();
        }
        if (klasses.size() == 1)
        {
            this.namedElement = klasses.get(0);
            return new PsiElementResolveResult[]{new PsiElementResolveResult(this.namedElement)};
        }

        List<KlassEnumeration> enumerations1 = KlassUtil.findEnumerations(project);
        List<KlassEnumeration> enumerations = enumerations1
                .stream()
                .filter(enumeration -> enumeration.getName().equals(this.name))
                .collect(Collectors.toList());
        if (enumerations.size() == 2)
        {
            throw new AssertionError();
        }
        if (enumerations.size() == 1)
        {
            this.namedElement = enumerations.get(0);
            return new PsiElementResolveResult[]{new PsiElementResolveResult(this.namedElement)};
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
        BracketsInsertHandler insertHandler = new BracketsInsertHandler();
        for (KlassKlass klassKlass : klassKlasses)
        {
            if (klassKlass.getName() != null && !klassKlass.getName().isEmpty())
            {
                LookupElementBuilder lookupElementBuilder = LookupElementBuilder.create(klassKlass.getName())
                        .withIcon(AllIcons.Nodes.Class)
                        .withTypeText(klassKlass.getContainingFile().getName())
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
