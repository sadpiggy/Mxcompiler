package Mutil;

import Mutil.error.SemanticError;
import Mutil.type.Type;
import org.antlr.v4.runtime.misc.Pair;

import java.util.HashMap;

public class GlobalScope extends Scope{//这个globalScope为什么要和Scope不一样呢,比如取名
    //之后初始化的时候要记得加上一些内置的函数，之后再加 todo
    private HashMap<String, Type> VarTypes = new HashMap<>();
    private HashMap<String, Type> FuncTypes = new HashMap<>();
    private HashMap<String, Type> ClassTypes = new HashMap<>();

    public GlobalScope(Scope fatherScope_) {
        super(fatherScope_);
    }


    public void add(String name,Type t,Position pos){
        if(t.isVar){addVarType(name,t,pos);return;}
        if(t.isFunc){addFuncType(name,t,pos);return;}
        if(t.isClass){addClassType(name,t,pos);return;}
        throw new SemanticError("没有将所有用到的type附上类别",pos);
    }

    public void addVarType(String name, Type t, Position pos) {
        if (VarTypes.containsKey(name)||ClassTypes.containsKey(name))
            throw new SemanticError("multiple definition of " + name, pos);
        VarTypes.put(name, t);
    }

    public void addFuncType(String name, Type t, Position pos) {
        if (FuncTypes.containsKey(name)||ClassTypes.containsKey(name))
            throw new SemanticError("multiple definition of " + name, pos);
        FuncTypes.put(name, t);
    }

    public void addClassType(String name, Type t, Position pos) {
        if (ClassTypes.containsKey(name)||FuncTypes.containsKey(name)||VarTypes.containsKey(name))
            throw new SemanticError("multiple definition of " + name, pos);
        ClassTypes.put(name, t);
    }

    public Pair<Type,Type> getTypeFromName(String name, Position pos){
        Type typeVar = getVarTypeFromName(name,pos);
        Type typeFunc = getFuncTypeFromName(name,pos);
        Type typeClass = getClassTypeFromName(name,pos);
        if(typeClass != null)return new Pair<Type,Type>(typeClass,null);
        if(typeVar!=null){
            if(typeFunc!=null)return new Pair<Type,Type>(typeVar,typeFunc);
            return new Pair<Type,Type>(typeVar,null);
        }
        if(typeFunc!=null)return new Pair<Type,Type>(typeFunc,null);
        return null;
    }

    public Type getVarTypeFromName(String name, Position pos) {//小心null 我也是迫不得已
        if (VarTypes.containsKey(name)) return VarTypes.get(name);
        //throw new SemanticError("no such type: " + name, pos);
        return null;
    }

    public Type getFuncTypeFromName(String name, Position pos) {//小心null 我也是迫不得已
        if (FuncTypes.containsKey(name)) return FuncTypes.get(name);
        //throw new SemanticError("no such type: " + name, pos);
        return null;
    }

    public Type getClassTypeFromName(String name, Position pos) {//小心null 我也是迫不得已
        if (ClassTypes.containsKey(name)) return ClassTypes.get(name);
        //throw new SemanticError("no such type: " + name, pos);
        return null;
    }
}
