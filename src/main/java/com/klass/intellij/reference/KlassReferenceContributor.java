package com.klass.intellij.reference;

import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.*;
import com.intellij.util.ProcessingContext;
import com.klass.intellij.psi.KlassKlassName;
import org.jetbrains.annotations.NotNull;

public class KlassReferenceContributor extends PsiReferenceContributor {
  @Override
  public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
    registrar.registerReferenceProvider(
        PlatformPatterns.psiElement(KlassKlassName.class),
        new PsiReferenceProvider() {
          @NotNull @Override
          public PsiReference[] getReferencesByElement(
              @NotNull PsiElement element, @NotNull ProcessingContext context) {
            KlassKlassName KlassKlassName = (KlassKlassName) element;

            String className = KlassKlassName.getText();
            if (className == null) {
              return PsiReference.EMPTY_ARRAY;
            }

            return new PsiReference[] {new KlassKlassReference(element, className)};
          }
        });
  }
}
