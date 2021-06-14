package Exceptions;

public class YouAlreadyChangedThisCardPosition extends Exception{
    public YouAlreadyChangedThisCardPosition(String message) {
        super(message);
    }
}
