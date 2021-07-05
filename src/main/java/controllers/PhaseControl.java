package controllers;

import controllers.move.Attack;
import controllers.move.SetSpell;
import models.*;
import Exceptions.*;
import Utility.CommandMatcher;
import models.card.monster.MonsterCard;
import models.card.spell.SpellCard;
import models.card.spell.SpellMode;
import models.card.trap.TrapCard;
import view.Effect;
import view.phase.BattlePhase;
import view.phase.MainPhase;

import java.util.regex.Matcher;

public class PhaseControl {
    private static PhaseControl instance;
    private boolean canDraw = true;

    public static PhaseControl getInstance() {
        if (instance == null) {
            instance = new PhaseControl();
        }
        return instance;
    }

    public void setCanDraw(boolean canDraw) {
        this.canDraw = canDraw;
    }

    public void payMessengerOfPeaceSpellCardHarm(String answer) {
        if (answer.equals("yes")) SetSpell.destroyMessengerOfPeace();
        else Game.whoseTurnPlayer().decreaseLP(100);
    }

    public void resetMoves() {
        Game.firstPlayer.setHeSummonedOrSet(false);
        Game.secondPlayer.setHeSummonedOrSet(false);
        Game.firstPlayer.setDidWeChangePositionThisCardInThisTurnCompletelyFalse();
        Game.secondPlayer.setDidWeChangePositionThisCardInThisTurnCompletelyFalse();
        Game.firstPlayer.setDidWeAttackByThisCardInThisCardInThisTurnCompletelyFalse();
        Game.secondPlayer.setDidWeAttackByThisCardInThisCardInThisTurnCompletelyFalse();
        Game.firstPlayer.setOneHisMonstersDestroyedInThisRound(false);
        Game.secondPlayer.setOneHisMonstersDestroyedInThisRound(false);
    }

    public static void checkIfGameEnded() {
        if (Game.firstPlayer.getLP() <= 0) {
            Game.setWinner(Game.secondPlayer);
            //Game.playTurn("EndGame");
        } else if (Game.secondPlayer.getLP() <= 0) {
            Game.setWinner(Game.firstPlayer);
            //Game.playTurn("EndGame");
        }
    }

    public String drawOneCard() throws Exception {
        if (!canDraw) {
            canDraw = true;
            return "You can't draw a card because of rival's Time Seal";
        }
        if (Game.playerTurn == PlayerTurn.FIRSTPLAYER) {
            String newAddedCard = Game.firstPlayer.addCardFromUnusedToHand();
            if (newAddedCard.equals("unused is empty")) {
                Game.setWinner(Game.secondPlayer);
                return "GAME OVER\n" + Game.whoseTurnPlayer() + " cannot draw a card, " + Game.whoseRivalPlayer() + " is the winner";
            } else if (!newAddedCard.equals("Hand is full")) {
                return "A new card has been added to " + Game.whoseTurnPlayer().getNickName() + "'s hand : " + newAddedCard;
            }
        } else {
            String newAddedCard = Game.secondPlayer.addCardFromUnusedToHand();
            if (newAddedCard.equals("unused is empty")) {
                Game.setWinner(Game.firstPlayer);
                return "GAME OVER\n" + Game.whoseTurnPlayer() + " cannot draw a card, " + Game.whoseRivalPlayer() + " is the winner";
            } else if (!newAddedCard.equals("Hand is full")) {
                return "A new card has been added to " + Game.whoseTurnPlayer().getNickName() + "'s hand : " + newAddedCard;
            }
        }
        return null;
    }

    public void doEffectEndPhase() {
        if (Game.firstPlayer.isOneHisSupplySquadActivated()) {
            if (Game.firstPlayer.isOneHisMonstersDestroyedInThisRound())
                if (!Game.firstPlayer.isHandFull()) Game.firstPlayer.addCardFromUnusedToHand();
        }
        if (Game.secondPlayer.isOneHisSupplySquadActivated()) {
            if (Game.secondPlayer.isOneHisMonstersDestroyedInThisRound())
                if (!Game.secondPlayer.isHandFull()) Game.secondPlayer.addCardFromUnusedToHand();
        }
    }

