package models.card.trap;

import models.card.trap.trap_effect.TrapEffect;

import java.util.ArrayList;

public class TrapCard {
    private String description;
    private String name;
    private String effect;
    private String description;
    private boolean isLimit;
    private int price;
    private TrapEffect trapEffect;
    private static ArrayList<TrapCard> trapCards;
    static{
        trapCards=new ArrayList<>();
    }
    public TrapCard(String name, String effect, boolean isLimit,int price, TrapEffect trapEffect){
        this.effect=effect;
        this.isLimit=isLimit;
        this.price=price;
        this.trapEffect=trapEffect;
        this.name=name;
        trapCards.add(this);
    }

    public void setDesctiption(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getEffect() {
        return effect;
    }

    public int getPrice() {
        return price;
    }

    public boolean ckeckIsLimit() {
        return isLimit;
    }
    public void runEffect(){
        this.trapEffect.run();
    }
    public static TrapCard getTrapCardByName(String name){
        for (TrapCard trapCard : trapCards)
            if (trapCard.name.equals(name)) return trapCard;
        return null;
    }
    public static void welcomeToEffect(){

    }
    public String getDescription() {
        return description;
    }
}
