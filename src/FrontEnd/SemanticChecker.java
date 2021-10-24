package FrontEnd;

import AST.*;
import Mutil.GlobalScope;
import Mutil.Position;
import Mutil.Scope;
import Mutil.error.SemanticError;
import Mutil.type.Type;
import org.antlr.v4.runtime.misc.Pair;

import java.util.ArrayList;
import java.util.Objects;

public class SemanticChecker implements AstVisitor {
    private Scope currentScope;
    private Scope globalScope;
    private Type currentClassType;//这个应该用得上
    private Type currentFuncType;//这个不知道什么时候用得上//用于funcDef中的初始化以及遍历
    private Type currentVarType;//用一个string是否能完成任务呢？//干嘛用的10/19号我忘记了
    private Type currentExprType;
    private int subScriptNum;//每次遍历完之后都要更新
   // private Type currentLambdaType;

    public SemanticChecker(GlobalScope globalScope_){
        currentScope = globalScope = globalScope_;//symbol collector collect the 顶端作用域
        addStringMethod();
        addBuildInFunc();
    }

    public void addStringMethod(){
        Type stringType = new Type();
        stringType.defineClass();
        //length
        Type methodType = new Type();
        methodType.defineFunc();
        methodType.isFunc = true;
        methodType.typeName = "int";
        methodType.isInt = true;
        stringType.addFuncMembers("length",methodType);//这里放进去的是什么，我改变外面的一整个值，会改变里面的值吗
        //substring(int left,int right)
        methodType = new Type();
        methodType.defineFunc();
        methodType.isFunc = true;
        methodType.isString = true;
        methodType.typeName = "string";
        Type paramType = new Type();
        paramType.typeName = "int";
        paramType.isVar = true;
        paramType.isInt = true;
        methodType.params.add(new Pair<>("left",paramType));
        paramType = new Type();
        paramType.typeName = "int";
        paramType.isVar = true;
        paramType.isInt = true;
        methodType.params.add(new Pair<>("right",paramType));
        stringType.addFuncMembers("substring",methodType);
        //parseInt()
        methodType = new Type();
        methodType.defineFunc();
        methodType.isFunc = true;
        methodType.isInt = true;
        methodType.typeName = "int";
        stringType.addFuncMembers("parseInt",methodType);
        //int ord(int pos)
        methodType = new Type();
        methodType.defineFunc();
        methodType.isFunc = true;
        methodType.isInt = true;
        methodType.typeName = "int";
        paramType = new Type();
        paramType.isInt = true;
        paramType.isVar = true;
        paramType.typeName = "int";
        methodType.params.add(new Pair<>("pos",paramType));
        stringType.addFuncMembers("ord",methodType);

        globalScope.putClass("string",stringType,new Position(0,0));

    }

    public void addBuildInFunc(){
        //void print(string str)
        Type buildInFuncType = new Type();
        buildInFuncType.defineFunc();
        buildInFuncType.isFunc = true;
        buildInFuncType.isVoid = true;
        buildInFuncType.typeName = "void";
        Type paramType = new Type();
        paramType.isVar = true;
        paramType.isString = true;
        paramType.typeName = "string";
        buildInFuncType.params.add(new Pair<>("str",paramType));
        globalScope.putFunc("print",buildInFuncType,new Position(0,0));
        //void println(string str)
        buildInFuncType = new Type();
        buildInFuncType.defineFunc();
        buildInFuncType.isFunc = true;
        buildInFuncType.isVoid = true;
        buildInFuncType.typeName = "void";
        paramType = new Type();
        paramType.isVar = true;
        paramType.isString = true;
        paramType.typeName = "string";
        buildInFuncType.params.add(new Pair<>("str",paramType));
        globalScope.putFunc("println",buildInFuncType,new Position(0,0));
        //void printInt(int n)
        buildInFuncType = new Type();
        buildInFuncType.defineFunc();
        buildInFuncType.isFunc = true;
        buildInFuncType.isVoid = true;
        buildInFuncType.typeName = "void";
        paramType = new Type();
        paramType.isVar = true;
        paramType.isInt = true;
        paramType.typeName = "int";
        buildInFuncType.params.add(new Pair<>("n",paramType));
        globalScope.putFunc("printInt",buildInFuncType,new Position(0,0));
        //void printlnInt(int n)
        buildInFuncType = new Type();
        buildInFuncType.defineFunc();
        buildInFuncType.isFunc = true;
        buildInFuncType.isVoid = true;
        buildInFuncType.typeName = "void";
        paramType = new Type();
        paramType.isVar = true;
        paramType.isInt = true;
        paramType.typeName = "int";
        buildInFuncType.params.add(new Pair<>("n",paramType));
        globalScope.putFunc("printlnInt",buildInFuncType,new Position(0,0));
        //string getString()
        buildInFuncType = new Type();
        buildInFuncType.defineFunc();
        buildInFuncType.isFunc = true;
        buildInFuncType.isString = true;
        buildInFuncType.typeName = "string";
        globalScope.putFunc("getString",buildInFuncType,new Position(0,0));
        //int getInt()
        buildInFuncType = new Type();
        buildInFuncType.defineFunc();
        buildInFuncType.isFunc = true;
        buildInFuncType.isInt = true;
        buildInFuncType.typeName = "int";
        globalScope.putFunc("getInt",buildInFuncType,new Position(0,0));
        //string toString(int i)
        buildInFuncType = new Type();
        buildInFuncType.defineFunc();
        buildInFuncType.isFunc = true;
        buildInFuncType.isString = true;
        buildInFuncType.typeName = "string";
        paramType = new Type();
        paramType.isVar = true;
        paramType.isInt = true;
        paramType.typeName = "int";
        buildInFuncType.params.add(new Pair<>("i",paramType));
        globalScope.putFunc("toString",buildInFuncType,new Position(0,0));
    }