    public void switchPlayerTurn() {
        if (Game.playerTurn == PlayerTurn.FIRSTPLAYER) {
            Game.playerTurn = PlayerTurn.SECONDPLAYER;
        } else {
            Game.playerTurn = PlayerTurn.FIRSTPLAYER;
        }
    }

    public String printWhoseTurnIsIt() {
        if (Game.playerTurn == PlayerTurn.FIRSTPLAYER) {
            return "its " + Game.firstPlayer.getNickName() + "’s turn";
        } else {
            return "its " + Game.secondPlayer.getNickName() + "’s turn";
        }
    }

    public void whatIsSet(Address address) {
        String whatKind = Game.whoseTurnPlayer().whatKindaCardIsInThisAddress(address);
        if (whatKind.equals("Monster")) {
            Game.getMainPhase1().setMonster(address);
        } else if (whatKind.equals("Trap")) {
            Game.getMainPhase1().setTrap(address);
        } else if (whatKind.equals("Spell")) {
            Game.getMainPhase1().setSpell(address);
        }
    }

    public void monsterSet(Address address) throws MyException {
        if (!Game.whoseTurnPlayer().isMonsterZoneFull()) {
            if (!Game.whoseTurnPlayer().isHeSummonedOrSet()) {
                if (Game.whoseTurnPlayer().getMonsterCardByAddress(address).getName().equals("Scanner")) {
                    Game.whoseTurnPlayer().setCardFromHandToMonsterZone(address).setIsScanner(true);
                } else Game.whoseTurnPlayer().setCardFromHandToMonsterZone(address);
                Game.whoseTurnPlayer().setHeSummonedOrSet(true);
            } else {
                throw new MyException("you already summoned/set on this turn!");
            }
        } else {
            throw new MyException("monster card zone is full!");
        }
    }

    public void trapSet(Address address) throws MyException {
        Player currentPlayer = Game.whoseTurnPlayer();
        if (!currentPlayer.isSpellZoneFull()) {
            activateSomeOfTraps(address, currentPlayer.getTrapCardByAddress(address), currentPlayer);
            currentPlayer.setCardFromHandToSpellZone(address);
        } else throw new MyException("spell card zone is full!");
    }

    private void activateSomeOfTraps(Address address, TrapCard trapCard, Player currentPlayer) {
        if (!Game.whoseRivalPlayer().doIHaveMirageDragonMonster()) {
            if (trapCard.getName().equals("Mind Crush")) {
                if (BattlePhase.getInstance().getPermissionForTrap("Mind Crush", true)) {
                    MainPhase.doMindCrushEffect();
                    currentPlayer.removeCard(address);
                }
            }
            if (trapCard.getName().equals("Time Seal")) {
                if (BattlePhase.getInstance().getPermissionForTrap("Time Seal", true)) {
                    PhaseControl.getInstance().setCanDraw(false);
                    currentPlayer.removeCard(address);
                }
            }
            if (trapCard.getName().equals("Call of The Haunted")) {
                if (BattlePhase.getInstance().getPermissionForTrap("Call of The Haunted", true)) {
                    if (Game.isAITurn())
                        AISummonFromGraveyard();
                    else
                        MainPhase.doCallOfTheHauntedEffect(true);
                    currentPlayer.removeCard(address);
                }
            }
        }
    }

    private void AISummonFromGraveyard() {
        Player player = Game.whoseTurnPlayer();
        int place = 0;
        int maxAttack = -1;
        for (int i = 1; i <= player.getGraveyardCard().size(); i++) {
            if (player.getGraveyardCard().containsKey(i) && player.getGraveyardCard().get(i).getKind().equals("Monster")
                    && player.getGraveyardCard().get(i).getAttack() >= maxAttack) {
                place = i;
                maxAttack = player.getGraveyardCard().get(i).getAttack();
            }
        }
        if (place == 0)
            return;
        else
            Board.summonThisCardFromGraveYardToMonsterZone(new Address(place, "graveyard", true));
    }

