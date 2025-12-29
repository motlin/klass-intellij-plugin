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
import com.klass.intellij.KlassUtil;
import com.klass.intellij.psi.KlassElementFactory;
import com.klass.intellij.psi.KlassEnumeration;
import com.klass.intellij.psi.KlassEnumerationType;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class KlassEnumerationReference extends PsiPolyVariantReferenceBase<PsiElement> {
  public static final String[] VARIANTS = {
    "Boolean", "Integer", "Long", "Double", "Float", "String", "Instant", "LocalDate",
  };
  public static final List<LookupElementBuilder> DATA_TYPE_VARIANTS =
      Stream.of(VARIANTS)
          .map(variant -> LookupElementBuilder.create(variant).withIcon(AllIcons.Nodes.Enum))
          .collect(Collectors.toList());

  private final String enumerationName;

  public KlassEnumerationReference(@NotNull PsiElement element, String enumerationName) {
    super(element, new TextRange(0, enumerationName.length()));
    this.enumerationName = enumerationName;
  }

  @NotNull @Override
  public ResolveResult[] multiResolve(boolean incompleteCode) {
    Project project = this.myElement.getProject();
    return KlassUtil.findEnumerations(project).stream()
        .filter(klassEnumeration -> klassEnumeration.getName().equals(this.enumerationName))
        .map(PsiElementResolveResult::new)
        .toArray(ResolveResult[]::new);
  }

  @Nullable @Override
  public PsiElement resolve() {
    ResolveResult[] resolveResults = this.multiResolve(false);
    return resolveResults.length == 1 ? resolveResults[0].getElement() : null;
  }

  @NotNull @Override
  public Object[] getVariants() {
    Project project = this.myElement.getProject();
    List<KlassEnumeration> klassEnumerations = KlassUtil.findEnumerations(project);
    List<LookupElement> variants = new ArrayList<>();

    for (KlassEnumeration klassEnumeration : klassEnumerations) {
      if (klassEnumeration.getName() != null && !klassEnumeration.getName().isEmpty()) {
        LookupElementBuilder lookupElementBuilder =
            LookupElementBuilder.create(klassEnumeration.getName())
                .withIcon(AllIcons.Nodes.Enum)
                .withTypeText(klassEnumeration.getContainingFile().getName());
        variants.add(lookupElementBuilder);
      }
    }

    variants.addAll(DATA_TYPE_VARIANTS);

    return variants.toArray();
  }

  @Override
  public PsiElement handleElementRename(String newElementName) {
    ASTNode node = this.myElement.getNode();
    if (node != null) {
      KlassEnumerationType enumerationType =
          KlassElementFactory.createEnumerationType(this.myElement.getProject(), newElementName);

      ASTNode newNode = enumerationType.getNode();
      node.getTreeParent().replaceChild(node, newNode);
    }
    return this.myElement;
  }
}
