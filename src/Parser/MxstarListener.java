// Generated from D:/LearningCS/java_project/Mxcompiler/src/Parser\Mxstar.g4 by ANTLR 4.9.1
package Parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link MxstarParser}.
 */
public interface MxstarListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link MxstarParser#contain}.
	 * @param ctx the parse tree
	 */
	void enterContain(MxstarParser.ContainContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#contain}.
	 * @param ctx the parse tree
	 */
	void exitContain(MxstarParser.ContainContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(MxstarParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(MxstarParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#programSection}.
	 * @param ctx the parse tree
	 */
	void enterProgramSection(MxstarParser.ProgramSectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#programSection}.
	 * @param ctx the parse tree
	 */
	void exitProgramSection(MxstarParser.ProgramSectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#classDef}.
	 * @param ctx the parse tree
	 */
	void enterClassDef(MxstarParser.ClassDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#classDef}.
	 * @param ctx the parse tree
	 */
	void exitClassDef(MxstarParser.ClassDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#funcDef}.
	 * @param ctx the parse tree
	 */
	void enterFuncDef(MxstarParser.FuncDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#funcDef}.
	 * @param ctx the parse tree
	 */
	void exitFuncDef(MxstarParser.FuncDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#varDef}.
	 * @param ctx the parse tree
	 */
	void enterVarDef(MxstarParser.VarDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#varDef}.
	 * @param ctx the parse tree
	 */
	void exitVarDef(MxstarParser.VarDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#classCreatorFuncDef}.
	 * @param ctx the parse tree
	 */
	void enterClassCreatorFuncDef(MxstarParser.ClassCreatorFuncDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#classCreatorFuncDef}.
	 * @param ctx the parse tree
	 */
	void exitClassCreatorFuncDef(MxstarParser.ClassCreatorFuncDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(MxstarParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(MxstarParser.TypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#basicType}.
	 * @param ctx the parse tree
	 */
	void enterBasicType(MxstarParser.BasicTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#basicType}.
	 * @param ctx the parse tree
	 */
	void exitBasicType(MxstarParser.BasicTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#nameType}.
	 * @param ctx the parse tree
	 */
	void enterNameType(MxstarParser.NameTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#nameType}.
	 * @param ctx the parse tree
	 */
	void exitNameType(MxstarParser.NameTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#arrayType}.
	 * @param ctx the parse tree
	 */
	void enterArrayType(MxstarParser.ArrayTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#arrayType}.
	 * @param ctx the parse tree
	 */
	void exitArrayType(MxstarParser.ArrayTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#varDefSection}.
	 * @param ctx the parse tree
	 */
	void enterVarDefSection(MxstarParser.VarDefSectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#varDefSection}.
	 * @param ctx the parse tree
	 */
	void exitVarDefSection(MxstarParser.VarDefSectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#paramList}.
	 * @param ctx the parse tree
	 */
	void enterParamList(MxstarParser.ParamListContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#paramList}.
	 * @param ctx the parse tree
	 */
	void exitParamList(MxstarParser.ParamListContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#param}.
	 * @param ctx the parse tree
	 */
	void enterParam(MxstarParser.ParamContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#param}.
	 * @param ctx the parse tree
	 */
	void exitParam(MxstarParser.ParamContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#suite}.
	 * @param ctx the parse tree
	 */
	void enterSuite(MxstarParser.SuiteContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#suite}.
	 * @param ctx the parse tree
	 */
	void exitSuite(MxstarParser.SuiteContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#init}.
	 * @param ctx the parse tree
	 */
	void enterInit(MxstarParser.InitContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#init}.
	 * @param ctx the parse tree
	 */
	void exitInit(MxstarParser.InitContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#condition}.
	 * @param ctx the parse tree
	 */
	void enterCondition(MxstarParser.ConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#condition}.
	 * @param ctx the parse tree
	 */
	void exitCondition(MxstarParser.ConditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#change}.
	 * @param ctx the parse tree
	 */
	void enterChange(MxstarParser.ChangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#change}.
	 * @param ctx the parse tree
	 */
	void exitChange(MxstarParser.ChangeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code block}
	 * labeled alternative in {@link MxstarParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterBlock(MxstarParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by the {@code block}
	 * labeled alternative in {@link MxstarParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitBlock(MxstarParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by the {@code vardefStmt}
	 * labeled alternative in {@link MxstarParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterVardefStmt(MxstarParser.VardefStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code vardefStmt}
	 * labeled alternative in {@link MxstarParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitVardefStmt(MxstarParser.VardefStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ifStmt}
	 * labeled alternative in {@link MxstarParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterIfStmt(MxstarParser.IfStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ifStmt}
	 * labeled alternative in {@link MxstarParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitIfStmt(MxstarParser.IfStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code forStmt}
	 * labeled alternative in {@link MxstarParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterForStmt(MxstarParser.ForStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code forStmt}
	 * labeled alternative in {@link MxstarParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitForStmt(MxstarParser.ForStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code whileStmt}
	 * labeled alternative in {@link MxstarParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterWhileStmt(MxstarParser.WhileStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code whileStmt}
	 * labeled alternative in {@link MxstarParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitWhileStmt(MxstarParser.WhileStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code returnStmt}
	 * labeled alternative in {@link MxstarParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterReturnStmt(MxstarParser.ReturnStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code returnStmt}
	 * labeled alternative in {@link MxstarParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitReturnStmt(MxstarParser.ReturnStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code breakStmt}
	 * labeled alternative in {@link MxstarParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterBreakStmt(MxstarParser.BreakStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code breakStmt}
	 * labeled alternative in {@link MxstarParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitBreakStmt(MxstarParser.BreakStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code continueStmt}
	 * labeled alternative in {@link MxstarParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterContinueStmt(MxstarParser.ContinueStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code continueStmt}
	 * labeled alternative in {@link MxstarParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitContinueStmt(MxstarParser.ContinueStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code pureExprStmt}
	 * labeled alternative in {@link MxstarParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterPureExprStmt(MxstarParser.PureExprStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code pureExprStmt}
	 * labeled alternative in {@link MxstarParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitPureExprStmt(MxstarParser.PureExprStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code emptyStmt}
	 * labeled alternative in {@link MxstarParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterEmptyStmt(MxstarParser.EmptyStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code emptyStmt}
	 * labeled alternative in {@link MxstarParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitEmptyStmt(MxstarParser.EmptyStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void enterExpressionList(MxstarParser.ExpressionListContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void exitExpressionList(MxstarParser.ExpressionListContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#yuanzi}.
	 * @param ctx the parse tree
	 */
	void enterYuanzi(MxstarParser.YuanziContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#yuanzi}.
	 * @param ctx the parse tree
	 */
	void exitYuanzi(MxstarParser.YuanziContext ctx);
	/**
	 * Enter a parse tree produced by the {@code newExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterNewExpr(MxstarParser.NewExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code newExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitNewExpr(MxstarParser.NewExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code prefixExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterPrefixExpr(MxstarParser.PrefixExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code prefixExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitPrefixExpr(MxstarParser.PrefixExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code lambda}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterLambda(MxstarParser.LambdaContext ctx);
	/**
	 * Exit a parse tree produced by the {@code lambda}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitLambda(MxstarParser.LambdaContext ctx);
	/**
	 * Enter a parse tree produced by the {@code subscriptExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterSubscriptExpr(MxstarParser.SubscriptExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code subscriptExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitSubscriptExpr(MxstarParser.SubscriptExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code memberExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterMemberExpr(MxstarParser.MemberExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code memberExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitMemberExpr(MxstarParser.MemberExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code methodExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterMethodExpr(MxstarParser.MethodExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code methodExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitMethodExpr(MxstarParser.MethodExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code suffixExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterSuffixExpr(MxstarParser.SuffixExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code suffixExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitSuffixExpr(MxstarParser.SuffixExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code atomExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterAtomExpr(MxstarParser.AtomExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code atomExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitAtomExpr(MxstarParser.AtomExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code binaryExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterBinaryExpr(MxstarParser.BinaryExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code binaryExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitBinaryExpr(MxstarParser.BinaryExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code funcCallExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterFuncCallExpr(MxstarParser.FuncCallExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code funcCallExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitFuncCallExpr(MxstarParser.FuncCallExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code assignExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterAssignExpr(MxstarParser.AssignExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code assignExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitAssignExpr(MxstarParser.AssignExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code errorCreator}
	 * labeled alternative in {@link MxstarParser#creator}.
	 * @param ctx the parse tree
	 */
	void enterErrorCreator(MxstarParser.ErrorCreatorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code errorCreator}
	 * labeled alternative in {@link MxstarParser#creator}.
	 * @param ctx the parse tree
	 */
	void exitErrorCreator(MxstarParser.ErrorCreatorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arrayCreator}
	 * labeled alternative in {@link MxstarParser#creator}.
	 * @param ctx the parse tree
	 */
	void enterArrayCreator(MxstarParser.ArrayCreatorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arrayCreator}
	 * labeled alternative in {@link MxstarParser#creator}.
	 * @param ctx the parse tree
	 */
	void exitArrayCreator(MxstarParser.ArrayCreatorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code normalCreator}
	 * labeled alternative in {@link MxstarParser#creator}.
	 * @param ctx the parse tree
	 */
	void enterNormalCreator(MxstarParser.NormalCreatorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code normalCreator}
	 * labeled alternative in {@link MxstarParser#creator}.
	 * @param ctx the parse tree
	 */
	void exitNormalCreator(MxstarParser.NormalCreatorContext ctx);
}