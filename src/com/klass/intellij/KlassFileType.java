package com.klass.intellij;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.*;

import javax.swing.*;

public class KlassFileType extends LanguageFileType
{
    public static final KlassFileType INSTANCE = new KlassFileType();

    private KlassFileType()
    {
        super(KlassLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName()
    {
        return "Klass file";
    }

    @NotNull
    @Override
    public String getDescription()
    {
        return "Klass language file";
    }

    @NotNull
    @Override
    public String getDefaultExtension()
    {
        return "klass";
    }

    @Nullable
    @Override
    public Icon getIcon()
    {
        return KlassIcons.FILE;
    }
}
