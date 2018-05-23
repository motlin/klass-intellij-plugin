package com.klass.intellij.psi;

import java.util.List;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFileFactory;
import com.klass.intellij.KlassFileType;

public class KlassElementFactory
{
    public static KlassKlass createClass(Project project, String name)
    {
        KlassFile file = KlassElementFactory.createFile(
                project,
                ""
                        + "package dummy\n"
                        + "\n"
                        + "class " + name + "\n"
                        + "{\n"
                        + "}");
        return file.findChildByClass(KlassKlass.class);
    }

    public static KlassFile createFile(Project project, String text)
    {
        String name = "dummy.klass";
        return (KlassFile) PsiFileFactory.getInstance(project)
                .createFileFromText(name, KlassFileType.INSTANCE, text);
    }

    public static KlassEnumeration createEnumeration(Project project, String name)
    {
        KlassFile file = KlassElementFactory.createFile(
                project,
                "package dummy\n"
                        + "\n"
                        + "enumeration " + name + "\n"
                        + "{\n"
                        + "    DUMMY,\n"
                        + "}");
        return file.findChildByClass(KlassEnumeration.class);
    }

    public static KlassProjection createProjection(Project project, String name)
    {
        KlassFile file = KlassElementFactory.createFile(
                project,
                "package dummy\n"
                        + "\n"
                        + "projection " + name + " on Dummy\n"
                        + "{\n"
                        + "}");
        return file.findChildByClass(KlassProjection.class);
    }

    public static KlassAssociation createAssociation(Project project, String name)
    {
        KlassFile file = KlassElementFactory.createFile(
                project,
                ""
                        + "package dummy\n"
                        + "\n"
                        + "association " + name + "\n"
                        + "{\n"
                        + "    firstSide: Dummy[1..1];\n"
                        + "    secondSide: Dummy[0..*];\n"
                        + "}");
        return file.findChildByClass(KlassAssociation.class);
    }

    public static KlassPrimitiveTypeProperty createPrimitiveTypeProperty(Project project, String name)
    {
        KlassFile file = KlassElementFactory.createFile(
                project,
                "package dummy\n"
                        + "\n"
                        + "class DummyClass\n"
                        + "{\n"
                        + "  " + name + ": String;\n"
                        + "}\n");
        KlassKlass        klassKlass = file.findChildByClass(KlassKlass.class);
        List<KlassMember> memberList = klassKlass.getMemberList();
        return (KlassPrimitiveTypeProperty) memberList.get(0);
    }

    public static KlassEnumerationProperty createEnumerationProperty(Project project, String name)
    {
        KlassFile file = KlassElementFactory.createFile(
                project,
                "package dummy\n"
                        + "\n"
                        + "class DummyClass\n"
                        + "{\n"
                        + "  " + name + ": Status;\n"
                        + "}\n");
        KlassKlass klassKlass = file.findChildByClass(KlassKlass.class);
        return (KlassEnumerationProperty) klassKlass.getMemberList().get(0);
    }

    public static KlassParameterizedProperty createParameterizedProperty(Project project, String name)
    {
        KlassFile file = KlassElementFactory.createFile(
                project,
                "package dummy\n"
                        + "\n"
                        + "class DummyClass\n"
                        + "{\n"
                        + "  " + name + "(): DummyClass[1..1]\n"
                        + "    {\n"
                        + "        this.id == DummyClass.id\n"
                        + "    }\n"
                        + "}\n");
        KlassKlass klassKlass = file.findChildByClass(KlassKlass.class);
        return (KlassParameterizedProperty) klassKlass.getMemberList().get(0);
    }

    public static KlassAssociationEnd createAssociationEnd(Project project, String name)
    {
        KlassFile file = KlassElementFactory.createFile(
                project,
                "package dummy\n"
                        + "\n"
                        + "association DummyAssociation\n"
                        + "{\n"
                        + "  " + name + ": DummyType[0..1];\n"
                        + "  target: DummyType[0..*];\n"
                        + "}\n");
        KlassAssociation klassAssociation = file.findChildByClass(KlassAssociation.class);
        return klassAssociation.getAssociationEndList().get(0);
    }

    public static KlassAssociationEnd createAssociationEndType(Project project, String name)
    {
        KlassFile file = KlassElementFactory.createFile(
                project,
                "package dummy\n"
                        + "\n"
                        + "association DummyAssociation\n"
                        + "{\n"
                        + "  source: " + name + "[0..1];\n"
                        + "  target: " + name + "[0..*];\n"
                        + "\n"
                        + "  relationship: this.id == " + name + ".sourceId\n"
                        + "}\n");
        KlassAssociation klassAssociation = file.findChildByClass(KlassAssociation.class);
        return klassAssociation.getAssociationEndList().get(0);
    }

    public static KlassParameterDeclaration createParameterDeclaration(Project project, String name)
    {
        KlassFile file = KlassElementFactory.createFile(
                project,
                "package dummy\n"
                        + "\n"
                        + "class Dummy\n"
                        + "{\n"
                        + "    dummyProperty(" + name + ": String[1..1]): Dummy[0..*]\n"
                        + "    {\n"
                        + "        this.id == Dummy.id\n"
                        + "    }\n"
                        + "}\n");
        KlassKlass                 klassKlass            = file.findChildByClass(KlassKlass.class);
        KlassParameterizedProperty parameterizedProperty = (KlassParameterizedProperty) klassKlass.getMemberList().get(0);
        return parameterizedProperty.getParameterDeclarationList().get(0);
    }

