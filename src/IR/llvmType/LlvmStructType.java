package IR.llvmType;

import java.util.ArrayList;

public class LlvmStructType extends LlvmAggregateType{
    public ArrayList<LlvmSingleValueType>members;
    public String structName;
    public int size;

    public LlvmStructType(String structName){
        this.structName = structName;
    }

    public void addMember(LlvmSingleValueType member){
        members.add(member);
        size+=member.getSize();
    }

    @Override
    public String toString() {
        return "%struct." + structName;
    }

    public String toPrintString(){
        StringBuilder stringBuilder = new StringBuilder(this.toString() + " = type {");
        if (!members.isEmpty()) {
            for (int i=0;i<=members.size()-2;i++){
                stringBuilder.append(" ").append(members.get(i).toString()).append(",");
            }
            stringBuilder.append(" ").append(members.get(members.size()-1).toString()).append(" ");
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    @Override
    public int getSize() {
        return size;
    }
}
