package ASM;

import IR.inst.*;

public interface IrVisitor {
    void visit(AllocInst inst);

    void visit(BinaryInst inst);

    void visit(BitcastInst inst);

    void visit(BrInst inst);

    void visit(CallInst inst);

    void visit(GetElementPtrInst inst);

    void visit(IcmpInst inst);

    void visit(LoadInst inst);

    void visit(RetInst inst);

    void visit(SextInst inst);

    void visit(StoreInst inst);

    void visit(TerminalInst inst);
}
