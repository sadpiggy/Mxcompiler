package Mutil.error;

import Mutil.Position;

abstract public class Error extends RuntimeException {
    private Position position;
    private String message;
    public Error(String message_,Position position_){
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
