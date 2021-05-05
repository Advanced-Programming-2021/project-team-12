package models.card.spell.spell_effect;

import controllers.move.Attack;

public class DestroyRivalControlMonsters implements SpellEffect{
    public void run(){
        Attack.destroyAllRivalMonsters();
    }
}
