package IR.operand;

import IR.llvmType.LlvmPointerType;
import IR.llvmType.LlvmSingleValueType;

public class Register extends Operand{
    public String name;

    public Register(LlvmSingleValueType type,String name) {
       //super(new LlvmPointerType(type));
        super(type);
        this.name = name;
    }

    @Override
    public String toString() {
       // StringBuilder stringBuilder = new StringBuilder(type.toString());
        //stringBuilder.append(" ");
        //stringBuilder.append("%" + name);
        //return stringBuilder.toString();
        //if(name==null)return "";
         return "%" + name;
    }
}
