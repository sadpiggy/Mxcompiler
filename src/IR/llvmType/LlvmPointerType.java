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

    @Override
    public int getSize() {
        return 4;
    }

    @Override
    public int getAlignSize() {
        return 4;
    }
}
