package models.card.spell;

import models.card.spell.spell_effect.SpellEffect;
//import card.trap.TrapCard;

import java.util.ArrayList;

public class SpellCard {
    private String effect;
    private int price;
    private SpellMode spellMode;
    private boolean isLimit;
    private String name;
    private SpellEffect spellEffect;
    private static ArrayList<SpellCard> spellCards;
    static {
        spellCards=new ArrayList<>();
    }
    public SpellCard(String name, SpellMode spellMode, boolean isLimit, String effect, int price, SpellEffect spellEffect){
        this.name=name;
        this.spellMode=spellMode;
        this.isLimit=isLimit;
        this.effect=effect;
        this.price=price;
        this.spellEffect=spellEffect;
        spellCards.add(this);
    }

    public static ArrayList<SpellCard> getSpellCards() {
        return spellCards;
    }

    public SpellMode getSpellMode() {
        return spellMode;
    }

    public boolean checkIsLimit() {
        return isLimit;
    }

    public int getPrice() {
        return price;
    }

    public String getEffect() {
        return effect;
    }

    public String getName() {
        return name;
    }

    public void runEffect() {
        this.spellEffect.run();
    }
    public static SpellCard getSpellCardByName(String name){
        for (SpellCard spellCard : spellCards)
            if (spellCard.name.equals(name)) return spellCard;
        return null;
    }
    public static void welcomeToEffect(){

    }
}
