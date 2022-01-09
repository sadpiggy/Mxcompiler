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
        StringBuilder stringBuilder = new StringBuilder();
        if (rs1.isAddress){
            stringBuilder.append("lw	"+ t1 + ", " + rs1.toString() + "(s0)\n");
            rs1 = t1;
        }
        if (rs2.isAddress){
            stringBuilder.append("lw	"+ t2 + ", " + rs1.toString() + "(s0)\n");
            rs2 = t2;
        }
        if (rd.isAddress){
            stringBuilder.append(op + " " + t0 + ", " + rs1 + ", " + rs2);
            stringBuilder.append("sw " + t0 + ", " + rd.toString() + "(s0)");
            return stringBuilder.toString();
        }
        return op + " " + rd + ", " + rs1 + ", " + rs2;
    }
}
