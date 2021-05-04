package models;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Address {
    private String kind;
    private int number;
    private Boolean isMine;

    public Address(String address) {
        Matcher matcher;
        this.number = 1;
        matcher = getCommandMatcher(address, "(select )*--([\\w]+)( --opponent)* ([\\d])");
        if (matcher.find()) {
            this.kind = matcher.group(2);
            this.number = Integer.parseInt(matcher.group(4));
            if (!matcher.group(3).isEmpty()) 
                this.isMine = false;
            else this.isMine = true;
        }
        else {
            matcher = getCommandMatcher(address, "(select )*--(field|graveyard)( --opponent)*");
            this.kind = matcher.group(2);
            if (!matcher.group(3).isEmpty()) 
                this.isMine = false;
            else this.isMine = true;
        }
    }

    public String getKind() {
        return kind;
    }

    public int getNumber() {
        return number;
    }

    public Boolean ckeckIsMine() {
        return isMine;
    }

    public static Boolean isAddressValid(String address) {
        if (address.matches("(select )*--monster( --opponent)* [12345]")
           || address.matches("(select )*--spell( --opponent)* [12345]")
           || address.matches("(select )*--field( --opponent)*")
           || address.matches("(select )*--graveyard( --opponent)*( [\\d]+)*")
           || address.matches("(select )*--hand( --opponent)* [123456]"))
            return true;
        else return false;
    }

    public static Matcher getCommandMatcher(String input, String regex) {
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(input);
        return matcher;
    }
}
