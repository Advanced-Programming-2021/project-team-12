package models.card.trap;

import models.card.trap.trap_effect.TrapEffect;

import java.util.ArrayList;

public class TrapCard {
    private String description;
    private String name;
    private String effect;
    private boolean isLimit;
    private int price;
    private TrapEffect trapEffect;
    private static ArrayList<TrapCard> trapCards;

    static {
        trapCards = new ArrayList<>();
    }

    public TrapCard(String name, boolean isLimit, int price, String description) {
        this.effect = effect;
        this.isLimit = isLimit;
        this.price = price;
        this.trapEffect = trapEffect;
        this.name = name;
        this.description = description;
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

    public boolean checkIsLimit() {
        return isLimit;
    }

    public void runEffect() {
        this.trapEffect.run();
    }

    public static TrapCard getTrapCardByName(String name) {
        for (TrapCard trapCard : trapCards)
            if (trapCard.getName().equals(name)) return trapCard;
        return null;
    }

    public static void welcomeToEffect() {

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLimit(boolean limit) {
        isLimit = limit;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setTrapEffect(TrapEffect trapEffect) {
        this.trapEffect = trapEffect;
    }
}
