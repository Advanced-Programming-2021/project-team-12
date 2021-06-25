package view.phase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Exceptions.MyException;
import controllers.BattlePhaseController;
import controllers.Game;
import controllers.PhaseControl;
import models.Board;
import models.Player;
import view.Main;

public class BattlePhase {
    public Boolean goToNextPhase = false;

    private static BattlePhase instance;

    public static BattlePhase getInstance() {
        if (instance == null) {
            instance = new BattlePhase();
        }
        return instance;
    }

    public void run() {
        System.out.println("phase: battle phase");
        Board.showBoard();
        selectCard();
        Game.playTurn("MainPhase2");
    }

    public void selectCard() {
        String input;
        if (Game.isAITurn())
            AIRun();
        else {
            while (true) {
                input = Main.scanner.nextLine().trim();
                PhaseControl.getInstance().checkIfGameEnded();
                try {
                    if (BattlePhaseController.getInstance().battlePhaseRun(input)) break;
                } catch (MyException e) {
                    System.out.println(e.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void AIRun() {
        Player player = Game.whoseTurnPlayer();
        for (int i = 1; i < 6; i++) {
            if (player.getMonsterZoneCard().containsKey(i)) {
                PhaseControl.getInstance().checkIfGameEnded();
                try {
                    if (BattlePhaseController.getInstance().battlePhaseRun("select --monster " + i)) break;
                } catch (MyException e) {
                    System.out.println(e.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private String getStrongestMonster() {
        Player player = Game.whoseTurnPlayer();
        int maxAttack = 0;
        int place = 0;
        for (int i = 1; i < 6; i++) {
            if (player.getMonsterZoneCard().containsKey(i) && player.getCardMonster(i).getAttack() > maxAttack) {
                place = i;
                maxAttack = player.getCardMonster(i).getAttack();
            }
        }
        return Integer.toString(place);
    }

//    private void selectMonster(Matcher matcher) {
//        if (matcher.find()) {
//            Board.showBoeard();
//            if (Address.isAddressValid(matcher.group(1))) {
//                String selectedCard = matcher.group(1);
//                String input;
//                while (true) {
//                    PhaseControl.getInstance().checkIfGameEnded();
//                    input = Main.scanner.nextLine().trim();
//                    if (input.matches("^[ ]*next phase[ ]*$")) {
//                        goToNextPhase = true;
//                        break;
//                    } else if (input.matches("^[ ]*select -d[ ]*$"))
//                        break;
//                    else if (input.matches("^[ ]*select [.*][ ]*$"))
//                        System.out.println("");
//                        //here
//                    else if (input.matches("^[ ]*summon[ ]*$"))
//                        System.out.println("you can’t summon this card");
//                    else if (input.matches("^[ ]*set[ ]*$"))
//                        System.out.println("you can’t set this card");
//                    else if (input.matches("^[ ]*set -- position (attack|defense)[ ]*$"))
//                        setPosition(getCommandMatcher(selectedCard, "(^[ ]*select --monster ([\\d]+)[ ]*$)"));
//                    else if (input.matches("^[ ]*flip-summon[ ]*$"))
//                        flipSummon(getCommandMatcher(selectedCard, "(^[ ]*select --monster ([\\d]+)[ ]*$)"));
//                    else if (input.matches("^[ ]*attack [\\d]+[ ]*$"))
//                        attack(getCommandMatcher(input, "(^[ ]*attack ([\\d]+)[ ]*$)"), selectedCard);
//                    else if (input.matches("^[ ]*attack direct[ ]*$"))
//                        directAttack(getCommandMatcher(selectedCard, "(^[ ]*select --monster ([\\d]+)[ ]*$)"));
//                    else if (input.matches("^[ ]*activate effect[ ]*$"))
//                        System.out.println("activate effect is only for spell cards.");
//                    else if (input.matches("^[ ]*show graveyard[ ]*$"))
//                        Game.getMainPhase1().showGraveyard();
//                    else if (input.matches("^[ ]*card show --selected[ ]*$"))
//                        Game.getMainPhase1().showSelectedCard(getCommandMatcher(selectedCard, "(^[ ]*select --monster ([\\d]+)[ ]*$)"));
//                    else if (input.matches("^[ ]*surrender[ ]*$"))
//                        Game.getMainPhase1().surrender();
//                    else
//                        System.out.println("invalid command");
//                }
//            } else
//                System.out.println("invalid selection");
//        }
//    }
//
//    private void selectOpponentMonster(Matcher matcher) {
//        if (matcher.find()) {
//            Board.showBoeard();
//            if (Address.isAddressValid(matcher.group(1))) {
//                String selectedCard = matcher.group(1);
//                String input;
//                while (true) {
//                    PhaseControl.getInstance().checkIfGameEnded();
//                    input = Main.scanner.nextLine().trim();
//                    if (input.matches("^[ ]*next phase[ ]*$")) {
//                        goToNextPhase = true;
//                        break;
//                    } else if (input.matches("^[ ]*select -d[ ]*$"))
//                        break;
//                    else if (input.matches("^[ ]*select [.*][ ]*$"))
//                        System.out.println("");
//                        //here
//                    else if (input.matches("^[ ]*summon[ ]*$"))
//                        System.out.println("you can’t summon this card");
//                    else if (input.matches("^[ ]*set[ ]*$"))
//                        System.out.println("you can’t set this card");
//                    else if (input.matches("^[ ]*set -- position (attack|defense)[ ]*$"))
//                        System.out.println("you can’t change this card position");
//                    else if (input.matches("^[ ]*flip-summon[ ]*$"))
//                        System.out.println("you can’t change this card position");
//                    else if (input.matches("^[ ]*attack [\\d]+[ ]*$"))
//                        System.out.println("you can’t attack with this card");
//                    else if (input.matches("^[ ]*attack direct[ ]*$"))
//                        System.out.println("you can’t attack with this card");
//                    else if (input.matches("^[ ]*activate effect[ ]*$"))
//                        System.out.println("activate effect is only for spell cards.");
//                    else if (input.matches("^[ ]*show graveyard[ ]*$"))
//                        Game.getMainPhase1().showGraveyard();
//                    else if (input.matches("^[ ]*card show --selected[ ]*$"))
//                        Game.getMainPhase1().showSelectedCard(getCommandMatcher(selectedCard, "(^[ ]*select --monster --opponent [\\d]+[ ]*$)"));
//                    else if (input.matches("^[ ]*surrender[ ]*$"))
//                        Game.getMainPhase1().surrender();
//                    else
//                        System.out.println("invalid command");
//                }
//            } else
//                System.out.println("invalid selection");
//        }
//    }
//
//    private void selectSpell(Matcher matcher) {
//        if (matcher.find()) {
//            Board.showBoeard();
//            if (Address.isAddressValid(matcher.group(1))) {
//                String selectedCard = matcher.group(1);
//                String input;
//                while (true) {
//                    PhaseControl.getInstance().checkIfGameEnded();
//                    input = Main.scanner.nextLine().trim();
//                    if (input.matches("^[ ]*next phase[ ]*$")) {
//                        goToNextPhase = true;
//                        break;
//                    } else if (input.matches("^[ ]*select -d[ ]*$"))
//                        break;
//                    else if (input.matches("^[ ]*select [.*][ ]*$"))
//                        System.out.println("");
//                        //here
//                    else if (input.matches("^[ ]*summon[ ]*$"))
//                        System.out.println("you can’t summon this card");
//                    else if (input.matches("^[ ]*set[ ]*$"))
//                        System.out.println("you can’t set this card");
//                    else if (input.matches("^[ ]*set -- position (attack|defense)[ ]*$"))
//                        System.out.println("you can’t change this card position");
//                    else if (input.matches("^[ ]*flip-summon[ ]*$"))
//                        System.out.println("you can’t change this card position");
//                    else if (input.matches("^[ ]*attack [\\d]+[ ]*$"))
//                        System.out.println("you can’t attack with this card");
//                    else if (input.matches("^[ ]*attack direct[ ]*$"))
//                        System.out.println("you can’t attack with this card");
//                    else if (input.matches("^[ ]*activate effect[ ]*$"))
//                        activeSpell(getCommandMatcher(selectedCard, "(^[ ]*select --spell ([\\d]+)[ ]*$)"));
//                    else if (input.matches("^[ ]*show graveyard[ ]*$"))
//                        Game.getMainPhase1().showGraveyard();
//                    else if (input.matches("^[ ]*card show --selected[ ]*$"))
//                        Game.getMainPhase1().showSelectedCard(getCommandMatcher(selectedCard, "(^[ ]*select --spell ([\\d]+)[ ]*$)"));
//                    else if (input.matches("^[ ]*surrender[ ]*$"))
//                        Game.getMainPhase1().surrender();
//                    else
//                        System.out.println("invalid command");
//                }
//            } else
//                System.out.println("invalid selection");
//        }
//    }
//
//    private void selectOpponentSpell(Matcher matcher) {
//        if (matcher.find()) {
//            if (Address.isAddressValid(matcher.group(1))) {
//                String selectedCard = matcher.group(1);
//                String input;
//                while (true) {
//                    PhaseControl.getInstance().checkIfGameEnded();
//                    Board.showBoeard();
//                    input = Main.scanner.nextLine().trim();
//                    if (input.matches("^[ ]*next phase[ ]*$")) {
//                        goToNextPhase = true;
//                        break;
//                    } else if (input.matches("^[ ]*select -d[ ]*$"))
//                        break;
//                    else if (input.matches("^[ ]*select [.*][ ]*$"))
//                        System.out.println("");
//                        //here
//                    else if (input.matches("^[ ]*summon[ ]*$"))
//                        System.out.println("you can’t summon this card");
//                    else if (input.matches("^[ ]*set[ ]*$"))
//                        System.out.println("you can’t set this card");
//                    else if (input.matches("^[ ]*set -- position (attack|defense)[ ]*$"))
//                        System.out.println("you can’t change this card position");
//                    else if (input.matches("^[ ]*flip-summon[ ]*$"))
//                        System.out.println("you can’t change this card position");
//                    else if (input.matches("^[ ]*attack [\\d]+[ ]*$"))
//                        System.out.println("you can’t attack with this card");
//                    else if (input.matches("^[ ]*attack direct[ ]*$"))
//                        System.out.println("you can’t attack with this card");
//                    else if (input.matches("^[ ]*activate effect[ ]*$"))
//                        System.out.println("activate effect is only for spell cards.");
//                    else if (input.matches("^[ ]*show graveyard[ ]*$"))
//                        Game.getMainPhase1().showGraveyard();
//                    else if (input.matches("^[ ]*card show --selected[ ]*$"))
//                        Game.getMainPhase1().showSelectedCard(getCommandMatcher(selectedCard, "(^[ ]*select --spell --opponent ([\\d]+)[ ]*$)"));
//                    else if (input.matches("^[ ]*surrender[ ]*$"))
//                        Game.getMainPhase1().surrender();
//                    else
//                        System.out.println("invalid command");
//                }
//            } else
//                System.out.println("invalid selection");
//        }
//    }
//
//    private void selectField() {
//        String input;
//        while (true) {
//            PhaseControl.getInstance().checkIfGameEnded();
//            Board.showBoeard();
//            input = Main.scanner.nextLine().trim();
//            if (input.matches("^[ ]*next phase[ ]*$")) {
//                goToNextPhase = true;
//                break;
//            } else if (input.matches("^[ ]*select -d[ ]*$"))
//                break;
//            else if (input.matches("^[ ]*select [.*][ ]*$"))
//                System.out.println("");
//                //here
//            else if (input.matches("^[ ]*summon[ ]*$"))
//                System.out.println("you can’t summon this card");
//            else if (input.matches("^[ ]*set[ ]*$"))
//                System.out.println("you can’t set this card");
//            else if (input.matches("^[ ]*set -- position (attack|defense)[ ]*$"))
//                System.out.println("you can’t change this card position");
//            else if (input.matches("^[ ]*flip-summon[ ]*$"))
//                System.out.println("you can’t change this card position");
//            else if (input.matches("^[ ]*attack [\\d]+[ ]*$"))
//                System.out.println("you can’t attack with this card");
//            else if (input.matches("^[ ]*attack direct[ ]*$"))
//                System.out.println("you can’t attack with this card");
//            else if (input.matches("^[ ]*activate effect[ ]*$"))
//                System.out.println("activate effect is only for spell cards.");
//            else if (input.matches("^[ ]*show graveyard[ ]*$"))
//                Game.getMainPhase1().showGraveyard();
//            else if (input.matches("^[ ]*card show --selected[ ]*$"))
//                Game.getMainPhase1().showFieldZoneCard();
//            else if (input.matches("^[ ]*surrender[ ]*$"))
//                Game.getMainPhase1().surrender();
//            else
//                System.out.println("invalid command");
//        }
//    }
//
//    private void selectOpponentField() {
//        String input;
//        while (true) {
//            PhaseControl.getInstance().checkIfGameEnded();
//            Board.showBoeard();
//            input = Main.scanner.nextLine().trim();
//            if (input.matches("^[ ]*next phase[ ]*$")) {
//                goToNextPhase = true;
//                break;
//            } else if (input.matches("^[ ]*select -d[ ]*$"))
//                break;
//            else if (input.matches("^[ ]*select [.*][ ]*$"))
//                System.out.println("");
//                //here
//            else if (input.matches("^[ ]*summon[ ]*$"))
//                System.out.println("you can’t summon this card");
//            else if (input.matches("^[ ]*set[ ]*$"))
//                System.out.println("you can’t set this card");
//            else if (input.matches("^[ ]*set -- position (attack|defense)[ ]*$"))
//                System.out.println("you can’t change this card position");
//            else if (input.matches("^[ ]*flip-summon[ ]*$"))
//                System.out.println("you can’t change this card position");
//            else if (input.matches("^[ ]*attack [\\d]+[ ]*$"))
//                System.out.println("you can’t attack with this card");
//            else if (input.matches("^[ ]*attack direct[ ]*$"))
//                System.out.println("you can’t attack with this card");
//            else if (input.matches("^[ ]*activate effect[ ]*$"))
//                System.out.println("activate effect is only for spell cards.");
//            else if (input.matches("^[ ]*show graveyard[ ]*$"))
//                Game.getMainPhase1().showGraveyard();
//            else if (input.matches("^[ ]*card show --selected[ ]*$"))
//                Game.getMainPhase1().showOpponentFieldZoneCard();
//            else if (input.matches("^[ ]*surrender[ ]*$"))
//                Game.getMainPhase1().surrender();
//            else
//                System.out.println("invalid command");
//        }
//    }
//
//    private void selectHand(Matcher matcher) {
//        if (matcher.find()) {
//            Board.showBoeard();
//            if (Address.isAddressValid(matcher.group(1))) {
//                String selectedCard = matcher.group(1);
//                String input;
//                while (true) {
//                    PhaseControl.getInstance().checkIfGameEnded();
//                    input = Main.scanner.nextLine().trim();
//                    if (input.matches("^[ ]*next phase[ ]*$")) {
//                        goToNextPhase = true;
//                        break;
//                    } else if (input.matches("^[ ]*select -d[ ]*$"))
//                        break;
//                    else if (input.matches("^[ ]*select [.*][ ]*$"))
//                        System.out.println("");
//                        //here
//                    else if (input.matches("^[ ]*summon[ ]*$"))
//                        summon(getCommandMatcher(selectedCard, "(^[ ]*select --hand [\\d]+[ ]*$)"));
//                    else if (input.matches("^[ ]*set[ ]*$"))
//                        System.out.println("you can’t do this action in this phase");
//                    else if (input.matches("^[ ]*set -- position (attack|defense)[ ]*$"))
//                        System.out.println("you can’t change this card position");
//                    else if (input.matches("^[ ]*flip-summon[ ]*$"))
//                        System.out.println("you can’t change this card position");
//                    else if (input.matches("^[ ]*attack [\\d]+[ ]*$"))
//                        System.out.println("you can’t attack with this card");
//                    else if (input.matches("^[ ]*attack direct[ ]*$"))
//                        System.out.println("you can’t attack with this card");
//                    else if (input.matches("^[ ]*activate effect[ ]*$"))
//                        System.out.println("activate effect is only for spell cards.");
//                    else if (input.matches("^[ ]*show graveyard[ ]*$"))
//                        Game.getMainPhase1().showGraveyard();
//                    else if (input.matches("^[ ]*card show --selected[ ]*$"))
//                        Game.getMainPhase1().showSelectedCard(getCommandMatcher(selectedCard, "(^[ ]*select --hand [\\d]+[ ]*$)"));
//                    else if (input.matches("^[ ]*surrender[ ]*$"))
//                        Game.getMainPhase1().surrender();
//                    else
//                        System.out.println("invalid command");
//                }
//            } else
//                System.out.println("invalid selection");
//        }
//    }

    public void summon(Matcher matcher) {
        System.out.println("action not allowed in this phase");
    }

//
//    private void attack(Matcher matcher, String myAddress) {
//        if (matcher.find()) {
//            Player currentPlayer = Game.whoseTurnPlayer();
//            Address address = new Address(Integer.parseInt(matcher.group(2)), "monster", false);
//            int index = currentPlayer.getIndexOfThisCardByAddress(new Address(myAddress));
//            if (currentPlayer.didWeAttackByThisCardInThisCardInThisTurn(index)) {
//                if (currentPlayer.getCardByAddress(address) != null) {
//                    MonsterCard myMonsterCard = currentPlayer.getMonsterCardByStringAddress(myAddress);
//                    MonsterCard rivalMonsterCard = currentPlayer.getMonsterCardByAddress(address);
//                    Address myAddressType = new Address(myAddress);
//                    currentPlayer.setDidWeAttackByThisCardInThisCardInThisTurn(index);
//                    if (Game.whoseRivalPlayer().doIHaveActivatedTrapNamedNegateAttack()) {
//                        goToNextPhase = true;
//                    } else if (Game.whoseRivalPlayer().doIHaveActivatedTrapNamedMirrorForce()) {
//                        Game.whoseRivalPlayer().destroyAllRivalMonstersWhichInAttackMode();
//                    } else if ((Board.whatKindaMonsterIsHere(address).getNormalAttack() >= 1500)
//                            && (SetSpell.doAnyOneHaveMessengerOfPeace())) {
//                        System.out.println("You can't attack by monster with attack equal or more than 1500 " +
//                                "because of MessengerOfPeace.");
//                    } else if (Game.whoseRivalPlayer().doIHaveActivatedTrapNamedMagicCylinder()) {
//                        currentPlayer.decreaseLP(myMonsterCard.getNormalAttack());
//                        System.out.println("Rival has trap named MagicCylinder so its effect get done.");
//                    } else {
//                        if (currentPlayer.positionOfCardInBoardByAddress(address) == PositionOfCardInBoard.OO) {
//                            int damage = myMonsterCard.getAttack() - rivalMonsterCard.getAttack();
//                            attackOO(myAddressType, address, index, currentPlayer, myMonsterCard, rivalMonsterCard, damage);
//                            Attack.timeToEffectAfterAttack();
//                        } else if (currentPlayer.positionOfCardInBoardByAddress(address) == PositionOfCardInBoard.DO) {
//                            if (rivalMonsterCard.getDefence(true) != -1) {
//                                int damage = myMonsterCard.getAttack() - rivalMonsterCard.getDefence(true);
//                                attackDO(myAddressType, address, index, currentPlayer, myMonsterCard, rivalMonsterCard, damage);
//                                Attack.timeToEffectAfterAttack();
//                            } else System.out.println("Attack has been cancelled for effect of a card");
//                        } else {
//                            int damage = myMonsterCard.getAttack() - rivalMonsterCard.getDefence(false);
//                            attackDH(myAddressType, address, index, currentPlayer, myMonsterCard, rivalMonsterCard, damage);
//                            Attack.timeToEffectAfterAttack();
//                        }
//                    }
//                } else System.out.println("there is no card to attack here");
//            } else System.out.println("this card already attacked");
//        }
//        PhaseControl.getInstance().checkIfGameEnded();
//    }
//
//    private void attackOO(Address myAddress, Address address, int index, Player currentPlayer, MonsterCard myMonsterCard, MonsterCard rivalMonsterCard, int damage) {
//        if (damage == 0) {
//            removeForAttack(address, myAddress);
//            removeForAttack(myAddress, address);
//            System.out.println("both you and your opponent monster cards are destroyed and no one receives damage");
//        } else if (damage > 0) {
//            removeForAttack(address, myAddress);
//            decreaseLP(myAddress, address, index, Game.whoseRivalPlayer(), myMonsterCard, rivalMonsterCard, damage);
//            System.out.println("your opponent’s monster is destroyed and your opponent receives " + damage + " battle damage");
//        } else {
//            damage = (-1) * damage;
//            currentPlayer.decreaseLP(damage);
//            removeForAttack(myAddress, address);
//            System.out.println("Your monster card is destroyed and you received " + damage + " battle damage");
//        }
//    }
//
//    private void attackDO(Address myAddress, Address address, int index, Player currentPlayer, MonsterCard myMonsterCard, MonsterCard rivalMonsterCard, int damage) {
//        if (damage == 0) System.out.println("no card is destroyed");
//        else if (damage > 0) {
//            removeForAttack(address, myAddress);
//            System.out.println("the defense position monster is destroyed");
//        } else {
//            damage = (-1) * damage;
//            decreaseLP(myAddress, address, index, currentPlayer, myMonsterCard, rivalMonsterCard, damage);
//            System.out.println("no card is destroyed and you received " + damage + " battle damage");
//        }
//    }
//
//    private void attackDH(Address myAddress, Address address, int index, Player currentPlayer, MonsterCard myMonsterCard, MonsterCard rivalMonsterCard, int damage) {
//        if (damage == 0)
//            System.out.println("opponent’s monster card was " + rivalMonsterCard.getName() + " and no card is destroyed");
//        else if (damage > 0) {
//            removeForAttack(address, myAddress);
//            System.out.println("opponent’s monster card was " + rivalMonsterCard.getName() + " and " + "the defense position monster is destroyed");
//        } else {
//            damage = (-1) * damage;
//            decreaseLP(myAddress, address, index, currentPlayer, myMonsterCard, rivalMonsterCard, damage);
//            System.out.println("opponent’s monster card was " + rivalMonsterCard.getName() + " and " + "no card is destroyed and you received " + damage + " battle damage");
//        }
//        currentPlayer.setPositionOfCardInBoardByAddress(address, PositionOfCardInBoard.DO);
//    }
//
//    private void decreaseLP(Address myAddress, Address address, int index, Player player, MonsterCard myMonsterCard, MonsterCard rivalMonsterCard, int damage) {
//        if (!(Game.whoseTurnPlayer().getMonsterCardByAddress(address).getName().equals("ExploderDragon"))) {
//            player.decreaseLP(damage);
//        }
//    }
//
//    private void removeForAttack(Address address, Address theOtherAddress) {
//        Player currentPlayer = Game.whoseTurnPlayer();
//        if (currentPlayer.getMonsterCardByAddress(address).getName().equals("ExploderDragon"))
//            currentPlayer.removeCard(theOtherAddress);
//        if (!currentPlayer.getMonsterCardByAddress(address).getName().equals("Marshmallon"))
//            currentPlayer.removeCard(address);
//    }
//
//    private void flipSummon(Matcher matcher) {
//        System.out.println("you can’t do this action in this phase");
//    }
//
//    private void setPosition(Matcher matcher) {
//        System.out.println("you can’t do this action in this phase");
//    }
//
//    private void directAttack(Matcher matcher) {
//        PhaseControl.getInstance().checkIfGameEnded();
//        if (matcher.find()) {
//            Player currentPlayer = Game.whoseTurnPlayer();
//            Player rivalPlayer = Game.whoseRivalPlayer();
//            int index = currentPlayer.getIndexOfThisCardByAddress(new Address(matcher.group(1)));
//            if (currentPlayer.didWeAttackByThisCardInThisCardInThisTurn(index)) {
//                currentPlayer.setDidWeAttackByThisCardInThisCardInThisTurn(index);
//                //doubt should regard an error for being empty of board?
//                MonsterCard monsterCardForDirectAttack = currentPlayer.getMonsterCardByStringAddress(matcher.group(1));
//                rivalPlayer.decreaseLP(monsterCardForDirectAttack.getAttack());
//                System.out.println("you opponent receives " + monsterCardForDirectAttack.getAttack() + " battle damage");
//            } else System.out.println("this card already attacked");
//        }
//        PhaseControl.getInstance().checkIfGameEnded();
//    }
//
//    private void specialSummon(Matcher matcher) {
//
//    }
//
//    private void activeSpell(Matcher matcher) {
//        System.out.println("you can’t do this action in this phase");
//    }
//
//    private void activeTrap(Matcher matcher) {
//
//    }
//
//    private void ritualSummon(Matcher matcher) {
//
//    }

    private static Matcher getCommandMatcher(String input, String regex) {
        input = input.trim();
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(input);
    }

    public boolean getPermissionForTrap(String cardName, boolean isMine) {
        if (!isMine){
            System.out.print("Dear " + Game.whoseRivalPlayer().getNickName() + ",");
        }
        System.out.println("do you want to activate " + cardName + "trap?(yes/no)");
        return (Main.scanner.nextLine().equals("yes"));
    }
}
