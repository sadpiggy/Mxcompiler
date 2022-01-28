package ASM.AsmInst;

import ASM.AsmBlock;
import ASM.AsmOperand.Imm;
import ASM.AsmOperand.IntegerImm;
import ASM.AsmOperand.PhysicalReg;
import ASM.AsmOperand.VirtualReg;

public class StoreInst extends asmInst{
    public enum StoreTypeOp{
        sb,sw
    }
    private PhysicalReg t1 = new PhysicalReg("t1","cnm");
    private PhysicalReg t2 = new PhysicalReg("t2","cnm");

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
        if (isDead)return "";
        PhysicalReg rs1_mid = rs1;
        PhysicalReg rs2_mid = rs2;
        StringBuilder stringBuilder = new StringBuilder();
        if (rs2.isAddress){
            stringBuilder.append("\t" + "lw	"+ t1 + ", " + rs2.toString() + "(s0)\n");
            rs2 = t1;
        }
        if (offset.isValidImm()) {
            stringBuilder.append("\t" + op + " " + rs2 + ", " + offset + "(" + rs1 + ")");
        }else {
            stringBuilder.append( "\tli " + t2 + ", " + offset + "\n");
            stringBuilder.append("\t" + "sub" + " " + t2 + ", " + rs1 + ", " + t2);
            stringBuilder.append("\n\t" + op + " " + rs2 + ", " + new IntegerImm(0) + "(" + t2 + ")");
        }
        rs1 = rs1_mid;
        rs2 = rs2_mid;
        return stringBuilder.toString();
    }
}
