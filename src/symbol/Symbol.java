package symbol;

import AST.AstNode;
import Mutil.Scope;
import Mutil.type.Type;

public class Symbol {
    private String name;
    private Type type;
    private Scope scope;
    private AstNode define;//define了什么呢=.=

    public Symbol(String name_,Type type_,AstNode define_){
        name = name_;
        type = type_;
        define = define_;
    }

    public String getName() {
        return name;
    }

    public Type getType(){
        return type;
    }

    public AstNode getDefine() {
        return define;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope_) {
        scope = scope_;
    }

    public boolean isFuncSymbol(){
        return false;
    }

    public boolean isClassSymbol(){
        return false;
    }

    public boolean isVarSymbol(){
        return false;
    }
}
