package models.card.spell.spell_effect;

import models.Board;

public class DestroyRivalControlTrapAndSpell implements SpellEffect{
    public void run(){
        Board.destroyAllRivalTrapAndSpells();
    }
}