    @Override
    public void visit(ArrayTypeNode node) {

    }

    //int a; a = 10;//只有在func里面会调用到这个
    @Override
    public void visit(AssignExprNode node) {
        node.getlExpr().acceptVisitor(this);
        Type lExprType = currentExprType;
        node.getrExpr().acceptVisitor(this);
        Type rExprType = currentExprType;
        if (!lExprType.isAssignAble)throw new SemanticError("only lValue can be assigned", node.getPosition());
        if ((!lExprType.isArray)&&(!lExprType.isId)) {
            if (!Objects.equals(lExprType.typeName, rExprType.typeName))
                throw new SemanticError("wrong type in assignExpr", node.getPosition());
        }
        else {
            if (!Objects.equals(lExprType.typeName, rExprType.typeName)) {
                if (!rExprType.isNull)
                    throw new SemanticError("wrong type in assignExpr", node.getPosition());
            }else {
                if (lExprType.numOfArray!= rExprType.numOfArray)throw new SemanticError("wrong type in assignExpr", node.getPosition());
            }
        }

        currentExprType = lExprType;
    }

    //
    @Override
    public void visit(BinaryExprNode node) {
        node.getlExpr().acceptVisitor(this);
        Type lType = currentExprType;
        node.getrExpr().acceptVisitor(this);
        Type rType = currentExprType;

        BinaryExprNode.Op op = node.getOp();

        if (op==BinaryExprNode.Op.Equal||op== BinaryExprNode.Op.NotEqual){
            if (lType.isArray||lType.isNull||lType.isString||lType.isId){
                if (!Objects.equals(lType.typeName, rType.typeName)){
                    if ((!lType.isNull)&&(!rType.isNull))throw new SemanticError("wrong binaryExpr", node.getPosition());
                }else {
                    if (lType.numOfArray!=rType.numOfArray)throw new SemanticError("wrong binaryExpr", node.getPosition());
                }
            }else {
                if (!Objects.equals(lType.typeName, rType.typeName))throw new SemanticError("wrong binaryExpr", node.getPosition());
                if (lType.numOfArray!=rType.numOfArray)throw new SemanticError("wrong binaryExpr", node.getPosition());
            }
            Type type = new Type();
            type.typeName = "bool";
            type.isBool = true;
            type.isAssignAble = false;
            currentExprType = type;
            return;
        }

        if (op== BinaryExprNode.Op.Less||op == BinaryExprNode.Op.LessEqual||op== BinaryExprNode.Op.Greater||op== BinaryExprNode.Op.GreaterEqual){
            if (lType.isArray||rType.isArray)throw new SemanticError("wrong binaryExpr", node.getPosition());
            if (!Objects.equals(lType.typeName, rType.typeName))throw new SemanticError("wrong binaryExpr", node.getPosition());
            if ((!lType.isInt)&&(!lType.isString))throw new SemanticError("wrong binaryExpr", node.getPosition());
            Type type = new Type();
            type.typeName = "bool";
            type.isBool = true;
            type.isAssignAble = false;
            currentExprType = type;
            return;
        }

        if (op== BinaryExprNode.Op.Add){
            if (lType.isArray||rType.isArray)throw new SemanticError("wrong binaryExpr", node.getPosition());
            if (!Objects.equals(lType.typeName, rType.typeName))throw new SemanticError("wrong binaryExpr", node.getPosition());
            if ((!lType.isInt)&&(!lType.isString))throw new SemanticError("wrong binaryExpr", node.getPosition());
            Type type = new Type();
            if (lType.isString){
                type.typeName = "string";
                type.isString = true;
            }else {
                type.typeName = "int";
                type.isInt = true;
            }
            type.isAssignAble = false;
            currentExprType = type;
            return;
        }

        if (op== BinaryExprNode.Op.LogicalAnd||op== BinaryExprNode.Op.LogicalOr){
            if (lType.isArray||rType.isArray)throw new SemanticError("wrong binaryExpr", node.getPosition());
            if (!Objects.equals(lType.typeName, rType.typeName))throw new SemanticError("wrong binaryExpr", node.getPosition());
            if (!lType.isBool)throw new SemanticError("wrong binaryExpr", node.getPosition());
            Type type = new Type();
            type.typeName = "bool";
            type.isBool = true;
            type.isAssignAble = false;
            currentExprType = type;
            return;
        }

        if (lType.isArray||rType.isArray)throw new SemanticError("wrong binaryExpr", node.getPosition());
        if (!Objects.equals(lType.typeName, rType.typeName))throw new SemanticError("wrong binaryExpr", node.getPosition());
        if (!lType.isInt)throw new SemanticError("wrong binaryExpr", node.getPosition());
        Type type = new Type();
        type.typeName = "int";
        type.isInt = true;
        type.isAssignAble = false;
        currentExprType = type;

    }

