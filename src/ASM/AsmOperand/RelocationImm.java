package ASM.AsmOperand;

public class RelocationImm extends Imm {
    public enum Type{
        hi,lo
    }

    public Type type;
    public String dataName;
    //public PhysicalReg physicalReg;

    public RelocationImm(Type type,String dataName){
        this.type = type;
        this.dataName = dataName;
    }

//    public RelocationImm(Type type,String dataName,PhysicalReg physicalReg){
//        this.type = type;
//        this.dataName = dataName;
//        this.physicalReg = physicalReg;
//    }

    @Override
    public String toString() {
        //if (physicalReg!=null&&type==Type.lo)return "%" + type + "(" + dataName + ")(" + physicalReg.toString() + ")";
        return "%" + type + "(" + dataName + ")";
    }

    @Override
    public boolean isValidImm() {
        return true;
    }
}
