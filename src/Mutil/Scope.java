package Mutil;

import Mutil.error.SemanticError;
import Mutil.type.Type;
import org.antlr.v4.runtime.misc.Pair;

import java.util.HashMap;
//注意:变量不支持前向引用,但是函数和类支持前向引用
//变量与函数之间允许重名，其他都不允许
//千古风流八咏楼，江山留与后人愁。水通南国三千里，气压江城十四州。
public class Scope {//可以把基类转换为非基类吗？？？
    public HashMap<String, Type>VarMembers;
    public HashMap<String, Type>FuncMembers;
    public HashMap<String, Type>ClassMembers;
    private Scope fatherScope;
    //下面这三个要从父辈那里继承
    public boolean inLoop;
    public boolean inFunc;
    public boolean inClass;
    public boolean inLambda;
    public Type currentLambdaType;
    //public HashMap<String, register> entities = new HashMap<>();

    public Scope(Scope fatherScope_){
        fatherScope =fatherScope_;
        //尽量做到加进了就初始化，如果没有立即初始化，之后也应该要记
        VarMembers = new HashMap<>();
        FuncMembers = new HashMap<>();
        ClassMembers = new HashMap<>();
        inFunc = false;
        inLoop = false;
        inClass = false;
        inLambda = false;
        currentLambdaType = new Type();
        currentLambdaType.isVar = true;
        currentLambdaType.isVoid = true;
        currentLambdaType.typeName = "void";
    }

    public Scope getFatherScope() {
        return fatherScope;
    }

    public void put(String name,Type t,Position pos){//所以以后新建type的时候，要记得赋值
        if (t.isVar){
            putVar(name, t,pos);
            return;
        }
        if (t.isFunc){
            putFunc(name, t,pos);
            return;
        }
        putClass(name, t,pos);

    }

    public void putVar(String name,Type t,Position pos){
        if (VarMembers.containsKey(name)||ClassMembers.containsKey(name)){throw new SemanticError("variable redefine",pos);}
        VarMembers.put(name,t);
    }

    public void putClass(String name,Type t,Position pos){
        if (ClassMembers.containsKey(name)||VarMembers.containsKey(name)||FuncMembers.containsKey(name))throw new SemanticError("class redefine",pos);
        ClassMembers.put(name,t);
    }

    public void putFunc(String name,Type t,Position pos){
        if (FuncMembers.containsKey(name)||ClassMembers.containsKey(name))throw new SemanticError("func redefine",pos);
        FuncMembers.put(name,t);
    }

    public boolean contain(String name,boolean lookUpon){
        if(containVar(name,lookUpon)||containFunc(name,lookUpon)||containClass(name,lookUpon))return true;
        return false;
    }

    //要不要写个整体的contain之类的？
    public boolean containVar(String name, boolean lookUpon) {
        if (VarMembers.containsKey(name)) return true;
        else if (fatherScope != null && lookUpon)
            return fatherScope.containVar(name, true);
        else return false;
    }

    public boolean containFunc(String name, boolean lookUpon) {
        if (FuncMembers.containsKey(name)) return true;
        else if (fatherScope != null && lookUpon)
            return fatherScope.containFunc(name, true);
        else return false;
    }

    public boolean containClass(String name, boolean lookUpon) {
        if (ClassMembers.containsKey(name)) return true;
        else if (fatherScope != null && lookUpon)
            return fatherScope.containClass(name, true);
        else return false;
    }


    public Pair<Type,Type> getType(String name, boolean lookUpon){//如果pair第一个返回值为null,那么久表示没有找到,但可能有两个，因为var 和 func 可以同时纯在,and i will let var be first.and if null,return null
        Type typeVar = getVarType(name,lookUpon);
        Type typeFunc = getFuncType(name,lookUpon);
        Type typeClass = getClassType(name,lookUpon);
        if(typeClass!=null)return new Pair<Type,Type>(typeClass,null);
        if(typeVar!=null){
            if(typeFunc!=null)return new Pair<Type,Type>(typeVar,typeFunc);
            return new Pair<Type,Type>(typeVar,null);
        }
        if(typeFunc!=null)return new Pair<Type,Type>(typeFunc,null);
        return null;
    }
    //以下三个函数纯属傻逼
    public Type getVarType(String name, boolean lookUpon) {
        if (VarMembers.containsKey(name)) return VarMembers.get(name);
        else if (fatherScope != null && lookUpon)
            return fatherScope.getVarType(name, true);
        return null;
    }

    public Type getFuncType(String name, boolean lookUpon) {
        if (FuncMembers.containsKey(name)) return FuncMembers.get(name);
        else if (fatherScope != null && lookUpon)
            return fatherScope.getFuncType(name, true);
        return null;
    }

    public Type getClassType(String name, boolean lookUpon) {
        if (ClassMembers.containsKey(name)) return ClassMembers.get(name);
        else if (fatherScope != null && lookUpon)
            return fatherScope.getClassType(name, true);
        return null;
    }
}
