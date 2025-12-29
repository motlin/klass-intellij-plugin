package com.klass.intellij.reference;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.icons.AllIcons;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiPolyVariantReferenceBase;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.util.PsiTreeUtil;
import com.klass.intellij.KlassUtil;
import com.klass.intellij.psi.KlassElementFactory;
import com.klass.intellij.psi.KlassKlass;
import com.klass.intellij.psi.KlassParameterName;
import com.klass.intellij.psi.KlassProjection;
import com.klass.intellij.psi.KlassUrl;
import com.klass.intellij.psi.KlassUrlGroup;
import com.klass.intellij.psi.KlassUrlPart;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class KlassParameterReference extends PsiPolyVariantReferenceBase<PsiElement> {
  private final String parameterName;

  public KlassParameterReference(@NotNull PsiElement element, String parameterName) {
    super(element, new TextRange(0, parameterName.length()));
    this.parameterName = parameterName;
  }

  @NotNull @Override
  public ResolveResult[] multiResolve(boolean incompleteCode) {
    KlassProjection klassProjection =
        PsiTreeUtil.getParentOfType(this.myElement, KlassProjection.class);
    KlassUrlGroup urlGroup = PsiTreeUtil.getParentOfType(this.myElement, KlassUrlGroup.class);
    if (klassProjection != null) {
      ResolveResult[] resolveResults =
          klassProjection
              .getParameterDeclarationsParens()
              .getParameterDeclarations()
              .getParameterDeclarationList()
              .stream()
              .filter(
                  parameterDeclaration -> parameterDeclaration.getName().equals(this.parameterName))
              .map(PsiElementResolveResult::new)
              .toArray(ResolveResult[]::new);
      return resolveResults;
    }

    if (urlGroup != null) {
      KlassUrl url = urlGroup.getUrl();
      ResolveResult[] resolveResults =
          url.getUrlPartList().stream()
              .map(KlassUrlPart::getParameterDeclaration)
              .filter(Objects::nonNull)
              .filter(pathParameter -> pathParameter.getName().equals(this.parameterName))
              .map(PsiElementResolveResult::new)
              .toArray(ResolveResult[]::new);
      return resolveResults;
    }

    return new ResolveResult[] {};
  }

  @Nullable @Override
  public PsiElement resolve() {
    ResolveResult[] resolveResults = this.multiResolve(false);
    return resolveResults.length == 1 ? resolveResults[0].getElement() : null;
  }

  @NotNull @Override
  public Object[] getVariants() {
    Project project = this.myElement.getProject();
    List<KlassKlass> klassKlasses = KlassUtil.findClasses(project);
    List<LookupElement> variants = new ArrayList<>();
    for (KlassKlass klassKlass : klassKlasses) {
      if (klassKlass.getName() != null && !klassKlass.getName().isEmpty()) {
        LookupElementBuilder lookupElementBuilder =
            LookupElementBuilder.create(klassKlass.getName())
                .withIcon(AllIcons.Nodes.Class)
                .withTypeText(klassKlass.getContainingFile().getName());
        variants.add(lookupElementBuilder);
      }
    }
    return variants.toArray();
  }

  @Override
  public PsiElement handleElementRename(String newElementName) {
    ASTNode node = this.myElement.getNode();
    if (node != null) {
      KlassParameterName parameterName =
          KlassElementFactory.createParameterName(this.myElement.getProject(), newElementName);

      ASTNode newNode = parameterName.getNode();
      node.getTreeParent().replaceChild(node, newNode);
    }
    return this.myElement;
  }
}
