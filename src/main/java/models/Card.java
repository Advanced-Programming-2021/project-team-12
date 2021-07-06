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

    public Card(String name, String kind) {
        this.kind = kind;
        this.name = name;
        cards.add(this);
    }

    public static ArrayList<Card> getAllCards() {
        return cards;
    }

    public String getCardName() {
        return name;
    }

    public String getKind() {
        return kind;
    }

    public String getType() {
        if (kind.equals("Monster")) {
            MonsterCard card = MonsterCard.getMonsterCardByName(name);
            return card.getMonsterMode().toString();
        }
        if (kind.equals("Spell")) {
            SpellCard card = SpellCard.getSpellCardByName(name);
            return card.getSpellMode().toString();
        } else
            return "Normal";
    }

    public String getDescription() {
        if (kind.equals("Monster")) {
            MonsterCard card = MonsterCard.getMonsterCardByName(name);
            return card.getDescription();
        }
        if (kind.equals("Spell")) {
            SpellCard card = SpellCard.getSpellCardByName(name);
            return card.getDescription();
        } else {
            TrapCard card = TrapCard.getTrapCardByName(name);
            return card.getDescription();
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
        } else if (kind.equals("Spell")) {
            SpellCard card = SpellCard.getSpellCardByName(name);
            return card.getPrice();
        } else if (kind.equals("Trap")) {
            TrapCard card = TrapCard.getTrapCardByName(name);
            return card.getPrice();
        }
        return 0;
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
        } else if (kind.equals("Spell")) {
            SpellCard card = SpellCard.getSpellCardByName(name);
            return card.getEffect();
        } else if (kind.equals("Trap")) {
            TrapCard card = TrapCard.getTrapCardByName(name);
            return card.getEffect();
        }
        return "there is no card with this name";
    }

    public Attribute getAttribute() {
        MonsterCard card = MonsterCard.getMonsterCardByName(name);
        return card.getAttribute();
    }

    public int getAttack() {
        MonsterCard card = MonsterCard.getMonsterCardByName(name);
        return card.getNormalAttack();
    }

    public int getDefence() {
        MonsterCard card = MonsterCard.getMonsterCardByName(name);
        return card.getNormalDefence();
    }

    public int getLevel() {
        MonsterCard card = MonsterCard.getMonsterCardByName(name);
        return card.getLevel();
    }

    public boolean isOriginal() {
        if (kind.equals("Monster")) return !MonsterCard.getMonsterCardByName(name).isNew();
        else if (kind.equals("Spell")) return !SpellCard.getSpellCardByName(name).isNew();
        else return !TrapCard.getTrapCardByName(name).isNew();
    }

}
