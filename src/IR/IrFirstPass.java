package IR;

import AST.*;
import IR.llvmType.*;
import IR.operand.GlobalOperand;
import IR.operand.Operand;
import IR.operand.Register;
import IR.operand.StringConst;

import java.util.ArrayList;
import java.util.Objects;

public class IrFirstPass implements AstVisitor {
    public ArrayList<LlvmStructType> structs;
    public ArrayList<IrFunc>funcs;
    public ArrayList<GlobalOperand>globalOperands;
    public ArrayList<StringConst>stringConsts;
    public IrScope nowScope;

    public IrFirstPass(){
        structs = new ArrayList<>();
        funcs = new ArrayList<>();
        globalOperands = new ArrayList<>();
        stringConsts = new ArrayList<>();
        nowScope = null;
    }

    public void run(){

    }

   // public void visit

    public void buildInStruct(){//array我不会处理
        LlvmStructType structString = new LlvmStructType("string");
        structString.addMember(new LlvmPointerType(new LlvmIntegerType(8,false)));
        structString.addMember(new LlvmIntegerType(32,false));

//        LlvmStructType structArray = new LlvmStructType("array");
//        structArray.addMember(new LlvmPointerType(structArray));
//        structArray.addMember(new LlvmIntegerType(32,false));
    }

    public void buildInFunc(){
         //array
        //IrFunc array_Size = new IrFunc();
        //string

        //others

        IrFunc _printf = new IrFunc();
    }

    public LlvmStructType getStruct(String name){
        for(var it : structs){
            if (Objects.equals(it.structName, name)){
                return it;
            }
        }
        return null;
    }

    public Operand getGlobalVar(String name){
        for(var it : globalOperands){
            if (Objects.equals(it.name, name))return it;
        }
        return null;
    }

    public Operand getVar(String name){
        if (nowScope==null){
            System.out.println("nima 53 IrFirstPass");
        }
        Operand operand = nowScope.getVar(name);
        if (operand!=null)return operand;
        return getGlobalVar(name);
    }

    public IrFunc getFunc(String name){
        for(var it : funcs){
            if (Objects.equals(it.name, name)){
                return it;
            }
        }
        return null;
    }


    public void visitClassFirst(ClassDefNode classDefNode){
        LlvmStructType nowStruct = getStruct(classDefNode.getIdentifier());
        for(var it : classDefNode.getMembers()){
            LlvmSingleValueType member = null;
            if (it.getTypeNode().getNum()==0){
                if (Objects.equals(it.getTypeNode().type_name, "int")){
                    member = new LlvmIntegerType(32,false);
                }else if (Objects.equals(it.getTypeNode().type_name, "bool")){
                    member = new LlvmIntegerType(8,true);
                }else {
                    member = new LlvmPointerType(getStruct(it.getTypeNode().getTypename()));
                }
            }else{

            }
            nowStruct.addMember(member);
        }
    }

    public void visitFuncFirst(FuncDefNode funcDefNode){

    }

    public void visitVarFirst(VarDefNode VarDefNode){

    }

    public void visitProgramFirst(ProgramNode programNode){
        //first collect structsName
        for(var it : programNode.getProgramSectionNodeList()){
            if (it instanceof ClassDefNode){
                LlvmStructType struct = new LlvmStructType(((ClassDefNode)it).getIdentifier());
                structs.add(struct);
            }
        }
        //second
        //and getGlobalVar and get funcName and getClassMemBer
        for(var it : programNode.getProgramSectionNodeList()){
            if (it instanceof ClassDefNode){
                visitClassFirst((ClassDefNode) it);
            }else if(it instanceof FuncDefNode){
                visitFuncFirst((FuncDefNode) it);
            }else {//varDefList 已经被拆开过了
                visitVarFirst((VarDefNode) it);
            }
        }
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

    @Override
    public void visit(VarDefNode node) {

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
