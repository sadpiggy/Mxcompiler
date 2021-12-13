package IR.inst;

import IR.IrBlock;
import IR.operand.Operand;
//just do it,既然我大概知道方向，就勇敢的去尝试，失败又何妨？
//脑袋掉了碗大个疤，代码错了不过重构
public class BitcastInst extends Inst{
    public Operand value;

    public BitcastInst(Operand destReg, IrBlock irBlock,Operand value) {
        super(destReg, irBlock);
        this.value = value;
    }

    @Override
    public String toString() {
        return destReg.toString() + " = bitcast " + value.type.toString() + " " + value.toString() + " to " + destReg.type.toString();
    }
}
