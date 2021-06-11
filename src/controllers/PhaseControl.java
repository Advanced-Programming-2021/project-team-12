package controllers;

import controllers.move.SetSpell;
import controllers.phase.MainPhase;
import models.Address;
import models.Board;
import models.GamePhase;
import models.PlayerTurn;
import Exceptions.*;
import Utility.*;
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

    public void doEffect() {
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

    public void surrender() {
        Game.setIsSurrender(true);
        Game.setWinner(Game.whoseRivalPlayer());
        Game.getMainPhase1().goToNextPhase = true;
    }


}
