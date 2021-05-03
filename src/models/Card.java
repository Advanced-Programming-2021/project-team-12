package models;

import java.util.ArrayList;

import models.card.monster.Attribute;
import models.card.monster.MonsterCard;
import models.card.monster.MonsterMode;
import models.card.spell.SpellCard;
import models.card.spell.SpellMode;
import models.card.trap.TrapCard;

public class Card {
    private String kind;
    private String name;
    private static ArrayList<Card> cards = new ArrayList<>();

    public Card(String name, String king) {
        this.kind = kind;
        this.name = name;
        cards.add(this);
    }

    public String getCardName() {
        return name;
    }

    public String getKind() {
        return kind;
    }

    public static String whatKind(String cardName) {
        for (Card card : cards) 
            if (card.getCardName().equals(cardName))
                return card.kind;
        return "there is no card with this name";
    }

    public void runEffect () {
        if (kind.equals("Monster")) {
            MonsterCard card = MonsterCard.getMonsterCardByName(name);
            card.runEffect();
        }
        else if (kind.equals("Spell")) {
            SpellCard card = SpellCard.getSpellCardByName(name);
            card.runEffect();
        }
        else if (kind.equals("Trap")) {
            TrapCard card = TrapCard.getTrapCardByName(name);
            card.runEffect();
        }
    }

    public static Card getCardByName(String cardName) {
        for (Card card : cards) 
            if (card.getCardName().equals(cardName))
                return card;
        return null;
    }

    public int getPrice() {
        if (kind.equals("Monster")) {
            MonsterCard card = MonsterCard.getMonsterCardByName(name);
            return card.getPrice();
        }
        else if (kind.equals("Spell")) {
            SpellCard card = SpellCard.getSpellCardByName(name);
            return card.getPrice();
        }
        else if (kind.equals("Trap")) {
            TrapCard card = TrapCard.getTrapCardByName(name);
            return card.getPrice();
        }
        return 0;
    }

    public Boolean ckeckIsLimit() {
        if (kind.equals("Spell")) {
            SpellCard card = SpellCard.getSpellCardByName(name);
            return card.checkIsLimit();
        }
        else if (kind.equals("Trap")) {
            TrapCard card = TrapCard.getTrapCardByName(name);
            return card.ckeckIsLimit();
        }
        return false;
    }

    public SpellMode getSpellMode() {
        SpellCard card = SpellCard.getSpellCardByName(name);
        return card.getSpellMode();
    }

    public MonsterMode getMonsterMode() {
        MonsterCard card = MonsterCard.getMonsterCardByName(name);
        return card.getMonsterMode();
    }

    public String getEffect() {
        if (kind.equals("Monster")) {
            MonsterCard card = MonsterCard.getMonsterCardByName(name);
            return card.getEffect();
        }
        else if (kind.equals("Spell")) {
            SpellCard card = SpellCard.getSpellCardByName(name);
            return card.getEffect();
        }
        else if (kind.equals("Trap")) {
            TrapCard card = TrapCard.getTrapCardByName(name);
            return card.getEffect();
        }
        return "there is no card with this name";
    }

    public Attribute getAttribute() {
        MonsterCard card = MonsterCard.getMonsterCardByName(name);
        return card.getAttribute();
    }

    public Boolean checkIsRitual() {
        MonsterCard card = MonsterCard.getMonsterCardByName(name);
        return card.checkIsRitual();
    }

    public int getAttack() {
        MonsterCard card = MonsterCard.getMonsterCardByName(name);
        return card.getAttack();
    }

    public int getDefence(Boolean isFaceUp) {
        MonsterCard card = MonsterCard.getMonsterCardByName(name);
        return card.getDefence(isFaceUp);
    }

    public int getLevel() {
        MonsterCard card = MonsterCard.getMonsterCardByName(name);
        return card.getLevel();
    }








}
