package com.klass.intellij.navigation;

import com.intellij.openapi.project.Project;
import com.klass.intellij.KlassUtil;
import com.klass.intellij.psi.KlassAssociationEnd;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

public class KlassChooseByNameSymbolContributor extends AbstractKlassChooseByNameContributor {
  @NotNull @Override
  public String[] getNames(Project project, boolean includeNonProjectItems) {
    List<String> names = new ArrayList<>();

    this.addClassNames(project, names);
    this.addAssociationNames(project, names);
    this.addAssociationEndNames(project, names);

    return names.toArray(new String[names.size()]);
  }

  protected void addAssociationEndNames(Project project, List<String> names) {
    KlassUtil.findAssociationEnds(project).stream()
        .map(KlassAssociationEnd::getName)
        .filter(Objects::nonNull)
        .filter(name -> !name.isEmpty())
        .forEachOrdered(names::add);
  }
}
