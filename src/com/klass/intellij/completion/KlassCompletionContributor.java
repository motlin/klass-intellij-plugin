package com.klass.intellij.completion;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PsiElementPattern.Capture;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.klass.intellij.KlassLanguage;
import com.klass.intellij.psi.KlassFile;
import org.jetbrains.annotations.NotNull;

import static com.intellij.patterns.PlatformPatterns.psiElement;

public class KlassCompletionContributor extends CompletionContributor
{
    private static final Capture<PsiElement> MULTIPLICITY = psiElement()
            .withLanguage(KlassLanguage.INSTANCE)
            .afterLeaf("[");

    private static final Capture<PsiElement> KEYWORD = psiElement()
            .withLanguage(KlassLanguage.INSTANCE)
            .withParent(KlassFile.class);

    public KlassCompletionContributor()
    {
        CompletionProvider<CompletionParameters> multiplicityCompletionProvider =
                new CompletionProvider<CompletionParameters>()
        {
            public void addCompletions(
                    @NotNull CompletionParameters parameters,
                    ProcessingContext context,
                    @NotNull CompletionResultSet resultSet)
            {
                resultSet.addElement(LookupElementBuilder.create("0..1").withTailText(" One, not required", true));
                resultSet.addElement(LookupElementBuilder.create("1..1").withTailText(" One, required", true));
                resultSet.addElement(LookupElementBuilder.create("0..*").withTailText(" Many", true));
                resultSet.addElement(LookupElementBuilder.create("1..*").withTailText(" Many, non-empty set", true));
            }
        };

        CompletionProvider<CompletionParameters> keywordCompletionProvider =
                new CompletionProvider<CompletionParameters>()
                {
                    @Override
                    protected void addCompletions(
                            @NotNull CompletionParameters parameters,
                            ProcessingContext context,
                            @NotNull CompletionResultSet result)
                    {
                        result.addElement(LookupElementBuilder.create("class"));
                        result.addElement(LookupElementBuilder.create("enumeration"));
                        result.addElement(LookupElementBuilder.create("association"));
            }
        };

        this.extend(CompletionType.BASIC, MULTIPLICITY, multiplicityCompletionProvider);

        this.extend(CompletionType.BASIC, KEYWORD, keywordCompletionProvider);
    }

    @Override
    public void fillCompletionVariants(@NotNull CompletionParameters parameters, @NotNull CompletionResultSet result)
    {
        super.fillCompletionVariants(parameters, result);
    }
}
