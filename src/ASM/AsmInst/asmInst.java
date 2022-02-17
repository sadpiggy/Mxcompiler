package ASM.AsmInst;

import ASM.AsmBlock;
import ASM.AsmOperand.PhysicalReg;
import ASM.AsmOperand.VirtualReg;

//先写一个最简单的不带寄存器分配的
public abstract class asmInst {
    public int name;
    public PhysicalReg rd,rs1,rs2;
    public asmInst nextInst = null;
    //public boolean isDead = false;
    public String specialTag = "";

    public AsmBlock belongTo;

    public asmInst(AsmBlock belongTo){
        this.belongTo = belongTo;
    }

    public String getRdName(){
        if (rd ==null)return "null";
        return rd.toString();
    }

    public String getRs1Name(){
        if (rs1 ==null)return "null";
        return rs1.toString();
    }

    public String getRs2Name(){
        if (rs2 ==null)return "null";
        return rs2.toString();
    }

    @Override public abstract String toString();
}