    public void spellSet(Address address) throws MyException {
        Player currentPlayer = Game.whoseTurnPlayer();
        SpellCard spellCard = currentPlayer.getSpellCardByStringAddress(address);
        if (spellCard.getSpellMode().equals(SpellMode.FIELD)) {
            Address address1 = new Address(1, "field", true);
            currentPlayer.removeCard(address1);
            currentPlayer.setCardFromHandToFieldZone(address);
        } else {
            if (!currentPlayer.isSpellZoneFull()) {
                currentPlayer.setCardFromHandToSpellZone(address);
            } else throw new MyException("spell zone is full!");
        }
    }

    public void summonControl(Address address) throws MyException {
        Player currentPlayer = Game.whoseTurnPlayer();
        if (currentPlayer.getMonsterCardByStringAddress(address) == null)
            throw new MyException("you cant summon spell or trap");
        if (!currentPlayer.isMonsterZoneFull()) {
            if (!currentPlayer.isHeSummonedOrSet()) {
                MonsterCard monsterCard = Game.whoseTurnPlayer().getMonsterCardByStringAddress(address);
                int level = monsterCard.getLevel();
                if (!currentPlayer.getMonsterCardByStringAddress(address).isRitual()) {
                    if (monsterCard.getName().equals("Gate Guardian")) {
                        if (!MainPhase.doSolemnWarningEffect(address))
                            Game.getMainPhase1().summonForTribute(3, address);
                    } else if (!MainPhase.doSolemnWarningEffect(address)) {
                        if (level <= 4) summonALowLevelMonster(currentPlayer, address);
                        else if (level <= 6) Game.getMainPhase1().summonForTribute(1, address);
                        else {
                            int index = Game.whoseTurnPlayer().getIndexOfThisCardByAddress(address);
                            if (Game.whoseTurnPlayer().getMonsterCardByAddress(address).getName().equals("Beast King Barbaros")
                                    && (Integer.parseInt(Effect.run("Beast King Barbaros")) == 3)) {
                                Game.whoseTurnPlayer().setDidBeastKingBarbarosSummonedSuperHighLevel(true, index);
                                Game.getMainPhase1().summonForTribute(3, address);
                            } else Game.getMainPhase1().summonForTribute(2, address);
                        }
                    } else throw new MyException("opponent solemn destroyed your monster");
                } else Game.getMainPhase1().ritualSummon(address, level);
            } else throw new MyException("you already summoned/set on this turn!");
        } else throw new MyException("monster card zone is full!");
    }

    public void summonALowLevelMonster(Player currentPlayer, Address address) {
        MonsterCard monsterCard = currentPlayer.getMonsterCardByAddress(address);
        if (currentPlayer.getMonsterCardByAddress(address).getName().equals("Scanner")) {
            currentPlayer.summonCardToMonsterZone(address).setIsScanner(true);
        } else if (currentPlayer.getMonsterCardByAddress(address).getName().equals("Terratiger, the Empowered Warrior")) {
            if (Integer.parseInt(Effect.run("Terratiger, the Empowered Warrior")) != 0) {
                Address address1 = new Address(Integer.parseInt(Effect.run("Terratiger, the Empowered Warrior")), "hand", true);
                if ((currentPlayer.getMonsterCardByAddress(address1) != null)
                        && (currentPlayer.getMonsterCardByAddress(address1).getLevel() <= 4)
                        && (!currentPlayer.isMonsterZoneFull()))
                    currentPlayer.setCardFromHandToMonsterZone(address1);
            }
        } else currentPlayer.summonCardToMonsterZone(address);
        if (Game.whoseRivalPlayer().doIHaveSpellCard("Trap Hole") && !Game.whoseTurnPlayer().doIHaveMirageDragonMonster()) {
            if (monsterCard.getNormalAttack() >= 1000) {
                currentPlayer.removeCard(address);
                Game.whoseRivalPlayer().removeOneOfTrapOrSpell("Trap Hole");
            }
        }
        if (Game.whoseRivalPlayer().doIHaveSpellCard("Torrential Tribute") && !Game.whoseTurnPlayer().doIHaveMirageDragonMonster()) {
            Attack.destroyAllMonstersInTheBoard();
            Game.whoseRivalPlayer().removeOneOfTrapOrSpell("Torrential Tribute");
        }
        currentPlayer.setHeSummonedOrSet(true);
    }

