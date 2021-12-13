package IR.llvmType;

public abstract class LlvmSingleValueType extends LlvmFirstClassType{
    @Override abstract  public String toString();

    @Override abstract public int getSize() ;
}
