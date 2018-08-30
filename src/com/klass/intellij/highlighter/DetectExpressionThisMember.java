package com.klass.intellij.highlighter;

import com.intellij.psi.PsiElement;
import com.klass.intellij.psi.KlassAtomicCriteria;
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
    public void visitAtomicCriteria(@NotNull KlassAtomicCriteria atomicCriteria)
    {
        atomicCriteria.accept(this);
    }

    @Override
    public void visitCriteriaAnd(@NotNull KlassCriteriaAnd criteriaAnd)
    {
        if (this.hasThisMember)
        {
            return;
        }

        this.visitCriteriaOr(criteriaAnd.getCriteriaOr());

        if (this.hasThisMember)
        {
            return;
        }

        if (criteriaAnd.getCriteriaAnd() != null)
        {
            this.visitCriteriaAnd(criteriaAnd.getCriteriaAnd());
        };
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
        if (this.hasThisMember)
        {
            return;
        }

        criteriaOr.getAtomicCriteria().accept(this);

        if (this.hasThisMember)
        {
            return;
        }

        if (criteriaOr.getCriteriaOr() != null)
        {
            this.visitCriteriaOr(criteriaOr.getCriteriaOr());
        }
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
}
