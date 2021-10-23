package AST;

import Mutil.Position;

public class StringContainNode extends ContainExprNode{
    private String value;

    public StringContainNode(Position pos_, String text_,String value_) {
        super(pos_, text_);
        value = value_;
    }

    public String getValue() {
        return value;
    }

    @Override
    public Position getPosition() {
        return super.getPosition();
    }

    @Override
    public String toString() {
        return "<StringContainNode>\nvalue: " + value + "\n";
    }

    @Override
    public void acceptVisitor(AstVisitor astVisitor) {
        astVisitor.visit(this);
    }
}