    @Override
    public void visit(BlockStmtNode node) {
        Scope blockScope = new Scope(currentScope);
        blockScope.inClass = currentScope.inClass;
        blockScope.inFunc = currentScope.inFunc;
        blockScope.inLoop = currentScope.inLoop;
        blockScope.inLambda = currentScope.inLambda;
        currentScope = blockScope;

        //if (currentScope.contain("c",true))System.out.println("mama");

        for(var it : node.getStmtNodes()){
            //因为Scope可能会在Stmt中变化，所以之后要重新赋回来
            //或者换一种，引入新的作用域后，出来的时候自动跳出
            if (it!=null)
            it.acceptVisitor(this);
        }
        currentScope.getFatherScope().currentLambdaType = currentScope.currentLambdaType.clone();
        currentScope = currentScope.getFatherScope();
    }

    @Override
    public void visit(BoolContainNode node) {
        Type type = new Type();
        type.isBool = true;
        type.typeName = "bool";
        type.isAssignAble = false;
        node.setType(type);
        currentExprType = type;
    }

    @Override
    public void visit(BreakStmtNode node) {
        if(!currentScope.inLoop)throw new SemanticError("break can only in loop",node.getPosition());
    }

    //class pig(){} method members class本身应该已经被我处理过了，所以，接下来进入method and member is ok.= . =
    @Override
    public void visit(ClassDefNode node) {
        //currentScope = node.classScope;//不知道这里该怎么处理
        currentScope = new Scope(globalScope);
        node.classScope = currentScope;
        currentScope.inClass = true;
        currentClassType = globalScope.getClassType(node.getIdentifier(),false);
        //将class中的varType funcType放入Scope
        currentClassType.varMembers.forEach((Id,type)->currentScope.putVar(Id,type,type.position));
        currentClassType.funcMembers.forEach((Id,type)->currentScope.putFunc(Id,type,type.position));
        var members = node.getMembers();
        var methods = node.getMethods();
        for(var it : members){
            if(it.getInit()!=null)throw new SemanticError("class member can't have default construction",it.getPosition());
            Type type = currentClassType.varMembers.get(it.getIdentifier());
            if(type.isId){
                if(!currentScope.containClass(type.getTypeName(),true))throw new SemanticError("use an undeclared class to define var",it.getPosition());
            }
        }

        for(var it : methods){
//            if(it.getIdentifier()== node.getIdentifier()){//构造函数
//                if (it.hasTypeNode()||it.hasParamList())throw new SemanticError("classConstructor can't have typeNode or Params",node.getPosition());
//            }
            currentFuncType = currentClassType.funcMembers.get(it.getIdentifier());
            it.acceptVisitor(this);
        }
        currentScope = currentScope.getFatherScope();
    }

    @Override
    public void visit(ContainExprNode node) {
        //应该进不来吧
    }

    @Override
    public void visit(ContinueStmtNode node) {
        if(!currentScope.inLoop)throw new SemanticError("continue can only in loop", node.getPosition());
    }

    @Override
    public void visit(EmptyStmtNode node) {

    }

    @Override
    public void visit(ExprNode node) {

    }

    @Override
    public void visit(ForStmtNode node) {
        //for()这里不引入作用域，后面才引入//错，就是这里就开始引入新作用域了//错，因为for(laLaLa) "laLaLa"里面没有变量定义，所以它不需要新开一个作用域
//        Scope forScope = new Scope(currentScope);
//        forScope.inLoop = currentScope.inLoop;
//        forScope.inFunc = currentScope.inFunc;
//        forScope.inClass = currentScope.inClass;
//        currentScope = forScope;

        ExprNode init = node.getInitExpr();
        ExprNode condition = node.getConditionExpr();
        ExprNode change = node.getChangeExpr();

        if(init!=null){
            init.acceptVisitor(this);
        }

       if (condition!=null) {
           condition.acceptVisitor(this);
           if(currentExprType.isArray||(!currentExprType.isBool))throw new SemanticError("in forStmt conditionExprType can only be bool", node.getPosition());
       }

       if (change!=null)change.acceptVisitor(this);

       boolean oldInLoop = currentScope.inLoop;
       currentScope.inLoop = true;
       node.getStmt().acceptVisitor(this);
       currentScope.inLoop = oldInLoop;

      //  currentScope = currentScope.getFatherScope();
    }

