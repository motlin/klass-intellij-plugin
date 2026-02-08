package com.klass.intellij.structure;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiFile;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import com.klass.intellij.psi.KlassAssociation;
import com.klass.intellij.psi.KlassAssociationEnd;
import com.klass.intellij.psi.KlassEnumeration;
import com.klass.intellij.psi.KlassEnumerationLiteral;
import com.klass.intellij.psi.KlassInterface;
import com.klass.intellij.psi.KlassKlass;
import com.klass.intellij.psi.KlassPrimitiveTypeProperty;
import com.klass.intellij.psi.KlassProjection;
import com.klass.intellij.psi.KlassServiceGroup;
import com.klass.intellij.psi.KlassUrlGroup;

public class KlassStructureViewTest extends BasePlatformTestCase {
  @Override
  protected String getTestDataPath() {
    return "src/test/testData";
  }

  public void testStructureViewRendersWithoutErrors() {
    myFixture.configureByFile("Example.klass");
    myFixture.testStructureView(
        structureViewComponent -> {
          StructureViewTreeElement root = structureViewComponent.getTreeModel().getRoot();
          assertNotNull("Root element should not be null", root);
        });
  }

  public void testRootFilePresentation() {
    PsiFile psiFile = myFixture.configureByFile("Example.klass");
    KlassStructureViewElement root = new KlassStructureViewElement(psiFile);

    ItemPresentation presentation = root.getPresentation();
    assertNotNull("Root file presentation should not be null", presentation);
    assertEquals("Example.klass", presentation.getPresentableText());
  }

  public void testRootChildrenAreTopLevelItems() {
    PsiFile psiFile = myFixture.configureByFile("Example.klass");
    KlassStructureViewElement root = new KlassStructureViewElement(psiFile);

    TreeElement[] children = root.getChildren();
    assertEquals("Example.klass should have 6 top-level items", 6, children.length);
  }

  public void testInterfaceElement() {
    PsiFile psiFile = myFixture.configureByFile("Example.klass");
    KlassStructureViewElement root = new KlassStructureViewElement(psiFile);
    TreeElement[] children = root.getChildren();

    // First top-level item is the Named interface
    StructureViewTreeElement interfaceElement = (StructureViewTreeElement) children[0];
    assertInstanceOf(interfaceElement.getValue(), KlassInterface.class);

    ItemPresentation presentation = interfaceElement.getPresentation();
    assertNotNull("Interface presentation should not be null", presentation);
    assertEquals("Named", presentation.getPresentableText());
  }

  public void testInterfaceMemberChildren() {
    PsiFile psiFile = myFixture.configureByFile("Example.klass");
    KlassStructureViewElement root = new KlassStructureViewElement(psiFile);
    TreeElement[] children = root.getChildren();

    // Named interface has 1 member property: name
    TreeElement[] interfaceMembers = children[0].getChildren();
    assertEquals("Named interface should have 1 member", 1, interfaceMembers.length);

    StructureViewTreeElement memberElement = (StructureViewTreeElement) interfaceMembers[0];
    assertInstanceOf(memberElement.getValue(), KlassPrimitiveTypeProperty.class);

    ItemPresentation memberPresentation = memberElement.getPresentation();
    assertNotNull("Member presentation should not be null", memberPresentation);
    assertEquals("name", memberPresentation.getPresentableText());
  }

  public void testClassElement() {
    PsiFile psiFile = myFixture.configureByFile("Example.klass");
    KlassStructureViewElement root = new KlassStructureViewElement(psiFile);
    TreeElement[] children = root.getChildren();

    // Second top-level item is the User class
    StructureViewTreeElement classElement = (StructureViewTreeElement) children[1];
    assertInstanceOf(classElement.getValue(), KlassKlass.class);

    ItemPresentation presentation = classElement.getPresentation();
    assertNotNull("Class presentation should not be null", presentation);
    assertEquals("User", presentation.getPresentableText());
  }

