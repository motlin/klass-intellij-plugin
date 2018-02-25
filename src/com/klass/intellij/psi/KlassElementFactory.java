package com.klass.intellij.psi;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFileFactory;
import com.klass.intellij.KlassFileType;

public class KlassElementFactory
{
    public static KlassClass createClass(Project project, String name)
    {
        KlassFile file = KlassElementFactory.createFile(project, String.format("class %s {}", name));
        return (KlassClass) file.getFirstChild();
    }

    public static KlassAssociation createAssociation(Project project, String name)
    {
        KlassFile file = KlassElementFactory.createFile(project, String.format("association %s {}", name));
        return (KlassAssociation) file.getFirstChild();
    }

    public static KlassFile createFile(Project project, String text)
    {
        String name = "dummy.klass";
        return (KlassFile) PsiFileFactory.getInstance(project).
                createFileFromText(name, KlassFileType.INSTANCE, text);
    }
}
