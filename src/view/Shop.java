package view;

import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.regex.Matcher;

import Exceptions.MyException;
import Utility.CommandMatcher;
import controllers.ShopControl;
import models.Card;
import models.User;

public class Shop {
    private User user;

    public Shop() {
        System.out.println(" ** SHOP **");
        this.user = MainMenu.user;
        String input;
        while (true) {
            input = Main.scanner.nextLine().trim();
            if (input.compareTo("menu exit") == 0)
                return;
            else if (input.matches("increase (--money|-m) [\\d]+"))
                cheatyIncreaseMoney(input);
            else if (input.matches("shop buy [\\w ]+"))
                buyCard(input);
            else if (input.matches("shop show (--all|-a)"))
                showCards();
            else if (input.matches("menu show-current"))
                System.out.println("Shop");
            else
                System.out.println("invalid command!");
        }
    }

    private void cheatyIncreaseMoney(String input) {
        Matcher matcher = CommandMatcher.getCommandMatcher(input, "increase (--money|-m) ([\\d]+)");
        matcher.find();
        int money = Integer.parseInt(matcher.group(2));
        new ShopControl().cheatyIncreaseMoney(money);
        System.out.println("money added successfully");
    }

    public void buyCard(String input) {
        String cardName;
        Matcher matcher = CommandMatcher.getCommandMatcher(input, "shop buy ([\\w ]+)");
        matcher.find();
        cardName = matcher.group(1).trim();
        try {
            new ShopControl().buyCard(cardName, user);
            System.out.println("you bought card successfully");
        } catch (MyException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showCards() {
        ArrayList<Card> allCards;
        allCards = Card.getAllCards();
        for (int i = 0; i < allCards.size(); i++)
            System.out.println(i + ". " +allCards.get(i).getCardName() + ":" + " " + allCards.get(i).getDescription());
    }

}
