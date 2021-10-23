package AST;

import Mutil.Position;
import Mutil.Scope;

import java.util.ArrayList;

public class ClassDefNode extends ProgramSectionNode{
    private String identifier;
    private ArrayList<FuncDefNode> methods;
    private ArrayList<VarDefNode> members;
    private FuncDefNode classConstructor;
    public Scope classScope;

    public ClassDefNode(Position pos_,String identifier_,ArrayList<FuncDefNode>methods_,ArrayList<VarDefNode>members_,FuncDefNode classConstructor_) {
        super(pos_);
        identifier = identifier_;
        methods = methods_;
        members = members_;
        classConstructor = classConstructor_;
    }

    @Override
    public Position getPosition() {
        return super.getPosition();
    }

    public String getIdentifier() {
        return identifier;
    }

    public FuncDefNode getClassConstructor() {
        return classConstructor;
    }

    public ArrayList<FuncDefNode> getMethods() {
        return methods;
    }

    public ArrayList<VarDefNode> getMembers() {
        return members;
    }

    public boolean hasConstructor(){
        return classConstructor != null;
    }

    public String toString(){
        StringBuilder stringBuilder = new StringBuilder("<ClassDefNode>\n"+"identifier: " + identifier + "\n"+"methods:\n");
        for(FuncDefNode it: methods) stringBuilder.append(it.toString());
        stringBuilder.append("members:\n");
        for(VarDefNode it: members) stringBuilder.append(it.toString());
        if(classConstructor != null) stringBuilder.append("constructor: " + classConstructor.toString() + '\n');
        return stringBuilder.toString();
    }

    @Override
    public void acceptVisitor(AstVisitor astVisitor) {
        astVisitor.visit(this);
    }
}
