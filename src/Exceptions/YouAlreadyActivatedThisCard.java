package Exceptions;

public class YouAlreadyActivatedThisCard extends Exception{
    public YouAlreadyActivatedThisCard(String message) {
        super(message);
    }
}
