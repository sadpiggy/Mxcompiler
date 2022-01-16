package IR;

import AST.*;
import IR.inst.*;
import IR.llvmType.*;
import IR.operand.*;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class IrFirstPass implements AstVisitor {//似乎可以2pass处理
    public ArrayList<LlvmStructType> structs;
    public ArrayList<IrFunc>funcs;
    public ArrayList<GlobalOperand>globalOperands;
    public ArrayList<StringConst>stringConsts;// str.hello //hello\0
    //public IrScope nowScope;
    public Const nowConst;
    public LlvmStructType nowStruct;
    public boolean isInClass;
    public IrFunc nowFunc;
    public ScopeForBuild nowScope;
    //public int nowRegisterNum;//for func?need? nowFunc.getReg
    public IrBlock nowBlock;
    // public Register nowRegister;
    public Operand nowOperand;
    public int stringConstNum;
    public boolean isLeft;//这个之后要想办法取代
    public boolean isFirstFunc;
    //为了控制流转换
    public IrBlock loopNextBlock;
    public IrBlock loopCondBlock;
    public ArrayList<AssignExprNode>globalInitNodes;
    // public IrBlock endBlock;
    //public String nowStructName;

    public IrFirstPass(){
        structs = new ArrayList<>();
        funcs = new ArrayList<>();
        globalOperands = new ArrayList<>();
        stringConsts = new ArrayList<>();
        // nowScope = null;
        nowConst = null;
        nowScope = null;
        nowStruct = null;
        nowFunc = null;
        isInClass = false;
        //nowRegister = null;
        nowOperand = null;
        stringConstNum = 0;
        isLeft = false;
        isFirstFunc = false;
        loopCondBlock = null;
        loopNextBlock = null;
        globalInitNodes = new ArrayList<>();
        // nowStructName = null;
    }

    public void run(ProgramNode node){
        buildInFunc();
        visitProgramFirst(node);
        node.acceptVisitor(this);
    }

    public void printIr() throws FileNotFoundException {
        System.setOut(new PrintStream(new BufferedOutputStream(
                new FileOutputStream("ravel/build/test.ll")),true));

        System.out.println("target datalayout = \"e-m:e-p270:32:32-p271:32:32-p272:64:64-i64:64-f80:128-n8:16:32:64-S128\"");
        System.out.println("target triple = \"x86_64-pc-linux-gnu\"");

        System.out.println("@.str = private unnamed_addr constant [3 x i8] c\"%s\\00\", align 1\n" +
                "@.str.1 = private unnamed_addr constant [4 x i8] c\"%s\\0A\\00\", align 1\n" +
                "@.str.2 = private unnamed_addr constant [3 x i8] c\"%d\\00\", align 1\n" +
                "@.str.3 = private unnamed_addr constant [4 x i8] c\"%d\\0A\\00\", align 1");

        for(var it: stringConsts){
            System.out.println(it.toPrintString());
        }

        for (var it : globalOperands){
            System.out.println(it.toPrintString());
        }

        for (var it : structs){
            System.out.println(it.toPrintString());
        }

        for (var it : funcs){
            it.PrintIr();
        }

    }

    public void buildInFunc(){
        //declare
        //printf
        IrFunc _printf = new IrFunc(new LlvmIntegerType(32,false),"printf",null,true);
        _printf.isDeclare = true;
        funcs.add(_printf);
        //scanf
        IrFunc _scanf = new IrFunc(new LlvmIntegerType(32,false),"scanf",null,true);
        _scanf.isDeclare = true;
        funcs.add(_scanf);
        //malloc
        IrFunc _malloc = new IrFunc(new LlvmPointerType(new LlvmIntegerType(8,false)),"malloc",null,true);
        _malloc.isDeclare = true;
        funcs.add(_malloc);

        IrFunc _sprintf = new IrFunc(new LlvmIntegerType(32,false),"sprintf",null,true);
        _sprintf.isDeclare = true;
        funcs.add(_sprintf);

        IrFunc _strcpy = new IrFunc(new LlvmPointerType(new LlvmIntegerType(8,false)),"strcpy",null,true);
        _strcpy.isDeclare = true;
        funcs.add(_strcpy);

        IrFunc _strcat = new IrFunc(new LlvmPointerType(new LlvmIntegerType(8,false)),"strcat",null,true);
        _strcat.isDeclare = true;
        funcs.add(_strcat);

        IrFunc _strcmp = new IrFunc(new LlvmIntegerType(32,false),"strcmp",null,true);
        _strcmp.isDeclare = true;
        funcs.add(_strcmp);

        IrFunc _strlen = new IrFunc(new LlvmIntegerType(32,false),"strlen",null,true);
        _strlen.isDeclare = true;
        funcs.add(_strlen);

        IrFunc _memcpy = new IrFunc(new LlvmVoidType(),"memcpy",null,true);
        _memcpy.isDeclare = true;
        funcs.add(_memcpy);

        IrFunc _sscanf = new IrFunc(new LlvmIntegerType(32,false),"sscanf",null,true);
        _sscanf.isDeclare = true;
        funcs.add(_sscanf);


        //内建函数
        IrFunc print = new IrFunc(new LlvmVoidType(),"print",null,true);
        funcs.add(print);

        IrFunc println = new IrFunc(new LlvmVoidType(),"println",null,true);
        funcs.add(println);

        IrFunc printInt = new IrFunc(new LlvmVoidType(),"printInt",null,true);
        funcs.add(printInt);

        IrFunc printlnInt = new IrFunc(new LlvmVoidType(),"printlnInt",null,true);
        funcs.add(printlnInt);

        IrFunc getString = new IrFunc(new LlvmPointerType(new LlvmIntegerType(8,false)),"getString",null,true);
        funcs.add(getString);

        IrFunc getInt = new IrFunc(new LlvmIntegerType(32,false),"getInt",null,true);
        funcs.add(getInt);

        IrFunc toString = new IrFunc(new LlvmPointerType(new LlvmIntegerType(8,false)),"toString",null,true);
        funcs.add(toString);
        //method buildIn
        IrFunc string_length = new IrFunc(new LlvmIntegerType(32,false),"string_length",null,true);
        funcs.add(string_length);
        IrFunc string_substring = new IrFunc(new LlvmPointerType(new LlvmIntegerType(8,false)),"string_substring",null,true);
        funcs.add(string_substring);
        IrFunc string_parseInt = new IrFunc(new LlvmIntegerType(32,false),"string_parseInt",null,true);
        funcs.add(string_parseInt);
        IrFunc string_ord = new IrFunc(new LlvmIntegerType(32,false),"string_ord",null,true);
        funcs.add(string_ord);
        //+ < <= > < >= == !=
        IrFunc string_add = new IrFunc(new LlvmPointerType(new LlvmIntegerType(8,false)),"string_add",null,true);
        funcs.add(string_add);
        IrFunc string_equal = new IrFunc(new LlvmIntegerType(8,true),"string_equal",null,true);
        funcs.add(string_equal);
        IrFunc string_notequal = new IrFunc(new LlvmIntegerType(8,true),"string_notequal",null,true);
        funcs.add(string_notequal);
        IrFunc string_lesser = new IrFunc(new LlvmIntegerType(8,true),"string_lesser",null,true);
        funcs.add(string_lesser);
        IrFunc string_greater = new IrFunc(new LlvmIntegerType(8,true),"string_greater",null,true);
        funcs.add(string_greater);
        IrFunc string_lessEqual = new IrFunc(new LlvmIntegerType(8,true),"string_lessEqual",null,true);
        funcs.add(string_lessEqual);
        IrFunc string_greatEqual = new IrFunc(new LlvmIntegerType(8,true),"string_greatEqual",null,true);
        funcs.add(string_greatEqual);
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
        if (isInClass)return getVar_inClass(name);
        else return getVar_notInClass(name);
    }

    public Operand getVar_notInClass(String name){//只在inclass==false时可以用
        AstNode node = nowScope.getNode(name);
        if (node!=null)return node.operand;
        return getGlobalVar(name);
    }

    public void getThisMember(String name){//aa.a
        //System.out.println(name);
        ArrayList<Operand>indexs = new ArrayList<>();
        indexs.add(new IntegerConst(32,false,0));
        //if (nowStruct==null)System.out.println(name);
        indexs.add(new IntegerConst(32,false,nowStruct.getIndex(name)));
        Register thisReg = new Register(new LlvmPointerType(nowStruct),nowFunc.getMidRegName());
        nowBlock.push_back(new LoadInst(thisReg,nowBlock,nowOperand));
        Register register = new Register(new LlvmPointerType(nowStruct.getMemberType(name)),nowFunc.getMidRegName());
        LlvmSingleValueType memberType = nowStruct.getMemberType(name);
        if (memberType instanceof LlvmPointerType){
            if (((LlvmPointerType) memberType).pointeeType instanceof LlvmStructType){
                //nowStruct = (LlvmStructType) (((LlvmPointerType) memberType).pointeeType);
                register.typeName = ((LlvmStructType) (((LlvmPointerType) memberType).pointeeType)).structName;
                //System.out.println(nowStruct);
            }
        }
        nowBlock.push_back(new GetElementPtrInst(register,nowBlock,thisReg,indexs));
        nowOperand = register;
        if (!isLeft){
            Register loadReg = new Register((LlvmSingleValueType) ((LlvmPointerType)(nowOperand.type)).pointeeType,nowFunc.getMidRegName());
            nowBlock.push_back(new LoadInst(loadReg,nowBlock,nowOperand));
            nowOperand = loadReg;
        }
    }

    public Operand getVar_inClass(String name){
        AstNode node = nowScope.getNode(name);
        if (node!=null)return node.operand;
        if(nowStruct.containMember(name)){
            ArrayList<Operand>indexs = new ArrayList<>();
            indexs.add(new IntegerConst(32,false,0));
            indexs.add(new IntegerConst(32,false,nowStruct.getIndex(name)));
            Register thisReg = new Register(new LlvmPointerType(nowStruct),nowFunc.getMidRegName());
            nowBlock.push_back(new LoadInst(thisReg,nowBlock,new Register(new LlvmPointerType(new LlvmPointerType(nowStruct)),"paramin_this")));
            Register register = new Register(new LlvmPointerType(nowStruct.getMemberType(name)),nowFunc.getMidRegName());
            nowBlock.push_back(new GetElementPtrInst(register,nowBlock,thisReg,indexs));
            nowOperand = register;
            return register;
        }
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

    public StringConst getStringConst(String value){//str.lala
        if (stringConsts.size()==0)return null;
        //name = "str." +  name;
        for (var it : stringConsts){
            if (Objects.equals(it.value, value))return it;
        }
        return null;
    }

    public void setStringConst(String value){
        stringConsts.add(new StringConst("str."+stringConstNum++,value));
    }


    public LlvmSingleValueType getLlvmArrayType(TypeNode typeNode,int num){//int a; a = new int;//这里的int的type不一样，所以应该分开弄
        LlvmSingleValueType llvmType = null;
        if (Objects.equals(typeNode.type_name, "int")){
            llvmType = new LlvmPointerType(new LlvmIntegerType(32,false));
        }else if (Objects.equals(typeNode.type_name, "bool")){
            llvmType = new LlvmPointerType(new LlvmIntegerType(8,true));
        }else if (Objects.equals(typeNode.type_name, "string")){
            llvmType = new LlvmPointerType(new LlvmIntegerType(8,false));
        }else {
            llvmType = new LlvmPointerType(new LlvmPointerType(getStruct(typeNode.type_name)));
        }
        /// System.out.println(typeNode.getNum());
        for (int i=2;i<= num;i++){
            llvmType = new LlvmPointerType((LlvmFirstClassType) llvmType);
            llvmType.isArray = true;
        }
        return llvmType;
    }

    public LlvmType getLlvmType(TypeNode typeNode){
        LlvmType llvmType = null;
        boolean isStruct = false;
        if (Objects.equals(typeNode.type_name, "int")){
            llvmType = new LlvmIntegerType(32,false);
        }else if (Objects.equals(typeNode.type_name, "bool")){
            llvmType = new LlvmIntegerType(8,true);
        }else if (Objects.equals(typeNode.type_name, "string")){
            llvmType = new LlvmPointerType(new LlvmIntegerType(8,false));
        }else if (Objects.equals(typeNode.getTypename(), "void")){
            llvmType = new LlvmVoidType();
        }else {
            llvmType = new LlvmPointerType(getStruct(typeNode.type_name));
            isStruct = true;
        }
        //return getStruct(typeNode.type_name);
        // if (!isStruct){
        for (int i=1;i<= typeNode.getNum();i++){
            llvmType = new LlvmPointerType((LlvmFirstClassType) llvmType);
            llvmType.isArray = true;
        }
        return llvmType;
    }


    //class之后要大改，先放着
    public void visitClassFirst(ClassDefNode classDefNode){
        nowStruct = getStruct(classDefNode.getIdentifier());
        for(var it : classDefNode.getMembers()){
            LlvmSingleValueType member = null;
            member = (LlvmSingleValueType) getLlvmType(it.getTypeNode());
            nowStruct.addMember(it.getIdentifier(),member);
        }
        for(var it : classDefNode.getMethods()){
            visitFuncFirst((FuncDefNode) it,nowStruct.structName + "_" + it.getIdentifier(),true);
        }
    }

    public void visitFuncFirst(FuncDefNode funcDefNode,String funcName,boolean isMethod){
        //int nowRegisterNum = 0;
        //if (funcName==null)funcName = funcDefNode.getIdentifier();
        ArrayList<Register>formParams = null;
        if (isMethod){
            formParams = new ArrayList<>();
            //这个，名字得改之后//todo
            Register formParam = new Register(new LlvmPointerType(nowStruct),"param_"+"this");
            //nowRegisterNum++;
            formParams.add(formParam);
            if (funcDefNode.getParamList()!=null&&funcDefNode.getParamList().size()!=0){
                for (var it:funcDefNode.getParamList()){
                    formParam = new Register((LlvmSingleValueType) getLlvmType(it.getTypeNode()),"param_"+it.getIdentifier());
                    //nowRegisterNum++;
                    it.operand = formParam;
                    formParams.add(formParam);
                }
            }
        }else {
            if (funcDefNode.getParamList()!=null&&funcDefNode.getParamList().size()!=0){
                formParams = new ArrayList<>();
                for (var it:funcDefNode.getParamList()){
                    Register formParam = new Register((LlvmSingleValueType) getLlvmType(it.getTypeNode()),"param_"+it.getIdentifier());
                    //nowRegisterNum++;
                    it.operand = formParam;
                    formParams.add(formParam);
                }
            }
        }
        //if (formParams!=null)System.out.println("???");
        IrFunc irFunc = new IrFunc((LlvmSingleValueType) getLlvmType(funcDefNode.getTypeNode()),funcName,formParams,false);
        irFunc.typeName = funcDefNode.getTypeNode().type_name;
        funcs.add(irFunc);
    }

    public void visitVarFirst(VarDefNode varDefNode){//global //int a = 5;
        //先写一个不带.size的
        LlvmType llvmType = getLlvmType(varDefNode.getTypeNode());
        String value = null;
        if (llvmType instanceof LlvmIntegerType)value = "0";
        else value = "null";
        varDefNode.operand = new GlobalOperand((LlvmFirstClassType) llvmType,varDefNode.getIdentifier(),value);
        varDefNode.operand.typeName = varDefNode.getTypeNode().type_name;
        globalOperands.add((GlobalOperand) varDefNode.operand);
        if (varDefNode.hasInit()){
            IdExprNode idExprNode =  new IdExprNode(varDefNode.getPosition(),"cnm", varDefNode.getIdentifier());
            idExprNode.operand = varDefNode.operand;
            globalInitNodes.add(new AssignExprNode(varDefNode.getPosition(), "cnm",idExprNode, varDefNode.getInit()));
        }
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
                visitFuncFirst((FuncDefNode) it,((FuncDefNode) it).getIdentifier(),false);
            }else {//varDefList 已经被拆开过了
                visitVarFirst((VarDefNode) it);
            }
        }
        //third getAllName
        // programNode.acceptVisitor(this);
        //fourth buildIr
    }

    @Override//先放着
    public void visit(ArrayTypeNode node) {

    }

    @Override
    public void visit(AssignExprNode node) {//a = b
        isLeft = true;
        node.getlExpr().acceptVisitor(this);
        Operand lOp = nowOperand;
        isLeft = false;
        node.getrExpr().acceptVisitor(this);
        Operand rOp = nowOperand;
//        if (!Objects.equals(rOp.type.toString(), ((LlvmPointerType) (lOp.type)).pointeeType.toString())){
//            Register bitcastReg = new Register(new LlvmPointerType(rOp.type),nowFunc.getMidRegName());
//            nowBlock.push_back(new BitcastInst(bitcastReg,nowBlock,lOp));
//            lOp = bitcastReg;
//        }
        StoreInst inst = new StoreInst(nowBlock,rOp,lOp);
        nowBlock.push_back(inst);
    }

    @Override
    public void visit(PrefixExprNode node) {//bool运算应该只有logic的吧  ++a
        boolean isLeftOld = isLeft;
        isLeft = false;
        node.getExprNode().acceptVisitor(this);
        isLeft = isLeftOld;
        Operand lReg = nowOperand;
        Inst inst = null;
        Register register = new Register(lReg.type,nowFunc.getMidRegName());
        switch (node.getOp()){
            case SignNeg:{
                inst = new BinaryInst(register,nowBlock, BinaryInst.InstOp.sub,new IntegerConst(32,false,0),lReg);
                nowOperand = register;
                nowBlock.push_back(inst);
                break;
            }
            case SignPos:return;
            case PrePlusPlus:{
                Boolean oldIsLeft = isLeft;
                isLeft = true;
                node.getExprNode().acceptVisitor(this);
                isLeft = false;
                isLeft = oldIsLeft;
                inst = new BinaryInst(register,nowBlock, BinaryInst.InstOp.add,lReg,new IntegerConst(32,false,1));
                nowBlock.push_back(inst);
                Inst storeInst = new StoreInst(nowBlock,register,nowOperand);
                nowBlock.push_back(storeInst);
                if (!isLeft)nowOperand = register;
                break;
            }
            case PreMinusMinus:{
                Boolean oldIsLeft = isLeft;
                isLeft = true;
                node.getExprNode().acceptVisitor(this);
                isLeft = false;
                isLeft = oldIsLeft;
                inst = new BinaryInst(register,nowBlock, BinaryInst.InstOp.sub,lReg,new IntegerConst(32,false,1));
                nowBlock.push_back(inst);
                Inst storeInst = new StoreInst(nowBlock,register,nowOperand);
                nowBlock.push_back(storeInst);
                if (!isLeft)nowOperand = register;
                break;
            }
            case BitWiseNot:{
                inst = new BinaryInst(register,nowBlock, BinaryInst.InstOp.xor,new IntegerConst(32,false,-1),lReg);
                nowOperand = register;
                nowBlock.push_back(inst);
                break;
            }
            case LogicalNot:{
                inst = new BinaryInst(register,nowBlock, BinaryInst.InstOp.xor,new IntegerConst(8,true,1),lReg);
                nowOperand = register;
                nowBlock.push_back(inst);
                break;
            }
        }
    }


    @Override
    public void visit(BinaryExprNode node) {//string 先放在后面//int c = a-b; a在inst前面部分//bool后面再弄//node.operand = ...,之后应该可以用作优化的
        if (node.getOp()== BinaryExprNode.Op.LogicalAnd||node.getOp()== BinaryExprNode.Op.LogicalOr){
            Register boolReg = new Register(new LlvmPointerType(new LlvmIntegerType(8,true)),nowFunc.getMidRegName());
            nowBlock.push_back(new AllocInst(new LlvmIntegerType(8,true),boolReg,nowBlock));
            node.getlExpr().acceptVisitor(this);
            Operand lReg = nowOperand;
            nowBlock.push_back(new StoreInst(nowBlock,nowOperand,boolReg));
            IrBlock boolBlock = new IrBlock(nowFunc.getBlockLabel());
            IrBlock nextBlock = new IrBlock(nowFunc.getBlockLabel());
            if (node.getOp()== BinaryExprNode.Op.LogicalAnd){
                nowBlock.push_back(new BrInst(nowBlock,nowOperand, boolBlock.label, nextBlock.label));
            }else {
                nowBlock.push_back(new BrInst(nowBlock,nowOperand, nextBlock.label, boolBlock.label));
            }
            nowFunc.setBlock(boolBlock);
            nowBlock = boolBlock;
            node.getrExpr().acceptVisitor(this);
            nowBlock.push_back(new StoreInst(nowBlock,nowOperand,boolReg));
            nowBlock.push_back(new BrInst(nowBlock, nextBlock.label));
            nowBlock = nextBlock;
            nowFunc.setBlock(nextBlock);
            Register boolResult = new Register(new LlvmIntegerType(8,true),nowFunc.getMidRegName());
            nowBlock.push_back(new LoadInst(boolResult,nowBlock,boolReg));
            nowOperand = boolResult;
            return;
        }
        ArrayList<Operand>params = new ArrayList<>();
        node.getlExpr().acceptVisitor(this);
        Operand lReg = nowOperand;
        String typeName = nowOperand.typeName;
        params.add(lReg);
        node.getrExpr().acceptVisitor(this);
        Operand rReg = nowOperand;
        params.add(rReg);
        nowOperand = new Register(lReg.type,nowFunc.getMidRegName());
        LlvmIntegerType boolType = new LlvmIntegerType(8,true);
        //System.out.println(lReg.typeName);
        if (typeName!=null&&typeName.equals("string")){//string之类的
            nowOperand.typeName = "string";
            CallInst inst = null;
            IrFunc irFunc = null;
            switch (node.getOp()){
                case Add: irFunc = getFunc("string_add");irFunc.isUsed = true;
                    inst = new CallInst(nowOperand,nowBlock,irFunc,params);break;
                case Equal:irFunc = getFunc("string_equal");irFunc.isUsed = true;
                    inst = new CallInst(nowOperand,nowBlock,irFunc,params);break;
                case NotEqual:irFunc = getFunc("string_notequal");irFunc.isUsed = true;
                    inst = new CallInst(nowOperand,nowBlock,irFunc,params);break;
                case Less:irFunc = getFunc("string_lesser");irFunc.isUsed = true;
                    inst = new CallInst(nowOperand,nowBlock,irFunc,params);break;
                case Greater:irFunc = getFunc("string_greater");irFunc.isUsed = true;
                    inst = new CallInst(nowOperand,nowBlock,irFunc,params);break;
                case LessEqual:irFunc = getFunc("string_lessEqual");irFunc.isUsed = true;
                    inst = new CallInst(nowOperand,nowBlock,irFunc,params);break;
                case GreaterEqual:irFunc = getFunc("string_greatEqual");irFunc.isUsed = true;
                    inst = new CallInst(nowOperand,nowBlock,irFunc,params);break;
            }
            nowBlock.push_back(inst);
            return;
        }
        //nowFunc.nowMidRegNum++;
        Inst inst = null;//inst = new BinaryInst(nowOperand,nowBlock,null,lReg,rReg);
        switch (node.getOp()){//Less, Greater, LessEqual, GreaterEqual,Equal, NotEqual,
            case Add: inst = new BinaryInst(nowOperand,nowBlock, BinaryInst.InstOp.add,lReg,rReg);break;
            case Sub: inst = new BinaryInst(nowOperand,nowBlock, BinaryInst.InstOp.sub,lReg,rReg);break;
            case Mul: inst = new BinaryInst(nowOperand,nowBlock, BinaryInst.InstOp.mul,lReg,rReg);break;
            case Div: inst = new BinaryInst(nowOperand,nowBlock, BinaryInst.InstOp.sdiv,lReg,rReg);break;
            case Mod: inst = new BinaryInst(nowOperand,nowBlock, BinaryInst.InstOp.srem,lReg,rReg);break;
            case ShiftLeft : inst = new BinaryInst(nowOperand,nowBlock, BinaryInst.InstOp.shl,lReg,rReg);break;
            case ShiftRight : inst = new BinaryInst(nowOperand,nowBlock, BinaryInst.InstOp.ashr,lReg,rReg);break;
            case BitwiseAnd : inst = new BinaryInst(nowOperand,nowBlock, BinaryInst.InstOp.and,lReg,rReg);break;
            case BitwiseOr : inst = new BinaryInst(nowOperand,nowBlock, BinaryInst.InstOp.or,lReg,rReg);break;
            case BitwiseXor : inst = new BinaryInst(nowOperand,nowBlock, BinaryInst.InstOp.xor,lReg,rReg);break;
            case Less: nowOperand.type = boolType;inst = new IcmpInst(nowOperand,nowBlock, IcmpInst.CondOp.slt,lReg,rReg);break;
            case Greater: nowOperand.type = boolType;inst = new IcmpInst(nowOperand,nowBlock, IcmpInst.CondOp.sgt,lReg,rReg);break;
            case LessEqual: nowOperand.type = boolType;inst = new IcmpInst(nowOperand,nowBlock, IcmpInst.CondOp.sle,lReg,rReg);break;
            case GreaterEqual: nowOperand.type = boolType;inst = new IcmpInst(nowOperand,nowBlock, IcmpInst.CondOp.sge,lReg,rReg);break;
            case Equal: nowOperand.type = boolType;inst = new IcmpInst(nowOperand,nowBlock, IcmpInst.CondOp.eq,lReg,rReg);break;
            case NotEqual: nowOperand.type = boolType;inst = new IcmpInst(nowOperand,nowBlock, IcmpInst.CondOp.ne,lReg,rReg);break;
        }
        nowBlock.push_back(inst);
    }

    @Override
    public void visit(BlockStmtNode node) {
        nowScope = new ScopeForBuild(nowScope);
        for(var it : node.getStmtNodes()){
            it.acceptVisitor(this);
        }
        nowScope = nowScope.fatherScope;
    }

    @Override
    public void visit(BoolContainNode node) {
        int value = node.getValue() ?1:0;
        nowConst = new IntegerConst(8,true,value);
        node.operand = nowConst;
        nowOperand = nowConst;
    }

    @Override
    public void visit(BreakStmtNode node) {//这个之后再弄
        nowBlock.push_back(new BrInst(nowBlock, loopNextBlock.label));
    }

    @Override
    public void visit(ClassDefNode node) {

    }

    @Override
    public void visit(ContainExprNode node) {

    }

    @Override
    public void visit(ContinueStmtNode node) {
        nowBlock.push_back(new BrInst(nowBlock, loopCondBlock.label));
    }

    @Override
    public void visit(EmptyStmtNode node) {

    }

    @Override
    public void visit(ExprNode node) {

    }

    @Override
    public void visit(ForStmtNode node) {//将a换成this.a不是特别容易啊
        if (node.getInitExpr()!=null)node.getInitExpr().acceptVisitor(this);
        IrBlock condBlock = new IrBlock(nowFunc.getBlockLabel());
        IrBlock bodyBlock = new IrBlock(nowFunc.getBlockLabel());
        IrBlock changeBlock = new IrBlock(nowFunc.getBlockLabel());
        IrBlock nextBlock = new IrBlock(nowFunc.getBlockLabel());
        IrBlock oldNextBlock = loopNextBlock;
        IrBlock oldCondBlock = loopCondBlock;
        loopCondBlock = condBlock;
        loopNextBlock = nextBlock;
        BrInst initBrInst = new BrInst(nowBlock,condBlock.label);
        nowBlock.push_back(initBrInst);
        nowBlock = condBlock;
        nowFunc.setBlock(condBlock);
        if (node.getConditionExpr()!=null){
            node.getConditionExpr().acceptVisitor(this);
            BrInst condBrinst = new BrInst(nowBlock,nowOperand, bodyBlock.label,nextBlock.label);
            nowBlock.push_back(condBrinst);
        }else nowBlock.push_back(new BrInst(nowBlock, bodyBlock.label));
        nowBlock = bodyBlock;
        nowFunc.setBlock(bodyBlock);
        node.getStmt().acceptVisitor(this);
        BrInst bodyBrinst = new BrInst(nowBlock, changeBlock.label);
        nowBlock.push_back(bodyBrinst);
        nowFunc.setBlock(changeBlock);
        nowBlock = changeBlock;
        if (node.getChangeExpr()!=null)
            node.getChangeExpr().acceptVisitor(this);
        BrInst changeBrinst = new BrInst(nowBlock, condBlock.label);
        ///////
        // System.out.println(node.getChangeExpr());
        nowBlock.push_back(changeBrinst);
        nowFunc.setBlock(nextBlock);
        nowBlock = nextBlock;
        loopCondBlock = oldCondBlock;
        loopNextBlock = oldNextBlock;
    }

    @Override
    public void visit(FuncCallExprNode node) {
        IrFunc callFunc = getFunc(node.getIdentifier());
        callFunc.isUsed = true;
        ArrayList<Operand>realParams = null;
        if (node.getParams()!=null&&node.getParams().size()!=0){//忘记之前怎么处理的了，只能冗余一下了
            realParams = new ArrayList<>();
            for (var it : node.getParams()){
                it.acceptVisitor(this);
                realParams.add(nowOperand);
            }
        }
        CallInst inst = null;
        // System.out.println(callFunc.type.toString());
        if (!Objects.equals(callFunc.type.toString(), "void")){
            nowOperand = new Register(callFunc.type, nowFunc.getMidRegName());
            if (Objects.equals(callFunc.name, "getString")|| Objects.equals(callFunc.name, "toString")|| Objects.equals(callFunc.name, "string_substring")|| Objects.equals(callFunc.name, "string_add")){
                nowOperand.typeName = "string";
                //System.out.println("ll");
            }
            // System.out.println(callFunc.name);
            inst = new CallInst(nowOperand,nowBlock,callFunc,realParams);
        }else {
            //System.out.println(callFunc.type.toString());
            inst = new CallInst(nowBlock,callFunc,realParams);
        }
        nowBlock.push_back(inst);
        //System.out.println(nowOperand.typeName);
    }

    @Override
    public void visit(FuncDefNode node) {//参数，this怎么处理//先globalFind,找不到再struct find,然后再全局find
        nowScope = new ScopeForBuild();

        IrBlock headBlock = new IrBlock(nowFunc.getBlockLabel());
        nowFunc.setBlock(headBlock);
        nowBlock = new IrBlock(nowFunc.getBlockLabel());
        nowFunc.setBlock(nowBlock);

        if (Objects.equals(node.getIdentifier(), "main")){
            for(var it : globalInitNodes)it.acceptVisitor(this);
        }

        if (isInClass){
            LlvmPointerType itType = new LlvmPointerType(nowStruct);
            Register register = new Register(new LlvmPointerType(itType),"paramin_"+"this");
            AllocInst allocInst = new AllocInst(itType,register,nowFunc.headBlock);
            nowFunc.headBlock.push_back(allocInst);
            StoreInst storeInst = new StoreInst(nowBlock,new Register(new LlvmPointerType(nowStruct),"param_"+"this"),register);
            nowBlock.push_back(storeInst);
            //nowScope.addNode(it.getIdentifier(),it);
        }

        if (node.getParamList()!=null&&node.getParamList().size()!=0){
            for (var it : node.getParamList()){
                LlvmType itType = getLlvmType(it.getTypeNode());
                Register register = new Register(new LlvmPointerType((LlvmSingleValueType) itType),"paramin_"+it.getIdentifier());
                AllocInst allocInst = new AllocInst((LlvmSingleValueType) itType,register,nowFunc.headBlock);
                nowFunc.headBlock.push_back(allocInst);
                StoreInst storeInst = new StoreInst(nowBlock,it.operand,register);
                nowBlock.push_back(storeInst);
                it.operand = register;
                it.operand.typeName = it.getTypeNode().type_name;
                nowScope.addNode(it.getIdentifier(),it);
                //System.out.println(it.getIdentifier());
            }
        }


        node.getSuit().acceptVisitor(this);
        BrInst inst = new BrInst(headBlock,null,nowFunc.blocks.get(1).label,null);
        headBlock.push_back(inst);
        if (nowBlock.tailInst==null) {
            if (Objects.equals(node.getTypeNode().type_name, "int")){
                nowBlock.push_back(new RetInst(nowBlock,new IntegerConst(32,false,0)));
            }else if (Objects.equals(node.getTypeNode().type_name, "void")){
                nowBlock.push_back(new RetInst(nowBlock,null));
            }else{
                nowBlock.push_back(new RetInst(nowBlock,new NullPointerConst()));
            }
        }
    }

    @Override
    public void visit(IdExprNode node) {//正常情况下，全局已经处理过了。int a = 5;先将a放入headBlock,然后
        node.operand = getVar(node.getIdentifier());
//        if (node.getType()==null){
//            System.out.println("null");
//        }
        if (node.operand.typeName==null)
        node.operand.typeName = node.getType().typeName;
        if (isLeft){
            nowOperand = node.operand;
            return;
        }
        nowOperand = new Register((LlvmSingleValueType) ((LlvmPointerType)(node.operand.type)).pointeeType,nowFunc.getMidRegName());
        nowOperand.typeName = node.operand.typeName;
        LoadInst inst = new LoadInst(nowOperand,nowBlock,node.operand);
        nowBlock.push_back(inst);
    }

    @Override
    public void visit(IfStmtNode node) {
        IrBlock trueBlock = new IrBlock(nowFunc.getBlockLabel());
        node.getConditionExpr().acceptVisitor(this);
        nowFunc.setBlock(trueBlock);
        if (node.hasFalseStmt()){
            IrBlock falseBlock = new IrBlock(nowFunc.getBlockLabel());
            IrBlock nextBlock = new IrBlock(nowFunc.getBlockLabel());
//            nowFunc.setBlock(falseBlock);
//            nowFunc.setBlock(nextBlock);
            BrInst initBrInst = new BrInst(nowBlock,nowOperand, trueBlock.label, falseBlock.label);
            nowBlock.push_back(initBrInst);
            nowBlock = trueBlock;
            node.getTrueStmt().acceptVisitor(this);
            BrInst trueBrinst = new BrInst(nowBlock, nextBlock.label);
            nowBlock.push_back(trueBrinst);
            nowBlock = falseBlock;
            nowFunc.setBlock(falseBlock);
            node.getFalseStmt().acceptVisitor(this);
            BrInst falseBrinst = new BrInst(nowBlock, nextBlock.label);
            nowBlock.push_back(falseBrinst);
            nowFunc.setBlock(nextBlock);
            nowBlock = nextBlock;
        }else {
            IrBlock nextBlock = new IrBlock(nowFunc.getBlockLabel());
            BrInst initBrinst = new BrInst(nowBlock,nowOperand, trueBlock.label, nextBlock.label);
            nowBlock.push_back(initBrinst);
            nowBlock = trueBlock;
            node.getTrueStmt().acceptVisitor(this);
            BrInst trueBrinst = new BrInst(nowBlock, nextBlock.label);
            nowBlock.push_back(trueBrinst);
            nowFunc.setBlock(nextBlock);
            nowBlock = nextBlock;
        }
        //IrBlock falseBlock = new IrBlock(nowBlock.getBlockLabel());
    }

    @Override
    public void visit(IntContainNode node) {
        nowConst = new IntegerConst(32,false,node.getValue());
        node.operand = nowConst;
        nowOperand = nowConst;
    }

    @Override
    public void visit(MemberExprNode node) {
        //pig.a b.p.a
        boolean oldIsLeft = isLeft;
        isLeft = true;
        node.getObjExpr().acceptVisitor(this);
        LlvmStructType oldStruct = nowStruct;
        if (!(node.getObjExpr()instanceof ThisExprNode)){
            nowStruct = getStruct(nowOperand.typeName);
        }else {
            nowOperand = new Register(new LlvmPointerType(new LlvmPointerType(nowStruct)),"paramin_this");
        }
        isLeft = oldIsLeft;
        // System.out.println(nowOperand.typeName);
       // System.out.println(nowStruct);
        getThisMember(node.getMemberName());
        //nowStruct = oldStruct;
    }

    @Override
    public void visit(MethodExprNode node) {
        node.getObjExpr().acceptVisitor(this);
        if (Objects.equals(node.getMethodName(), "size")){
            Register arraySize = new Register(new LlvmIntegerType(32,false),nowFunc.getMidRegName());
            Register sizeAddress = new Register(new LlvmPointerType(new LlvmIntegerType(32,false)),nowFunc.getMidRegName());
            Register oralAddress = new Register(nowOperand.type,nowFunc.getMidRegName());
            ArrayList<Operand>index = new ArrayList<>();
            index.add(new IntegerConst(64,false,-1));
            Register sizeAddress2 = new Register(new LlvmPointerType(new LlvmIntegerType(32,false)),nowFunc.getMidRegName());
            nowBlock.push_back(new BitcastInst(sizeAddress2,nowBlock,nowOperand));
            nowBlock.push_back(new GetElementPtrInst(oralAddress,nowBlock,sizeAddress2,index));
            nowBlock.push_back(new BitcastInst(sizeAddress,nowBlock,oralAddress));
            nowBlock.push_back(new LoadInst(arraySize,nowBlock,sizeAddress));
            nowOperand = arraySize;
            nowOperand.typeName = "int";
            return;
        }
        //System.out.println(nowOperand.typeName);
        IrFunc callFunc = getFunc(nowOperand.typeName+"_"+node.getMethodName());
        if (callFunc==null)System.out.println(nowOperand.typeName+"_"+node.getMethodName());
        callFunc.isUsed = true;
        ArrayList<Operand>realParams = new ArrayList<>();
        realParams.add(nowOperand);
        if (node.getParams()!=null&&node.getParams().size()!=0){//忘记之前怎么处理的了，只能冗余一下了
            for (var it : node.getParams()){
                it.acceptVisitor(this);
                realParams.add(nowOperand);
            }
        }
        CallInst inst = null;
        // System.out.println(callFunc.type.toString());
        if (!Objects.equals(callFunc.type.toString(), "void")){
            nowOperand = new Register(callFunc.type, nowFunc.getMidRegName());
            if (Objects.equals(callFunc.name, "getString")|| Objects.equals(callFunc.name, "toString")|| Objects.equals(callFunc.name, "string_substring")|| Objects.equals(callFunc.name, "string_add")){
                nowOperand.typeName = "string";
                //System.out.println("ll");
            }else {nowOperand.typeName = callFunc.typeName;}
            inst = new CallInst(nowOperand,nowBlock,callFunc,realParams);
        }else {
            //System.out.println(callFunc.type.toString());
            inst = new CallInst(nowBlock,callFunc,realParams);
        }
        //nowOperand.typeName = callFunc.typeName;
        nowBlock.push_back(inst);
    }

    @Override
    public void visit(NameTypeNode node) {

    }

    public Register arrayCreator(ArrayList<ExprNode> idxNodes, int currentDim, LlvmPointerType currentType) {
        //System.out.println(currentType);
        Register destReg = new Register(currentType,nowFunc.getMidRegName());
        IrFunc mallocFunc = getFunc("malloc");
        Register sizeReg = new Register(new LlvmIntegerType(64,false),nowFunc.getMidRegName());
        idxNodes.get(currentDim).acceptVisitor(this);
        Register sextReg = new Register(new LlvmIntegerType(64,false),nowFunc.getMidRegName());
        nowBlock.push_back(new SextInst(sextReg,nowBlock,nowOperand));
        //产生size
        Register arraySize = new Register(new LlvmPointerType(new LlvmIntegerType(32,false)),nowFunc.getMidRegName());
        Register arraySizeBitCast = new Register(new LlvmPointerType(new LlvmIntegerType(8,false)),nowFunc.getMidRegName());
        ArrayList<Operand>sizeParams = new ArrayList<>();
        //sizeParams.add(new IntegerConst(64,false,4));
        Register sizeReg2 = new Register(new LlvmIntegerType(64,false),nowFunc.getMidRegName());
        nowBlock.push_back(new BinaryInst(sizeReg,nowBlock, BinaryInst.InstOp.mul,new IntegerConst(64,false,currentType.pointeeType.getAlignSize()),sextReg));
        nowBlock.push_back(new BinaryInst(sizeReg2,nowBlock, BinaryInst.InstOp.add,new IntegerConst(64,false,4),sizeReg));
        //ArrayList<Operand>params = new ArrayList<>();

        sizeParams.add(sizeReg2);

        nowBlock.push_back(new CallInst(arraySizeBitCast,nowBlock,mallocFunc,sizeParams));
        nowBlock.push_back(new BitcastInst(arraySize,nowBlock,arraySizeBitCast));
        nowBlock.push_back(new StoreInst(nowBlock,nowOperand,arraySize));

        //nowBlock.push_back(new BinaryInst(sizeReg,nowBlock, BinaryInst.InstOp.mul,new IntegerConst(64,false,currentType.pointeeType.getAlignSize()),sextReg));
        //ArrayList<Operand>params = new ArrayList<>();
        //params.add(sizeReg);
        Register bitcastReg = new Register(new LlvmPointerType(new LlvmIntegerType(32,false)),nowFunc.getMidRegName());
        ArrayList<Operand>params = new ArrayList<>();
        params.add(new IntegerConst(32,false,1));
        nowBlock.push_back(new GetElementPtrInst(bitcastReg,nowBlock,arraySize,params));
        //nowBlock.push_back(new CallInst(bitcastReg,nowBlock,mallocFunc,params));
        nowBlock.push_back(new BitcastInst(destReg,nowBlock,bitcastReg));
        //然后一个for循环
        if (currentDim!= idxNodes.size()-1){
            //init
            Register nowIndex = new Register(new LlvmPointerType(new LlvmIntegerType(32,false)),nowFunc.getMidRegName());
            nowBlock.push_back(new AllocInst(new LlvmIntegerType(32,false),nowIndex,nowBlock));
            nowBlock.push_back(new StoreInst(nowBlock,new IntegerConst(32,false,0),nowIndex));
            IrBlock condBlock = new IrBlock(nowFunc.getBlockLabel());
            IrBlock bodyBlock = new IrBlock(nowFunc.getBlockLabel());
            IrBlock incrBlock = new IrBlock(nowFunc.getBlockLabel());
            IrBlock nextBlock = new IrBlock(nowFunc.getBlockLabel());
            nowBlock.push_back(new BrInst(nowBlock, condBlock.label));
            //cond
            nowBlock = condBlock;
            nowFunc.setBlock(condBlock);
            // nowBlock.push_back(new BinaryInst(nowIndex,nowBlock, BinaryInst.InstOp.add,new IntegerConst(32,false,0),new IntegerConst(32,false,0)));
            Register nowIndexTemp = new Register(new LlvmIntegerType(32,false),nowFunc.getMidRegName());
            nowBlock.push_back(new LoadInst(nowIndexTemp,nowBlock,nowIndex));
            Register condReg = new Register(new LlvmIntegerType(8,true),nowFunc.getMidRegName());
            idxNodes.get(currentDim).acceptVisitor(this);
            nowBlock.push_back(new IcmpInst(condReg,nowBlock, IcmpInst.CondOp.slt,nowIndexTemp,nowOperand));
            nowBlock.push_back(new BrInst(nowBlock,condReg, bodyBlock.label, nextBlock.label));
            //body
            nowBlock = bodyBlock;
            nowFunc.setBlock(bodyBlock);
            Register ptr = arrayCreator(idxNodes, currentDim+1, (LlvmPointerType) currentType.pointeeType);
            Register nowIndexTemp2 = new Register(new LlvmIntegerType(32,false),nowFunc.getMidRegName());
            nowBlock.push_back(new LoadInst(nowIndexTemp2,nowBlock,nowIndex));
            Register sextIndex = new Register(new LlvmIntegerType(64,false),nowFunc.getMidRegName());
            nowBlock.push_back(new SextInst(sextIndex,nowBlock,nowIndexTemp2));
            Register destReg2 = new Register(currentType,nowFunc.getMidRegName());//todo  这个的类型
            ArrayList<Operand>params2 = new ArrayList<>();
            params2.add(sextIndex);
            nowBlock.push_back(new GetElementPtrInst(destReg2,nowBlock,destReg,params2));
//          Register ptr = arrayCreator(idxNodes, currentDim+1, (LlvmPointerType) currentType.pointeeType);
            nowBlock.push_back(new StoreInst(nowBlock,ptr,destReg2));
            nowBlock.push_back(new BrInst(nowBlock,incrBlock.label));
            nowBlock = incrBlock;
            nowFunc.setBlock(incrBlock);
            //%17 = getelementptr inbounds i32*, i32** %14, i64 %16
            // incr
            Register nowIndexTemp3 = new Register(new LlvmIntegerType(32,false),nowFunc.getMidRegName());
            nowBlock.push_back(new LoadInst(nowIndexTemp3,nowBlock,nowIndex));
            Register nowIndexTemp4 = new Register(new LlvmIntegerType(32,false),nowFunc.getMidRegName());
            nowBlock.push_back(new BinaryInst(nowIndexTemp4,nowBlock, BinaryInst.InstOp.add,new IntegerConst(32,false,1),nowIndexTemp3));
            nowBlock.push_back(new StoreInst(nowBlock,nowIndexTemp4,nowIndex));
            nowBlock.push_back(new BrInst(nowBlock,condBlock.label));
            //next
            nowBlock = nextBlock;
            nowFunc.setBlock(nextBlock);
        }
        return destReg;
    }

    @Override
    public void visit(NewExprNode node) {
        //node.num是怎么得到的啊,new int[3][3][]的num应该是3吧，希望当时是这样写的//感激，之前是这样写的
        //pig[] a; a = new pig[]//int[] a;a=new int
        //int*a = new int; int*a = new int[10];
        if (node.getNum()==0){
            Operand leftValue = nowOperand;
            String name = null;
            boolean isStruct = false;
            LlvmFirstClassType type = (LlvmSingleValueType) getLlvmType(node.getNameTypeNode());
            if (type instanceof LlvmPointerType){
                type = getStruct(node.getNameTypeNode().type_name);
                name = ((LlvmStructType) type).structName;
                isStruct = true;
            }
            IrFunc mallocFunc = getFunc("malloc");
            Register callReg = new Register(new LlvmPointerType(new LlvmIntegerType(8,false)),nowFunc.getMidRegName());
            ArrayList<Operand>params = new ArrayList<>();
            params.add(new IntegerConst(64,false,type.getAlignSize()));
            CallInst callInst = new CallInst(callReg,nowBlock,mallocFunc,params);
            Register bitCastReg = new Register(new LlvmPointerType(type),nowFunc.getMidRegName());
            BitcastInst bitcastInst = new BitcastInst(bitCastReg,nowBlock,callReg);
            nowBlock.push_back(callInst);
            nowBlock.push_back(bitcastInst);
            nowOperand = bitCastReg;
            if (isStruct){
                ArrayList<Operand>paramsConstruct = new ArrayList<>();
                paramsConstruct.add(nowOperand);
                nowBlock.push_back(new CallInst(nowBlock,getFunc(name+"_" +name),paramsConstruct));
            }
        }else {//a = new int;//这个我现在的体系解决不了 //new pig //new pig[10]
            boolean oldIsLeft = isLeft;//SB设计
            isLeft = false;
            nowOperand = arrayCreator(node.getExprInBracket(),0, (LlvmPointerType) getLlvmArrayType(node.getNameTypeNode(),node.getNum()));
            isLeft = oldIsLeft;
        }
    }

    @Override
    public void visit(NullContainNode node) {
        nowConst = new NullPointerConst();
        node.operand = nowConst;
        nowOperand = nowConst;
    }


    @Override
    public void visit(ProgramNode node) {
        for (var it : node.getProgramSectionNodeList()){
            if (it instanceof ClassDefNode){
                nowStruct = getStruct(((ClassDefNode) it).getIdentifier());
                isInClass = true;
                for(var method : ((ClassDefNode) it).getMethods()){
                    nowFunc = getFunc(((ClassDefNode) it).getIdentifier()+"_"+method.getIdentifier());
                    method.acceptVisitor(this);
                }
            }else if (it instanceof FuncDefNode){
                isInClass = false;
                nowFunc = getFunc(((FuncDefNode) it).getIdentifier());
                // System.out.println(nowFunc.toString());
                it.acceptVisitor(this);
            }else {

            }
        }
    }

    @Override
    public void visit(ProgramSectionNode node) {

    }

    @Override
    public void visit(PureExprStmtNode node) {
        node.getExprNode().acceptVisitor(this);
    }

    @Override
    public void visit(ReturnStmtNode node) {
        RetInst inst = null;
        if (node.hasExprNode()) {
            node.getExprNode().acceptVisitor(this);
            inst = new RetInst(nowBlock,nowOperand);
        }else {
            inst = new RetInst(nowBlock,null);
        }
        nowBlock.push_back(inst);
    }

    @Override
    public void visit(StmtNode node) {

    }

    @Override
    public void visit(StringContainNode node) {
        nowConst = getStringConst(node.getValue());
        if (nowConst==null){
            setStringConst(node.getValue());
            nowConst = getStringConst(node.getValue());
        }
        node.operand = nowConst;
        nowConst.typeName = "string";
        nowOperand = nowConst;
    }

