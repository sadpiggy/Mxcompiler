package ASM.AsmOperand;

public class liveValue {
    public int value;
    public boolean notChange = false;
    public liveValue oldLive = null;
    public liveValue(int value){
        this.value = value;
    }
}
