package com.klass.intellij;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.indexing.FileBasedIndex;
import com.klass.intellij.psi.*;

import java.util.*;

public class KlassUtil
{
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