//    @Override
//    public void visit(SubScriptExprNode node) {
//        boolean oldIsLeft = isLeft;
//        isLeft = false;
//        node.getExprNode().acceptVisitor(this);
//        String typeName = nowOperand.typeName;
//        Register pointer = (Register) nowOperand;
//        //System.out.println(pointer.type);
//        Register destReg = new Register(pointer.type,nowFunc.getMidRegName());
//        node.getIndexNode().acceptVisitor(this);
//        ArrayList<Operand>params = new ArrayList<>();
//        Register sextReg = new Register(new LlvmIntegerType(64,false),nowFunc.getMidRegName());
//        nowBlock.push_back(new SextInst(sextReg,nowBlock,nowOperand));
//        params.add(sextReg);
//        nowBlock.push_back(new GetElementPtrInst(destReg,nowBlock,pointer,params));
//        nowOperand = destReg;
//        isLeft = oldIsLeft;
//        if (!isLeft){
//            nowOperand = new Register((LlvmSingleValueType) ((LlvmPointerType)destReg.type).pointeeType,nowFunc.getMidRegName());
//            nowBlock.push_back(new LoadInst(nowOperand,nowBlock,destReg));
//        }
//        nowOperand.typeName = typeName;
//    }

    @Override
    public void visit(SubScriptExprNode node) {
        boolean oldIsLeft = isLeft;
        isLeft = false;
        node.getExprNode().acceptVisitor(this);
        String typeName = nowOperand.typeName;
        Register pointer = (Register) nowOperand;
        //System.out.println(pointer.type);
        Register destReg = new Register(pointer.type,nowFunc.getMidRegName());
        //isLeft = false;
        node.getIndexNode().acceptVisitor(this);
        ArrayList<Operand>params = new ArrayList<>();
        Register sextReg = new Register(new LlvmIntegerType(64,false),nowFunc.getMidRegName());
        nowBlock.push_back(new SextInst(sextReg,nowBlock,nowOperand));
        params.add(sextReg);
        nowBlock.push_back(new GetElementPtrInst(destReg,nowBlock,pointer,params));
        nowOperand = destReg;
        isLeft = oldIsLeft;
        if (!isLeft){
            nowOperand = new Register((LlvmSingleValueType) ((LlvmPointerType)destReg.type).pointeeType,nowFunc.getMidRegName());
            nowBlock.push_back(new LoadInst(nowOperand,nowBlock,destReg));
        }
        nowOperand.typeName = typeName;
    }

    @Override
    public void visit(SuffixExprNode node) {
        node.getExprNode().acceptVisitor(this);
        Operand lReg = nowOperand;
        Inst inst = null;
        Register register = new Register(lReg.type,nowFunc.getMidRegName());
        switch (node.getOp()){
            case SufPlus:{
                Boolean oldIsLeft = isLeft;
                isLeft = true;
                node.getExprNode().acceptVisitor(this);
                isLeft = false;
                isLeft = oldIsLeft;
                inst = new BinaryInst(register,nowBlock, BinaryInst.InstOp.add,lReg,new IntegerConst(32,false,1));
                nowBlock.push_back(inst);
                Inst storeInst = new StoreInst(nowBlock,register,nowOperand);
                nowBlock.push_back(storeInst);
                nowOperand = lReg;
                break;
            }
            case SufMinus:{
                Boolean oldIsLeft = isLeft;
                isLeft = true;
                node.getExprNode().acceptVisitor(this);
                isLeft = false;
                isLeft = oldIsLeft;
                inst = new BinaryInst(register,nowBlock, BinaryInst.InstOp.sub,lReg,new IntegerConst(32,false,1));
                nowBlock.push_back(inst);
                Inst storeInst = new StoreInst(nowBlock,register,nowOperand);
                nowBlock.push_back(storeInst);
                nowOperand = lReg;
                break;
            }
        }
    }

    @Override
    public void visit(ThisExprNode node) {
        //nowOperand = nowStruct;
        //nowOperand = new Register(new LlvmPointerType(nowStruct),nowFunc.getMidRegName());
        //nowBlock.push_back(new LoadInst(nowOperand,nowBlock,new Register(new LlvmPointerType(new LlvmPointerType(nowStruct)),"paramin_this")));
    }

    @Override
    public void visit(TypeNode node) {

    }

    @Override
    public void visit(VarDefStmtNode node) {
        for(var it : node.getVarDefNodes()){
            it.acceptVisitor(this);
        }
    }

    @Override
    public void visit(VarDefNode node) {//int a = 5;//拆开之后是int a a=5;还是int a = 5?
        LlvmType nodeType = getLlvmType(node.getTypeNode());
        Register register = new Register(new LlvmPointerType((LlvmSingleValueType) nodeType),node.getIdentifier()+nowBlock.getBlockLabel());
        AllocInst allocInst = new AllocInst((LlvmSingleValueType) nodeType,register,nowFunc.headBlock);
        nowFunc.headBlock.push_back(allocInst);
        node.operand = register;
        node.operand.typeName = node.getTypeNode().type_name;
        nowOperand = register;
        nowScope.addNode(node.getIdentifier(),node);
        if (node.getInit()!=null){
            node.getInit().acceptVisitor(this);
            StoreInst storeInst = new StoreInst(nowBlock,nowOperand,register);
            nowBlock.push_back(storeInst);
        }
    }

    @Override
    public void visit(VarDefListNode node) {
        for (var it : node.getVarDefNodes())it.acceptVisitor(this);
    }

    @Override
    public void visit(VoidTypeNode node) {

    }

    @Override
    public void visit(WhileStmtNode node) {
        IrBlock condBlock = new IrBlock(nowFunc.getBlockLabel());
        IrBlock bodyBlock = new IrBlock(nowFunc.getBlockLabel());
        IrBlock nextBlock = new IrBlock(nowFunc.getBlockLabel());
        IrBlock oldNextBlock = loopNextBlock;
        IrBlock oldCondBlock = loopCondBlock;
        loopNextBlock = nextBlock;
        loopCondBlock = condBlock;
        BrInst initInst = new BrInst(nowBlock, condBlock.label);
        nowBlock.push_back(initInst);
        nowFunc.setBlock(condBlock);
        nowBlock = condBlock;
        node.getConditionExpr().acceptVisitor(this);
        BrInst condBrinst = new BrInst(nowBlock,nowOperand, bodyBlock.label, nextBlock.label);
        nowBlock.push_back(condBrinst);
        nowFunc.setBlock(bodyBlock);
        nowBlock = bodyBlock;
        node.getStmt().acceptVisitor(this);
        BrInst bodydBrinst = new BrInst(nowBlock, condBlock.label);
        nowBlock.push_back(bodydBrinst);
        nowFunc.setBlock(nextBlock);
        nowBlock = nextBlock;
        loopNextBlock = oldNextBlock;
        loopCondBlock = oldCondBlock;
    }

    @Override
    public void visit(LambdaExprNode node) {
        System.out.println("wo want to lose weight");
    }
}