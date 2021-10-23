package AST;

import Mutil.Position;

public class NameTypeNode extends TypeNode{
    NameTypeNode(Position pos_, String type_name_) {
        super(pos_, type_name_,0);
    }

    @Override
    public Position getPosition() {
        return super.getPosition();
    }

    @Override
    public String toString() {
        return "<NameTypeNode>\n" +
                "type_name: " + type_name + "\n";
    }

    @Override
    public void acceptVisitor(AstVisitor astVisitor) {
        astVisitor.visit(this);
    }

    @Override
    public int getNum(){
        return super.getNum();
    }
}