    // TODO: Try running all the rename refactorings
    public static KlassEnumerationLiteral createEnumerationLiteral(Project project, String name)
    {
        KlassFile file = KlassElementFactory.createFile(
                project,
                "package dummy\n"
                        + "\n"
                        + "enumeration DummyEnumeration\n"
                        + "{\n"
                        + "    " + name + ",\n"
                        + "}");
        KlassEnumeration klassEnumeration = file.findChildByClass(KlassEnumeration.class);
        return klassEnumeration.getEnumerationLiteralList().get(0);
    }

    public static KlassMemberName createPropertyName(Project project, String newElementName)
    {
        KlassFile file = KlassElementFactory.createFile(
                project,
                "package dummy\n"
                        + "\n"
                        + "projection DummyProjection on DummyClass\n"
                        + "{\n"
                        + "  " + newElementName + ": \"Dummy Header\",\n"
                        + "}\n");
        KlassProjection klassProjection = file.findChildByClass(KlassProjection.class);
        KlassProjectionLeafNode projectionLeafNode = (KlassProjectionLeafNode) klassProjection.getProjectionNodeList().get(
                0);
        return projectionLeafNode.getMemberName();
    }

    public static KlassProjectionName createProjectionName(Project project, String newElementName)
    {
        KlassFile file = KlassElementFactory.createFile(
                project,
                "package dummy\n"
                        + "\n"
                        + "class Dummy\n"
                        + "    read(" + newElementName + ")\n"
                        + "{\n"
                        + "}\n");

        return file.findChildByClass(KlassKlass.class)
                .getServiceProjectionList()
                .get(0)
                .getProjectionName();
    }

    public static KlassEnumerationType createEnumerationType(Project project, String name)
    {
        KlassFile file = KlassElementFactory.createFile(
                project,
                "package dummy\n"
                        + "\n"
                        + "class DummyClass\n"
                        + "{\n"
                        + "  dummyProperty: " + name + ";\n"
                        + "}\n");
        KlassKlass               klassKlass               = file.findChildByClass(KlassKlass.class);
        KlassEnumerationProperty klassEnumerationProperty = (KlassEnumerationProperty) klassKlass.getMemberList().get(0);
        return klassEnumerationProperty.getEnumerationType();
    }

    public static KlassKlassName createKlassName(Project project, String newElementName)
    {
        KlassFile file = KlassElementFactory.createFile(
                project,
                "package dummy\n"
                        + "\n"
                        + "projection DummyProjection on " + newElementName + "\n"
                        + "{\n"
                        + "}\n");
        KlassProjection klassProjection = file.findChildByClass(KlassProjection.class);
        return klassProjection.getKlassName();
    }

    public static KlassAssociationEndName createAssociationEndName(Project project, String newElementName)
    {
        KlassFile file = KlassElementFactory.createFile(
                project,
                "package dummy\n"
                        + "\n"
                        + "projection DummyProjection on DummyClass\n"
                        + "{\n"
                        + "    " + newElementName + ":\n"
                        + "    {\n"
                        + "    },\n"
                        + "}\n");
        KlassProjection klassProjection = file.findChildByClass(KlassProjection.class);
        return ((KlassProjectionAssociationEndNode) klassProjection.getProjectionNodeList().get(0)).getAssociationEndName();
    }

    public static KlassParameterizedPropertyName createParameterizedPropertyName(Project project, String newElementName)
    {
        KlassFile file = KlassElementFactory.createFile(
                project,
                "package dummy\n"
                        + "\n"
                        + "projection DummyProjection on DummyClass\n"
                        + "{\n"
                        + "    " + newElementName + "():\n"
                        + "    {\n"
                        + "    },\n"
                        + "}\n");
        KlassProjection klassProjection = file.findChildByClass(KlassProjection.class);
        return ((KlassProjectionParameterizedPropertyNode) klassProjection.getProjectionNodeList().get(0)).getParameterizedPropertyName();
    }

    public static KlassParameterName createParameterName(Project project, String newElementName)
    {
        KlassFile file = KlassElementFactory.createFile(
                project,
                "package dummy\n"
                        + "\n"
                        + "projection DummyProjection on DummyClass\n"
                        + "{\n"
                        + "    dummyParameterizedProperty(" + newElementName + "):\n"
                        + "    {\n"
                        + "    },\n"
                        + "}\n");
        KlassProjection klassProjection = file.findChildByClass(KlassProjection.class);
        KlassProjectionNode klassProjectionNode = klassProjection
                .getProjectionNodeList()
                .get(0);
        return ((KlassProjectionParameterizedPropertyNode) klassProjectionNode)
                .getParameterNameList()
                .get(0);
    }

    public static KlassExpressionVariableName createExpressionVariableName(Project project, String newElementName)
    {
        KlassFile file = KlassElementFactory.createFile(
                project,
                "package dummy\n"
                        + "\n"
                        + "class Dummy\n"
                        + "{\n"
                        + "    dummyProperty(" + newElementName + ": Long[1..1]): Dummy[0..*]\n"
                        + "    {\n"
                        + "        this.id == " + newElementName + "\n"
                        + "    }\n"
                        + "}\n");
        KlassKlass                 klassKlass            = file.findChildByClass(KlassKlass.class);
        KlassParameterizedProperty parameterizedProperty = (KlassParameterizedProperty) klassKlass.getMemberList().get(0);
        KlassCriteriaOr            criteriaOr            = (KlassCriteriaOr) parameterizedProperty.getCriteriaExpression();
        KlassCriteriaOperator      criteriaOperator      = (KlassCriteriaOperator) criteriaOr.getAtomicCriteriaList().get(
                0);
        return (KlassExpressionVariableName) criteriaOperator
                .getTargetExpressionValue()
                .getExpressionValue();
    }
}
