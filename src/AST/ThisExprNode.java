package AST;

import Mutil.Position;

public class ThisExprNode extends ExprNode{
    public ThisExprNode(Position pos_) {
        super(pos_, "this");
    }

    @Override
    public String toString() {
        return "<ThisExprNode>\n";
    }

    @Override
    public void acceptVisitor(AstVisitor astVisitor) {
        astVisitor.visit(this);
    }
}
