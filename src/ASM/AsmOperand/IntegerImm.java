package ASM.AsmOperand;

public class IntegerImm extends Imm {
    public int value;
    public IntegerImm(int value){
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
