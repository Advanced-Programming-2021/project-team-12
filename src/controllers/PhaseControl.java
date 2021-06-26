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

    public boolean isCanDraw() {
        return canDraw;
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
            Game.setWinner(Game.firstPlayer);
            Game.playTurn("EndGame");
        } else if (Game.secondPlayer.getLP() <= 0) {
            Game.setWinner(Game.secondPlayer);
            Game.playTurn("EndGame");
        }
    }

    public String drawOneCard() {
        if (!canDraw) {
            canDraw = true;
            return "you cant draw because of Time Seal";
        }
        if (Game.playerTurn == PlayerTurn.FIRSTPLAYER) {
            String newAddedCard = Game.firstPlayer.addCardFromUnusedToHand();
            if (newAddedCard.equals("unused is empty")) {
                Game.setWinner(Game.secondPlayer);
                return "first player cannot draw a card, second player is the winner";
            } else if (!newAddedCard.equals("Hand is full")) {
                return "new card added to the hand : " + newAddedCard;
            }
        } else {
            String newAddedCard = Game.secondPlayer.addCardFromUnusedToHand();
            if (newAddedCard.equals("unused is empty")) {
                Game.setWinner(Game.firstPlayer);
                return "second player cannot draw a card, first player is the winner";
            } else if (!newAddedCard.equals("Hand is full")) {
                return "new card added to the hand : " + newAddedCard;
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

    public void checkInputNonCardSelected(String input) throws BreakException, MyException {
        if (input.matches("^[ ]*next phase[ ]*$"))
            throw new BreakException("next phase!");
        else if (input.matches("^[ ]*select .*$"))
            whatIsSelected(input);
        else if (input.matches("[ ]*increase --LP [\\d]+[ ]*"))
            forcedIncreaseLP(input);
        else if (input.matches("[ ]*duel set-winner(.)*"))
            forcedSetWinner();
        else if (input.matches("^[ ]*summon[ ]*$"))
            throw new MyException("no card is selected!");
        else if (input.matches("^[ ]*set[ ]*$"))
            throw new MyException("no card is selected!");
        else if (input.matches("^[ ]*set -- position (attack|defense)[ ]*$"))
            throw new MyException("no card is selected!");
        else if (input.matches("^[ ]*flip-summon[ ]*$"))
            throw new MyException("no card is selected!");
        else if (input.matches("^[ ]*attack [\\d]+[ ]*$"))
            throw new MyException("no card is selected!");
        else if (input.matches("^[ ]*attack direct[ ]*$"))
            throw new MyException("no card is selected!");
        else if (input.matches("^[ ]*activate effect[ ]*$"))
            throw new MyException("no card is selected!");
        else if (input.matches("^[ ]*show graveyard[ ]*$"))
            Board.showGraveyard();
        else if (input.matches("^[ ]*card show --selected[ ]*$"))
            throw new MyException("no card is selected!");
        else if (input.matches("^[ ]*surrender[ ]*$"))
            surrender();
        else
            throw new MyException("invalid command!");
    }

    public void whatIsSelected(String input) throws MyException {
        if (Address.isAddressValid(input) && Board.getCardByAddress(new Address(input)) == null)
            throw new MyException("address is empty");
        if (input.matches("^[ ]*select --monster [\\d]+[ ]*$"))
            Game.getMainPhase1().selectMonster(CommandMatcher.getCommandMatcher(input, "(^[ ]*select --monster ([\\d]+)[ ]*$)"));
        else if (input.matches("^[ ]*select --monster --opponent [\\d]+[ ]*$"))
            Game.getMainPhase1().selectOpponentMonster(CommandMatcher.getCommandMatcher(input, "(^[ ]*select --monster --opponent ([\\d]+)[ ]*$)"));
        else if (input.matches("^[ ]*select --spell [\\d]+[ ]*$"))
            Game.getMainPhase1().selectSpell(CommandMatcher.getCommandMatcher(input, "(^[ ]*select --spell ([\\d]+)[ ]*$)"));
        else if (input.matches("^[ ]*select --spell --opponent [\\d]+[ ]*$"))
            Game.getMainPhase1().selectOpponentSpell(CommandMatcher.getCommandMatcher(input, "(^[ ]*select --spell --opponent ([\\d]+)[ ]*$)"));
        else if (input.matches("^[ ]*select --field[ ]*$"))
            Game.getMainPhase1().selectField();
        else if (input.matches("^[ ]*select --field --opponent[ ]*$"))
            Game.getMainPhase1().selectOpponentField();
        else if (input.matches("^[ ]*select --hand [\\d]+[ ]*$"))
            Game.getMainPhase1().selectHand(CommandMatcher.getCommandMatcher(input, "(^[ ]*select --hand ([\\d]+)[ ]*$)"));
        else if (input.matches("^[ ]*select -d[ ]*$"))
            throw new MyException("no card is selected!");
        else if (Address.isAddressValid(input))
            throw new MyException("invalid card selection!");
        else
            throw new MyException("invalid card selection!");
    }

    public void monsterCardSelected(String input, String selectedCard) throws BreakException, MyException {
        if (input.matches("^[ ]*next phase[ ]*$")) {
            throw new BreakException("next phase!");
        } else if (input.matches("^[ ]*select -d[ ]*$"))
            Game.getMainPhase1().getSelectedCard();
        else if (input.matches("^[ ]*summon[ ]*$"))
            throw new MyException("You can't summon this card!");
        else if (input.matches("^[ ]*set[ ]*$"))
            throw new MyException("You can't set this card!");
        else if (input.matches("^[ ]*set -- position (attack|defense)[ ]*$"))
            Game.getMainPhase1().setPosition(input, CommandMatcher.getCommandMatcher(selectedCard, "(^[ ]*select --monster ([\\d]+)[ ]*$)"));
        else if (input.matches("^[ ]*flip-summon[ ]*$"))
            Game.getMainPhase1().flipSummon(CommandMatcher.getCommandMatcher(selectedCard, "(^[ ]*select --monster ([\\d]+)[ ]*$)"));
        else if (input.matches("^[ ]*attack [\\d]+[ ]*$"))
            Game.getMainPhase1().attack(CommandMatcher.getCommandMatcher(input, "(^[ ]*attack ([\\d]+)[ ]*$)"));
        else if (input.matches("^[ ]*attack direct[ ]*$"))
            Game.getMainPhase1().directAttack(CommandMatcher.getCommandMatcher(selectedCard, "(^[ ]*select --monster ([\\d]+)[ ]*$)"));
        else if (input.matches("^[ ]*activate effect[ ]*$"))
            throw new MyException("You can only activate spell card!");
        else if (input.matches("^[ ]*show graveyard[ ]*$"))
            Board.showGraveyard();
        else if (input.matches("^[ ]*card show --selected[ ]*$"))
            Game.getMainPhase1().showSelectedCard(CommandMatcher.getCommandMatcher(selectedCard, "(^[ ]*select --monster ([\\d]+)[ ]*$)"));
        else if (input.matches("^[ ]*surrender[ ]*$"))
            surrender();
        else
            throw new MyException("invalid command!");
    }

    public void OpponentMonsterCardSelected(String input, String selectedCard) throws BreakException, MyException {
        if (input.matches("^[ ]*next phase[ ]*$")) {
            throw new BreakException("next phase!");
        } else if (input.matches("^[ ]*select -d[ ]*$"))
            Game.getMainPhase1().getSelectedCard();
        else if (input.matches("^[ ]*summon[ ]*$"))
            throw new MyException("You can't summon this card!");
        else if (input.matches("^[ ]*set[ ]*$"))
            throw new MyException("You can't set this card!");
        else if (input.matches("^[ ]*set -- position (attack|defense)[ ]*$"))
            throw new MyException("you can’t change this card position!");
        else if (input.matches("^[ ]*flip-summon[ ]*$"))
            throw new MyException("you can’t change this card position!");
        else if (input.matches("^[ ]*attack [\\d]+[ ]*$"))
            throw new MyException("you can’t attack with this card");
        else if (input.matches("^[ ]*attack direct[ ]*$"))
            throw new MyException("you can’t attack with this card");
        else if (input.matches("^[ ]*activate effect[ ]*$"))
            throw new MyException("You can only activate spell card!");
        else if (input.matches("^[ ]*show graveyard[ ]*$"))
            Board.showGraveyard();
        else if (input.matches("^[ ]*card show --selected[ ]*$"))
            Game.getMainPhase1().showSelectedCard(CommandMatcher.getCommandMatcher(selectedCard, "(^[ ]*select --monster --opponent [\\d]+[ ]*$)"));
        else if (input.matches("^[ ]*surrender[ ]*$"))
            surrender();
        else
            throw new MyException("invalid command!");
    }

    public void spellSelected(String input, String selectedCard) throws BreakException, MyException {
        if (input.matches("^[ ]*next phase[ ]*$")) {
            throw new BreakException("next phase!");
        } else if (input.matches("^[ ]*select -d[ ]*$"))
            Game.getMainPhase1().getSelectedCard();
        else if (input.matches("^[ ]*summon[ ]*$"))
            throw new MyException("You can't summon this card!");
        else if (input.matches("^[ ]*set[ ]*$"))
            throw new MyException("You can't set this card!");
        else if (input.matches("^[ ]*set -- position (attack|defense)[ ]*$"))
            throw new MyException("you can’t change this card position!");
        else if (input.matches("^[ ]*flip-summon[ ]*$"))
            throw new MyException("you can’t change this card position!");
        else if (input.matches("^[ ]*attack [\\d]+[ ]*$"))
            throw new MyException("you can’t attack with this card");
        else if (input.matches("^[ ]*attack direct[ ]*$"))
            throw new MyException("you can’t attack with this card");
        else if (input.matches("^[ ]*activate effect[ ]*$"))
            Game.getMainPhase1().activeSpell(CommandMatcher.getCommandMatcher(selectedCard, "(^[ ]*select --spell ([\\d]+)[ ]*$)"));
        else if (input.matches("^[ ]*show graveyard[ ]*$"))
            Board.showGraveyard();
        else if (input.matches("^[ ]*card show --selected[ ]*$"))
            Game.getMainPhase1().showSelectedCard(CommandMatcher.getCommandMatcher(selectedCard, "(^[ ]*select --spell ([\\d]+)[ ]*$)"));
        else if (input.matches("^[ ]*surrender[ ]*$"))
            surrender();
        else
            throw new MyException("invalid command!");
    }

    public void OpponentSpellCardSelected(String input, String selectedCard) throws BreakException, MyException {
        if (input.matches("^[ ]*next phase[ ]*$")) {
            throw new BreakException("next phase!");
        } else if (input.matches("^[ ]*select -d[ ]*$"))
            Game.getMainPhase1().getSelectedCard();
        else if (input.matches("^[ ]*summon[ ]*$"))
            throw new MyException("You can't summon this card!");
        else if (input.matches("^[ ]*set[ ]*$"))
            throw new MyException("You can't set this card!");
        else if (input.matches("^[ ]*set -- position (attack|defense)[ ]*$"))
            throw new MyException("you can’t change this card position!");
        else if (input.matches("^[ ]*flip-summon[ ]*$"))
            throw new MyException("you can’t change this card position!");
        else if (input.matches("^[ ]*attack [\\d]+[ ]*$"))
            throw new MyException("you can’t attack with this card");
        else if (input.matches("^[ ]*attack direct[ ]*$"))
            throw new MyException("you can’t attack with this card");
        else if (input.matches("^[ ]*activate effect[ ]*$"))
            throw new MyException("You can only activate spell card!");
        else if (input.matches("^[ ]*show graveyard[ ]*$"))
            Board.showGraveyard();
        else if (input.matches("^[ ]*card show --selected[ ]*$"))
            Game.getMainPhase1().showSelectedCard(CommandMatcher.getCommandMatcher(selectedCard, "(^[ ]*select --spell --opponent ([\\d]+)[ ]*$)"));
        else if (input.matches("^[ ]*surrender[ ]*$"))
            surrender();
        else
            throw new MyException("invalid command!");
    }

    public void fieldCardSelected(String input) throws BreakException, MyException {
        if (input.matches("^[ ]*next phase[ ]*$")) {
            throw new BreakException("next phase!");
        } else if (input.matches("^[ ]*select -d[ ]*$"))
            Game.getMainPhase1().getSelectedCard();
        else if (input.matches("^[ ]*summon[ ]*$"))
            throw new MyException("You can't summon this card!");
        else if (input.matches("^[ ]*set[ ]*$"))
            throw new MyException("You can't set this card!");
        else if (input.matches("^[ ]*set -- position (attack|defense)[ ]*$"))
            throw new MyException("you can’t change this card position!");
        else if (input.matches("^[ ]*flip-summon[ ]*$"))
            throw new MyException("you can’t change this card position!");
        else if (input.matches("^[ ]*attack [\\d]+[ ]*$"))
            throw new MyException("you can’t attack with this card");
        else if (input.matches("^[ ]*attack direct[ ]*$"))
            throw new MyException("you can’t attack with this card");
        else if (input.matches("^[ ]*activate effect[ ]*$"))
            throw new MyException("You can only activate spell card!");
        else if (input.matches("^[ ]*show graveyard[ ]*$"))
            Board.showGraveyard();
        else if (input.matches("^[ ]*card show --selected[ ]*$"))
            Game.getMainPhase1().showFieldZoneCard();
        else if (input.matches("^[ ]*surrender[ ]*$"))
            surrender();
        else
            throw new MyException("invalid command!");
    }

    public void opponentFieldCardSelected(String input) throws BreakException, MyException {
        if (input.matches("^[ ]*next phase[ ]*$")) {
            throw new BreakException("next phase!");
        } else if (input.matches("^[ ]*select -d[ ]*$"))
            Game.getMainPhase1().getSelectedCard();
        else if (input.matches("^[ ]*summon[ ]*$"))
            throw new MyException("You can't summon this card!");
        else if (input.matches("^[ ]*set[ ]*$"))
            throw new MyException("You can't set this card!");
        else if (input.matches("^[ ]*set -- position (attack|defense)[ ]*$"))
            throw new MyException("you can’t change this card position!");
        else if (input.matches("^[ ]*flip-summon[ ]*$"))
            throw new MyException("you can’t change this card position!");
        else if (input.matches("^[ ]*attack [\\d]+[ ]*$"))
            throw new MyException("you can’t attack with this card");
        else if (input.matches("^[ ]*attack direct[ ]*$"))
            throw new MyException("you can’t attack with this card");
        else if (input.matches("^[ ]*activate effect[ ]*$"))
            throw new MyException("You can only activate spell card!");
        else if (input.matches("^[ ]*show graveyard[ ]*$"))
            Board.showGraveyard();
        else if (input.matches("^[ ]*card show --selected[ ]*$"))
            Game.getMainPhase1().showOpponentFieldZoneCard();
        else if (input.matches("^[ ]*surrender[ ]*$"))
            surrender();
        else
            throw new MyException("invalid command!");
    }

    public void handSelected(String input, String selectedCard) throws BreakException, MyException {
        if (input.matches("^[ ]*next phase[ ]*$")) {
            throw new BreakException("next phase!");
        } else if (input.matches("^[ ]*select -d[ ]*$"))
            Game.getMainPhase1().getSelectedCard();
        else if (input.matches("^[ ]*summon[ ]*$"))
            Game.getMainPhase1().summon(CommandMatcher.getCommandMatcher(selectedCard, "(^[ ]*select --hand [\\d]+[ ]*$)"));
        else if (input.matches("^[ ]*set[ ]*$"))
            whatIsSet(CommandMatcher.getCommandMatcher(selectedCard, "(^[ ]*select --hand ([\\d]+)[ ]*$)"));
        else if (input.matches("^[ ]*set -- position (attack|defense)[ ]*$"))
            throw new MyException("you can’t change this card position!");
        else if (input.matches("^[ ]*flip-summon[ ]*$"))
            throw new MyException("you can’t change this card position!");
        else if (input.matches("^[ ]*attack [\\d]+[ ]*$"))
            throw new MyException("you can’t attack with this card");
        else if (input.matches("^[ ]*attack direct[ ]*$"))
            throw new MyException("you can’t attack with this card");
        else if (input.matches("^[ ]*activate effect[ ]*$"))
            throw new MyException("You can only activate spell card!");
        else if (input.matches("^[ ]*show graveyard[ ]*$"))
            Board.showGraveyard();
        else if (input.matches("^[ ]*card show --selected[ ]*$"))
            Game.getMainPhase1().showSelectedCard(CommandMatcher.getCommandMatcher(selectedCard, "(^[ ]*select --hand [\\d]+[ ]*$)"));
        else if (input.matches("^[ ]*surrender[ ]*$"))
            surrender();
        else
            throw new MyException("invalid command!");
    }

    public void whatIsSet(Matcher matcher) {
        if (matcher.find()) {
            String whatKind = Game.whoseTurnPlayer().whatKindaCardIsInThisAddress(matcher.group(1));
            if (whatKind.equals("Monster")) {
                Game.getMainPhase1().setMonster(CommandMatcher.getCommandMatcher(matcher.group(1), "(^[ ]*select --hand [\\d]+[ ]*$)"));
            } else if (whatKind.equals("Trap")) {
                Game.getMainPhase1().setTrap(CommandMatcher.getCommandMatcher(matcher.group(1), "(^[ ]*select --hand [\\d]+[ ]*$)"));
            } else if (whatKind.equals("Spell")) {
                Game.getMainPhase1().setSpell(CommandMatcher.getCommandMatcher(matcher.group(1), "(^[ ]*select --hand [\\d]+[ ]*$)"));
            }
        }
    }

    public void monsterSet(Matcher matcher) throws MyException {
        if (matcher.find()) {
            if (!Game.whoseTurnPlayer().isMonsterZoneFull()) {
                if (!Game.whoseTurnPlayer().isHeSummonedOrSet()) {
                    Address address = new Address(matcher.group(1));
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
    }

    public void trapSet(Matcher matcher) throws MyException {
        if (matcher.find()) {
            Player currentPlayer = Game.whoseTurnPlayer();
            if (!currentPlayer.isSpellZoneFull()) {
                Address address = new Address(matcher.group(1));
                activateSomeOfTraps(address, currentPlayer.getTrapCardByAddress(address), currentPlayer);
                currentPlayer.setCardFromHandToSpellZone(matcher.group(1));
            } else throw new MyException("spell card zone is full!");
        }
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
            if (trapCard.getName().equals("Call of the Haunted")) {
                if (BattlePhase.getInstance().getPermissionForTrap("Call of the Haunted", true)) {
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

    public void spellSet(Matcher matcher) throws MyException {
        if (matcher.find()) {
            Player currentPlayer = Game.whoseTurnPlayer();
            if (!currentPlayer.isSpellZoneFull()) {
                currentPlayer.setCardFromHandToSpellZone(matcher.group(1));
            } else throw new MyException("spell card zone is full!");
        }
    }

    public void summonControl(Matcher matcher) throws MyException {
        if (matcher.find()) {
            Player currentPlayer = Game.whoseTurnPlayer();
            if (currentPlayer.getMonsterCardByStringAddress(matcher.group(1)) == null)
                throw new MyException("you cant summon spell or trap");
            if (!currentPlayer.isMonsterZoneFull()) {
                if (!currentPlayer.isHeSummonedOrSet()) {
                    Address address = new Address(matcher.group(1));
                    MonsterCard monsterCard = Game.whoseTurnPlayer().getMonsterCardByStringAddress(matcher.group(1));
                    int level = monsterCard.getLevel();
                    if (!currentPlayer.getMonsterCardByStringAddress(matcher.group(1)).isRitual()) {
                        if (monsterCard.getName().equals("Gate Guardian")) {
                            if (!MainPhase.doSolemnWarningEffect(address))
                                Game.getMainPhase1().summonForTribute(3, matcher.group(1));
                        } else {
                            if (!MainPhase.doSolemnWarningEffect(address)) {
                                if (level <= 4) summonALowLevelMonster(matcher, currentPlayer, address);
                                else if (level <= 6) Game.getMainPhase1().summonForTribute(1, matcher.group(1));
                                else {
                                    int index = Game.whoseTurnPlayer().getIndexOfThisCardByAddress(address);
                                    if (Game.whoseTurnPlayer().getMonsterCardByAddress(address).getName().equals("Beast King Barbaros")
                                            && (Integer.parseInt(Effect.run("Beast King Barbaros")) == 3)) {
                                        Game.whoseTurnPlayer().setDidBeastKingBarbarosSummonedSuperHighLevel(true, index);
                                        Game.getMainPhase1().summonForTribute(3, matcher.group(1));
                                    } else Game.getMainPhase1().summonForTribute(2, matcher.group(1));
                                }
                            }
                        }
                    } else Game.getMainPhase1().ritualSummon(matcher.group(1), level);
                } else throw new MyException("you already summoned/set on this turn!");
            } else throw new MyException("monster card zone is full!");
        }
    }

    public void summonALowLevelMonster(Matcher matcher, Player currentPlayer, Address address) {
        MonsterCard monsterCard = currentPlayer.getMonsterCardByAddress(address);
        if (currentPlayer.getMonsterCardByAddress(address).getName().equals("Scanner")) {
            currentPlayer.summonCardToMonsterZone(matcher.group(1)).setIsScanner(true);
        } else if (currentPlayer.getMonsterCardByAddress(address).getName().equals("Terratiger, the Empowered Warrior")) {
            if (Integer.parseInt(Effect.run("Terratiger, the Empowered Warrior")) != 0) {
                Address address1 = new Address(Integer.parseInt(Effect.run("Terratiger, the Empowered Warrior")), "hand", true);
                if ((currentPlayer.getMonsterCardByAddress(address1) != null)
                        && (currentPlayer.getMonsterCardByAddress(address1).getLevel() <= 4)
                        && (!currentPlayer.isMonsterZoneFull()))
                    currentPlayer.setCardFromHandToMonsterZone(address1);
            }
        } else currentPlayer.summonCardToMonsterZone(matcher.group(1));
        if (Game.whoseRivalPlayer().doIHaveSpellCard("Trap Hole") && !Game.whoseTurnPlayer().doIHaveMirageDragonMonster()) {
            if (monsterCard.getNormalAttack() >= 1000) {
                currentPlayer.removeCard(address);
                Game.whoseRivalPlayer().removeOneOfTrapOrSpell("Trap Hole");
            }
        }
        if (Game.whoseRivalPlayer().doIHaveSpellCard("Torrential Tribute") && !Game.whoseTurnPlayer().doIHaveMirageDragonMonster()) {
            Attack.destroyAllMonstersInTheBoard();
            Game.whoseRivalPlayer().removeOneOfTrapOrSpell("Torrential Tribute");
//                                System.out.println("All monster card got destroyed by effect of Torrential Tribute effect.");
        }
        currentPlayer.setHeSummonedOrSet(true);
    }

    public void summonAMediumLevelMonster(String address) throws MyException {
        Address address1 = new Address(address);
        Player currentPlayer = Game.whoseTurnPlayer();
        MonsterCard monsterCard = currentPlayer.getMonsterCardByAddress(address1);
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
                            currentPlayer.removeCard(address1);
                            Game.whoseRivalPlayer().removeOneOfTrapOrSpell("Trap Hole");
//                        System.out.println("The summoned card got destroyed by effect of Trap Hole effect.");
                        }
                    }
                    if (Game.whoseRivalPlayer().doIHaveSpellCard("Torrential Tribute") && !Game.whoseTurnPlayer().doIHaveMirageDragonMonster()) {
                        Attack.destroyAllMonstersInTheBoard();
                        Game.whoseRivalPlayer().removeOneOfTrapOrSpell("Torrential Tribute");
//                        System.out.println("All monster card got destroyed by effect of Torrential Tribute effect.");
                    }
                } else throw new MyException("There is no monster in this address!");
            }
        } else throw new MyException("there are not enough cards for tribute");
    }

    public void summonAHighLevelMonster(String address) throws MyException {
        Address address1 = new Address(address);
        Player currentPlayer = Game.whoseTurnPlayer();
        MonsterCard monsterCard = currentPlayer.getMonsterCardByAddress(address1);
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
                                currentPlayer.removeCard(address1);
                                Game.whoseRivalPlayer().removeOneOfTrapOrSpell("Trap Hole");
                                //System.out.println("The summmoned card got destroyed by effect of Trap Hole efffect.");
                            }
                        }
                        if (Game.whoseRivalPlayer().doIHaveSpellCard("Torrential Tribute") && !Game.whoseTurnPlayer().doIHaveMirageDragonMonster()) {
                            Attack.destroyAllMonstersInTheBoard();
                            Game.whoseRivalPlayer().removeOneOfTrapOrSpell("Torrential Tribute");
                            //System.out.println("All monster card got destroyed by effect of Torrential Tribute efffect.");
                        }
                    } else throw new MyException("There is no monster in this address!");
                }
            }
        } else throw new MyException("there are not enough cards for tribute");
    }

    public void summonASuperHighLevelMonster(String address) throws MyException  {
        Address address1 = new Address(address);
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
                                currentPlayer.removeCard(address1);
                                Game.whoseRivalPlayer().removeOneOfTrapOrSpell("Trap Hole");
                                //System.out.println("The summoned card got destroyed by effect of Trap Hole effect.");
                            }
                            if (Game.whoseRivalPlayer().doIHaveSpellCard("Torrential Tribute") && !Game.whoseTurnPlayer().doIHaveMirageDragonMonster()) {
                                Attack.destroyAllMonstersInTheBoard();
                                Game.whoseRivalPlayer().removeOneOfTrapOrSpell("Torrential Tribute");
                                //System.out.println("All monster card got destroyed by effect of Torrential Tribute effect.");
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

    public void flipSummon(Matcher matcher) throws MyException {
        if (matcher.find()) {
            Player currentPlayer = Game.whoseTurnPlayer();
            if (currentPlayer.isThisMonsterOnDHPosition(matcher.group(1))) {
                currentPlayer.convertThisMonsterFromDHToOO(matcher.group(1));
                Address address = new Address(matcher.group(1));
                MonsterCard monsterCard = currentPlayer.getMonsterCardByAddress(address);
                if (currentPlayer.getMonsterCardByAddress(address).getName().equals("Man-Eater Bug")) {
                    doManEaterBugEffect();
                }
                if (Game.whoseRivalPlayer().doIHaveSpellCard("Trap Hole")) {
                    if (monsterCard.getNormalAttack() >= 1000) {
                        currentPlayer.removeCard(address);
                        Game.whoseRivalPlayer().removeOneOfTrapOrSpell("Trap Hole");
//                    System.out.println("The summoned card got destroyed by effect of Trap Hole effect.");
                    }
                }
            } else throw new MyException("You cant do this action in this phase!");
        }
    }

    public void setPosition(String input, Matcher matcher) throws MyException {
        Player currentPlayer = Game.whoseTurnPlayer();
        if (matcher.find()) {
            int index = currentPlayer.getIndexOfThisCardByAddress(new Address(matcher.group(1)));
            Matcher matcher1 = CommandMatcher.getCommandMatcher(input, "^[ ]*set -- position (attack|defense)[ ]*$");
            if (matcher1.find()) {
                if (matcher1.group(1).equals("attack")) {
                    if (!currentPlayer.isThisMonsterOnAttackPosition(matcher.group(1))) {
                        if (!currentPlayer.didWeChangePositionThisCardInThisTurn(index)) {
                            currentPlayer.setDidWeChangePositionThisCardInThisTurn(index);
                            currentPlayer.convertThisMonsterFromDefenceToAttack(matcher.group(1));
                        } else
                            throw new MyException("you already changed this card position in this turn");
                    } else throw new MyException("This card is already in the wanted position!");
                } else {
                    if (currentPlayer.isThisMonsterOnAttackPosition(matcher.group(1))) {
                        if (!currentPlayer.didWeChangePositionThisCardInThisTurn(index)) {
                            currentPlayer.setDidWeChangePositionThisCardInThisTurn(index);
                            currentPlayer.convertThisMonsterFromAttackToDefence(matcher.group(1));
                        } else
                            throw new MyException("you already changed this card position in this turn");
                    } else throw new MyException("This card is already in the wanted position!");
                }
            }
        }
    }

    public void activeSpell(Matcher matcher) throws MyException {
        if (matcher.find()) {
            String stringAddress = matcher.group(1);
            Address address = new Address(stringAddress);
            Player currentPlayer = Game.whoseTurnPlayer();
            int index = currentPlayer.getIndexOfThisCardByAddress(address);
            if (!currentPlayer.isThisSpellActivated(index)) {
                if (currentPlayer.getSpellCardByStringAddress(stringAddress).getSpellMode() == SpellMode.FIELD)
                    activateThisKindOfAddress(stringAddress, address, currentPlayer, index, "field");
                else if (currentPlayer.getSpellCardByStringAddress(stringAddress).getSpellMode() == SpellMode.EQUIP)
                    activateThisKindOfAddress(stringAddress, address, currentPlayer, index, "equip");
                else if (!(currentPlayer.isSpellZoneFull()))
                    activateThisKindOfAddress(stringAddress, address, currentPlayer, index, "spell");
                else throw new MyException("spell card zone is full!");
            } else throw new MyException("you have already activated this card");
        }
    }

    private void activateThisKindOfAddress(String stringAddress, Address address, Player currentPlayer, int index, String cardState) throws MyException {
        if (SpellCard.canWeActivateThisSpell(address)) {
            if (Game.whoseRivalPlayer().doIHaveSpellCard("Magic Jammer")
                    && !Game.whoseTurnPlayer().doIHaveMirageDragonMonster()
                    && BattlePhase.getInstance().getPermissionForTrap("Magic Jammer", false)) {
                if (MainPhase.removeCardFromMyHand()) Game.whoseTurnPlayer().removeCard(address);
            } else {
                currentPlayer.setIsSpellFaceUp(address.getNumber(), true);
                currentPlayer.setIsThisSpellActivated(true, index);
                SpellCard.doSpellAbsorptionEffect();
                currentPlayer.getSpellCardByStringAddress(stringAddress).doEffect(address);
                //currentPlayer.addCardToAddress(Board.getCardByAddress(address), cardState, index)
            }
        } else throw new MyException("preparations of this spell are not done yet");
    }

    public void doEffectMainPhase() {
        Player currentPlayer = Game.whoseTurnPlayer();
        if (currentPlayer.doWeHaveThisCardInBoard("Scanner")) {
            Address address = new Address(Integer.parseInt(Effect.run("Scanner")), "graveyard", false);
            if (Board.getCardByAddress(address).getKind().equals("monster")) {
                MonsterCard monsterCard = Game.whoseTurnPlayer().getMonsterCardByAddress(address);
                //address.setIfItIsScannerThenWhat(monsterCard);
            }
        }
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

    private void forcedIncreaseLP(String input) {
        Matcher matcher = CommandMatcher.getCommandMatcher(input, "increase --LP ([\\d]+)");
        matcher.find();
        int LP = Integer.parseInt(matcher.group(1));
        Game.whoseTurnPlayer().increaseLP(LP);
    }

    private void forcedSetWinner() {
        Game.setWinner(Game.whoseTurnPlayer());
        Game.playTurn("EndGame");
    }

    public int minOfTwoNumber(int num1, int num2) {
        if (num1 <= num2) return num1;
        return num2;
    }

    public void surrender() {
        Game.setIsSurrender(true);
        Game.setWinner(Game.whoseRivalPlayer());
        Game.playTurn("EndGame");
    }


    public boolean activeTrap(Matcher matcher, Player player) {
        if (!player.doIHaveMirageDragonMonster()) if (Game.getMainPhase1().permission()) return true;
        return false;
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
