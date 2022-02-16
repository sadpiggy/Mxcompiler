package ASM.AsmInst;

import ASM.AsmBlock;
import ASM.AsmOperand.PhysicalReg;
import ASM.AsmOperand.VirtualReg;

public class SzInst extends asmInst{
    public enum SzTypeOp {
        seqz, snez
    }

    private PhysicalReg t0 = new PhysicalReg("t0","cnm");
    private PhysicalReg t1 = new PhysicalReg("t1","cnm");

    public SzTypeOp op;

    public SzInst(AsmBlock belongTo, SzTypeOp op, PhysicalReg rd, PhysicalReg rs1) {
        super(belongTo);
        this.op = op;
        this.rd = rd;
        this.rs1 = rs1;
    }

    @Override
    public String toString() {
        PhysicalReg rs1_mid = rs1;
        PhysicalReg rs2_mid = rs2;
        StringBuilder stringBuilder = new StringBuilder();
        if (rs1.isAddress){
            stringBuilder.append("\t"+"lw	"+ t1 + ", " + rs1.toString() + "(s0)\n");
            rs1 = t1;
        }
        if (rd.isAddress){
            stringBuilder.append("\t"+op + " " + t0 + ", " + rs1+"\n");
            stringBuilder.append("\t"+"sw " + t0 + ", " + rd.toString() + "(s0)");
            rs1 = rs1_mid;
            return stringBuilder.toString();
        }
        stringBuilder.append(op + " " + rd + ", " + rs1);
        rs1 = rs1_mid;
        return stringBuilder.toString();
    }
}