    public void summonAMediumLevelMonster(Address address) throws MyException {
        Player currentPlayer = Game.whoseTurnPlayer();
        MonsterCard monsterCard = currentPlayer.getMonsterCardByAddress(address);
        if (currentPlayer.isThereAnyCardInMonsterZone()) {
            String tributeCard = Game.getMainPhase1().getTributeCard();
            if (tributeCard.equals("cancel")) {
                throw new MyException("canceled successfully!");
            } else {
                if (currentPlayer.isMonsterInThisMonsterZoneTypeAddress(Integer.parseInt(tributeCard))) {
                    currentPlayer.setHeSummonedOrSet(true);
                    currentPlayer.removeMonsterByInt(Integer.parseInt(tributeCard));
                    currentPlayer.summonCardToMonsterZone(address);
                    if (Game.whoseRivalPlayer().doIHaveSpellCard("Trap Hole") && !Game.whoseTurnPlayer().doIHaveMirageDragonMonster()) {
                        if (monsterCard.getNormalAttack() >= 1000) {
                            currentPlayer.removeCard(address);
                            Game.whoseRivalPlayer().removeOneOfTrapOrSpell("Trap Hole");
                        }
                    }
                    if (Game.whoseRivalPlayer().doIHaveSpellCard("Torrential Tribute") && !Game.whoseTurnPlayer().doIHaveMirageDragonMonster()) {
                        Attack.destroyAllMonstersInTheBoard();
                        Game.whoseRivalPlayer().removeOneOfTrapOrSpell("Torrential Tribute");
                    }
                } else throw new MyException("There is no monster in this address!");
            }
        } else throw new MyException("there are not enough cards for tribute");
    }

    public void summonAHighLevelMonster(Address address) throws MyException {
        Player currentPlayer = Game.whoseTurnPlayer();
        MonsterCard monsterCard = currentPlayer.getMonsterCardByAddress(address);
        if (Game.whoseTurnPlayer().isThereTwoCardInMonsterZone()) {
            String tributeCard1 = Game.getMainPhase1().scanForTribute(1);
            if (tributeCard1.equals("cancel")) {
                throw new MyException("canceled successfully!");
            } else {
                String tributeCard2 = Game.getMainPhase1().scanForTribute(2);
                if (tributeCard2.equals("cancel")) {
                    throw new MyException("canceled successfully!");
                } else {
                    if (Game.whoseTurnPlayer().isMonsterInThisMonsterZoneTypeAddress(Integer.parseInt(tributeCard1))
                            && Game.whoseTurnPlayer().isMonsterInThisMonsterZoneTypeAddress(Integer.parseInt(tributeCard2))) {
                        Game.whoseTurnPlayer().setHeSummonedOrSet(true);
                        Game.whoseTurnPlayer().removeMonsterByInt(Integer.parseInt(tributeCard1));
                        Game.whoseTurnPlayer().removeMonsterByInt(Integer.parseInt(tributeCard2));
                        Game.whoseTurnPlayer().summonCardToMonsterZone(address);
                        if (Game.whoseRivalPlayer().doIHaveSpellCard("Trap Hole") && !Game.whoseTurnPlayer().doIHaveMirageDragonMonster()) {
                            if (monsterCard.getNormalAttack() >= 1000) {
                                currentPlayer.removeCard(address);
                                Game.whoseRivalPlayer().removeOneOfTrapOrSpell("Trap Hole");
                            }
                        }
                        if (Game.whoseRivalPlayer().doIHaveSpellCard("Torrential Tribute") && !Game.whoseTurnPlayer().doIHaveMirageDragonMonster()) {
                            Attack.destroyAllMonstersInTheBoard();
                            Game.whoseRivalPlayer().removeOneOfTrapOrSpell("Torrential Tribute");
                        }
                    } else throw new MyException("There is no monster in this address!");
                }
            }
        } else throw new MyException("there are not enough cards for tribute");
    }

