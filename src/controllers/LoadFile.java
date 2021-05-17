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
        int number = 0;
        String monsterJsonData = null;
        File file = new File("data//monsterCards" + number + ".json");
        while (file.exists() && !file.isDirectory()) {
            try {
                Scanner scanner = new Scanner(file);
                if (scanner.hasNextLine())
                    monsterJsonData = scanner.nextLine();
            } catch (Exception e) {
                e.printStackTrace();
            }
            JSONObject json = new JSONObject(monsterJsonData);
            loadMonsterFromJson(json);
            number++;
            file = new File(number + ".json");
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
    }

    private void loadSpellCards() {
        int number = 0;
        String spellJsonData = null;
        File file = new File( "data//spellCards" + number + ".json");
        while (file.exists() && !file.isDirectory()) {
            try {
                Scanner scanner = new Scanner(file);
                if (scanner.hasNextLine())
                    spellJsonData = scanner.nextLine();
            } catch (Exception e) {
                e.printStackTrace();
            }
            JSONObject json = new JSONObject(spellJsonData);
            loadSpellFromJson(json);
            number++;
            file = new File("data//spellCards" + number + ".json");
        }
    }

    private void loadSpellFromJson(JSONObject json) {
        String name = json.getString("name");
        String desctiption = json.getString("desctiption");
        int price = json.getInt("price");
        String spellMode = json.getString("spellMode");
        Boolean isLimit = json.getBoolean("isLimit");
        new SpellCard(name, SpellMode.valueOf(spellMode), isLimit, price, desctiption);
    }

    private void loadTrapCards() {
        int number = 0;
        String trapJsonData = null;
        File file = new File("data//trapCards" + number + ".json");
        while (file.exists() && !file.isDirectory()) {
            try {
                Scanner scanner = new Scanner(file);
                if (scanner.hasNextLine())
                    trapJsonData = scanner.nextLine();
            } catch (Exception e) {
                e.printStackTrace();
            }
            JSONObject json = new JSONObject(trapJsonData);
            loadTrapFromJson(json);
            number++;
            file = new File("data//trapCards" + number + ".json");
        }
    }

    private void loadTrapFromJson(JSONObject json) {
        String description = json.getString("description");
        String name = json.getString("name");
        Boolean isLimit = json.getBoolean("isLimit");
        int price = json.getInt("price");
        new TrapCard(name, isLimit, price, description);
    }

    private void loadUsers() {
        int number = 0;
        String userJsonData = null;
        File file = new File("data//User" + number + ".json");
        while (file.exists() && !file.isDirectory()) {
            try {
                Scanner scanner = new Scanner(file);
                if (scanner.hasNextLine())
                    userJsonData = scanner.nextLine();
            } catch (Exception e) {
                e.printStackTrace();
            }
            JSONObject json = new JSONObject(userJsonData);
            loadUserFromJson(json);
            number++;
            file = new File(number + ".json");
        }
    }

    private void loadUserFromJson(JSONObject json) {
        String userName = json.getString("userName");
        String nickName = json.getString("nickName");
        String password = json.getString("password");
        int score = json.getInt("score");
        int money = json.getInt("money");
        Gson gson = new Gson();
        Type StringListType = new TypeToken<ArrayList<String>>(){}.getType();
        ArrayList<String> userCardsName = gson.fromJson(json.get("allCards").toString(), StringListType);
        User user = new User(nickName, userName, password);
        user.setMoney(money);
        user.setScore(score);
        for (String cardName : userCardsName)
            user.addCardToAllCard(Card.getCardByName(cardName));
        loadDecks(user);
    }

    private void loadDecks(User user) {
        int number = 0;
        String deckJsonData = null;
        File file = new File("data//Decks" + user.getName() + ".json");
        if (file.exists() && !file.isDirectory()) {
            try {
                Scanner scanner = new Scanner(file);
                if (scanner.hasNextLine())
                    deckJsonData = scanner.nextLine();
            } catch (Exception e) {
                e.printStackTrace();
            }
            JSONObject json = new JSONObject(deckJsonData);
            loadDeckFromJson(json, user);
            number++;
            file = new File("data//trapCards" + number + ".json");
        }
    }

    private void loadDeckFromJson(JSONObject json, User user) {
        int deckNumber = 0;
        while(true) {
            deckNumber++;
            if (!json.has("deckName" + deckNumber))
                break;
            String deckName = json.getString("deckName");
            Boolean isActive = json.getBoolean("isActive");
            Gson gson = new Gson();
            Type StringListType = new TypeToken<ArrayList<String>>(){}.getType();
            ArrayList<String> mainCards = gson.fromJson(json.get("mainCards" + deckNumber).toString(), StringListType);
            ArrayList<String> sideCards = gson.fromJson(json.get("sideCards" + deckNumber).toString(), StringListType);
            Deck deck = new Deck(deckName);
            deck.changeIsActive(isActive);
            for (String cardName : mainCards)
                deck.addCard(cardName, "m");
            for (String cardName : sideCards)
                deck.addCard(cardName, "s");
        }
    }


}
