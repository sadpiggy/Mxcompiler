package ASM.AsmInst;

import ASM.AsmBlock;
import ASM.AsmOperand.Imm;
import ASM.AsmOperand.PhysicalReg;
import ASM.AsmOperand.VirtualReg;

public class ITypeInst extends asmInst{
    public enum ITypeOp {
        addi, slti, sltiu, andi, ori, xori, slli
    }
    private PhysicalReg t0 = new PhysicalReg("t0","cnm");
    private PhysicalReg t1 = new PhysicalReg("t1","cnm");

    public Imm imm;
    public ITypeOp op;

    public ITypeInst(AsmBlock belongTo, ITypeOp op, PhysicalReg rd, PhysicalReg rs1, Imm imm) {
        super(belongTo);
        this.op = op;
        this.imm = imm;
        this.rd = rd;
        this.rs1 = rs1;
    }

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();
        if (rs1.isAddress){
           stringBuilder.append("\t" + "lw	"+ t1 + ", " + rs1.toString() + "(s0)\n");
           rs1 = t1;
        }
        if (rd.isAddress){
            stringBuilder.append("\t" + op + " " + t0 + ", " + rs1 + ", " + imm + "\n");
            stringBuilder.append("\t" + "sw " + t0 + ", " + rd.toString() + "(s0)" );
            return stringBuilder.toString();
        }
        return stringBuilder.append("\t" + op + " " + rd + ", " + rs1 + ", " + imm).toString();
    }
}
