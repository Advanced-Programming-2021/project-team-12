package controllers;

import Exceptions.*;
import Utility.CommandMatcher;
import controllers.move.Attack;
import controllers.move.SetSpell;
import models.*;
import view.phase.BattlePhase;
import models.card.monster.MonsterCard;
import view.Main;

import java.util.regex.Matcher;

public class BattlePhaseController {
    private static BattlePhaseController instance;

    public static BattlePhaseController getInstance() {
        if (instance == null)
            instance = new BattlePhaseController();
        return instance;
    }

    public boolean battlePhaseRun(String input) throws MyException {
        if (input.matches("^[ ]*next phase[ ]*$"))
            return true;
        else if (input.matches("^[ ]*select .*$"))
            whatIsSelected(input);
        else if (input.matches("^[ ]*summon[ ]*$"))
            throw new MyException("no card is selected yet");
        else if (input.matches("^[ ]*set[ ]*$"))
            throw new MyException("no card is selected yet");
        else if (input.matches("^[ ]*set -- position (attack|defense)[ ]*$"))
            throw new MyException("no card is selected yet");
        else if (input.matches("^[ ]*flip-summon[ ]*$"))
            throw new MyException("no card is selected yet");
        else if (input.matches("^[ ]*attack [\\d]+[ ]*$"))
            throw new MyException("no card is selected yet");
        else if (input.matches("^[ ]*attack direct[ ]*$"))
            throw new MyException("no card is selected yet");
        else if (input.matches("^[ ]*activate effect[ ]*$"))
            throw new MyException("no card is selected yet");
        else if (input.matches("^[ ]*show graveyard[ ]*$"))
            Board.showGraveyard();
        else if (input.matches("^[ ]*card show --selected[ ]*$"))
            throw new MyException("no card is selected yet");
        else if (input.matches("^[ ]*surrender[ ]*$"))
            PhaseControl.getInstance().surrender();
        else
            throw new MyException("invalid command");
        return false;
    }

    public void whatIsSelected(String input) throws MyException {
        if (Address.isAddressValid(input) && Board.getCardByAddress(new Address(input)) == null)
            throw new MyException("address is empty");
        PhaseControl.getInstance().checkIfGameEnded();
        if (input.matches("^[ ]*select --monster [12345][ ]*$"))
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
            throw new MyException("no card is selected yet");
        else
            throw new MyException("invalid command");
    }

    private void selectMonster(Matcher matcher) throws MyException {
        if (matcher.find()) {
            if (!Game.isAITurn())
                Board.showBoard();
            if (Address.isAddressValid(matcher.group(1))) {
                String selectedCard = matcher.group(1);
                String input;
                while (true) {
                    PhaseControl.getInstance().checkIfGameEnded();
                    if (!Game.isAITurn())
                        input = Main.scanner.nextLine().trim();
                    else input = getAIAttack(selectedCard);
                    if (input.matches("^[ ]*next phase[ ]*$")) {
                        BattlePhase.getInstance().goToNextPhase = true;
                        Game.playTurn("MainPhase2");
                    } else if (input.matches("^[ ]*select -d[ ]*$"))
                        BattlePhase.getInstance().selectCard();
                    else if (input.matches("^[ ]*select .*$"))
                        System.out.println("you already selected a card");
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
                        throw new MyException("invalid command");
                }
            } else
                throw new MyException("invalid selection");
        }
    }

    private String getAIAttack(String cardNumber) {
        Card card = Game.whoseTurnPlayer().getCardMonster(Integer.parseInt(cardNumber));
        Address address = new Address(Integer.parseInt(cardNumber), "monster", true);
        Player player = Game.whoseRivalPlayer();
        if (player.getMonsterZoneCard().size() == 0)
            return "attack direct";
        int place = 0;
        int damage = 0;
        for (int i = 1; i < 6; i++) {
            int tempDamage = 0;
            if (!player.getMonsterZoneCard().containsKey(i))
                continue;
            if (player.getMonsterPosition(i).equals(PositionOfCardInBoard.DO))
                tempDamage = MonsterCard.getMonsterCardByName(card.getCardName()).getAttack(address) - MonsterCard.getMonsterCardByName(player.getCardMonster(i).getCardName()).getDefence(true,new Address(i, "monster", false) );
            if (player.getMonsterPosition(i).equals(PositionOfCardInBoard.OO))
                tempDamage = MonsterCard.getMonsterCardByName(card.getCardName()).getAttack(address) - MonsterCard.getMonsterCardByName(player.getCardMonster(i).getCardName()).getAttack(new Address(i, "monster", true));
            if (damage < tempDamage) {
                damage = tempDamage;
                place = i;
            }
        }
        if (place == 0)
            return "select -d";
        else return "attack " + place;
    }

