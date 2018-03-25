package com.klass.intellij.psi;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFileFactory;
import com.klass.intellij.KlassFileType;

public class KlassElementFactory
{
    public static KlassKlass createClass(Project project, String name)
    {
        KlassFile file = KlassElementFactory.createFile(project, String.format("class %s {}", name));
        return (KlassKlass) file.getFirstChild();
    }

    public static KlassEnumeration createEnumeration(Project project, String name)
    {
        KlassFile file = KlassElementFactory.createFile(project, String.format("enumeration %s {}", name));
        return (KlassEnumeration) file.getFirstChild();
    }

    public static KlassProjection createProjection(Project project, String name)
    {
        KlassFile file = KlassElementFactory.createFile(project, String.format("projection %s(Dummy) {}", name));
        return (KlassProjection) file.getFirstChild();
    }

    public static KlassAssociation createAssociation(Project project, String name)
    {
        KlassFile file = KlassElementFactory.createFile(project, String.format("association %s {}", name));
        return (KlassAssociation) file.getFirstChild();
    }

    public static KlassDataTypeProperty createDataTypeProperty(Project project, String name)
    {
        KlassFile file = KlassElementFactory.createFile(
                project,
                "class DummyClass\n"
                        + "{\n"
                        + "  " + name + ": String\n"
                        + "}\n");
        return (KlassDataTypeProperty) ((KlassKlass) file.getFirstChild()).getMemberList().get(0);
    }

    public static KlassEnumerationProperty createEnumerationProperty(Project project, String name)
    {
        KlassFile file = KlassElementFactory.createFile(
                project,
                "class DummyClass\n"
                        + "{\n"
                        + "  " + name + ": Status\n"
                        + "}\n");
        return (KlassEnumerationProperty) ((KlassKlass) file.getFirstChild()).getMemberList().get(0);
    }

    public static KlassParameterizedProperty createParameterizedProperty(Project project, String name)
    {
        KlassFile file = KlassElementFactory.createFile(
                project,
                "class DummyClass\n"
                        + "{\n"
                        + "  " + name + "(): DummyClass[1..1]\n"
                        + "    {\n"
                        + "        this.id == DummyClass.id\n"
                        + "    }\n"
                        + "}\n");
        return (KlassParameterizedProperty) ((KlassKlass) file.getFirstChild()).getMemberList().get(0);
    }

    public static KlassEnumerationType createEnumerationType(Project project, String name)
    {
        KlassFile file = KlassElementFactory.createFile(
                project,
                "class DummyClass\n"
                        + "{\n"
                        + "  dummyProperty: " + name + "\n"
                        + "}\n");
        return (KlassEnumerationType) ((KlassKlass) file.getFirstChild()).getMemberList().get(0);
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
        return ((KlassAssociation) file.getFirstChild()).getAssociationEndList().get(0);
    }

    public static KlassKlassName createAssociationEndType(Project project, String name)
    {
        KlassFile file = KlassElementFactory.createFile(
                project,
                "association DummyAssociation\n"
                        + "{\n"
                        + "  source: " + name + "[0..1]\n"
                        + "  target: " + name + "[0..*]\n"
                        + "}\n");
        return ((KlassAssociation) file.getFirstChild()).getAssociationEndList().get(0).getKlassName();
    }

    public static KlassParameterDeclaration createParameterDeclaration(Project project, String name)
    {
        KlassFile file = KlassElementFactory.createFile(
                project,
                "class Dummy\n"
                        + "{\n"
                        + "    " + name + "(): Dummy[0..*]\n"
                        + "    {\n"
                        + "        this.id == Dummy.id\n"
                        + "    }\n"
                        + "}\n");
        return (KlassParameterDeclaration) file.getFirstChild().getFirstChild();
    }

    public static KlassFile createFile(Project project, String text)
    {
        String name = "dummy.klass";
        return (KlassFile) PsiFileFactory.getInstance(project).
                createFileFromText(name, KlassFileType.INSTANCE, text);
    }
}
