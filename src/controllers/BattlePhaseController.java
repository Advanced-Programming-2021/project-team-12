package controllers;

import Exceptions.*;
import Exceptions.CantSummonThisCard;
import Exceptions.InvalidCardSelection;
import Exceptions.InvalidCommandException;
import Exceptions.NoSelectedCardException;
import Utility.CommandMatcher;
import controllers.move.Attack;
import controllers.move.SetSpell;
import view.phase.BattlePhase;
import models.Address;
import models.Board;
import models.Player;
import models.PositionOfCardInBoard;
import models.card.monster.MonsterCard;
import view.Main;

import java.util.regex.Matcher;

public class BattlePhaseController {
    private static BattlePhaseController instance;

    public static BattlePhaseController getInstance() {
        if (instance == null) {
            instance = new BattlePhaseController();
        }
        return instance;
    }

    public boolean battlePhaseRun(String input) throws Exception {
        if (input.matches("^[ ]*next phase[ ]*$"))
            return true;
        else if (input.matches("^[ ]*select [.*][ ]*$"))
            whatIsSelected(input);
        else if (input.matches("^[ ]*summon[ ]*$"))
            throw new NoSelectedCardException("no card is selected yet");
        else if (input.matches("^[ ]*set[ ]*$"))
            throw new NoSelectedCardException("no card is selected yet");
        else if (input.matches("^[ ]*set -- position (attack|defense)[ ]*$"))
            throw new NoSelectedCardException("no card is selected yet");
        else if (input.matches("^[ ]*flip-summon[ ]*$"))
            throw new NoSelectedCardException("no card is selected yet");
        else if (input.matches("^[ ]*attack [\\d]+[ ]*$"))
            throw new NoSelectedCardException("no card is selected yet");
        else if (input.matches("^[ ]*attack direct[ ]*$"))
            throw new NoSelectedCardException("no card is selected yet");
        else if (input.matches("^[ ]*activate effect[ ]*$"))
            throw new NoSelectedCardException("no card is selected yet");
        else if (input.matches("^[ ]*show graveyard[ ]*$"))
            Board.showGraveyard();
        else if (input.matches("^[ ]*card show --selected[ ]*$"))
            throw new NoSelectedCardException("no card is selected yet");
        else if (input.matches("^[ ]*surrender[ ]*$"))
            PhaseControl.getInstance().surrender();
        else
            throw new InvalidCommandException("invalid command");
        return false;
    }

    public void whatIsSelected(String input) throws Exception {
        PhaseControl.getInstance().checkIfGameEnded();
        if (input.matches("^[ ]*select --monster [\\d]+[ ]*$"))
            selectMonster(CommandMatcher.getCommandMatcher(input, "(^[ ]*select --monster ([\\d]+)[ ]*$)"));
        else if (input.matches("^[ ]*select --monster --opponent [\\d]+[ ]*$"))
            selectOpponentMonster(CommandMatcher.getCommandMatcher(input, "(^[ ]*select --monster --opponent ([\\d]+)[ ]*$)"));
        else if (input.matches("^[ ]*select --spell [\\d]+[ ]*$"))
            selectSpell(CommandMatcher.getCommandMatcher(input, "(^[ ]*select --spell ([\\d]+)[ ]*$)"));
        else if (input.matches("^[ ]*select --spell --opponent [\\d]+[ ]*$"))
            selectOpponentSpell(CommandMatcher.getCommandMatcher(input, "(^[ ]*select --spell --opponent ([\\d]+)[ ]*$)"));
        else if (input.matches("^[ ]*select --field[ ]*$"))
            selectField();
        else if (input.matches("^[ ]*select --field --opponent[ ]*$"))
            selectOpponentField();
        else if (input.matches("^[ ]*select --hand [\\d]+[ ]*$"))
            selectHand(CommandMatcher.getCommandMatcher(input, "(^[ ]*select --hand ([\\d]+)[ ]*$)"));
        else if (input.matches("^[ ]*select -d[ ]*$"))
            throw new NoSelectedCardException("no card is selected yet");
        else
            throw new InvalidCommandException("invalid command");
    }

