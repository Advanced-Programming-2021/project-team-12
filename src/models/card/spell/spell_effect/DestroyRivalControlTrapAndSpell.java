package models.card.spell.spell_effect;

import models.Board;
import view.Game;

public class DestroyRivalControlTrapAndSpell implements SpellEffect{
    public void run(){
        Game.whoseTurnPlayer().destroyAllRivalTrapAndSpells();
    }
}
