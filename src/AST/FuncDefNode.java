package AST;

import Mutil.Position;
import Mutil.Scope;

import java.util.ArrayList;

public class FuncDefNode extends ProgramSectionNode{
    private String identifier;
    private TypeNode typeNode;
    private ArrayList<VarDefNode> paramList;
    private BlockStmtNode suit;
    public Scope funcScope;

    public FuncDefNode(Position pos_,String identifier_,TypeNode typeNode_,ArrayList<VarDefNode> paramList_,BlockStmtNode suit_) {
        super(pos_);
        identifier = identifier_;
        typeNode = typeNode_;
        paramList = paramList_;
        suit = suit_;
    }

    @Override
    public Position getPosition() {
        return super.getPosition();
    }

    public String getIdentifier() {
        return identifier;
    }

    public TypeNode getTypeNode() {
        return typeNode;
    }

    public BlockStmtNode getSuit() {
        return suit;
    }

    public ArrayList<VarDefNode> getParamList() {
        return paramList;
    }

    public boolean hasTypeNode(){
        return typeNode != null;
    }

    public boolean hasParamList(){
        return paramList != null;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("<FuncDefNode>\n");
        if(typeNode!=null){stringBuilder.append("typeNode: " + typeNode.toString());}
        else {stringBuilder.append("typeNode: "+"");}
        stringBuilder.append("identifier: " + identifier);
        stringBuilder.append("paramList:\n" + paramList.toString());
        stringBuilder.append("suite:\n" + suit.toString());
        return stringBuilder.toString();
    }

    @Override
    public void acceptVisitor(AstVisitor astVisitor) {
        astVisitor.visit(this);
    }
}
