package com.klass.intellij.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.klass.intellij.KlassFileType;
import com.klass.intellij.KlassLanguage;
import javax.swing.*;
import org.jetbrains.annotations.NotNull;

public class KlassFile extends PsiFileBase {
  public KlassFile(@NotNull FileViewProvider viewProvider) {
    super(viewProvider, KlassLanguage.INSTANCE);
  }

  @NotNull @Override
  public FileType getFileType() {
    return KlassFileType.INSTANCE;
  }

  @Override
  public String toString() {
    return "Klass File";
  }

  @Override
  public Icon getIcon(int flags) {
    return super.getIcon(flags);
  }
}
