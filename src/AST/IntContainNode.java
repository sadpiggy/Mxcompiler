package AST;

import Mutil.Position;

public class IntContainNode extends ContainExprNode{
    private int value;

    public IntContainNode(Position pos_, String text_,int value_) {
        super(pos_, text_);
        value = value_;
    }

    @Override
    public Position getPosition() {
        return super.getPosition();
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
     return "<IntContainNode>\nvalue: " + value + "\n";
    }

    @Override
    public void acceptVisitor(AstVisitor astVisitor) {
        astVisitor.visit(this);
    }
}
