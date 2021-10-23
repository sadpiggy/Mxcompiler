package AST;

import Mutil.Position;

public class BoolContainNode extends ContainExprNode{
    private boolean value;

    public BoolContainNode(Position pos_, String text_,boolean value_) {
        super(pos_, text_);
        value = value_;
    }

    @Override
    public Position getPosition() {
        return super.getPosition();
    }

    public boolean getValue(){
        return value;
    }

    @Override
    public String toString() {
        return  "<BoolContainNode>\nvalue: " + value + "\n";
    }

    @Override
    public void acceptVisitor(AstVisitor astVisitor) {
        astVisitor.visit(this);
    }
}
