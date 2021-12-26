package IR.operand;

import IR.llvmType.LlvmIntegerType;
import IR.llvmType.LlvmPointerType;
import IR.llvmType.LlvmSingleValueType;

public class StringConst extends Const{
    public String name;//str.hello
    public String value;//"hello"
    public int stringSize;

    public StringConst(String name,String value) {//name .str.8  ;value hello //value += "/0"//在里面加，方便字符串拼加
        super(new LlvmPointerType(new LlvmIntegerType(8,false)));
        this.name = name;
        this.value = value;
        stringSize = value.length() + 1;
        for(int i=0;i<value.length();i++){
            if (value.charAt(i)=='\\'){
                stringSize--;
                if (i!=value.length()-1&&value.charAt(i+1)=='\\'){
                    i = i+1;
                }
            }
        }
    }

    public int stringSize(){
        return stringSize;
    }


    @Override
    public String toString() {//用于operand.toString
        return "getelementptr inbounds ([" + stringSize()+ " x i8] ,[" + stringSize() + " x i8]* @" + name + ", i64 0, i64 0)";
    }

    public String getReplaceValue(){
        return ((value)
                //.replace("\0", "\00")
                .replace("\\\\", "\\5C")
                .replace("\\n", "\\0A")
                .replace("\\\"", "\\22")
                .replace("\\t", "\\09") )+ "\\00";
    }

    public String toPrintString(){//constant [5 x i8] c"hello ", align 1
        return "@" + name +  " = constant [" +stringSize() +  " x i8] c\"" + getReplaceValue() +"\", align 1";
    }

    //public StringConst stringAdd()

}
