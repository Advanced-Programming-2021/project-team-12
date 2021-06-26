package view;

import Exceptions.MyException;
import Utility.CommandMatcher;
import controllers.AIController;
import controllers.Game;
import controllers.SetMainAndSlide;
import models.Card;
import models.Player;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class SetMainCards {
    public SetMainCards(Player firstPlayer, Player secondPlayer) {
        setCards(firstPlayer, "First");
        if (!Game.isAITurn())
            setCards(secondPlayer, "Second");
        else
            new SetMainAndSlide().setAICard();
    }

    private void setCards(Player player, String flag) {
        System.out.println("Set Main Card Of " + flag + " Player:");
        System.out.println("--- add slide card (slide number) to main card ---");
        System.out.println("--- add main card (main number) to slide card ---");
        System.out.println("--- show main and slide deck ---");
        System.out.println("--- end ---\n");
        while (true) {
            String input = Main.scanner.nextLine().trim();
            if (input.compareTo("end") == 0)
                break;
            else if (input.matches("add slide card [\\d]+ to main card"))
                addSlideToMain(input, player);
            else if (input.matches("add main card [\\d]+ to slide card"))
                addMainToSlide(input, player);
            else if (input.matches("show main and slide deck"))
                showDeck(player);
            else
                System.out.println("invalid command!");
        }
        player.setHandCard();
    }

    private void addMainToSlide(String input, Player player) {
        Matcher matcher;
        int mainNumber;
        matcher = CommandMatcher.getCommandMatcher(input.trim(), "add main card ([\\d]+) to slide card");
        matcher.find();
        mainNumber = Integer.parseInt(matcher.group(1));
        try {
            new SetMainAndSlide().setMainToSlide(mainNumber, player);
            System.out.println("main card " + mainNumber + "added to slide deck successfully");
        } catch (MyException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addSlideToMain(String input, Player player) {
        Matcher matcher;
        int slideNumber;
        matcher = CommandMatcher.getCommandMatcher(input.trim(), "add slide card ([\\d]+) to main card");
        matcher.find();
        slideNumber = Integer.parseInt(matcher.group(1));
        try {
            new SetMainAndSlide().setSlideToMain(slideNumber, player);
            System.out.println("slide card " + slideNumber + "added to main deck successfully");
        } catch (MyException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDeck(Player player) {
        ArrayList<Card> cards;
        cards = player.getMainCards();
        System.out.println("Main Deck:");
        for (int i = 1; i <= cards.size(); i++)
            System.out.println(i + ": " + cards.get(i - 1).getCardName());
        cards = player.getSideCards();
        System.out.println("Side deck: ");
        for (int i = 1; i <= cards.size(); i++)
            System.out.println(i + ": " + cards.get(i - 1).getCardName());
    }
}
