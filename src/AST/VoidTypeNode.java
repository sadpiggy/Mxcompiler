package AST;

import Mutil.Position;

public class VoidTypeNode extends TypeNode{
    VoidTypeNode(Position pos_) {
        super(pos_, "void",0);
    }

    @Override
    public Position getPosition() {
        return super.getPosition();
    }

    @Override
    public String toString() {
        return "<VoidTypeNode>\n";
    }

    @Override
    public void acceptVisitor(AstVisitor astVisitor) {
        astVisitor.visit(this);
    }

    @Override
    public int getNum(){
        return super.num;
    }
}
