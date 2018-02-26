package com.klass.intellij.navigation;

import com.intellij.navigation.ChooseByNameContributor;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.project.Project;
import com.klass.intellij.KlassUtil;
import com.klass.intellij.psi.KlassAssociation;
import com.klass.intellij.psi.KlassAssociationEnd;
import com.klass.intellij.psi.KlassClass;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class KlassChooseByNameContributor implements ChooseByNameContributor
{
    @NotNull
    @Override
    public String[] getNames(Project project, boolean includeNonProjectItems)
    {
        List<String> names = new ArrayList<>();

        KlassUtil.findClasses(project).stream()
                .map(KlassClass::getName)
                .filter(Objects::nonNull)
                .filter(name -> !name.isEmpty())
                .forEachOrdered(names::add);

        KlassUtil.findAssociations(project).stream()
                .map(KlassAssociation::getName)
                .filter(Objects::nonNull)
                .filter(name -> !name.isEmpty())
                .forEachOrdered(names::add);

        KlassUtil.findAssociationEnds(project).stream()
                .map(KlassAssociationEnd::getName)
                .filter(Objects::nonNull)
                .filter(name -> !name.isEmpty())
                .forEachOrdered(names::add);

        return names.toArray(new String[names.size()]);
    }

    @NotNull
    @Override
    public NavigationItem[] getItemsByName(String name, String pattern, Project project, boolean includeNonProjectItems)
    {
        // todo include non project items
        List<NavigationItem> navigationItems = new ArrayList<>();
        navigationItems.addAll(KlassUtil.findClasses(project));
        navigationItems.addAll(KlassUtil.findAssociations(project));
        navigationItems.addAll(KlassUtil.findAssociationEnds(project));
        return navigationItems.toArray(new NavigationItem[]{});
    }
}