    //cook(a,b)
    @Override
    public void visit(FuncCallExprNode node) {
        if (!currentScope.containFunc(node.getIdentifier(),true))throw new SemanticError("no this func in the program", node.getPosition());
        Type funcType = currentScope.getFuncType(node.getIdentifier(),true);
        if (node.getParams()==null||node.getParams().size()==0){
            if (funcType.params.size()!=0)
                throw new SemanticError("wrong params in funcCall1", node.getPosition());
        }else {

            //System.out.println(funcType.params.size());

            if (funcType.params.size()!=node.getParams().size())throw new SemanticError("wrong params in funcCall2",node.getPosition());
            int i = 0;
            for (var it : node.getParams()){
                it.acceptVisitor(this);
                Type formParamType = funcType.params.get(i).b;
                if (formParamType.isArray||formParamType.isId||formParamType.isString){
                    if(!Objects.equals(currentExprType.typeName, formParamType.typeName)){
                        if (!Objects.equals(currentExprType.typeName, "null"))throw new SemanticError("wrong type of params in funcCall", node.getPosition());
                    }else {
                        if (currentExprType.numOfArray != formParamType.numOfArray)throw new SemanticError("wrong type of params in funcCall", node.getPosition());
                    }
                }else {
                    if ((!Objects.equals(currentExprType.typeName, formParamType.typeName)) || currentExprType.numOfArray != formParamType.numOfArray)
                        throw new SemanticError("wrong type of params in funcCall", node.getPosition());
                }
                i++;
            }
        }
        currentExprType = funcType;
        currentExprType.isAssignAble = false;
    }

    @Override
    public void visit(FuncDefNode node) {
        currentScope = new Scope(currentScope);
        currentScope.inClass = currentScope.getFatherScope().inClass;
        currentScope.inFunc = true;
        node.funcScope = currentScope;

        //currentFuncType.defineFunc();

        if (currentFuncType.isId){
            if (!globalScope.containClass(currentFuncType.typeName,false))throw new SemanticError("no this class in the globalScope", node.getPosition());
        }

        //currentFuncType.defineFunc();

        if (node.getParamList()!=null){
        for(var it: node.getParamList()){
//            if(it.getInit()!=null){
//                throw new SemanticError("param in funcDef can't have init",node.getPosition());
//            }

            visitTypeName(it.getTypeNode().getTypename(),it.getPosition(),it.getTypeNode().getNum());
            //currentFuncType.params.add(new Pair<>(it.getIdentifier(),currentVarType));
            currentScope.putVar(it.getIdentifier(),currentVarType, it.getPosition());//好像没什么用处，因为后面会跳出这个作用域//有用，因为会进入函数体里面
             }
        }
        //比较return的type是否和currentFunc的一样
        node.getSuit().acceptVisitor(this);

        currentScope = currentScope.getFatherScope();

    }

    @Override
    public void visit(IdExprNode node) {

        //if (currentScope.contain("c",true))System.out.println("la");

        String name = node.getIdentifier();
       // System.out.println(name);

        if(!currentScope.containVar(name,true))throw new SemanticError("undeclared var",node.getPosition());
        Type type = currentScope.getVarType(name,true).clone();
        type.isAssignAble = true;
        currentExprType = type;
        node.setType(type);//总觉得没有必要
    }

    //if(cond)
    @Override
    public void visit(IfStmtNode node) {
        //currentScope.inLoop = true;

        if (node.getConditionExpr()==null)throw new SemanticError("conditionExpr in ifExpr can't be empty", node.getPosition());
        node.getConditionExpr().acceptVisitor(this);
        if (currentExprType.isArray||(!currentExprType.isBool))throw new SemanticError("conditionExpr in ifExpr must be bool", node.getPosition());
        node.getTrueStmt().acceptVisitor(this);
        if (node.getFalseStmt()!=null)node.getFalseStmt().acceptVisitor(this);

       // currentScope.inLoop = false;

        //currentScope = currentScope.getFatherScope();
    }

    @Override
    public void visit(IntContainNode node) {
        //这里需要标记position吗？
        Type type = new Type();
        type.isInt = true;
        type.typeName = "int";
        type.isAssignAble = false;
        node.setType(type);//这个不一定是必需的
        currentExprType = type;
    }

