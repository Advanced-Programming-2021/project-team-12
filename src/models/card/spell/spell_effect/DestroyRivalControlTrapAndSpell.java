package models.card.spell.spell_effect;

import models.Board;
import controllers.Game;

public class DestroyRivalControlTrapAndSpell implements SpellEffect{
    public void run(){
        Board.destroyAllTrapAndSpells(Game.whoseRivalPlayer());
    }
}
