package models.card.spell.spell_effect;

import controllers.move.Attack;

public class DestroyAllMonsters implements SpellEffect{
    public void run(){
       Attack.destroyAllMonsters();
    }
}
