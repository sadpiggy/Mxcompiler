package AST;

import Mutil.Position;

public class IdExprNode extends ExprNode{
    private String identifier;

    public IdExprNode(Position pos_, String text_,String identifier_) {
        super(pos_, text_);
        identifier = identifier_;
    }

    @Override
    public Position getPosition() {
        return super.getPosition();
    }

    public String getIdentifier() {
        return identifier;
    }

    public boolean isAssignAble(){
        return true;
    }

    @Override
    public String toString() {
        return "<IdExprNode>\nidentifier: " + identifier + "\n";
    }

    @Override
    public void acceptVisitor(AstVisitor astVisitor) {
        astVisitor.visit(this);
    }
}
