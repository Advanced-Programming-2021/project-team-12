package controllers;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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
            if (card.getKind().equals("Monster"))
                saveMonster(card);
            else if (card.getKind().equals("Spell"))
                saveSpell(card);
            else
                saveTrap(card);
        }
    }

    public static void saveCards() {
        for (Card card : Card.getAllCards()) {
            if (card.getKind().equals("Monster"))
                saveMonster(card);
            else if (card.getKind().equals("Spell"))
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
        obj.put("avatar", user.getIntAvatar());
        obj.put("countAvatar", user.getAvatarCount());
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

    public static void exportCard(File file, String cardName) {
        Card card = Card.getCardByName(cardName);
        if (card.getKind().equals("Monster"))
            exportMonster(card, file);
        else if (card.getKind().equals("Spell"))
            exportSpell(card, file);
        else
            exportTrap(card, file);
    }

    private static void exportTrap(Card card, File file) {
        TrapCard trap = TrapCard.getTrapCardByName(card.getCardName());
        JSONObject obj = new JSONObject();
        if (card.getNamesForEffect() != null)
            obj.put("namesForEffect", card.getNamesForEffect());
        else obj.put("namesForEffect", new ArrayList<String>());
        obj.put("description", trap.getDescription());
        obj.put("name", trap.getRealName());
        obj.put("isLimit", trap.checkIsLimit());
        obj.put("price", trap.getPrice());
        try {
            FileWriter writer = new FileWriter(file.getAbsolutePath() + "//" + card.getCardName() + ".json");
            writer.write(obj.toJSONString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void exportSpell(Card card, File file) {
        SpellCard spell = SpellCard.getSpellCardByName(card.getCardName());
        JSONObject obj = new JSONObject();
        if (card.getNamesForEffect() != null)
            obj.put("namesForEffect", card.getNamesForEffect());
        else obj.put("namesForEffect", new ArrayList<String>());
        obj.put("desctiption", spell.getDescription());
        obj.put("price", spell.getPrice());
        obj.put("spellMode", spell.getSpellMode());
        obj.put("isLimit", spell.checkIsLimit());
        obj.put("name", spell.getRealName());
        try {
            FileWriter writer = new FileWriter(file.getAbsolutePath() + "//" + card.getCardName() + ".json");
            writer.write(obj.toJSONString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void exportMonster(Card card, File file) {
        MonsterCard monster = MonsterCard.getMonsterCardByName(card.getCardName());
        JSONObject obj = new JSONObject();
        if (card.getNamesForEffect() != null)
            obj.put("namesForEffect", card.getNamesForEffect());
        else obj.put("namesForEffect", new ArrayList<String>());
        obj.put("name", monster.getRealName());
        obj.put("monsterMode", monster.getMonsterMode().toString());
        obj.put("attack", monster.getNormalAttack());
        obj.put("defence", monster.getDefenceNumber());
        obj.put("attribute", monster.getAttribute().toString());
        obj.put("description", monster.getDescription());
        obj.put("level", monster.getLevel());
        obj.put("price", monster.getPrice());
        obj.put("isRitual", monster.isRitual());
        try {
            FileWriter writer = new FileWriter(file.getAbsolutePath() + "//" + card.getCardName() + ".json");
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

    private static void saveMonster(Card card) {
        MonsterCard monster = MonsterCard.getMonsterCardByName(card.getCardName());
        JSONObject obj = new JSONObject();
        if (card.getNamesForEffect() != null)
            obj.put("namesForEffect", card.getNamesForEffect());
        else obj.put("namesForEffect", new ArrayList<String>());
        obj.put("name", monster.getRealName());
        obj.put("monsterMode", monster.getMonsterMode().toString());
        obj.put("attack", monster.getNormalAttack());
        obj.put("defence", monster.getDefenceNumber());
        obj.put("attribute", monster.getAttribute().toString());
        obj.put("description", monster.getDescription());
        obj.put("level", monster.getLevel());
        obj.put("price", monster.getPrice());
        obj.put("isRitual", monster.isRitual());
        obj.put("isOriginal", !monster.isNew());
        try {
            FileWriter writer = new FileWriter("data//monsterCards//" + monster.getRealName() + ".json");
            writer.write(obj.toJSONString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveSpell(Card card) {
        SpellCard spell = SpellCard.getSpellCardByName(card.getCardName());
        JSONObject obj = new JSONObject();
        if (card.getNamesForEffect() != null)
            obj.put("namesForEffect", card.getNamesForEffect());
        else obj.put("namesForEffect", new ArrayList<String>());
        obj.put("desctiption", spell.getDescription());
        obj.put("price", spell.getPrice());
        obj.put("spellMode", spell.getSpellMode());
        obj.put("isLimit", spell.checkIsLimit());
        obj.put("name", spell.getRealName());
        obj.put("isOriginal", !spell.isNew());
        try {
            FileWriter writer = new FileWriter("data//spellCards//" + spell.getRealName() + ".json");
            writer.write(obj.toJSONString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveTrap(Card card) {
        TrapCard trap = TrapCard.getTrapCardByName(card.getCardName());
        JSONObject obj = new JSONObject();
        if (card.getNamesForEffect() != null)
            obj.put("namesForEffect", card.getNamesForEffect());
        else obj.put("namesForEffect", new ArrayList<String>());
        obj.put("description", trap.getDescription());
        obj.put("name", trap.getRealName());
        obj.put("isLimit", trap.checkIsLimit());
        obj.put("price", trap.getPrice());
        obj.put("isOriginal", !trap.isNew());
        try {
            FileWriter writer = new FileWriter("data//trapCards//" + trap.getRealName() + ".json");
            writer.write(obj.toJSONString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addAvatarImage(File srcFile, User user) throws IOException {
        int newAvatar = user.getAvatarCount() + 1;
        File desFile =  new File("src//main//Characters//" + user.getName() + ".Chara001.dds" + newAvatar + ".png");
        Files.copy(srcFile.toPath(), desFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    public static void removeDeck(String deckName, User user) {
        File file = new File("data//Decks//" + user.getName() + "//" + deckName + ".json");
        file.delete();
    }
}
