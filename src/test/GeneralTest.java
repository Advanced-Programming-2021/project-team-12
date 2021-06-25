package test;

import controllers.LoadFile;
import controllers.PhaseControl;
import models.*;
import org.junit.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class GeneralTest {
    static User user;
    static Player player;
    static Matcher matcher;
    static Address address;
    static Card card;
    static Deck deck;
    @Before
    public void init() {
        new LoadFile();
        user = new User("a", "a", "a");
        player = new Player();
        matcher = Utility.CommandMatcher.getCommandMatcher("select --hand 2", "(^[ ]*select --hand [\\d]+[ ]*$)");
        address = new Address("select --hand 2");
        card = new Card("Battle Ox", "Monster");
        deck = new Deck("asas", user);
        deck.setAcctive();
        ArrayList<Card> cards = new ArrayList<>();
        for (int i = 0; i < 60; i++) deck.addCard("Battle Ox","m");
    }

    @Test
    public void summonALowLevelMonsterTest() {
        player.getHandCard().put(2, card);
        PhaseControl.getInstance().summonALowLevelMonster(matcher, player, address);
        Assert.assertTrue(player.isHeSummonedOrSet());
    }

}
