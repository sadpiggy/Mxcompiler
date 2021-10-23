package AST;

import Mutil.Position;

abstract public class TypeNode extends AstNode {
    public String type_name;
    public int num;

    TypeNode(Position pos_,String type_name_,int num_) {
        super(pos_);
        type_name = type_name_;
        num = num_;
    }

    @Override
    public String toString() {
        return "<TypeNode>\n" +
                "type_name: " + type_name + "\n";
    }

    public String getTypename(){
        return type_name;
    }

    public int getNum(){
        return num;
    }
}
