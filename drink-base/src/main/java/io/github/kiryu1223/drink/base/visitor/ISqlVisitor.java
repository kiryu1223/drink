package io.github.kiryu1223.drink.base.visitor;

import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.expressionTree.expressions.*;

public interface ISqlVisitor {
    ISqlExpression visit(BinaryExpression binaryExpression);
    ISqlExpression visit(UnaryExpression unaryExpression);
    ISqlExpression visit(ConstantExpression constantExpression);
    ISqlExpression visit(FieldSelectExpression fieldSelectExpression);
    ISqlExpression visit(MethodCallExpression methodCallExpression);
    ISqlExpression visit(ParameterExpression parameterExpression);
    ISqlExpression visit(NewExpression newExpression);
    ISqlExpression visit(BlockExpression blockExpression);
    ISqlExpression visit(LambdaExpression<?> lambdaExpression);
    ISqlExpression visit(VariableExpression variableExpression);
    ISqlExpression visit(NewArrayExpression newArrayExpression);
    ISqlExpression visit(IndexExpression indexExpression);
    ISqlExpression visit(AssignExpression assignExpression);
    ISqlExpression visit(AssignOpExpression assignOpExpression);
    ISqlExpression visit(ReferenceExpression referenceExpression);
    ISqlExpression visit(StaticClassExpression staticClassExpression);
    ISqlExpression visit(ReturnExpression returnExpression);
    ISqlExpression visit(BreakExpression breakExpression);
    ISqlExpression visit(ContinueExpression continueExpression);
    ISqlExpression visit(ConditionalExpression conditionalExpression);
    ISqlExpression visit(IfExpression ifExpression);
    ISqlExpression visit(ParensExpression parensExpression);
    ISqlExpression visit(ForeachExpression foreachExpression);
    ISqlExpression visit(ForExpression forExpression);
    ISqlExpression visit(WhileExpression whileExpression);
    ISqlExpression visit(SwitchExpression switchExpression);
    ISqlExpression visit(CaseExpression caseExpression);
    ISqlExpression visit(CatchExpression catchExpression);
    ISqlExpression visit(TryExpression tryExpression);
    ISqlExpression visit(ThrowExpression throwExpression);
    ISqlExpression visit(TypeCastExpression typeCastExpression);
}
