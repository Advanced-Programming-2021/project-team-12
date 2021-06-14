package controllers;

import controllers.move.Attack;
import controllers.move.SetSpell;
import controllers.phase.MainPhase;
import models.*;
import Exceptions.*;
import Utility.*;
import models.card.monster.MonsterCard;
import models.card.spell.SpellCard;
import models.card.spell.SpellMode;
import models.card.trap.TrapCard;
import view.Effect;
import view.Game;
import view.Main;

import java.util.regex.Matcher;

public class PhaseControl {
    private static PhaseControl instance;

    public static PhaseControl getInstance() {
        if (instance == null) {
            instance = new PhaseControl();
        }
        return instance;
    }

    public void payMessengerOfPeaceSpellCardHarm(String answer) {
        if (SetSpell.doIHaveMessengerOfPeace()) {
            if (answer.equals("yes")) SetSpell.destroyMessengerOfPeace();
            else Game.whoseTurnPlayer().decreaseLP(100);
        }
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
        if (Game.firstPlayer.getLP() < 0) {
            Game.setWinner(Game.firstPlayer);
        } else if (Game.secondPlayer.getLP() < 0) {
            Game.setWinner(Game.secondPlayer);
        }
    }

    public String drawOneCard() {
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
                return "second player is the winner";
            } else if (!newAddedCard.equals("Hand is full")) {
                return "second player cannot draw a card,new card added to the hand : " + newAddedCard;
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

    public void checkInputNonCardSelected(String input) throws NoSelectedCardException, InvalidCommandException, BreakException, InvalidCardSelection {
        if (input.matches("^[ ]*next phase[ ]*$"))
            throw new BreakException("next phase!");
        else if (input.matches("^[ ]*select [.*][ ]*$"))
            whatIsSelected(input);
        else if (input.matches("^[ ]*summon[ ]*$"))
            throw new NoSelectedCardException("no card is selected!");
        else if (input.matches("^[ ]*set[ ]*$"))
            throw new NoSelectedCardException("no card is selected!");
        else if (input.matches("^[ ]*set -- position (attack|defense)[ ]*$"))
            throw new NoSelectedCardException("no card is selected!");
        else if (input.matches("^[ ]*flip-summon[ ]*$"))
            throw new NoSelectedCardException("no card is selected!");
        else if (input.matches("^[ ]*attack [\\d]+[ ]*$"))
            throw new NoSelectedCardException("no card is selected!");
        else if (input.matches("^[ ]*attack direct[ ]*$"))
            throw new NoSelectedCardException("no card is selected!");
        else if (input.matches("^[ ]*activate effect[ ]*$"))
            throw new NoSelectedCardException("no card is selected!");
        else if (input.matches("^[ ]*show graveyard[ ]*$"))
            Board.showGraveyard();
        else if (input.matches("^[ ]*card show --selected[ ]*$"))
            throw new NoSelectedCardException("no card is selected!");
        else if (input.matches("^[ ]*surrender[ ]*$"))
            surrender();
        else
            throw new InvalidCommandException("invalid command!");
    }

    public void whatIsSelected(String input) throws NoSelectedCardException, InvalidCardSelection {
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
            throw new NoSelectedCardException("no card is selected!");
        else if (Address.isAddressValid(input))
            throw new InvalidCardSelection("invalid card selection!");
        else
            throw new InvalidCardSelection("invalid card selection!");
    }

    public void monsterCardSelected(String input, String selectedCard) throws InvalidCommandException, BreakException, CantSummonThisCard, CantSetThisCard, CantActivateEffect {
        if (input.matches("^[ ]*next phase[ ]*$")) {
            throw new BreakException("next phase!");
        } else if (input.matches("^[ ]*select -d[ ]*$"))
            Game.getMainPhase1().getSelectedCard();
        else if (input.matches("^[ ]*summon[ ]*$"))
            throw new CantSummonThisCard("You can't summon this card!");
        else if (input.matches("^[ ]*set[ ]*$"))
            throw new CantSetThisCard("You can't set this card!");
        else if (input.matches("^[ ]*set -- position (attack|defense)[ ]*$"))
            Game.getMainPhase1().setPosition(input, CommandMatcher.getCommandMatcher(selectedCard, "(^[ ]*select --monster ([\\d]+)[ ]*$)"));
        else if (input.matches("^[ ]*flip-summon[ ]*$"))
            Game.getMainPhase1().flipSummon(CommandMatcher.getCommandMatcher(selectedCard, "(^[ ]*select --monster ([\\d]+)[ ]*$)"));
        else if (input.matches("^[ ]*attack [\\d]+[ ]*$"))
            Game.getMainPhase1().attack(CommandMatcher.getCommandMatcher(input, "(^[ ]*attack ([\\d]+)[ ]*$)"));
        else if (input.matches("^[ ]*attack direct[ ]*$"))
            Game.getMainPhase1().directAttack(CommandMatcher.getCommandMatcher(selectedCard, "(^[ ]*select --monster ([\\d]+)[ ]*$)"));
        else if (input.matches("^[ ]*activate effect[ ]*$"))
            throw new CantActivateEffect("You can only activate spell card!");
        else if (input.matches("^[ ]*show graveyard[ ]*$"))
            Board.showGraveyard();
        else if (input.matches("^[ ]*card show --selected[ ]*$"))
            Game.getMainPhase1().showSelectedCard(CommandMatcher.getCommandMatcher(selectedCard, "(^[ ]*select --monster ([\\d]+)[ ]*$)"));
        else if (input.matches("^[ ]*surrender[ ]*$"))
            surrender();
        else
            throw new InvalidCommandException("invalid command!");
    }

    public void OpponentMonstercardSelected(String input, String selectedCard) throws InvalidCommandException, BreakException, CantSummonThisCard, CantSetThisCard, CantActivateEffect, CantChangeCardPosition, CantAttack {
        if (input.matches("^[ ]*next phase[ ]*$")) {
            throw new BreakException("next phase!");
        } else if (input.matches("^[ ]*select -d[ ]*$"))
            Game.getMainPhase1().getSelectedCard();
        else if (input.matches("^[ ]*summon[ ]*$"))
            throw new CantSummonThisCard("You can't summon this card!");
        else if (input.matches("^[ ]*set[ ]*$"))
            throw new CantSetThisCard("You can't set this card!");
        else if (input.matches("^[ ]*set -- position (attack|defense)[ ]*$"))
            throw new CantChangeCardPosition("you can’t change this card position!");
        else if (input.matches("^[ ]*flip-summon[ ]*$"))
            throw new CantChangeCardPosition("you can’t change this card position!");
        else if (input.matches("^[ ]*attack [\\d]+[ ]*$"))
            throw new CantAttack("you can’t attack with this card");
        else if (input.matches("^[ ]*attack direct[ ]*$"))
            throw new CantAttack("you can’t attack with this card");
        else if (input.matches("^[ ]*activate effect[ ]*$"))
            throw new CantActivateEffect("You can only activate spell card!");
        else if (input.matches("^[ ]*show graveyard[ ]*$"))
            Board.showGraveyard();
        else if (input.matches("^[ ]*card show --selected[ ]*$"))
            Game.getMainPhase1().showSelectedCard(CommandMatcher.getCommandMatcher(selectedCard, "(^[ ]*select --monster --opponent [\\d]+[ ]*$)"));
        else if (input.matches("^[ ]*surrender[ ]*$"))
            surrender();
        else
            throw new InvalidCommandException("invalid command!");
    }

    public void spellSelected(String input, String selectedCard) throws InvalidCommandException, BreakException, CantSummonThisCard, CantSetThisCard, CantChangeCardPosition, CantAttack {
        if (input.matches("^[ ]*next phase[ ]*$")) {
            throw new BreakException("next phase!");
        } else if (input.matches("^[ ]*select -d[ ]*$"))
            Game.getMainPhase1().getSelectedCard();
        else if (input.matches("^[ ]*summon[ ]*$"))
            throw new CantSummonThisCard("You can't summon this card!");
        else if (input.matches("^[ ]*set[ ]*$"))
            throw new CantSetThisCard("You can't set this card!");
        else if (input.matches("^[ ]*set -- position (attack|defense)[ ]*$"))
            throw new CantChangeCardPosition("you can’t change this card position!");
        else if (input.matches("^[ ]*flip-summon[ ]*$"))
            throw new CantChangeCardPosition("you can’t change this card position!");
        else if (input.matches("^[ ]*attack [\\d]+[ ]*$"))
            throw new CantAttack("you can’t attack with this card");
        else if (input.matches("^[ ]*attack direct[ ]*$"))
            throw new CantAttack("you can’t attack with this card");
        else if (input.matches("^[ ]*activate effect[ ]*$"))
            Game.getMainPhase1().activeSpell(CommandMatcher.getCommandMatcher(selectedCard, "(^[ ]*select --spell ([\\d]+)[ ]*$)"));
        else if (input.matches("^[ ]*show graveyard[ ]*$"))
            Board.showGraveyard();
        else if (input.matches("^[ ]*card show --selected[ ]*$"))
            Game.getMainPhase1().showSelectedCard(CommandMatcher.getCommandMatcher(selectedCard, "(^[ ]*select --spell ([\\d]+)[ ]*$)"));
        else if (input.matches("^[ ]*surrender[ ]*$"))
            surrender();
        else
            throw new InvalidCommandException("invalid command!");
    }

    public void OpponentSpellCardSelected(String input, String selectedCard) throws InvalidCommandException, BreakException, CantSummonThisCard, CantSetThisCard, CantActivateEffect, CantChangeCardPosition, CantAttack {
        if (input.matches("^[ ]*next phase[ ]*$")) {
            throw new BreakException("next phase!");
        } else if (input.matches("^[ ]*select -d[ ]*$"))
            Game.getMainPhase1().getSelectedCard();
        else if (input.matches("^[ ]*summon[ ]*$"))
            throw new CantSummonThisCard("You can't summon this card!");
        else if (input.matches("^[ ]*set[ ]*$"))
            throw new CantSetThisCard("You can't set this card!");
        else if (input.matches("^[ ]*set -- position (attack|defense)[ ]*$"))
            throw new CantChangeCardPosition("you can’t change this card position!");
        else if (input.matches("^[ ]*flip-summon[ ]*$"))
            throw new CantChangeCardPosition("you can’t change this card position!");
        else if (input.matches("^[ ]*attack [\\d]+[ ]*$"))
            throw new CantAttack("you can’t attack with this card");
        else if (input.matches("^[ ]*attack direct[ ]*$"))
            throw new CantAttack("you can’t attack with this card");
        else if (input.matches("^[ ]*activate effect[ ]*$"))
            throw new CantActivateEffect("You can only activate spell card!");
        else if (input.matches("^[ ]*show graveyard[ ]*$"))
            Board.showGraveyard();
        else if (input.matches("^[ ]*card show --selected[ ]*$"))
            Game.getMainPhase1().showSelectedCard(CommandMatcher.getCommandMatcher(selectedCard, "(^[ ]*select --spell --opponent ([\\d]+)[ ]*$)"));
        else if (input.matches("^[ ]*surrender[ ]*$"))
            surrender();
        else
            throw new InvalidCommandException("invalid command!");
    }

    public void fieldCardSelected(String input) throws InvalidCommandException, BreakException, CantSummonThisCard, CantSetThisCard, CantActivateEffect, CantChangeCardPosition, CantAttack {
        if (input.matches("^[ ]*next phase[ ]*$")) {
            throw new BreakException("next phase!");
        } else if (input.matches("^[ ]*select -d[ ]*$"))
            Game.getMainPhase1().getSelectedCard();
        else if (input.matches("^[ ]*summon[ ]*$"))
            throw new CantSummonThisCard("You can't summon this card!");
        else if (input.matches("^[ ]*set[ ]*$"))
            throw new CantSetThisCard("You can't set this card!");
        else if (input.matches("^[ ]*set -- position (attack|defense)[ ]*$"))
            throw new CantChangeCardPosition("you can’t change this card position!");
        else if (input.matches("^[ ]*flip-summon[ ]*$"))
            throw new CantChangeCardPosition("you can’t change this card position!");
        else if (input.matches("^[ ]*attack [\\d]+[ ]*$"))
            throw new CantAttack("you can’t attack with this card");
        else if (input.matches("^[ ]*attack direct[ ]*$"))
            throw new CantAttack("you can’t attack with this card");
        else if (input.matches("^[ ]*activate effect[ ]*$"))
            throw new CantActivateEffect("You can only activate spell card!");
        else if (input.matches("^[ ]*show graveyard[ ]*$"))
            Board.showGraveyard();
        else if (input.matches("^[ ]*card show --selected[ ]*$"))
            Game.getMainPhase1().showFieldZoneCard();
        else if (input.matches("^[ ]*surrender[ ]*$"))
            surrender();
        else
            throw new InvalidCommandException("invalid command!");
    }

    public void opponentFieldCardSelected(String input) throws InvalidCommandException, BreakException, CantSummonThisCard, CantSetThisCard, CantActivateEffect, CantChangeCardPosition, CantAttack {
        if (input.matches("^[ ]*next phase[ ]*$")) {
            throw new BreakException("next phase!");
        } else if (input.matches("^[ ]*select -d[ ]*$"))
            Game.getMainPhase1().getSelectedCard();
        else if (input.matches("^[ ]*summon[ ]*$"))
            throw new CantSummonThisCard("You can't summon this card!");
        else if (input.matches("^[ ]*set[ ]*$"))
            throw new CantSetThisCard("You can't set this card!");
        else if (input.matches("^[ ]*set -- position (attack|defense)[ ]*$"))
            throw new CantChangeCardPosition("you can’t change this card position!");
        else if (input.matches("^[ ]*flip-summon[ ]*$"))
            throw new CantChangeCardPosition("you can’t change this card position!");
        else if (input.matches("^[ ]*attack [\\d]+[ ]*$"))
            throw new CantAttack("you can’t attack with this card");
        else if (input.matches("^[ ]*attack direct[ ]*$"))
            throw new CantAttack("you can’t attack with this card");
        else if (input.matches("^[ ]*activate effect[ ]*$"))
            throw new CantActivateEffect("You can only activate spell card!");
        else if (input.matches("^[ ]*show graveyard[ ]*$"))
            Board.showGraveyard();
        else if (input.matches("^[ ]*card show --selected[ ]*$"))
            Game.getMainPhase1().showOpponentFieldZoneCard();
        else if (input.matches("^[ ]*surrender[ ]*$"))
            surrender();
        else
            throw new InvalidCommandException("invalid command!");
    }

    public void handSelected(String input, String selectedCard) throws InvalidCommandException, BreakException, CantActivateEffect, CantChangeCardPosition, CantAttack {
        if (input.matches("^[ ]*next phase[ ]*$")) {
            throw new BreakException("next phase!");
        } else if (input.matches("^[ ]*select -d[ ]*$"))
            Game.getMainPhase1().getSelectedCard();
        else if (input.matches("^[ ]*summon[ ]*$"))
            Game.getMainPhase1().summon(CommandMatcher.getCommandMatcher(selectedCard, "(^[ ]*select --hand [\\d]+[ ]*$)"));
        else if (input.matches("^[ ]*set[ ]*$"))
            whatIsSet(CommandMatcher.getCommandMatcher(selectedCard, "(^[ ]*select --hand ([\\d]+)[ ]*$)"));
        else if (input.matches("^[ ]*set -- position (attack|defense)[ ]*$"))
            throw new CantChangeCardPosition("you can’t change this card position!");
        else if (input.matches("^[ ]*flip-summon[ ]*$"))
            throw new CantChangeCardPosition("you can’t change this card position!");
        else if (input.matches("^[ ]*attack [\\d]+[ ]*$"))
            throw new CantAttack("you can’t attack with this card");
        else if (input.matches("^[ ]*attack direct[ ]*$"))
            throw new CantAttack("you can’t attack with this card");
        else if (input.matches("^[ ]*activate effect[ ]*$"))
            throw new CantActivateEffect("You can only activate spell card!");
        else if (input.matches("^[ ]*show graveyard[ ]*$"))
            Board.showGraveyard();
        else if (input.matches("^[ ]*card show --selected[ ]*$"))
            Game.getMainPhase1().showSelectedCard(CommandMatcher.getCommandMatcher(selectedCard, "(^[ ]*select --hand [\\d]+[ ]*$)"));
        else if (input.matches("^[ ]*surrender[ ]*$"))
            surrender();
        else
            throw new InvalidCommandException("invalid command!");
    }

    public void whatIsSet(Matcher matcher) {
        if (matcher.find()) {
            String whatKind = Game.whoseTurnPlayer().whatKindaCardIsInThisAddress(matcher.group(1));
            if (whatKind.equals("monster")) {
                Game.getMainPhase1().setMonster(CommandMatcher.getCommandMatcher(matcher.group(1), "(^[ ]*select --hand [\\d]+[ ]*$)"));
            } else if (whatKind.equals("trap")) {
                Game.getMainPhase1().setTrap(CommandMatcher.getCommandMatcher(matcher.group(1), "(^[ ]*select --hand [\\d]+[ ]*$)"));
            } else if (whatKind.equals("spell")) {
                Game.getMainPhase1().setSpell(CommandMatcher.getCommandMatcher(matcher.group(1), "(^[ ]*select --hand [\\d]+[ ]*$)"));
            }
        }
    }

    public void monsterSet(Matcher matcher) throws MonsterZoneFull, AlreadySummonedOrSet {
        if (matcher.find()) {
            if (!Game.whoseTurnPlayer().isMonsterZoneFull()) {
                if (!Game.whoseTurnPlayer().isHeSummonedOrSet()) {
                    Address address = new Address(matcher.group(1));
                    if (Game.whoseTurnPlayer().getMonsterCardByAddress(address).getName().equals("Scanner")) {
                        Game.whoseTurnPlayer().setCardFromHandToMonsterZone(address).setIsScanner(true);
                    } else Game.whoseTurnPlayer().setCardFromHandToMonsterZone(address);
                    Game.whoseTurnPlayer().setHeSummonedOrSet(true);
                } else {
                    throw new AlreadySummonedOrSet("you already summoned/set on this turn!");
                }
            } else {
                throw new MonsterZoneFull("monster card zone is full!");
            }
        }
    }

    public void trapSet(Matcher matcher) throws SpellZoneFull {
        if (matcher.find()) {
            if (!Game.whoseTurnPlayer().isSpellZoneFull()) {
                Game.whoseTurnPlayer().setCardFromHandToSpellZone(matcher.group(1));
                Game.whoseTurnPlayer().setHeSummonedOrSet(true);
            } else throw new SpellZoneFull("spell card zone is full!");
        }
    }

    public void spellSet(Matcher matcher) throws SpellZoneFull, RitualSummonException, CantRitualSummon {
        if (matcher.find()) {
            Player currentPlayer = Game.whoseTurnPlayer();
            if (!currentPlayer.isSpellZoneFull()) {
                if (currentPlayer.getSpellCardByStringAddress(matcher.group(1)).getSpellMode() != SpellMode.RITUAL) {
                    currentPlayer.setCardFromHandToSpellZone(matcher.group(1));
                    currentPlayer.setHeSummonedOrSet(true);
                } else {
                    if (currentPlayer.isThereAnyRitualTypeMonsterInOurHand()
                            && currentPlayer.isOneOfLevelOfRitualMonstersInTheHandIsEqualToSumOfLevelOfSubsetOfMonsterZone()) {
                        throw new RitualSummonException("ritual card!");
                    } else throw new CantRitualSummon("there is no way you could ritual summon a monster");
                }
            } else throw new SpellZoneFull("spell card zone is full!");
        }
    }

    public void summonControl(Matcher matcher) throws MonsterZoneFull, AlreadySummonedOrSet, CancelException, NotEnoughTribute, NoMonsterInThisAddress {
        if (matcher.find()) {
            Player currentPlayer = Game.whoseTurnPlayer();
            if (!currentPlayer.isMonsterZoneFull()) {
                if (!currentPlayer.isHeSummonedOrSet()) {
                    if (!currentPlayer.getMonsterCardByStringAddress(matcher.group(1)).isRitual()) {
                        Address address = new Address(matcher.group(1));
                        int level = Game.whoseTurnPlayer().getMonsterCardByStringAddress(matcher.group(1)).getLevel();
                        if (level <= 4) {
                            currentPlayer.setHeSummonedOrSet(true);
                            if (currentPlayer.getMonsterCardByAddress(address).getName().equals("Scanner")) {
                                currentPlayer.summonCardFromHandToMonsterZone(matcher.group(1)).setIsScanner(true);
                            } else currentPlayer.summonCardFromHandToMonsterZone(matcher.group(1));
                            if (currentPlayer.getMonsterCardByAddress(address).getName().equals("Terratiger")) {
                                if (Integer.parseInt(Effect.run("Terratiger")) != 0) {
                                    Address address1 = new Address(Integer.parseInt(Effect.run("Terratiger")), "hand", true);
                                    if ((currentPlayer.getMonsterCardByAddress(address1) != null)
                                            && (currentPlayer.getMonsterCardByAddress(address1).getLevel() <= 4)
                                            && (!currentPlayer.isMonsterZoneFull()))
                                        currentPlayer.setCardFromHandToMonsterZone(address1);
                                }
                            }
                            if (Game.whoseRivalPlayer().doIHaveTrapHoleTrapOnTheBoard()) {
                                currentPlayer.removeCard(address);
                                Game.whoseRivalPlayer().removeOneOfMyTrapHoleTrapOnTheBoard();
//                                System.out.println("The summoned card got destroyed by effect of Trap Hole effect.");
                            }
                            if (Game.whoseRivalPlayer().doIHaveTorrentialTributeTrapOnTheBoard()) {
                                Attack.destroyAllMonstersInTheBoard();
                                Game.whoseRivalPlayer().removeOneOfMyTorrentialTributeTrapOnTheBoard();
//                                System.out.println("All monster card got destroyed by effect of TorrentialTribute effect.");
                            }
                        } else if (level <= 6) {
                            summonAMediumLevelMonster(matcher.group(1));
                        } else {
                            int index = Game.whoseTurnPlayer().getIndexOfThisCardByAddress(address);
                            if (Game.whoseTurnPlayer().getMonsterCardByAddress(address).getName().equals("BeastKingBarbaros")
                                    && (Integer.parseInt(Effect.run("BeastKingBarbaros")) == 3)) {
                                Game.whoseTurnPlayer().setDidBeastKingBarbarosSummonedSuperHighLevel(true, index);
                                summonASuperHighLevelMonster(matcher.group(1));
                            } else summonAHighLevelMonster(matcher.group(1));
                        }
                    } else Game.getMainPhase1().ritualSummon(matcher);
                } else throw new AlreadySummonedOrSet("you already summoned/set on this turn!");
            } else throw new MonsterZoneFull("monster card zone is full!");
        }
    }

    public void summonAMediumLevelMonster(String address) throws NotEnoughTribute, CancelException, NoMonsterInThisAddress {
        Address address1 = new Address(address);
        Player currentPlayer = Game.whoseTurnPlayer();
        if (currentPlayer.isThereAnyCardInMonsterZone()) {
            String tributeCard = Game.getMainPhase1().getTributeCard();
            if (tributeCard.equals("cancel")) {
                throw new CancelException("canceled successfully!");
            } else {
                if (currentPlayer.isMonsterInThisMonsterZoneTypeAddress(Integer.parseInt(tributeCard))) {
                    currentPlayer.setHeSummonedOrSet(true);
                    currentPlayer.removeThisMonsterZoneTypeAddressForTribute(Integer.parseInt(tributeCard));
                    currentPlayer.summonCardFromHandToMonsterZone(address);
                    if (Game.whoseRivalPlayer().doIHaveTrapHoleTrapOnTheBoard()) {
                        currentPlayer.removeCard(address1);
                        Game.whoseRivalPlayer().removeOneOfMyTrapHoleTrapOnTheBoard();
//                        System.out.println("The summoned card got destroyed by effect of Trap Hole effect.");
                    }
                    if (Game.whoseRivalPlayer().doIHaveTorrentialTributeTrapOnTheBoard()) {
                        Attack.destroyAllMonstersInTheBoard();
                        Game.whoseRivalPlayer().removeOneOfMyTorrentialTributeTrapOnTheBoard();
//                        System.out.println("All monster card got destroyed by effect of TorrentialTribute efffect.");
                    }
                } else throw new NoMonsterInThisAddress("There is no monster in this address!");
            }
        } else throw new NotEnoughTribute("there are not enough cards for tribute");
    }

    public void summonAHighLevelMonster(String address) throws NotEnoughTribute, NoMonsterInThisAddress, CancelException {
        Address address1 = new Address(address);
        Player currentPlayer = Game.whoseTurnPlayer();
        if (Game.whoseTurnPlayer().isThereTwoCardInMonsterZone()) {
            String tributeCard1 = Game.getMainPhase1().scanForTribute();
            if (tributeCard1.equals("cancel")) {
                throw new CancelException("canceled successfully!");
            } else {
                String tributeCard2 = Game.getMainPhase1().scanForTribute();
                if (tributeCard2.equals("cancel")) {
                    throw new CancelException("canceled successfully!");
                } else {
                    if (Game.whoseTurnPlayer().isMonsterInThisMonsterZoneTypeAddress(Integer.parseInt(tributeCard1))
                            && Game.whoseTurnPlayer().isMonsterInThisMonsterZoneTypeAddress(Integer.parseInt(tributeCard2))) {
                        Game.whoseTurnPlayer().setHeSummonedOrSet(true);
                        Game.whoseTurnPlayer().removeThisMonsterZoneTypeAddressForTribute(Integer.parseInt(tributeCard1));
                        Game.whoseTurnPlayer().removeThisMonsterZoneTypeAddressForTribute(Integer.parseInt(tributeCard2));
                        Game.whoseTurnPlayer().summonCardFromHandToMonsterZone(address);
                        if (Game.whoseRivalPlayer().doIHaveTrapHoleTrapOnTheBoard()) {
                            currentPlayer.removeCard(address1);
                            Game.whoseRivalPlayer().removeOneOfMyTrapHoleTrapOnTheBoard();
                            //System.out.println("The summmoned card got destroyed by effect of Trap Hole efffect.");
                        }
                        if (Game.whoseRivalPlayer().doIHaveTorrentialTributeTrapOnTheBoard()) {
                            Attack.destroyAllMonstersInTheBoard();
                            Game.whoseRivalPlayer().removeOneOfMyTorrentialTributeTrapOnTheBoard();
                            //System.out.println("All monster card got destroyed by effect of TorrentialTribute efffect.");
                        }
                    } else throw new NoMonsterInThisAddress("There is no monster in this address!");
                }
            }
        } else throw new NotEnoughTribute("there are not enough cards for tribute");
    }

    public void summonASuperHighLevelMonster(String address) throws CancelException, NoMonsterInThisAddress, NotEnoughTribute {
        Address address1 = new Address(address);
        Player currentPlayer = Game.whoseTurnPlayer();
        if (Game.whoseTurnPlayer().isThereThreeCardInMonsterZone()) {
            String tributeCard1 = Game.getMainPhase1().scanForTribute();
            if (Game.getMainPhase1().isCancelled(tributeCard1)) {
                throw new CancelException("canceled successfully!");
            } else {
                String tributeCard2 = Game.getMainPhase1().scanForTribute();
                if (Game.getMainPhase1().isCancelled(tributeCard2)) {
                    throw new CancelException("canceled successfully!");
                } else {
                    String tributeCard3 = Game.getMainPhase1().scanForTribute();
                    if (Game.getMainPhase1().isCancelled(tributeCard3)) {
                        throw new CancelException("canceled successfully!");
                    } else {
                        if (Game.whoseTurnPlayer().isMonsterInThisMonsterZoneTypeAddress(Integer.parseInt(tributeCard1))
                                && Game.whoseTurnPlayer().isMonsterInThisMonsterZoneTypeAddress(Integer.parseInt(tributeCard2))
                                && Game.whoseTurnPlayer().isMonsterInThisMonsterZoneTypeAddress(Integer.parseInt(tributeCard3))) {
                            Game.whoseTurnPlayer().setHeSummonedOrSet(true);
                            Game.whoseTurnPlayer().removeThisMonsterZoneTypeAddressForTribute(Integer.parseInt(tributeCard1));
                            Game.whoseTurnPlayer().removeThisMonsterZoneTypeAddressForTribute(Integer.parseInt(tributeCard2));
                            Game.whoseTurnPlayer().removeThisMonsterZoneTypeAddressForTribute(Integer.parseInt(tributeCard3));
                            Game.whoseTurnPlayer().summonCardFromHandToMonsterZone(address);
                            if (Game.whoseRivalPlayer().doIHaveTrapHoleTrapOnTheBoard()) {
                                currentPlayer.removeCard(address1);
                                Game.whoseRivalPlayer().removeOneOfMyTrapHoleTrapOnTheBoard();
                                //System.out.println("The summmoned card got destroyed by effect of Trap Hole efffect.");
                            }
                            if (Game.whoseRivalPlayer().doIHaveTorrentialTributeTrapOnTheBoard()) {
                                Attack.destroyAllMonstersInTheBoard();
                                Game.whoseRivalPlayer().removeOneOfMyTorrentialTributeTrapOnTheBoard();
                                //System.out.println("All monster card got destroyed by effect of TorrentialTribute efffect.");
                            }
                        } else throw new NoMonsterInThisAddress("There is no monster in this address!");
                    }
                }
            }
        } else throw new NotEnoughTribute("there are not enough cards for tribute");
    }

    public void showSelectedCard(Matcher matcher) {
        if (matcher.find()) {
            String kind = Game.whoseTurnPlayer().whatKindaCardIsInThisAddress(matcher.group(1));
            if (kind.equals("monster")) {
                MonsterCard monsterCardForShow = Game.whoseTurnPlayer().getMonsterCardByStringAddress(matcher.group(1));
                Game.getMainPhase1().printMonsterAttributes(monsterCardForShow);
            } else if (kind.equals("spell")) {
                SpellCard spellCardForShow = Game.whoseTurnPlayer().getSpellCardByStringAddress(matcher.group(1));
                Game.getMainPhase1().printSpellAttributes(spellCardForShow);
            } else {
                TrapCard trapCardForShow = Game.whoseTurnPlayer().getTrapCardByStringAddress(matcher.group(1));
                Game.getMainPhase1().printTrapAttributes(trapCardForShow);
            }
        }
    }

    public void flipSummon(Matcher matcher) throws CantDoInThisPhase{
        if (matcher.find()) {
            Player currentPlayer = Game.whoseTurnPlayer();
            if (currentPlayer.isThisMonsterOnDHPosition(matcher.group(1))) {
                currentPlayer.convertThisMonsterFromDHToOO(matcher.group(1));
                Address address = new Address(matcher.group(1));
                if (currentPlayer.getMonsterCardByAddress(address).getName().equals("ManEaterBug")) {
                    doManEaterBugEffect();
                }
                if (Game.whoseRivalPlayer().doIHaveTrapHoleTrapOnTheBoard()) {
                    currentPlayer.removeCard(address);
                    Game.whoseRivalPlayer().removeOneOfMyTrapHoleTrapOnTheBoard();
//                    System.out.println("The summoned card got destroyed by effect of Trap Hole effect.");
                }
            } else throw new CantDoInThisPhase("You cant do this action in this phase!");
        }
    }

    public void setPosition(String input, Matcher matcher) throws ThisCardIsInWantedPosition, YouAlreadyChangedThisCardPosition {
        Player currentPlayer = Game.whoseTurnPlayer();
        if (matcher.find()) {
            int index = currentPlayer.getIndexOfThisCardByAddress(new Address(matcher.group(1)));
            Matcher matcher1 = CommandMatcher.getCommandMatcher(input, "^[ ]*set -- position (attack|defense)[ ]*$");
            if (matcher1.find()) {
                if (matcher1.group(1).equals("attack")) {
                    if (currentPlayer.isThisMonsterOnAttackPosition(matcher.group(1))) {
                        if (currentPlayer.didWeChangePositionThisCardInThisTurn(index)) {
                            currentPlayer.setDidWeChangePositionThisCardInThisTurn(index);
                            currentPlayer.convertThisMonsterFromAttackToDefence(matcher.group(1));
                        } else throw new YouAlreadyChangedThisCardPosition("you already changed this card position in this turn");
                    } else throw new ThisCardIsInWantedPosition("This card is already in the wanted position!");
                } else {
                    if (!currentPlayer.isThisMonsterOnAttackPosition(matcher.group(1))) {
                        if (currentPlayer.didWeChangePositionThisCardInThisTurn(index)) {
                            currentPlayer.setDidWeChangePositionThisCardInThisTurn(index);
                            currentPlayer.convertThisMonsterFromDefenceToAttack(matcher.group(1));
                        } else throw new YouAlreadyChangedThisCardPosition("you already changed this card position in this turn");
                    } else throw new ThisCardIsInWantedPosition("This card is already in the wanted position!");
                }
            }
        }
    }

    public void activeSpell(Matcher matcher) throws YouAlreadyActivatedThisCard, PreperationsAreNotDoneYet, SpellZoneFull {
        if (matcher.find()) {
            Address address = new Address(matcher.group(1));
            Player currentPlayer = Game.whoseTurnPlayer();
            int index = currentPlayer.getIndexOfThisCardByAddress(new Address(matcher.group(1)));
            if (currentPlayer.didWeActivateThisSpell(index)) {
                if (currentPlayer.getSpellCardByStringAddress(matcher.group(1)).getSpellMode() == SpellMode.FIELD) {
                    if (SpellCard.canWeActivateThisSpell(address)) {
                        currentPlayer.setDidWeActivateThisSpell(index);
                        currentPlayer.setIsThisSpellActivated(true, index);
                        SpellCard.doSpellAbsorptionEffect();
                        currentPlayer.getSpellCardByStringAddress(matcher.group(1)).doEffect(currentPlayer.addCardToAddress(Board.getCardByAddress(address), "field", index));
                    } else throw new PreperationsAreNotDoneYet("preparations of this spell are not done yet");
                } else if (!(currentPlayer.isSpellZoneFull())) {
                    if (SpellCard.canWeActivateThisSpell(address)) {
                        currentPlayer.setDidWeActivateThisSpell(index);
                        currentPlayer.setIsThisSpellActivated(true, index);
                        SpellCard.doSpellAbsorptionEffect();
                        currentPlayer.getSpellCardByStringAddress(matcher.group(1)).doEffect(currentPlayer.addCardToAddress(Board.getCardByAddress(address), "spell", index));
                    } else throw new PreperationsAreNotDoneYet("preparations of this spell are not done yet");
                } else throw new SpellZoneFull("spell card zone is full!");
            } else throw new YouAlreadyActivatedThisCard("you have already activated this card");
        }
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
        int count = Integer.parseInt(Effect.run("HeraldOfCreation1"));
        int numberOfHeraldOfCreation = Game.whoseTurnPlayer().howManyHeraldOfCreationDoWeHave("HeraldOfCreation2");
        for (int i = 0; i < minOfTwoNumber(numberOfHeraldOfCreation, count) - Game.getMainPhase1().howManyHeraldOfCreationDidWeUseEffect; i++) {
            Address shouldBeRemoved = new Address(Integer.parseInt(Effect.run("HeraldOfCreation2")), "monster", true);
            if (!Board.isAddressEmpty(shouldBeRemoved)) {
                Address comeBackFromGraveyard = new Address(Integer.parseInt(Effect.run("HeraldOfCreation3")), "graveyard", true);
                MonsterCard monsterCardForHeraldOfCreation = Board.whatKindaMonsterIsHere(comeBackFromGraveyard);
                if (monsterCardForHeraldOfCreation != null) {
                    if (monsterCardForHeraldOfCreation.getLevel() >= 7) {
                        currentPlayer.removeCard(shouldBeRemoved);
                        currentPlayer.setCardFromGraveyardToMonsterZone(comeBackFromGraveyard);
                        if (Game.getMainPhase1().whatMainIsPhase == 1) Game.getMainPhase2().increaseHowManyHeraldOfCreationDidWeUseEffect();
                    }
                }
            }
        }
    }

    public void doManEaterBugEffect() {
        int monsterZoneNumber = Integer.parseInt(Effect.run("ManEaterBug"));
        if (monsterZoneNumber <= 5 && monsterZoneNumber >= 1) {
            Address address1 = new Address(monsterZoneNumber, "monster", false);
            if (!Board.isAddressEmpty(address1)) {
                Attack.destroyThisAddress(address1);
            }
        }
    }

    public int minOfTwoNumber(int num1, int num2) {
        if (num1 <= num2) return num1;
        return num2;
    }

    public void surrender() {
        Game.setIsSurrender(true);
        Game.setWinner(Game.whoseRivalPlayer());
        Game.getMainPhase1().goToNextPhase = true;
    }


}
