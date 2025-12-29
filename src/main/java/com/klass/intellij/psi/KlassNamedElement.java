package com.klass.intellij.psi;

import com.intellij.navigation.NavigationItem;
import com.intellij.psi.PsiNameIdentifierOwner;
import org.jetbrains.annotations.NotNull;

public interface KlassNamedElement extends PsiNameIdentifierOwner, NavigationItem {
  @NotNull KlassNombre getNombre();
}
