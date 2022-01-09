package IR.llvmType;

public class LlvmVoidType extends LlvmSingleValueType{
    public LlvmVoidType(){

    }

    @Override
    public String toString() {
        return "void";
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
