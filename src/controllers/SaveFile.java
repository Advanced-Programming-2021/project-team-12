package controllers;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import models.Card;
import models.User;
import models.card.monster.MonsterCard;
import org.json.simple.JSONObject;

public class SaveFile {
    public SaveFile() {
        for (User user : User.getUsers())
            saveUser(user);
        for (Card card : Card.getAllCards()) {
            if (card.getKind().equals("monster"))
                saveMonster(card);
            if (card.getKind().equals("spell"))
                saveSpell(card);
            else
                saveTrap(card);
        }
    }

    private void saveUser(User user) {
        JSONObject obj = new JSONObject();
        obj.put("name", user.getName());
        obj.put("nikName", user.getNickName());
        obj.put("password", user.getPassword());
        obj.put("score", user.getScore());
        obj.put("money", user.getMoney());
        obj.put("allCards", user.getAllCards());
        try {
            int number = 0;
            while (new File("data//User//" + number + ".txt").exists())
                number++;
            FileWriter writer = new FileWriter("data//User//" + number + ".txt");
            writer.write(obj.toJSONString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveMonster(Card card) {
        MonsterCard monster = MonsterCard.getMonsterCardByName(card.getCardName());
        JSONObject obj = new JSONObject();
        obj.put("name", monster.getName());
        obj.put("monsterMode", monster.getMonsterMode());
        obj.put("attack", monster.getAttack());
        obj.put("defence", monster.getDefenceNumber());
        obj.put("attribute", monster.getAttribute());
        obj.put("description", monster.getDescription());
        obj.put("effect", monster.getEffect());
        obj.put("level", monster.getLevel());
        obj.put("price", monster.getPrice());
        try {
            int number = 0;
            while (new File("data//monsterCards//" + number + ".txt").exists())
                number++;
            FileWriter writer = new FileWriter("data//monsterCards//" + number + ".txt");
            writer.write(obj.toJSONString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
