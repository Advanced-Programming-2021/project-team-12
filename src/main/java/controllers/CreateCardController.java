package controllers;

import models.Card;
import models.card.monster.MonsterCard;
import models.card.monster.MonsterMode;
import models.card.spell.SpellCard;
import models.card.spell.SpellMode;
import models.card.trap.TrapCard;
import view.MainMenu;

import java.util.ArrayList;

public class CreateCardController {
    public static void createMonsterCard(String level, String attack, String defence, MonsterMode monsterMode
            , String realName, int price, ArrayList<MonsterCard> monsters) {
        try {
            String description = null;
            ArrayList<String> names = new ArrayList<>();
            int levelI = Integer.parseInt(level);
            int attackI = Integer.parseInt(attack);
            int defenceI = Integer.parseInt(defence);
            boolean isRitual = false;
            for (MonsterCard monster : monsters) {
                description += monster.getDescription();
                names.add(monster.getRealName());
                if (monster.isRitual()) isRitual = true;
            }
            if (monsterMode != null && realName != null && MonsterCard.getMonsterCardByName(realName) == null) {
                new MonsterCard(levelI, attackI, defenceI, monsterMode, isRitual, realName, price, description, names, false);
                new Card(realName, "Monster");
                SaveFile.saveCards();
                MainMenu.user.decreaseMoney(price / 10);
            }
        } catch (Exception e) {

        }
    }

    public static void createSpellCard(String realName, SpellMode spellMode, int price, ArrayList<SpellCard> spellCards) {
        try {
            if (realName != null && spellMode != null && SpellCard.getSpellCardByName(realName) == null) {
                String description = null;
                ArrayList<String> names = new ArrayList<>();
                for (SpellCard spellCard : spellCards) {
                    description += spellCard.getDescription();
                    names.add(spellCard.getRealName());
                }
                new SpellCard(realName, spellMode, price, description, names, false);
                new Card(realName, "Spell");
                SaveFile.saveCards();
                MainMenu.user.decreaseMoney(price / 10);
            }
        } catch (Exception e) {
        }
    }

    public static void createTrapCard(String realName, ArrayList<TrapCard> trapCards, int price) {
        if (realName != null && TrapCard.getTrapCardByName(realName) == null) {
            String description = null;
            ArrayList<String> names = new ArrayList<>();
            for (TrapCard trapCard : trapCards) {
                description += trapCard.getDescription();
                names.add(trapCard.getRealName());
            }
            new TrapCard(realName, price, description, names, false);
            new Card(realName, "Trap");
            SaveFile.saveCards();
            MainMenu.user.decreaseMoney(price / 10);
        }
    }
}
