package AST;

import Mutil.Position;
import Mutil.error.SemanticError;
import Mutil.error.SyntaxError;
import Parser.MxstarBaseVisitor;
import Parser.MxstarParser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;

public class AstBuilder extends MxstarBaseVisitor<AstNode> {
    @Override
    public AstNode visitProgram(MxstarParser.ProgramContext ctx) {
        ArrayList<ProgramSectionNode>programSections = new ArrayList<>();
        for (var it : ctx.programSection()){
            ProgramSectionNode programSectionNode =(ProgramSectionNode) visit(it);//至于这个为什么可以跑出来Node,我还是不得而知
            if(programSectionNode instanceof VarDefListNode){//如果是一堆vardefNode,就拆开
                programSections.addAll(((VarDefListNode)programSectionNode).getVarDefNodes());
            }else {
                programSections.add(programSectionNode);
            }
        }
        return new ProgramNode(new Position(ctx.getStart()),programSections);
    }

    @Override
    public AstNode visitProgramSection(MxstarParser.ProgramSectionContext ctx) {
        if(ctx.classDef()!=null){
            return (ClassDefNode)visit(ctx.classDef());
        }if(ctx.funcDef()!=null){
            return (FuncDefNode)visit(ctx.funcDef());
        }
        return (VarDefListNode)visit(ctx.varDef());//这里应该转换成什么呢？vardefnode or vardefnodelist?todo
    }

    // class pig{var;func;classCreator}  int pig(){}
    @Override
    public AstNode visitClassDef(MxstarParser.ClassDefContext ctx) {
        String identifier = ctx.Identifier().getText();
        ArrayList<VarDefNode>members = new ArrayList<>();
        ArrayList<FuncDefNode>methods = new ArrayList<>();
        FuncDefNode classConstructor = null;
        for(var it : ctx.varDef()){
            members.addAll(((VarDefListNode)visit(it)).getVarDefNodes());
        }
        boolean hasConstructor = false;
        for(var it : ctx.funcDef()){
            if(Objects.equals(it.Identifier().getText(), identifier)){
                throw new SemanticError("wrong class creator",new Position(it.getStart()));
            }else {
                methods.add((FuncDefNode) visit(it));
            }
        }

        if (ctx.classCreatorFuncDef()!=null){
            if (!Objects.equals(ctx.classCreatorFuncDef().Identifier().getText(), identifier))
                throw new SemanticError("wrong classCreator",new Position(ctx.getStart()));
            methods.add((FuncDefNode) visit(ctx.classCreatorFuncDef()));//?????todo
        }

        return new ClassDefNode(new Position(ctx.getStart()),identifier,methods,members,classConstructor);
    }


    @Override
    public AstNode visitClassCreatorFuncDef(MxstarParser.ClassCreatorFuncDefContext ctx) {
        return new FuncDefNode(
                new Position(ctx.getStart()),
                ctx.Identifier().getText(),
                new VoidTypeNode(new Position(ctx.getStart())),
                new ArrayList<>(),
                (BlockStmtNode) visit(ctx.suite())
        );
    }

    @Override
    public AstNode visitFuncDef(MxstarParser.FuncDefContext ctx) {
        TypeNode typeNode = (TypeNode) visit(ctx.type());
        String identifier = ctx.Identifier().getText();
        ArrayList<VarDefNode>paramList = new ArrayList<>();
        if(ctx.paramList()!=null){
            paramList.addAll(((VarDefListNode)visit(ctx.paramList())).getVarDefNodes());
        }
        BlockStmtNode suit = (BlockStmtNode) visit(ctx.suite());
        return new FuncDefNode(new Position(ctx.getStart()),identifier,typeNode,paramList,suit);
    }

    @Override
    public AstNode visitVarDef(MxstarParser.VarDefContext ctx) {
        ArrayList<VarDefNode>VarDefNodes = new ArrayList<>();
        for (var it : ctx.varDefSection()){
            VarDefNode varDefNode =(VarDefNode) visit(it);
            varDefNode.setTypeNode((TypeNode) visit(ctx.type()));
            VarDefNodes.add(varDefNode);
        }
        return new VarDefListNode(new Position(ctx.getStart()),VarDefNodes);
    }

    //a = expr;
    @Override
    public AstNode visitVarDefSection(MxstarParser.VarDefSectionContext ctx) {
        String identifier = ctx.Identifier().getText();
        ExprNode expr;
        if(ctx.expression()==null){
            expr = null;
        }else {
            expr = (ExprNode) visit(ctx.expression());
        }
        //type可能为空,那我要怎么处理呢？
        return new VarDefNode(new Position(ctx.getStart()),null,identifier,expr);
    }

