package IR;

import AST.AstNode;

import java.util.HashMap;

public class ScopeForBuild {
    public HashMap<String, AstNode>defNodes;
    public ScopeForBuild fatherScope;

    public ScopeForBuild(){
        defNodes = new HashMap<>();
        fatherScope = null;
    }

    public ScopeForBuild(ScopeForBuild fatherScope){
        defNodes = new HashMap<>();
        this.fatherScope = fatherScope;
    }

    public AstNode getNode(String name){
       if (defNodes.containsKey(name))return defNodes.get(name);
       if (fatherScope!=null)return fatherScope.getNode(name);
       return null;
    }

    public void addNode(String name,AstNode node){
        defNodes.put(name,node);
    }

}
