package IR.operand;

import IR.llvmType.LlvmFirstClassType;
import IR.llvmType.LlvmPointerType;
import IR.llvmType.LlvmSingleValueType;
import IR.llvmType.LlvmStructType;

public class GlobalOperand extends Operand{
    public String name;
    public String value;
    public int alignSize;
   // public boolean isString;
   // boolean isConst;
   // boolean isConstString;

    public GlobalOperand(LlvmFirstClassType type,String name,String value) {
        super(new LlvmPointerType(type));
        this.name = name;
        this.value = value;//String 的value为 string const的一串编号
       // this.isString = isString;
       if (type instanceof LlvmStructType){
           alignSize = 8;
       }else {
           if (type.getSize()==1||type.getSize()==8){
               alignSize = 1;
           }else if (type.getSize()==32){
               alignSize = 4;
           }else{
               alignSize = 8;
           }
       }
       // this.isConst = isConst;
       // this.isConstString = isString;
    }


//    public String getValue(){
//        return value.replace("\\", "\\5C")
//                .replace("\n", "\\0A")
//                .replace("\"", "\\22")
//                .replace("\t", "\\09");
//    }

    @Override
    public String toString() {
            return "@" + name;
    }

    public String toPrintString(){
        //if (!isString){
            return "@" + name +" = global " + ((LlvmPointerType)(type)).pointeeType.toString() + " " + value +", align " + alignSize;//     i32 5, align 4
        //}
//        else {
//            return "@" + name + " = dso_local global i8* getelementptr inbounds ([" +   5 x i8], [5 x i8]* @.str, i32 0, i32 0), align 8
//            return "@" + name + " = global i8* getelementptr inbounds ([" + value.length() + " x i8] ,[" + value.length() + " x i8]* " + name + ", i64 0, i64 0)";
//        }
    }

}
