package com.klass.intellij.reference;

import com.intellij.codeInsight.AutoPopupController;
import com.intellij.codeInsight.completion.InsertHandler;
import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.icons.AllIcons;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiPolyVariantReferenceBase;
import com.intellij.psi.PsiReference;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.text.CharArrayUtil;
import com.klass.intellij.psi.KlassAssociation;
import com.klass.intellij.psi.KlassAssociationEnd;
import com.klass.intellij.psi.KlassAssociationEndName;
import com.klass.intellij.psi.KlassClassModifier;
import com.klass.intellij.psi.KlassClassifier;
import com.klass.intellij.psi.KlassClassifierName;
import com.klass.intellij.psi.KlassElementFactory;
import com.klass.intellij.psi.KlassEnumeration;
import com.klass.intellij.psi.KlassExpressionThisMember;
import com.klass.intellij.psi.KlassExpressionTypeMember;
import com.klass.intellij.psi.KlassExtendsClause;
import com.klass.intellij.psi.KlassImplementsClause;
import com.klass.intellij.psi.KlassImplementsList;
import com.klass.intellij.psi.KlassInterface;
import com.klass.intellij.psi.KlassInterfaceName;
import com.klass.intellij.psi.KlassKlass;
import com.klass.intellij.psi.KlassKlassName;
import com.klass.intellij.psi.KlassMember;
import com.klass.intellij.psi.KlassMemberName;
import com.klass.intellij.psi.KlassOrderByProperty;
import com.klass.intellij.psi.KlassParameterizedProperty;
import com.klass.intellij.psi.KlassParameterizedPropertyName;
import com.klass.intellij.psi.KlassProjection;
import com.klass.intellij.psi.KlassProjectionAssociationEndNode;
import com.klass.intellij.psi.KlassProjectionLeafNode;
import com.klass.intellij.psi.KlassProjectionParameterizedPropertyNode;
import com.klass.intellij.psi.KlassServiceGroup;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class KlassMemberReference extends PsiPolyVariantReferenceBase<PsiElement> {
  public static final ImmutableList<String> VALID_PROPERTIES =
      Lists.immutable.with("valid", "validFrom", "validTo");
  public static final ImmutableList<String> SYSTEM_PROPERTIES =
      Lists.immutable.with("system", "systemFrom", "systemTo");
  private final String propertyName;

  public KlassMemberReference(@NotNull PsiElement element, String propertyName) {
    super(element, new TextRange(0, propertyName.length()));
    this.propertyName = propertyName;
  }

  @NotNull @Override
  public ResolveResult[] multiResolve(boolean incompleteCode) {
    PsiElement parent = this.myElement.getParent();
    if (!(this.myElement instanceof KlassMemberName)) {
      return new ResolveResult[] {};
    }

    if (parent instanceof KlassExpressionThisMember) {
      KlassKlass klass = PsiTreeUtil.getParentOfType(this.myElement, KlassKlass.class);
      if (klass != null) {
        return this.getKlassResolveResults(klass);
      }

      KlassServiceGroup serviceGroup =
          PsiTreeUtil.getParentOfType(this.myElement, KlassServiceGroup.class);
      if (serviceGroup != null) {
        PsiReference reference = serviceGroup.getKlassName().getReference();
        KlassKlass serviceKlass = (KlassKlass) reference.resolve();
        if (serviceKlass == null) {
          return new ResolveResult[] {};
        }
        return this.getKlassResolveResults(serviceKlass);
      }

      KlassAssociation association =
          PsiTreeUtil.getParentOfType(this.myElement, KlassAssociation.class);

      if (association != null) {
        KlassAssociationEnd associationEnd =
            association.getAssociationBlock().getAssociationBody().getAssociationEndList().get(0);
        KlassKlassReference klassReference =
            (KlassKlassReference) associationEnd.getKlassName().getReference();
        KlassKlass klassKlass = (KlassKlass) klassReference.resolve();
        if (klassKlass == null) {
          return new ResolveResult[] {};
        }

        return this.getKlassResolveResults(klassKlass);
      }
    } else if (parent instanceof KlassExpressionTypeMember) {
      KlassExpressionTypeMember expressionTypeMember = (KlassExpressionTypeMember) parent;
      KlassKlassName klassName = expressionTypeMember.getKlassName();

      PsiReference klassNameReference = klassName.getReference();
      PsiElement resolve = klassNameReference.resolve();
      if (resolve instanceof KlassKlass) {
        KlassKlass klass = (KlassKlass) resolve;
        return this.getKlassResolveResults(klass);
      }
      if (resolve instanceof KlassEnumeration) {
        KlassEnumeration enumeration = (KlassEnumeration) resolve;
        return this.getEnumerationResolveResults(enumeration);
      }
      if (resolve != null) {
        throw new AssertionError(resolve);
      }
    } else if (parent instanceof KlassOrderByProperty) {
      KlassServiceGroup klassServiceGroup =
          PsiTreeUtil.getParentOfType(this.myElement, KlassServiceGroup.class);
      if (klassServiceGroup != null) {
        PsiReference reference = klassServiceGroup.getKlassName().getReference();
        KlassKlass serviceKlass = (KlassKlass) reference.resolve();
        if (serviceKlass == null) {
          return new ResolveResult[] {};
        }
        return this.getKlassResolveResults(serviceKlass);
      }

      KlassAssociationEnd associationEnd =
          PsiTreeUtil.getParentOfType(this.myElement, KlassAssociationEnd.class);
      KlassParameterizedProperty parameterizedProperty =
          PsiTreeUtil.getParentOfType(this.myElement, KlassParameterizedProperty.class);

      if (associationEnd != null) {
        KlassKlassName klassName = associationEnd.getKlassName();
        PsiReference klassNameReference = klassName.getReference();
        KlassKlass klassKlass = (KlassKlass) klassNameReference.resolve();

        if (klassKlass != null) {
          return this.getKlassResolveResults(klassKlass);
        }
      } else if (parameterizedProperty != null) {
        KlassKlassName klassName = parameterizedProperty.getKlassName();
        PsiReference klassNameReference = klassName.getReference();
        KlassKlass klassKlass = (KlassKlass) klassNameReference.resolve();

        if (klassKlass != null) {
          return this.getKlassResolveResults(klassKlass);
        }
      }
    } else if (parent instanceof KlassProjectionLeafNode) {
      KlassProjectionLeafNode projectionLeafNode = (KlassProjectionLeafNode) parent;
      KlassClassifierName classifierName = projectionLeafNode.getClassifierName();
      if (classifierName != null) {
        PsiReference classifierReference = classifierName.getReference();
        KlassClassifier classifier = (KlassClassifier) classifierReference.resolve();
        if (classifier != null) {
          return this.getClassifierResolveResults(classifier);
        }
      }

      PsiElement projectionNodeParent = projectionLeafNode.getParent().getParent().getParent();
      if (projectionNodeParent instanceof KlassProjection) {
        KlassProjection projection = (KlassProjection) projectionNodeParent;
        PsiReference classifierReference = projection.getClassifierName().getReference();
        PsiElement resolved = classifierReference.resolve();

        if (resolved instanceof KlassClassifier) {
          return this.getClassifierResolveResults((KlassClassifier) resolved);
        }
      } else if (projectionNodeParent instanceof KlassProjectionAssociationEndNode) {
        KlassProjectionAssociationEndNode associationEndNode =
            (KlassProjectionAssociationEndNode) projectionNodeParent;
        KlassAssociationEndName associationEndName = associationEndNode.getAssociationEndName();

        PsiReference associationEndReference = associationEndName.getReference();
        PsiElement resolve = associationEndReference.resolve();

        if (resolve instanceof KlassAssociationEnd) {
          KlassAssociationEnd klassAssociationEnd = (KlassAssociationEnd) resolve;

          KlassKlassName klassName = klassAssociationEnd.getKlassName();
          PsiReference klassNameReference = klassName.getReference();
          KlassKlass klassKlass = (KlassKlass) klassNameReference.resolve();

          if (klassKlass != null) {
            return this.getKlassResolveResults(klassKlass);
          }
        } else if (resolve instanceof KlassClassModifier) {
          return new PsiElementResolveResult[] {new PsiElementResolveResult(resolve)};
        } else if (resolve != null) {
          throw new AssertionError(resolve);
        }
      } else if (projectionNodeParent instanceof KlassProjectionParameterizedPropertyNode) {
        KlassProjectionParameterizedPropertyNode parameterizedPropertyNode =
            (KlassProjectionParameterizedPropertyNode) projectionNodeParent;
        KlassParameterizedPropertyName parameterizedPropertyName =
            parameterizedPropertyNode.getParameterizedPropertyName();

        PsiReference parameterizedPropertyNameReference = parameterizedPropertyName.getReference();
        KlassParameterizedProperty klassParameterizedProperty =
            (KlassParameterizedProperty) parameterizedPropertyNameReference.resolve();

        if (klassParameterizedProperty != null) {
          KlassKlassName klassName = klassParameterizedProperty.getKlassName();
          PsiReference klassNameReference = klassName.getReference();
          KlassKlass klassKlass = (KlassKlass) klassNameReference.resolve();

          if (klassKlass != null) {
            return this.getKlassResolveResults(klassKlass);
          }
        }
      } else {
        throw new AssertionError(projectionNodeParent.getClass());
      }
    }

    return new ResolveResult[] {};
  }

  private ResolveResult[] getEnumerationResolveResults(KlassEnumeration enumeration) {
    return enumeration
        .getEnumerationBlock()
        .getEnumerationBody()
        .getEnumerationLiteralList()
        .stream()
        .filter(enumerationLiteral -> enumerationLiteral.getName().equals(this.propertyName))
        .map(PsiElementResolveResult::new)
        .toArray(ResolveResult[]::new);
  }

  @Nullable @Override
  public PsiElement resolve() {
    ResolveResult[] resolveResults = this.multiResolve(false);
    if (resolveResults.length == 1) {
      return resolveResults[0].getElement();
    } else {
      return null;
    }
  }

  @NotNull @Override
  public Object[] getVariants() {
    PsiElement parent = this.myElement.getParent();
    PsiElement grandparent = parent.getParent();
    PsiElement greatGrandparent = grandparent.getParent();

    if (grandparent instanceof KlassProjectionAssociationEndNode) {
      KlassProjectionAssociationEndNode associationEndNode =
          (KlassProjectionAssociationEndNode) grandparent;
      KlassAssociationEndName associationEndName = associationEndNode.getAssociationEndName();
      KlassAssociationEndReference associationEndReference =
          (KlassAssociationEndReference) associationEndName.getReference();
      KlassAssociationEnd associationEnd = (KlassAssociationEnd) associationEndReference.resolve();
      if (associationEnd != null) {
        KlassKlassName klassName = associationEnd.getKlassName();
        PsiReference klassNameReference = klassName.getReference();
        KlassKlass klass = (KlassKlass) klassNameReference.resolve();
        if (klass != null) {
          List<KlassMember> propertyList = klass.getClassBlock().getClassBody().getMemberList();
          return propertyList.stream()
              .map(
                  member ->
                      LookupElementBuilder.create(member.getName())
                          .withIcon(AllIcons.Nodes.Property)
                          .withTypeText(member.getContainingFile().getName())
                          .withInsertHandler(ProjectionLeafInsertHandler.INSTANCE))
              .toArray();
        }
      }
    } else if (grandparent instanceof KlassProjectionParameterizedPropertyNode) {
      KlassProjectionParameterizedPropertyNode parameterizedPropertyNode =
          (KlassProjectionParameterizedPropertyNode) grandparent;
      KlassParameterizedPropertyName parameterizedPropertyName =
          parameterizedPropertyNode.getParameterizedPropertyName();
      KlassParameterizedPropertyReference parameterizedPropertyReference =
          (KlassParameterizedPropertyReference) parameterizedPropertyName.getReference();
      KlassParameterizedProperty parameterizedProperty =
          (KlassParameterizedProperty) parameterizedPropertyReference.resolve();
      if (parameterizedProperty != null) {
        KlassKlassName klassName = parameterizedProperty.getKlassName();
        PsiReference klassNameReference = klassName.getReference();
        KlassKlass klass = (KlassKlass) klassNameReference.resolve();
        if (klass != null) {
          List<KlassMember> propertyList = klass.getClassBlock().getClassBody().getMemberList();
          return propertyList.stream()
              .map(
                  klassMember ->
                      LookupElementBuilder.create(klassMember.getName())
                          .withIcon(AllIcons.Nodes.Property)
                          .withTypeText(klassMember.getContainingFile().getName())
                          .withInsertHandler(ProjectionLeafInsertHandler.INSTANCE))
              .toArray();
        }
      }
    } else if (grandparent instanceof KlassProjection) {
      KlassClassifierName classifierName = ((KlassProjection) grandparent).getClassifierName();
      PsiReference classifierReference = classifierName.getReference();
      PsiElement resolved = classifierReference.resolve();

      if (resolved instanceof KlassKlass) {
        return this.getKlassMemberLookups((KlassKlass) resolved);
      }
      if (resolved instanceof KlassInterface) {
        return this.getInterfaceMemberLookups((KlassInterface) resolved);
      }
      return new Object[] {};
    } else if (parent instanceof KlassExpressionThisMember) {
      KlassKlass klassKlass = PsiTreeUtil.getParentOfType(this.myElement, KlassKlass.class);

      if (klassKlass != null) {
        return this.getKlassMemberLookups(klassKlass);
      }

      KlassServiceGroup klassServiceGroup =
          PsiTreeUtil.getParentOfType(this.myElement, KlassServiceGroup.class);
      if (klassServiceGroup != null) {
        PsiReference reference = klassServiceGroup.getKlassName().getReference();
        KlassKlass serviceKlass = (KlassKlass) reference.resolve();
        return this.getKlassMemberLookups(serviceKlass);
      }
    } else if (parent instanceof KlassExpressionTypeMember) {
      KlassExpressionTypeMember expressionTypeMember = (KlassExpressionTypeMember) parent;
      KlassKlassName klassName = expressionTypeMember.getKlassName();

      KlassKlassReference klassNameReference = (KlassKlassReference) klassName.getReference();
      PsiElement resolve = klassNameReference.resolve();
      if (resolve instanceof KlassKlass) {
        KlassKlass klassKlass = (KlassKlass) resolve;
        return this.getKlassMemberLookups(klassKlass);
      }
      if (resolve instanceof KlassEnumeration) {
        KlassEnumeration enumeration = (KlassEnumeration) resolve;
        return this.getEnumerationLiteralLookups(enumeration);
      }
      if (resolve != null) {
        throw new AssertionError();
      }
    } else if (parent instanceof KlassOrderByProperty) {
      if (greatGrandparent instanceof KlassAssociationEnd) {
        KlassAssociationEnd associationEnd = (KlassAssociationEnd) greatGrandparent;
        KlassKlassName klassName = associationEnd.getKlassName();
        PsiReference klassNameReference = klassName.getReference();
        KlassKlass klass = (KlassKlass) klassNameReference.resolve();
        if (klass != null) {
          List<KlassMember> propertyList = klass.getClassBlock().getClassBody().getMemberList();
          return propertyList.stream()
              .map(
                  member ->
                      LookupElementBuilder.create(member.getName())
                          .withIcon(AllIcons.Nodes.Property)
                          .withTypeText(member.getContainingFile().getName()))
              .toArray();
        }
      }
    } else if (grandparent instanceof KlassProjectionLeafNode) {
      KlassProjectionLeafNode projectionLeafNode = (KlassProjectionLeafNode) parent;
      PsiElement projectionNodeParent = projectionLeafNode.getParent().getParent().getParent();
      if (projectionNodeParent instanceof KlassProjection) {
        KlassProjection projection = (KlassProjection) projectionNodeParent;
        PsiReference classifierReference = projection.getClassifierName().getReference();
        PsiElement resolved = classifierReference.resolve();

        if (resolved instanceof KlassKlass) {
          return this.getKlassMemberLookups((KlassKlass) resolved);
        }
        if (resolved instanceof KlassInterface) {
          return this.getInterfaceMemberLookups((KlassInterface) resolved);
        }
      } else if (projectionNodeParent instanceof KlassProjectionAssociationEndNode) {
        KlassProjectionAssociationEndNode associationEndNode =
            (KlassProjectionAssociationEndNode) projectionNodeParent;
        KlassAssociationEndName associationEndName = associationEndNode.getAssociationEndName();

        PsiReference associationEndReference = associationEndName.getReference();
        PsiElement resolve = associationEndReference.resolve();

        if (resolve instanceof KlassAssociationEnd) {
          KlassAssociationEnd klassAssociationEnd = (KlassAssociationEnd) resolve;

          KlassKlassName klassName = klassAssociationEnd.getKlassName();
          PsiReference klassNameReference = klassName.getReference();
          KlassKlass klassKlass = (KlassKlass) klassNameReference.resolve();

          if (klassKlass != null) {
            return this.getKlassMemberLookups(klassKlass);
          }
        } else if (resolve instanceof KlassClassModifier) {
          return new PsiElementResolveResult[] {new PsiElementResolveResult(resolve)};
        } else if (resolve != null) {
          throw new AssertionError(resolve);
        }
      } else if (projectionNodeParent instanceof KlassProjectionParameterizedPropertyNode) {
        KlassProjectionParameterizedPropertyNode parameterizedPropertyNode =
            (KlassProjectionParameterizedPropertyNode) projectionNodeParent;
        KlassParameterizedPropertyName parameterizedPropertyName =
            parameterizedPropertyNode.getParameterizedPropertyName();

        PsiReference parameterizedPropertyNameReference = parameterizedPropertyName.getReference();
        KlassParameterizedProperty klassParameterizedProperty =
            (KlassParameterizedProperty) parameterizedPropertyNameReference.resolve();

        if (klassParameterizedProperty != null) {
          KlassKlassName klassName = klassParameterizedProperty.getKlassName();
          PsiReference klassNameReference = klassName.getReference();
          KlassKlass klassKlass = (KlassKlass) klassNameReference.resolve();

          if (klassKlass != null) {
            return this.getKlassMemberLookups(klassKlass);
          }
        }
      } else {
        throw new AssertionError(projectionNodeParent.getClass());
      }
    } else {
      throw new AssertionError(parent.getClass().getCanonicalName());
    }

    return new Object[] {};
  }

  private ResolveResult[] getClassifierResolveResults(KlassClassifier classifier) {
    if (classifier instanceof KlassKlass) {
      return getKlassResolveResults((KlassKlass) classifier);
    }
    if (classifier instanceof KlassInterface) {
      return getInterfaceResolveResults((KlassInterface) classifier);
    }
    throw new AssertionError();
  }

  private ResolveResult[] getKlassResolveResults(KlassKlass klass) {
    ResolveResult[] resolveResults =
        klass.getClassBlock().getClassBody().getMemberList().stream()
            .filter(klassMember -> klassMember.getName().equals(this.propertyName))
            .map(PsiElementResolveResult::new)
            .toArray(ResolveResult[]::new);
    ResolveResult[] temporalResolveResults =
        this.getTemporalResolveResults(resolveResults, klass.getClassModifierList());
    if (temporalResolveResults.length != 0) {
      return temporalResolveResults;
    }

    KlassExtendsClause extendsClause = klass.getExtendsClause();
    if (extendsClause != null) {
      PsiElement resolvedSuperClass = extendsClause.getKlassName().getReference().resolve();
      if (resolvedSuperClass != null) {
        ResolveResult[] superClassResolvedResults =
            this.getKlassResolveResults((KlassKlass) resolvedSuperClass);

        if (superClassResolvedResults.length > 0) {
          return superClassResolvedResults;
        }
      }
    }

    KlassImplementsClause implementsClause = klass.getImplementsClause();
    if (implementsClause == null) {
      return temporalResolveResults;
    }

    return this.getImplementsListResolveResults(implementsClause.getImplementsList());
  }

  private ResolveResult[] getInterfaceResolveResults(KlassInterface klassInterface) {
    ResolveResult[] resolveResults =
        klassInterface.getInterfaceBlock().getInterfaceBody().getMemberList().stream()
            .filter(klassMember -> klassMember.getName().equals(this.propertyName))
            .map(PsiElementResolveResult::new)
            .toArray(ResolveResult[]::new);
    ResolveResult[] temporalResolveResults =
        this.getTemporalResolveResults(resolveResults, klassInterface.getClassModifierList());
    if (temporalResolveResults.length != 0) {
      return temporalResolveResults;
    }

    KlassImplementsClause implementsClause = klassInterface.getImplementsClause();
    if (implementsClause == null) {
      return temporalResolveResults;
    }

    return this.getImplementsListResolveResults(implementsClause.getImplementsList());
  }

  @NotNull private ResolveResult[] getImplementsListResolveResults(KlassImplementsList implementsList) {
    return implementsList.getInterfaceNameList().stream()
        .map(KlassInterfaceName::getReference)
        .map(PsiReference::resolve)
        .filter(Objects::nonNull)
        .map(KlassInterface.class::cast)
        .flatMap(superInterface -> Arrays.stream(this.getInterfaceResolveResults(superInterface)))
        .toArray(ResolveResult[]::new);
  }

  // TODO: superclasses and interfaces
  private Object[] getKlassMemberLookups(KlassKlass klassKlass) {
    if (klassKlass == null) {
      return new Object[] {};
    }
    return Objects.requireNonNull(klassKlass)
        .getClassBlock()
        .getClassBody()
        .getMemberList()
        .stream()
        .map(PsiNamedElement::getName)
        .map(LookupElementBuilder::create)
        .map(lookupElementBuilder -> lookupElementBuilder.withIcon(AllIcons.Nodes.Property))
        .toArray();
  }

  private Object[] getInterfaceMemberLookups(KlassInterface klassInterface) {
    if (klassInterface == null) {
      return new Object[] {};
    }
    return klassInterface.getInterfaceBlock().getInterfaceBody().getMemberList().stream()
        .map(PsiNamedElement::getName)
        .map(LookupElementBuilder::create)
        .map(lookupElementBuilder -> lookupElementBuilder.withIcon(AllIcons.Nodes.Property))
        .toArray();
  }

  public Object[] getEnumerationLiteralLookups(KlassEnumeration enumeration) {
    return Objects.requireNonNull(enumeration)
        .getEnumerationBlock()
        .getEnumerationBody()
        .getEnumerationLiteralList()
        .stream()
        .map(PsiNamedElement::getName)
        .map(LookupElementBuilder::create)
        .map(lookupElementBuilder -> lookupElementBuilder.withIcon(AllIcons.Nodes.Enum))
        .toArray();
  }

  @NotNull public ResolveResult[] getTemporalResolveResults(
      ResolveResult[] resolveResults, List<KlassClassModifier> classModifierList) {
    if (resolveResults.length != 0) {
      return resolveResults;
    }
    if (VALID_PROPERTIES.contains(this.propertyName)) {
      return this.getTemporalReference("validTemporal", classModifierList);
    }
    if (SYSTEM_PROPERTIES.contains(this.propertyName)) {
      return this.getTemporalReference("systemTemporal", classModifierList);
    }
    return resolveResults;
  }

  @NotNull private ResolveResult[] getTemporalReference(
      String temporalKeyword, List<KlassClassModifier> classModifierList) {
    return classModifierList.stream()
        .filter(
            classModifier ->
                classModifier.getText().equals(temporalKeyword)
                    || classModifier.getText().equals("bitemporal"))
        .map(PsiElementResolveResult::new)
        .toArray(ResolveResult[]::new);
  }

  @Override
  public PsiElement handleElementRename(String newElementName) {
    ASTNode node = this.myElement.getNode();
    if (node != null) {
      KlassMemberName propertyName =
          KlassElementFactory.createPropertyName(this.myElement.getProject(), newElementName);

      ASTNode newNode = propertyName.getNode();
      node.getTreeParent().replaceChild(node, newNode);
    }
    return this.myElement;
  }

  // Based heavily on the insert handler for xml attributes, where it inserts =""
  public static class ProjectionLeafInsertHandler implements InsertHandler<LookupElement> {
    public static final ProjectionLeafInsertHandler INSTANCE = new ProjectionLeafInsertHandler();

    @Override
    public void handleInsert(InsertionContext context, LookupElement item) {
      Editor editor = context.getEditor();

      Document document = editor.getDocument();
      int caretOffset = editor.getCaretModel().getOffset();
      PsiFile file = context.getFile();

      CharSequence chars = document.getCharsSequence();
      boolean hasQuotes = CharArrayUtil.regionMatches(chars, caretOffset, ":\"");
      if (!hasQuotes) {
        PsiElement fileContext = file.getContext();
        String toInsert = null;

        if (fileContext != null) {
          if (fileContext.getText().startsWith("\"")) {
            toInsert = ":''";
          }
          if (fileContext.getText().startsWith("\'")) {
            toInsert = ":\"\"";
          }
        }
        if (toInsert == null) {
          toInsert = ":\"\"";
        }

        document.insertString(
            caretOffset, caretOffset >= document.getTextLength() ? toInsert + " " : toInsert);

        if (':' == context.getCompletionChar()) {
          context.setAddCompletionChar(false); // IDEA-19449
        }
      }

      editor.getCaretModel().moveToOffset(caretOffset + 2);
      editor.getScrollingModel().scrollToCaret(ScrollType.RELATIVE);
      editor.getSelectionModel().removeSelection();
      AutoPopupController.getInstance(editor.getProject()).scheduleAutoPopup(editor);
    }
  }
}