    private void selectMonster(Matcher matcher) throws Exception {
        if (matcher.find()) {
            Board.showBoard();
            if (Address.isAddressValid(matcher.group(1))) {
                String selectedCard = matcher.group(1);
                String input;
                while (true) {
                    PhaseControl.getInstance().checkIfGameEnded();
                    input = Main.scanner.nextLine().trim();
                    if (input.matches("^[ ]*next phase[ ]*$")) {
                        BattlePhase.getInstance().goToNextPhase = true;
                        break;
                    } else if (input.matches("^[ ]*select -d[ ]*$"))
                        break;
                    else if (input.matches("^[ ]*select [.*][ ]*$"))
                        System.out.println("");
                        //here
                    else if (input.matches("^[ ]*summon[ ]*$"))
                        System.out.println("you can’t summon this card");
                    else if (input.matches("^[ ]*set[ ]*$"))
                        System.out.println("you can’t set this card");
                    else if (input.matches("^[ ]*set -- position (attack|defense)[ ]*$"))
                        setPosition(CommandMatcher.getCommandMatcher(selectedCard, "(^[ ]*select --monster ([\\d]+)[ ]*$)"));
                    else if (input.matches("^[ ]*flip-summon[ ]*$"))
                        flipSummon(CommandMatcher.getCommandMatcher(selectedCard, "(^[ ]*select --monster ([\\d]+)[ ]*$)"));
                    else if (input.matches("^[ ]*attack [\\d]+[ ]*$"))
                        attack(CommandMatcher.getCommandMatcher(input, "(^[ ]*attack ([\\d]+)[ ]*$)"), selectedCard);
                    else if (input.matches("^[ ]*attack direct[ ]*$"))
                        directAttack(CommandMatcher.getCommandMatcher(selectedCard, "(^[ ]*select --monster ([\\d]+)[ ]*$)"));
                    else if (input.matches("^[ ]*activate effect[ ]*$"))
                        System.out.println("activate effect is only for spell cards.");
                    else if (input.matches("^[ ]*show graveyard[ ]*$"))
                        Board.showGraveyard();
                    else if (input.matches("^[ ]*card show --selected[ ]*$"))
                        Game.getMainPhase1().showSelectedCard(CommandMatcher.getCommandMatcher(selectedCard, "(^[ ]*select --monster ([\\d]+)[ ]*$)"));
                    else if (input.matches("^[ ]*surrender[ ]*$"))
                        PhaseControl.getInstance().surrender();
                    else
                        throw new InvalidCommandException("invalid command");
                }
            } else
                throw new InvalidCardSelection("invalid selection");
        }
    }

    private void selectOpponentMonster(Matcher matcher) throws InvalidCommandException, InvalidCardSelection, CantSummonThisCard, CantSetThisCard, CantChangeCardPosition, CantAttack, CantActivateEffect {
        if (matcher.find()) {
            Board.showBoard();
            if (Address.isAddressValid(matcher.group(1))) {
                String selectedCard = matcher.group(1);
                String input;
                while (true) {
                    PhaseControl.getInstance().checkIfGameEnded();
                    input = Main.scanner.nextLine().trim();
                    if (input.matches("^[ ]*next phase[ ]*$")) {
                        BattlePhase.getInstance().goToNextPhase = true;
                        break;
                    } else if (input.matches("^[ ]*select -d[ ]*$"))
                        break;
                    else if (input.matches("^[ ]*select [.*][ ]*$"))
                        System.out.println("");
                        //here
                        //TODO:
                    else if (input.matches("^[ ]*summon[ ]*$"))
                        throw new CantSummonThisCard("you can’t summon this card");
                    else if (input.matches("^[ ]*set[ ]*$"))
                        throw new CantSetThisCard("you can’t set this card");
                    else if (input.matches("^[ ]*set -- position (attack|defense)[ ]*$"))
                        throw new CantChangeCardPosition("you can’t change this card position");
                    else if (input.matches("^[ ]*flip-summon[ ]*$"))
                        throw new CantChangeCardPosition("you can’t change this card position");
                    else if (input.matches("^[ ]*attack [\\d]+[ ]*$"))
                        throw new CantAttack("you can’t attack with this card");
                    else if (input.matches("^[ ]*attack direct[ ]*$"))
                        throw new CantAttack("you can’t attack with this card");
                    else if (input.matches("^[ ]*activate effect[ ]*$"))
                        throw new CantActivateEffect("activate effect is only for spell cards.");
                    else if (input.matches("^[ ]*show graveyard[ ]*$"))
                        Board.showGraveyard();
                    else if (input.matches("^[ ]*card show --selected[ ]*$"))
                        Game.getMainPhase1().showSelectedCard(CommandMatcher.getCommandMatcher(selectedCard, "(^[ ]*select --monster --opponent [\\d]+[ ]*$)"));
                    else if (input.matches("^[ ]*surrender[ ]*$"))
                        PhaseControl.getInstance().surrender();
                    else
                        throw new InvalidCommandException("invalid command");
                }
            } else
                throw new InvalidCardSelection("invalid selection");
        }
    }

