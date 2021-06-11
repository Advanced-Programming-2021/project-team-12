package models.card.monster.monster_effect;

public interface MonsterEffect {
    public void run();
    public int attackEffect(int attack);
    public int defenceEffect(int defence);
}
