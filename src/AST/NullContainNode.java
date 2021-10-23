package AST;

import Mutil.Position;

public class NullContainNode extends ContainExprNode{
    public NullContainNode(Position pos_) {
        super(pos_, "null");
    }

    @Override
    public Position getPosition() {
        return super.getPosition();
    }

    @Override
    public String toString() {
        return "<NullContainNode>\n";
    }

    @Override
    public void acceptVisitor(AstVisitor astVisitor) {
        astVisitor.visit(this);
    }
}
