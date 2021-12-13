grammar Mxstar;
//终结词
//保留词
Int:'int';
Bool:'bool';
True:'true';
False:'false';
String:'string';
Null:'null';
Void:'void';
If:'if';
Else:'else';
For:'for';
While:'while';
Break:'break';
Continue:'continue';
Return:'return';
New:'new';
Class:'class';
This:'this';
//others
LeftParen : '(';
RightParen : ')';
LeftBracket : '[';
RightBracket : ']';
LeftBrace : '{';
RightBrace : '}';

Less : '<';
LessEqual : '<=';
Greater : '>';
GreaterEqual : '>=';
LeftShift : '<<';
RightShift : '>>';

Plus : '+';
Minus : '-';
Star:'*';
Div:'/';
Mod:'%';

SelfPlus:'++';
SelfMinus:'--';

And : '&';
Or : '|';
AndAnd : '&&';
OrOr : '||';
Xor : '^';
Not : '!';
Tilde : '~';

Question : '?';
Colon : ':';
Semi : ';';
Comma : ',';

Assign : '=';
Equal : '==';
NotEqual : '!=';

Dot:'.';
//文档里面Mxstar是没有\r的，对吧？？？？
fragment ESC//
    : '\\'[tnr"\\]; //\t \n \" \\等// \r似乎还是得加上不然有些数据跑不了
StringContain: '"' (ESC|.)*? '"';//
IntContain: [1-9] [0-9]* | '0';

contain
    : IntContain
    | StringContain
    | BoolContain=(True|False)
    | Null
    ;


Identifier: [a-zA-Z] [a-zA-Z0-9_]*;//if [a-zA-Z][\w]*可否     // ~[1-9]和[~1-9]是一个意思吗 todo//[a|b]应该等价于[ab]吧  kn//不应该写错=成前面那个样子吧
Whitespace
    :   [ \t]+
        -> skip
    ;

Newline
    :    ('\r''\n'?|'\n')
         -> skip
    ;


LineComment
    :   '//' ~[\r\n]*
        -> skip
    ;

BlockComment
    :   '/*' .*? '*/'
        -> skip
    ;


//非终结词
program : programSection*  EOF;

//mainFn:'int' 'main()' suite;

programSection:varDef|funcDef|classDef;

classDef: Class Identifier '{' (varDef | funcDef)* classCreatorFuncDef? (varDef | funcDef)*'}' ';';
funcDef: type Identifier '(' paramList? ')' suite;
varDef: type varDefSection (',' varDefSection)* ';';
classCreatorFuncDef:Identifier '('')'suite;

type:Void|nameType|arrayType;

basicType:Bool|Int|String;
nameType:basicType|Identifier;
arrayType:nameType ('[' ']')+;

varDefSection:Identifier ('=' expression)?;

paramList:param (',' param)*;
param:type Identifier;

suite:'{' statement* '}';

init:expression;
condition:expression;
change:expression;

statement
    : suite                                                 #block//左递归
    | varDef                                                #vardefStmt
    | If '(' expression ')' trueStmt=statement
        (Else falseStmt=statement)?                         #ifStmt
    | For '(' init? ';' condition? ';'change? ')' statement        #forStmt//所以这是不支持for(a=1,b=2;a<=10;a++)的对吧，既然是expression
    | While '(' expression ')' statement                    #whileStmt
    | Return expression? ';'                                #returnStmt
    | Break ';'                                             #breakStmt
    | Continue ';'                                          #continueStmt
    | expression ';'                                        #pureExprStmt
    | ';'                                                   #emptyStmt
    ;


expressionList: expression (',' expression)*;
//[&](Parameters) -> {Statements}()
yuanzi:contain| '(' expression ')' | This| Identifier;
expression
    : yuanzi                                                                     #atomExpr
    | expression '.' Identifier                                                  #memberExpr
    | expression '.' Identifier '(' expressionList? ')'                          #methodExpr
    | <assoc=right> New creator                                                  #newExpr
    | expression '[' expression ']'                                              #subscriptExpr
    | '[' '&' ']' ('(' paramList? ')')? '-' '>' suite '(' expressionList? ')'    #lambda//优先级需要界定吗？如果需要，要放在那里呢？
    | Identifier '(' expressionList? ')'                                         #funcCallExpr
    | expression suffix = ('++' | '--')                                          #suffixExpr//根据C语言性质，后缀表达式优先级高于前缀
    | <assoc=right> prefix = ('+' | '-' | '++' | '--' | '~' | '!') expression    #prefixExpr
    | expression op=('*' | '/' | '%') expression                                 #binaryExpr
    | expression op=('+' | '-') expression                                       #binaryExpr
    | expression op=('<<' | '>>') expression                                     #binaryExpr
    | expression op=('<' | '>' | '<=' | '>=') expression                         #binaryExpr
    | expression op=('==' | '!=') expression                                     #binaryExpr
    | expression op='&' expression                                               #binaryExpr
    | expression op='^' expression                                               #binaryExpr
    | expression op='|' expression                                               #binaryExpr
    | expression op='&&' expression                                              #binaryExpr
    | expression op='||' expression                                              #binaryExpr
    | <assoc=right> expression '=' expression                                    #assignExpr
    ;

creator//new int()//我这样无法解决
    : nameType ('[' expression ']')+ ('[' ']')+ ('[' expression ']')+ #errorCreator//errorCreator是干嘛的呢
    | nameType ('[' expression ']')+ ('[' ']')*                       #arrayCreator
    | nameType ('(' ')')?                                             #normalCreator
    ;