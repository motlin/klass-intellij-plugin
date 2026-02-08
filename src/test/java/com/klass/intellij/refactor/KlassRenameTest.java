package com.klass.intellij.refactor;

import com.intellij.testFramework.fixtures.BasePlatformTestCase;

public class KlassRenameTest extends BasePlatformTestCase {
  @Override
  protected String getTestDataPath() {
    return "src/test/testData";
  }

  public void testRenameClassDeclaration() {
    myFixture.testRename("RenameClassBefore.klass", "RenameClassAfter.klass", "Person");
  }

  public void testRenameClassFromReference() {
    myFixture.testRename(
        "RenameClassFromReferenceBefore.klass", "RenameClassFromReferenceAfter.klass", "Person");
  }

  public void testRenameInterfaceDeclaration() {
    myFixture.testRename(
        "RenameInterfaceBefore.klass", "RenameInterfaceAfter.klass", "Identifiable");
  }

  public void testRenameEnumerationDeclaration() {
    myFixture.testRename("RenameEnumerationBefore.klass", "RenameEnumerationAfter.klass", "State");
  }

  public void testRenameAssociationDeclaration() {
    myFixture.testRename(
        "RenameAssociationBefore.klass", "RenameAssociationAfter.klass", "UserRole");
  }

  public void testRenameMemberProperty() {
    myFixture.testRename(
        "RenameMemberPropertyBefore.klass", "RenameMemberPropertyAfter.klass", "fullName");
  }

  public void testRenameEnumerationLiteral() {
    myFixture.testRename(
        "RenameEnumerationLiteralBefore.klass", "RenameEnumerationLiteralAfter.klass", "ENABLED");
  }

  public void testRenameMemberPropertyFromReference() {
    myFixture.testRename(
        "RenameMemberPropertyFromReferenceBefore.klass",
        "RenameMemberPropertyFromReferenceAfter.klass",
        "fullName");
  }

  public void testRenameMemberPropertyFromCriteria() {
    myFixture.testRename(
        "RenameMemberPropertyFromCriteriaBefore.klass",
        "RenameMemberPropertyFromCriteriaAfter.klass",
        "fullName");
  }

  public void testRenameInterfaceFromReference() {
    myFixture.testRename(
        "RenameInterfaceFromReferenceBefore.klass",
        "RenameInterfaceFromReferenceAfter.klass",
        "Identifiable");
  }

  public void testRenameEnumerationFromReference() {
    myFixture.testRename(
        "RenameEnumerationFromReferenceBefore.klass",
        "RenameEnumerationFromReferenceAfter.klass",
        "State");
  }
}