    //int a
    @Override
    public AstNode visitParam(MxstarParser.ParamContext ctx) {
        return new VarDefNode(
               new Position(ctx.getStart()),
                (TypeNode) visit(ctx.type()),
                ctx.Identifier().getText(),
                null
        );
    }

    @Override
    public AstNode visitParamList(MxstarParser.ParamListContext ctx) {
        ArrayList<VarDefNode>paramList = new ArrayList<>();
        for (var it : ctx.param()){
            paramList.add(new VarDefNode(
                    new Position(ctx.getStart()),
                    (TypeNode) visit(it.type()),
                    it.Identifier().getText(),
                    null
            ));
        }
        return new VarDefListNode(new Position(ctx.getStart()),paramList);
    }

    @Override
    public AstNode visitType(MxstarParser.TypeContext ctx) {
        if(ctx.arrayType()!=null){
            return (ArrayTypeNode)visit(ctx.arrayType());
        }
        if(ctx.nameType()!=null){
            return (NameTypeNode)visit(ctx.nameType());
        }
        if(ctx.Void()!=null){
           // return (VoidTypeNode)visit(ctx.Void());
            return new VoidTypeNode(new Position(ctx.getStart()));
        }
        return null;
    }

    @Override
    public AstNode visitNameType(MxstarParser.NameTypeContext ctx) {
        if(ctx.basicType()!=null){
            return visit(ctx.basicType());
        }
        if(ctx.Identifier()!=null){
            return new NameTypeNode(new Position(ctx.getStart()),ctx.Identifier().getText());
        }
        return null;
    }




    @Override
    public AstNode visitBasicType(MxstarParser.BasicTypeContext ctx) {
        if(ctx.Int()!=null){
            return new NameTypeNode(new Position(ctx.getStart()),"int");
        }
        if(ctx.Bool()!=null){
            return new NameTypeNode(new Position(ctx.getStart()),"bool");
        }
        if(ctx.String()!=null){
            return new NameTypeNode(new Position(ctx.getStart()),"string");
        }
        return null;
    }

    @Override
    public AstNode visitArrayType(MxstarParser.ArrayTypeContext ctx) {
       return new ArrayTypeNode(
               new Position(ctx.getStart()),
               (NameTypeNode)visit(ctx.nameType()),
               ctx.LeftBracket().size()
       );
    }

    @Override
    public AstNode visitSuite(MxstarParser.SuiteContext ctx) {
        LinkedList<StmtNode>stmtNodes = new LinkedList<>();
        for(var it : ctx.statement()){
            stmtNodes.add((StmtNode) visit(it));
        }
        return new BlockStmtNode(new Position(ctx.getStart()),stmtNodes);
    }

    @Override
    public AstNode visitVardefStmt(MxstarParser.VardefStmtContext ctx) {
        ArrayList<VarDefNode>VarDefNodes = ((VarDefListNode)visit(ctx.varDef())).getVarDefNodes();
        return new VarDefStmtNode(new Position(ctx.getStart()),VarDefNodes);
    }

    @Override
    public AstNode visitIfStmt(MxstarParser.IfStmtContext ctx) {
        return new IfStmtNode(
                new Position(ctx.getStart()),
                (ExprNode) visit(ctx.expression()),
                (StmtNode) visit(ctx.trueStmt),
                (ctx.falseStmt==null?null:(StmtNode) visit(ctx.falseStmt))
        );
    }

    //for(;;)suit
    @Override
    public AstNode visitForStmt(MxstarParser.ForStmtContext ctx) {
        return new ForStmtNode(
                new Position(ctx.getStart()),
                (ctx.init()==null?null:(ExprNode) visit(ctx.init())),
                (ctx.condition()==null?null:(ExprNode) visit(ctx.condition())),
                (ctx.change()==null?null:(ExprNode) visit(ctx.change())),
                (StmtNode) visit(ctx.statement())
        );
    }

    @Override
    public AstNode visitWhileStmt(MxstarParser.WhileStmtContext ctx) {
        return new WhileStmtNode(
                new Position(ctx.getStart()),
                (ExprNode) visit(ctx.expression()),
                (StmtNode) visit(ctx.statement())
        );
    }

    @Override
    public AstNode visitReturnStmt(MxstarParser.ReturnStmtContext ctx) {
        return new ReturnStmtNode(
                new Position(ctx.getStart()),
                (ctx.expression()==null?null:(ExprNode) visit(ctx.expression()))
        );
    }

    @Override
    public AstNode visitBreakStmt(MxstarParser.BreakStmtContext ctx) {
        return new BreakStmtNode(new Position(ctx.getStart()));
    }

    @Override
    public AstNode visitContinueStmt(MxstarParser.ContinueStmtContext ctx) {
        return new ContinueStmtNode(new Position(ctx.getStart()));
    }

