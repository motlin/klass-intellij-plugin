package com.klass.intellij.psi;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFileFactory;
import com.klass.intellij.KlassFileType;

public class KlassElementFactory
{
    public static KlassKlass createClass(Project project, String name)
    {
        KlassFile file = KlassElementFactory.createFile(project, String.format("class %s {}", name));
        return (KlassKlass) file.getFirstChild().getFirstChild();
    }

    public static KlassEnumeration createEnumeration(Project project, String name)
    {
        KlassFile file = KlassElementFactory.createFile(project, String.format("enumeration %s {}", name));
        return (KlassEnumeration) file.getFirstChild().getFirstChild();
    }

    public static KlassAssociation createAssociation(Project project, String name)
    {
        KlassFile file = KlassElementFactory.createFile(project, String.format("association %s {}", name));
        return (KlassAssociation) file.getFirstChild().getFirstChild();
    }

    public static KlassDataTypeProperty createDataTypeProperty(Project project, String name)
    {
        KlassFile file = KlassElementFactory.createFile(
                project,
                "class DummyClass\n"
                + "{\n"
                + "  " + name + ": String\n"
                + "}\n");
        return ((KlassKlass) file.getFirstChild().getFirstChild()).getPropertyList().get(0).getDataTypeProperty();
    }

    public static KlassEnumerationProperty createEnumerationProperty(Project project, String name)
    {
        KlassFile file = KlassElementFactory.createFile(
                project,
                "class DummyClass\n"
                        + "{\n"
                        + "  " + name + ": Status\n"
                        + "}\n");
        return ((KlassKlass) file.getFirstChild()).getPropertyList().get(0).getEnumerationProperty();
    }

    public static KlassEnumerationType createEnumerationType(Project project, String name)
    {
        KlassFile file = KlassElementFactory.createFile(
                project,
                "class DummyClass\n"
                        + "{\n"
                        + "  dummyProperty: " + name + "\n"
                        + "}\n");
        return ((KlassKlass) file.getFirstChild().getFirstChild()).getPropertyList().get(0).getEnumerationProperty().getEnumerationType();
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
        return ((KlassAssociation) file.getFirstChild().getFirstChild()).getSourceAssociationEnd().getAssociationEnd();
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
        return ((KlassAssociation) file.getFirstChild().getFirstChild()).getSourceAssociationEnd().getAssociationEnd().getAssociationEndType();
    }

    public static KlassFile createFile(Project project, String text)
    {
        String name = "dummy.klass";
        return (KlassFile) PsiFileFactory.getInstance(project).
                createFileFromText(name, KlassFileType.INSTANCE, text);
    }
}
