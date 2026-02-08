package com.klass.intellij.completion;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.psi.PsiReference;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import com.klass.intellij.reference.KlassClassifierReference;
import com.klass.intellij.reference.KlassDataTypeReference;
import com.klass.intellij.reference.KlassEnumerationReference;
import com.klass.intellij.reference.KlassInterfaceReference;
import com.klass.intellij.reference.KlassKlassReference;
import com.klass.intellij.reference.KlassMemberReference;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class KlassCompletionTest extends BasePlatformTestCase {
  @Override
  protected String getTestDataPath() {
    return "src/test/testData";
  }

  public void testReferenceGetVariantsReturnsDataTypes() {
    myFixture.configureByText(
        "test.klass", "package example\n\nclass User\n{\n    name: <caret>String key;\n}\n");

    PsiReference ref = myFixture.getReferenceAtCaretPosition();
    assertNotNull("Expected a reference at caret position", ref);
    assertInstanceOf(ref, KlassDataTypeReference.class);

    Object[] variants = ((KlassDataTypeReference) ref).getVariants();
    List<String> variantStrings =
        Arrays.stream(variants).map(Object::toString).collect(Collectors.toList());

    assertContainsElements(
        variantStrings,
        "Boolean",
        "Integer",
        "Long",
        "Double",
        "Float",
        "String",
        "Instant",
        "LocalDate");
  }

  public void testReferenceGetVariantsReturnsClasses() {
    myFixture.configureByFile("CompletionClassInAssociation.klass");

    PsiReference ref = myFixture.getReferenceAtCaretPosition();
    assertNotNull("Expected a reference at caret position", ref);
    assertInstanceOf(ref, KlassKlassReference.class);

    Object[] variants = ((KlassKlassReference) ref).getVariants();
    List<String> variantStrings =
        Arrays.stream(variants)
            .filter(o -> o instanceof LookupElement)
            .map(o -> ((LookupElement) o).getLookupString())
            .collect(Collectors.toList());

    assertContainsElements(variantStrings, "User", "Order");
  }

  public void testReferenceGetVariantsReturnsInterfaces() {
    myFixture.configureByFile("CompletionInterfaceInImplements.klass");

    PsiReference ref = myFixture.getReferenceAtCaretPosition();
    assertNotNull("Expected a reference at caret position", ref);
    assertInstanceOf(ref, KlassInterfaceReference.class);

    Object[] variants = ((KlassInterfaceReference) ref).getVariants();
    List<String> variantStrings =
        Arrays.stream(variants)
            .filter(o -> o instanceof LookupElement)
            .map(o -> ((LookupElement) o).getLookupString())
            .collect(Collectors.toList());

    assertContainsElements(variantStrings, "Named", "Versioned");
  }

  public void testReferenceGetVariantsReturnsEnumerations() {
    myFixture.configureByFile("CompletionEnumerationInProperty.klass");

    PsiReference ref = myFixture.getReferenceAtCaretPosition();
    assertNotNull("Expected a reference at caret position", ref);
    assertInstanceOf(ref, KlassEnumerationReference.class);

    Object[] variants = ((KlassEnumerationReference) ref).getVariants();
    List<String> variantStrings =
        Arrays.stream(variants)
            .filter(o -> o instanceof LookupElement)
            .map(o -> ((LookupElement) o).getLookupString())
            .collect(Collectors.toList());

    assertContainsElements(variantStrings, "Status", "Priority");
  }

  public void testReferenceGetVariantsReturnsMembers() {
    myFixture.configureByFile("CompletionMemberInExpression.klass");

    PsiReference ref = myFixture.getReferenceAtCaretPosition();
    assertNotNull("Expected a reference at caret position", ref);
    assertInstanceOf(ref, KlassMemberReference.class);

    Object[] variants = ((KlassMemberReference) ref).getVariants();
    List<String> variantStrings =
        Arrays.stream(variants)
            .filter(o -> o instanceof LookupElement)
            .map(o -> ((LookupElement) o).getLookupString())
            .collect(Collectors.toList());

    assertContainsElements(variantStrings, "name", "email");
  }

  public void testReferenceGetVariantsReturnsClassesAndInterfaces() {
    myFixture.configureByFile("CompletionClassifierInProjection.klass");

    PsiReference ref = myFixture.getReferenceAtCaretPosition();
    assertNotNull("Expected a reference at caret position", ref);
    assertInstanceOf(ref, KlassClassifierReference.class);

    Object[] variants = ((KlassClassifierReference) ref).getVariants();
    List<String> variantStrings =
        Arrays.stream(variants)
            .filter(o -> o instanceof LookupElement)
            .map(o -> ((LookupElement) o).getLookupString())
            .collect(Collectors.toList());

    assertContainsElements(variantStrings, "User", "Named");
  }
}