    private void selectSpell(Matcher matcher) throws InvalidCommandException, InvalidCardSelection, CantAttack, CantChangeCardPosition, CantSetThisCard, CantSummonThisCard, CantDoInThisPhase {
        if (matcher.find()) {
            Board.showBoard();
            if (Address.isAddressValid(matcher.group(1))) {
                String selectedCard = matcher.group(1);
                String input;
                while (true) {
                    PhaseControl.getInstance().checkIfGameEnded();
                    input = Main.scanner.nextLine().trim();
                    if (input.matches("^[ ]*next phase[ ]*$")) {
                        BattlePhase.getInstance().goToNextPhase = true;
                        break;
                    } else if (input.matches("^[ ]*select -d[ ]*$"))
                        break;
                    else if (input.matches("^[ ]*select [.*][ ]*$"))
                        System.out.println("");
                        //here
                        //TODO:
                    else if (input.matches("^[ ]*summon[ ]*$"))
                        throw new CantSummonThisCard("you can’t summon this card");
                    else if (input.matches("^[ ]*set[ ]*$"))
                        throw new CantSetThisCard("you can’t set this card");
                    else if (input.matches("^[ ]*set -- position (attack|defense)[ ]*$"))
                        throw new CantChangeCardPosition("you can’t change this card position");
                    else if (input.matches("^[ ]*flip-summon[ ]*$"))
                        throw new CantChangeCardPosition("you can’t change this card position");
                    else if (input.matches("^[ ]*attack [\\d]+[ ]*$"))
                        throw new CantAttack("you can’t attack with this card");
                    else if (input.matches("^[ ]*attack direct[ ]*$"))
                        throw new CantAttack("you can’t attack with this card");
                    else if (input.matches("^[ ]*activate effect[ ]*$"))
                        activeSpell(CommandMatcher.getCommandMatcher(selectedCard, "(^[ ]*select --spell ([\\d]+)[ ]*$)"));
                    else if (input.matches("^[ ]*show graveyard[ ]*$"))
                        Board.showGraveyard();
                    else if (input.matches("^[ ]*card show --selected[ ]*$"))
                        Game.getMainPhase1().showSelectedCard(CommandMatcher.getCommandMatcher(selectedCard, "(^[ ]*select --spell ([\\d]+)[ ]*$)"));
                    else if (input.matches("^[ ]*surrender[ ]*$"))
                        PhaseControl.getInstance().surrender();
                    else
                        throw new InvalidCommandException("invalid command");
                }
            } else
                throw new InvalidCardSelection("invalid selection");
        }
    }


