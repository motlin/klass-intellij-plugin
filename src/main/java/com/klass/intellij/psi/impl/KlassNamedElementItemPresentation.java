package com.klass.intellij.psi.impl;

import com.intellij.navigation.ItemPresentation;
import com.klass.intellij.psi.KlassNamedElement;
import javax.swing.*;

public class KlassNamedElementItemPresentation implements ItemPresentation {
  protected final KlassNamedElement element;
  private final String locationString;
  private final Icon icon;

  public KlassNamedElementItemPresentation(
      KlassNamedElement element, String locationString, Icon icon) {
    this.element = element;
    this.locationString = locationString;
    this.icon = icon;
  }

  @Override
  public String getPresentableText() {
    return this.element.getNombre().getNombreText().getText();
  }

  @Override
  public String getLocationString() {
    return this.locationString;
  }

  @Override
  public Icon getIcon(boolean unused) {
    return this.icon;
  }
}
