package models.card.trap;

import models.card.trap.trap_effect.TrapEffect;

import java.util.ArrayList;

public class TrapCard {
    private String name;
    private String effect;
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

    public String getName() {
        return name;
    }

    public String getEffect() {
        return effect;
    }

    public int getPrice() {
        return price;
    }

    public boolean isLimit() {
        return isLimit;
    }
    public void runEffect(){
        this.trapEffect.run();
    }
    public TrapCard getTrapCardByName(String name){
        for (TrapCard trapCard : trapCards)
            if (trapCard.name.equals(name)) return trapCard;
        return null;
    }
}
