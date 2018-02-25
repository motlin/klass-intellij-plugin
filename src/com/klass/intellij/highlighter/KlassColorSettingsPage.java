package com.klass.intellij.highlighter;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import com.klass.intellij.KlassIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Map;

public class KlassColorSettingsPage implements ColorSettingsPage
{
    private static final AttributesDescriptor[] DESCRIPTORS =
            {
                    new AttributesDescriptor("Keyword", KlassHighlightingColors.KEYWORD),
                    new AttributesDescriptor("Class name", KlassHighlightingColors.CLASS_NAME_ATTRIBUTES),
                    new AttributesDescriptor("Number", KlassHighlightingColors.NUMBER),
                    new AttributesDescriptor("Dot", KlassHighlightingColors.DOT),
                    new AttributesDescriptor("Comma", KlassHighlightingColors.COMMA),
                    new AttributesDescriptor("Semicolon", KlassHighlightingColors.KLASS_SEMICOLON),
                    new AttributesDescriptor("Operation Sign", KlassHighlightingColors.OPERATION_SIGN),
                    new AttributesDescriptor("Braces", KlassHighlightingColors.BRACES),
                    new AttributesDescriptor("Brackets", KlassHighlightingColors.BRACKETS),
                    new AttributesDescriptor("Parenthesis", KlassHighlightingColors.PARENTHESES),
            };

    @Nullable
    @Override
    public Icon getIcon()
    {
        return KlassIcons.FILE;
    }

    @NotNull
    @Override
    public SyntaxHighlighter getHighlighter()
    {
        return new KlassSyntaxHighlighter();
    }

    @NotNull
    @Override
    public String getDemoText()
    {
        return "class ExampleClass\n"
               + "{\n"
               + "}\n"
               + "\n"
               + "association ExampleAssociation\n"
               + "{\n"
               + "}\n"
               + "\n";
    }

    @Nullable
    @Override
    public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap()
    {
        return null;
    }

    @NotNull
    @Override
    public AttributesDescriptor[] getAttributeDescriptors()
    {
        return DESCRIPTORS;
    }

    @NotNull
    @Override
    public ColorDescriptor[] getColorDescriptors()
    {
        return ColorDescriptor.EMPTY_ARRAY;
    }

    @NotNull
    @Override
    public String getDisplayName()
    {
        return "Klass";
    }
}
