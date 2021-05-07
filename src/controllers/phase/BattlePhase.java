package controllers.phase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import models.Address;
import models.Board;
import models.Player;
import models.card.monster.MonsterCard;
import view.Game;
import view.Main;

public class BattlePhase {
    Boolean goToNextPhase = false;

    public void run() {
        System.out.println("phase: draw phase");
        Board.showBoeard();
        String input;
        while (true) {
            StandByPhase.checkIfGameEnded();
            input = Main.scanner.nextLine().trim();
            if (input.matches("^[ ]*next phase[ ]*$"))
                break;
            else if (input.matches("^[ ]*select [.*][ ]*$"))
                whatIsSelected(input);
            else if (input.matches("^[ ]*summon[ ]*$"))
                System.out.println("no card is selected yet");
            else if (input.matches("^[ ]*set[ ]*$"))
                System.out.println("no card is selected yet");
            else if (input.matches("^[ ]*set -- position (attack|defense)[ ]*$"))
                System.out.println("no card is selected yet");
            else if (input.matches("^[ ]*flip-summon[ ]*$"))
                System.out.println("no card is selected yet");
            else if (input.matches("^[ ]*attack [\\d]+[ ]*$"))
                System.out.println("no card is selected yet");
            else if (input.matches("^[ ]*attack direct[ ]*$"))
                System.out.println("no card is selected yet");
            else if (input.matches("^[ ]*activate effect[ ]*$"))
                System.out.println("no card is selected yet");
            else if (input.matches("^[ ]*show graveyard[ ]*$"))
                showGraveyard();
            else if (input.matches("^[ ]*card show --selected[ ]*$"))
                System.out.println("no card is selected yet");
            else if (input.matches("^[ ]*surrender[ ]*$"))
                surrender();
            else
                System.out.println("invalid command");
        }
    }

    private void whatIsSelected(String input) {
        StandByPhase.checkIfGameEnded();
        if (input.matches("^[ ]*select --monster [\\d]+[ ]*$"))
            selectMonster(getCommandMatcher(input, "(^[ ]*select --monster ([\\d]+)[ ]*$)"));
        else if (input.matches("^[ ]*select --monster --opponent [\\d]+[ ]*$"))
            selectOpponentMonster(getCommandMatcher(input, "(^[ ]*select --monster --opponent ([\\d]+)[ ]*$)"));
        else if (input.matches("^[ ]*select --spell [\\d]+[ ]*$"))
            selectSpell(getCommandMatcher(input, "(^[ ]*select --spell ([\\d]+)[ ]*$)"));
        else if (input.matches("^[ ]*select --spell --opponent [\\d]+[ ]*$"))
            selectOpponentSpell(getCommandMatcher(input, "(^[ ]*select --spell --opponent ([\\d]+)[ ]*$)"));
        else if (input.matches("^[ ]*select --field[ ]*$"))
            selectField();
        else if (input.matches("^[ ]*select --field --opponent[ ]*$"))
            selectOpponentField();
        else if (input.matches("^[ ]*select --hand [\\d]+[ ]*$"))
            selectHand(getCommandMatcher(input, "(^[ ]*select --hand ([\\d]+)[ ]*$)"));
        else if (input.matches("^[ ]*select -d[ ]*$"))
            System.out.println("no card is selected yet");
        else
            System.out.println("invalid selection");
    }

