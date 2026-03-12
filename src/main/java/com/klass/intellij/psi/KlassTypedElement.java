package com.klass.intellij.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public interface KlassTypedElement extends PsiElement {
	@NotNull default PsiElement getType() {
		if (this instanceof KlassProjectionWithAssociationEnd end) {
			return end.getAssociationEndName();
		}

		throw new AssertionError();
	}
}
