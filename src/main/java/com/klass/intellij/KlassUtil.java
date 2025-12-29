package com.klass.intellij;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.indexing.FileBasedIndex;
import com.klass.intellij.psi.KlassAssociation;
import com.klass.intellij.psi.KlassAssociationEnd;
import com.klass.intellij.psi.KlassEnumeration;
import com.klass.intellij.psi.KlassFile;
import com.klass.intellij.psi.KlassInterface;
import com.klass.intellij.psi.KlassKlass;
import com.klass.intellij.psi.KlassProjection;

public class KlassUtil
{
    public static List<KlassInterface> findInterfaces(Project project)
    {
        return KlassUtil.findElementsOfType(project, KlassInterface.class);
    }

    public static List<KlassKlass> findClasses(Project project)
    {
        return KlassUtil.findElementsOfType(project, KlassKlass.class);
    }

    public static List<KlassAssociation> findAssociations(Project project)
    {
        return KlassUtil.findElementsOfType(project, KlassAssociation.class);
    }

    public static List<KlassAssociationEnd> findAssociationEnds(Project project)
    {
        return KlassUtil.findElementsOfType(project, KlassAssociationEnd.class);
    }

    public static List<KlassEnumeration> findEnumerations(Project project)
    {
        return KlassUtil.findElementsOfType(project, KlassEnumeration.class);
    }

    public static List<KlassProjection> findProjections(Project project)
    {
        return KlassUtil.findElementsOfType(project, KlassProjection.class);
    }

    public static <T extends PsiElement> List<T> findElementsOfType(Project project, Class<T> klass)
    {
        PsiManager psiManager = PsiManager.getInstance(project);

        Collection<VirtualFile> virtualFiles = FileBasedIndex.getInstance()
                .getContainingFiles(
                        FileTypeIndex.NAME,
                        KlassFileType.INSTANCE,
                        GlobalSearchScope.allScope(project));

        List<T> result = new ArrayList<>();

        virtualFiles.stream()
                .map(psiManager::findFile)
                .map(KlassFile.class::cast)
                .filter(Objects::nonNull)
                .map(klassFile -> PsiTreeUtil.getChildrenOfType(klassFile, klass))
                .filter(Objects::nonNull)
                .forEach(classes -> Collections.addAll(result, classes));
        return result;
    }
}
