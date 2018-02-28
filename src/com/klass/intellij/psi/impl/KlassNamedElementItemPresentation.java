package com.klass.intellij.psi.impl;

import com.intellij.navigation.ItemPresentation;
import com.klass.intellij.psi.KlassNamedElement;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.swing.*;

public class KlassNamedElementItemPresentation implements ItemPresentation
{
    protected final KlassNamedElement element;
    private final String locationString;
    private final Icon icon;

    public KlassNamedElementItemPresentation(
            KlassNamedElement element,
            String locationString,
            Icon icon)
    {
        this.element = element;
        this.locationString = locationString;
        this.icon = icon;
    }

    @Nullable
    @Override
    public String getPresentableText()
    {
        return this.element.getNombre().getText();
    }

    @NotNull
    @Override
    public String getLocationString()
    {
        return this.locationString;
    }

    @Nullable
    @Override
    public Icon getIcon(boolean unused)
    {
        return this.icon;
    }
}
