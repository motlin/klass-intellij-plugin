package com.klass.intellij;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.CachedValue;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiModificationTracker;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

public class KlassUtil {
  private static final Key<CachedValue<ConcurrentHashMap<Class<?>, List<?>>>>
      ALL_ELEMENTS_CACHE_KEY = Key.create("klass.util.allElements");

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

  // Module-scoped: for references, restricts to current module and its dependencies.
  public static <T extends PsiElement> List<T> findElementsOfType(
      @NotNull PsiElement context, @NotNull Class<T> klass) {
    GlobalSearchScope scope = getModuleScope(context);
    return filterByScope(getAllElementsOfType(context.getProject(), klass), scope);
  }

  // Project-scoped: for navigation (Go to Class/Symbol), searches all modules.
  public static <T extends PsiElement> List<T> findElementsOfType(
      @NotNull Project project, @NotNull Class<T> klass) {
    return getAllElementsOfType(project, klass);
  }

  @SuppressWarnings("unchecked")
  private static <T extends PsiElement> List<T> getAllElementsOfType(
      @NotNull Project project, @NotNull Class<T> klass) {
    ConcurrentHashMap<Class<?>, List<?>> cache =
        CachedValuesManager.getManager(project)
            .getCachedValue(
                project,
                ALL_ELEMENTS_CACHE_KEY,
                () ->
                    CachedValueProvider.Result.create(
                        new ConcurrentHashMap<>(), PsiModificationTracker.MODIFICATION_COUNT),
                false);
    return (List<T>) cache.computeIfAbsent(klass, k -> scanAllElementsOfType(project, klass));
  }

  private static <T extends PsiElement> List<T> filterByScope(
      @NotNull List<T> elements, @NotNull GlobalSearchScope scope) {
    return elements.stream()
        .filter(
            element -> {
              VirtualFile vf = element.getContainingFile().getVirtualFile();
              return vf != null && scope.contains(vf);
            })
        .collect(Collectors.toList());
  }

  private static <T extends PsiElement> List<T> scanAllElementsOfType(
      @NotNull Project project, @NotNull Class<T> klass) {
    PsiManager psiManager = PsiManager.getInstance(project);

    Collection<VirtualFile> virtualFiles =
        FilenameIndex.getAllFilesByExt(project, "klass", GlobalSearchScope.allScope(project));

    List<T> result = new ArrayList<>();

    virtualFiles.stream()
        .map(psiManager::findFile)
        .filter(KlassFile.class::isInstance)
        .map(KlassFile.class::cast)
        .map(klassFile -> PsiTreeUtil.getChildrenOfType(klassFile, klass))
        .filter(Objects::nonNull)
        .forEach(classes -> Collections.addAll(result, classes));

    return Collections.unmodifiableList(result);
  }
}
