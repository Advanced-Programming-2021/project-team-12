package models.card.monster.monster_effect;

import models.card.monster.monster_effect.MonsterEffect;

public class BringLevel7OrMoreFromGraveyard implements MonsterEffect {
    public void run(){

    }

    @Override
    public int attackEffect(int attack) {
        return 0;
    }

    @Override
    public int defenceEffect(int defence) {
        return 0;
    }
}
