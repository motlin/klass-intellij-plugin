package com.klass.intellij.reference;

import java.util.List;
import java.util.Objects;

import com.intellij.codeInsight.AutoPopupController;
import com.intellij.codeInsight.completion.InsertHandler;
import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.text.CharArrayUtil;
import com.klass.intellij.psi.KlassAssociation;
import com.klass.intellij.psi.KlassAssociationEnd;
import com.klass.intellij.psi.KlassAssociationEndName;
import com.klass.intellij.psi.KlassCriteriaType;
import com.klass.intellij.psi.KlassEnumeration;
import com.klass.intellij.psi.KlassExpressionProperty;
import com.klass.intellij.psi.KlassKlass;
import com.klass.intellij.psi.KlassKlassName;
import com.klass.intellij.psi.KlassMember;
import com.klass.intellij.psi.KlassOrderByProperty;
import com.klass.intellij.psi.KlassParameterizedProperty;
import com.klass.intellij.psi.KlassParameterizedPropertyName;
import com.klass.intellij.psi.KlassProjection;
import com.klass.intellij.psi.KlassProjectionAssociationEndNode;
import com.klass.intellij.psi.KlassProjectionLeafNode;
import com.klass.intellij.psi.KlassProjectionParameterizedPropertyNode;
import com.klass.intellij.psi.KlassPropertyName;
import com.klass.intellij.psi.KlassServiceGroup;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class KlassMemberReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference
{
    public static final ImmutableList<String> VALID_PROPERTIES  =
            Lists.immutable.with("valid", "validFrom", "validTo");
    public static final ImmutableList<String> SYSTEM_PROPERTIES =
            Lists.immutable.with("system", "systemFrom", "systemTo");
    private final       String                propertyName;

    public KlassMemberReference(@NotNull PsiElement element, String propertyName)
    {
        super(element, new TextRange(0, propertyName.length()));
        this.propertyName = propertyName;
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode)
    {
        PsiElement parent = this.myElement.getParent();
        if (!(this.myElement instanceof KlassPropertyName))
        {
            return new ResolveResult[]{};
        }

        if (parent instanceof KlassExpressionProperty)
        {
            KlassExpressionProperty expressionProperty = (KlassExpressionProperty) parent;
            KlassCriteriaType       criteriaType       = expressionProperty.getCriteriaType();
            KlassKlassName          klassName          = criteriaType.getKlassName();

            if (klassName == null)
            {
                KlassKlass klass =
                        PsiTreeUtil.getParentOfType(this.myElement, KlassKlass.class);

                if (klass != null)
                {
                    return this.getKlassResolveResults(klass);
                }

                KlassServiceGroup serviceGroup =
                        PsiTreeUtil.getParentOfType(this.myElement, KlassServiceGroup.class);
                if (serviceGroup != null)
                {
                    PsiReference reference    = serviceGroup.getKlassName().getReference();
                    KlassKlass   serviceKlass = (KlassKlass) reference.resolve();
                    if (serviceKlass == null)
                    {
                        return new ResolveResult[]{};
                    }
                    return this.getKlassResolveResults(serviceKlass);
                }

                KlassAssociation association =
                        PsiTreeUtil.getParentOfType(this.myElement, KlassAssociation.class);

                if (association != null)
                {
                    KlassAssociationEnd associationEnd = association.getAssociationEndList().get(0);
                    KlassKlassReference klassReference =
                            (KlassKlassReference) associationEnd.getKlassName().getReference();
                    KlassKlass klassKlass = (KlassKlass) klassReference.resolve();

                    if (klassKlass == null)
                    {
                        return new ResolveResult[]{};
                    }

                    ResolveResult[] resolveResults = klassKlass.getMemberList()
                            .stream()
                            .filter(klassMember -> klassMember.getName().equals(this.propertyName))
                            .map(PsiElementResolveResult::new)
                            .toArray(ResolveResult[]::new);
                    return this.getTemporalResolveResults(klass, resolveResults);
                }
            }
            else
            {
                PsiReference klassNameReference = klassName.getReference();
                PsiElement   resolve            = klassNameReference.resolve();
                if (resolve instanceof KlassKlass)
                {
                    KlassKlass klass = (KlassKlass) resolve;
                    return this.getKlassResolveResults(klass);
                }
                if (resolve instanceof KlassEnumeration)
                {
                    KlassEnumeration enumeration = (KlassEnumeration) resolve;
                    return this.getEnumerationResolveResults(enumeration);
                }
                else if (resolve != null)
                {
                    throw new AssertionError(resolve);
                }
            }
        }
        else if (parent instanceof KlassOrderByProperty)
        {
            KlassServiceGroup klassServiceGroup =
                    PsiTreeUtil.getParentOfType(this.myElement, KlassServiceGroup.class);
            if (klassServiceGroup != null)
            {
                PsiReference reference    = klassServiceGroup.getKlassName().getReference();
                KlassKlass   serviceKlass = (KlassKlass) reference.resolve();
                return this.getKlassResolveResults(serviceKlass);
            }

            KlassAssociationEnd associationEnd =
                    PsiTreeUtil.getParentOfType(this.myElement, KlassAssociationEnd.class);
            KlassParameterizedProperty parameterizedProperty =
                    PsiTreeUtil.getParentOfType(this.myElement, KlassParameterizedProperty.class);

            if (associationEnd != null)
            {
                KlassKlassName klassName          = associationEnd.getKlassName();
                PsiReference   klassNameReference = klassName.getReference();
                KlassKlass     klassKlass         = (KlassKlass) klassNameReference.resolve();

                if (klassKlass != null)
                {
                    return this.getKlassResolveResults(klassKlass);
                }
            }
            else if (parameterizedProperty != null)
            {
                KlassKlassName klassName          = parameterizedProperty.getKlassName();
                PsiReference   klassNameReference = klassName.getReference();
                KlassKlass     klassKlass         = (KlassKlass) klassNameReference.resolve();

                if (klassKlass != null)
                {
                    return this.getKlassResolveResults(klassKlass);
                }
            }
        }
        else if (parent instanceof KlassProjectionLeafNode)
        {
            KlassProjectionLeafNode projectionLeafNode   = (KlassProjectionLeafNode) parent;
            PsiElement              projectionNodeParent = projectionLeafNode.getParent();
            if (projectionNodeParent instanceof KlassProjection)
            {
                KlassProjection projection     = (KlassProjection) projectionNodeParent;
                PsiReference    klassReference = projection.getKlassName().getReference();
                KlassKlass      klassKlass     = (KlassKlass) klassReference.resolve();

                if (klassKlass != null)
                {
                    return this.getKlassResolveResults(klassKlass);
                }
            }
            else if (projectionNodeParent instanceof KlassProjectionAssociationEndNode)
            {
                KlassProjectionAssociationEndNode associationEndNode =
                        (KlassProjectionAssociationEndNode) projectionNodeParent;
                KlassAssociationEndName associationEndName =
                        associationEndNode.getAssociationEndName();

                PsiReference        associationEndReference = associationEndName.getReference();
                KlassAssociationEnd klassAssociationEnd     = (KlassAssociationEnd) associationEndReference.resolve();

                if (klassAssociationEnd != null)
                {
                    KlassKlassName klassName          = klassAssociationEnd.getKlassName();
                    PsiReference   klassNameReference = klassName.getReference();
                    KlassKlass     klassKlass         = (KlassKlass) klassNameReference.resolve();

                    if (klassKlass != null)
                    {
                        return this.getKlassResolveResults(klassKlass);
                    }
                }
            }
            else if (projectionNodeParent instanceof KlassProjectionParameterizedPropertyNode)
            {
                KlassProjectionParameterizedPropertyNode parameterizedPropertyNode =
                        (KlassProjectionParameterizedPropertyNode) projectionNodeParent;
                KlassParameterizedPropertyName parameterizedPropertyName =
                        parameterizedPropertyNode.getParameterizedPropertyName();

                PsiReference parameterizedPropertyNameReference = parameterizedPropertyName.getReference();
                KlassParameterizedProperty klassParameterizedProperty =
                        (KlassParameterizedProperty) parameterizedPropertyNameReference.resolve();

                if (klassParameterizedProperty != null)
                {
                    KlassKlassName klassName          = klassParameterizedProperty.getKlassName();
                    PsiReference   klassNameReference = klassName.getReference();
                    KlassKlass     klassKlass         = (KlassKlass) klassNameReference.resolve();

                    if (klassKlass != null)
                    {
                        return this.getKlassResolveResults(klassKlass);
                    }
                }
            }
            else
            {
                throw new AssertionError(projectionNodeParent.getClass());
            }
        }

        return new ResolveResult[]{};
    }

    private ResolveResult[] getEnumerationResolveResults(KlassEnumeration enumeration)
    {
        return enumeration.getEnumerationLiteralList()
                .stream()
                .filter(enumerationLiteral -> enumerationLiteral.getName().equals(this.propertyName))
                .map(PsiElementResolveResult::new)
                .toArray(ResolveResult[]::new);
    }

    @Nullable
    @Override
    public PsiElement resolve()
    {
        ResolveResult[] resolveResults = this.multiResolve(false);
        return resolveResults.length == 1 ? resolveResults[0].getElement() : null;
    }

    @NotNull
    @Override
    public Object[] getVariants()
    {
        PsiElement parent            = this.myElement.getParent();
        PsiElement containingElement = parent.getParent();
        if (containingElement instanceof KlassProjectionAssociationEndNode)
        {
            KlassProjectionAssociationEndNode associationEndNode =
                    (KlassProjectionAssociationEndNode) containingElement;
            KlassAssociationEndName associationEndName = associationEndNode.getAssociationEndName();
            KlassAssociationEndReference associationEndReference =
                    (KlassAssociationEndReference) associationEndName.getReference();
            KlassAssociationEnd associationEnd = (KlassAssociationEnd) associationEndReference.resolve();
            if (associationEnd != null)
            {
                KlassKlassName klassName          = associationEnd.getKlassName();
                PsiReference   klassNameReference = klassName.getReference();
                KlassKlass     klass              = (KlassKlass) klassNameReference.resolve();
                if (klass != null)
                {
                    List<KlassMember> propertyList = klass.getMemberList();
                    return propertyList.stream()
                            .map(member -> LookupElementBuilder.create(member.getName())
                                    .withIcon(AllIcons.Nodes.Property)
                                    .withTypeText(member.getContainingFile().getName())
                                    .withInsertHandler(ProjectionLeafInsertHandler.INSTANCE))
                            .toArray();
                }
            }
        }
        else if (containingElement instanceof KlassProjectionParameterizedPropertyNode)
        {
            KlassProjectionParameterizedPropertyNode parameterizedPropertyNode =
                    (KlassProjectionParameterizedPropertyNode) containingElement;
            KlassParameterizedPropertyName parameterizedPropertyName =
                    parameterizedPropertyNode.getParameterizedPropertyName();
            KlassParameterizedPropertyReference parameterizedPropertyReference =
                    (KlassParameterizedPropertyReference) parameterizedPropertyName.getReference();
            KlassParameterizedProperty parameterizedProperty =
                    (KlassParameterizedProperty) parameterizedPropertyReference.resolve();
            if (parameterizedProperty != null)
            {
                KlassKlassName klassName          = parameterizedProperty.getKlassName();
                PsiReference   klassNameReference = klassName.getReference();
                KlassKlass     klass              = (KlassKlass) klassNameReference.resolve();
                if (klass != null)
                {
                    List<KlassMember> propertyList = klass.getMemberList();
                    return propertyList.stream()
                            .map(klassMember -> LookupElementBuilder.create(klassMember.getName())
                                    .withIcon(AllIcons.Nodes.Property)
                                    .withTypeText(klassMember.getContainingFile().getName())
                                    .withInsertHandler(ProjectionLeafInsertHandler.INSTANCE))
                            .toArray();
                }
            }
        }
        else if (containingElement instanceof KlassProjection)
        {
            KlassKlassName klassName      = ((KlassProjection) containingElement).getKlassName();
            PsiReference   klassReference = klassName.getReference();

            KlassKlass klassKlass = (KlassKlass) klassReference.resolve();
            return this.getKlassResolveResults(klassKlass);
        }
        else if (parent instanceof KlassExpressionProperty)
        {
            KlassExpressionProperty expressionProperty = (KlassExpressionProperty) parent;
            KlassCriteriaType       criteriaType       = expressionProperty.getCriteriaType();
            KlassKlassName          klassName          = criteriaType.getKlassName();

            if (klassName == null)
            {
                KlassKlass klassKlass =
                        PsiTreeUtil.getParentOfType(this.myElement, KlassKlass.class);

                if (klassKlass != null)
                {
                    return this.getKlassMemberLookups(klassKlass);
                }

                KlassServiceGroup klassServiceGroup =
                        PsiTreeUtil.getParentOfType(this.myElement, KlassServiceGroup.class);
                if (klassServiceGroup != null)
                {
                    PsiReference reference    = klassServiceGroup.getKlassName().getReference();
                    KlassKlass   serviceKlass = (KlassKlass) reference.resolve();
                    return this.getKlassMemberLookups(serviceKlass);
                }
            }
            else
            {
                KlassKlassReference klassNameReference = (KlassKlassReference) klassName.getReference();
                PsiElement          resolve            = klassNameReference.resolve();
                if (resolve instanceof KlassKlass)
                {
                    KlassKlass klassKlass = (KlassKlass) resolve;
                    return this.getKlassMemberLookups(klassKlass);
                }
                if (resolve instanceof KlassEnumeration)
                {
                    KlassEnumeration enumeration = (KlassEnumeration) resolve;
                    return this.getEnumerationLiteralLookups(enumeration);
                }
                if (resolve != null)
                {
                    throw new AssertionError();
                }
            }
        }
        else
        {
            throw new AssertionError();
        }

        return new Object[]{};
    }

    private ResolveResult[] getKlassResolveResults(KlassKlass klass)
    {
        ResolveResult[] resolveResults = klass.getMemberList()
                .stream()
                .filter(klassMember -> klassMember.getName().equals(this.propertyName))
                .map(PsiElementResolveResult::new)
                .toArray(ResolveResult[]::new);
        return this.getTemporalResolveResults(klass, resolveResults);
    }

    private Object[] getKlassMemberLookups(KlassKlass klassKlass)
    {
        if (klassKlass == null)
        {
            return new Object[]{};
        }
        return Objects.requireNonNull(klassKlass)
                .getMemberList()
                .stream()
                .map(PsiNamedElement::getName)
                .map(LookupElementBuilder::create)
                .map(lookupElementBuilder -> lookupElementBuilder.withIcon(AllIcons.Nodes.Property))
                .toArray();
    }

    public Object[] getEnumerationLiteralLookups(KlassEnumeration enumeration)
    {
        return Objects.requireNonNull(enumeration)
                .getEnumerationLiteralList()
                .stream()
                .map(PsiNamedElement::getName)
                .map(LookupElementBuilder::create)
                .map(lookupElementBuilder -> lookupElementBuilder.withIcon(AllIcons.Nodes.Enum))
                .toArray();
    }

    public ResolveResult[] getTemporalResolveResults(KlassKlass klass, ResolveResult[] resolveResults)
    {
        if (resolveResults.length != 0)
        {
            return resolveResults;
        }
        if (VALID_PROPERTIES.contains(this.propertyName))
        {
            return this.getTemporalReference(klass, "validTemporal");
        }
        if (SYSTEM_PROPERTIES.contains(this.propertyName))
        {
            return this.getTemporalReference(klass, "systemTemporal");
        }
        return resolveResults;
    }

    private ResolveResult[] getTemporalReference(KlassKlass klass, String temporalKeyword)
    {
        return klass
                .getKeywordOnClassList()
                .stream()
                .filter(keywordOnClass ->
                        keywordOnClass.getText().equals(temporalKeyword)
                                || keywordOnClass.getText().equals("bitemporal"))
                .map(PsiElementResolveResult::new)
                .toArray(ResolveResult[]::new);
    }

    // Based heavily on the insert handler for xml attributes, where it inserts =""
    public static class ProjectionLeafInsertHandler implements InsertHandler<LookupElement>
    {
        public static final ProjectionLeafInsertHandler INSTANCE = new ProjectionLeafInsertHandler();

        @Override
        public void handleInsert(InsertionContext context, LookupElement item)
        {
            Editor editor = context.getEditor();

            Document document    = editor.getDocument();
            int      caretOffset = editor.getCaretModel().getOffset();
            PsiFile  file        = context.getFile();

            CharSequence chars     = document.getCharsSequence();
            boolean      hasQuotes = CharArrayUtil.regionMatches(chars, caretOffset, ":\"");
            if (!hasQuotes)
            {
                PsiElement fileContext = file.getContext();
                String     toInsert    = null;

                if (fileContext != null)
                {
                    if (fileContext.getText().startsWith("\""))
                    {
                        toInsert = ":''";
                    }
                    if (fileContext.getText().startsWith("\'"))
                    {
                        toInsert = ":\"\"";
                    }
                }
                if (toInsert == null)
                {
                    toInsert = ":\"\"";
                }

                document.insertString(caretOffset, caretOffset >= document.getTextLength() ? toInsert + " " : toInsert);

                if (':' == context.getCompletionChar())
                {
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
