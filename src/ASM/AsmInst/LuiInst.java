package ASM.AsmInst;

import ASM.AsmBlock;
import ASM.AsmOperand.Imm;
import ASM.AsmOperand.PhysicalReg;
import ASM.AsmOperand.VirtualReg;

public class LuiInst extends asmInst{
    public Imm imm;
    private PhysicalReg t0 = new PhysicalReg("t0","cnm");
    private PhysicalReg t1 = new PhysicalReg("t1","cnm");

    public LuiInst(AsmBlock belongTo, PhysicalReg rd, Imm imm) {
        super(belongTo);
        this.rd = rd;
        this.imm = imm;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (rd.isAddress){
            stringBuilder.append("lui " + t0 + ", " + imm);
            stringBuilder.append("sw " + t0 + ", " + rd.toString() + "(s0)");
            return stringBuilder.toString();
        }
        return "lui " + rd + ", " + imm;
    }
}
