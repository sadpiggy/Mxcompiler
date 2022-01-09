package IR.llvmType;

import IR.operand.Operand;

import java.util.ArrayList;
import java.util.Objects;

public class LlvmStructType extends LlvmAggregateType{
    public ArrayList<LlvmSingleValueType>members = new ArrayList<>();
    public ArrayList<String>memberNames = new ArrayList<>();
    public ArrayList<Integer>memberSize = new ArrayList<>();
    public String structName;
    public int size = 0;

    public LlvmStructType(String structName){
        this.structName = structName;
    }

    public void addMember(String memberName,LlvmSingleValueType member){
        members.add(member);
        memberNames.add(memberName);
//        memberSize.add(member.getSize());
//        size+=member.getSize();
        memberSize.add(4);
        size+=4;
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

    @Override
    public int getAlignSize() {
        return 0;
    }

    public int getIndex(String memberName){
        for(int i=0;i<memberNames.size();i++){
            if (Objects.equals(memberName, memberNames.get(i)))return i;
        }
       // System.out.println("llStruct 50");
        return 0;
    }

    public int getIndexAlign(int index){
        int indexAlign = 0;
        for (int i=0;i<index;i++){
            indexAlign+=memberSize.get(i);
        }
        //System.out.println(indexAlign);
        return indexAlign;
    }

    public boolean containMember(String memberName){
        for (String name : memberNames) {
            if (Objects.equals(memberName, name)) return true;
        }
        return false;
    }

    public LlvmSingleValueType getMemberType(String memberName){
        for(int i=0;i<memberNames.size();i++){
            if (Objects.equals(memberName, memberNames.get(i)))return members.get(i);
        }
        return null;
        //System.out.println("llStruct 50");
        //return 0;
    }
}
