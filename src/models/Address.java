package models;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Address {
    private String kind;
    private int number;
    private Boolean isMine;
    private Boolean isScanner;

    public Address(String address) {
        Matcher matcher;
        this.number = 1;
        matcher = getCommandMatcher(address, "(select )*(--[\\w]+|-[\\w])( --opponent| -o)* ([\\d])");
        if (matcher.find()) {
            if (matcher.group(2).equals("-m"))
                this.kind = "monster";
            else if (matcher.group(2).equals("-s"))
                this.kind = "spell";
            else if (matcher.group(2).equals("-h"))
                this.kind = "hand";
            else 
                this.kind = matcher.group(2).substring(2);
            this.number = Integer.parseInt(matcher.group(4));
            if (!matcher.group(3).isEmpty())
                this.isMine = false;
            else this.isMine = true;
        } else {
            matcher = getCommandMatcher(address, "(select )*(--field|--graveyard|-f|-g)( --opponent| -o)*");
            if (matcher.group(2).equals("-f"))
                this.kind = "field";
            if (matcher.group(2).equals("-g"))
                this.kind = "graveyard";
            else 
                this.kind = matcher.group(2).substring(2);
            if (!matcher.group(3).isEmpty())
                this.isMine = false;
            else this.isMine = true;
        }
    }

    public Address(int number, String kind, boolean isMine) {
        this.number = number;
        this.kind = kind;
        this.isMine = isMine;
    }

    public String getKind() {
        return kind;
    }

    public int getNumber() {
        return number;
    }

    public Boolean checkIsMine() {
        return isMine;
    }

    public void setIsScanner(Boolean isScanner) {
        this.isScanner = isScanner;
    }

    public Boolean getScanner() {
        return isScanner;
    }

    public static Boolean isAddressValid(String address) {
        if (address.matches("(select )*(--monster|-m)( --opponent| -o)* [12345]")
                || address.matches("(select )*(--spell|-s)( --opponent| -o)* [12345]")
                || address.matches("(select )*(--field|-f)( --opponent| -o)*")
                || address.matches("(select )*(--graveyard|-g)( --opponent| -o)*( [\\d]+)*")
                || address.matches("(select )*(--hand|-h)( --opponent| -o)* [123456]"))
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