    public void summonASuperHighLevelMonster(Address address) throws MyException {
        Player currentPlayer = Game.whoseTurnPlayer();
        if (Game.whoseTurnPlayer().isThereThreeCardInMonsterZone()) {
            String tributeCard1 = Game.getMainPhase1().scanForTribute(1);
            if (Game.getMainPhase1().isCancelled(tributeCard1)) {
                throw new MyException("canceled successfully!");
            } else {
                String tributeCard2 = Game.getMainPhase1().scanForTribute(2);
                if (Game.getMainPhase1().isCancelled(tributeCard2)) {
                    throw new MyException("canceled successfully!");
                } else {
                    String tributeCard3 = Game.getMainPhase1().scanForTribute(3);
                    if (Game.getMainPhase1().isCancelled(tributeCard3)) {
                        throw new MyException("canceled successfully!");
                    } else {
                        if (Game.whoseTurnPlayer().isMonsterInThisMonsterZoneTypeAddress(Integer.parseInt(tributeCard1))
                                && Game.whoseTurnPlayer().isMonsterInThisMonsterZoneTypeAddress(Integer.parseInt(tributeCard2))
                                && Game.whoseTurnPlayer().isMonsterInThisMonsterZoneTypeAddress(Integer.parseInt(tributeCard3))) {
                            Game.whoseTurnPlayer().setHeSummonedOrSet(true);
                            Game.whoseTurnPlayer().removeMonsterByInt(Integer.parseInt(tributeCard1));
                            Game.whoseTurnPlayer().removeMonsterByInt(Integer.parseInt(tributeCard2));
                            Game.whoseTurnPlayer().removeMonsterByInt(Integer.parseInt(tributeCard3));
                            Game.whoseTurnPlayer().summonCardToMonsterZone(address);
                            if (Game.whoseRivalPlayer().doIHaveSpellCard("Trap Hole") && !Game.whoseTurnPlayer().doIHaveMirageDragonMonster()) {
                                currentPlayer.removeCard(address);
                                Game.whoseRivalPlayer().removeOneOfTrapOrSpell("Trap Hole");
                            }
                            if (Game.whoseRivalPlayer().doIHaveSpellCard("Torrential Tribute") && !Game.whoseTurnPlayer().doIHaveMirageDragonMonster()) {
                                Attack.destroyAllMonstersInTheBoard();
                                Game.whoseRivalPlayer().removeOneOfTrapOrSpell("Torrential Tribute");
                            }
                        } else throw new MyException("There is no monster in this address!");
                    }
                }
            }
        } else throw new MyException("there are not enough cards for tribute");
    }

    public void showSelectedCard(Address address) {
        String kind = Game.whoseTurnPlayer().getCardByAddress(address).getKind();
        if (kind.equals("Monster")) {
            MonsterCard monsterCardForShow = Game.whoseTurnPlayer().getMonsterCardByAddress(address);
            Game.getMainPhase1().printMonsterAttributes(monsterCardForShow);
        } else if (kind.equals("Spell")) {
            SpellCard spellCardForShow = Game.whoseTurnPlayer().getSpellCardByAddress(address);
            Game.getMainPhase1().printSpellAttributes(spellCardForShow);
        } else if (kind.equals("Trap")) {
            TrapCard trapCardForShow = Game.whoseTurnPlayer().getTrapCardByAddress(address);
            Game.getMainPhase1().printTrapAttributes(trapCardForShow);
        }
    }

