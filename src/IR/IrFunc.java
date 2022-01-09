package IR;

import IR.llvmType.LlvmSingleValueType;
import IR.operand.Operand;
import IR.operand.Register;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;

public class IrFunc extends Operand{
    public String name;
    public ArrayList<Register>formParams;
    public LinkedList<IrBlock>blocks;
    public boolean isBuildIn;
    public boolean isDeclare;
    public int nowBlockNum;//p0
    public IrBlock headBlock;
    public int nowMidRegNum;//m0
    public boolean isUsed;
    //public int nowRegisterNum;
   // public String formParamString;
    //public IrScope irScope;


    public IrFunc(LlvmSingleValueType type,String name,ArrayList<Register>formParams,boolean isBuildIn) {
        super(type);
        isDeclare = false;
        this.name = name;
        this.formParams = formParams;
        this.blocks = new LinkedList<>();
        this.isBuildIn = isBuildIn;
        nowBlockNum = 0;
        headBlock = null;
        nowMidRegNum = 0;
        isUsed = false;
        //this.nowRegisterNum = nowRegisterNum;
    }

    public String getBlockLabel(){
        return "block" + name + nowBlockNum++;
    }

    public String getMidRegName(){return "m" + nowMidRegNum++;}

//    public void setBlocks(LinkedList<IrBlock>blocks){//blockNum自己加
//        this.blocks = blocks;
//        //nowBlockNum++;
//    }

    public void setBlock(IrBlock block){
        blocks.addLast(block);
        if (headBlock == null)headBlock = block;
    }

    public String toString(ArrayList<Operand>realParams){
        isUsed = true;
        StringBuilder stringBuilder = new StringBuilder("(");
        if (realParams!=null){
            stringBuilder.append(realParams.get(0).type.toString()).append(" ").append(realParams.get(0).toString());
            for(int i=1;i<realParams.size();i++){
                stringBuilder.append(",");
                stringBuilder.append(realParams.get(i).type.toString()).append(" ").append(realParams.get(i).toString());
            }
        }
        stringBuilder.append(")");

       if (Objects.equals(name, "printf")){
           return "i32 (i8*, ...) @printf" + stringBuilder.toString();
       }else if (Objects.equals(name, "scanf")){
           return "i32 (i8*, ...) @scanf" + stringBuilder.toString();
       }else if (Objects.equals(name, "sprintf")){
           return "i32 (i8*, i8*, ...) @sprintf" + stringBuilder.toString();
       }else if (Objects.equals(name, "strcpy")){
           return "i8* @strcpy" + stringBuilder.toString();
       }else if (Objects.equals(name, "strcat")){
           return "i8* @strcat" + stringBuilder.toString();
       }else if (Objects.equals(name, "strcmp")){
           return "i32 @strcmp" + stringBuilder.toString();
       }else if (Objects.equals(name, "strlen")){
           return "i64 @strlen" + stringBuilder.toString();
       }else if (Objects.equals(name, "memcpy")){
           return "void @memcpy" + stringBuilder.toString();
       }else if (Objects.equals(name, "sscanf")){
           return "i32 (i8*, i8*, ...) @sscanf" + stringBuilder.toString();
       } else if (Objects.equals(name, "print")){
           return "void @print" + stringBuilder.toString();
       }else if (Objects.equals(name, "println")){
           return "void @println" + stringBuilder.toString();
       }else if (Objects.equals(name, "printInt")){
           return "void @printInt" + stringBuilder.toString();
       }else if (Objects.equals(name, "printlnInt")){
           return "void @printlnInt" + stringBuilder.toString();
       }else if (Objects.equals(name, "getString")){
           return "i8* @getString" + stringBuilder.toString();
       }else if (Objects.equals(name, "getInt")){
           return "i32 @getInt" + stringBuilder.toString();
       }else if (Objects.equals(name, "toString")){
           return "i8* @toString" + stringBuilder.toString();
       }else if (Objects.equals(name, "malloc")){
           return "i8* @malloc" + stringBuilder.toString();
       }else if (Objects.equals(name, "string_add")){
           return "i8* @string_add" + stringBuilder.toString();
       }else if (Objects.equals(name, "string_equal")){
           return "i1 @string_equal" + stringBuilder.toString();
       }else if (Objects.equals(name, "string_notequal")){
           return "i1 @string_notequal" + stringBuilder.toString();
       }else if (Objects.equals(name, "string_lesser")){
           return "i1 @string_lesser" + stringBuilder.toString();
       }else if (Objects.equals(name, "string_greater")){
           return "i1 @string_greater" + stringBuilder.toString();
       }else if (Objects.equals(name, "string_lessEqual")){
           return "i1 @string_lessEqual" + stringBuilder.toString();
       }else if (Objects.equals(name, "string_greatEqual")){
           return "i1 @string_greatEqual" + stringBuilder.toString();
       }

        return type.toString() +  " @" + name + stringBuilder.toString();
    }

