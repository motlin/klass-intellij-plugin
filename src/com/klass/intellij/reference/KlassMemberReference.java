package com.klass.intellij.reference;

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
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.text.CharArrayUtil;
import com.klass.intellij.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class KlassMemberReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference
{
    private final String propertyName;

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
            KlassCriteriaType criteriaType = expressionProperty.getCriteriaType();
            KlassKlassName klassName = criteriaType.getKlassName();

            if (klassName == null)
            {
                KlassKlass klassKlass =
                        PsiTreeUtil.getParentOfType(this.myElement, KlassKlass.class);

                if (klassKlass != null)
                {
                    return this.getKlassResolveResults(klassKlass);
                }

                KlassServiceGroup klassServiceGroup =
                        PsiTreeUtil.getParentOfType(this.myElement, KlassServiceGroup.class);
                if (klassServiceGroup != null)
                {
                    PsiReference reference = klassServiceGroup.getKlassName().getReference();
                    KlassKlass serviceKlass = (KlassKlass) reference.resolve();
                    return this.getKlassResolveResults(serviceKlass);
                }
            }
            else
            {
                PsiReference klassNameReference = klassName.getReference();
                KlassKlass klassKlass = (KlassKlass) klassNameReference.resolve();

                return this.getKlassResolveResults(klassKlass);
            }
        }
        else if (parent instanceof KlassOrderByProperty)
        {
            KlassServiceGroup klassServiceGroup =
                    PsiTreeUtil.getParentOfType(this.myElement, KlassServiceGroup.class);
            if (klassServiceGroup != null)
            {
                PsiReference reference = klassServiceGroup.getKlassName().getReference();
                KlassKlass serviceKlass = (KlassKlass) reference.resolve();
                return this.getKlassResolveResults(serviceKlass);
            }

            KlassAssociationEnd associationEnd =
                    PsiTreeUtil.getParentOfType(this.myElement, KlassAssociationEnd.class);
            KlassParameterizedProperty parameterizedProperty =
                    PsiTreeUtil.getParentOfType(this.myElement, KlassParameterizedProperty.class);

            if (associationEnd != null)
            {
                KlassKlassName klassName = associationEnd.getKlassName();
                PsiReference klassNameReference = klassName.getReference();
                KlassKlass klassKlass = (KlassKlass) klassNameReference.resolve();

                if (klassKlass != null)
                {
                    return this.getKlassResolveResults(klassKlass);
                }
            }
            else if (parameterizedProperty != null)
            {
                KlassKlassName klassName = parameterizedProperty.getKlassName();
                PsiReference klassNameReference = klassName.getReference();
                KlassKlass klassKlass = (KlassKlass) klassNameReference.resolve();

                if (klassKlass != null)
                {
                    return this.getKlassResolveResults(klassKlass);
                }
            }
        }
        else if (parent instanceof KlassProjectionLeafNode)
        {
            KlassProjectionLeafNode projectionLeafNode = (KlassProjectionLeafNode) parent;
            PsiElement projectionNodeParent = projectionLeafNode.getParent();
            if (projectionNodeParent instanceof KlassProjection)
            {
                KlassProjection projection = (KlassProjection) projectionNodeParent;
                PsiReference klassReference = projection.getKlassName().getReference();
                KlassKlass klassKlass = (KlassKlass) klassReference.resolve();

                if (klassKlass != null)
                {
                    return this.getKlassResolveResults(klassKlass);
                }
            }
            else if (projectionNodeParent instanceof KlassProjectionInnerNode)
            {
                KlassProjectionInnerNode projectionInnerNode = (KlassProjectionInnerNode) projectionNodeParent;
                KlassAssociationEndName associationEndName =
                        projectionInnerNode.getAssociationEndName();
                KlassParameterizedPropertyName parameterizedPropertyName =
                        projectionInnerNode.getParameterizedPropertyName();

                if (associationEndName != null)
                {
                    PsiReference associationEndReference = associationEndName.getReference();
                    KlassAssociationEnd klassAssociationEnd = (KlassAssociationEnd) associationEndReference.resolve();

                    if (klassAssociationEnd != null)
                    {
                        KlassKlassName klassName = klassAssociationEnd.getKlassName();
                        PsiReference klassNameReference = klassName.getReference();
                        KlassKlass klassKlass = (KlassKlass) klassNameReference.resolve();

                        if (klassKlass != null)
                        {
                            return this.getKlassResolveResults(klassKlass);
                        }
                    }
                }
                else if (parameterizedPropertyName != null)
                {
                    PsiReference parameterizedPropertyNameReference = parameterizedPropertyName.getReference();
                    KlassParameterizedProperty klassParameterizedProperty =
                            (KlassParameterizedProperty) parameterizedPropertyNameReference.resolve();

                    if (klassParameterizedProperty != null)
                    {
                        KlassKlassName klassName = klassParameterizedProperty.getKlassName();
                        PsiReference klassNameReference = klassName.getReference();
                        KlassKlass klassKlass = (KlassKlass) klassNameReference.resolve();

                        if (klassKlass != null)
                        {
                            return this.getKlassResolveResults(klassKlass);
                        }
                    }
                }
            }
        }

        return new ResolveResult[]{};
    }

    private ResolveResult[] getKlassResolveResults(KlassKlass klassKlass)
    {
        return klassKlass.getMemberList()
                .stream()
                .filter(klassMember -> klassMember.getName().equals(this.propertyName))
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
        PsiElement parent = this.myElement.getParent();
        PsiElement containingElement = parent.getParent();
        if (containingElement instanceof KlassProjectionInnerNode)
        {
            KlassAssociationEndName associationEndName =
                    ((KlassProjectionInnerNode) containingElement).getAssociationEndName();
            PsiReference reference = associationEndName.getReference();
            if (reference != null)
            {
                KlassAssociationEnd klassAssociationEnd = (KlassAssociationEnd) reference.resolve();
                if (klassAssociationEnd != null)
                {
                    KlassKlassName klassName = klassAssociationEnd.getKlassName();
                    PsiReference klassNameReference = klassName.getReference();
                    KlassKlass klassKlass = (KlassKlass) klassNameReference.resolve();
                    if (klassKlass != null)
                    {
                        List<KlassMember> propertyList = klassKlass.getMemberList();
                        return propertyList.stream()
                                .map(klassMember -> LookupElementBuilder.create(klassMember.getName())
                                        .withIcon(AllIcons.Nodes.Property)
                                        .withTypeText(klassMember.getContainingFile().getName())
                                        .withInsertHandler(ProjectionLeafInsertHandler.INSTANCE))
                                .toArray();
                    }
                }
            }
        }
        else if (containingElement instanceof KlassProjection)
        {
            KlassKlassName klassName = ((KlassProjection) containingElement).getKlassName();
            PsiReference klassReference = klassName.getReference();

            KlassKlass klassKlass = (KlassKlass) klassReference.resolve();
            return this.getKlassResolveResults(klassKlass);
        }
        else if (parent instanceof KlassExpressionProperty)
        {
            KlassExpressionProperty expressionProperty = (KlassExpressionProperty) parent;
            KlassCriteriaType criteriaType = expressionProperty.getCriteriaType();
            KlassKlassName klassName = criteriaType.getKlassName();

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
                    PsiReference reference = klassServiceGroup.getKlassName().getReference();
                    KlassKlass serviceKlass = (KlassKlass) reference.resolve();
                    return this.getKlassMemberLookups(serviceKlass);
                }
            }
            else
            {
                PsiReference klassNameReference = klassName.getReference();
                KlassKlass klassKlass = (KlassKlass) klassNameReference.resolve();
                return this.getKlassMemberLookups(klassKlass);
            }
        }

        return new Object[]{};
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

    // Based heavily on the insert handler for xml attributes, where it inserts =""
    public static class ProjectionLeafInsertHandler implements InsertHandler<LookupElement>
    {
        public static final ProjectionLeafInsertHandler INSTANCE = new ProjectionLeafInsertHandler();

        @Override
        public void handleInsert(InsertionContext context, LookupElement item)
        {
            Editor editor = context.getEditor();

            Document document = editor.getDocument();
            int caretOffset = editor.getCaretModel().getOffset();
            PsiFile file = context.getFile();

            CharSequence chars = document.getCharsSequence();
            boolean hasQuotes = CharArrayUtil.regionMatches(chars, caretOffset, ":\"");
            if (!hasQuotes)
            {
                PsiElement fileContext = file.getContext();
                String toInsert = null;

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
