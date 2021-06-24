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

    public static ArrayList<Card> getAllCards() {
        return cards;
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

    //Please don't call this method without my permission.
    public void runEffect() {
        if (kind.equals("Monster")) {
            MonsterCard card = MonsterCard.getMonsterCardByName(name);
            card.runEffect();
        } else if (kind.equals("Spell")) {
            SpellCard card = SpellCard.getSpellCardByName(name);
            card.runEffect();
        } else if (kind.equals("Trap")) {
            TrapCard card = TrapCard.getTrapCardByName(name);
            card.runEffect();
        }
    }

    public void showCard() {
        if (kind.equals("monster")) {
            System.out.println("Name: " + name + "\\nLevel: " + getLevel() + "\\nType: " + "\\nATK: " + getAttack()
                    + "\\nDEF: " + getDefence(true) + "\\nDescription: " + getDescription());
        }

        if (kind.equals("monster")) {
            System.out.println(
                    "Name: " + name + "\\nSpell" + "\\nType: " + getType() + "\\nDescription: " + getDescription());
        }

        if (kind.equals("monster")) {
            System.out.println(
                    "Name: " + name + "\\nTrap" + "\\nType: " + getType() + "\\nDescription: " + getDescription());
        }
    }

    public String getType() {
        if (kind.equals("monster")) {
            MonsterCard card = MonsterCard.getMonsterCardByName(name);
            return card.getMonsterMode().toString();
        }
        if (kind.equals("spell")) {
            SpellCard card = SpellCard.getSpellCardByName(name);
            return card.getSpellMode().toString();
        } else
            return "Normal";
    }

    public String getDescription() {
        if (kind.equals("monster")) {
            MonsterCard card = MonsterCard.getMonsterCardByName(name);
            return card.getDescription();
        }
        if (kind.equals("spell")) {
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

    public Boolean checkIsLimit() {
        if (kind.equals("Spell")) {
            SpellCard card = SpellCard.getSpellCardByName(name);
            return card.checkIsLimit();
        } else if (kind.equals("Trap")) {
            TrapCard card = TrapCard.getTrapCardByName(name);
            return card.checkIsLimit();
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

    public Boolean checkIsRitual() {
        MonsterCard card = MonsterCard.getMonsterCardByName(name);
        return card.isRitual();
    }

    public int getAttack() {
        MonsterCard card = MonsterCard.getMonsterCardByName(name);
        return card.getNormalAttack();
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
