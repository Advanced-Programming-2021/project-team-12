package models.card.trap;

import java.util.ArrayList;

public class TrapCard {
    private boolean isOriginal;
    private String description;
    private String realName;
    private String effect;
    private ArrayList<String> namesForEffect = new ArrayList<>();
    private boolean isLimit;
    private int price;
    private static ArrayList<TrapCard> trapCards;

    static {
        trapCards = new ArrayList<>();
    }

    public TrapCard(String name, boolean isLimit, int price, String description) {
        this.isLimit = isLimit;
        this.price = price;
        realName = name;
        isOriginal = true;
        this.description = description;
        trapCards.add(this);
        namesForEffect.add(name);
    }

    public TrapCard(String realName, int price, String description, ArrayList<String> names, boolean isOriginal) {
        this.price = price;
        this.realName = realName;
        this.namesForEffect = names;
        this.description = description;
        this.isOriginal = isOriginal;
        trapCards.add(this);
    }

    public ArrayList<String> getNamesForEffect() {
        return namesForEffect;
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

    public static TrapCard getTrapCardByName(String name) {
        for (TrapCard trapCard : trapCards)
            if (trapCard.realName.equals(name)) return trapCard;
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

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getRealName() {
        return realName;
    }

    public boolean isNew() {
        return !isOriginal;
    }

    public static ArrayList<TrapCard> getOriginalTrapCards() {
        ArrayList<TrapCard> originalTrapCards= new ArrayList<>();
        for (TrapCard trapCard : trapCards)
            if(!trapCard.isNew()) originalTrapCards.add(trapCard);
        return originalTrapCards;
    }
}
