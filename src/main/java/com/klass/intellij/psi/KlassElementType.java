package com.klass.intellij.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.java.IJavaElementType;
import com.klass.intellij.KlassLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class KlassElementType extends IElementType
{
    public KlassElementType(@NotNull @NonNls String debugName)
    {
        super(debugName, KlassLanguage.INSTANCE);
    }
}
