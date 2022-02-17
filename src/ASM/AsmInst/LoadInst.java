package ASM.AsmInst;

import ASM.AsmBlock;
import ASM.AsmOperand.Imm;
import ASM.AsmOperand.IntegerImm;
import ASM.AsmOperand.PhysicalReg;
import ASM.AsmOperand.VirtualReg;

public class LoadInst extends asmInst{
    public enum LoadTypeOp {
        lbu, lw
    }
    private PhysicalReg t0 = new PhysicalReg("t0","cnm");
    private PhysicalReg t1 = new PhysicalReg("t1","cnm");
    private PhysicalReg t2 = new PhysicalReg("t2","cnm");

    public Imm imm;
    public LoadTypeOp op;

    public LoadInst(AsmBlock belongTo, LoadTypeOp op, PhysicalReg rd, PhysicalReg rs1, Imm imm) {
        super(belongTo);
        this.op = op;
        this.rd = rd;
        this.rs1 = rs1;
        this.imm = imm;
    }

    @Override
    public String toString() {

        PhysicalReg rs1_mid = rs1;
        PhysicalReg rs2_mid = rs2;
        //if (isDead)return "";
        StringBuilder stringBuilder = new StringBuilder();
        if (rs1.isAddress){
            stringBuilder.append("\t" + "lw	"+ t1 + ", " + rs1.toString() + "(s0)\n");
            rs1 = t1;
        }
        if (rd.isAddress){
            stringBuilder.append("\t" + op + " " + t0 + ", " + imm + "(" + rs1 + ")\n");
            stringBuilder.append("\t" + "sw " + t0 + ", " + rd.toString() + "(s0)");
            rs1 = rs1_mid;
            return stringBuilder.toString();
        }
        if (imm.isValidImm()) {
            stringBuilder.append("\t" + op + " " + rd + ", " + imm + "(" + rs1 + ")");
            rs1 = rs1_mid;
            return stringBuilder.toString();
        }else {
            stringBuilder.append( "\tli " + t2 + ", " + imm + "\n");
            stringBuilder.append("\t" + "add" + " " + t2 + ", " + rs1 + ", " + t2);
            stringBuilder.append("\n\t" + op + " " + rd + ", " + new IntegerImm(0) + "(" + t2 + ")");
            rs1 = rs1_mid;
            return stringBuilder.toString();
        }
    }
}
