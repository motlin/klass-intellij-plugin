package com.klass.intellij.highlighter;

import com.intellij.testFramework.fixtures.BasePlatformTestCase;

public class KlassAnnotatorTest extends BasePlatformTestCase {
  @Override
  protected String getTestDataPath() {
    return "src/test/testData";
  }

  public void testValidCodeHasNoErrors() {
    myFixture.configureByFile("AnnotatorValidCode.klass");
    myFixture.checkHighlighting(false, false, false);
  }

  public void testUnresolvedClassReferenceIsHighlightedAsError() {
    myFixture.configureByFile("AnnotatorUnresolvedReference.klass");
    myFixture.checkHighlighting(false, false, false);
  }

  public void testUnresolvedInterfaceReferenceIsHighlightedAsError() {
    myFixture.configureByFile("AnnotatorUnresolvedInterface.klass");
    myFixture.checkHighlighting(false, false, false);
  }

  public void testUnresolvedEnumerationTypeIsHighlightedAsError() {
    myFixture.configureByFile("AnnotatorUnresolvedEnumeration.klass");
    myFixture.checkHighlighting(false, false, false);
  }

  public void testDuplicatePropertyIsHighlightedAsError() {
    myFixture.configureByFile("AnnotatorDuplicateProperty.klass");
    myFixture.checkHighlighting(false, false, false);
  }

  public void testReservedJavaKeywordIsHighlightedAsError() {
    myFixture.configureByFile("AnnotatorReservedKeyword.klass");
    myFixture.checkHighlighting(false, false, false);
  }
}
