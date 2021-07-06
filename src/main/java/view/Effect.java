package view;


import controllers.Game;
import view.phase.BattlePhase;

public class Effect {
    public static String AIEffect(String cardName) {
        if(cardName.equals("Suijin"))
            return "yes";
        if(cardName.equals("Man-Eater Bug"))
            return Integer.toString(BattlePhase.getInstance().getStrongestMonster(Game.whoseRivalPlayer()));
        if(cardName.equals("Scanner"))
            return "1";
        if(cardName.equals("Beast King Barbaros"))
           return "2";
        if(cardName.equals("Herald of Creation1"))
            return "0";
        if(cardName.equals("Herald of Creation2"))
            Game.getGameView().scanForTribute(1);
        if(cardName.equals("Herald of Creation3"))
            return "0";
        if(cardName.equals("Terratiger, the Empowered Warrior"))
            return "0";
        if(cardName.equals("Terraforming"))
            return "Umiiruka";
        if(cardName.equals("Twin Twisters"))
            return "1"+","+"2"+","+"3";
        if(cardName.equals("Mystical space typhoon"))
            return "1";
        else return "0";
    }
}