  public void testClassMemberChildren() {
    PsiFile psiFile = myFixture.configureByFile("Example.klass");
    KlassStructureViewElement root = new KlassStructureViewElement(psiFile);
    TreeElement[] children = root.getChildren();

    // User class has 3 members: name, email, age
    TreeElement[] classMembers = children[1].getChildren();
    assertEquals("User class should have 3 members", 3, classMembers.length);

    StructureViewTreeElement nameElement = (StructureViewTreeElement) classMembers[0];
    assertEquals("name", nameElement.getPresentation().getPresentableText());

    StructureViewTreeElement emailElement = (StructureViewTreeElement) classMembers[1];
    assertEquals("email", emailElement.getPresentation().getPresentableText());

    StructureViewTreeElement ageElement = (StructureViewTreeElement) classMembers[2];
    assertEquals("age", ageElement.getPresentation().getPresentableText());
  }

  public void testEnumerationElement() {
    PsiFile psiFile = myFixture.configureByFile("Example.klass");
    KlassStructureViewElement root = new KlassStructureViewElement(psiFile);
    TreeElement[] children = root.getChildren();

    // Third top-level item is the Status enumeration
    StructureViewTreeElement enumerationElement = (StructureViewTreeElement) children[2];
    assertInstanceOf(enumerationElement.getValue(), KlassEnumeration.class);

    ItemPresentation presentation = enumerationElement.getPresentation();
    assertNotNull("Enumeration presentation should not be null", presentation);
    assertEquals("Status", presentation.getPresentableText());
  }

  public void testEnumerationLiteralChildren() {
    PsiFile psiFile = myFixture.configureByFile("Example.klass");
    KlassStructureViewElement root = new KlassStructureViewElement(psiFile);
    TreeElement[] children = root.getChildren();

    // Status enumeration has 2 literals: ACTIVE, INACTIVE
    TreeElement[] enumLiterals = children[2].getChildren();
    assertEquals("Status enumeration should have 2 literals", 2, enumLiterals.length);

    StructureViewTreeElement activeElement = (StructureViewTreeElement) enumLiterals[0];
    assertInstanceOf(activeElement.getValue(), KlassEnumerationLiteral.class);
    assertEquals("ACTIVE", activeElement.getPresentation().getPresentableText());

    StructureViewTreeElement inactiveElement = (StructureViewTreeElement) enumLiterals[1];
    assertInstanceOf(inactiveElement.getValue(), KlassEnumerationLiteral.class);
    assertEquals("INACTIVE", inactiveElement.getPresentation().getPresentableText());
  }

  public void testAssociationElement() {
    PsiFile psiFile = myFixture.configureByFile("Example.klass");
    KlassStructureViewElement root = new KlassStructureViewElement(psiFile);
    TreeElement[] children = root.getChildren();

    // Fourth top-level item is the UserStatus association
    StructureViewTreeElement associationElement = (StructureViewTreeElement) children[3];
    assertInstanceOf(associationElement.getValue(), KlassAssociation.class);

    ItemPresentation presentation = associationElement.getPresentation();
    assertNotNull("Association presentation should not be null", presentation);
    assertEquals("UserStatus", presentation.getPresentableText());
  }

  public void testAssociationEndChildren() {
    PsiFile psiFile = myFixture.configureByFile("Example.klass");
    KlassStructureViewElement root = new KlassStructureViewElement(psiFile);
    TreeElement[] children = root.getChildren();

    // UserStatus association has 2 association ends: user, status
    TreeElement[] associationEnds = children[3].getChildren();
    assertEquals("UserStatus association should have 2 ends", 2, associationEnds.length);

    StructureViewTreeElement userEnd = (StructureViewTreeElement) associationEnds[0];
    assertInstanceOf(userEnd.getValue(), KlassAssociationEnd.class);
    assertEquals("user", userEnd.getPresentation().getPresentableText());

    StructureViewTreeElement statusEnd = (StructureViewTreeElement) associationEnds[1];
    assertInstanceOf(statusEnd.getValue(), KlassAssociationEnd.class);
    assertEquals("status", statusEnd.getPresentation().getPresentableText());
  }

