package card.monster;

import card.monster.monster_effect.MonsterEffect;
//import card.spell.SpellCard;

import java.util.ArrayList;

public class MonsterCard {
    private int level;
    private int attack;
    private int defence;
    private String effect;
    private MonsterMode monsterMode;
    private boolean isRitual;
    private String name;
    private int price;
    private Attribute attribute;
    private MonsterEffect monsterEffect;
    private static ArrayList<MonsterCard> monsterCards;

    static {
        monsterCards = new ArrayList<>();
    }

    public MonsterCard(int level, int attack, int defence, String effect, MonsterMode monsterMode,
                       boolean isRitual, String name, int price, Attribute attribute,MonsterEffect monsterEffect) {
    this.level=level;
    this.attack=attack;
    this.defence=defence;
    this.effect=effect;
    this.monsterMode=monsterMode;
    this.isRitual=isRitual;
    this.name=name;
    this.price=price;
    this.attribute=attribute;
    this.monsterEffect=monsterEffect;
    monsterCards.add(this);
    }
    public void runEffect(){
    this.monsterEffect.run();
    }
    public int getLevel(){
        return level;
    }
    public int getAttack(){
        return attack;
    }
    public int getDefence(){
        return defence;
    }
    public int getPrice(){
        return price;
    }
    public String getEffect(){
        return effect;
    }
    public String getName(){
        return name;
    }

    public MonsterMode getMonsterMode() {
        return monsterMode;
    }

    public static ArrayList<MonsterCard> getMonsterCards() {
        return monsterCards;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public boolean checkIsRitual() {
        return isRitual;
    }
    public MonsterCard getMonsterCardByName(String name){
        for (MonsterCard monsterCard : monsterCards)
            if (monsterCard.name.equals(name)) return monsterCard;
        return null;
    }
}
