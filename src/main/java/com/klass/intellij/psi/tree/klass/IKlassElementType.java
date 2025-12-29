package com.klass.intellij.psi.tree.klass;

import com.intellij.psi.tree.IElementType;
import com.klass.intellij.KlassLanguage;
import org.jetbrains.annotations.NonNls;

public class IKlassElementType extends IElementType {
  public IKlassElementType(@NonNls String debugName) {
    super(debugName, KlassLanguage.INSTANCE);
  }
}
