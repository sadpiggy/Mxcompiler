package IR.operand;

import IR.llvmType.LlvmSingleValueType;

public class Register extends Operand{
    public String name;

    public Register(LlvmSingleValueType type,String name) {
        super(type);
        this.name = name;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(type.toString());
        stringBuilder.append(" ");
        stringBuilder.append("%" + name);
        return stringBuilder.toString();
        //if(name==null)return "";
        //else return "%" + name;
    }
}
