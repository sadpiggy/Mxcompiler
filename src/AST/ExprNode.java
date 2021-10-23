package AST;

import Mutil.Position;
import Mutil.type.Type;

public abstract class ExprNode extends AstNode{
    private String text;
    private Type type;

    public ExprNode(Position pos_,String text_) {
        super(pos_);
        text = text_;
    }

    @Override
    public Position getPosition() {
        return super.getPosition();
    }

    public String getText() {
        return text;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean isAssignAble(){
        return false;
    }


}
