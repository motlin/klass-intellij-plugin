package com.klass.intellij.navigation;

import com.intellij.navigation.ChooseByNameContributor;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.project.Project;
import com.klass.intellij.KlassUtil;
import com.klass.intellij.psi.KlassAssociation;
import com.klass.intellij.psi.KlassKlass;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractKlassChooseByNameContributor implements ChooseByNameContributor {
  protected void addAssociationNames(Project project, List<String> names) {
    KlassUtil.findAssociations(project).stream()
        .map(KlassAssociation::getName)
        .filter(Objects::nonNull)
        .filter(name -> !name.isEmpty())
        .forEachOrdered(names::add);
  }

  protected void addClassNames(Project project, List<String> names) {
    KlassUtil.findClasses(project).stream()
        .map(KlassKlass::getName)
        .filter(Objects::nonNull)
        .filter(name -> !name.isEmpty())
        .forEachOrdered(names::add);
  }

  @NotNull @Override
  public NavigationItem[] getItemsByName(
      String name, String pattern, Project project, boolean includeNonProjectItems) {
    // todo include non project items
    List<NavigationItem> navigationItems = new ArrayList<>();
    navigationItems.addAll(KlassUtil.findClasses(project));
    navigationItems.addAll(KlassUtil.findAssociations(project));
    navigationItems.addAll(KlassUtil.findAssociationEnds(project));
    return navigationItems.toArray(new NavigationItem[] {});
  }
}
