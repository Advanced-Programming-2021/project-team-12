package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class User {
    private String userName;
    private String nickName;
    private String password;
    private int score;
    private ArrayList<Card> allCards = new ArrayList<>();
    private int money;
    private static ArrayList<User> users = new ArrayList<>();

    public User(String nickName, String userName, String password) {
        this.userName = userName;
        this.nickName = nickName;
        this.password = password;
        score = 0;
        money = 180000;
        users.add(this);
    }

    public User() {
        this.userName = "AI";
        this.nickName = "AI";
        this.password = "AI";
        score = 0;
        money = 180000;
    }

    public void increaseScore(int score) {
        this.score += score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Boolean checkIfHasCard(Card card) {
        return allCards.contains(card);
    }

    public ArrayList<Card> getAllCards() {
        if (allCards == null) return  null;
        allCards.sort(Comparator.comparing(Card::getCardName));
        return allCards;
    }

    public String getName() {
        return userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void increaseMoney(int amount) {
        money += amount;
    }

    public void decreaseMoney(int amount) {
        money -= amount;
    }

    public String getNickName() {
        return nickName;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public static User getUserByName(String userName) {
        for (User user : users)
            if (user.getName().equals(userName))
                return user;
        return null;
    }

    public static User getUserByNickName(String nickName) {
        for (User user : users)
            if (user.getNickName().equals(nickName))
                return user;
        return null;
    }

    public void addCardToAllCard(Card card) {
        allCards.add(card);
    }

    public static ArrayList<User> getUsers() {
        users.sort((u1, u2) -> {
            if (u1.getScore() != u2.getScore())
                return (u1.getScore() > u2.getScore()) ? -1 : 1;
            else
                return u1.getNickName().compareTo(u2.getNickName());
        });
        return users;
    }

    public void removeCardFromAllCard (Card card) {
        allCards.remove(card);
    }

    public String getPassword() {
        return password;
    }

    public int getScore() {
        return score;
    }
}