    private void selectOpponentMonster(Matcher matcher) throws MyException {
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
                        Game.playTurn("MainPhase2");;
                    } else if (input.matches("^[ ]*select -d[ ]*$"))
                        BattlePhase.getInstance().selectCard();
                    else if (input.matches("^[ ]*select .*$"))
                        System.out.println("you already selected a card");
                    else if (input.matches("^[ ]*summon[ ]*$"))
                        throw new MyException("you can’t summon this card");
                    else if (input.matches("^[ ]*set[ ]*$"))
                        throw new MyException("you can’t set this card");
                    else if (input.matches("^[ ]*set -- position (attack|defense)[ ]*$"))
                        throw new MyException("you can’t change this card position");
                    else if (input.matches("^[ ]*flip-summon[ ]*$"))
                        throw new MyException("you can’t change this card position");
                    else if (input.matches("^[ ]*attack [\\d]+[ ]*$"))
                        throw new MyException("you can’t attack with this card");
                    else if (input.matches("^[ ]*attack direct[ ]*$"))
                        throw new MyException("you can’t attack with this card");
                    else if (input.matches("^[ ]*activate effect[ ]*$"))
                        throw new MyException("activate effect is only for spell cards.");
                    else if (input.matches("^[ ]*show graveyard[ ]*$"))
                        Board.showGraveyard();
                    else if (input.matches("^[ ]*card show --selected[ ]*$"))
                        Game.getMainPhase1().showSelectedCard(CommandMatcher.getCommandMatcher(selectedCard, "(^[ ]*select --monster --opponent [\\d]+[ ]*$)"));
                    else if (input.matches("^[ ]*surrender[ ]*$"))
                        PhaseControl.getInstance().surrender();
                    else
                        throw new MyException("invalid command");
                }
            } else
                throw new MyException("invalid selection");
        }
    }

    private void selectSpell(Matcher matcher) throws MyException {
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
                        Game.playTurn("MainPhase2");;
                    } else if (input.matches("^[ ]*select -d[ ]*$"))
                        BattlePhase.getInstance().selectCard();
                    else if (input.matches("^[ ]*select .*$"))
                        System.out.println("you already selected a card");
                    else if (input.matches("^[ ]*summon[ ]*$"))
                        throw new MyException("you can’t summon this card");
                    else if (input.matches("^[ ]*set[ ]*$"))
                        throw new MyException("you can’t set this card");
                    else if (input.matches("^[ ]*set -- position (attack|defense)[ ]*$"))
                        throw new MyException("you can’t change this card position");
                    else if (input.matches("^[ ]*flip-summon[ ]*$"))
                        throw new MyException("you can’t change this card position");
                    else if (input.matches("^[ ]*attack [\\d]+[ ]*$"))
                        throw new MyException("you can’t attack with this card");
                    else if (input.matches("^[ ]*attack direct[ ]*$"))
                        throw new MyException("you can’t attack with this card");
                    else if (input.matches("^[ ]*activate effect[ ]*$"))
                        throw new MyException("you can’t do this action in this phase");
                    else if (input.matches("^[ ]*show graveyard[ ]*$"))
                        Board.showGraveyard();
                    else if (input.matches("^[ ]*card show --selected[ ]*$"))
                        Game.getMainPhase1().showSelectedCard(CommandMatcher.getCommandMatcher(selectedCard, "(^[ ]*select --spell ([\\d]+)[ ]*$)"));
                    else if (input.matches("^[ ]*surrender[ ]*$"))
                        PhaseControl.getInstance().surrender();
                    else
                        throw new MyException("invalid command");
                }
            } else
                throw new MyException("invalid selection");
        }
    }


    private void selectOpponentSpell(Matcher matcher) throws MyException {
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
                        Game.playTurn("MainPhase2");;
                    } else if (input.matches("^[ ]*select -d[ ]*$"))
                        BattlePhase.getInstance().selectCard();
                    else if (input.matches("^[ ]*select .*$"))
                        System.out.println("you already selected a card");
                    else if (input.matches("^[ ]*summon[ ]*$"))
                        throw new MyException("you can’t summon this card");
                    else if (input.matches("^[ ]*set[ ]*$"))
                        throw new MyException("you can’t set this card");
                    else if (input.matches("^[ ]*set -- position (attack|defense)[ ]*$"))
                        throw new MyException("you can’t change this card position");
                    else if (input.matches("^[ ]*flip-summon[ ]*$"))
                        throw new MyException("you can’t change this card position");
                    else if (input.matches("^[ ]*attack [\\d]+[ ]*$"))
                        throw new MyException("you can’t attack with this card");
                    else if (input.matches("^[ ]*attack direct[ ]*$"))
                        throw new MyException("you can’t attack with this card");
                    else if (input.matches("^[ ]*activate effect[ ]*$"))
                        throw new MyException("activate effect is only for spell cards.");
                    else if (input.matches("^[ ]*show graveyard[ ]*$"))
                        Board.showGraveyard();
                    else if (input.matches("^[ ]*card show --selected[ ]*$"))
                        Game.getMainPhase1().showSelectedCard(CommandMatcher.getCommandMatcher(selectedCard, "(^[ ]*select --spell --opponent ([\\d]+)[ ]*$)"));
                    else if (input.matches("^[ ]*surrender[ ]*$"))
                        PhaseControl.getInstance().surrender();
                    else
                        throw new MyException("invalid command");
                }
            } else
                throw new MyException("invalid selection");
        }
    }

    private void selectField() throws MyException {
        String input;
        while (true) {
            PhaseControl.getInstance().checkIfGameEnded();
            Board.showBoard();
            input = Main.scanner.nextLine().trim();
            if (input.matches("^[ ]*next phase[ ]*$")) {
                BattlePhase.getInstance().goToNextPhase = true;
                Game.playTurn("MainPhase2");;
            } else if (input.matches("^[ ]*select -d[ ]*$"))
                BattlePhase.getInstance().selectCard();
            else if (input.matches("^[ ]*select .*$"))
                System.out.println("you already selected a card");
            else if (input.matches("^[ ]*summon[ ]*$"))
                throw new MyException("you can’t summon this card");
            else if (input.matches("^[ ]*set[ ]*$"))
                throw new MyException("you can’t set this card");
            else if (input.matches("^[ ]*set -- position (attack|defense)[ ]*$"))
                throw new MyException("you can’t change this card position");
            else if (input.matches("^[ ]*flip-summon[ ]*$"))
                throw new MyException("you can’t change this card position");
            else if (input.matches("^[ ]*attack [\\d]+[ ]*$"))
                throw new MyException("you can’t attack with this card");
            else if (input.matches("^[ ]*attack direct[ ]*$"))
                throw new MyException("you can’t attack with this card");
            else if (input.matches("^[ ]*activate effect[ ]*$"))
                throw new MyException("activate effect is only for spell cards.");
            else if (input.matches("^[ ]*show graveyard[ ]*$"))
                Board.showGraveyard();
            else if (input.matches("^[ ]*card show --selected[ ]*$"))
                Game.getMainPhase1().showFieldZoneCard();
            else if (input.matches("^[ ]*surrender[ ]*$"))
                PhaseControl.getInstance().surrender();
            else
                throw new MyException("invalid command");
        }
    }

    private void selectOpponentField() throws MyException {
        String input;
        while (true) {
            PhaseControl.getInstance().checkIfGameEnded();
            Board.showBoard();
            input = Main.scanner.nextLine().trim();
            if (input.matches("^[ ]*next phase[ ]*$")) {
                BattlePhase.getInstance().goToNextPhase = true;
                Game.playTurn("MainPhase2");
            } else if (input.matches("^[ ]*select -d[ ]*$"))
                BattlePhase.getInstance().selectCard();
            else if (input.matches("^[ ]*select .*$"))
                System.out.println("you already selected a card");
            else if (input.matches("^[ ]*summon[ ]*$"))
                throw new MyException("you can’t summon this card");
            else if (input.matches("^[ ]*set[ ]*$"))
                throw new MyException("you can’t set this card");
            else if (input.matches("^[ ]*set -- position (attack|defense)[ ]*$"))
                throw new MyException("you can’t change this card position");
            else if (input.matches("^[ ]*flip-summon[ ]*$"))
                throw new MyException("you can’t change this card position");
            else if (input.matches("^[ ]*attack [\\d]+[ ]*$"))
                throw new MyException("you can’t attack with this card");
            else if (input.matches("^[ ]*attack direct[ ]*$"))
                throw new MyException("you can’t attack with this card");
            else if (input.matches("^[ ]*activate effect[ ]*$"))
                throw new MyException("activate effect is only for spell cards.");
            else if (input.matches("^[ ]*show graveyard[ ]*$"))
                Board.showGraveyard();
            else if (input.matches("^[ ]*card show --selected[ ]*$"))
                Game.getMainPhase1().showOpponentFieldZoneCard();
            else if (input.matches("^[ ]*surrender[ ]*$"))
                PhaseControl.getInstance().surrender();
            else
                throw new MyException("invalid command");
        }
    }

    private void selectHand(Matcher matcher) throws MyException {
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
                        Game.playTurn("MainPhase2");;
                    } else if (input.matches("^[ ]*select -d[ ]*$"))
                        BattlePhase.getInstance().selectCard();
                    else if (input.matches("^[ ]*select .*$"))
                        System.out.println("you already selected a card");
                    else if (input.matches("^[ ]*summon[ ]*$"))
                        BattlePhase.getInstance().summon(CommandMatcher.getCommandMatcher(selectedCard, "(^[ ]*select --hand [\\d]+[ ]*$)"));
                    else if (input.matches("^[ ]*set[ ]*$"))
                        throw new MyException("you can’t do this action in this phase");
                    else if (input.matches("^[ ]*set -- position (attack|defense)[ ]*$"))
                        throw new MyException("you can’t change this card position");
                    else if (input.matches("^[ ]*flip-summon[ ]*$"))
                        throw new MyException("you can’t change this card position");
                    else if (input.matches("^[ ]*attack [\\d]+[ ]*$"))
                        throw new MyException("you can’t attack with this card");
                    else if (input.matches("^[ ]*attack direct[ ]*$"))
                        throw new MyException("you can’t attack with this card");
                    else if (input.matches("^[ ]*activate effect[ ]*$"))
                        throw new MyException("activate effect is only for spell cards.");
                    else if (input.matches("^[ ]*show graveyard[ ]*$"))
                        Board.showGraveyard();
                    else if (input.matches("^[ ]*card show --selected[ ]*$"))
                        Game.getMainPhase1().showSelectedCard(CommandMatcher.getCommandMatcher(selectedCard, "(^[ ]*select --hand [\\d]+[ ]*$)"));
                    else if (input.matches("^[ ]*surrender[ ]*$"))
                        PhaseControl.getInstance().surrender();
                    else
                        throw new MyException("invalid command");
                }
            } else
                throw new MyException("invalid selection");
        }
    }

    private void attack(Matcher matcher, String myAddress) throws MyException {
        if (matcher.find()) {
            Address myAddressType = new Address(myAddress);
            Player currentPlayer = Game.whoseTurnPlayer();
            Address address = new Address(Integer.parseInt(matcher.group(2)), "monster", false);
            MonsterCard myMonsterCard = currentPlayer.getMonsterCardByStringAddress(myAddress);
            if (currentPlayer.getMonsterPosition(myAddressType.getNumber()).equals(PositionOfCardInBoard.OO)) {
                int index = currentPlayer.getIndexOfThisCardByAddress(myAddressType);
                if (!currentPlayer.didWeAttackByThisCardInThisCardInThisTurn(index)) {
                    if (Game.whoseRivalPlayer().getCardByAddress(address) != null) {
                        MonsterCard rivalMonsterCard = Game.whoseRivalPlayer().getMonsterCardByAddress(address);
                        currentPlayer.setDidWeAttackByThisCardInThisCardInThisTurn(index);
                        if (Game.whoseRivalPlayer().doIHaveSpellCard("Negate Attack") && !Game.whoseTurnPlayer().doIHaveMirageDragonMonster()) {
                            BattlePhase.getInstance().goToNextPhase = true;
                        } else if (Game.whoseRivalPlayer().doIHaveSpellCard("Mirror Force") && !Game.whoseTurnPlayer().doIHaveMirageDragonMonster()) {
                            if(BattlePhase.getInstance().getPermissionForTrap("Mirror Force")) {
                                Board.destroyAllAttackerMonster(Game.whoseTurnPlayer());
                                Game.whoseRivalPlayer().destroyMyTrapNamed("Mirror Force");
                            }
                        } else if ((Board.whatKindaMonsterIsHere(address).getNormalAttack() >= 1500)
                                && (SetSpell.doAnyOneHaveMessengerOfPeace())) {
                            throw new MyException("You can't attack by monster with attack equal or more than 1500 " +
                                    "because of MessengerOfPeace.");
                        } else if (Game.whoseRivalPlayer().doIHaveSpellCard("Magic Cylinder") && !Game.whoseTurnPlayer().doIHaveMirageDragonMonster()) {
                            if(BattlePhase.getInstance().getPermissionForTrap("Magic Cylinder")) {
                                currentPlayer.decreaseLP(myMonsterCard.getNormalAttack());
                                Game.whoseRivalPlayer().destroyMyTrapNamed("Magic Cylinder");
                                throw new MyException("Rival has trap named Magic Cylinder so its effect get done.");
                            }
                        } else if (rivalMonsterCard.getName().equals("Texchanger")) {
                            Game.getMainPhase1().summonCyberse();
                        } else {
                            if (Game.whoseRivalPlayer().positionOfCardInBoardByAddress(address).equals(PositionOfCardInBoard.OO)) {
                                int damage = myMonsterCard.getAttack(myAddressType) - rivalMonsterCard.getAttack(address);
                                attackOO(myAddressType, address, index, currentPlayer, myMonsterCard, rivalMonsterCard, damage);
                                Attack.timeToEffectAfterAttack();
                            } else if (Game.whoseRivalPlayer().positionOfCardInBoardByAddress(address).equals(PositionOfCardInBoard.DO)) {
                                if (rivalMonsterCard.getDefence(true, address) != -1) {
                                    int damage = myMonsterCard.getAttack(myAddressType) - rivalMonsterCard.getDefence(true, address);
                                    attackDO(myAddressType, address, index, currentPlayer, myMonsterCard, rivalMonsterCard, damage);
                                    Attack.timeToEffectAfterAttack();
                                } else throw new MyException("Attack has been cancelled for effect of a card");
                            } else {
                                int damage = myMonsterCard.getAttack(myAddressType) - rivalMonsterCard.getDefence(false, address);
                                attackDH(myAddressType, address, index, currentPlayer, myMonsterCard, rivalMonsterCard, damage);
                                Attack.timeToEffectAfterAttack();
                            }
                        }
                    } else throw new MyException("there is no card to attack here");
                } else throw new MyException("this card already attacked");
            } else throw new MyException("you cant attack with this card");
        }
    }

    private void attackOO(Address myAddress, Address address, int index, Player currentPlayer, MonsterCard myMonsterCard, MonsterCard rivalMonsterCard, int damage) throws MyException {
        if (damage == 0) {
            removeForAttack(address, myAddress);
            removeForAttack(myAddress, address);
            throw new MyException("both you and your opponent monster cards are destroyed and no one receives damage");
        } else if (damage > 0) {
            removeForAttack(address, myAddress);
            decreaseLP(myAddress, address, index, Game.whoseRivalPlayer(), myMonsterCard, rivalMonsterCard, damage);
            throw new MyException("your opponent’s monster is destroyed and your opponent receives " + damage + " battle damage");
        } else {
            damage = (-1) * damage;
            currentPlayer.decreaseLP(damage);
            removeForAttack(myAddress, address);
            throw new MyException("Your monster card is destroyed and you received " + damage + " battle damage");
        }
    }

    private void attackDO(Address myAddress, Address address, int index, Player currentPlayer, MonsterCard myMonsterCard, MonsterCard rivalMonsterCard, int damage) throws MyException {
        if (damage == 0) throw new MyException("no card is destroyed");
        else if (damage > 0) {
            removeForAttack(address, myAddress);
            throw new MyException("the defense position monster is destroyed");
        } else {
            damage = (-1) * damage;
            decreaseLP(myAddress, address, index, currentPlayer, myMonsterCard, rivalMonsterCard, damage);
            throw new MyException("no card is destroyed and you received " + damage + " battle damage");
        }
    }

    private void attackDH(Address myAddress, Address address, int index, Player currentPlayer, MonsterCard myMonsterCard, MonsterCard rivalMonsterCard, int damage) throws MyException {
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
        throw new MyException(shouldBePrinted);
    }

    private void decreaseLP(Address myAddress, Address address, int index, Player player, MonsterCard myMonsterCard, MonsterCard rivalMonsterCard, int damage) {
        if (!(Game.whoseTurnPlayer().getMonsterCardByAddress(address).getName().equals("ExploderDragon"))) {
            player.decreaseLP(damage);
        }
        PhaseControl.getInstance().checkIfGameEnded();
    }

    private void removeForAttack(Address destroyedAddress, Address theOtherAddress) {
        if (Board.getCardByAddress(theOtherAddress) != null &&
                Board.getCardByAddress(destroyedAddress).getCardName().equals("Exploder Dragon"))
            Board.removeCardByAddress(theOtherAddress);
        if (!Board.getCardByAddress(destroyedAddress).getCardName().equals("Marshmallon"))
            Board.removeCardByAddress(destroyedAddress);
    }

    private void flipSummon(Matcher matcher) throws MyException {
        throw new MyException("you can’t do this action in this phase");
    }

    private void setPosition(Matcher matcher) throws MyException {
        throw new MyException("you can’t do this action in this phase");
    }

    private void directAttack(Matcher matcher) throws MyException {
        PhaseControl.getInstance().checkIfGameEnded();
        if (matcher.find()) {
            Player currentPlayer = Game.whoseTurnPlayer();
            Player rivalPlayer = Game.whoseRivalPlayer();
            if (currentPlayer.getMonsterPosition(Integer.parseInt(matcher.group(2))).equals(PositionOfCardInBoard.OO)) {
                if (rivalPlayer.getMonsterZoneCard().size() == 0) {
                    int index = currentPlayer.getIndexOfThisCardByAddress(new Address(matcher.group(1)));
                    if (!currentPlayer.didWeAttackByThisCardInThisCardInThisTurn(index)) {
                        currentPlayer.setDidWeAttackByThisCardInThisCardInThisTurn(index);
                        MonsterCard monsterCardForDirectAttack = currentPlayer.getMonsterCardByStringAddress(matcher.group(1));
                        rivalPlayer.decreaseLP(monsterCardForDirectAttack.getAttack(new Address(matcher.group(1))));
                        throw new MyException("you opponent receives " + monsterCardForDirectAttack.getAttack(new Address(matcher.group(1))) + " battle damage");
                    } else throw new MyException("this card already attacked");
                } else throw new MyException("rival monster zone is not empty");
            } else throw new MyException("you cant attack with this card");
        }
        PhaseControl.getInstance().checkIfGameEnded();
    }

    private void specialSummon(Matcher matcher) {

    }

    private void activeSpell(Matcher matcher) throws MyException {
        throw new MyException("you can’t do this action in this phase");
    }

    private void activeTrap(Matcher matcher) {

    }

    private void ritualSummon(Matcher matcher) {

    }
}
