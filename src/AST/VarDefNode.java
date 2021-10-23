package AST;

import Mutil.Position;

public class VarDefNode extends ProgramSectionNode{//如果传出去的是引用，那么，会被在外面修改吗？？？如果我不想让他在外边被修改，该怎么办？todo
    private TypeNode typeNode;
    private String identifier;
    private ExprNode init;

    public VarDefNode(Position pos_,TypeNode typeNode_,String identifier_,ExprNode init_) {
        super(pos_);
        typeNode = typeNode_;
        identifier = identifier_;
        init = init_;
    }

    public boolean hasInit(){
        return init != null;
    }

    public void setTypeNode(TypeNode typeNode_){
        typeNode = typeNode_;
    }

    @Override
    public Position getPosition() {
        return super.getPosition();
    }

    public String getIdentifier() {
        return identifier;
    }

    public ExprNode getInit() {
        return init;
    }

    public TypeNode getTypeNode() {
        return typeNode;
    }

    public String toString(){
        return "<VarNode>:\ntypeNode: " + typeNode.toString() + "identifier: " + identifier +
                (this.hasInit()? "\ninitExpr:\n" + init.toString(): "") + "\n";
    }

    @Override
    public void acceptVisitor(AstVisitor astVisitor) {
        astVisitor.visit(this);
    }
}
