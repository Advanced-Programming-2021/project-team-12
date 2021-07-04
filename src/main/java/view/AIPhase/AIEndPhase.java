package view.AIPhase;

import Utility.CommandMatcher;
import controllers.Game;
import controllers.PhaseControl;
import models.Address;
import models.Player;
import view.Main;

import java.util.regex.Matcher;

public class AIEndPhase {
    public void run() {
        PhaseControl.getInstance().doEffectEndPhase();
        System.out.println("phase: end phase");
        if (Game.whoseTurnPlayer().howManyCardIsInTheHandCard() == 6) {
            Player player = Game.whoseTurnPlayer();
            int place = 0;
            int minAttack = 1000000;
            for (int i = 1; i <=6 ; i++) {
                if (player.getCardHand(i).getKind().equals("Monster")) {
                    if (player.getCardHand(i).getAttack() < minAttack) {
                        place = i;
                        minAttack = player.getCardHand(i).getAttack();
                    }
                }
            }
            if (place == 0)
                place = 6;
            Address address = new Address(place, "hand", true);
            Game.whoseTurnPlayer().removeCard(address);
        }
        PhaseControl.getInstance().checkIfGameEnded();
        PhaseControl.getInstance().switchPlayerTurn();
        //Game.playTurn("DrawPhase");
    }
}
