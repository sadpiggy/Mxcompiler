package AST;

import Mutil.Position;
import Mutil.type.Type;

public class AssignExprNode extends ExprNode{
    private ExprNode lExpr;
    private ExprNode rExpr;

    public AssignExprNode(Position pos_, String text_,ExprNode lExpr_,ExprNode rExpr_) {
        super(pos_, text_);
        lExpr = lExpr_;
        rExpr = rExpr_;
    }

    @Override
    public Position getPosition() {
        return super.getPosition();
    }

    @Override
    public String getText() {
        return super.getText();
    }

    public ExprNode getlExpr() {
        return lExpr;
    }

    public ExprNode getrExpr() {
        return rExpr;
    }

    @Override
    public String toString() {
        return "<AssignExprNode>\nlhsExpr:\n" + lExpr.toString() + "rhsExpr:\n" + rExpr.toString();
    }

    @Override
    public void acceptVisitor(AstVisitor astVisitor) {
        astVisitor.visit(this);
    }
}
