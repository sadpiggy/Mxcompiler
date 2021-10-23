package AST;

import Mutil.Position;

public class SuffixExprNode extends ExprNode{
    public enum Op{
        SufPlus,SufMinus
    }

    private ExprNode exprNode;
    private Op op;

    public SuffixExprNode(Position pos_, String text_,ExprNode exprNode_,Op op_) {
        super(pos_, text_);
        exprNode = exprNode_;
        op = op_;
    }

    public Op getOp() {
        return op;
    }

    public ExprNode getExprNode() {
        return exprNode;
    }

    @Override
    public Position getPosition() {
        return super.getPosition();
    }

    @Override
    public boolean isAssignAble() {
        return false;
    }

    @Override
    public String toString() {
        return "<SuffixExprNode>\nExprNode: " + exprNode.toString()
                + "Op: " + op + "\n";
    }

    @Override
    public void acceptVisitor(AstVisitor astVisitor) {
        astVisitor.visit(this);
    }
}
