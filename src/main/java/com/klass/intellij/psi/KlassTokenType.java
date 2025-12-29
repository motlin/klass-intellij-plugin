package com.klass.intellij.psi;

import com.intellij.psi.tree.IElementType;
import com.klass.intellij.KlassLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class KlassTokenType extends IElementType {
  public static final IElementType C_STYLE_COMMENT = new KlassTokenType("C_STYLE_COMMENT");
  public static final IElementType END_OF_LINE_COMMENT = new KlassTokenType("END_OF_LINE_COMMENT");

  public KlassTokenType(@NotNull @NonNls String debugName) {
    super(debugName, KlassLanguage.INSTANCE);
  }

  @Override
  public String toString() {
    return KlassTokenType.class.getSimpleName() + '.' + super.toString();
  }
}
