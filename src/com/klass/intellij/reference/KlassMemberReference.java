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
        if (this.myElement instanceof KlassPropertyName)
        {
            KlassExpressionProperty expressionProperty = (KlassExpressionProperty) this.myElement.getParent();
            KlassCriteriaType criteriaType = expressionProperty.getCriteriaType();
            KlassKlassName klassName = criteriaType.getKlassName();

            if (klassName == null)
            {
                KlassKlass klassKlass =
                        PsiTreeUtil.getParentOfType(this.myElement, KlassKlass.class);

                ResolveResult[] resolveResults = klassKlass.getMemberList()
                        .stream()
                        .filter(klassMember -> klassMember.getName().equals(this.propertyName))
                        .map(PsiElementResolveResult::new)
                        .toArray(ResolveResult[]::new);
                return resolveResults;
            }
            PsiReference klassNameReference = klassName.getReference();
            KlassKlass klassKlass = (KlassKlass) klassNameReference.resolve();

            ResolveResult[] resolveResults = klassKlass.getMemberList()
                    .stream()
                    .filter(klassMember -> klassMember.getName().equals(this.propertyName))
                    .map(PsiElementResolveResult::new)
                    .toArray(ResolveResult[]::new);
            return resolveResults;
        }
        return new ResolveResult[]{};
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
        PsiElement containingElement = this.myElement.getParent().getParent();
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
                        Object[] result = propertyList.stream()
                                .map(klassMember -> LookupElementBuilder.create(klassMember.getName())
                                        .withIcon(AllIcons.Nodes.Property)
                                        .withTypeText(klassMember.getContainingFile().getName())
                                        .withInsertHandler(ProjectionLeafInsertHandler.INSTANCE))
                                .toArray();
                        return result;
                    }
                }
            }
        }
        else if (containingElement instanceof KlassProjection)
        {
            KlassKlassName klassName = ((KlassProjection) containingElement).getKlassName();
            PsiReference klassReference = klassName.getReference();

            KlassKlass klassKlass = (KlassKlass) klassReference.resolve();
            ResolveResult[] resolveResults = klassKlass.getMemberList()
                    .stream()
                    .filter(klassMember -> klassMember.getName().equals(this.propertyName))
                    .map(PsiElementResolveResult::new)
                    .toArray(ResolveResult[]::new);
            return resolveResults;
        }

        return new Object[]{};
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
