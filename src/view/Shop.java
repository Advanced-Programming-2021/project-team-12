package view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import models.Card;
import models.User;

public class Shop {
    private User user;
    public Shop() {
        this.user = MainMenu.user;
        String input;
        while (true) {
            input = Main.scanner.nextLine().trim();
            if (input.compareTo("menu exit") == 0) 
                return;
            else if (input.matches("shop buy [\\w]+"))
                buyCard(input);
            else if (input.matches("shop show (--all|-a)"))
                showCards();
            else if (input.matches("menu show-current"))
                System.out.println("Shop");
            else 
                System.out.println("invalid command!");
        }
    }

    public void buyCard(String input) {
        String cardName;
        Matcher matcher = getCommandMatcher(input, "shop buy ([\\w]+)");
        matcher.find();
        cardName = matcher.group(1);
        Card card = Card.getCardByName(cardName);
        if (card == null)
            System.out.println("there is no card with this name");
        else if (user.getMoney() < card.getPrice())
            System.out.println("not enough money");
        else {
            user.decreaseMoney(card.getPrice());
            user.addCardToAllCard(card);
        }

    }

    public void showCards() {
        ArrayList<Card> allCards;
        allCards = Card.getAllCards(); 
        for (Card card : allCards) 
            System.out.println(card.getCardName() + ":" + " " + card.getDescription());
    }

    public static Matcher getCommandMatcher(String input, String regex) {
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(input);
        return matcher;
    }
}
