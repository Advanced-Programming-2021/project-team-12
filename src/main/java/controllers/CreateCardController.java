package controllers;

import models.card.monster.MonsterCard;
import models.card.monster.MonsterMode;
import view.MainMenu;

import java.util.ArrayList;

public class CreateCardController {
    public static void createMonsterCard(String level, String attack, String defence, MonsterMode monsterMode
    , String realName, int price, ArrayList<MonsterCard> monsters){
        try {
            String description = null;
            ArrayList<String> names = new ArrayList<>();
            int levelI = Integer.parseInt(level);
            int attackI = Integer.parseInt(attack);
            int defenceI = Integer.parseInt(defence);
            boolean isRitual = false;
            for (MonsterCard monster : monsters) {
                description += monster.getDescription();
                names.add(monster.getName());
                if (monster.isRitual()) isRitual = true;
            }
            if (monsterMode != null && realName != null) {
                new MonsterCard(levelI, attackI, defenceI, monsterMode, isRitual, realName, price, description, names);
            }
            MainMenu.user.decreaseMoney(price / 10);
        } catch (Exception e) {

        }
    }
}
