package ASM.AsmInst;

import ASM.AsmBlock;
import ASM.AsmOperand.AsmGlobalValue;
import ASM.AsmOperand.PhysicalReg;
import ASM.AsmOperand.VirtualReg;

public class LaInst extends asmInst{
    public AsmGlobalValue data;

    public LaInst(AsmBlock belongTo, AsmGlobalValue data, PhysicalReg rd) {
        super(belongTo);
        this.rd = rd;
        this.data = data;
    }

    @Override
    public String toString() {
        return "la " + rd + ", " + data.name;
    }
}