  public void testProjectionElement() {
    PsiFile psiFile = myFixture.configureByFile("Example.klass");
    KlassStructureViewElement root = new KlassStructureViewElement(psiFile);
    TreeElement[] children = root.getChildren();

    // Fifth top-level item is the UserProjection projection
    StructureViewTreeElement projectionElement = (StructureViewTreeElement) children[4];
    assertInstanceOf(projectionElement.getValue(), KlassProjection.class);

    ItemPresentation presentation = projectionElement.getPresentation();
    assertNotNull("Projection presentation should not be null", presentation);
    assertEquals("UserProjection", presentation.getPresentableText());
    assertEquals("on User", presentation.getLocationString());
  }

  public void testServiceGroupElement() {
    PsiFile psiFile = myFixture.configureByFile("Example.klass");
    KlassStructureViewElement root = new KlassStructureViewElement(psiFile);
    TreeElement[] children = root.getChildren();

    // Sixth top-level item is the UserService service group
    StructureViewTreeElement serviceGroupElement = (StructureViewTreeElement) children[5];
    assertInstanceOf(serviceGroupElement.getValue(), KlassServiceGroup.class);

    ItemPresentation presentation = serviceGroupElement.getPresentation();
    assertNotNull("ServiceGroup presentation should not be null", presentation);
    assertEquals("User", presentation.getPresentableText());
  }

  public void testServiceGroupUrlGroupChildren() {
    PsiFile psiFile = myFixture.configureByFile("Example.klass");
    KlassStructureViewElement root = new KlassStructureViewElement(psiFile);
    TreeElement[] children = root.getChildren();

    // UserService has 1 url group: /user
    TreeElement[] urlGroups = children[5].getChildren();
    assertEquals("UserService should have 1 url group", 1, urlGroups.length);

    StructureViewTreeElement urlGroupElement = (StructureViewTreeElement) urlGroups[0];
    assertInstanceOf(urlGroupElement.getValue(), KlassUrlGroup.class);

    ItemPresentation urlGroupPresentation = urlGroupElement.getPresentation();
    assertNotNull("UrlGroup presentation should not be null", urlGroupPresentation);
  }

  public void testServiceWithinUrlGroup() {
    PsiFile psiFile = myFixture.configureByFile("Example.klass");
    KlassStructureViewElement root = new KlassStructureViewElement(psiFile);
    TreeElement[] children = root.getChildren();

    // The url group /user has 1 service: GET
    TreeElement[] urlGroups = children[5].getChildren();
    TreeElement[] services = urlGroups[0].getChildren();
    assertEquals("Url group /user should have 1 service", 1, services.length);

    StructureViewTreeElement serviceElement = (StructureViewTreeElement) services[0];
    ItemPresentation servicePresentation = serviceElement.getPresentation();
    assertNotNull("Service presentation should not be null", servicePresentation);
    assertEquals("GET", servicePresentation.getPresentableText());
  }

  public void testAllTopLevelPresentationsNonNull() {
    PsiFile psiFile = myFixture.configureByFile("Example.klass");
    KlassStructureViewElement root = new KlassStructureViewElement(psiFile);

    TreeElement[] children = root.getChildren();
    for (int i = 0; i < children.length; i++) {
      StructureViewTreeElement element = (StructureViewTreeElement) children[i];
      ItemPresentation presentation = element.getPresentation();
      assertNotNull(
          "Presentation should not be null for top-level item at index " + i, presentation);
      assertNotNull(
          "Presentable text should not be null for top-level item at index " + i,
          presentation.getPresentableText());
    }
  }

  public void testLeafElementsHaveNoChildren() {
    PsiFile psiFile = myFixture.configureByFile("Example.klass");
    KlassStructureViewElement root = new KlassStructureViewElement(psiFile);
    TreeElement[] children = root.getChildren();

    // Projection has no children in the structure view (leaf in current implementation)
    TreeElement[] projectionChildren = children[4].getChildren();
    assertEquals("Projection should have no children", 0, projectionChildren.length);

    // Check that enumeration literals are leaves
    TreeElement[] enumLiterals = children[2].getChildren();
    for (TreeElement literal : enumLiterals) {
      assertEquals("Enumeration literal should have no children", 0, literal.getChildren().length);
    }

    // Check that association ends are leaves
    TreeElement[] associationEnds = children[3].getChildren();
    for (TreeElement end : associationEnds) {
      assertEquals("Association end should have no children", 0, end.getChildren().length);
    }
  }
}
