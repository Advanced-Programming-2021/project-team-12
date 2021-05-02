package models;
import java.util.ArrayList;

public class User {
    private String userName;
    private String nickName;
    private String passowrd;
    private int score;
    private ArrayList<Card> allCards;
    private ArrayList<Card> secondaryDeck;
    private ArrayList<Card> activeCards;
    private int money;
    private static ArrayList<User> users = new ArrayList<>();

    public User(String nickName, String userName, String password) {
        this.userName = userName;
        this.nickName = nickName;
        this.passowrd = password;
        users.add(this);
    }

    public ArrayList<Card> getActiveCards() {
        return activeCards;
    }

    public ArrayList<Card> getAllCards() {
        return allCards;
    }

    public String getName() {
        return userName;
    }

    public void setPassword(String password) {
        this.passowrd = password;
    }

    public boolean checkPassword(String password) {
        if (this.passowrd.equals(password))
            return true;
        return false;
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

    public static ArrayList<User> getUsers(){
        return users;
    }

    public int getScore() {
        return score;
    }

}
