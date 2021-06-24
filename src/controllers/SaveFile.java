package controllers;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import models.Card;
import models.Deck;
import models.User;
import models.card.monster.MonsterCard;
import models.card.spell.SpellCard;
import models.card.trap.TrapCard;
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

    public static void saveUser(User user) {
        JSONObject obj = new JSONObject();
        ArrayList<String> userCardsName = new ArrayList<>();
        if (user.getAllCards() != null)
            for (Card card : user.getAllCards())
                userCardsName.add(card.getCardName());
        obj.put("userName", user.getName());
        obj.put("nickName", user.getNickName());
        obj.put("password", user.getPassword());
        obj.put("score", user.getScore());
        obj.put("money", user.getMoney());
        obj.put("allCards", userCardsName);
        try {
            String name = user.getName();
            FileWriter writer = new FileWriter("data//User//" + name + ".json");
            writer.write(obj.toJSONString());
            writer.close();
            File theDir = new File("data//Decks//" + name);
            if (!theDir.exists())
                theDir.mkdirs();
        } catch (IOException e) {
            e.printStackTrace();
        }
        saveUserDecks(user);
    }

    public static void exportCard(String address, String cardName) {
        Card card = Card.getCardByName(cardName);
        if (card.getKind().equals("monster"))
            exportMonster(card, address);
        if (card.getKind().equals("spell"))
            exportSpell(card, address);
        else
            exportTrap(card, address);
    }

    private static void exportTrap(Card card, String address) {
        TrapCard trap = TrapCard.getTrapCardByName(card.getCardName());
        JSONObject obj = new JSONObject();
        obj.put("description", trap.getDescription());
        obj.put("name", trap.getName());
        obj.put("isLimit", trap.checkIsLimit());
        obj.put("price", trap.getPrice());
        try {
            FileWriter writer = new FileWriter(address);
            writer.write(obj.toJSONString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void exportSpell(Card card, String address) {
        SpellCard spell = SpellCard.getSpellCardByName(card.getCardName());
        JSONObject obj = new JSONObject();
        obj.put("desctiption", spell.getDescription());
        obj.put("price", spell.getPrice());
        obj.put("spellMode", spell.getSpellMode());
        obj.put("isLimit", spell.checkIsLimit());
        obj.put("name", spell.getName());
        try {
            FileWriter writer = new FileWriter(address);
            writer.write(obj.toJSONString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void exportMonster(Card card, String address) {
        MonsterCard monster = MonsterCard.getMonsterCardByName(card.getCardName());
        JSONObject obj = new JSONObject();
        obj.put("name", monster.getName());
        obj.put("monsterMode", monster.getMonsterMode().toString());
        obj.put("attack", monster.getNormalAttack());
        obj.put("defence", monster.getDefenceNumber());
        obj.put("attribute", monster.getAttribute().toString());
        obj.put("description", monster.getDescription());
        obj.put("level", monster.getLevel());
        obj.put("price", monster.getPrice());
        obj.put("isRitual", monster.isRitual());
        try {
            FileWriter writer = new FileWriter(address);
            writer.write(obj.toJSONString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void saveUserDecks(User user) {
        ArrayList<Deck> userDecks = Deck.getDecksOfUser(user);
        for (Deck deck: userDecks) {
            JSONObject obj = new JSONObject();
            ArrayList<String> mainCardNames = new ArrayList<>();
            ArrayList<String> sideCardNames = new ArrayList<>();
            for (Card card : deck.getMainCards())
                mainCardNames.add(card.getCardName());
            for (Card card : deck.getSideCards())
                sideCardNames.add(card.getCardName());
            obj.put("deckName", deck.getName());
            obj.put("isActive", deck.checkIsActive());
            obj.put("mainCards", mainCardNames);
            obj.put("sideCards", sideCardNames);
            try {
                FileWriter writer = new FileWriter("data//Decks//" + user.getName() + "//" + deck.getName() + ".json");
                writer.write(obj.toJSONString());
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveMonster(Card card) {
        MonsterCard monster = MonsterCard.getMonsterCardByName(card.getCardName());
        JSONObject obj = new JSONObject();
        obj.put("name", monster.getName());
        obj.put("monsterMode", monster.getMonsterMode().toString());
        obj.put("attack", monster.getNormalAttack());
        obj.put("defence", monster.getDefenceNumber());
        obj.put("attribute", monster.getAttribute().toString());
        obj.put("description", monster.getDescription());
        obj.put("level", monster.getLevel());
        obj.put("price", monster.getPrice());
        obj.put("isRitual", monster.isRitual());
        try {
            FileWriter writer = new FileWriter("data//monsterCards//" + monster.getName() + ".json");
            writer.write(obj.toJSONString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveSpell(Card card) {
        SpellCard spell = SpellCard.getSpellCardByName(card.getCardName());
        JSONObject obj = new JSONObject();
        obj.put("desctiption", spell.getDescription());
        obj.put("price", spell.getPrice());
        obj.put("spellMode", spell.getSpellMode());
        obj.put("isLimit", spell.checkIsLimit());
        obj.put("name", spell.getName());
        try {
            FileWriter writer = new FileWriter("data//spellCards//" + spell.getName() + ".json");
            writer.write(obj.toJSONString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveTrap(Card card) {
        TrapCard trap = TrapCard.getTrapCardByName(card.getCardName());
        JSONObject obj = new JSONObject();
        obj.put("description", trap.getDescription());
        obj.put("name", trap.getName());
        obj.put("isLimit", trap.checkIsLimit());
        obj.put("price", trap.getPrice());
        try {
            FileWriter writer = new FileWriter("data//trapCards//" + trap.getName() + ".json");
            writer.write(obj.toJSONString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
