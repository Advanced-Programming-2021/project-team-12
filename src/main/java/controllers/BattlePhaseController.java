package controllers;

import Exceptions.*;
import Utility.CommandMatcher;
import controllers.move.Attack;
import controllers.move.SetSpell;
import models.*;
import view.GameView;
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
        else if (input.matches("[ ]*increase --LP [\\d]+[ ]*"))
            throw new MyException("you can’t do this action in this phase");
        else if (input.matches("duel set-winner(.)*"))
            forcedSetWinner();
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
            throw new MyException("no card is selected yet");
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
                    else input = getAIAttack(matcher.group(2));
                    if (input.matches("^[ ]*next phase[ ]*$")) {
                        Game.getGameView().goToMainPhaseTwo(null);
                    } else if (input.matches("^[ ]*select -d[ ]*$"))
                        BattlePhase.getInstance().selectCard();
                    else if (input.matches("^[ ]*select .*$"))
                        System.out.println("you already selected a card");
                    else if (input.matches("^[ ]*summon[ ]*$"))
                        System.out.println("you can’t summon this card");
                    else if (input.matches("^[ ]*set[ ]*$"))
                        System.out.println("you can’t set this card");
                    else if (input.matches("^[ ]*set -- position (attack|defense)[ ]*$"))
                        throw new MyException("you can’t do this action in this phase");
                    else if (input.matches("^[ ]*flip-summon[ ]*$"))
                        throw new MyException("you can’t do this action in this phase");
                    else if (input.matches("^[ ]*attack [\\d]+[ ]*$"))
                        throw new MyException("you can’t do this action in this phase");
                    else if (input.matches("^[ ]*attack direct[ ]*$"))
                        throw new MyException("you can’t do this action in this phase");
                    else if (input.matches("^[ ]*activate effect[ ]*$"))
                        System.out.println("activate effect is only for spell cards.");
                    else if (input.matches("^[ ]*show graveyard[ ]*$"))
                        throw new MyException("no card is selected yet");
                    else if (input.matches("^[ ]*card show --selected[ ]*$"))
                        Game.getMainPhase1().showSelectedCard(CommandMatcher.getCommandMatcher(selectedCard, "(^[ ]*select --monster ([\\d]+)[ ]*$)"));
                    else if (input.matches("^[ ]*surrender[ ]*$"))
                        PhaseControl.getInstance().surrender();
                    else
                        throw new MyException("invalid command");
                    if (Game.isAITurn()){

                    }
                        //Game.playTurn("MainPhase2");
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
                tempDamage = MonsterCard.getMonsterCardByName(card.getCardName()).getAttack(address) - MonsterCard.getMonsterCardByName(player.getCardMonster(i).getCardName()).getDefence(true, new Address(i, "monster", false));
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
                        Game.getGameView().goToMainPhaseTwo(null);
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
                        throw new MyException("no card is selected yet");
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
                        Game.getGameView().goToMainPhaseTwo(null);
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
                        throw new MyException("no card is selected yet");
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
                        Game.getGameView().goToMainPhaseTwo(null);
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
                        throw new MyException("no card is selected yet");
                    else if (input.matches("^[ ]*card show --selected[ ]*$"))
                        Game.getMainPhase1().showSelectedCard(CommandMatcher.getCommandMatcher(selectedCard, "(^[ ]*select --spell --opponent ([\\d]+)[ ]*$)"));
                    else if (input.matches("^[ ]*surrender[ ]*$"))
                        PhaseControl.getInstance().surrender();
                    else
                        throw new MyException("invalid command");
                }
            } else throw new MyException("invalid selection");
        }
    }

    private void selectField() throws MyException {
        String input;
        while (true) {
            PhaseControl.getInstance().checkIfGameEnded();
            Board.showBoard();
            input = Main.scanner.nextLine().trim();
            if (input.matches("^[ ]*next phase[ ]*$")) {
                Game.getGameView().goToMainPhaseTwo(null);
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
                throw new MyException("no card is selected yet");
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
                Game.getGameView().goToMainPhaseTwo(null);
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
                throw new MyException("no card is selected yet");
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
                        Game.getGameView().goToMainPhaseTwo(null);
                    } else if (input.matches("^[ ]*select -d[ ]*$"))
                        BattlePhase.getInstance().selectCard();
                    else if (input.matches("^[ ]*select .*$"))
                        System.out.println("you already selected a card");
                    else if (input.matches("^[ ]*summon[ ]*$"))
                        throw new MyException("action not allowed in this phase");
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
                        throw new MyException("no card is selected yet");
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

    public void attack(Address address, Address myAddress) throws MyException {
        if (Game.getCurrentPhase().equals("Battle Phase")) {
            Player currentPlayer = Game.whoseTurnPlayer();
            Attack.setAddress(myAddress, true);
            Attack.setAddress(address, false);
            MonsterCard myMonsterCard = currentPlayer.getMonsterCardByAddress(myAddress);
            if (currentPlayer.getMonsterPosition(myAddress.getNumber()).equals(PositionOfCardInBoard.OO)) {
                int index = currentPlayer.getIndexOfThisCardByAddress(myAddress);
                if (!currentPlayer.didWeAttackByThisCardInThisCardInThisTurn(index)) {
                    if (Game.whoseRivalPlayer().getCardByAddress(address) != null) {
                        MonsterCard rivalMonsterCard = Game.whoseRivalPlayer().getMonsterCardByAddress(address);
                        currentPlayer.setDidWeAttackByThisCardInThisCardInThisTurn(index);
                        if (Game.whoseRivalPlayer().doIHaveSpellCard("Negate Attack")
                                && !Game.whoseTurnPlayer().doIHaveMirageDragonMonster()) {
                            Game.getGameView().getPermissionForTrap("Negate Attack", false, null, null, 1, null);
                        } else if (Game.whoseRivalPlayer().doIHaveSpellCard("Mirror Force")
                                && !Game.whoseTurnPlayer().doIHaveMirageDragonMonster()) {
                            Game.getGameView().getPermissionForTrap("Mirror Force", false, null, null, 1, null);
                        } else if ((Board.whatKindaMonsterIsHere(address).getNormalAttack() >= 1500)
                                && (SetSpell.doAnyOneHaveMessengerOfPeace())) {
                            throw new MyException("You can't attack by monster with attack equal or more than 1500 " +
                                    "because of Messenger of peace.");
                        } else if (Game.whoseRivalPlayer().doIHaveSpellCard("Magic Cylinder")
                                && !Game.whoseTurnPlayer().doIHaveMirageDragonMonster()) {
                            Game.getGameView().getPermissionForTrap("Magic Cylinder", false, myMonsterCard, null, 1, null);
                        } else if (rivalMonsterCard.getNamesForEffect().contains("Texchanger")) {
                            Game.getGameView().summonCyberse();
                        } else {
                            if (Game.whoseRivalPlayer().positionOfCardInBoardByAddress(address).equals(PositionOfCardInBoard.OO)) {
                                int damage = myMonsterCard.getAttack(myAddress) - rivalMonsterCard.getAttack(address);
                                attackOO(myAddress, address, currentPlayer, damage);
                                Attack.timeToEffectAfterAttack();
                            } else if (Game.whoseRivalPlayer().positionOfCardInBoardByAddress(address).equals(PositionOfCardInBoard.DO)) {
                                if (rivalMonsterCard.getDefence(true, address) != -1) {
                                    int damage = myMonsterCard.getAttack(myAddress) - rivalMonsterCard.getDefence(true, address);
                                    attackDO(myAddress, address, currentPlayer, damage);
                                    Attack.timeToEffectAfterAttack();
                                } else throw new MyException("Attack has been cancelled for effect of a card");
                            } else {
                                int damage = myMonsterCard.getAttack(myAddress) - rivalMonsterCard.getDefence(false, address);
                                attackDH(myAddress, address, currentPlayer, rivalMonsterCard, damage);
                                Attack.timeToEffectAfterAttack();
                            }
                        }
                    } else throw new MyException("there is no card to attack here");
                } else throw new MyException("this card already attacked");
            } else throw new MyException("you cant attack with this card");
        }
    }

    public void doMagicCylinder(Player currentPlayer, MonsterCard myMonsterCard) throws MyException {
        if(Game.getGameView().yesOrNo){
            if (!currentPlayer.doIHaveSpellCard("Ring of defense")) {
                currentPlayer.decreaseLP(myMonsterCard.getNormalAttack());
            }
            Game.whoseRivalPlayer().removeOneOfTrapOrSpell("Magic Cylinder");
            throw new MyException("Rival has trap named Magic Cylinder so its effect get done.");
        }
    }

    public void doMirrorForce() {
        if(Game.getGameView().yesOrNo){
            Board.destroyAllAttackerMonster(Game.whoseRivalPlayer());
            Game.whoseRivalPlayer().removeOneOfTrapOrSpell("Mirror Force");
        }
    }

    public void doNegateAttack() {
        if(Game.getGameView().yesOrNo){
            Game.whoseRivalPlayer().removeOneOfTrapOrSpell("Negate Attack");
            Game.getGameView().reset();
            //Game.getGameView().goToMainPhaseTwo(null);
        }
    }

    private void attackOO(Address myAddress, Address address, Player currentPlayer, int damage) throws MyException {
        if (Game.getCurrentPhase().equals("Battle Phase")) {
            if (damage == 0) {
                removeForAttack(address, myAddress);
                removeForAttack(myAddress, address);
                throw new MyException("Both you and your opponent monster cards are destroyed and no one receives damage");
            } else if (damage > 0) {
                removeForAttack(address, myAddress);
                decreaseLP(address, Game.whoseRivalPlayer(), damage);
                throw new MyException("Your opponent’s monster is destroyed and your opponent receives " + damage + " battle damage");
            } else {
                damage = (-1) * damage;
                currentPlayer.decreaseLP(damage);
                removeForAttack(myAddress, address);
                throw new MyException("Your monster card is destroyed and you received " + damage + " battle damage");
            }
        }
    }

    private void attackDO(Address myAddress, Address address, Player currentPlayer, int damage) throws MyException {
        if (Game.getCurrentPhase().equals("Battle Phase")) {
            if (damage == 0) throw new MyException("No card is destroyed");
            else if (damage > 0) {
                removeForAttack(address, myAddress);
                throw new MyException("The defense position monster is destroyed");
            } else {
                damage = (-1) * damage;
                decreaseLP(address, currentPlayer, damage);
                throw new MyException("No card is destroyed and you received " + damage + " battle damage");
            }
        }
    }

    public void attackDH(Address myAddress, Address address, Player currentPlayer, MonsterCard rivalMonsterCard, int damage) throws MyException {
        if (Game.getCurrentPhase().equals("Battle Phase")) {
            String shouldBePrinted;
            if (damage == 0)
                shouldBePrinted = "Opponent’s monster card was " + rivalMonsterCard.getRealName() + " and no card is destroyed";
            else if (damage > 0) {
                removeForAttack(address, myAddress);
                shouldBePrinted = "Opponent’s monster card was " + rivalMonsterCard.getRealName() + " and " + "the defense position monster is destroyed";
            } else {
                damage = (-1) * damage;
                decreaseLP(address, currentPlayer, damage);
                shouldBePrinted = "Opponent’s monster card was " + rivalMonsterCard.getRealName() + " and " + "no card is destroyed and you received " + damage + " battle damage";
            }
            currentPlayer.setPositionOfCardInBoardByAddress(address, PositionOfCardInBoard.DO);
            throw new MyException(shouldBePrinted);
        }
    }

    private void decreaseLP(Address address, Player player, int damage) {
        if (!(Game.whoseTurnPlayer().getMonsterCardByAddress(address).getNamesForEffect().contains("Exploder Dragon")))
            player.decreaseLP(damage);
        PhaseControl.getInstance().checkIfGameEnded();
    }

    private void removeForAttack(Address destroyedAddress, Address theOtherAddress) {
        if (Game.getCurrentPhase().equals("Battle Phase")) {
            if (Board.getCardByAddress(theOtherAddress) != null &&
                    Board.getCardByAddress(destroyedAddress).getCardName().equals("Exploder Dragon"))
                Board.removeCardByAddress(theOtherAddress);
            if (!Board.getCardByAddress(destroyedAddress).getCardName().equals("Marshmallon"))
                Board.removeCardByAddress(destroyedAddress);
        }
    }

    public void directAttack(Address address) throws MyException {
        if (Game.getCurrentPhase().equals("Battle Phase")) {
            PhaseControl.getInstance().checkIfGameEnded();
            Player currentPlayer = Game.whoseTurnPlayer();
            Player rivalPlayer = Game.whoseRivalPlayer();
            if (currentPlayer.getMonsterPosition(address.getNumber()).equals(PositionOfCardInBoard.OO)) {
                if (rivalPlayer.getMonsterZoneCard().size() == 0) {
                    int index = currentPlayer.getIndexOfThisCardByAddress(address);
                    if (!currentPlayer.didWeAttackByThisCardInThisCardInThisTurn(index)) {
                        currentPlayer.setDidWeAttackByThisCardInThisCardInThisTurn(index);
                        MonsterCard monsterCardForDirectAttack = currentPlayer.getMonsterCardByAddress(address);
                        rivalPlayer.decreaseLP(monsterCardForDirectAttack.getAttack(address));
                        throw new MyException("you opponent receives " + monsterCardForDirectAttack.getAttack(address) + " battle damage");
                    } else throw new MyException("this card already attacked");
                } else throw new MyException("rival monster zone is not empty");
            } else throw new MyException("you cant attack with this card");
        }
    }

    private void forcedIncreaseLP(int LP) {
        Game.whoseTurnPlayer().increaseLP(LP);
    }

    private void forcedSetWinner() {
        Game.setWinner(Game.whoseTurnPlayer());
        //Game.playTurn("EndGame");
    }
}
