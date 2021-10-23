package AST;

public interface AstVisitor {
    void visit(ArrayTypeNode node);

    void visit(AssignExprNode node);

    void visit(BinaryExprNode node);

    void visit(BlockStmtNode node);

    void visit(BoolContainNode node);

    void visit(BreakStmtNode node);

    void visit(ClassDefNode node);

    void visit(ContainExprNode node);

    void visit(ContinueStmtNode node);

    void visit(EmptyStmtNode node);

    void visit(ExprNode node);

    void visit(ForStmtNode node);

    void visit(FuncCallExprNode node);

    void visit(FuncDefNode node);

    void visit(IdExprNode node);

    void visit(IfStmtNode node);

    void visit(IntContainNode node);

    void visit(MemberExprNode node);

    void visit(MethodExprNode node);

    void visit(NameTypeNode node);

    void visit(NewExprNode node);

    void visit(NullContainNode node);

    void visit(PrefixExprNode node);

    void visit(ProgramNode node);

    void visit(ProgramSectionNode node);

    void visit(PureExprStmtNode node);

    void visit(ReturnStmtNode node);

    void visit(StmtNode node);

    void visit(StringContainNode node);

    void visit(SubScriptExprNode node);

    void visit(SuffixExprNode node);

    void visit(ThisExprNode node);

    void visit(TypeNode node);

    void visit(VarDefStmtNode node);

    void visit(VarDefNode node);

    void visit(VarDefListNode node);

    void visit(VoidTypeNode node);

    void visit(WhileStmtNode node);

    void visit(LambdaExprNode node);
}
