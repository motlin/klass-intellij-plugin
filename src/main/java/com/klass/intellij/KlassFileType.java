package com.klass.intellij;

import com.intellij.openapi.fileTypes.LanguageFileType;
import javax.swing.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class KlassFileType extends LanguageFileType {
  public static final KlassFileType INSTANCE = new KlassFileType();

  private KlassFileType() {
    super(KlassLanguage.INSTANCE);
  }

  @NotNull @Override
  public String getName() {
    return "Klass file";
  }

  @NotNull @Override
  public String getDescription() {
    return "Klass language file";
  }

  @NotNull @Override
  public String getDefaultExtension() {
    return "klass";
  }

  @Nullable @Override
  public Icon getIcon() {
    return KlassIcons.FILE;
  }
}
