package ASM.AsmOperand;

import java.util.LinkedList;

public class PhysicalReg extends AsmReg{

    public String labelName;
    public String phyType;
    public boolean isVirtual;
    public boolean isAddress;
    public int offset;
    public int liveStart;
    public int liveEnd;
    public LinkedList<PhysicalReg>conflictRegs;

    public static String[] PhyRegNames = {
            "zero", "ra", "sp", "gp", "tp",
            "t0", "t1", "t2", "s0", "s1",
            "a0", "a1", "a2", "a3", "a4", "a5", "a6", "a7",
            "s2", "s3", "s4", "s5", "s6", "s7", "s8", "s9", "s10", "s11",
            "t3", "t4", "t5", "t6"
    };

    public static String[] callerSavePhyRegNames = {
            "ra", "t0", "t1", "t2",
            "a0", "a1", "a2", "a3", "a4", "a5", "a6", "a7",
            "t3", "t4", "t5", "t6"
    };

    public static String[] calleeSavePhyRegNames = {
            "s0", "s1", "s2", "s3", "s4", "s5", "s6", "s7", "s8", "s9", "s10", "s11"
    };

//    public static String[] allocatablePhyRegNames = {
//            "a0", "a1", "a2", "a3", "a4", "a5", "a6", "a7",
//            "s0", "s1", "s2", "s3", "s4", "s5", "s6", "s7", "s8", "s9", "s10", "s11",
//            "t0", "t1", "t2", "t3", "t4", "t5", "t6",
//            "ra"
//    };

    public static String[] allocatablePhyRegNames = {
            //"a0", "a1", "a2", "a3", "a4", "a5", "a6", "a7",
            "s1", "s2", "s3", "s4", "s5", "s6", "s7", "s8", "s9", "s10", "s11",
             "t3", "t4", "t5", "t6",
    };



    public PhysicalReg(String phyType,String labelName){//physical
        this.phyType = phyType;
        isVirtual = false;
        isAddress = false;
        offset = 0;
        this.labelName = labelName;
        conflictRegs = new LinkedList<>();
    }

    public PhysicalReg(String labelName,int offset){//address
        this.phyType = "address";
        this.offset = offset;
        this.isAddress = true;
        this.isVirtual = false;
        this.labelName = labelName;
        conflictRegs = new LinkedList<>();
    }

    public PhysicalReg(String labelName){//virtual
        this.isVirtual = true;
        this.isAddress = false;
        this.phyType = "virtual";
        this.labelName = labelName;
        offset = 0;
        conflictRegs = new LinkedList<>();
    }

    public void addConflict(PhysicalReg physicalReg){
        conflictRegs.add(physicalReg);
    }

    public void deleteConflict(PhysicalReg physicalReg){
        conflictRegs.remove(physicalReg);
    }

    @Override
    public String toString() {
        if (isVirtual){
            return "virtual" + labelName;
        }else if (isAddress){
            return String.valueOf(offset);
        }
        return phyType;
    }
}