    @Override
    public AstNode visitPureExprStmt(MxstarParser.PureExprStmtContext ctx) {
        return new PureExprStmtNode(
                new Position(ctx.getStart()),
                (ctx.expression()==null?null:(ExprNode) visit(ctx.expression()))
        );
    }

    @Override
    public AstNode visitExpressionList(MxstarParser.ExpressionListContext ctx) {
        //写的啥玩意儿？
        ArrayList<ExprNode>exprList = new ArrayList<>();
        for(var it : ctx.expression()){
            exprList.add((ExprNode) visit(it));
        }
        //todo id
        return new FuncCallExprNode(
                new Position(ctx.getStart()),
                ctx.getText(),
                null,
                exprList
        );
    }

    @Override
    public AstNode visitAtomExpr(MxstarParser.AtomExprContext ctx) {
        //
        //return super.visitAtomExpr(ctx);
        return visit(ctx.yuanzi());
    }

    //expr.id
    @Override
    public AstNode visitMemberExpr(MxstarParser.MemberExprContext ctx) {
        return new MemberExprNode(
                new Position(ctx.getStart()),
                ctx.getText(),
                (ExprNode) visit(ctx.expression()),
                ctx.Identifier().getText()
        );
    }

    @Override
    public AstNode visitMethodExpr(MxstarParser.MethodExprContext ctx) {
        return new MethodExprNode(
                new Position(ctx.getStart()),
                ctx.getText(),
                (ExprNode) visit(ctx.expression()),
                ctx.Identifier().getText(),
                (ctx.expressionList()==null?new ArrayList<>():((FuncCallExprNode)visit(ctx.expressionList())).getParams())
        );
    }

    @Override
    public AstNode visitNewExpr(MxstarParser.NewExprContext ctx) {
        return new NewExprNode(
                new Position(ctx.getStart()),
                ctx.getText(),
                ((NewExprNode)visit(ctx.creator())).getNameTypeNode(),
                ((NewExprNode)visit(ctx.creator())).getExprInBracket(),
                ((NewExprNode)visit(ctx.creator())).getNum()
        );
    }

    @Override
    public AstNode visitSubscriptExpr(MxstarParser.SubscriptExprContext ctx) {
        return new SubScriptExprNode(
                new Position(ctx.getStart()),
                ctx.getText(),
                (ExprNode) visit(ctx.expression(0)),
                (ExprNode) visit(ctx.expression(1))
        );
    }

    @Override
    public AstNode visitFuncCallExpr(MxstarParser.FuncCallExprContext ctx) {
        return new FuncCallExprNode(
                new Position(ctx.getStart()),
                ctx.getText(),
                ctx.Identifier().getText(),
                (ctx.expressionList()==null?new ArrayList<>():((FuncCallExprNode)visit(ctx.expressionList())).getParams())
        );
    }

    @Override
    public AstNode visitSuffixExpr(MxstarParser.SuffixExprContext ctx) {
        SuffixExprNode.Op op = null;
        if(Objects.equals(ctx.suffix.getText(), "++"))op = SuffixExprNode.Op.SufPlus;
        if(Objects.equals(ctx.suffix.getText(), "--"))op = SuffixExprNode.Op.SufMinus;
        return new SuffixExprNode(
                new Position(ctx.getStart()),
                ctx.getText(),
                (ExprNode) visit(ctx.expression()),
                op
        );
    }

    @Override
    public AstNode visitPrefixExpr(MxstarParser.PrefixExprContext ctx) {
        PrefixExprNode.Op op =null;
        String prefixOp = ctx.prefix.getText();
        if(Objects.equals(prefixOp, "++"))op = PrefixExprNode.Op.PrePlusPlus;
        if(Objects.equals(prefixOp, "--"))op = PrefixExprNode.Op.PreMinusMinus;
        if(Objects.equals(prefixOp, "+"))op = PrefixExprNode.Op.SignPos;
        if(Objects.equals(prefixOp, "-"))op = PrefixExprNode.Op.SignNeg;
        if(Objects.equals(prefixOp, "~"))op = PrefixExprNode.Op.BitWiseNot;
        if(Objects.equals(prefixOp, "!"))op = PrefixExprNode.Op.LogicalNot;
        return new PrefixExprNode(
                new Position(ctx.getStart()),
                ctx.getText(),
                op,
                (ExprNode) visit(ctx.expression())
        );
    }

