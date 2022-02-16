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

    private PhysicalReg t0 = new PhysicalReg("t0","cnm");
    private PhysicalReg t1 = new PhysicalReg("t1","cnm");

    @Override
    public String toString() {
        if (rd.isAddress){
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\tli " + t1 + ", " + imm+"\n");//sw t0, -152(s0)
            stringBuilder.append("\tsw t1, " + rd+"(s0)");
            return stringBuilder.toString();
        }
        return "\tli " + rd + ", " + imm;
    }
}