    private void selectOpponentSpell(Matcher matcher) throws InvalidCardSelection, InvalidCommandException, CantActivateEffect, CantAttack, CantChangeCardPosition, CantSetThisCard, CantSummonThisCard {
        if (matcher.find()) {
            if (Address.isAddressValid(matcher.group(1))) {
                String selectedCard = matcher.group(1);
                String input;
                while (true) {
                    PhaseControl.getInstance().checkIfGameEnded();
                    Board.showBoard();
                    input = Main.scanner.nextLine().trim();
                    if (input.matches("^[ ]*next phase[ ]*$")) {
                        BattlePhase.getInstance().goToNextPhase = true;
                        break;
                    } else if (input.matches("^[ ]*select -d[ ]*$"))
                        break;
                    else if (input.matches("^[ ]*select [.*][ ]*$"))
                        System.out.println("");
                        //here
                        //TODO:
                    else if (input.matches("^[ ]*summon[ ]*$"))
                        throw new CantSummonThisCard("you can’t summon this card");
                    else if (input.matches("^[ ]*set[ ]*$"))
                        throw new CantSetThisCard("you can’t set this card");
                    else if (input.matches("^[ ]*set -- position (attack|defense)[ ]*$"))
                        throw new CantChangeCardPosition("you can’t change this card position");
                    else if (input.matches("^[ ]*flip-summon[ ]*$"))
                        throw new CantChangeCardPosition("you can’t change this card position");
                    else if (input.matches("^[ ]*attack [\\d]+[ ]*$"))
                        throw new CantAttack("you can’t attack with this card");
                    else if (input.matches("^[ ]*attack direct[ ]*$"))
                        throw new CantAttack("you can’t attack with this card");
                    else if (input.matches("^[ ]*activate effect[ ]*$"))
                        throw new CantActivateEffect("activate effect is only for spell cards.");
                    else if (input.matches("^[ ]*show graveyard[ ]*$"))
                        Board.showGraveyard();
                    else if (input.matches("^[ ]*card show --selected[ ]*$"))
                        Game.getMainPhase1().showSelectedCard(CommandMatcher.getCommandMatcher(selectedCard, "(^[ ]*select --spell --opponent ([\\d]+)[ ]*$)"));
                    else if (input.matches("^[ ]*surrender[ ]*$"))
                        PhaseControl.getInstance().surrender();
                    else
                        throw new InvalidCommandException("invalid command");
                }
            } else
                throw new InvalidCardSelection("invalid selection");
        }
    }

    private void selectField() throws CantSummonThisCard, CantSetThisCard, CantChangeCardPosition, CantAttack, CantActivateEffect, InvalidCommandException {
        String input;
        while (true) {
            PhaseControl.getInstance().checkIfGameEnded();
            Board.showBoard();
            input = Main.scanner.nextLine().trim();
            if (input.matches("^[ ]*next phase[ ]*$")) {
                BattlePhase.getInstance().goToNextPhase = true;
                break;
            } else if (input.matches("^[ ]*select -d[ ]*$"))
                break;
            else if (input.matches("^[ ]*select [.*][ ]*$"))
                System.out.println("");
                //here
                //TODO:
            else if (input.matches("^[ ]*summon[ ]*$"))
                throw new CantSummonThisCard("you can’t summon this card");
            else if (input.matches("^[ ]*set[ ]*$"))
                throw new CantSetThisCard("you can’t set this card");
            else if (input.matches("^[ ]*set -- position (attack|defense)[ ]*$"))
                throw new CantChangeCardPosition("you can’t change this card position");
            else if (input.matches("^[ ]*flip-summon[ ]*$"))
                throw new CantChangeCardPosition("you can’t change this card position");
            else if (input.matches("^[ ]*attack [\\d]+[ ]*$"))
                throw new CantAttack("you can’t attack with this card");
            else if (input.matches("^[ ]*attack direct[ ]*$"))
                throw new CantAttack("you can’t attack with this card");
            else if (input.matches("^[ ]*activate effect[ ]*$"))
                throw new CantActivateEffect("activate effect is only for spell cards.");
            else if (input.matches("^[ ]*show graveyard[ ]*$"))
                Board.showGraveyard();
            else if (input.matches("^[ ]*card show --selected[ ]*$"))
                Game.getMainPhase1().showFieldZoneCard();
            else if (input.matches("^[ ]*surrender[ ]*$"))
                PhaseControl.getInstance().surrender();
            else
                throw new InvalidCommandException("invalid command");
        }
    }

