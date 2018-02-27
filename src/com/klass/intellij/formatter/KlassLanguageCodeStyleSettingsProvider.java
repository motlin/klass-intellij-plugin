package com.klass.intellij.formatter;

import com.intellij.lang.Language;
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizable;
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider;
import com.klass.intellij.KlassLanguage;
import org.jetbrains.annotations.NotNull;

public class KlassLanguageCodeStyleSettingsProvider extends LanguageCodeStyleSettingsProvider
{
    @NotNull
    @Override
    public Language getLanguage()
    {
        return KlassLanguage.INSTANCE;
    }

    @Override
    public void customizeSettings(@NotNull CodeStyleSettingsCustomizable consumer, @NotNull SettingsType settingsType)
    {
        if (settingsType== SettingsType.SPACING_SETTINGS)
        {

        }
        if (settingsType == SettingsType.WRAPPING_AND_BRACES_SETTINGS)
        {
            consumer.showStandardOptions("KEEP_LINE_BREAKS", "ALIGN_GROUP_FIELD_DECLARATIONS", "BRACE_STYLE");
            consumer.renameStandardOption("ALIGN_GROUP_FIELD_DECLARATIONS", "Align property declarations");
        }
        if (settingsType == SettingsType.BLANK_LINES_SETTINGS)
        {

        }
    }

    @Override
    public String getCodeSample(@NotNull SettingsType settingsType)
    {
        return ""
               + "class Question\n"
               + "{\n"
               + "    title   : String\n"
               + "    body    : String\n"
               + "}\n"
               + "\n"
               + "class Answer\n"
               + "{\n"
               + "    body    : String?\n"
               + "}\n"
               + "\n"
               + "association QuestionHasAnswer\n"
               + "{\n"
               + "    question: Question[1..1]\n"
               + "    answer  : Answer[0..*]\n"
               + "}\n";
    }
}
