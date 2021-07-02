package test;

import controllers.Game;
import controllers.LoadFile;
import controllers.PhaseControl;
import models.*;
import org.junit.jupiter.api.*;
import Utility.CommandMatcher;

import java.util.regex.Matcher;

public class GeneralTest {
    static User user;
    static Player currentPlayer;
    static Player rivalPlayer;
    static Matcher matcher;
    static Address address;
    static Card card;
    static Deck deck;
    @BeforeAll
    public static void init() {
        new LoadFile();
        user = new User("a", "a", "a");
        currentPlayer = new Player();
        matcher = CommandMatcher.getCommandMatcher("select --hand 2", "(^[ ]*select --hand [\\d]+[ ]*$)");
        address = new Address("select --hand 2");
        card = new Card("Battle OX", "Monster");
        Game.firstPlayer = currentPlayer;
        Game.secondPlayer = rivalPlayer;
//        deck = new Deck("asas", user);
//        deck.setAcctive();
//        ArrayList<Card> cards = new ArrayList<>();
//        for (int i = 0; i < 60; i++) deck.addCard("Battle Ox","m");
    }

    @Test
    public static void summonALowLevelMonsterTest() {
        currentPlayer.getHandCard().put(2, card);
        PhaseControl.getInstance().summonALowLevelMonster(currentPlayer, address);
        Assertions.assertTrue(currentPlayer.isHeSummonedOrSet());
    }

}
