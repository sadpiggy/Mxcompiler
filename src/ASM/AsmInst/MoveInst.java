package ASM.AsmInst;

import ASM.AsmBlock;
import ASM.AsmOperand.PhysicalReg;
import ASM.AsmOperand.VirtualReg;

import java.io.BufferedOutputStream;
import java.io.PrintStream;

public class MoveInst extends asmInst{
    public MoveInst(AsmBlock belongTo, PhysicalReg rd, PhysicalReg rs1) {

        super(belongTo);
        this.rd = rd;
        this.rs1 = rs1;
        this.isSp = false;
    }
    public MoveInst(AsmBlock belongTo, PhysicalReg rd, PhysicalReg rs1,boolean isSp) {
        super(belongTo);
        this.rd = rd;
        this.rs1 = rs1;
        this.isSp = true;
    }
   // boolean hadPrint = false;

    //public boolean isDeadInst = false;
    public boolean isSp;

    private PhysicalReg t0 = new PhysicalReg("t0","cnm");
    private PhysicalReg t1 = new PhysicalReg("t1","cnm");

    @Override
    public String toString() {
        boolean debug = true;

        String s0 = "(s0)";
        if (isSp) s0 = "(sp)";
        PhysicalReg rs1_mid = rs1;
        if (isDead)return "";
        StringBuilder stringBuilder = new StringBuilder();
        if (rs1.isAddress){
            if (!rd.isAddress){
                stringBuilder.append("\t" + "lw	"+ rd + ", " + rs1.toString() + s0);
                return stringBuilder.toString();
            }
            stringBuilder.append("\t" + "lw	"+ t1 + ", " + rs1.toString() + s0 + "\n");
            rs1 = t1;
        }
        if (rd.isAddress){
            stringBuilder.append("\t" + "sw " + rs1 + ", " + rd.toString() + s0);
            rs1 = rs1_mid;
            return stringBuilder.toString();
        }
        stringBuilder.append("\t" + "mv " + rd + ", " + rs1);
        rs1 = rs1_mid;
        return stringBuilder.toString();
    }
}
