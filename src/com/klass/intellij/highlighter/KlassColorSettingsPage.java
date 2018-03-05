package com.klass.intellij.highlighter;

import com.intellij.codeHighlighting.RainbowHighlighter;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import com.intellij.psi.codeStyle.DisplayPriority;
import com.intellij.psi.codeStyle.DisplayPrioritySortable;
import com.klass.intellij.KlassIcons;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Map;

public class KlassColorSettingsPage implements ColorSettingsPage, DisplayPrioritySortable
{
    @NonNls
    private static final Map<String, TextAttributesKey> OUR_TAGS = RainbowHighlighter.createRainbowHLM();

    static
    {
        OUR_TAGS.put("property", KlassHighlightingColors.INSTANCE_FINAL_FIELD_ATTRIBUTES);
        OUR_TAGS.put("klass", KlassHighlightingColors.CLASS_NAME_ATTRIBUTES);
        OUR_TAGS.put("enumeration", KlassHighlightingColors.ENUM_NAME_ATTRIBUTES);
        OUR_TAGS.put("enumerationLiteral", KlassHighlightingColors.ENUM_LITERAL_ATTRIBUTES);
        OUR_TAGS.put("projection", KlassHighlightingColors.PROJECTION_NAME_ATTRIBUTES);
    }

    private static final AttributesDescriptor[] DESCRIPTORS =
            {
                    new AttributesDescriptor("Keyword", KlassHighlightingColors.KEYWORD),
                    new AttributesDescriptor("Class name", KlassHighlightingColors.CLASS_NAME_ATTRIBUTES),
                    new AttributesDescriptor("Enumeration name", KlassHighlightingColors.ENUM_NAME_ATTRIBUTES),
                    new AttributesDescriptor("Enumeration literal", KlassHighlightingColors.ENUM_LITERAL_ATTRIBUTES),
                    new AttributesDescriptor("Projection name", KlassHighlightingColors.PROJECTION_NAME_ATTRIBUTES),
                    new AttributesDescriptor("Number", KlassHighlightingColors.NUMBER),
                    new AttributesDescriptor("Dot", KlassHighlightingColors.DOT),
                    new AttributesDescriptor("Comma", KlassHighlightingColors.COMMA),
                    new AttributesDescriptor("Braces", KlassHighlightingColors.BRACES),
                    new AttributesDescriptor("Brackets", KlassHighlightingColors.BRACKETS),
                    new AttributesDescriptor("Parenthesis", KlassHighlightingColors.PARENTHESES),
                    new AttributesDescriptor("Block Comment", KlassHighlightingColors.KLASS_BLOCK_COMMENT),
                    new AttributesDescriptor("End of line Comment", KlassHighlightingColors.LINE_COMMENT),
                    new AttributesDescriptor("Property name", KlassHighlightingColors.INSTANCE_FINAL_FIELD_ATTRIBUTES),
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
        return ""
                + "enumeration <enumeration>Status</enumeration>\n"
                + "{\n"
                + "    <enumerationLiteral>OPEN</enumerationLiteral>,\n"
                + "    <enumerationLiteral>ON_HOLD</enumerationLiteral>,\n"
                + "    <enumerationLiteral>CLOSED</enumerationLiteral>,\n"
                + "}\n"
                + "\n"
                + "class <klass>Question</klass>\n"
                + "{\n"
                + "    <property>title</property> : String\n"
                + "    <property>body</property>  : String\n"
                + "    <property>status</property>: <enumeration>Status</enumeration>\n"
                + "}\n"
                + "\n"
                + "class <klass>Answer</klass>\n"
                + "{\n"
                + "    <property>body</property>    : String?\n"
                + "}\n"
                + "\n"
                + "association <klass>QuestionHasAnswer</klass>\n"
                + "{\n"
                + "    <property>question</property>: <klass>Question</klass>[1..1]\n"
                + "    <property>answer</property>  : <klass>Answer</klass>[0..*]\n"
                + "}\n"
                + "\n"
                + "projection QuestionReadProjection (Question)\n"
                + "{\n"
                + "    title: \"Question title\"\n"
                + "    body: \"Question body\" \n"
                + "    answers: {\n"
                + "        body: \"Answer body\"\n"
                + "    }\n"
                + "}\n";
    }

    @Nullable
    @Override
    public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap()
    {
        return OUR_TAGS;
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

    @Override
    public DisplayPriority getPriority()
    {
        return DisplayPriority.LANGUAGE_SETTINGS;
    }
}
