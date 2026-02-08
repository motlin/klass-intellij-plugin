package com.klass.intellij.usages;

import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import com.intellij.usageView.UsageInfo;
import com.klass.intellij.psi.KlassAssociation;
import com.klass.intellij.psi.KlassAssociationEnd;
import com.klass.intellij.psi.KlassEnumeration;
import com.klass.intellij.psi.KlassEnumerationLiteral;
import com.klass.intellij.psi.KlassInterface;
import com.klass.intellij.psi.KlassKlass;
import com.klass.intellij.psi.KlassMember;
import com.klass.intellij.psi.KlassProjection;
import java.util.Collection;

public class KlassFindUsagesTest extends BasePlatformTestCase {
  @Override
  protected String getTestDataPath() {
    return "src/test/testData";
  }

  public void testFindUsagesOfClass() {
    myFixture.configureByFile("FindUsagesClass.klass");

    Collection<UsageInfo> usages = myFixture.findUsages(myFixture.getElementAtCaret());
    // User is referenced in: association end type User[1..1], association end type User[0..*],
    // relationship User.name, and projection "on User"
    assertFalse("Expected usages of class 'User' but found none", usages.isEmpty());
  }

  public void testFindUsagesOfInterface() {
    myFixture.configureByFile("FindUsagesInterface.klass");

    Collection<UsageInfo> usages = myFixture.findUsages(myFixture.getElementAtCaret());
    // Named is referenced in: class User implements Named
    assertFalse("Expected usages of interface 'Named' but found none", usages.isEmpty());
  }

  public void testFindUsagesOfEnumeration() {
    myFixture.configureByFile("FindUsagesEnumeration.klass");

    Collection<UsageInfo> usages = myFixture.findUsages(myFixture.getElementAtCaret());
    // Status is referenced in: status: Status;
    assertFalse("Expected usages of enumeration 'Status' but found none", usages.isEmpty());
  }

  public void testCanFindUsagesForNamedElements() {
    PsiFile psiFile = myFixture.configureByFile("Example.klass");
    KlassFindUsagesProvider provider = new KlassFindUsagesProvider();

    KlassInterface klassInterface = PsiTreeUtil.findChildOfType(psiFile, KlassInterface.class);
    assertNotNull("Expected to find an interface in the file", klassInterface);
    assertTrue(
        "Should be able to find usages of an interface", provider.canFindUsagesFor(klassInterface));

    KlassKlass klassKlass = PsiTreeUtil.findChildOfType(psiFile, KlassKlass.class);
    assertNotNull("Expected to find a class in the file", klassKlass);
    assertTrue("Should be able to find usages of a class", provider.canFindUsagesFor(klassKlass));

    KlassEnumeration klassEnumeration =
        PsiTreeUtil.findChildOfType(psiFile, KlassEnumeration.class);
    assertNotNull("Expected to find an enumeration in the file", klassEnumeration);
    assertTrue(
        "Should be able to find usages of an enumeration",
        provider.canFindUsagesFor(klassEnumeration));
  }

  public void testGetTypeReturnsCorrectStrings() {
    PsiFile psiFile = myFixture.configureByFile("Example.klass");
    KlassFindUsagesProvider provider = new KlassFindUsagesProvider();

    KlassInterface klassInterface = PsiTreeUtil.findChildOfType(psiFile, KlassInterface.class);
    assertNotNull(klassInterface);
    assertEquals("interface", provider.getType(klassInterface));

    KlassKlass klassKlass = PsiTreeUtil.findChildOfType(psiFile, KlassKlass.class);
    assertNotNull(klassKlass);
    assertEquals("class", provider.getType(klassKlass));

    KlassEnumeration klassEnumeration =
        PsiTreeUtil.findChildOfType(psiFile, KlassEnumeration.class);
    assertNotNull(klassEnumeration);
    assertEquals("enumeration", provider.getType(klassEnumeration));

    KlassAssociation klassAssociation =
        PsiTreeUtil.findChildOfType(psiFile, KlassAssociation.class);
    assertNotNull(klassAssociation);
    assertEquals("association", provider.getType(klassAssociation));

    KlassAssociationEnd klassAssociationEnd =
        PsiTreeUtil.findChildOfType(psiFile, KlassAssociationEnd.class);
    assertNotNull(klassAssociationEnd);
    assertEquals("association end", provider.getType(klassAssociationEnd));

    KlassProjection klassProjection = PsiTreeUtil.findChildOfType(psiFile, KlassProjection.class);
    assertNotNull(klassProjection);
    assertEquals("projection", provider.getType(klassProjection));

    KlassEnumerationLiteral klassEnumerationLiteral =
        PsiTreeUtil.findChildOfType(psiFile, KlassEnumerationLiteral.class);
    assertNotNull(klassEnumerationLiteral);
    assertEquals("enumeration literal", provider.getType(klassEnumerationLiteral));
  }

  public void testGetDescriptiveNameReturnsElementName() {
    PsiFile psiFile = myFixture.configureByFile("Example.klass");
    KlassFindUsagesProvider provider = new KlassFindUsagesProvider();

    KlassInterface klassInterface = PsiTreeUtil.findChildOfType(psiFile, KlassInterface.class);
    assertNotNull(klassInterface);
    assertEquals("Named", provider.getDescriptiveName(klassInterface));

    KlassKlass klassKlass = PsiTreeUtil.findChildOfType(psiFile, KlassKlass.class);
    assertNotNull(klassKlass);
    assertEquals("User", provider.getDescriptiveName(klassKlass));

    KlassEnumeration klassEnumeration =
        PsiTreeUtil.findChildOfType(psiFile, KlassEnumeration.class);
    assertNotNull(klassEnumeration);
    assertEquals("Status", provider.getDescriptiveName(klassEnumeration));
  }

  public void testGetNodeTextReturnsDescriptiveName() {
    PsiFile psiFile = myFixture.configureByFile("Example.klass");
    KlassFindUsagesProvider provider = new KlassFindUsagesProvider();

    KlassKlass klassKlass = PsiTreeUtil.findChildOfType(psiFile, KlassKlass.class);
    assertNotNull(klassKlass);
    assertEquals("User", provider.getNodeText(klassKlass, false));
    assertEquals("User", provider.getNodeText(klassKlass, true));
  }

  public void testGetTypeReturnsEmptyForUnknownElement() {
    PsiFile psiFile = myFixture.configureByFile("Example.klass");
    KlassFindUsagesProvider provider = new KlassFindUsagesProvider();

    // The PsiFile itself is not a known type
    assertEquals("", provider.getType(psiFile));
  }

  public void testWordsScannerIsNotNull() {
    KlassFindUsagesProvider provider = new KlassFindUsagesProvider();
    WordsScanner wordsScanner = provider.getWordsScanner();
    assertNotNull("Words scanner should not be null", wordsScanner);
  }

  public void testGetTypeForMember() {
    PsiFile psiFile = myFixture.configureByFile("Example.klass");
    KlassFindUsagesProvider provider = new KlassFindUsagesProvider();

    KlassMember klassMember = PsiTreeUtil.findChildOfType(psiFile, KlassMember.class);
    if (klassMember != null) {
      assertEquals("property", provider.getType(klassMember));
    }
  }

  public void testGetDescriptiveNameReturnsEmptyForNonNamedElement() {
    PsiFile psiFile = myFixture.configureByFile("Example.klass");
    KlassFindUsagesProvider provider = new KlassFindUsagesProvider();

    // The PsiFile itself is not a KlassNamedElement
    assertEquals("", provider.getDescriptiveName(psiFile));
  }
}
