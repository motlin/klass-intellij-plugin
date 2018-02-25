package com.klass.intellij.refactor;

import com.intellij.lang.refactoring.RefactoringSupportProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.LocalSearchScope;
import com.intellij.psi.search.PsiSearchHelper;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.klass.intellij.psi.KlassAssociation;
import com.klass.intellij.psi.KlassClass;
import org.jetbrains.annotations.NotNull;

public class KlassRefactoringSupportProvider extends RefactoringSupportProvider
{
    @Override
    public boolean isSafeDeleteAvailable(@NotNull PsiElement element)
    {
        return element instanceof KlassClass || element instanceof KlassAssociation;
    }

    @Override
    public boolean isMemberInplaceRenameAvailable(@NotNull PsiElement element, PsiElement context)
    {
        return element instanceof KlassClass || element instanceof KlassAssociation;
    }

    @Override
    public boolean isInplaceRenameAvailable(@NotNull PsiElement element, PsiElement context)
    {
        return KlassRefactoringSupportProvider.mayRenameInplace(element, context);
    }

    public static boolean mayRenameInplace(PsiElement elementToRename, PsiElement nameSuggestionContext)
    {
        if (nameSuggestionContext != null
            && nameSuggestionContext.getContainingFile() != elementToRename.getContainingFile())
        {
            return false;
        }
        if (!(elementToRename instanceof KlassClass)
            && !(elementToRename instanceof KlassAssociation))
        {
            return false;
        }
        SearchScope useScope =
                PsiSearchHelper.SERVICE.getInstance(elementToRename.getProject()).getUseScope(elementToRename);
        if (!(useScope instanceof LocalSearchScope))
        {
            return false;
        }
        PsiElement[] scopeElements = ((LocalSearchScope) useScope).getScope();
        if (scopeElements.length > 1)
        {
            return false;
        }
        PsiFile containingFile = elementToRename.getContainingFile();
        return PsiTreeUtil.isAncestor(containingFile, scopeElements[0], false);
    }
}