    public void flipSummon(Address address) throws MyException {
            Player currentPlayer = Game.whoseTurnPlayer();
            if (currentPlayer.isThisMonsterOnDHPosition(address)) {
                currentPlayer.convertThisMonsterFromDHToOO(address);
                MonsterCard monsterCard = currentPlayer.getMonsterCardByAddress(address);
                if (currentPlayer.getMonsterCardByAddress(address).getName().equals("Man-Eater Bug")) {
                    doManEaterBugEffect();
                }
                if (Game.whoseRivalPlayer().doIHaveSpellCard("Trap Hole")) {
                    if (monsterCard.getNormalAttack() >= 1000) {
                        currentPlayer.removeCard(address);
                        Game.whoseRivalPlayer().removeOneOfTrapOrSpell("Trap Hole");
                    }
                }
            } else throw new MyException("You cant do this action in this phase!");
    }

    public void setPosition(String input, Address address) throws MyException {
        Player currentPlayer = Game.whoseTurnPlayer();
            int index = currentPlayer.getIndexOfThisCardByAddress(address);
            Matcher matcher1 = CommandMatcher.getCommandMatcher(input, "^[ ]*set -- position (attack|defense)[ ]*$");
            if (matcher1.find()) {
                if (matcher1.group(1).equals("attack")) {
                    if (!currentPlayer.isThisMonsterOnAttackPosition(address)) {
                        if (!currentPlayer.didWeChangePositionThisCardInThisTurn(index)) {
                            currentPlayer.setDidWeChangePositionThisCardInThisTurn(index);
                            currentPlayer.convertThisMonsterFromDefenceToAttack(address);
                        } else
                            throw new MyException("you already changed this card position in this turn");
                    } else throw new MyException("This card is already in the wanted position!");
                } else {
                    if (currentPlayer.isThisMonsterOnAttackPosition(address)) {
                        if (!currentPlayer.didWeChangePositionThisCardInThisTurn(index)) {
                            currentPlayer.setDidWeChangePositionThisCardInThisTurn(index);
                            currentPlayer.convertThisMonsterFromAttackToDefence(address);
                        } else
                            throw new MyException("you already changed this card position in this turn");
                    } else throw new MyException("This card is already in the wanted position!");
                }
            }
    }

    public void activeSpell(Address address) throws MyException {
        Player currentPlayer = Game.whoseTurnPlayer();
        if (currentPlayer.getSpellCardByStringAddress(address) == null)
            return;
        int index = currentPlayer.getIndexOfThisCardByAddress(address);
        if (!currentPlayer.isThisSpellActivated(index)) {
            if (currentPlayer.getSpellCardByStringAddress(address).getSpellMode() == SpellMode.FIELD)
                activateThisKindOfAddress(address, currentPlayer, index);
            else if (currentPlayer.getSpellCardByStringAddress(address).getSpellMode() == SpellMode.EQUIP)
                activateThisKindOfAddress(address, currentPlayer, index);
            else if (!(currentPlayer.isSpellZoneFull()))
                activateThisKindOfAddress(address, currentPlayer, index);
            else throw new MyException("spell card zone is full!");
        } else throw new MyException("you have already activated this card");
    }

    private void activateThisKindOfAddress(Address address, Player currentPlayer, int index) throws MyException {
        if (SpellCard.canWeActivateThisSpell(address)) {
            if (Game.whoseRivalPlayer().doIHaveSpellCard("Magic Jammer")
                    && !Game.whoseTurnPlayer().doIHaveMirageDragonMonster()
                    && BattlePhase.getInstance().getPermissionForTrap("Magic Jammer", false)) {
                if (MainPhase.removeCardFromMyHand()) Game.whoseTurnPlayer().removeCard(address);
            } else {
                currentPlayer.setIsSpellFaceUp(address.getNumber(), true);
                currentPlayer.setIsThisSpellActivated(true, index);
                SpellCard.doSpellAbsorptionEffect();
                currentPlayer.getSpellCardByStringAddress(address).doEffect(address);
            }
        } else throw new MyException("preparations of this spell are not done yet");
    }

