package ASM.AsmInst;

import ASM.AsmBlock;
import ASM.AsmOperand.Imm;
import ASM.AsmOperand.PhysicalReg;
import ASM.AsmOperand.VirtualReg;

public class LiInst extends asmInst{
    public Imm imm;

    public LiInst(AsmBlock belongTo, PhysicalReg rd, Imm imm) {
        super(belongTo);
        this.rd = rd;
        this.imm = imm;
    }

    @Override
    public String toString() {
        return "li " + rd + ", " + imm;
    }
}