    @Override
    public void visit(MemberExprNode node) {//a.pig
        ExprNode objExpr = node.getObjExpr();

//        System.out.println(objExpr.getText());
//        System.out.println(node.getMemberName());

        objExpr.acceptVisitor(this);
//        System.out.println(objExpr.getText());
//        System.out.println(currentExprType.typeName);
        if (currentExprType.isId&&(!currentExprType.isArray)){
            String typeName = currentExprType.typeName;
            Type classType = globalScope.getClassType(typeName,false);
            if(!classType.varMembers.containsKey(node.getMemberName()))throw new SemanticError("there isn't this member in the class",node.getPosition());
            currentExprType = classType.varMembers.get(node.getMemberName()).clone();
            currentExprType.isAssignAble = true;//array算不算assignable?还是说得将她a[2]之后，才assignable
        }else throw new SemanticError("only classVar has member",node.getPosition());

    }

    @Override
    public void visit(MethodExprNode node) {//a.bark(exprList)
        ExprNode objExpr = node.getObjExpr();
        objExpr.acceptVisitor(this);

        if(currentExprType.isArray){//可能之后ir的时候要加以修改吧
            if(!Objects.equals(node.getMethodName(), "size")){
                throw new SemanticError("only method in array is size()",node.getPosition());
            }
            Type type = new Type();
            type.isVar = true;
            type.isInt = true;
            type.typeName = "int";
            type.isAssignAble = false;
            currentExprType = type;
            return;
        }

        if (currentExprType.isString){
            //todo 根据内置函数
            if (!currentExprType.isAssignAble){
                //throw new SemanticError("这样的行为未定义“pig”.la()", node.getPosition());
            }
            Type classType = globalScope.getClassType("string",false);
            if (!classType.funcMembers.containsKey(node.getMethodName()))throw new SemanticError("no this method in string",node.getPosition());
            Type funcType = classType.funcMembers.get(node.getMethodName());
            var realParamList = node.getParams();
            if (funcType.params!=null&&funcType.params.size()!=0){
                var formParamList = funcType.params;
                if (realParamList==null||realParamList.size()==0)throw new SemanticError("wrong param in methodCall 1", node.getPosition());
                if (formParamList.size()!=realParamList.size())throw new SemanticError("wrong param in methodCall 2",node.getPosition());
                int i = 0;
                for (var it : realParamList){
                    it.acceptVisitor(this);
                    Type formParamType = formParamList.get(i).b;
                    if ((!Objects.equals(currentExprType.typeName, formParamType.typeName))||currentExprType.numOfArray!=formParamType.numOfArray)throw new SemanticError("wrong params in methodCall 3",node.getPosition());
                    i++;
                }
            }else{
                if (realParamList!=null&&realParamList.size()!=0)throw new SemanticError("wrong param in methodCall 4",node.getPosition());
            }
            currentExprType = funcType;
            currentExprType.isAssignAble = false;
            return;
        }

        if(currentExprType.isId){
            String typeName = currentExprType.typeName;
            Type classType = globalScope.getClassType(typeName,false);
            if(!classType.funcMembers.containsKey(node.getMethodName()))throw new SemanticError("no this method in class",node.getPosition());
            Type funcType = classType.funcMembers.get(node.getMethodName());

            if (Objects.equals(typeName, node.getMethodName()))throw new SemanticError("can use constructor as method", node.getPosition());

            var formParamList = funcType.params;
            var realParamList = node.getParams();
            if (realParamList==null){
                if (formParamList==null||formParamList.size()==0){}
                else throw new SemanticError("wrong params in methodCall1",node.getPosition());
            }else {

                //System.out.println(node.getMethodName());

                if(formParamList==null)throw new SemanticError("wrong params in method Call2", node.getPosition());
            }
            if (formParamList.size()!=realParamList.size())throw new SemanticError("wrong params in methodCall3", node.getPosition());
            int i = 0;
            for (var it : realParamList){
                it.acceptVisitor(this);
                Type formParamType = formParamList.get(i).b;
                if (!Objects.equals(currentExprType.typeName, formParamType.typeName))throw new SemanticError("wrong params in methodCall4",node.getPosition());
                i++;
            }

            currentExprType = funcType.clone();
            currentExprType.isVar = true;
            // if(funcType.isId)currentExprType.isAssignAble = true;//todo 什么时候可赋值？
            //else currentExprType.isAssignAble = false;
            currentExprType.isAssignAble = false;

        }else throw new SemanticError("only classVar Array String has method", node.getPosition());
    }

    @Override
    public void visit(NameTypeNode node) {//这个应该访问不到，因为我之前封装大失败了，之后再慢慢修改

    }

