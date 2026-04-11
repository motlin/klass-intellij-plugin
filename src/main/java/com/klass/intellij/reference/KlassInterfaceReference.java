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
import com.klass.intellij.psi.KlassInterface;
import com.klass.intellij.psi.KlassInterfaceName;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class KlassInterfaceReference extends PsiPolyVariantReferenceBase<PsiElement> {

	private final String name;

	public KlassInterfaceReference(@NotNull PsiElement element, String name) {
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
		ResolveResult[] interfaceResolveResults = KlassUtil.findInterfaces(this.myElement)
			.stream()
			.filter((klassInterface) -> klassInterface.getName().equals(this.name))
			.map(PsiElementResolveResult::new)
			.toArray(ResolveResult[]::new);
		if (interfaceResolveResults.length > 0) {
			return KlassUtil.preferSamePackage(this.myElement, interfaceResolveResults);
		}

		return new ResolveResult[] {};
	}

	@NotNull @Override
	public Object[] getVariants() {
		List<KlassInterface> interfaces = KlassUtil.findInterfaces(this.myElement);
		List<LookupElement> variants = new ArrayList<>();
		for (KlassInterface klassInterface : interfaces) {
			if (klassInterface.getName() != null && !klassInterface.getName().isEmpty()) {
				LookupElementBuilder lookupElementBuilder = LookupElementBuilder.create(klassInterface.getName())
					.withIcon(AllIcons.Nodes.Interface)
					.withTypeText(klassInterface.getContainingFile().getName());
				variants.add(lookupElementBuilder);
			}
		}
		return variants.toArray();
	}

	@Override
	public PsiElement handleElementRename(String newElementName) {
		ASTNode node = this.myElement.getNode();
		if (node != null) {
			KlassInterfaceName interfaceName = KlassElementFactory.createInterfaceName(
				this.myElement.getProject(),
				newElementName
			);

			ASTNode newNode = interfaceName.getNode();
			node.getTreeParent().replaceChild(node, newNode);
		}
		return this.myElement;
	}
}
