package ASM.AsmInst;

import ASM.AsmBlock;
import ASM.AsmOperand.PhysicalReg;
import ASM.AsmOperand.VirtualReg;

public class BrInst extends asmInst{
    public enum BrTypeOp {
        beqz, bnez, blez, bgez, bltz, bgtz
    }
    private PhysicalReg t0 = new PhysicalReg("t0","cnm");

    public BrTypeOp op;
    public String target;

    public BrInst(AsmBlock belongTo, BrTypeOp op, PhysicalReg rs1, String target) {
        super(belongTo);
        this.op = op;
        this.rs1 = rs1;
        this.target = target;
    }

    @Override
    public String toString() {
        if (rs1.isAddress){
            StringBuilder stringBuilder = new StringBuilder("lw	"+ t0 + ", " + rs1.toString() + "(s0)\n");
            stringBuilder.append( op + " " + t0 + ", "  + target);
            return stringBuilder.toString();
        }
        return op + " " + rs1 + ", "  + target;
    }
}
