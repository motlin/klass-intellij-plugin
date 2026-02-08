package com.klass.intellij.highlighter;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.psi.PsiElement;
import com.klass.intellij.psi.KlassNombre;
import com.klass.intellij.psi.KlassUrlConstant;
import com.klass.intellij.psi.KlassVisitor;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.jetbrains.annotations.NotNull;

public class ReservedNameVisitor extends KlassVisitor {
  protected static final ImmutableList<String> JAVA_KEYWORDS =
      Lists.immutable.with(
          "abstract",
          "assert",
          "boolean",
          "break",
          "byte",
          "case",
          "catch",
          "char",
          "class",
          "const",
          "continue",
          "default",
          "do",
          "double",
          "else",
          "enum",
          "extends",
          "final",
          "finally",
          "float",
          "for",
          "goto",
          "if",
          "implements",
          "import",
          "instanceof",
          "int",
          "interface",
          "long",
          "native",
          "new",
          "package",
          "private",
          "protected",
          "public",
          "return",
          "short",
          "static",
          "strictfp",
          "super",
          "switch",
          "synchronized",
          "this",
          "throw",
          "throws",
          "transient",
          "try",
          "var",
          "void",
          "volatile",
          "while");

  protected static final ImmutableList<String> JAVA_LITERALS =
      Lists.immutable.with("true", "false", "null");

  private final AnnotationHolder annotationHolder;

  public ReservedNameVisitor(AnnotationHolder annotationHolder) {
    this.annotationHolder = annotationHolder;
  }

  @Override
  public void visitNombre(@NotNull KlassNombre nombre) {
    PsiElement parent = nombre.getParent();
    if (parent instanceof KlassUrlConstant) {
      return;
    }
    if (JAVA_KEYWORDS.contains(nombre.getText())) {
      String message = "Reserved Java keyword";
      this.annotationHolder.newAnnotation(HighlightSeverity.ERROR, message).range(nombre).create();
    } else if (JAVA_LITERALS.contains(nombre.getText())) {
      String message = "Reserved Java literal";
      this.annotationHolder.newAnnotation(HighlightSeverity.ERROR, message).range(nombre).create();
    }
  }
}
