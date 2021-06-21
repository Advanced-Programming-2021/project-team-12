package controllers;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import models.Card;
import models.Deck;
import models.User;
import models.card.monster.Attribute;
import models.card.monster.MonsterCard;
import models.card.monster.MonsterMode;
import models.card.spell.SpellCard;
import models.card.spell.SpellMode;
import models.card.trap.TrapCard;
import org.json.*;
import view.MainMenu;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.io.File;
import java.util.Scanner;

public class LoadFile {
    public LoadFile() {
        loadMonsterCards();
        loadSpellCards();
        loadTrapCards();
        loadUsers();
    }

    private void loadMonsterCards() {
        String monsterJsonData = null;
        File folder = new File("data//monsterCards");
        for (File fileEntry : folder.listFiles()) {
            try {
                Scanner scanner = new Scanner(fileEntry);
                if (scanner.hasNextLine())
                    monsterJsonData = scanner.nextLine();
            } catch (Exception e) {
                e.printStackTrace();
            }
            JSONObject json = new JSONObject(monsterJsonData);
            loadMonsterFromJson(json);
        }
    }

    private void loadMonsterFromJson(JSONObject json) {
        String name = json.getString("name");
        String monsterMode = json.getString("monsterMode");
        String attribute = json.getString("attribute");
        int attack = json.getInt("attack");
        int defence = json.getInt("defence");
        String description = json.getString("description");
        int level = json.getInt("level");
        int price = json.getInt("price");
        Boolean isRitual = json.getBoolean("isRitual");
        new MonsterCard(level, attack, defence, MonsterMode.valueOf(monsterMode)
                , isRitual, name, price, Attribute.valueOf(attribute), description);
        new Card(name, "Monster");
    }

    public void importCard(File file, String flag) {
        String jsonData = null;
        try {
            Scanner scanner = new Scanner(file);
            if (scanner.hasNextLine())
                jsonData = scanner.nextLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject json = new JSONObject(jsonData);
        if (flag.equals("m"))
            loadMonsterFromJson(json);
        else if (flag.equals("s"))
            loadSpellFromJson(json);
        else if (flag.equals("t"))
            loadTrapFromJson(json);
    }

    private void loadSpellCards() {
        String spellJsonData = null;
        File folder = new File("data//spellCards");
        for (File fileEntry : folder.listFiles()) {
            try {
                Scanner scanner = new Scanner(fileEntry);
                if (scanner.hasNextLine())
                    spellJsonData = scanner.nextLine();
            } catch (Exception e) {
                e.printStackTrace();
            }
            JSONObject json = new JSONObject(spellJsonData);
            loadSpellFromJson(json);
        }
    }

    private void loadSpellFromJson(JSONObject json) {
        String name = json.getString("name");
        String desctiption = json.getString("description");
        int price = json.getInt("price");
        String spellMode = json.getString("spellMode");
        Boolean isLimit = json.getBoolean("isLimit");
        new SpellCard(name, SpellMode.valueOf(spellMode), isLimit, price, desctiption);
        new Card(name, "Spell");
    }

    private void loadTrapCards() {
        String trapJsonData = null;
        File folder = new File("data//trapCards");
        for (File fileEntry : folder.listFiles()) {
            try {
                Scanner scanner = new Scanner(fileEntry);
                if (scanner.hasNextLine())
                    trapJsonData = scanner.nextLine();
            } catch (Exception e) {
                e.printStackTrace();
            }
            JSONObject json = new JSONObject(trapJsonData);
            loadTrapFromJson(json);
        }
    }

    private void loadTrapFromJson(JSONObject json) {
        String description = json.getString("description");
        String name = json.getString("name");
        Boolean isLimit = json.getBoolean("isLimit");
        int price = json.getInt("price");
        new TrapCard(name, isLimit, price, description);
        new Card(name, "Trap");
    }

    private void loadUsers() {
        String userJsonData = null;
        File folder = new File("data//User");
        for (File fileEntry : folder.listFiles()) {
            try {
                Scanner scanner = new Scanner(fileEntry);
                if (scanner.hasNextLine())
                    userJsonData = scanner.nextLine();
            } catch (Exception e) {
                e.printStackTrace();
            }
            JSONObject json = new JSONObject(userJsonData);
            loadUserFromJson(json);
        }
    }

    private void loadUserFromJson(JSONObject json) {
        String userName = json.getString("userName");
        String nickName = json.getString("nickName");
        String password = json.getString("password");
        int score = json.getInt("score");
        int money = json.getInt("money");
        Gson gson = new Gson();
        Type stringListType = new TypeToken<ArrayList<String>>(){}.getType();
        ArrayList<String> userCardsName = gson.fromJson(json.get("allCards").toString(), stringListType);
        User user = new User(nickName, userName, password);
        user.setMoney(money);
        user.setScore(score);
        for (String cardName : userCardsName)
            user.addCardToAllCard(Card.getCardByName(cardName));
        loadDecks(user);
    }

    private void loadDecks(User user) {
        String deckJsonData = null;
        File folder = new File("data//Decks//" + user.getName());
        for (File fileEntry : folder.listFiles()) {
            try {
                Scanner scanner = new Scanner(fileEntry);
                if (scanner.hasNextLine())
                    deckJsonData = scanner.nextLine();
            } catch (Exception e) {
                e.printStackTrace();
            }
            JSONObject json = new JSONObject(deckJsonData);
            loadDeckFromJson(json, user);
        }
    }

    private void loadDeckFromJson(JSONObject json, User user) {
        String deckName = json.getString("deckName");
        Boolean isActive = json.getBoolean("isActive");
        Gson gson = new Gson();
        Type StringListType = new TypeToken<ArrayList<String>>(){}.getType();
        ArrayList<String> mainCards = gson.fromJson(json.get("mainCards").toString(), StringListType);
        ArrayList<String> sideCards = gson.fromJson(json.get("sideCards").toString(), StringListType);
        Deck deck = new Deck(deckName, user);
        if (isActive)
            deck.changeIsActive(isActive);
        for (String cardName : mainCards)
            deck.addCard(cardName, "m");
        for (String cardName : sideCards)
            deck.addCard(cardName, "s");
    }
}