    @Override//a = new int[ex]+[]*   []?
    public void visit(NewExprNode node) {
        NameTypeNode nameTypeNode = node.getNameTypeNode();

        visitTypeName(nameTypeNode.getTypename(),node.getPosition(),node.getNum());
        Type type = currentVarType;

        if (!type.isArray)
            if (type.isInt||type.isBool||type.isString)
                throw new SemanticError("can use a = new int", node.getPosition());

        //type.isNewExpr = true;

        if (type.isArray){
            var exprNodes = node.getExprInBracket();
            for(var it : exprNodes){
                it.acceptVisitor(this);
                if (currentExprType.isArray||(!currentExprType.isInt))throw new SemanticError("type in new[] must be int",it.getPosition());
            }
        }

        type.isAssignAble = false;
        currentExprType = type;

    }

    @Override
    public void visit(NullContainNode node) {
        Type type = new Type();
        type.isNull = true;
        type.typeName = "null";
        type.isAssignAble = false;
        node.setType(type);
        currentExprType = type;
    }

    @Override
    public void visit(PrefixExprNode node) {//+ - ++ -- ~ !
        node.getExprNode().acceptVisitor(this);
        if (node.getOp()!=PrefixExprNode.Op.LogicalNot) {
            if (currentExprType.isArray || (!currentExprType.isInt))
                throw new SemanticError("prefixExpr only can be used in int", node.getPosition());
            if (node.getOp()==PrefixExprNode.Op.PrePlusPlus||node.getOp()==PrefixExprNode.Op.PreMinusMinus) {
                if (!currentExprType.isAssignAble)
                    throw new SemanticError("prefixExpr only can be used in lValue", node.getPosition());
            }else {
                currentExprType.isAssignAble = false;
            }
        }else {
            if (currentExprType.isArray||(!currentExprType.isBool)){
                throw new SemanticError("wrong prefixExpr", node.getPosition());
            }
            currentExprType.isAssignAble = false;
        }
        //currentType不变
    }

    //class func var
    @Override
    public void visit(ProgramNode node) {//先检查是否引用了不存在的东西，再看存在的东西的地址是不是比自己还要靠前//这个思路，不知道对不对
        ArrayList<ProgramSectionNode>ProgramSections = node.getProgramSectionNodeList();
        for(var it : ProgramSections){
            currentScope = globalScope;
            if(it instanceof ClassDefNode){
                ClassDefNode classDefNode = (ClassDefNode) it;
                classDefNode.acceptVisitor(this);
            }

            if(it instanceof VarDefNode){

                VarDefNode varDefNode = (VarDefNode) it;
                visitTypeName(varDefNode.getTypeNode().getTypename(),varDefNode.getPosition(),varDefNode.getTypeNode().getNum());
                //currentVarType = type;//用于比较init
//                globalScope.putVar(varDefNode.getIdentifier(),currentVarType,varDefNode.getPosition());

                varDefNode.acceptVisitor(this);
                globalScope.putVar(varDefNode.getIdentifier(),currentVarType,varDefNode.getPosition());
            }

            if(it instanceof FuncDefNode){//main
                currentFuncType = globalScope.getFuncType(((FuncDefNode)it).getIdentifier(),false);
                ((FuncDefNode)it).acceptVisitor(this);
            }

        }
    }

    @Override
    public void visit(ProgramSectionNode node) {
        //应该大概或许maybe访问不到//确实

    }

    @Override
    public void visit(PureExprStmtNode node) {//没有引入新的作用域
        node.getExprNode().acceptVisitor(this);
    }

    @Override
    public void visit(ReturnStmtNode node) {//没有引入新的作用域

        if (currentScope.inLambda){
            if (!node.hasExprNode()){
                currentScope.currentLambdaType.isVoid = true;
                currentScope.currentLambdaType.typeName = "void";
                currentScope.currentLambdaType.isVar = true;
            }else {
                node.getExprNode().acceptVisitor(this);
                currentScope.currentLambdaType = currentExprType;
                //System.out.println(currentScope.currentLambdaType.typeName);
            }
            return;
        }


        if(!currentScope.inFunc)throw new SemanticError("returnStmt must in func",node.getPosition());
        if(!node.hasExprNode()){
            if(!Objects.equals(currentFuncType.typeName, "void")){//注释掉的部分已经处理过了
             throw new SemanticError("Wrong return type in func",node.getPosition());
            }
        }else {
            if(Objects.equals(currentFuncType.typeName, "void"))throw new SemanticError("wrong return type in func",node.getPosition());
            ExprNode exprNode = node.getExprNode();
            exprNode.acceptVisitor(this);
            //if(currentExprType.isNull)throw new SemanticError("can't return null",node.getPosition());//todo 我不确定
            if (!Objects.equals(currentExprType.typeName, currentFuncType.typeName)) {
                if (currentFuncType.isArray || currentFuncType.isId || currentFuncType.isString) {
                    if (!currentExprType.isNull)
                    throw new SemanticError("wrong return type in func", node.getPosition());
                }else {throw new SemanticError("wrong return type in func", node.getPosition());}
            }
            if (currentExprType.numOfArray!=currentFuncType.numOfArray)throw new SemanticError("wrong return dimension of ArrayType",node.getPosition());
        }
    }

