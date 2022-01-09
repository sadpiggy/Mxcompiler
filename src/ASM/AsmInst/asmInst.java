package ASM.AsmInst;

import ASM.AsmBlock;
import ASM.AsmOperand.PhysicalReg;
import ASM.AsmOperand.VirtualReg;

//先写一个最简单的不带寄存器分配的
public abstract class asmInst {
    public int name;
    public PhysicalReg rd,rs1,rs2;

    public AsmBlock belongTo;

    public asmInst(AsmBlock belongTo){
        this.belongTo = belongTo;
    }

    @Override public abstract String toString();
}