    @Override
    public AstNode visitBinaryExpr(MxstarParser.BinaryExprContext ctx) {
        BinaryExprNode.Op op = null;
        switch (ctx.op.getText()){
            case "*" : op = BinaryExprNode.Op.Mul;break;
            case "/" : op = BinaryExprNode.Op.Div;break;
            case "%" : op = BinaryExprNode.Op.Mod;break;
            case "+" : op = BinaryExprNode.Op.Add;break;
            case "-" : op = BinaryExprNode.Op.Sub;break;
            case "<<" : op = BinaryExprNode.Op.ShiftLeft;break;
            case ">>" : op = BinaryExprNode.Op.ShiftRight;break;
            case "<" : op = BinaryExprNode.Op.Less;break;
            case ">" : op = BinaryExprNode.Op.Greater;break;
            case "<=" : op = BinaryExprNode.Op.LessEqual;break;
            case ">=" : op = BinaryExprNode.Op.GreaterEqual;break;
            case "==" : op = BinaryExprNode.Op.Equal;break;
            case "!=" : op = BinaryExprNode.Op.NotEqual;break;
            case "&" : op = BinaryExprNode.Op.BitwiseAnd;break;
            case "^" : op = BinaryExprNode.Op.BitwiseXor;break;
            case "|" : op = BinaryExprNode.Op.BitwiseOr;break;
            case "&&" : op = BinaryExprNode.Op.LogicalAnd;break;
            case "||" : op = BinaryExprNode.Op.LogicalOr;break;
            default :
        }
        return new BinaryExprNode(
                new Position(ctx.getStart()),
                ctx.getText(),
                (ExprNode) visit(ctx.expression(0)),
                op,
                (ExprNode) visit(ctx.expression(1))
        );
    }

    @Override
    public AstNode visitAssignExpr(MxstarParser.AssignExprContext ctx) {
        return new AssignExprNode(
                new Position(ctx.getStart()),
                ctx.getText(),
                (ExprNode) visit(ctx.expression(0)),
                (ExprNode) visit(ctx.expression(1))
        );
    }

    @Override
    public AstNode visitYuanzi(MxstarParser.YuanziContext ctx) {
        if(ctx.This()!=null)
            return new ThisExprNode(new Position(ctx.getStart()));
        if(ctx.Identifier()!=null)
            return new IdExprNode(
                    new Position(ctx.getStart()),
                    ctx.getText(),
                    ctx.Identifier().getText()
            );
        if(ctx.contain()!=null)
            return visit(ctx.contain());
        if(ctx.expression()!=null)
            return visit(ctx.expression());
        return null;
    }

    @Override
    public AstNode visitContain(MxstarParser.ContainContext ctx) {
        if(ctx.IntContain()!=null)
            return new IntContainNode(
                    new Position(ctx.getStart()),
                    ctx.getText(),
                    Integer.parseInt(ctx.getText())
            );
        if(ctx.BoolContain!=null)
            return new BoolContainNode(
                    new Position(ctx.getStart()),
                    ctx.getText(),
                    ctx.getText().equals("true")
            );
        if(ctx.StringContain()!=null)
            return new StringContainNode(
                    new Position(ctx.getStart()),
                    ctx.getText(),
                    ctx.getText().substring(1,ctx.getText().length()-1)
            );
        return new NullContainNode(
                new Position(ctx.getStart())
        );
    }

    @Override
    public AstNode visitErrorCreator(MxstarParser.ErrorCreatorContext ctx) {
        throw new SyntaxError("Wrong syntax of creator",new Position(ctx.getStart()));
    }

    @Override
    public AstNode visitArrayCreator(MxstarParser.ArrayCreatorContext ctx) {
        ArrayList<ExprNode>ExprInBrackets = new ArrayList<>();
        for(var it : ctx.expression()){
            ExprInBrackets.add((ExprNode) visit(it));
        }
        return new NewExprNode(
                new Position(ctx.getStart()),
                ctx.getText(),
                (NameTypeNode) visit(ctx.nameType()),
                ExprInBrackets,
                ctx.LeftBracket().size()
        );
    }

    @Override
    public AstNode visitNormalCreator(MxstarParser.NormalCreatorContext ctx) {
        return new NewExprNode(
                new Position(ctx.getStart()),
                ctx.getText(),
                (NameTypeNode) visit(ctx.nameType()),
                new ArrayList<>(),
                0
        );
    }

    //[&](int a,int b){}()
    @Override
    public AstNode visitLambda(MxstarParser.LambdaContext ctx) {

        ArrayList<VarDefNode>formParamList = new ArrayList<>();
        if(ctx.paramList()!=null){
            formParamList.addAll(((VarDefListNode)visit(ctx.paramList())).getVarDefNodes());
        }

        return new LambdaExprNode(
                new Position(ctx.getStart()),
                ctx.getText(),
                (ctx.paramList()==null?new ArrayList<>():formParamList),
                (BlockStmtNode) visit(ctx.suite()),
                (ctx.expressionList()==null?new ArrayList<>():((FuncCallExprNode)visit(ctx.expressionList())).getParams())
        );
    }
}