    @Override
    public void visit(StmtNode node) {

    }

    @Override
    public void visit(StringContainNode node) {
        Type type = new Type();
        type.isString = true;
        type.typeName = "string";
        type.isAssignAble = false;
        currentExprType = type;
        node.setType(type);
    }

    //a[][].
    @Override
    public void visit(SubScriptExprNode node) {
        if (node.getIndexNode() instanceof SubScriptExprNode) {
            int oldNum = subScriptNum;
            subScriptNum = 0;
            node.getIndexNode().acceptVisitor(this);
            subScriptNum = oldNum;
        }else {
            node.getIndexNode().acceptVisitor(this);
        }


        if ((!currentExprType.isInt)||currentExprType.isArray)throw new SemanticError("wrong indexExpr in subScriptExpr",node.getPosition());
        subScriptNum++;
        if (node.getExprNode() instanceof SubScriptExprNode){
            node.getExprNode().acceptVisitor(this);
        }
        else {
            node.getExprNode().acceptVisitor(this);
            if (!currentExprType.isArray)throw new SemanticError("only array has subScriptExpr", node.getPosition());
            if (currentExprType.numOfArray<subScriptNum){
                //System.out.println(currentExprType.typeName);
                throw new SemanticError("the num of array can't be less of subScriptNum", node.getPosition());
            }
            currentExprType = currentExprType.clone();
            currentExprType.numOfArray = currentExprType.numOfArray-subScriptNum;
            currentExprType.isArray = currentExprType.numOfArray!=0;
            currentExprType.isAssignAble = true;
            subScriptNum = 0;
        }

    }

    //a++ --
    @Override
    public void visit(SuffixExprNode node) {
        node.getExprNode().acceptVisitor(this);
        if (!currentExprType.isAssignAble)throw new SemanticError("only lValue can have suffixExpr", node.getPosition());
        if (currentExprType.isArray||(!currentExprType.isInt))throw new SemanticError("wrong lValue in suffixExpr", node.getPosition());
        currentExprType.isAssignAble = false;
    }

    @Override
    public void visit(ThisExprNode node) {
        if(!currentScope.inClass)throw new SemanticError("thisExpr can only be in class",node.getPosition());
        Type type = new Type();
        type.isVar = true;
        type.isId = true;
        type.isAssignAble = false;
        type.typeName = currentClassType.typeName;
        currentExprType = type;
        node.setType(type);
    }

    @Override//这个应该访问不到
    public void visit(TypeNode node) {
        System.out.println("不应该访问到这里啊！！！，我在SemanticChecker381");
    }

    @Override
    public void visit(VarDefStmtNode node) {
        for(var it : node.getVarDefNodes()){

            VarDefNode varDefNode = (VarDefNode) it;
            Type type = new Type();
            type.isVar = true;
            type.typeName = varDefNode.getTypeNode().getTypename();
            boolean isId = true;
            if(Objects.equals(type.typeName, "int")){type.isInt=true;isId = false;}
            if(Objects.equals(type.typeName, "bool")){type.isBool=true;isId = false;}
            if(Objects.equals(type.typeName, "string")){type.isString=true;isId = false;}
            if(Objects.equals(type.typeName, "void")){throw new SemanticError("VarDef type can't be void",varDefNode.getPosition());}
            if(isId){
                type.isId = true;
                if(!currentScope.containClass(type.getTypeName(),true))
                    throw new SemanticError("use an undeclared class to define var",it.getPosition());
            }
            if(varDefNode.getTypeNode() instanceof ArrayTypeNode){
                type.isArray=true;
                type.numOfArray = ((ArrayTypeNode)(varDefNode.getTypeNode())).getNum();
            }
            type.position = varDefNode.getPosition();
            currentVarType = type;//用于比较init
//            currentScope.putVar(varDefNode.getIdentifier(),type, varDefNode.getPosition());

            varDefNode.acceptVisitor(this);
            currentScope.putVar(varDefNode.getIdentifier(),type, varDefNode.getPosition());
        }
    }


