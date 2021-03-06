package view.phase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Exceptions.MyException;
import controllers.BattlePhaseController;
import controllers.Game;
import controllers.PhaseControl;
import models.Address;
import models.Board;
import models.Player;
import view.Main;

public class BattlePhase {
    public Boolean goToNextPhase = false;

    private static BattlePhase instance;

    public static BattlePhase getInstance() {
        if (instance == null) {
            instance = new BattlePhase();
        }
        return instance;
    }

    public void run() {
        Game.setDidWePassBattle(true);
        System.out.println("phase: battle phase");
        Board.showBoard();
        selectCard();
        //Game.playTurn("MainPhase2");
    }

    public void selectCard() {
        String input;
        if (Game.isAITurn())
            AIRun();
        else {
            while (true) {
                input = Main.scanner.nextLine().trim();
                PhaseControl.getInstance().checkIfGameEnded();
                try {
                    if (BattlePhaseController.getInstance().battlePhaseRun(input)) break;
                } catch (MyException e) {
                    System.out.println(e.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //Game.playTurn("MainPhase2");
        }
    }

    private void AIRun() {
        int place = getStrongestMonster(Game.whoseTurnPlayer());
        if (place != 0) {
            PhaseControl.getInstance().checkIfGameEnded();
            try {
                int place1 = getWeakestMonster(Game.whoseRivalPlayer());
                if (place1 == 0)
                    BattlePhaseController.getInstance().directAttack(new Address(place, "monster", true));
                else
                BattlePhaseController.getInstance().attack(new Address(place1
                        , "monster", false), new Address(place, "monster", true));
            } catch (MyException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //Game.playTurn("MainPhase2");
    }

    public int getStrongestMonster(Player player) {
        int maxAttack = 0;
        int place = 0;
        for (int i = 1; i < 6; i++) {
            if (player.getMonsterZoneCard().containsKey(i) && player.getCardMonster(i).getAttack() > maxAttack) {
                place = i;
                maxAttack = player.getCardMonster(i).getAttack();
            }
        }
        return place;
    }

    public int getWeakestMonster(Player player) {
        int minAttack = 0;
        int place = 0;
        for (int i = 1; i < 6; i++) {
            if (player.getMonsterZoneCard().containsKey(i) && player.getCardMonster(i).getAttack() < minAttack) {
                place = i;
                minAttack = player.getCardMonster(i).getAttack();
            }
        }
        return place;
    }
}
