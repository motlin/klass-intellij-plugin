package com.klass.intellij.formatter;

import com.intellij.application.options.CodeStyleAbstractConfigurable;
import com.intellij.application.options.CodeStyleAbstractPanel;
import com.intellij.application.options.TabbedLanguageCodeStylePanel;
import com.intellij.openapi.options.Configurable;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CodeStyleSettingsProvider;
import com.intellij.psi.codeStyle.CustomCodeStyleSettings;
import com.klass.intellij.KlassLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class KlassCodeStyleSettingsProvider extends CodeStyleSettingsProvider
{
    @Override
    public CustomCodeStyleSettings createCustomSettings(CodeStyleSettings settings)
    {
        return new KlassCodeStyleSettings(settings);
    }

    @Nullable
    @Override
    public String getConfigurableDisplayName()
    {
        return "Klass";
    }

    @NotNull
    @Override
    public Configurable createSettingsPage(CodeStyleSettings settings, CodeStyleSettings originalSettings)
    {
        return new CodeStyleAbstractConfigurable(settings, originalSettings, "Klass")
        {
            @Override
            protected CodeStyleAbstractPanel createPanel(CodeStyleSettings settings)
            {
                return new KlassCodeStyleMainPanel(this.getCurrentSettings(), settings);
            }

            @Nullable
            @Override
            public String getHelpTopic()
            {
                return null;
            }
        };
    }

    private static class KlassCodeStyleMainPanel extends TabbedLanguageCodeStylePanel
    {
        private KlassCodeStyleMainPanel(CodeStyleSettings currentSettings, CodeStyleSettings settings)
        {
            super(KlassLanguage.INSTANCE, currentSettings, settings);
        }
    }
}
