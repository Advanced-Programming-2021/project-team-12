package models;

import Utility.CommandMatcher;
import models.card.monster.MonsterCard;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Address {
    private String kind;
    private int number;
    private Boolean isMine;
    private Boolean isScanner;
    private MonsterCard ifItIsScannerThenWhat;
    public Address(String address) {
        Matcher matcher;
        this.number = 1;
        matcher = CommandMatcher.getCommandMatcher(address, "(select )*(--[\\w]+|-[\\w])( --opponent| -o)* ([\\d])");
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
            matcher = CommandMatcher.getCommandMatcher(address, "(select )*(--field|--graveyard|-f|-g)( --opponent| -o)*");
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
        return Objects.hash(number, kind, isMine);
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

    public MonsterCard getIfItIsScannerThenWhat() {
        return ifItIsScannerThenWhat;
    }

    public void setIfItIsScannerThenWhat(MonsterCard ifItIsScannerThenWhat) {
        this.ifItIsScannerThenWhat = ifItIsScannerThenWhat;
    }
}
