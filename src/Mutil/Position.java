package Mutil;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;
public class Position {
    private int row;
    private int col;

    public Position(int row_,int col_){
        row = row_;
        col = col_;
    }

    public Position(Token token){
        row = token.getLine();
        col = token.getCharPositionInLine();
    }

    public Position(TerminalNode terminalNode){
        Token token = terminalNode.getSymbol();
        row = token.getLine();
        col = token.getCharPositionInLine();
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public String getString(){
        return row + "," + col;
    }
}
