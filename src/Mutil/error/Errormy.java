package Mutil.error;

import Mutil.Position;
//Error之后应该会有很多类,都是从这个上面继承而来
abstract public class Errormy extends RuntimeException {
    private final Position position;
    private final String message;
    public Errormy(String message_,Position position_){
        message = message_;
        position = position_;
    }
    public String getString(){
        if(position==null){
            return message + "" + ".\n";
        }else {
            return message + " at " + position.getString() + ".\n";
        }
    }
}
