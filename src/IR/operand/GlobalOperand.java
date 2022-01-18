package IR.operand;

import IR.llvmType.LlvmFirstClassType;
import IR.llvmType.LlvmPointerType;
import IR.llvmType.LlvmSingleValueType;
import IR.llvmType.LlvmStructType;

public class GlobalOperand extends BasicReg{
    public String name;
    public String value;
    public int alignSize;

    public GlobalOperand(LlvmFirstClassType type,String name,String value,String typeName,Operand valueReg) {
        super(new LlvmPointerType(type),typeName,valueReg);
        this.name = name;
        this.value = value;//String 的value为 string const的一串编号
        if (type instanceof LlvmPointerType)alignSize = 8;
        else alignSize = 4;
    }

    public String getTypeName(){
        return typeName;
    }



    @Override
    public String toString() {
            return "@" + name;
    }

    public String toPrintString(){
        //if (!isString){
            return "@" + name +" = global " + ((LlvmPointerType)(type)).pointeeType.toString() + " " + value +", align " + alignSize;//     i32 5, align 4

    }

}
