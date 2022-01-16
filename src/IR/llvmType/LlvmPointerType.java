package IR.llvmType;

public class LlvmPointerType extends LlvmSingleValueType{
    public LlvmFirstClassType pointeeType;

    public LlvmPointerType(LlvmFirstClassType pointeeType){
        this.pointeeType = pointeeType;
    }

    @Override
    public String toString() {
        return pointeeType.toString() + "*";
    }


    private int alignSize = 8;

    @Override
    public int getSize() {
        return alignSize;
    }

    @Override
    public int getAlignSize() {
        return alignSize;
    }
}
