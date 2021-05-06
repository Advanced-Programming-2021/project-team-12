package controllers.phase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import models.Address;
import models.Board;
import models.Player;
import models.card.monster.MonsterCard;
import models.card.spell.SpellCard;
import models.card.trap.TrapCard;
import view.Game;
import view.Main;

public class MainPhase1 {
    Boolean goToNextPhase = false;

    public void run() {
        System.out.println("phase: draw phase");
        Board.showBoeard();
        String input;
        while (true) {
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
                    input = Main.scanner.nextLine().trim();
                    if (input.matches("^[ ]*next phase[ ]*$")) {
                        goToNextPhase = true;
                        break;
                    } else if (input.matches("^[ ]*select -d[ ]*$"))
                        break;
                    else if (input.matches("^[ ]*summon[ ]*$"))
                        System.out.println("you can’t summon this card");
                    else if (input.matches("^[ ]*set[ ]*$"))
                        System.out.println("you can’t set this card");
                    else if (input.matches("^[ ]*set -- position (attack|defense)[ ]*$"))
                        setPosition(input, getCommandMatcher(selectedCard, "(^[ ]*select --monster ([\\d]+)[ ]*$)"));
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
                    input = Main.scanner.nextLine().trim();
                    if (input.matches("^[ ]*next phase[ ]*$")) {
                        goToNextPhase = true;
                        break;
                    } else if (input.matches("^[ ]*select -d[ ]*$"))
                        break;
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
                    input = Main.scanner.nextLine().trim();
                    if (input.matches("^[ ]*next phase[ ]*$")) {
                        goToNextPhase = true;
                        break;
                    } else if (input.matches("^[ ]*select -d[ ]*$"))
                        break;
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
                    Board.showBoeard();
                    input = Main.scanner.nextLine().trim();
                    if (input.matches("^[ ]*next phase[ ]*$")) {
                        goToNextPhase = true;
                        break;
                    } else if (input.matches("^[ ]*select -d[ ]*$"))
                        break;
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
            Board.showBoeard();
            input = Main.scanner.nextLine().trim();
            if (input.matches("^[ ]*next phase[ ]*$")) {
                goToNextPhase = true;
                break;
            } else if (input.matches("^[ ]*select -d[ ]*$"))
                break;
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
            Board.showBoeard();
            input = Main.scanner.nextLine().trim();
            if (input.matches("^[ ]*next phase[ ]*$")) {
                goToNextPhase = true;
                break;
            } else if (input.matches("^[ ]*select -d[ ]*$"))
                break;
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
                    input = Main.scanner.nextLine().trim();
                    if (input.matches("^[ ]*next phase[ ]*$")) {
                        goToNextPhase = true;
                        break;
                    } else if (input.matches("^[ ]*select -d[ ]*$"))
                        break;
                    else if (input.matches("^[ ]*summon[ ]*$"))
                        summon(getCommandMatcher(selectedCard, "(^[ ]*select --hand [\\d]+[ ]*$)"));
                    else if (input.matches("^[ ]*set[ ]*$"))
                        whatIsSet(getCommandMatcher(selectedCard, "(^[ ]*select --hand ([\\d]+)[ ]*$)"));
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
        if (matcher.find()) {
            if (!Game.whoseTurnPlayer().isMonsterZoneFull()) {
                if (!Game.whoseTurnPlayer().isHeSummonedOrSet()) {
                    int level = Game.whoseTurnPlayer().getMonsterCardByStringAddress(matcher.group(1)).getLevel();
                    if (level <= 4) {
                        Game.whoseTurnPlayer().summonCardFromHandToMonsterZone(matcher.group(1));
                    } else if (level <= 6) {
                        summonAMediumLevelMonster(matcher.group(1));
                    } else {
                        summonAHighLevelMonster(matcher.group(1));
                    }
                } else {
                    System.out.println("you already summoned/set on this turn");
                }
            } else {
                System.out.println("monster card zone is full");
            }
        }
    }

    private void summonAHighLevelMonster(String address) {
        if (Game.whoseTurnPlayer().isThereTwoCardInMonsterZone()) {
            System.out.println("Please select two monster for tribute!(type monster address or cancel)");
            String tributeCard1 = Main.scanner.nextLine();
            while (!(tributeCard1.matches("[\\d+]") || tributeCard1.matches("cancel"))) {
                System.out.println("invalid command!");
                tributeCard1 = Main.scanner.nextLine();
            }
            if (tributeCard1.equals("cancel")) {
                System.out.println("canceled successfully");
            } else {
                String tributeCard2 = Main.scanner.nextLine();
                while (!(tributeCard1.matches("[\\d+]"))) {
                    System.out.println("invalid command!");
                    tributeCard1 = Main.scanner.nextLine();
                }
                if (Game.whoseTurnPlayer().isMonsterInThisMonsterZoneTypeAddress(Integer.parseInt(tributeCard1))
                        && Game.whoseTurnPlayer().isMonsterInThisMonsterZoneTypeAddress(Integer.parseInt(tributeCard2))) {
                    System.out.println("summoned successfully");
                    Game.whoseTurnPlayer().removeThisMonsterZoneTypeAddressForTribute(Integer.parseInt(tributeCard1));
                    Game.whoseTurnPlayer().removeThisMonsterZoneTypeAddressForTribute(Integer.parseInt(tributeCard2));
                    Game.whoseTurnPlayer().summonCardFromHandToMonsterZone(address);
                } else System.out.println("there no monsters on one of this addresses");
            }
        } else System.out.println("there are not enough cards for tribute");
    }

    private void summonAMediumLevelMonster(String address) {
        if (Game.whoseTurnPlayer().isThereAnyCardInMonsterZone()) {
            System.out.println("Please select two monster for tribute!(type monster address or cancel)");
            String tributeCard = Main.scanner.nextLine();
            while (!(tributeCard.matches("[\\d+]") || tributeCard.matches("cancel"))) {
                System.out.println("invalid command!");
                tributeCard = Main.scanner.nextLine();
            }
            if (tributeCard.equals("cancel")) {
                System.out.println("canceled successfully");
            } else {
                if (Game.whoseTurnPlayer().isMonsterInThisMonsterZoneTypeAddress(Integer.parseInt(tributeCard))) {
                    System.out.println("summoned successfully");
                    Game.whoseTurnPlayer().removeThisMonsterZoneTypeAddressForTribute(Integer.parseInt(tributeCard));
                    Game.whoseTurnPlayer().summonCardFromHandToMonsterZone(address);
                } else System.out.println("there no monsters one this address");
            }
        } else System.out.println("there are not enough cards for tribute");
    }

    private void whatIsSet(Matcher matcher) {
        if (matcher.find()) {
            String whatKind = Game.whoseTurnPlayer().whatKindaCardIsInThisAddress(matcher.group(1));
            if (whatKind.equals("monster")) {
                setMonster(getCommandMatcher(matcher.group(1), "(^[ ]*select --hand [\\d]+[ ]*$)"));
            } else if (whatKind.equals("trap")) {
                setTrap(getCommandMatcher(matcher.group(1), "(^[ ]*select --hand [\\d]+[ ]*$)"));
            } else if (whatKind.equals("spell")) {
                setSpell(getCommandMatcher(matcher.group(1), "(^[ ]*select --hand [\\d]+[ ]*$)"));
            }
        }
    }

    private void setMonster(Matcher matcher) {
        if (matcher.find()) {
            if (!Game.whoseTurnPlayer().isMonsterZoneFull()) {
                if (!Game.whoseTurnPlayer().isHeSummonedOrSet()) {
                    Game.whoseTurnPlayer().setCardFromHandToMonsterZone(matcher.group(1));
                    System.out.println("set successfully");
                } else {
                    System.out.println("you already summoned/set on this turn");
                }
            } else {
                System.out.println("monster card zone is full");
            }
        }
    }

    private void setTrap(Matcher matcher) {
        if (matcher.find()) {
            if (Game.whoseTurnPlayer().isSpellZoneFull()) {
                Game.whoseTurnPlayer().setCardFromHandToSpellZone(matcher.group(1));
            } else System.out.println("spell card zone is full");
        }
    }

    private void setSpell(Matcher matcher) {
        if (matcher.find()) {
            if (Game.whoseTurnPlayer().isSpellZoneFull()) {
                Game.whoseTurnPlayer().setCardFromHandToSpellZone(matcher.group(1));
            } else System.out.println("spell card zone is full");
        }
    }

    // be careful for duplicate
    private void showSelectedCard(Matcher matcher) {
        if (matcher.find()) {
            String kind = Game.whoseTurnPlayer().whatKindaCardIsInThisAddress(matcher.group(1));
            if (kind.equals("monster")) {
                MonsterCard monsterCardForShow = Game.whoseTurnPlayer().getMonsterCardByStringAddress(matcher.group(1));
                System.out.println("Name: " + monsterCardForShow.getName());
                System.out.println("Level: " + monsterCardForShow.getLevel());
                System.out.println("Type: " + monsterCardForShow.getMonsterMode());
                System.out.println("ATK: " + monsterCardForShow.getNormalAttack());
                System.out.println("DEF: " + monsterCardForShow.getNormalDefence());
                System.out.println("Description: " + monsterCardForShow.getDescription());
            } else if (kind.equals("spell")) {
                SpellCard spellCardForShow = Game.whoseTurnPlayer().getSpellCardByStringAddress(matcher.group(1));
                System.out.println("Name: " + spellCardForShow.getName());
                System.out.println("Spell");
                System.out.println("Type: " + spellCardForShow.getSpellMode());
                System.out.println("Description: " + spellCardForShow.getDescription());
            } else {
                TrapCard trapCardForShow = Game.whoseTurnPlayer().getTrapCardByStringAddress(matcher.group(1));
                System.out.println("Name: " + trapCardForShow.getName());
                System.out.println("Trap");
                System.out.println("Type: Normal");
                System.out.println("Description: " + trapCardForShow.getDescription());
            }
        }

    }

    private void attack(Matcher matcher) {

    }

    private void flipSummon(Matcher matcher) {
        if (matcher.find()) {
            if (Game.whoseTurnPlayer().isThisMonsterOnDHPosition(matcher.group(1))) {
                Game.whoseTurnPlayer().convertThisMonsterFromDHToOO(matcher.group(1));
                System.out.println("flip summoned successfully");
            } else System.out.println("you can’t do this action in this phase");
        }
    }

    private void setPosition(String input, Matcher matcher) {
        Player currentPlayer=Game.whoseTurnPlayer();
        if (matcher.find()) {
            int index = currentPlayer.getIndexOfThisCardByAddress(matcher.group(1));
            Matcher matcher1 = getCommandMatcher(input, "^[ ]*set -- position (attack|defense)[ ]*$");
            if (matcher1.find()) {
                if (matcher1.group(1).equals("attack")) {
                    if (currentPlayer.isThisMonsterOnAttackPosition(matcher.group(1))) {
                        if(currentPlayer.didWeChangePositionThisCardInThisTurn(index)){
                            currentPlayer.setDidWeChangePositionThisCardInThisTurn(index);
                            currentPlayer.convertThisMonsterFromAttackToDefence(matcher.group(1));
                        }
                    } else System.out.println("this card is already in the wanted position");
                } else {
                    if (!currentPlayer.isThisMonsterOnAttackPosition(matcher.group(1))) {
                        if(currentPlayer.didWeChangePositionThisCardInThisTurn(index)){
                            currentPlayer.setDidWeChangePositionThisCardInThisTurn(index);
                            currentPlayer.convertThisMonsterFromDefenceToAttack(matcher.group(1));
                        }
                    } else System.out.println("this card is already in the wanted position");
                }
            }
        }
    }

    private void showGraveyard() {
    Board.showGraveyard();
    }

    private void showFieldZoneCard() {
    Game.whoseTurnPlayer().showFieldZoneCard();
    }

    private void showOpponentFieldZoneCard() {
        Game.whoseTurnPlayer().showOpponentFieldZoneCard();
    }

    private void directAttack(Matcher matcher) {
        System.out.println("you can’t do this action in this phase");
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
