package IR.llvmType;

public class LlvmLabelType extends LlvmFirstClassType{
    public int index;

    public LlvmLabelType(int index){
        this.index = index;
    }

    @Override
    public String toString() {
        return "%" + index;
    }

    @Override
    public int getSize() {
        return 0;
    }
}