    private void selectOpponentField() throws InvalidCommandException, CantSummonThisCard, CantSetThisCard, CantChangeCardPosition, CantAttack, CantActivateEffect {
        String input;
        while (true) {
            PhaseControl.getInstance().checkIfGameEnded();
            Board.showBoard();
            input = Main.scanner.nextLine().trim();
            if (input.matches("^[ ]*next phase[ ]*$")) {
                BattlePhase.getInstance().goToNextPhase = true;
                break;
            } else if (input.matches("^[ ]*select -d[ ]*$"))
                break;
            else if (input.matches("^[ ]*select [.*][ ]*$"))
                System.out.println("");
                //here
            else if (input.matches("^[ ]*summon[ ]*$"))
                throw new CantSummonThisCard("you can’t summon this card");
            else if (input.matches("^[ ]*set[ ]*$"))
                throw new CantSetThisCard("you can’t set this card");
            else if (input.matches("^[ ]*set -- position (attack|defense)[ ]*$"))
                throw new CantChangeCardPosition("you can’t change this card position");
            else if (input.matches("^[ ]*flip-summon[ ]*$"))
                throw new CantChangeCardPosition("you can’t change this card position");
            else if (input.matches("^[ ]*attack [\\d]+[ ]*$"))
                throw new CantAttack("you can’t attack with this card");
            else if (input.matches("^[ ]*attack direct[ ]*$"))
                throw new CantAttack("you can’t attack with this card");
            else if (input.matches("^[ ]*activate effect[ ]*$"))
                throw new CantActivateEffect("activate effect is only for spell cards.");
            else if (input.matches("^[ ]*show graveyard[ ]*$"))
                Board.showGraveyard();
            else if (input.matches("^[ ]*card show --selected[ ]*$"))
                Game.getMainPhase1().showOpponentFieldZoneCard();
            else if (input.matches("^[ ]*surrender[ ]*$"))
                PhaseControl.getInstance().surrender();
            else
                throw new InvalidCommandException("invalid command");
        }
    }

    private void selectHand(Matcher matcher) throws InvalidCommandException, CantChangeCardPosition, CantAttack, CantActivateEffect, InvalidCardSelection, CantDoInThisPhase {
        if (matcher.find()) {
            Board.showBoard();
            if (Address.isAddressValid(matcher.group(1))) {
                String selectedCard = matcher.group(1);
                String input;
                while (true) {
                    PhaseControl.getInstance().checkIfGameEnded();
                    input = Main.scanner.nextLine().trim();
                    if (input.matches("^[ ]*next phase[ ]*$")) {
                        BattlePhase.getInstance().goToNextPhase = true;
                        break;
                    } else if (input.matches("^[ ]*select -d[ ]*$"))
                        break;
                    else if (input.matches("^[ ]*select [.*][ ]*$"))
                        System.out.println("");
                        //here
                    else if (input.matches("^[ ]*summon[ ]*$"))
                        BattlePhase.getInstance().summon(CommandMatcher.getCommandMatcher(selectedCard, "(^[ ]*select --hand [\\d]+[ ]*$)"));
                    else if (input.matches("^[ ]*set[ ]*$"))
                        throw new CantDoInThisPhase("you can’t do this action in this phase");
                    else if (input.matches("^[ ]*set -- position (attack|defense)[ ]*$"))
                        throw new CantChangeCardPosition("you can’t change this card position");
                    else if (input.matches("^[ ]*flip-summon[ ]*$"))
                        throw new CantChangeCardPosition("you can’t change this card position");
                    else if (input.matches("^[ ]*attack [\\d]+[ ]*$"))
                        throw new CantAttack("you can’t attack with this card");
                    else if (input.matches("^[ ]*attack direct[ ]*$"))
                        throw new CantAttack("you can’t attack with this card");
                    else if (input.matches("^[ ]*activate effect[ ]*$"))
                        throw new CantActivateEffect("activate effect is only for spell cards.");
                    else if (input.matches("^[ ]*show graveyard[ ]*$"))
                        Board.showGraveyard();
                    else if (input.matches("^[ ]*card show --selected[ ]*$"))
                        Game.getMainPhase1().showSelectedCard(CommandMatcher.getCommandMatcher(selectedCard, "(^[ ]*select --hand [\\d]+[ ]*$)"));
                    else if (input.matches("^[ ]*surrender[ ]*$"))
                        PhaseControl.getInstance().surrender();
                    else
                        throw new InvalidCommandException("invalid command");
                }
            } else
                throw new InvalidCardSelection("invalid selection");
        }
    }

