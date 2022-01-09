package IR.llvmType;

public abstract class LlvmFirstClassType extends LlvmType{

    @Override abstract public String toString() ;

    abstract public int getSize();

    abstract public int getAlignSize();
}
