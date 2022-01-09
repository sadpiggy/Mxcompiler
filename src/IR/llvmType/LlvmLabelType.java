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

    public String getName(){
        return ".L"+index;
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public int getAlignSize() {
        return 0;
    }
}
