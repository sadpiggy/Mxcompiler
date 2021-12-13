package IR.llvmType;

public abstract class LlvmAggregateType extends LlvmFirstClassType{
    @Override abstract public String toString();

    @Override abstract public int getSize();
}