    private void attack(Matcher matcher, String myAddress) throws Exception {
        if (matcher.find()) {
            Player currentPlayer = Game.whoseTurnPlayer();
            Address address = new Address(Integer.parseInt(matcher.group(2)), "monster", false);
            int index = currentPlayer.getIndexOfThisCardByAddress(new Address(myAddress));
            if (currentPlayer.didWeAttackByThisCardInThisCardInThisTurn(index)) {
                if (currentPlayer.getCardByAddress(address) != null) {
                    MonsterCard myMonsterCard = currentPlayer.getMonsterCardByStringAddress(myAddress);
                    MonsterCard rivalMonsterCard = currentPlayer.getMonsterCardByAddress(address);
                    Address myAddressType = new Address(myAddress);
                    currentPlayer.setDidWeAttackByThisCardInThisCardInThisTurn(index);
                    if (Game.whoseRivalPlayer().doIHaveActivatedTrapNamedNegateAttack() && !Game.whoseTurnPlayer().doIHaveMirageDragonMonster()) {
                        BattlePhase.getInstance().goToNextPhase = true;
                    } else if (Game.whoseRivalPlayer().doIHaveActivatedTrapNamedMirrorForce() && !Game.whoseTurnPlayer().doIHaveMirageDragonMonster()) {
                        Game.whoseRivalPlayer().destroyAllRivalMonstersWhichInAttackMode();
                    } else if ((Board.whatKindaMonsterIsHere(address).getNormalAttack() >= 1500)
                            && (SetSpell.doAnyOneHaveMessengerOfPeace())) {
                        throw new Exception("You can't attack by monster with attack equal or more than 1500 " +
                                "because of MessengerOfPeace.");
                    } else if (Game.whoseRivalPlayer().doIHaveActivatedTrapNamedMagicCylinder() && !Game.whoseTurnPlayer().doIHaveMirageDragonMonster()) {
                        currentPlayer.decreaseLP(myMonsterCard.getNormalAttack());
                        throw new Exception("Rival has trap named MagicCylinder so its effect get done.");
                    } else if (rivalMonsterCard.getName().equals("Texchanger")) {
                        Game.getMainPhase1().summonCyberse();
                    } else {
                        if (currentPlayer.positionOfCardInBoardByAddress(address) == PositionOfCardInBoard.OO) {
                            int damage = myMonsterCard.getAttack(myAddressType) - rivalMonsterCard.getAttack(address);
                            attackOO(myAddressType, address, index, currentPlayer, myMonsterCard, rivalMonsterCard, damage);
                            Attack.timeToEffectAfterAttack();
                        } else if (currentPlayer.positionOfCardInBoardByAddress(address) == PositionOfCardInBoard.DO) {
                            if (rivalMonsterCard.getDefence(true) != -1) {
                                int damage = myMonsterCard.getAttack(myAddressType) - rivalMonsterCard.getDefence(true);
                                attackDO(myAddressType, address, index, currentPlayer, myMonsterCard, rivalMonsterCard, damage);
                                Attack.timeToEffectAfterAttack();
                            } else throw new Exception("Attack has been cancelled for effect of a card");
                        } else {
                            int damage = myMonsterCard.getAttack(myAddressType) - rivalMonsterCard.getDefence(false);
                            attackDH(myAddressType, address, index, currentPlayer, myMonsterCard, rivalMonsterCard, damage);
                            Attack.timeToEffectAfterAttack();
                        }
                    }
                } else throw new Exception("there is no card to attack here");
            } else throw new Exception("this card already attacked");
        }
    }

    private void attackOO(Address myAddress, Address address, int index, Player currentPlayer, MonsterCard myMonsterCard, MonsterCard rivalMonsterCard, int damage) throws Exception {
        if (damage == 0) {
            removeForAttack(address, myAddress);
            removeForAttack(myAddress, address);
            throw new Exception("both you and your opponent monster cards are destroyed and no one receives damage");
        } else if (damage > 0) {
            removeForAttack(address, myAddress);
            decreaseLP(myAddress, address, index, Game.whoseRivalPlayer(), myMonsterCard, rivalMonsterCard, damage);
            throw new Exception("your opponent’s monster is destroyed and your opponent receives " + damage + " battle damage");
        } else {
            damage = (-1) * damage;
            currentPlayer.decreaseLP(damage);
            removeForAttack(myAddress, address);
            throw new Exception("Your monster card is destroyed and you received " + damage + " battle damage");
        }
    }

