package AST;

import Mutil.Position;

public class PrefixExprNode extends ExprNode{
    public enum Op{
        SignPos,SignNeg,
        PrePlusPlus,PreMinusMinus,
        BitWiseNot,LogicalNot
    }

    private Op op;
    private ExprNode exprNode;

    public PrefixExprNode(Position pos_, String text_,Op op_, ExprNode exprNode_) {
        super(pos_, text_);
        op = op_;
        exprNode = exprNode_;
    }

    @Override
    public Position getPosition() {
        return super.getPosition();
    }

    public Op getOp() {
        return op;
    }

    public ExprNode getExprNode() {
        return exprNode;
    }

    @Override
    public String toString() {
        return "<PrefixExprNode>\nOp: \n" + op
                + "\nExprNode: " + exprNode.toString();
    }

    @Override
    public boolean isAssignAble() {
        return op == Op.PreMinusMinus || op == Op.PrePlusPlus;
    }

    @Override
    public void acceptVisitor(AstVisitor astVisitor) {
        astVisitor.visit(this);
    }
}
