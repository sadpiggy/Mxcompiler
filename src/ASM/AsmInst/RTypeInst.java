package ASM.AsmInst;

import ASM.AsmBlock;
import ASM.AsmOperand.Imm;
import ASM.AsmOperand.PhysicalReg;
import ASM.AsmOperand.VirtualReg;

public class RTypeInst extends asmInst{
    public enum RTypeOp {
        add, sub, mul, div, rem, sll, sra, xor, slt, sltu, or, and
    }
    private PhysicalReg t0 = new PhysicalReg("t0","cnm");
    private PhysicalReg t1 = new PhysicalReg("t1","cnm");
    private PhysicalReg t2 = new PhysicalReg("t2","cnm");
    public RTypeOp op;

    public RTypeInst(AsmBlock belongTo, RTypeOp op, PhysicalReg rd, PhysicalReg rs1, PhysicalReg rs2) {
        super(belongTo);
        this.op = op;
        this.rd = rd;
        this.rs1 = rs1;
        this.rs2 = rs2;
    }

    @Override
    public String toString() {
        PhysicalReg rs1_mid = rs1;
        PhysicalReg rs2_mid = rs2;
        StringBuilder stringBuilder = new StringBuilder();
        if (rs1.isAddress){
            stringBuilder.append("\t" + "lw	"+ t1 + ", " + rs1.toString() + "(s0)\n");
            rs1 = t1;
        }
        if (rs2.isAddress){
            stringBuilder.append("\t" + "lw	"+ t2 + ", " + rs2.toString() + "(s0)\n");
            rs2 = t2;
        }
        if (rd.isAddress){
            stringBuilder.append("\t" + op + " " + t0 + ", " + rs1 + ", " + rs2+"\n");
            stringBuilder.append("\t" + "sw " + t0 + ", " + rd.toString() + "(s0)");
            rs1 = rs1_mid;
            rs2 = rs2_mid;
            return stringBuilder.toString();
        }
        stringBuilder.append("\t" + op + " " + rd + ", " + rs1 + ", " + rs2);
        rs1 = rs1_mid;
        rs2 = rs2_mid;
        return stringBuilder.toString();
    }
}