    private void attackDO(Address myAddress, Address address, int index, Player currentPlayer, MonsterCard myMonsterCard, MonsterCard rivalMonsterCard, int damage) throws Exception {
        if (damage == 0) throw new Exception("no card is destroyed");
        else if (damage > 0) {
            removeForAttack(address, myAddress);
            throw new Exception("the defense position monster is destroyed");
        } else {
            damage = (-1) * damage;
            decreaseLP(myAddress, address, index, currentPlayer, myMonsterCard, rivalMonsterCard, damage);
            throw new Exception("no card is destroyed and you received " + damage + " battle damage");
        }
    }

    private void attackDH(Address myAddress, Address address, int index, Player currentPlayer, MonsterCard myMonsterCard, MonsterCard rivalMonsterCard, int damage) throws Exception {
        String shouldBePrinted;
        if (damage == 0)
            shouldBePrinted = "opponent’s monster card was " + rivalMonsterCard.getName() + " and no card is destroyed";
        else if (damage > 0) {
            removeForAttack(address, myAddress);
            shouldBePrinted = "opponent’s monster card was " + rivalMonsterCard.getName() + " and " + "the defense position monster is destroyed";
        } else {
            damage = (-1) * damage;
            decreaseLP(myAddress, address, index, currentPlayer, myMonsterCard, rivalMonsterCard, damage);
            shouldBePrinted = "opponent’s monster card was " + rivalMonsterCard.getName() + " and " + "no card is destroyed and you received " + damage + " battle damage";
        }
        currentPlayer.setPositionOfCardInBoardByAddress(address, PositionOfCardInBoard.DO);
        throw new Exception(shouldBePrinted);
    }

    private void decreaseLP(Address myAddress, Address address, int index, Player player, MonsterCard myMonsterCard, MonsterCard rivalMonsterCard, int damage) {
        if (!(Game.whoseTurnPlayer().getMonsterCardByAddress(address).getName().equals("ExploderDragon"))) {
            player.decreaseLP(damage);
        }
        PhaseControl.getInstance().checkIfGameEnded();
    }

    private void removeForAttack(Address address, Address theOtherAddress) {
        Player currentPlayer = Game.whoseTurnPlayer();
        if (currentPlayer.getMonsterCardByAddress(address).getName().equals("ExploderDragon"))
            currentPlayer.removeCard(theOtherAddress);
        if (!currentPlayer.getMonsterCardByAddress(address).getName().equals("Marshmallon"))
            currentPlayer.removeCard(address);
    }

    private void flipSummon(Matcher matcher) throws CantDoInThisPhase {
        throw new CantDoInThisPhase("you can’t do this action in this phase");
    }

    private void setPosition(Matcher matcher) throws CantDoInThisPhase {
        throw new CantDoInThisPhase("you can’t do this action in this phase");
    }

    private void directAttack(Matcher matcher) throws Exception {
        PhaseControl.getInstance().checkIfGameEnded();
        if (matcher.find()) {
            Player currentPlayer = Game.whoseTurnPlayer();
            Player rivalPlayer = Game.whoseRivalPlayer();
            int index = currentPlayer.getIndexOfThisCardByAddress(new Address(matcher.group(1)));
            if (currentPlayer.didWeAttackByThisCardInThisCardInThisTurn(index)) {
                currentPlayer.setDidWeAttackByThisCardInThisCardInThisTurn(index);
                //doubt should regard an error for being empty of board?
                MonsterCard monsterCardForDirectAttack = currentPlayer.getMonsterCardByStringAddress(matcher.group(1));
                rivalPlayer.decreaseLP(monsterCardForDirectAttack.getAttack(new Address(matcher.group(1))));
                throw new Exception("you opponent receives " + monsterCardForDirectAttack.getAttack(new Address(matcher.group(1))) + " battle damage");
            } else throw new Exception("this card already attacked");
        }
        PhaseControl.getInstance().checkIfGameEnded();
    }

    private void specialSummon(Matcher matcher) {

    }

    private void activeSpell(Matcher matcher) throws CantDoInThisPhase {
        throw new CantDoInThisPhase("you can’t do this action in this phase");
    }

    private void activeTrap(Matcher matcher) {

    }

    private void ritualSummon(Matcher matcher) {

    }
}
