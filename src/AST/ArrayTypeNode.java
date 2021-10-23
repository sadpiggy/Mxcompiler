package AST;

import Mutil.Position;

public class ArrayTypeNode extends TypeNode{//
    private TypeNode NameTypeNode;
//    private int num;

    ArrayTypeNode(Position pos_, TypeNode NameTypeNode_,int num_) {
        super(pos_,NameTypeNode_.type_name,num_) ;
        NameTypeNode = NameTypeNode_;
    }

    @Override
    public int getNum() {
        return super.num;
    }


    @Override
    public Position getPosition() {
        return super.getPosition();
    }

    @Override
    public String getTypename(){
        return super.getTypename();
    }

    public TypeNode getNameTypeNode() {
        return NameTypeNode;
    }

    @Override
    public String toString() {
        return  "<ArrayTypeNode>\nNameTypeNode: " + NameTypeNode.toString() + " num: " + super.num + "\n";
    }

    @Override
    public void acceptVisitor(AstVisitor astVisitor) {
        astVisitor.visit(this);
    }
}
