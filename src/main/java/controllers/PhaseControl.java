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
import view.GameView;
import view.Main;

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
        Game.whoseTurnPlayer().decreaseLP(100);
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
                if (!Game.firstPlayer.isHandFull()) {
                    Game.firstPlayer.addCardFromUnusedToHand();
                    Game.getGameView().reset();
                }
        }
        if (Game.secondPlayer.isOneHisSupplySquadActivated()) {
            if (Game.secondPlayer.isOneHisMonstersDestroyedInThisRound())
                if (!Game.secondPlayer.isHandFull()) {
                    Game.secondPlayer.addCardFromUnusedToHand();
                    Game.getGameView().reset();
                }
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
            return "its " + Game.firstPlayer.getNickName() + "???s turn";
        } else {
            return "its " + Game.secondPlayer.getNickName() + "???s turn";
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
                if (Game.whoseTurnPlayer().getMonsterCardByAddress(address).getNamesForEffect().contains("Scanner")) {
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

    public void trapSet(Address address) throws Exception {
        Player currentPlayer = Game.whoseTurnPlayer();
        if (!currentPlayer.isSpellZoneFull()) {
            activateSomeOfTraps(address, currentPlayer.getTrapCardByAddress(address), currentPlayer);
            currentPlayer.setCardFromHandToSpellZone(address);
        } else throw new MyException("spell card zone is full!");
    }

    private void activateSomeOfTraps(Address address, TrapCard trapCard, Player currentPlayer) throws Exception {
        if (!Game.whoseRivalPlayer().doIHaveMirageDragonMonster()) {
            if (trapCard.getNamesForEffect().contains("Mind Crush")) {
                Game.getGameView().getPermissionForTrap("Mind Crush", true, null, address, 1, null);
            }
            if (trapCard.getNamesForEffect().contains("Time Seal")) {
                Game.getGameView().getPermissionForTrap("Time Seal", true, null, address, 1, null);
            }
            if (trapCard.getNamesForEffect().contains("Call of The Haunted")) {
                Game.getGameView().getPermissionForTrap("Call of The Haunted", true, null, address, 1, null);
            }
        }
    }

    public void doCallOfTheHaunted(Address address, Player currentPlayer) {
        if (Game.getGameView().yesOrNo) {
            if (Game.isAITurn()){
                AISummonFromGraveyard();
                Game.getGameView().reset();
            } else
                Game.getGameView().doCallOfTheHauntedEffect(true);
            currentPlayer.removeCard(address);
        }
    }

    public void doTimeSeal(Address address, Player currentPlayer) {
        if (Game.getGameView().yesOrNo) {
            PhaseControl.getInstance().setCanDraw(false);
            currentPlayer.removeCard(address);
            Game.getGameView().reset();
        }
    }

    public void doMindCrush(Address address, Player currentPlayer) {
        if (Game.getGameView().yesOrNo) {
            Game.getGameView().doMindCrushEffect(address);
            Game.getGameView().reset();
        }
    }

    public void doMindCrushEffect(Address address) {
        Player currentPlayer = Game.whoseTurnPlayer();
        Player rivalPlayer = Game.whoseRivalPlayer();
        if (rivalPlayer.doIHaveCardWithThisNameInMyHand(Game.getGameView().cardName)) {
            rivalPlayer.removeAllCardWithThisNameInMyHand(Game.getGameView().cardName);
        } else currentPlayer.removeOneOfHandCard();
        currentPlayer.removeOneOfTrapOrSpell("Mind Crush");
        Game.getGameView().reset();
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

    public void summonControl(Address address) throws Exception {
        Player currentPlayer = Game.whoseTurnPlayer();
        if (currentPlayer.getMonsterCardByStringAddress(address) == null)
            throw new MyException("you can't summon spell or trap");
        if (!currentPlayer.isMonsterZoneFull()) {
            if (!currentPlayer.isHeSummonedOrSet()) {
                MonsterCard monsterCard = Game.whoseTurnPlayer().getMonsterCardByStringAddress(address);
                int level = monsterCard.getLevel();
                if (!currentPlayer.getMonsterCardByStringAddress(address).isRitual()) {
                    if (monsterCard.getNamesForEffect().contains("Gate Guardian")) {
                        Game.getGameView().doSolemnWarningEffect(address, 1, monsterCard);
                    } else {
                        Game.getGameView().doSolemnWarningEffect(address, 2, monsterCard);
                    }
                } else Game.getGameView().ritualSummon(address, level);
            } else throw new MyException("you already summoned/set on this turn!");
        } else throw new MyException("Monster card zone is full!");
    }

    public void doSolemnWarningEffect2(Address address, Player currentPlayer, MonsterCard monsterCard) throws MyException {
        int level = monsterCard.getLevel();
        if (!Game.getGameView().yesOrNo) {
            if (level <= 4) summonALowLevelMonster(currentPlayer, address);
            else if (level <= 6) Game.getGameView().summonForTribute(1, address);
            else {
                if (Game.whoseTurnPlayer().getMonsterCardByAddress(address).getNamesForEffect().contains("Beast King Barbaros")) {
                    Game.getGameView().runEffect("Beast King Barbaros", address, null);
                } else Game.getGameView().summonForTribute(2, address);
            }
        } else {
            throw new MyException("Opponent solemn destroyed your monster");
        }
    }

    public void doBeastKingBarbarosEffect(Address address) throws MyException {
        if (Game.getGameView().answer.equals("2")) {
            int index = Game.whoseTurnPlayer().getIndexOfThisCardByAddress(address);
            Game.whoseTurnPlayer().setDidBeastKingBarbarosSummonedSuperHighLevel(true, index);
            Game.getGameView().summonForTribute(2, address);
            Game.getGameView().reset();
        } else if (Game.getGameView().answer.equals("3")) {
            int index = Game.whoseTurnPlayer().getIndexOfThisCardByAddress(address);
            Game.whoseTurnPlayer().setDidBeastKingBarbarosSummonedSuperHighLevel(true, index);
            Game.getGameView().summonForTribute(3, address);
            Game.getGameView().reset();
        }
    }

    public void doSolemnWarningEffect1(Address address) throws MyException {
        if (!Game.getGameView().yesOrNo)
            Game.getGameView().summonForTribute(3, address);
    }

    public void summonALowLevelMonster(Player currentPlayer, Address address) {
        MonsterCard monsterCard = currentPlayer.getMonsterCardByAddress(address);
        if (currentPlayer.getMonsterCardByAddress(address).getNamesForEffect().contains("Terratiger, the Empowered Warrior")) {
            Game.getGameView().runEffect("Terratiger, the Empowered Warrior", null, null);
        }
        currentPlayer.summonCardToMonsterZone(address);
        if (Game.whoseRivalPlayer().doIHaveSpellCard("Trap Hole") && !Game.whoseTurnPlayer().doIHaveMirageDragonMonster()) {
            if (monsterCard.getNormalAttack() >= 1000) {
                currentPlayer.removeCard(address);
                Game.whoseRivalPlayer().removeOneOfTrapOrSpell("Trap Hole");
            }
        }
        currentPlayer.setHeSummonedOrSet(true);
        int index = Game.whoseTurnPlayer().getIndexOfThisCardByAddress(address);
        Game.whoseTurnPlayer().setDidWeChangePositionThisCardInThisTurn(index);
        if (Game.whoseRivalPlayer().doIHaveSpellCard("Torrential Tribute") && !Game.whoseTurnPlayer().doIHaveMirageDragonMonster()) {
            Attack.destroyAllMonstersInTheBoard();
            Game.whoseRivalPlayer().removeOneOfTrapOrSpell("Torrential Tribute");
        }
        Game.getGameView().reset();
    }

    public void doTerratigerEffect(Player currentPlayer) throws MyException {
        if (Integer.parseInt(Game.getGameView().answer) != 0) {
            Address address1 = new Address(Integer.parseInt(Game.getGameView().answer), "hand", true);
            if ((currentPlayer.getMonsterCardByAddress(address1) != null)
                    && (currentPlayer.getMonsterCardByAddress(address1).getLevel() <= 4)
                    && (!currentPlayer.isMonsterZoneFull())) {
                currentPlayer.setCardFromHandToMonsterZone(address1);
                Game.getGameView().reset();
            } else throw new MyException("Conditions are not met");
        } else throw new MyException("Canceled");
    }

    public void summonAMediumLevelMonster(Address address) throws MyException {
        Player currentPlayer = Game.whoseTurnPlayer();
        MonsterCard monsterCard = currentPlayer.getMonsterCardByAddress(address);
        if (currentPlayer.isThereAnyCardInMonsterZone()) {
            Game.getGameView().scanForTribute(1, address, monsterCard, "medium");
        } else throw new MyException("There are not enough cards for tribute");
    }

    public void continueMediumLevelSummon(Address address, Player currentPlayer, MonsterCard monsterCard, String tributeCard) throws MyException {
        if (tributeCard.equals("cancel")) {
            throw new MyException("Canceled successfully");
        } else {
            if (currentPlayer.isMonsterInThisMonsterZoneTypeAddress(Integer.parseInt(tributeCard))) {
                currentPlayer.setHeSummonedOrSet(true);
                int index = Game.whoseTurnPlayer().getIndexOfThisCardByAddress(address);
                Game.whoseTurnPlayer().setDidWeChangePositionThisCardInThisTurn(index);
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
                Game.getGameView().reset();
            } else throw new MyException("There is no monster in this address!");
        }
    }

    public void summonAHighLevelMonster(Address address) throws MyException {
        Player currentPlayer = Game.whoseTurnPlayer();
        MonsterCard monsterCard = currentPlayer.getMonsterCardByAddress(address);
        if (Game.whoseTurnPlayer().isThereTwoCardInMonsterZone()) {
            Game.getGameView().scanForTribute(1, address, monsterCard, "high1");
        } else throw new MyException("There are not enough cards for tribute");
    }

    public void continueFirstHigh(Address address, Player currentPlayer, MonsterCard monsterCard, String tributeCard1) throws MyException {
        if (tributeCard1.equals("cancel")) {
            throw new MyException("Canceled successfully!");
        } else {
            Game.getGameView().scanForTribute(2, address, monsterCard, "high2");
        }
    }

    public void continueSecondHigh(Address address, Player currentPlayer, MonsterCard monsterCard, String tributeCard1, String tributeCard2) throws MyException {
        if (tributeCard2.equals("cancel")) {
            throw new MyException("Canceled successfully!");
        } else {
            if (Game.whoseTurnPlayer().isMonsterInThisMonsterZoneTypeAddress(Integer.parseInt(tributeCard1))
                    && Game.whoseTurnPlayer().isMonsterInThisMonsterZoneTypeAddress(Integer.parseInt(tributeCard2))) {
                Game.whoseTurnPlayer().setHeSummonedOrSet(true);
                int index = Game.whoseTurnPlayer().getIndexOfThisCardByAddress(address);
                Game.whoseTurnPlayer().setDidWeChangePositionThisCardInThisTurn(index);
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
                Game.getGameView().reset();
            } else throw new MyException("There is no monster in this address!");
        }
    }

    public void summonASuperHighLevelMonster(Address address) throws MyException {
        Player currentPlayer = Game.whoseTurnPlayer();
        MonsterCard monsterCard = currentPlayer.getMonsterCardByAddress(address);
        if (Game.whoseTurnPlayer().isThereThreeCardInMonsterZone()) {
            Game.getGameView().scanForTribute(1, address, monsterCard, "superHigh1");
        } else throw new MyException("There are not enough cards for tribute");
    }

    public void continueFirstSuperHigh(Address address, Player currentPlayer, MonsterCard monsterCard, String tributeCard1) throws MyException {
        if (Game.getMainPhase1().isCancelled(tributeCard1)) {
            throw new MyException("Canceled successfully!");
        } else {
            Game.getGameView().scanForTribute(2, address, monsterCard, "superHigh2");
        }
    }

    public void continueSecondSuperHigh(Address address, Player currentPlayer, MonsterCard monsterCard, String tributeCard1, String tributeCard2) throws MyException {
        if (Game.getMainPhase1().isCancelled(tributeCard2)) {
            throw new MyException("Canceled successfully!");
        } else {
            Game.getGameView().scanForTribute(3, address, monsterCard, "superHigh3");
        }
    }

    public void continueThirdSuperHigh(Address address, Player currentPlayer, String tributeCard1, String tributeCard2, String tributeCard3) throws MyException {
        if (Game.getMainPhase1().isCancelled(tributeCard3)) {
            throw new MyException("Canceled successfully!");
        } else {
            if (Game.whoseTurnPlayer().isMonsterInThisMonsterZoneTypeAddress(Integer.parseInt(tributeCard1))
                    && Game.whoseTurnPlayer().isMonsterInThisMonsterZoneTypeAddress(Integer.parseInt(tributeCard2))
                    && Game.whoseTurnPlayer().isMonsterInThisMonsterZoneTypeAddress(Integer.parseInt(tributeCard3))) {
                Game.whoseTurnPlayer().setHeSummonedOrSet(true);
                int index = Game.whoseTurnPlayer().getIndexOfThisCardByAddress(address);
                Game.whoseTurnPlayer().setDidWeChangePositionThisCardInThisTurn(index);
                Game.whoseTurnPlayer().removeMonsterByInt(Integer.parseInt(tributeCard1));
                Game.whoseTurnPlayer().removeMonsterByInt(Integer.parseInt(tributeCard2));
                Game.whoseTurnPlayer().removeMonsterByInt(Integer.parseInt(tributeCard3));
                if (Game.whoseTurnPlayer().getCardByAddress(address).equals("Beast King Barbaros")) {
                    Board.destroyAllMonster(Game.whoseRivalPlayer());
                }
                Game.whoseTurnPlayer().summonCardToMonsterZone(address);
                if (Game.whoseRivalPlayer().doIHaveSpellCard("Trap Hole") && !Game.whoseTurnPlayer().doIHaveMirageDragonMonster()) {
                    currentPlayer.removeCard(address);
                    Game.whoseRivalPlayer().removeOneOfTrapOrSpell("Trap Hole");
                }
                if (Game.whoseRivalPlayer().doIHaveSpellCard("Torrential Tribute") && !Game.whoseTurnPlayer().doIHaveMirageDragonMonster()) {
                    Attack.destroyAllMonstersInTheBoard();
                    Game.whoseRivalPlayer().removeOneOfTrapOrSpell("Torrential Tribute");
                }
                Game.getGameView().reset();
            } else throw new MyException("There is no monster in this address!");
        }
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

    public void flipSummon(Address address) throws Exception {
        Player currentPlayer = Game.whoseTurnPlayer();
        if (currentPlayer.isThisMonsterOnDHPosition(address)) {
            int index = currentPlayer.getIndexOfThisCardByAddress(address);
            if (!currentPlayer.didWeChangePositionThisCardInThisTurn(index)) {
                currentPlayer.convertThisMonsterFromDHToOO(address);
                currentPlayer.setDidWeChangePositionThisCardInThisTurn(index);
                MonsterCard monsterCard = currentPlayer.getMonsterCardByAddress(address);
                if (currentPlayer.getMonsterCardByAddress(address).getNamesForEffect().contains("Man-Eater Bug")) {
                    Game.getGameView().runEffect("Man-Eater Bug", null, null);
                }
                if (Game.whoseRivalPlayer().doIHaveSpellCard("Trap Hole")) {
                    if (monsterCard.getNormalAttack() >= 1000) {
                        Game.getGameView().getPermissionForTrap("Trap Hole", false, null, address, 1, null);
                    }
                }
            } else throw new MyException("You have changed this cards position in this turn");
        } else throw new MyException("You cant do this action in this phase!");
    }

    public void setPosition(String input, Address address) throws MyException {
        Player currentPlayer = Game.whoseTurnPlayer();
        int index = currentPlayer.getIndexOfThisCardByAddress(address);
        if (input.equals("attack")) {
            if (!currentPlayer.isThisMonsterOnAttackPosition(address)) {
                if (!currentPlayer.didWeChangePositionThisCardInThisTurn(index)) {
                    currentPlayer.setDidWeChangePositionThisCardInThisTurn(index);
                    currentPlayer.convertThisMonsterFromDefenceToAttack(address);
                } else
                    throw new MyException("you already changed this cards position in this turn");
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

    public void activeSpell(Address address) throws Exception {
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

    private void activateThisKindOfAddress(Address address, Player currentPlayer, int index) throws Exception {
        if (SpellCard.canWeActivateThisSpell(address)) {
            if (Game.whoseRivalPlayer().doIHaveSpellCard("Magic Jammer")
                    && !Game.whoseTurnPlayer().doIHaveMirageDragonMonster()) {
                Game.getGameView().getPermissionForTrap("Magic Jammer", false, null, address, index, null);
            } else {
                currentPlayer.setIsSpellFaceUp(address.getNumber(), true);
                currentPlayer.setIsThisSpellActivated(true, index);
                SpellCard.doSpellAbsorptionEffect();
                try {
                    currentPlayer.getSpellCardByStringAddress(address).doEffect(address);
                    Game.getGameView().reset();
                } catch (MyException myException) {
                    throw new MyException(myException.getMessage());
                }
            }
        } else throw new MyException("Preparations of this spell are not done yet");
    }

    private void continueWithoutMagicJammer(Address address, int index) throws MyException {
        Game.whoseTurnPlayer().setIsSpellFaceUp(address.getNumber(), true);
        Game.whoseTurnPlayer().setIsThisSpellActivated(true, index);
        SpellCard.doSpellAbsorptionEffect();
        try {
            Game.whoseTurnPlayer().getSpellCardByStringAddress(address).doEffect(address);
            Game.getGameView().reset();
        } catch (MyException myException) {
            throw new MyException(myException.getMessage());
        }
    }

    public void doMagicJammer(Address address, int number) throws MyException {
        if (Game.getGameView().yesOrNo) {
            Game.getGameView().removeCardFromMyHand(address);
        } else {
            PhaseControl.getInstance().continueWithoutMagicJammer(address, number);
        }
    }

    public void doEffectMainPhase() {
        Player currentPlayer = Game.whoseTurnPlayer();
        if (Game.whoseTurnPlayer().doIHaveMonsterCardInMonsterZone("Herald of Creation")) {
            Game.getGameView().runEffect("Herald of Creation1", null, null);
        }
    }

    public void doHeraldOfCreation1Effect(Player currentPlayer) {
        int count = Integer.parseInt(Game.getGameView().answer);
        int numberOfHeraldOfCreation = Game.whoseTurnPlayer().howManyHeraldOfCreationDoWeHave();
        for (int i = 0; i < minOfTwoNumber(numberOfHeraldOfCreation, count) - Game.getGameView().howManyHeraldOfCreationDidWeUseEffect; i++) {
                Game.getGameView().runEffect("Herald of Creation2", null, null);
        }
    }

    public void doHeraldOfCreation2Effect(Player currentPlayer) {
        Address shouldBeRemoved = new Address(Integer.parseInt(Game.getGameView().answer), "monster", true);
        if (!Board.isAddressEmpty(shouldBeRemoved)) {
            Game.getGameView().runEffect("Herald of Creation3", null, shouldBeRemoved);
            Game.getGameView().reset();
        }
        Game.getGameView().reset();
    }

    public void doHeraldOfCreation3Effect(Player currentPlayer, Address shouldBeRemoved) throws MyException {
        Address comeBackFromGraveyard = new Address(Integer.parseInt(Game.getGameView().answer), "graveyard", true);
        MonsterCard monsterCardForHeraldOfCreation = Board.whatKindaMonsterIsHere(comeBackFromGraveyard);
        if (monsterCardForHeraldOfCreation != null) {
            if (monsterCardForHeraldOfCreation.getLevel() >= 7) {
                currentPlayer.removeCard(shouldBeRemoved);
                currentPlayer.setCardFromGraveyardToHand(comeBackFromGraveyard);
                if (Game.getCurrentPhase().equals("Main Phase 1"))
                    Game.getGameView().increaseHowManyHeraldOfCreationDidWeUseEffect();
                Game.getGameView().reset();
            } else throw new MyException("The cars you selected was not level 7 or higher");
        } else throw new MyException("There is no graveyard card at this address");
    }

    public void doManEaterBugEffect() {
        int monsterZoneNumber = Integer.parseInt(Game.getGameView().answer);
        if (monsterZoneNumber <= 5 && monsterZoneNumber >= 1) {
            Address address1 = new Address(monsterZoneNumber, "monster", false);
            if (!Board.isAddressEmpty(address1)) {
                Game.whoseRivalPlayer().removeCard(address1);
                Game.getGameView().reset();
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
            if (!Game.whoseTurnPlayer().isSpellFaceUp(address.getNumber()))
                throw new MyException("card is not visible");
            Game.getMainPhase1().printSpellAttributes(spellCardForShow);
        } else if (kind.equals("Trap")) {
            TrapCard trapCardForShow = Game.whoseTurnPlayer().getTrapCardByAddress(address);
            if (!Game.whoseTurnPlayer().isSpellFaceUp(address.getNumber()))
                throw new MyException("card is not visible");
            Game.getMainPhase1().printTrapAttributes(trapCardForShow);
        }
    }

    public void removeCardForMagicJammer(Address submitedAddress, Address address) {
        if (Game.whoseRivalPlayer().getCardByAddress(submitedAddress) != null) {
            Game.whoseRivalPlayer().removeCard(submitedAddress);
            Game.whoseTurnPlayer().removeCard(address);
        }
        Game.getGameView().reset();
    }

    public void doBringBackCardFromGraveyard(boolean isMine, Player currentPlayer) throws MyException {
        Address address = new Address(Integer.parseInt(Game.getGameView().answer), "graveyard", isMine);
        if (currentPlayer.getMonsterCardByAddress(address) != null) {
            Board.summonThisCardFromGraveYardToMonsterZone(address);
            Game.getGameView().reset();
        } else throw new MyException("you chose the wrong card");
    }
}
