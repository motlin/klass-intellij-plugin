package com.klass.intellij.navigation;

import com.intellij.openapi.project.Project;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class KlassChooseByNameClassContributor extends AbstractKlassChooseByNameContributor {
  @NotNull @Override
  public String[] getNames(Project project, boolean includeNonProjectItems) {
    List<String> names = new ArrayList<>();

    this.addClassNames(project, names);
    this.addAssociationNames(project, names);

    return names.toArray(new String[names.size()]);
  }
}
