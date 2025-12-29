package com.klass.intellij.formatter;

import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CustomCodeStyleSettings;

public class KlassCodeStyleSettings extends CustomCodeStyleSettings {
  public KlassCodeStyleSettings(CodeStyleSettings settings) {
    super("KlassCodeStyleSettings", settings);
  }
}
