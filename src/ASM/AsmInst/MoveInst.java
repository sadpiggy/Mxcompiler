package ASM.AsmInst;

import ASM.AsmBlock;
import ASM.AsmOperand.PhysicalReg;
import ASM.AsmOperand.VirtualReg;

public class MoveInst extends asmInst{
    public MoveInst(AsmBlock belongTo, PhysicalReg rd, PhysicalReg rs1) {
        super(belongTo);
        this.rd = rd;
        this.rs1 = rs1;
    }
    private PhysicalReg t0 = new PhysicalReg("t0","cnm");
    private PhysicalReg t1 = new PhysicalReg("t1","cnm");

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (rs1.isAddress){
            stringBuilder.append("\t" + "lw	"+ t1 + ", " + rs1.toString() + "(s0)\n");
            rs1 = t1;
        }
        if (rd.isAddress){
            stringBuilder.append("\t" + "mv " + t0 + ", " + rs1+"\n");
            stringBuilder.append("\t" + "sw " + t0 + ", " + rd.toString() + "(s0)");
            return stringBuilder.toString();
        }
        return stringBuilder.append("\t" + "mv " + rd + ", " + rs1).toString();
    }
}
