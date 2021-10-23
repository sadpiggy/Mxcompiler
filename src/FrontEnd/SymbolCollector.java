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

public class SymbolCollector implements AstVisitor {
    public Scope globalScope;
    public Type nowClassType;
    public Type currentVarType;
    //public Type nowVarType;

    public SymbolCollector(GlobalScope globalScope){
        this.globalScope = globalScope;
        addBuildInFunc();
    }

    public void addBuildInFunc(){
        //把那些内置函数加上
        //可以把string真正的当成一个类型，这样就可以将其放在class里面，然后在class里面放置func
        //对了，保留词lexer和parser会帮我处理吗？
    }

    public void addStringMethod(){//这个不如放在SemanticChecker里面
        Type stringType = new Type();
        stringType.defineClass();
        Type methodType = new Type();

    }


    public void visitTypeName(String typeName, Position typePosition, int typeNUM){
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
//            if(!currentScope.containClass(type.typeName,true)){
//                throw new SemanticError("use an undeclared class to define var",typePosition);
//            }
        }
        if(typeNUM!=0){
            type.isArray=true;
            type.numOfArray = typeNUM;
        }
        //varDefNode.acceptVisitor(this);
        type.position = typePosition;
        currentVarType = type;
    }

    @Override
    public void visit(ArrayTypeNode node) {

    }

    @Override
    public void visit(AssignExprNode node) {

    }

    @Override
    public void visit(BinaryExprNode node) {

    }

    @Override
    public void visit(BlockStmtNode node) {

    }

    @Override
    public void visit(BoolContainNode node) {

    }

    @Override
    public void visit(BreakStmtNode node) {

    }

    @Override
    public void visit(ClassDefNode node) {

        for(var it : node.getMethods()){

            FuncDefNode funcDefNode = (FuncDefNode) it;
            if(Objects.equals(funcDefNode.getIdentifier(), "main")){
                throw new SemanticError("MainFn in class",funcDefNode.getPosition());
            }

            Type type = new Type();
            type.isFunc = true;
            if(funcDefNode.hasTypeNode()) type.typeName = funcDefNode.getTypeNode().getTypename();
            else throw new SemanticError("symbol78,kuai lai zhao wo",node.getPosition());//不应该会func没有type
            //else type.typeName="void";//todo constructor
            type.position = funcDefNode.getPosition();

            boolean isId = false;
            if(Objects.equals(type.typeName, "int")){type.isInt=true;isId = true;}
            if(Objects.equals(type.typeName, "bool")){type.isBool=true;isId = true;}
            if(Objects.equals(type.typeName, "string")){type.isString=true;isId = true;}
            if(Objects.equals(type.typeName, "void")){type.isVoid=true;isId = true;}
            if(!isId){type.isId = true;}
            if(funcDefNode.getTypeNode() instanceof ArrayTypeNode){
                type.isArray=true;
                type.numOfArray = ((ArrayTypeNode)(funcDefNode.getTypeNode())).getNum();
            }


            type.defineFunc();

            if (it.getParamList()!=null){
                for(var it2: it.getParamList()){
                    if(it2.getInit()!=null){
                        throw new SemanticError("param in funcDef can't have init",node.getPosition());
                    }

                    visitTypeName(it2.getTypeNode().getTypename(),it2.getPosition(),it2.getTypeNode().getNum());
                    type.params.add(new Pair<>(it.getIdentifier(),currentVarType));
                    //currentScope.putVar(it.getIdentifier(),currentVarType, it.getPosition());//好像没什么用处，因为后面会跳出这个作用域//有用，因为会进入函数体里面
                }
            }

            nowClassType.addFuncMembers(funcDefNode.getIdentifier(),type);
        }

        for(var it : node.getMembers()){
            VarDefNode varDefNode = (VarDefNode) it;
            Type type = new Type();
            type.isVar = true;
            type.typeName = varDefNode.getTypeNode().getTypename();
            boolean isId = false;
            if(Objects.equals(type.typeName, "int")){type.isInt=true;isId = true;}
            if(Objects.equals(type.typeName, "bool")){type.isBool=true;isId = true;}
            if(Objects.equals(type.typeName, "string")){type.isString=true;isId = true;}
            if(Objects.equals(type.typeName, "void")){throw new SemanticError("VarDef type can't be void",varDefNode.getPosition());}
            if(!isId){type.isId = true;}
            if(varDefNode.getTypeNode() instanceof ArrayTypeNode){
                type.isArray=true;
                type.numOfArray = ((ArrayTypeNode)(varDefNode.getTypeNode())).getNum();
            }
            type.position = varDefNode.getPosition();
           // if(nowClassType.varMembers.containsKey(varDefNode.getIdentifier()))throw new SemanticError("repeated varName in class",varDefNode.getPosition());
            //nowClassType.varMembers.put(varDefNode.getIdentifier(),type);
            nowClassType.addVarMembers(varDefNode.getIdentifier(),type);
        }

    }

    @Override
    public void visit(ContainExprNode node) {

    }

    @Override
    public void visit(ContinueStmtNode node) {

    }

    @Override
    public void visit(EmptyStmtNode node) {

    }

    @Override
    public void visit(ExprNode node) {

    }

    @Override
    public void visit(ForStmtNode node) {

    }

    @Override
    public void visit(FuncCallExprNode node) {

    }

    @Override
    public void visit(FuncDefNode node) {

    }

    @Override
    public void visit(IdExprNode node) {

    }

    @Override
    public void visit(IfStmtNode node) {

    }

    @Override
    public void visit(IntContainNode node) {

    }

    @Override
    public void visit(MemberExprNode node) {

    }

    @Override
    public void visit(MethodExprNode node) {

    }

    @Override
    public void visit(NameTypeNode node) {

    }

    @Override
    public void visit(NewExprNode node) {

    }

    @Override
    public void visit(NullContainNode node) {

    }

    @Override
    public void visit(PrefixExprNode node) {

    }

    @Override
    public void visit(ProgramNode node) {
        ArrayList<ProgramSectionNode> ProgramSections = node.getProgramSectionNodeList();
        int MainFuncNum = 0;
        for(var it : ProgramSections){

            //System.out.println("hao dare you are");

            if(it instanceof ClassDefNode){
                ClassDefNode classDefNode = (ClassDefNode) it;
                Type type = new Type();
                type.typeName = classDefNode.getIdentifier();
                type.isClass = true;
                type.defineClass();
                nowClassType = type;//然后我修改nowClassType,是不是就会修改到type
                classDefNode.acceptVisitor(this);
                type.position = classDefNode.getPosition();
                globalScope.putClass(((ClassDefNode) it).getIdentifier(),type,it.getPosition());
            }

            if(it instanceof FuncDefNode){//main

                //System.out.println(((FuncDefNode) it).getIdentifier());
                //if (!((FuncDefNode) it).hasTypeNode())System.out.println("???");

                FuncDefNode funcDefNode = (FuncDefNode) it;
                if(Objects.equals(funcDefNode.getIdentifier(), "main")){

                    //System.out.println("伞兵");

                    if(!Objects.equals(funcDefNode.getTypeNode().type_name, "int"))throw new SemanticError("WrongType MainFunction",funcDefNode.getPosition());
                    if(funcDefNode.getParamList().size()!=0)throw new SemanticError("WrongParamMainFunction",funcDefNode.getPosition());
                    MainFuncNum+=1;
                    if(MainFuncNum > 1)throw new SemanticError("Repeated MainFunction",funcDefNode.getPosition());
                }
                Type type = new Type();
                type.isFunc = true;
                if(funcDefNode.getTypeNode()==null){throw new SemanticError("no type func only can be constructor",funcDefNode.getPosition());}
                type.typeName = funcDefNode.getTypeNode().getTypename();
                type.position = funcDefNode.getPosition();

                boolean isId = false;
                if(Objects.equals(type.typeName, "int")){type.isInt=true;isId = true;}
                if(Objects.equals(type.typeName, "bool")){type.isBool=true;isId = true;}
                if(Objects.equals(type.typeName, "string")){type.isString=true;isId = true;}
                if(Objects.equals(type.typeName, "void")){type.isVoid=true;isId=true;}
                if(!isId){type.isId = true;}
                if(funcDefNode.getTypeNode() instanceof ArrayTypeNode){
                    type.isArray=true;
                    type.numOfArray = ((ArrayTypeNode)(funcDefNode.getTypeNode())).getNum();
                }
                type.position = funcDefNode.getPosition();

                type.defineFunc();

                if (((FuncDefNode) it).getParamList()!=null){
                    for(var it2: ((FuncDefNode) it).getParamList()){
                        if(it2.getInit()!=null){
                            throw new SemanticError("param in funcDef can't have init",node.getPosition());
                        }

                        visitTypeName(it2.getTypeNode().getTypename(),it2.getPosition(),it2.getTypeNode().getNum());
                        type.params.add(new Pair<>(((FuncDefNode) it).getIdentifier(),currentVarType));
                        //currentScope.putVar(it.getIdentifier(),currentVarType, it.getPosition());//好像没什么用处，因为后面会跳出这个作用域//有用，因为会进入函数体里面
                    }
                }


                globalScope.putFunc(funcDefNode.getIdentifier(),type,funcDefNode.getPosition());
            }

            if(it instanceof VarDefNode){
                //什么都不做
            }

            //currentScope = globalScope;

        }

        if(MainFuncNum != 1){throw new SemanticError("No MainFunction",node.getPosition());}



    }

    @Override
    public void visit(ProgramSectionNode node) {

    }

    @Override
    public void visit(PureExprStmtNode node) {

    }

    @Override
    public void visit(ReturnStmtNode node) {

    }

    @Override
    public void visit(StmtNode node) {

    }

    @Override
    public void visit(StringContainNode node) {

    }

    @Override
    public void visit(SubScriptExprNode node) {

    }

    @Override
    public void visit(SuffixExprNode node) {

    }

    @Override
    public void visit(ThisExprNode node) {

    }

    @Override
    public void visit(TypeNode node) {

    }

    @Override
    public void visit(VarDefStmtNode node) {

    }


    @Override //int a = 5;
    public void visit(VarDefNode node) {//具体的语义判断，还是交给checker

    }

    @Override
    public void visit(VarDefListNode node) {

    }

    @Override
    public void visit(VoidTypeNode node) {

    }

    @Override
    public void visit(WhileStmtNode node) {

    }

    @Override
    public void visit(LambdaExprNode node) {

    }
}