    @Override
    public String toString() {
        return toString(null);
    }

    public void PrintIr() throws FileNotFoundException {
        if (isBuildIn){
            switch (name){
                //declare
                case "printf": {System.out.println("declare dso_local i32 @printf(i8*, ...)");break;}
                case "scanf":{ System.out.println("declare dso_local i32 @scanf(i8*, ...)");break;}
                case "malloc":{System.out.println("declare dso_local noalias i8* @malloc(i64)");break;}

                case "sprintf": {System.out.println("declare dso_local i32 @sprintf(i8*, i8*, ...)");break;}
                case "strcpy": {System.out.println("declare dso_local i8* @strcpy(i8*, i8*)");break;}
                case "strcat": {System.out.println("declare dso_local i8* @strcat(i8*, i8*)");break;}
                case "strcmp": {System.out.println("declare dso_local i32 @strcmp(i8*, i8*)");break;}
                case "strlen": {System.out.println("declare dso_local i64 @strlen(i8*)");break;}
                case "memcpy": {System.out.println("declare void @llvm.memcpy.p0i8.p0i8.i64(i8* noalias nocapture writeonly, i8* noalias nocapture readonly, i64, i1 immarg)");break;}
                case "sscanf": {System.out.println("declare dso_local i32 @__isoc99_sscanf(i8*, i8*, ...)");break;}
                //buildIn
                case "print":{
                    if (isUsed){
                        System.out.println("define dso_local void @print(i8* %0) #0 {\n" +
                                "  %2 = alloca i8*, align 8\n" +
                                "  store i8* %0, i8** %2, align 8\n" +
                                "  %3 = load i8*, i8** %2, align 8\n" +
                                "  %4 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([3 x i8], [3 x i8]* @.str, i64 0, i64 0), i8* %3)\n" +
                                "  ret void\n" +
                                "}");
                    }
                    break;}
                case "println":{
                    if (isUsed){
                        System.out.println("define dso_local void @println(i8* %0) #0 {\n" +
                                "  %2 = alloca i8*, align 8\n" +
                                "  store i8* %0, i8** %2, align 8\n" +
                                "  %3 = load i8*, i8** %2, align 8\n" +
                                "  %4 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.str.1, i64 0, i64 0), i8* %3)\n" +
                                "  ret void\n" +
                                "}");
                    }
                    break;}
                case "printInt":{
                    if (isUsed){
                        System.out.println("define dso_local void @printInt(i32 %0) #0 {\n" +
                                "  %2 = alloca i32, align 4\n" +
                                "  store i32 %0, i32* %2, align 4\n" +
                                "  %3 = load i32, i32* %2, align 4\n" +
                                "  %4 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([3 x i8], [3 x i8]* @.str.2, i64 0, i64 0), i32 %3)\n" +
                                "  ret void\n" +
                                "}");
                    }
                    break;}
                case "printlnInt":{
                    if (isUsed){
                        System.out.println("define dso_local void @printlnInt(i32 %0) #0 {\n" +
                                "  %2 = alloca i32, align 4\n" +
                                "  store i32 %0, i32* %2, align 4\n" +
                                "  %3 = load i32, i32* %2, align 4\n" +
                                "  %4 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.str.3, i64 0, i64 0), i32 %3)\n" +
                                "  ret void\n" +
                                "}");
                    }
                    break;}
                case "getString":{
                    if (isUsed){
                        System.out.println("define dso_local i8* @getString() #0 {\n" +
                                "  %1 = alloca i8*, align 8\n" +
                                "  %2 = call noalias i8* @malloc(i64 1024) #5\n" +
                                "  store i8* %2, i8** %1, align 8\n" +
                                "  %3 = load i8*, i8** %1, align 8\n" +
                                "  %4 = call i32 (i8*, ...) @scanf(i8* getelementptr inbounds ([3 x i8], [3 x i8]* @.str, i64 0, i64 0), i8* %3)\n" +
                                "  %5 = load i8*, i8** %1, align 8\n" +
                                "  ret i8* %5\n" +
                                "}");
                    }
                    break;}
                case "getInt":{
                    if (isUsed){
                        System.out.println("define dso_local i32 @getInt() #0 {\n" +
                                "  %1 = alloca i32, align 4\n" +
                                "  %2 = call i32 (i8*, ...) @scanf(i8* getelementptr inbounds ([3 x i8], [3 x i8]* @.str.2, i64 0, i64 0), i32* %1)\n" +
                                "  %3 = load i32, i32* %1, align 4\n" +
                                "  ret i32 %3\n" +
                                "}");
                    }
                    break;}
                case "toString":{
                    if (isUsed){
                        System.out.println("define dso_local i8* @toString(i32 %0) #0 {\n" +
                                "  %2 = alloca i32, align 4\n" +
                                "  %3 = alloca i8*, align 8\n" +
                                "  store i32 %0, i32* %2, align 4\n" +
                                "  %4 = call noalias i8* @malloc(i64 15) #5\n" +
                                "  store i8* %4, i8** %3, align 8\n" +
                                "  %5 = load i8*, i8** %3, align 8\n" +
                                "  %6 = load i32, i32* %2, align 4\n" +
                                "  %7 = call i32 (i8*, i8*, ...) @sprintf(i8* %5, i8* getelementptr inbounds ([3 x i8], [3 x i8]* @.str.2, i64 0, i64 0), i32 %6) #5\n" +
                                "  %8 = load i8*, i8** %3, align 8\n" +
                                "  ret i8* %8\n" +
                                "}");
                    }
                    break;}
                case "string_length":{
                    if (isUsed){
                        System.out.println("define dso_local i32 @string_length(i8* %0) #0 {\n" +
                                "  %2 = alloca i8*, align 8\n" +
                                "  store i8* %0, i8** %2, align 8\n" +
                                "  %3 = load i8*, i8** %2, align 8\n" +
                                "  %4 = call i64 @strlen(i8* %3) #6\n" +
                                "  %5 = trunc i64 %4 to i32\n" +
                                "  ret i32 %5\n" +
                                "}");
                    }
                    break;}
                case "string_substring":{
                    if (isUsed){
                        System.out.println("define dso_local i8* @string_substring(i8* %0, i32 %1, i32 %2) #0 {\n" +
                                "  %4 = alloca i8*, align 8\n" +
                                "  %5 = alloca i32, align 4\n" +
                                "  %6 = alloca i32, align 4\n" +
                                "  %7 = alloca i8*, align 8\n" +
                                "  store i8* %0, i8** %4, align 8\n" +
                                "  store i32 %1, i32* %5, align 4\n" +
                                "  store i32 %2, i32* %6, align 4\n" +
                                "  %8 = load i32, i32* %6, align 4\n" +
                                "  %9 = load i32, i32* %5, align 4\n" +
                                "  %10 = sub nsw i32 %8, %9\n" +
                                "  %11 = add nsw i32 %10, 1\n" +
                                "  %12 = sext i32 %11 to i64\n" +
                                "  %13 = mul i64 1, %12\n" +
                                "  %14 = call noalias i8* @malloc(i64 %13) #5\n" +
                                "  store i8* %14, i8** %7, align 8\n" +
                                "  %15 = load i8*, i8** %7, align 8\n" +
                                "  %16 = load i8*, i8** %4, align 8\n" +
                                "  %17 = load i32, i32* %5, align 4\n" +
                                "  %18 = sext i32 %17 to i64\n" +
                                "  %19 = getelementptr inbounds i8, i8* %16, i64 %18\n" +
                                "  %20 = load i32, i32* %6, align 4\n" +
                                "  %21 = load i32, i32* %5, align 4\n" +
                                "  %22 = sub nsw i32 %20, %21\n" +
                                "  %23 = sext i32 %22 to i64\n" +
                                "  call void @llvm.memcpy.p0i8.p0i8.i64(i8* align 1 %15, i8* align 1 %19, i64 %23, i1 false)\n" +
                                "  %24 = load i8*, i8** %7, align 8\n" +
                                "  %25 = load i32, i32* %6, align 4\n" +
                                "  %26 = load i32, i32* %5, align 4\n" +
                                "  %27 = sub nsw i32 %25, %26\n" +
                                "  %28 = sext i32 %27 to i64\n" +
                                "  %29 = getelementptr inbounds i8, i8* %24, i64 %28\n" +
                                "  store i8 0, i8* %29, align 1\n" +
                                "  %30 = load i8*, i8** %7, align 8\n" +
                                "  ret i8* %30\n" +
                                "}\n");
                    }
                    break;}
                case "string_parseInt":{
                    if (isUsed){
                        System.out.println("define dso_local i32 @string_parseInt(i8* %0) #0 {\n" +
                                "  %2 = alloca i8*, align 8\n" +
                                "  %3 = alloca i32, align 4\n" +
                                "  store i8* %0, i8** %2, align 8\n" +
                                "  %4 = load i8*, i8** %2, align 8\n" +
                                "  %5 = call i32 (i8*, i8*, ...) @__isoc99_sscanf(i8* %4, i8* getelementptr inbounds ([3 x i8], [3 x i8]* @.str.2, i64 0, i64 0), i32* %3) #5\n" +
                                "  %6 = load i32, i32* %3, align 4\n" +
                                "  ret i32 %6\n" +
                                "}\n");
                    }
                    break;}
                case "string_ord":{
                    if (isUsed){
                        System.out.println("define dso_local i32 @string_ord(i8* %0, i32 %1) #0 {\n" +
                                "  %3 = alloca i8*, align 8\n" +
                                "  %4 = alloca i32, align 4\n" +
                                "  store i8* %0, i8** %3, align 8\n" +
                                "  store i32 %1, i32* %4, align 4\n" +
                                "  %5 = load i8*, i8** %3, align 8\n" +
                                "  %6 = load i32, i32* %4, align 4\n" +
                                "  %7 = sext i32 %6 to i64\n" +
                                "  %8 = getelementptr inbounds i8, i8* %5, i64 %7\n" +
                                "  %9 = load i8, i8* %8, align 1\n" +
                                "  %10 = sext i8 %9 to i32\n" +
                                "  ret i32 %10\n" +
                                "}\n");
                    }
                    break;}
                case "string_add":{
                    if (isUsed){
                        System.out.println("define dso_local i8* @string_add(i8* %0, i8* %1) #0 {\n" +
                                "  %3 = alloca i8*, align 8\n" +
                                "  %4 = alloca i8*, align 8\n" +
                                "  %5 = alloca i8*, align 8\n" +
                                "  store i8* %0, i8** %3, align 8\n" +
                                "  store i8* %1, i8** %4, align 8\n" +
                                "  %6 = call noalias i8* @malloc(i64 1024) #5\n" +
                                "  store i8* %6, i8** %5, align 8\n" +
                                "  %7 = load i8*, i8** %5, align 8\n" +
                                "  %8 = load i8*, i8** %3, align 8\n" +
                                "  %9 = call i8* @strcpy(i8* %7, i8* %8) #5\n" +
                                "  %10 = load i8*, i8** %5, align 8\n" +
                                "  %11 = load i8*, i8** %4, align 8\n" +
                                "  %12 = call i8* @strcat(i8* %10, i8* %11) #5\n" +
                                "  %13 = load i8*, i8** %5, align 8\n" +
                                "  ret i8* %13\n" +
                                "}");
                    }
                    break;}
                case "string_equal":{
                    if (isUsed){
                        System.out.println("define dso_local zeroext i1 @string_equal(i8* %0, i8* %1) #0 {\n" +
                                "  %3 = alloca i8*, align 8\n" +
                                "  %4 = alloca i8*, align 8\n" +
                                "  store i8* %0, i8** %3, align 8\n" +
                                "  store i8* %1, i8** %4, align 8\n" +
                                "  %5 = load i8*, i8** %3, align 8\n" +
                                "  %6 = load i8*, i8** %4, align 8\n" +
                                "  %7 = call i32 @strcmp(i8* %5, i8* %6) #6\n" +
                                "  %8 = icmp eq i32 %7, 0\n" +
                                "  ret i1 %8\n" +
                                "}");
                    }
                    break;}
                case "string_notequal":{
                    if (isUsed){
                        System.out.println("define dso_local zeroext i1 @string_notequal(i8* %0, i8* %1) #0 {\n" +
                                "  %3 = alloca i8*, align 8\n" +
                                "  %4 = alloca i8*, align 8\n" +
                                "  store i8* %0, i8** %3, align 8\n" +
                                "  store i8* %1, i8** %4, align 8\n" +
                                "  %5 = load i8*, i8** %3, align 8\n" +
                                "  %6 = load i8*, i8** %4, align 8\n" +
                                "  %7 = call i32 @strcmp(i8* %5, i8* %6) #6\n" +
                                "  %8 = icmp ne i32 %7, 0\n" +
                                "  ret i1 %8\n" +
                                "}");
                    }
                    break;}
                case "string_lesser":{
                    if (isUsed){
                        System.out.println("define dso_local zeroext i1 @string_lesser(i8* %0, i8* %1) #0 {\n" +
                                "  %3 = alloca i8*, align 8\n" +
                                "  %4 = alloca i8*, align 8\n" +
                                "  store i8* %0, i8** %3, align 8\n" +
                                "  store i8* %1, i8** %4, align 8\n" +
                                "  %5 = load i8*, i8** %3, align 8\n" +
                                "  %6 = load i8*, i8** %4, align 8\n" +
                                "  %7 = call i32 @strcmp(i8* %5, i8* %6) #6\n" +
                                "  %8 = icmp slt i32 %7, 0\n" +
                                "  ret i1 %8\n" +
                                "}");
                    }
                    break;}
                case "string_greater":{
                    if (isUsed){
                        System.out.println("define dso_local zeroext i1 @string_greater(i8* %0, i8* %1) #0 {\n" +
                                "  %3 = alloca i8*, align 8\n" +
                                "  %4 = alloca i8*, align 8\n" +
                                "  store i8* %0, i8** %3, align 8\n" +
                                "  store i8* %1, i8** %4, align 8\n" +
                                "  %5 = load i8*, i8** %3, align 8\n" +
                                "  %6 = load i8*, i8** %4, align 8\n" +
                                "  %7 = call i32 @strcmp(i8* %5, i8* %6) #6\n" +
                                "  %8 = icmp sgt i32 %7, 0\n" +
                                "  ret i1 %8\n" +
                                "}");
                    }
                    break;}
                case "string_lessEqual":{
                    if (isUsed){
                        System.out.println("define dso_local zeroext i1 @string_lessEqual(i8* %0, i8* %1) #0 {\n" +
                                "  %3 = alloca i8*, align 8\n" +
                                "  %4 = alloca i8*, align 8\n" +
                                "  store i8* %0, i8** %3, align 8\n" +
                                "  store i8* %1, i8** %4, align 8\n" +
                                "  %5 = load i8*, i8** %3, align 8\n" +
                                "  %6 = load i8*, i8** %4, align 8\n" +
                                "  %7 = call i32 @strcmp(i8* %5, i8* %6) #6\n" +
                                "  %8 = icmp sle i32 %7, 0\n" +
                                "  ret i1 %8\n" +
                                "}");
                    }
                    break;}
                case "string_greatEqual":{
                    if (isUsed){
                        System.out.println("define dso_local zeroext i1 @string_greatEqual(i8* %0, i8* %1) #0 {\n" +
                                "  %3 = alloca i8*, align 8\n" +
                                "  %4 = alloca i8*, align 8\n" +
                                "  store i8* %0, i8** %3, align 8\n" +
                                "  store i8* %1, i8** %4, align 8\n" +
                                "  %5 = load i8*, i8** %3, align 8\n" +
                                "  %6 = load i8*, i8** %4, align 8\n" +
                                "  %7 = call i32 @strcmp(i8* %5, i8* %6) #6\n" +
                                "  %8 = icmp sge i32 %7, 0\n" +
                                "  ret i1 %8\n" +
                                "}");
                    }
                    break;}
            }
            return;
        }
//        System.setOut(new PrintStream(new BufferedOutputStream(
//                new FileOutputStream("ravel/build/test.ll")),true));
        StringBuilder stringBuilder = new StringBuilder("(");
        if (formParams!=null){
            stringBuilder.append(formParams.get(0).type.toString());
            stringBuilder.append(" ");
            stringBuilder.append(formParams.get(0).toString());
            for(int i=1;i<formParams.size();i++){
                stringBuilder.append(",");
                stringBuilder.append(formParams.get(i).type.toString());
                stringBuilder.append(" ");
                stringBuilder.append(formParams.get(i).toString());
            }
        }
        stringBuilder.append(") {");


        //else {
            System.out.println(//define dso_local void @pp(i32 %0) #0 {
                    "define " + type.toString() + " @" + name + stringBuilder.toString()
            );
        //}
        //if (!isDeclare){
            for(var it : blocks)it.printIr();
            //System.out.println(headBlock.insts.get(0).toString());
            System.out.println("}");
        //}
    }

}
