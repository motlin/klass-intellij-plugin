package com.klass.intellij.reference;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.icons.AllIcons;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiPolyVariantReferenceBase;
import com.intellij.psi.ResolveResult;
import com.klass.intellij.KlassUtil;
import com.klass.intellij.psi.KlassElementFactory;
import com.klass.intellij.psi.KlassProjection;
import com.klass.intellij.psi.KlassProjectionName;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class KlassProjectionReference extends PsiPolyVariantReferenceBase<PsiElement> {
  private final String projectionName;

  private KlassProjection projection;

  public KlassProjectionReference(@NotNull PsiElement element, String projectionName) {
    super(element, new TextRange(0, projectionName.length()));
    this.projectionName = projectionName;
  }

  @NotNull @Override
  public ResolveResult[] multiResolve(boolean incompleteCode) {
    if (this.projection != null) {
      return new PsiElementResolveResult[] {new PsiElementResolveResult(this.projection)};
    }

    ResolveResult[] resolveResults =
        KlassUtil.findProjections(this.myElement).stream()
            .filter(projection -> projection.getName().equals(this.projectionName))
            .map(PsiElementResolveResult::new)
            .toArray(ResolveResult[]::new);
    if (resolveResults.length == 1) {
      this.projection = (KlassProjection) resolveResults[0].getElement();
    }
    return resolveResults;
  }

  @Nullable @Override
  public PsiElement resolve() {
    ResolveResult[] resolveResults = this.multiResolve(false);
    return resolveResults.length == 1 ? resolveResults[0].getElement() : null;
  }

  @NotNull @Override
  public Object[] getVariants() {
    List<KlassProjection> projections = KlassUtil.findProjections(this.myElement);
    List<LookupElement> variants = new ArrayList<>();
    for (KlassProjection projection : projections) {
      if (projection.getName() != null && !projection.getName().isEmpty()) {
        LookupElementBuilder lookupElementBuilder =
            LookupElementBuilder.create(projection.getName())
                .withIcon(AllIcons.Hierarchy.Subtypes)
                .withTypeText(projection.getContainingFile().getName());
        variants.add(lookupElementBuilder);
      }
    }
    return variants.toArray();
  }

  @Override
  public PsiElement handleElementRename(String newElementName) {
    ASTNode node = this.myElement.getNode();
    if (node != null) {
      KlassProjectionName projectionReference =
          KlassElementFactory.createProjectionName(this.myElement.getProject(), newElementName);

      ASTNode newNode = projectionReference.getNode();
      node.getTreeParent().replaceChild(node, newNode);
    }
    return this.myElement;
  }
}
