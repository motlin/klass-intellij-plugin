package com.klass.intellij;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.klass.intellij.psi.KlassAssociation;
import com.klass.intellij.psi.KlassAssociationEnd;
import com.klass.intellij.psi.KlassEnumeration;
import com.klass.intellij.psi.KlassFile;
import com.klass.intellij.psi.KlassInterface;
import com.klass.intellij.psi.KlassKlass;
import com.klass.intellij.psi.KlassProjection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

public class KlassUtil {
  @NotNull private static GlobalSearchScope getModuleScope(@NotNull PsiElement context) {
    Module module = ModuleUtilCore.findModuleForPsiElement(context);
    if (module != null) {
      return module.getModuleContentWithDependenciesScope();
    }
    return GlobalSearchScope.allScope(context.getProject());
  }

  public static List<KlassInterface> findInterfaces(@NotNull PsiElement context) {
    return findElementsOfType(context, KlassInterface.class);
  }

  public static List<KlassKlass> findClasses(@NotNull PsiElement context) {
    return findElementsOfType(context, KlassKlass.class);
  }

  public static List<KlassAssociation> findAssociations(@NotNull PsiElement context) {
    return findElementsOfType(context, KlassAssociation.class);
  }

  public static List<KlassAssociationEnd> findAssociationEnds(@NotNull PsiElement context) {
    return findElementsOfType(context, KlassAssociationEnd.class);
  }

  public static List<KlassEnumeration> findEnumerations(@NotNull PsiElement context) {
    return findElementsOfType(context, KlassEnumeration.class);
  }

  public static List<KlassProjection> findProjections(@NotNull PsiElement context) {
    return findElementsOfType(context, KlassProjection.class);
  }

  public static List<KlassInterface> findInterfaces(@NotNull Project project) {
    return findElementsOfType(project, KlassInterface.class);
  }

  public static List<KlassKlass> findClasses(@NotNull Project project) {
    return findElementsOfType(project, KlassKlass.class);
  }

  public static List<KlassAssociation> findAssociations(@NotNull Project project) {
    return findElementsOfType(project, KlassAssociation.class);
  }

  public static List<KlassAssociationEnd> findAssociationEnds(@NotNull Project project) {
    return findElementsOfType(project, KlassAssociationEnd.class);
  }

  public static List<KlassEnumeration> findEnumerations(@NotNull Project project) {
    return findElementsOfType(project, KlassEnumeration.class);
  }

  public static List<KlassProjection> findProjections(@NotNull Project project) {
    return findElementsOfType(project, KlassProjection.class);
  }

  public static <T extends PsiElement> List<T> findElementsOfType(
      @NotNull PsiElement context, @NotNull Class<T> klass) {
    return findElementsOfType(context.getProject(), klass, getModuleScope(context));
  }

  public static <T extends PsiElement> List<T> findElementsOfType(
      @NotNull Project project, @NotNull Class<T> klass) {
    return findElementsOfType(project, klass, GlobalSearchScope.allScope(project));
  }

  private static <T extends PsiElement> List<T> findElementsOfType(
      @NotNull Project project, @NotNull Class<T> klass, @NotNull GlobalSearchScope scope) {
    PsiManager psiManager = PsiManager.getInstance(project);

    Collection<VirtualFile> virtualFiles = FilenameIndex.getAllFilesByExt(project, "klass", scope);

    List<T> result = new ArrayList<>();

    virtualFiles.stream()
        .map(psiManager::findFile)
        .filter(KlassFile.class::isInstance)
        .map(KlassFile.class::cast)
        .map(klassFile -> PsiTreeUtil.getChildrenOfType(klassFile, klass))
        .filter(Objects::nonNull)
        .forEach(classes -> Collections.addAll(result, classes));

    return result;
  }
}
