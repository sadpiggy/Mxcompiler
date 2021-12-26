package IR.llvmType;

public class LlvmLabelType extends LlvmFirstClassType{
    public String index;

    public LlvmLabelType(String index){
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