    private void selectMonster(Matcher matcher) {
        if (matcher.find()) {
            Board.showBoeard();
            if (Address.isAddressValid(matcher.group(1))) {
                String selectedCard = matcher.group(1);
                String input;
                while (true) {
                    StandByPhase.checkIfGameEnded();
                    input = Main.scanner.nextLine().trim();
                    if (input.matches("^[ ]*next phase[ ]*$")) {
                        goToNextPhase = true;
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
                        setPosition(getCommandMatcher(selectedCard, "(^[ ]*select --monster ([\\d]+)[ ]*$)"));
                    else if (input.matches("^[ ]*flip-summon[ ]*$"))
                        flipSummon(getCommandMatcher(selectedCard, "(^[ ]*select --monster ([\\d]+)[ ]*$)"));
                    else if (input.matches("^[ ]*attack [\\d]+[ ]*$"))
                        attack(getCommandMatcher(input, "(^[ ]*attack ([\\d]+)[ ]*$)"));
                    else if (input.matches("^[ ]*attack direct[ ]*$"))
                        directAttack(getCommandMatcher(selectedCard, "(^[ ]*select --monster ([\\d]+)[ ]*$)"));
                    else if (input.matches("^[ ]*activate effect[ ]*$"))
                        System.out.println("activate effect is only for spell cards.");
                    else if (input.matches("^[ ]*show graveyard[ ]*$"))
                        showGraveyard();
                    else if (input.matches("^[ ]*card show --selected[ ]*$"))
                        showSelectedCard(getCommandMatcher(selectedCard, "(^[ ]*select --monster ([\\d]+)[ ]*$)"));
                    else if (input.matches("^[ ]*surrender[ ]*$"))
                        surrender();
                    else
                        System.out.println("invalid command");
                }
            } else
                System.out.println("invalid selection");
        }
    }

    private void selectOpponentMonster(Matcher matcher) {
        if (matcher.find()) {
            Board.showBoeard();
            if (Address.isAddressValid(matcher.group(1))) {
                String selectedCard = matcher.group(1);
                String input;
                while (true) {
                    StandByPhase.checkIfGameEnded();
                    input = Main.scanner.nextLine().trim();
                    if (input.matches("^[ ]*next phase[ ]*$")) {
                        goToNextPhase = true;
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
                        System.out.println("you can’t change this card position");
                    else if (input.matches("^[ ]*flip-summon[ ]*$"))
                        System.out.println("you can’t change this card position");
                    else if (input.matches("^[ ]*attack [\\d]+[ ]*$"))
                        System.out.println("you can’t attack with this card");
                    else if (input.matches("^[ ]*attack direct[ ]*$"))
                        System.out.println("you can’t attack with this card");
                    else if (input.matches("^[ ]*activate effect[ ]*$"))
                        System.out.println("activate effect is only for spell cards.");
                    else if (input.matches("^[ ]*show graveyard[ ]*$"))
                        showGraveyard();
                    else if (input.matches("^[ ]*card show --selected[ ]*$"))
                        showSelectedCard(getCommandMatcher(selectedCard, "(^[ ]*select --monster --opponent [\\d]+[ ]*$)"));
                    else if (input.matches("^[ ]*surrender[ ]*$"))
                        surrender();
                    else
                        System.out.println("invalid command");
                }
            } else
                System.out.println("invalid selection");
        }
    }

    private void selectSpell(Matcher matcher) {
        if (matcher.find()) {
            Board.showBoeard();
            if (Address.isAddressValid(matcher.group(1))) {
                String selectedCard = matcher.group(1);
                String input;
                while (true) {
                    StandByPhase.checkIfGameEnded();
                    input = Main.scanner.nextLine().trim();
                    if (input.matches("^[ ]*next phase[ ]*$")) {
                        goToNextPhase = true;
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
                        System.out.println("you can’t change this card position");
                    else if (input.matches("^[ ]*flip-summon[ ]*$"))
                        System.out.println("you can’t change this card position");
                    else if (input.matches("^[ ]*attack [\\d]+[ ]*$"))
                        System.out.println("you can’t attack with this card");
                    else if (input.matches("^[ ]*attack direct[ ]*$"))
                        System.out.println("you can’t attack with this card");
                    else if (input.matches("^[ ]*activate effect[ ]*$"))
                        activeSpell(getCommandMatcher(selectedCard, "(^[ ]*select --spell ([\\d]+)[ ]*$)"));
                    else if (input.matches("^[ ]*show graveyard[ ]*$"))
                        showGraveyard();
                    else if (input.matches("^[ ]*card show --selected[ ]*$"))
                        showSelectedCard(getCommandMatcher(selectedCard, "(^[ ]*select --spell ([\\d]+)[ ]*$)"));
                    else if (input.matches("^[ ]*surrender[ ]*$"))
                        surrender();
                    else
                        System.out.println("invalid command");
                }
            } else
                System.out.println("invalid selection");
        }
    }

    private void selectOpponentSpell(Matcher matcher) {
        if (matcher.find()) {
            if (Address.isAddressValid(matcher.group(1))) {
                String selectedCard = matcher.group(1);
                String input;
                while (true) {
                    StandByPhase.checkIfGameEnded();
                    Board.showBoeard();
                    input = Main.scanner.nextLine().trim();
                    if (input.matches("^[ ]*next phase[ ]*$")) {
                        goToNextPhase = true;
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
                        System.out.println("you can’t change this card position");
                    else if (input.matches("^[ ]*flip-summon[ ]*$"))
                        System.out.println("you can’t change this card position");
                    else if (input.matches("^[ ]*attack [\\d]+[ ]*$"))
                        System.out.println("you can’t attack with this card");
                    else if (input.matches("^[ ]*attack direct[ ]*$"))
                        System.out.println("you can’t attack with this card");
                    else if (input.matches("^[ ]*activate effect[ ]*$"))
                        System.out.println("activate effect is only for spell cards.");
                    else if (input.matches("^[ ]*show graveyard[ ]*$"))
                        showGraveyard();
                    else if (input.matches("^[ ]*card show --selected[ ]*$"))
                        showSelectedCard(getCommandMatcher(selectedCard, "(^[ ]*select --spell --opponent ([\\d]+)[ ]*$)"));
                    else if (input.matches("^[ ]*surrender[ ]*$"))
                        surrender();
                    else
                        System.out.println("invalid command");
                }
            } else
                System.out.println("invalid selection");
        }
    }

    private void selectField() {
        String input;
        while (true) {
            StandByPhase.checkIfGameEnded();
            Board.showBoeard();
            input = Main.scanner.nextLine().trim();
            if (input.matches("^[ ]*next phase[ ]*$")) {
                goToNextPhase = true;
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
                System.out.println("you can’t change this card position");
            else if (input.matches("^[ ]*flip-summon[ ]*$"))
                System.out.println("you can’t change this card position");
            else if (input.matches("^[ ]*attack [\\d]+[ ]*$"))
                System.out.println("you can’t attack with this card");
            else if (input.matches("^[ ]*attack direct[ ]*$"))
                System.out.println("you can’t attack with this card");
            else if (input.matches("^[ ]*activate effect[ ]*$"))
                System.out.println("activate effect is only for spell cards.");
            else if (input.matches("^[ ]*show graveyard[ ]*$"))
                showGraveyard();
            else if (input.matches("^[ ]*card show --selected[ ]*$"))
                showFieldZoneCard();
            else if (input.matches("^[ ]*surrender[ ]*$"))
                surrender();
            else
                System.out.println("invalid command");
        }
    }

    private void selectOpponentField() {
        String input;
        while (true) {
            StandByPhase.checkIfGameEnded();
            Board.showBoeard();
            input = Main.scanner.nextLine().trim();
            if (input.matches("^[ ]*next phase[ ]*$")) {
                goToNextPhase = true;
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
                System.out.println("you can’t change this card position");
            else if (input.matches("^[ ]*flip-summon[ ]*$"))
                System.out.println("you can’t change this card position");
            else if (input.matches("^[ ]*attack [\\d]+[ ]*$"))
                System.out.println("you can’t attack with this card");
            else if (input.matches("^[ ]*attack direct[ ]*$"))
                System.out.println("you can’t attack with this card");
            else if (input.matches("^[ ]*activate effect[ ]*$"))
                System.out.println("activate effect is only for spell cards.");
            else if (input.matches("^[ ]*show graveyard[ ]*$"))
                showGraveyard();
            else if (input.matches("^[ ]*card show --selected[ ]*$"))
                showOpponentFieldZoneCard();
            else if (input.matches("^[ ]*surrender[ ]*$"))
                surrender();
            else
                System.out.println("invalid command");
        }
    }

    private void selectHand(Matcher matcher) {
        if (matcher.find()) {
            Board.showBoeard();
            if (Address.isAddressValid(matcher.group(1))) {
                String selectedCard = matcher.group(1);
                String input;
                while (true) {
                    StandByPhase.checkIfGameEnded();
                    input = Main.scanner.nextLine().trim();
                    if (input.matches("^[ ]*next phase[ ]*$")) {
                        goToNextPhase = true;
                        break;
                    } else if (input.matches("^[ ]*select -d[ ]*$"))
                        break;
                    else if (input.matches("^[ ]*select [.*][ ]*$"))
                        System.out.println("");
                        //here
                    else if (input.matches("^[ ]*summon[ ]*$"))
                        summon(getCommandMatcher(selectedCard, "(^[ ]*select --hand [\\d]+[ ]*$)"));
                    else if (input.matches("^[ ]*set[ ]*$"))
                        System.out.println("you can’t do this action in this phase");
                    else if (input.matches("^[ ]*set -- position (attack|defense)[ ]*$"))
                        System.out.println("you can’t change this card position");
                    else if (input.matches("^[ ]*flip-summon[ ]*$"))
                        System.out.println("you can’t change this card position");
                    else if (input.matches("^[ ]*attack [\\d]+[ ]*$"))
                        System.out.println("you can’t attack with this card");
                    else if (input.matches("^[ ]*attack direct[ ]*$"))
                        System.out.println("you can’t attack with this card");
                    else if (input.matches("^[ ]*activate effect[ ]*$"))
                        System.out.println("activate effect is only for spell cards.");
                    else if (input.matches("^[ ]*show graveyard[ ]*$"))
                        showGraveyard();
                    else if (input.matches("^[ ]*card show --selected[ ]*$"))
                        showSelectedCard(getCommandMatcher(selectedCard, "(^[ ]*select --hand [\\d]+[ ]*$)"));
                    else if (input.matches("^[ ]*surrender[ ]*$"))
                        surrender();
                    else
                        System.out.println("invalid command");
                }
            } else
                System.out.println("invalid selection");
        }
    }

    private void summon(Matcher matcher) {

    }

    private void showSelectedCard(Matcher matcher) {

    }

    private void attack(Matcher matcher) {



        StandByPhase.checkIfGameEnded();
    }

    private void flipSummon(Matcher matcher) {

    }

    private void setPosition(Matcher matcher) {

    }

    private void showGraveyard() {

    }

    private void showFieldZoneCard() {

    }

    private void showOpponentFieldZoneCard() {

    }

    private void directAttack(Matcher matcher) {
        StandByPhase.checkIfGameEnded();
        if (matcher.find()) {
            Player currentPlayer = Game.whoseTurnPlayer();
            Player rivalPlayer=Game.whoseRivalPlayer();
            int index = currentPlayer.getIndexOfThisCardByAddress(matcher.group(1));
            if (currentPlayer.didWeAttackByThisCardInThisCardInThisTurn(index)) {
                currentPlayer.setDidWeAttackByThisCardInThisCardInThisTurn(index);
                //doubt should regard an error for being empty of board?
                MonsterCard monsterCardForDirectAttack = currentPlayer.getMonsterCardByStringAddress(matcher.group(1));
                rivalPlayer.decreaseLP(monsterCardForDirectAttack.getAttack());
                System.out.println("you opponent receives "+monsterCardForDirectAttack.getAttack()+" battle damage");
            } else System.out.println("this card already attacked");
        }
        StandByPhase.checkIfGameEnded();
    }

    private void specialSummon(Matcher matcher) {

    }

    private void activeSpell(Matcher matcher) {

    }

    private void activeTrap(Matcher matcher) {

    }

    private void ritualSummon(Matcher matcher) {

    }

    private void surrender() {

    }

    private static Matcher getCommandMatcher(String input, String regex) {
        input.trim();
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(input);
    }
}
