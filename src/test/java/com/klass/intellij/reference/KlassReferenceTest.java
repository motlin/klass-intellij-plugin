package com.klass.intellij.reference;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import com.klass.intellij.psi.KlassEnumeration;
import com.klass.intellij.psi.KlassInterface;
import com.klass.intellij.psi.KlassKlass;
import com.klass.intellij.psi.KlassMember;

public class KlassReferenceTest extends BasePlatformTestCase {
  @Override
  protected String getTestDataPath() {
    return "src/test/testData";
  }

  public void testClassNameReferenceResolvesToClassDeclaration() {
    myFixture.configureByFile("ReferenceClassInAssociation.klass");

    PsiReference reference = myFixture.getReferenceAtCaretPositionWithAssertion();
    PsiElement resolved = reference.resolve();
    assertNotNull("Class name reference should resolve to a declaration", resolved);
    assertInstanceOf(resolved, KlassKlass.class);
    assertEquals("User", ((KlassKlass) resolved).getName());
  }

  public void testInterfaceNameReferenceResolvesToInterfaceDeclaration() {
    myFixture.configureByFile("ReferenceInterfaceInImplements.klass");

    PsiReference reference = myFixture.getReferenceAtCaretPositionWithAssertion();
    PsiElement resolved = reference.resolve();
    assertNotNull("Interface name reference should resolve to a declaration", resolved);
    assertInstanceOf(resolved, KlassInterface.class);
    assertEquals("Named", ((KlassInterface) resolved).getName());
  }

  public void testEnumerationTypeReferenceResolvesToEnumerationDeclaration() {
    myFixture.configureByFile("ReferenceEnumerationInProperty.klass");

    PsiReference reference = myFixture.getReferenceAtCaretPositionWithAssertion();
    PsiElement resolved = reference.resolve();
    assertNotNull("Enumeration type reference should resolve to a declaration", resolved);
    assertInstanceOf(resolved, KlassEnumeration.class);
    assertEquals("Status", ((KlassEnumeration) resolved).getName());
  }

  public void testMemberNameReferenceInExpressionResolvesToMemberDeclaration() {
    myFixture.configureByFile("ReferenceMemberInExpression.klass");

    PsiReference reference = myFixture.getReferenceAtCaretPositionWithAssertion();
    PsiElement resolved = reference.resolve();
    assertNotNull("Member name reference should resolve to a member declaration", resolved);
    assertInstanceOf(resolved, KlassMember.class);
    assertEquals("name", ((KlassMember) resolved).getName());
  }

  public void testClassifierNameReferenceInProjectionResolvesToClassDeclaration() {
    myFixture.configureByFile("ReferenceClassifierInProjection.klass");

    PsiReference reference = myFixture.getReferenceAtCaretPositionWithAssertion();
    PsiElement resolved = reference.resolve();
    assertNotNull(
        "Classifier name reference in projection should resolve to a declaration", resolved);
    assertInstanceOf(resolved, KlassKlass.class);
    assertEquals("User", ((KlassKlass) resolved).getName());
  }

  public void testUnresolvedReferenceReturnsNull() {
    myFixture.configureByFile("ReferenceUnresolved.klass");

    PsiReference reference = myFixture.getReferenceAtCaretPositionWithAssertion();
    PsiElement resolved = reference.resolve();
    assertNull("Reference to non-existent type should not resolve", resolved);
  }
}
