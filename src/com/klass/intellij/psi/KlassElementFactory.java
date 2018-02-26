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

    public static KlassAssociationEnd createAssociationEnd(Project project, String name)
    {
        KlassFile file = KlassElementFactory.createFile(
                project,
                "association DummyAssociation\n"
                + "{\n"
                + "  " + name + ": DummyType[0..1]\n"
                + "  target: DummyType[0..*]\n"
                + "}\n");
        return ((KlassAssociation) file.getFirstChild()).getSourceAssociationEnd().getAssociationEnd();
    }

    public static KlassAssociationEndType createAssociationEndType(Project project, String name)
    {
        KlassFile file = KlassElementFactory.createFile(
                project,
                "association DummyAssociation\n"
                + "{\n"
                + "  source: " + name + "[0..1]\n"
                + "  target: " + name + "[0..*]\n"
                + "}\n");
        return ((KlassAssociation) file.getFirstChild()).getSourceAssociationEnd().getAssociationEnd().getAssociationEndType();
    }

    public static KlassFile createFile(Project project, String text)
    {
        String name = "dummy.klass";
        return (KlassFile) PsiFileFactory.getInstance(project).
                createFileFromText(name, KlassFileType.INSTANCE, text);
    }
}
