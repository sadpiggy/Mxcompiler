package IR.operand;

import IR.llvmType.LlvmIntegerType;
import IR.llvmType.LlvmPointerType;
import IR.llvmType.LlvmSingleValueType;

public class StringConst extends Const{
   public String name;
   public String value;

    public StringConst(String name,String value) {//name .str.8  ;value hello //value += "/n"
        super(new LlvmPointerType(new LlvmIntegerType(8,false)));
        this.name = name;
        this.value = value + "/n";
    }


    @Override
    public String toString() {//用于operand.toString
        return "getelementptr inbounds ([" + value.length() + " x i8] ,[" + value.length() + " x i8]* " + name + ", i64 0, i64 0)";
    }

    public String getReplaceValue(){
        return value.replace("\\", "\\5C")
                .replace("\n", "\\0A")
                .replace("\"", "\\22")
                .replace("\t", "\\09");
    }

    public String toPrintString(){
        return "@" + name +  " = constant [5 x i8] c\"" + getReplaceValue() +"\", align 1";
    }

}
