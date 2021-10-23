//package Mutil;
//
//import AST.AstNode;
//import Mutil.error.SemanticError;
//import symbol.ClassSymbol;
//import symbol.FuncSymbol;
//import symbol.Symbol;
//import symbol.VarSymbol;
//
//import java.util.LinkedHashMap;
//import java.util.Map;
//
//public abstract class BaseScope implements Scope {
//    public Map<String, Symbol>SymbolMap = new LinkedHashMap<>();//这又是什么意思呢
//    private String name;
//    private Scope FatherScope;
//
//    BaseScope(String name_,Scope FatherScope_){
//        name = name_;
//        FatherScope = FatherScope_;
//    }
//
//    @Override
//    public String getScopeName(){
//        return name;
//    }
//
//    @Override
//    public Scope getFatherScope(){
//        return FatherScope;
//    }
//
//    @Override
//    public void defVar(VarSymbol varSymbol_){
//        if(SymbolMap.containsKey(varSymbol_.getName())){
//            throw new SemanticError("repeated identifiers",varSymbol_.getDefine().pos);
//        }
//        varSymbol_.setScope(this);
//        SymbolMap.put(varSymbol_.getName(),varSymbol_);
//    }
//
//    @Override
//    public void defCLass(ClassSymbol classSymbol_){
//        if(SymbolMap.containsKey(classSymbol_.getName())){
//            throw new SemanticError("repeated identifiers",classSymbol_.getDefine().pos);
//        }
//        classSymbol_.setScope(this);
//        SymbolMap.put(classSymbol_.getName(),classSymbol_);
//    }
//
//    @Override
//    public void defFunc(FuncSymbol funcSymbol_){
//        if(SymbolMap.containsKey(funcSymbol_.getName())){
//            throw new SemanticError("repeated identifiers",funcSymbol_.getDefine().pos);
//        }
//        funcSymbol_.setScope(this);
//        SymbolMap.put(funcSymbol_.getName(),funcSymbol_);
//    }
//
//}
