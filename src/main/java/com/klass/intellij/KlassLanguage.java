package com.klass.intellij;

import com.intellij.lang.Language;

public class KlassLanguage extends Language {
  public static final KlassLanguage INSTANCE = new KlassLanguage();

  private KlassLanguage() {
    super("Klass");
  }
}
