package AST;

import Mutil.Position;

public class BinaryExprNode extends ExprNode{
    public enum Op {
        Mul, Div, Mod,
        Add, Sub,
        ShiftLeft, ShiftRight,
        Less, Greater, LessEqual, GreaterEqual,
        Equal, NotEqual,
        BitwiseAnd, BitwiseXor, BitwiseOr,
        LogicalAnd, LogicalOr
    }
    private ExprNode lExpr, rExpr;
    private Op op;

    public BinaryExprNode(Position pos_, String text_,ExprNode lExpr_,Op op_,ExprNode rExpr_) {
        super(pos_, text_);
        lExpr = lExpr_;
        op = op_;
        rExpr = rExpr_;
    }

    public Op getOp() {
        return op;
    }

    public ExprNode getrExpr() {
        return rExpr;
    }

    public ExprNode getlExpr() {
        return lExpr;
    }

    public void setrExpr(ExprNode rExpr) {
        this.rExpr = rExpr;
    }

    @Override
    public String toString() {
       return "<BinaryExprNode>\nlhsExpr:\n" + lExpr.toString() + "Op: " + op
               + "\nrhsExpr:\n" + rExpr.toString();
    }

    @Override
    public void acceptVisitor(AstVisitor astVisitor) {
        astVisitor.visit(this);
    }
}
