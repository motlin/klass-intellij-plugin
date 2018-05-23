package com.klass.intellij.highlighter;

import java.util.List;

import com.intellij.psi.PsiElement;
import com.klass.intellij.psi.KlassCriteriaAnd;
import com.klass.intellij.psi.KlassCriteriaEdgePoint;
import com.klass.intellij.psi.KlassCriteriaExpression;
import com.klass.intellij.psi.KlassCriteriaGroup;
import com.klass.intellij.psi.KlassCriteriaNative;
import com.klass.intellij.psi.KlassCriteriaOperator;
import com.klass.intellij.psi.KlassCriteriaOr;
import com.klass.intellij.psi.KlassExpressionThisMember;
import com.klass.intellij.psi.KlassExpressionTypeMember;
import com.klass.intellij.psi.KlassVisitor;
import org.jetbrains.annotations.NotNull;

public class DetectExpressionThisMember extends KlassVisitor
{
    private boolean hasThisMember;

    public boolean hasThisMember()
    {
        return this.hasThisMember;
    }

    @Override
    public void visitCriteriaAnd(@NotNull KlassCriteriaAnd criteriaAnd)
    {
        this.visitList(criteriaAnd.getCriteriaOrList());
    }

    @Override
    public void visitCriteriaEdgePoint(@NotNull KlassCriteriaEdgePoint criteriaEdgePoint)
    {
        // Intentionally blank
    }

    @Override
    public void visitCriteriaExpression(@NotNull KlassCriteriaExpression criteriaExpression)
    {
        criteriaExpression.getCriteriaAnd().accept(this);
    }

    @Override
    public void visitCriteriaGroup(@NotNull KlassCriteriaGroup criteriaGroup)
    {
        criteriaGroup.getCriteriaExpression().accept(this);
    }

    @Override
    public void visitCriteriaNative(@NotNull KlassCriteriaNative criteriaNative)
    {
        // Intentionally blank
    }

    @Override
    public void visitCriteriaOperator(@NotNull KlassCriteriaOperator criteriaOperator)
    {
        criteriaOperator.getSourceExpressionValue().getExpressionValue().accept(this);
        criteriaOperator.getTargetExpressionValue().getExpressionValue().accept(this);
    }

    @Override
    public void visitCriteriaOr(@NotNull KlassCriteriaOr criteriaOr)
    {
        this.visitList(criteriaOr.getAtomicCriteriaList());
    }

    @Override
    public void visitExpressionThisMember(@NotNull KlassExpressionThisMember expressionThisMember)
    {
        this.hasThisMember = true;
    }

    @Override
    public void visitExpressionTypeMember(@NotNull KlassExpressionTypeMember o)
    {
        // Intentionally blank
    }

    @Override
    public void visitPsiElement(@NotNull PsiElement psiElement)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".visitPsiElement() not implemented yet");
    }

    private void visitList(@NotNull List<? extends PsiElement> elements)
    {
        for (PsiElement element : elements)
        {
            if (this.hasThisMember)
            {
                return;
            }
            element.accept(this);
        }
    }
}
