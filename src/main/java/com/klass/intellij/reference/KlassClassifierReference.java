package com.klass.intellij.reference;

import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.completion.util.ParenthesesInsertHandler;
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
import com.klass.intellij.psi.KlassClassifierName;
import com.klass.intellij.psi.KlassElementFactory;
import com.klass.intellij.psi.KlassInterface;
import com.klass.intellij.psi.KlassKlass;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class KlassClassifierReference extends PsiPolyVariantReferenceBase<PsiElement> {
  private final String name;

  public KlassClassifierReference(@NotNull PsiElement element, String name) {
    super(element, new TextRange(0, name.length()));
    this.name = name;
  }

  @Nullable @Override
  public PsiElement resolve() {
    ResolveResult[] resolveResults = this.multiResolve(false);
    return resolveResults.length == 1 ? resolveResults[0].getElement() : null;
  }

  @NotNull @Override
  public ResolveResult[] multiResolve(boolean incompleteCode) {
    // TODO: Combine
    ResolveResult[] interfaceResolveResults =
        KlassUtil.findInterfaces(this.myElement).stream()
            .filter(klassInterface -> klassInterface.getName().equals(this.name))
            .map(PsiElementResolveResult::new)
            .toArray(ResolveResult[]::new);
    if (interfaceResolveResults.length > 0) {
      return interfaceResolveResults;
    }

    ResolveResult[] klassResolveResults =
        KlassUtil.findClasses(this.myElement).stream()
            .filter(klass -> klass.getName().equals(this.name))
            .map(PsiElementResolveResult::new)
            .toArray(ResolveResult[]::new);
    if (klassResolveResults.length > 0) {
      return klassResolveResults;
    }

    return new ResolveResult[] {};
  }

  @NotNull @Override
  public Object[] getVariants() {
    List<LookupElement> variants = new ArrayList<>();
    BracketsInsertHandler insertHandler = new BracketsInsertHandler();
    for (KlassInterface klassInterface : KlassUtil.findInterfaces(this.myElement)) {
      if (klassInterface.getName() != null && !klassInterface.getName().isEmpty()) {
        variants.add(
            LookupElementBuilder.create(klassInterface.getName())
                .withIcon(AllIcons.Nodes.Interface)
                .withTypeText(klassInterface.getContainingFile().getName())
                .withInsertHandler(insertHandler));
      }
    }
    for (KlassKlass klassKlass : KlassUtil.findClasses(this.myElement)) {
      if (klassKlass.getName() != null && !klassKlass.getName().isEmpty()) {
        variants.add(
            LookupElementBuilder.create(klassKlass.getName())
                .withIcon(AllIcons.Nodes.Class)
                .withTypeText(klassKlass.getContainingFile().getName())
                .withInsertHandler(insertHandler));
      }
    }
    return variants.toArray();
  }

  private static class BracketsInsertHandler extends ParenthesesInsertHandler<LookupElement> {
    private BracketsInsertHandler() {
      super(false, false, true, false, '[', ']');
    }

    @Override
    protected boolean placeCaretInsideParentheses(InsertionContext context, LookupElement item) {
      return true;
    }
  }

  @Override
  public PsiElement handleElementRename(String newElementName) {
    ASTNode node = this.myElement.getNode();
    if (node != null) {
      KlassClassifierName classifierName =
          KlassElementFactory.createClassifierName(this.myElement.getProject(), newElementName);

      ASTNode newNode = classifierName.getNode();
      node.getTreeParent().replaceChild(node, newNode);
    }
    return this.myElement;
  }
}
