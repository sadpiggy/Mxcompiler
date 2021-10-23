package AST;

import Mutil.Position;
import Mutil.Scope;

public abstract class AstNode {
    public Position pos;
    //public Scope scope;

    public AstNode(Position pos_){
        pos = pos_;
    }

    public Position getPosition(){
        return pos;
    }

    abstract public void acceptVisitor(AstVisitor astVisitor);

//    public Scope getScope() {
//        return scope;
//    }
//
//    public void setScope(Scope scope) {
//        this.scope = scope;
//    }
}
