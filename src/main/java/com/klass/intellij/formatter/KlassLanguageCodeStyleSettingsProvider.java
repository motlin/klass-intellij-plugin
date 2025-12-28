package com.klass.intellij.formatter;

import com.intellij.application.options.IndentOptionsEditor;
import com.intellij.application.options.SmartIndentOptionsEditor;
import com.intellij.lang.Language;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizable;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider;
import com.intellij.util.LocalTimeCounter;
import com.klass.intellij.KlassFileType;
import com.klass.intellij.KlassLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
        if (settingsType == SettingsType.SPACING_SETTINGS)
        {
            consumer.showAllStandardOptions();
        }
        if (settingsType == SettingsType.WRAPPING_AND_BRACES_SETTINGS)
        {
            consumer.showStandardOptions("KEEP_LINE_BREAKS", "ALIGN_GROUP_FIELD_DECLARATIONS", "BRACE_STYLE");
            consumer.renameStandardOption("ALIGN_GROUP_FIELD_DECLARATIONS", "Align property declarations");
        }
        if (settingsType == SettingsType.BLANK_LINES_SETTINGS)
        {
            consumer.showStandardOptions(
                    "KEEP_BLANK_LINES_IN_DECLARATIONS",
                    "KEEP_BLANK_LINES_IN_CODE",
                    "KEEP_BLANK_LINES_BEFORE_RBRACE",
                    "BLANK_LINES_AROUND_CLASS"
            );

            consumer.renameStandardOption("BLANK_LINES_AROUND_FIELD", "Blank lines around property");
        }
    }

    @Override
    public CommonCodeStyleSettings getDefaultCommonSettings()
    {
        CommonCodeStyleSettings defaultSettings = new CommonCodeStyleSettings(KlassLanguage.INSTANCE);
        defaultSettings.initIndentOptions();
        return defaultSettings;
    }

    @Nullable
    @Override
    public IndentOptionsEditor getIndentOptionsEditor()
    {
        return new SmartIndentOptionsEditor();
    }

    @Override
    public String getCodeSample(@NotNull SettingsType settingsType)
    {
        return ""
                + "enumeration Status\n"
                + "{\n"
                + "    OPEN,\n"
                + "    ON_HOLD,\n"
                + "    CLOSED,\n"
                + "}\n"
                + "\n"
                + "class Question\n"
                + "{\n"
                + "    title : String\n"
                + "    body  : String\n"
                + "    status: Status\n"
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

    @Nullable
    @Override
    public PsiFile createFileFromText(Project project, String text)
    {
        return PsiFileFactory.getInstance(project)
                .createFileFromText(
                        "sample.klass",
                        KlassFileType.INSTANCE,
                        text,
                        LocalTimeCounter.currentTime(),
                        false,
                        false);
    }
}