    //class不能跳到这个函数，因为class的members和methods已经预处理过了.同理,program也是
    //或者换一种方式，将var装进scope是外面的事，里面不负责这个
    //进入这个函数的，从varStmt func的参数中  class的member中  顶层的声明中
    //其中呢，由于class 和 参数中的没有init,所以可以直接在外面处理，就不进来了
    //关于typeNode就在外面直接进行了
    //并且currentType已经处理好了
    //int[][] a; a = new int[5][];//type.num = newExpr.num = 括号.num
    //悲伤 不能这样  因为int x = pig(x);
    @Override//int a = 5;
    public void visit(VarDefNode node) {
        VarDefNode varDefNode = (VarDefNode) node;
        TypeNode typeNode = varDefNode.getTypeNode();
        //这个不知道是不是多余的 应该是多余的
        if(typeNode==null)throw new SemanticError("我这里处理出问题了540semanticChecker",varDefNode.getPosition());
        Type type = currentVarType;

        if(varDefNode.hasInit()){
            varDefNode.getInit().acceptVisitor(this);

            if ((!type.isArray)&&(!type.isId)) {//还有assign
                if (!Objects.equals(type.typeName, currentExprType.typeName))
                    throw new SemanticError("wrong type in varInit1", node.getPosition());
            }
            else {
                if (!Objects.equals(type.typeName, currentExprType.typeName)) {
                    if (!currentExprType.isNull)
                        throw new SemanticError("wrong type in varInit2", node.getPosition());
                }else {
                   // System.out.println(currentExprType.numOfArray);
                    if (type.numOfArray!= currentExprType.numOfArray)throw new SemanticError("wrong type in varInit3", node.getPosition());
                }
            }

//            if (!Objects.equals(type.getTypeName(), currentExprType.getTypeName()))throw new SemanticError("wrong init in varDef",node.getPosition());
//            if (type.isArray){
//                if ((!currentExprType.isArray)||currentExprType.numOfArray!=type.numOfArray)throw new SemanticError("wrong init in varDef",node.getPosition());
//            }

        }

    }

    @Override
    public void visit(VarDefListNode node) {
        for(var it : node.getVarDefNodes()){
            it.acceptVisitor(this);
        }
    }

    @Override
    public void visit(VoidTypeNode node) {

    }

    //while(){}
    @Override
    public void visit(WhileStmtNode node) {
        boolean oldInLoop = currentScope.inLoop;
        currentScope.inLoop = true;
        if (node.getConditionExpr()==null)throw new SemanticError("Expr in whileExpr can't be null", node.getPosition());
        node.getConditionExpr().acceptVisitor(this);
        if (currentExprType.isArray||(!currentExprType.isBool))throw new SemanticError("wrong conditionType in whileExpr", node.getPosition());
        node.getStmt().acceptVisitor(this);
        currentScope.inLoop = oldInLoop;
    }

    //fParam suit rParam
    @Override
    public void visit(LambdaExprNode node) {
        Scope lambdaScope = new Scope(currentScope);
        lambdaScope.inLambda = true;
        currentScope = lambdaScope;
//        currentScope.currentLambdaType = new Type();
//        currentScope.currentLambdaType.isVar = true;
//        currentScope.currentLambdaType.isVoid = true;
//        currentScope.currentLambdaType.typeName = "void";

        var formParamList = node.getFormalParas();
        var realParamList = node.getActualParas();


        if (formParamList!=null&&formParamList.size()!=0){
            if (realParamList==null||realParamList.size()==0)throw new SemanticError("wrong param in methodCall 1", node.getPosition());
            if (formParamList.size()!=realParamList.size())throw new SemanticError("wrong param in methodCall 2",node.getPosition());
            int i = 0;
            for (var it : realParamList){
                VarDefNode formNode = formParamList.get(i);
                formNode.getTypeNode().acceptVisitor(this);
                Type formType = currentVarType;

                //System.out.println(formNode.getIdentifier());

                currentScope.putVar(formNode.getIdentifier(),formType,formNode.getPosition());
                it.acceptVisitor(this);

                if ((!Objects.equals(currentExprType.typeName, formType.typeName))||(formType.numOfArray!=currentExprType.numOfArray))throw new SemanticError("wrong params in methodCall 3erzi",node.getPosition());
                i++;
            }
        }else{
            if (realParamList!=null&&realParamList.size()!=0)throw new SemanticError("wrong param in methodCall 4",node.getPosition());
        }

        if (node.getSuit()!=null)
        node.getSuit().acceptVisitor(this);

        //System.out.println(curr);

        currentExprType = currentScope.currentLambdaType.clone();
        currentScope = currentScope.getFatherScope();


        //currentExprType = currentLambdaType;

    }

    public void visitTypeName(String typeName, Position typePosition,int typeNUM){
        Type type = new Type();
        type.isVar = true;
        type.typeName = typeName;//Array return the nameType
        boolean isId = true;//封装大失败
        if(Objects.equals(type.typeName, "int")){type.isInt=true;isId = false;}
        if(Objects.equals(type.typeName, "bool")){type.isBool=true;isId = false;}
        if(Objects.equals(type.typeName, "string")){type.isString=true;isId = false;}
        if(Objects.equals(type.typeName, "void")){throw new SemanticError("VarDef type can't be void",typePosition);}
        if(isId){//要看这个id是否存在
            type.isId = true;
            if(!currentScope.containClass(type.typeName,true)){
                throw new SemanticError("use an undeclared class to define var",typePosition);
            }
        }
        if(typeNUM!=0){
            type.isArray=true;
            type.numOfArray = typeNUM;
        }
        //varDefNode.acceptVisitor(this);
        type.position = typePosition;
        currentVarType = type;
    }
}
