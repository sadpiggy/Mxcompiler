package IR.operand;

import IR.llvmType.LlvmFirstClassType;
import IR.llvmType.LlvmPointerType;
import IR.llvmType.LlvmSingleValueType;

public class GlobalOperand extends Operand{
    public String name;
    public String value;
    boolean isConst;
    boolean isString;

    public GlobalOperand(LlvmFirstClassType type,String name,String value,boolean isConst,boolean isString) {
        super(new LlvmPointerType(type));
        this.name = name;
        this.value = value;
        this.isConst = isConst;
        this.isString = isString;
    }


    public String getValue(){
        return value.replace("\\", "\\5C")
                .replace("\n", "\\0A")
                .replace("\"", "\\22")
                .replace("\t", "\\09");
    }

    @Override
    public String toString() {
        return "@" + name;
    }
}
