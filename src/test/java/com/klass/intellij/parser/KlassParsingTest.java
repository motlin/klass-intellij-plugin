package com.klass.intellij.parser;

import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testFramework.ParsingTestCase;
import java.io.IOException;
import java.util.Collection;

public class KlassParsingTest extends ParsingTestCase {
  public KlassParsingTest() {
    super("", "klass", new KlassParserDefinition());
  }

  @Override
  protected String getTestDataPath() {
    return "src/test/testData";
  }

  public void testExampleParsingNoErrors() throws IOException {
    PsiFile psiFile = parseFile("Example", loadFile("Example.klass"));
    assertNotNull("Parsed file should not be null", psiFile);

    Collection<PsiErrorElement> errors =
        PsiTreeUtil.collectElementsOfType(psiFile, PsiErrorElement.class);
    assertTrue(
        "Expected no parse errors but found " + errors.size() + ": " + errors, errors.isEmpty());
  }
}
