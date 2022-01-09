package ASM.AsmInst;

import ASM.AsmBlock;
import ASM.AsmOperand.Imm;
import ASM.AsmOperand.PhysicalReg;
import ASM.AsmOperand.VirtualReg;

public class StoreInst extends asmInst{
    public enum StoreTypeOp{
        sb,sw
    }
    private PhysicalReg t1 = new PhysicalReg("t1","cnm");

    public Imm offset;
    StoreTypeOp op;

    public StoreInst(AsmBlock belongTo, StoreTypeOp op, Imm offset, PhysicalReg rs2, PhysicalReg rs1) {
        super(belongTo);
        this.op = op;
        this.offset = offset;
        this.rs2 = rs2;
        this.rs1 = rs1;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (rs2.isAddress){
            stringBuilder.append("lw	"+ t1 + ", " + rs2.toString() + "(s0)\n");
            rs2 = t1;
        }
        return op + " " + rs2 + ", " + offset + "(" + rs1 + ")";
    }
}
