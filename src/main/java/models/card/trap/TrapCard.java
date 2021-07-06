package models.card.trap;

import java.util.ArrayList;

public class TrapCard {
    private String description;
    private String name;
    private String realName;
    private String effect;
    private ArrayList<String> names = new ArrayList<>();
    private boolean isLimit;
    private int price;
    private static ArrayList<TrapCard> trapCards;

    static {
        trapCards = new ArrayList<>();
    }

    public TrapCard(String name, boolean isLimit, int price, String description) {
        this.isLimit = isLimit;
        this.price = price;
        this.name = name;
        realName = name;
        this.description = description;
        trapCards.add(this);
    }

    public TrapCard(String realName, int price, String description, ArrayList<String> names) {
        this.price = price;
        this.realName = realName;
        this.names = names;
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

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public static ArrayList<TrapCard> getTrapCards() {
        return trapCards;
    }

}