    public void doEffectMainPhase() {
        Player currentPlayer = Game.whoseTurnPlayer();
        if (Game.whoseTurnPlayer().doIHaveMonsterCardInMonsterZone("Herald of Creation")) {
            int count = Integer.parseInt(Effect.run("Herald of Creation1"));
            int numberOfHeraldOfCreation = Game.whoseTurnPlayer().howManyHeraldOfCreationDoWeHave();
            for (int i = 0; i < minOfTwoNumber(numberOfHeraldOfCreation, count) - Game.getMainPhase1().howManyHeraldOfCreationDidWeUseEffect; i++) {
                Address shouldBeRemoved = new Address(Integer.parseInt(Effect.run("Herald of Creation2")), "monster", true);
                if (!Board.isAddressEmpty(shouldBeRemoved)) {
                    Address comeBackFromGraveyard = new Address(Integer.parseInt(Effect.run("Herald of Creation3")), "graveyard", true);
                    MonsterCard monsterCardForHeraldOfCreation = Board.whatKindaMonsterIsHere(comeBackFromGraveyard);
                    if (monsterCardForHeraldOfCreation != null) {
                        if (monsterCardForHeraldOfCreation.getLevel() >= 7) {
                            currentPlayer.removeCard(shouldBeRemoved);
                            currentPlayer.setCardFromGraveyardToHand(comeBackFromGraveyard);
                            if (Game.getMainPhase1().whatMainIsPhase == 1)
                                Game.getMainPhase2().increaseHowManyHeraldOfCreationDidWeUseEffect();
                        }
                    }
                }
            }
        }
    }

    public void doManEaterBugEffect() {
        int monsterZoneNumber = Integer.parseInt(Effect.run("Man-Eater Bug"));
        if (monsterZoneNumber <= 5 && monsterZoneNumber >= 1) {
            Address address1 = new Address(monsterZoneNumber, "monster", false);
            if (!Board.isAddressEmpty(address1)) {
                Attack.destroyThisAddress(address1);
            }
        }
    }

    private void forcedIncreaseLP(int LP) {
        Game.whoseTurnPlayer().increaseLP(LP);
    }

    private void forcedSetWinner() {
        Game.setWinner(Game.whoseTurnPlayer());
        //Game.playTurn("EndGame");
    }

    public int minOfTwoNumber(int num1, int num2) {
        if (num1 <= num2) return num1;
        return num2;
    }

    public void surrender() {
        Game.setIsSurrender(true);
        Game.setWinner(Game.whoseRivalPlayer());
        //Game.playTurn("EndGame");
    }

    public void showOpponentCard(Address address) throws MyException {
        String kind = Game.whoseTurnPlayer().getCardByAddress(address).getKind();
        if (kind.equals("Monster")) {
            MonsterCard monsterCardForShow = Game.whoseTurnPlayer().getMonsterCardByAddress(address);
            if (Game.whoseTurnPlayer().getMonsterPosition(address.getNumber()).equals(PositionOfCardInBoard.DH))
                throw new MyException("card is not visible");
            Game.getMainPhase1().printMonsterAttributes(monsterCardForShow);
        } else if (kind.equals("Spell")) {
            SpellCard spellCardForShow = Game.whoseTurnPlayer().getSpellCardByAddress(address);
            if (!Game.whoseTurnPlayer().getSpellPosition(address.getNumber()))
                throw new MyException("card is not visible");
            Game.getMainPhase1().printSpellAttributes(spellCardForShow);
        } else if (kind.equals("Trap")) {
            TrapCard trapCardForShow = Game.whoseTurnPlayer().getTrapCardByAddress(address);
            if (!Game.whoseTurnPlayer().getSpellPosition(address.getNumber()))
                throw new MyException("card is not visible");
            Game.getMainPhase1().printTrapAttributes(trapCardForShow);
        }
    }

}
