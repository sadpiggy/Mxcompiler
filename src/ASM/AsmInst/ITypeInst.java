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
    private PhysicalReg t2 = new PhysicalReg("t2","cnm");

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
        PhysicalReg rs1_mid = rs1;
        PhysicalReg rs2_mid = rs2;
        StringBuilder stringBuilder = new StringBuilder();

        if (!imm.isValidImm()){//只会出现在sw (100)s0,addi sp 100
            if (rs1.isAddress){
                stringBuilder.append("\t" + "lw	"+ t1 + ", " + rs1.toString() + "(s0)\n");
                rs1 = t1;
            }
            stringBuilder.append( "\tli " + t2 + ", " + imm + "\n");
            if (rd.isAddress){
                stringBuilder.append("\t" + "add" + " " + t0 + ", " + rs1 + ", " + t2 + "\n");
                stringBuilder.append("\t" + "sw " + t0 + ", " + rd.toString() + "(s0)" );
                rs1 = rs1_mid;
                return stringBuilder.toString();
            }else {
                stringBuilder.append("\t" + "add" + " " + rd + ", " + rs1 + ", " + t2);
                rs1 = rs1_mid;
                return stringBuilder.toString();
            }
        }

        if (rs1.isAddress){
           stringBuilder.append("\t" + "lw	"+ t1 + ", " + rs1.toString() + "(s0)\n");
           rs1 = t1;
        }
        if (rd.isAddress){
            stringBuilder.append("\t" + op + " " + t0 + ", " + rs1 + ", " + imm + "\n");
            stringBuilder.append("\t" + "sw " + t0 + ", " + rd.toString() + "(s0)" );
            rs1 = rs1_mid;
            return stringBuilder.toString();
        }
        stringBuilder.append("\t" + op + " " + rd + ", " + rs1 + ", " + imm);
        rs1 = rs1_mid;
        return stringBuilder.toString();
    }
}
