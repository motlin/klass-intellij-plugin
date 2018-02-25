package com.klass.intellij.completion;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.klass.intellij.KlassLanguage;
import com.klass.intellij.psi.KlassTypes;
import org.jetbrains.annotations.NotNull;

public class KlassCompletionContributor extends CompletionContributor
{
    public KlassCompletionContributor()
    {
        CompletionProvider<CompletionParameters> helloCompletionProvider = new CompletionProvider<CompletionParameters>()
        {
            public void addCompletions(
                    @NotNull CompletionParameters parameters,
                    ProcessingContext context,
                    @NotNull CompletionResultSet resultSet)
            {
                PsiElement originalPosition = parameters.getOriginalPosition();
                PsiElement position = parameters.getPosition();
                System.out.println("position = " + position);
                System.out.println("originalPosition = " + originalPosition);
                resultSet.addElement(LookupElementBuilder.create("Hello"));
                resultSet.addElement(LookupElementBuilder.create("Answer2"));
                resultSet.addElement(LookupElementBuilder.create("Question2"));
            }
        };

        extend(
                CompletionType.BASIC,
                PlatformPatterns.psiElement(KlassTypes.ASSOCIATION_END_TYPE)
                        .withLanguage(KlassLanguage.INSTANCE),
                helloCompletionProvider
        );
        extend(
                CompletionType.BASIC,
                PlatformPatterns.psiElement(KlassTypes.ASSOCIATION_END)
                        .withLanguage(KlassLanguage.INSTANCE),
                helloCompletionProvider
        );
        extend(
                CompletionType.BASIC,
                PlatformPatterns.psiElement(KlassTypes.ASSOCIATION)
                        .withLanguage(KlassLanguage.INSTANCE),
                helloCompletionProvider
        );
    }
}
