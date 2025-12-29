package com.klass.intellij.completion;

import static com.intellij.patterns.PlatformPatterns.psiElement;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PsiElementPattern.Capture;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiErrorElement;
import com.intellij.util.ProcessingContext;
import com.klass.intellij.KlassLanguage;
import org.jetbrains.annotations.NotNull;

public class KlassCompletionContributor extends CompletionContributor {
  private static final Capture<PsiElement> MULTIPLICITY =
      psiElement().withLanguage(KlassLanguage.INSTANCE).afterLeaf("[");

  private static final Capture<PsiElement> KEYWORD =
      psiElement().withLanguage(KlassLanguage.INSTANCE).withParent(PsiErrorElement.class);

  public KlassCompletionContributor() {
    CompletionProvider<CompletionParameters> multiplicityCompletionProvider =
        new CompletionProvider<CompletionParameters>() {
          public void addCompletions(
              @NotNull CompletionParameters parameters,
              ProcessingContext context,
              @NotNull CompletionResultSet resultSet) {
            resultSet.addElement(
                LookupElementBuilder.create("0..1").withTailText(" One, not required", true));
            resultSet.addElement(
                LookupElementBuilder.create("1..1").withTailText(" One, required", true));
            resultSet.addElement(LookupElementBuilder.create("0..*").withTailText(" Many", true));
            resultSet.addElement(
                LookupElementBuilder.create("1..*").withTailText(" Many, non-empty set", true));
          }
        };

    CompletionProvider<CompletionParameters> keywordCompletionProvider =
        new CompletionProvider<CompletionParameters>() {
          @Override
          protected void addCompletions(
              @NotNull CompletionParameters parameters,
              ProcessingContext context,
              @NotNull CompletionResultSet result) {
            result.addElement(LookupElementBuilder.create("class"));
            result.addElement(LookupElementBuilder.create("enumeration"));
            result.addElement(LookupElementBuilder.create("association"));
            result.addElement(LookupElementBuilder.create("projection"));
          }
        };

    // TODO: These oversimplified forms of completion don't seem to work well at all.

    // this.extend(CompletionType.BASIC, MULTIPLICITY, multiplicityCompletionProvider);

    // this.extend(CompletionType.BASIC, KEYWORD, keywordCompletionProvider);
  }

  @Override
  public void fillCompletionVariants(
      @NotNull CompletionParameters parameters, @NotNull CompletionResultSet result) {
    super.fillCompletionVariants(parameters, result);
  }
}
