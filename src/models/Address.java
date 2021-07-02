package models;

import Utility.CommandMatcher;

import java.util.Objects;
import java.util.regex.Matcher;

public class Address {
    private String kind;
    private int number;
    private Boolean isMine;
    private Boolean isScanner;
    public Address(String address) {
        Matcher matcher;
        this.number = 1;
        matcher = CommandMatcher.getCommandMatcher(address, "(select )*(--[\\w]+|-[\\w]+)( --opponent| -o)* ([\\d])");
        if (matcher.find()) {
            if (matcher.group(2).equals("-m") || matcher.group(2).equals("--monster"))
                this.kind = "monster";
            else if (matcher.group(2).equals("-s") || matcher.group(2).equals("--spell"))
                this.kind = "spell";
            else if (matcher.group(2).equals("-h") || matcher.group(2).equals("--hand"))
                this.kind = "hand";
            else 
                this.kind = matcher.group(2).substring(2);
            this.number = Integer.parseInt(matcher.group(4));
        } else {
            matcher = CommandMatcher.getCommandMatcher(address, "(select )*(--field|--graveyard|-f|-g)( --opponent| -o)*");
            if (matcher.group(2).equals("-f") || matcher.group(2).equals("--field"))
                this.kind = "field";
            if (matcher.group(2).equals("-g") || matcher.group(2).equals("-graveyard"))
                this.kind = "graveyard";
            else 
                this.kind = matcher.group(2).substring(2);
        }
        if (CommandMatcher.getCommandMatcher(address, "( --opponent| -o)").find())
            this.isMine = false;
        else this.isMine = true;
    }

    public Address(int number, String kind, boolean isMine) {
        this.number = number;
        this.kind = kind;
        this.isMine = isMine;
    }

    @Override
    public boolean equals(Object ob)
    {
        if (ob == this) {
            return true;
        }

        if (ob == null || ob.getClass() != getClass()) {
            return false;
        }

        Address a = (Address) ob;
        return kind.equals(a.getKind()) && number == a.getNumber();
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, kind);
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
}
