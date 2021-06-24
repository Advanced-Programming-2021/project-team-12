package view.phase;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import controllers.PhaseControl;
import models.Address;
import models.Board;
import models.card.monster.MonsterCard;
import models.card.spell.SpellCard;
import models.card.trap.TrapCard;
import controllers.Game;
import Exceptions.*;
import view.Main;

public class MainPhase {
    public Boolean goToNextPhase = false;
    public int howManyHeraldOfCreationDidWeUseEffect = 0;
    public int whatMainIsPhase;

    public void run() {
        if (!goToNextPhase) {
            if (whatMainIsPhase == 1) {
                Game.getMainPhase2().setHowManyHeraldOfCreationDidWeUseEffect(0);
                PhaseControl.getInstance().doEffectMainPhase();
            }
            System.out.println("phase: draw phase");
            Board.showBoard();
            getSelectedCard();
        }
    }

    public void getSelectedCard() {
        String input;
        while (true) {
            input = Main.scanner.nextLine().trim();
            try {
                PhaseControl.getInstance().checkInputNonCardSelected(input);
            } catch (InvalidCommandException | InvalidCardSelection | NoSelectedCardException e) {
                System.out.println(e.getMessage());
            } catch (BreakException e) {
                break;
            }
        }
    }

    public void selectMonster(Matcher matcher) {
        if (matcher.find()) {
            Board.showBoard();
            String selectedCard = matcher.group(1);
            String input;
            while (true) {
                input = Main.scanner.nextLine().trim();
                try {
                    PhaseControl.getInstance().monsterCardSelected(input, selectedCard);
                } catch (CantSetThisCard | CantSummonThisCard | CantActivateEffect | InvalidCommandException e) {
                    System.out.println(e.getMessage());
                } catch (BreakException e) {
                    break;
                }
            }
        }
    }

    public void selectOpponentMonster(Matcher matcher) {
        if (matcher.find()) {
            Board.showBoard();
            String selectedCard = matcher.group(1);
            String input;
            while (true) {
                input = Main.scanner.nextLine().trim();
                try {
                    PhaseControl.getInstance().OpponentMonsterCardSelected(input, selectedCard);
                } catch (CantAttack | CantChangeCardPosition | CantSummonThisCard | CantActivateEffect | InvalidCommandException | CantSetThisCard e) {
                    System.out.println(e.getMessage());
                } catch (BreakException e) {
                    break;
                }
            }
        }
    }

    public void selectSpell(Matcher matcher) {
        if (matcher.find()) {
            Board.showBoard();
            String selectedCard = matcher.group(1);
            String input;
            while (true) {
                input = Main.scanner.nextLine().trim();
                try {
                    PhaseControl.getInstance().spellSelected(input, selectedCard);
                } catch (CantAttack | CantChangeCardPosition | CantSummonThisCard | InvalidCommandException | CantSetThisCard e) {
                    System.out.println(e.getMessage());
                } catch (BreakException e) {
                    break;
                }
            }
        }
    }

    public void selectOpponentSpell(Matcher matcher) {
        if (matcher.find()) {
            String selectedCard = matcher.group(1);
            String input;
            while (true) {
                Board.showBoard();
                input = Main.scanner.nextLine().trim();
                try {
                    PhaseControl.getInstance().OpponentSpellCardSelected(input, selectedCard);
                } catch (CantAttack | CantChangeCardPosition | CantSummonThisCard | CantActivateEffect | InvalidCommandException | CantSetThisCard e) {
                    System.out.println(e.getMessage());
                } catch (BreakException e) {
                    break;
                }
            }
        }
    }

    public void selectField() {
        String input;
        while (true) {
            Board.showBoard();
            input = Main.scanner.nextLine().trim();
            try {
                PhaseControl.getInstance().fieldCardSelected(input);
            } catch (CantAttack | CantChangeCardPosition | CantSummonThisCard | CantActivateEffect | InvalidCommandException | CantSetThisCard e) {
                System.out.println(e.getMessage());
            } catch (BreakException e) {
                break;
            }
        }
    }

    public void selectOpponentField() {
        String input;
        while (true) {
            Board.showBoard();
            input = Main.scanner.nextLine().trim();
            try {
                PhaseControl.getInstance().opponentFieldCardSelected(input);
            } catch (CantAttack | CantChangeCardPosition | CantSummonThisCard | CantActivateEffect | InvalidCommandException | CantSetThisCard e) {
                System.out.println(e.getMessage());
            } catch (BreakException e) {
                break;
            }
        }
    }

    public void selectHand(Matcher matcher) {
        if (matcher.find()) {
            Board.showBoard();
            String selectedCard = matcher.group(1);
            String input;
            while (true) {
                input = Main.scanner.nextLine().trim();
                try {
                    PhaseControl.getInstance().handSelected(input, selectedCard);
                } catch (CantAttack | CantChangeCardPosition | CantActivateEffect | InvalidCommandException e) {
                    System.out.println(e.getMessage());
                } catch (BreakException e) {
                    break;
                }
            }
        }
    }

    public void summon(Matcher matcher) {
        try {
            PhaseControl.getInstance().summonControl(matcher);
            System.out.println("summoned successfully");
        } catch (AlreadySummonedOrSet | MonsterZoneFull | CancelException | NotEnoughTribute | NoMonsterInThisAddress e) {
            System.out.println(e.getMessage());
        }
    }


    public void summonForTribute(int numberOfTributes, String address) throws CancelException, NotEnoughTribute, NoMonsterInThisAddress {
        System.out.println("select" + numberOfTributes + "monsters for tribute(write in different lines.)");
        if (numberOfTributes == 1) PhaseControl.getInstance().summonAMediumLevelMonster(address);
        else if (numberOfTributes == 2) PhaseControl.getInstance().summonAHighLevelMonster(address);
        else if (numberOfTributes == 3) PhaseControl.getInstance().summonASuperHighLevelMonster(address);
    }

    public void ritualSummon(String monsterCardAddress, int monsterLevel) {
        Address ritualSpellCardAddress = getOneOfRitualSpellCardAddress();
        if (ritualSpellCardAddress != null) {
            System.out.println("Please choose some monsters from your hand or on the board for tribute!" +
                    "(sum of the chosen monsters' level should be equal to level the monster you want to summon ritually)" +
                    "\nplease type them in different lines!");
            int sumOfLevel = 0;
            List<Address> monsterCardsAddress = new ArrayList<>();
            while (sumOfLevel < monsterLevel && canHeContinueTribute(monsterLevel - sumOfLevel, monsterCardsAddress)) {
                Address address = new Address(Main.scanner.nextLine());
                MonsterCard monsterCard1 = Board.whatKindaMonsterIsHere(address);
                monsterCardsAddress.add(address);
                sumOfLevel += monsterCard1.getLevel();
            }
            if (sumOfLevel == monsterLevel) {
                tributeThisCards(monsterCardsAddress);
                for (Address cardsAddress : monsterCardsAddress) Game.whoseTurnPlayer().removeCard(cardsAddress);
                Game.whoseTurnPlayer().removeCard(ritualSpellCardAddress);
                Game.whoseTurnPlayer().summonCardToMonsterZone(monsterCardAddress);
            } else System.out.println("You chose the wrong cards.");
        }
    }

    private void tributeThisCards(List<Address> monsterCardsAddress) {

    }

    private boolean canHeContinueTribute(int i, List<Address> monsterCardsAddress) {

    }

    private Address getOneOfRitualSpellCardAddress() {
    }

    public String scanForTribute() {
        System.out.println("Please select a monsters for tribute!(type monster address or cancel)");
        String tributeCard = Main.scanner.nextLine();
        while (!(tributeCard.matches("[12345]{1}"))) {
            System.out.println("invalid command!");
            tributeCard = Main.scanner.nextLine();
        }
        return tributeCard;
    }

    public boolean isCancelled(String input) {
        return input.equals("cancel");
    }

    public String getTributeCard() {
        System.out.println("Please select two monster for tribute!(type monster address or cancel)");
        String tributeCard = Main.scanner.nextLine();
        while (!(tributeCard.matches("[\\d+]") || tributeCard.matches("cancel"))) {
            System.out.println("invalid command!");
            tributeCard = Main.scanner.nextLine();
        }
        return tributeCard;
    }

    public void setMonster(Matcher matcher) {
        try {
            PhaseControl.getInstance().monsterSet(matcher);
            System.out.println("set successfully");
        } catch (AlreadySummonedOrSet | MonsterZoneFull e) {
            System.out.println(e.getMessage());
        }
    }

    public void setTrap(Matcher matcher) {
        try {
            PhaseControl.getInstance().trapSet(matcher);
            System.out.println("set successfully");
        } catch (SpellZoneFull e) {
            System.out.println(e.getMessage());
        }
    }

    public void setSpell(Matcher matcher) {
        try {
            PhaseControl.getInstance().spellSet(matcher);
            System.out.println("set successfully");
        } catch (RitualSummonException e) {
            checkRitualSpell(matcher);
        } catch (SpellZoneFull | CantRitualSummon e) {
            System.out.println(e.getMessage());
        }
    }

    private void checkRitualSpell(Matcher matcher) {
        if (matcher.find()) {
            System.out.println("Choose a ritual card from hand which has the condition!");
            String input = Main.scanner.nextLine();
            while (!(input.matches("^[ ]*select --hand [\\d]+[ ]*$"))) {
                System.out.println("invalid command!");
                input = Main.scanner.nextLine();
            }
            setTheRitualSpell(input, matcher.group(1));
        }
    }

    public void setTheRitualSpell(String input, String address) {
        //TODO inshaallah
    }

    public void showSelectedCard(Matcher matcher) {
        PhaseControl.getInstance().showSelectedCard(matcher);
    }

    public void printTrapAttributes(TrapCard trapCardForShow) {
        System.out.println("Name: " + trapCardForShow.getName());
        System.out.println("Trap");
        System.out.println("Type: Normal");
        System.out.println("Description: " + trapCardForShow.getDescription());
    }

    public void printSpellAttributes(SpellCard spellCardForShow) {
        System.out.println("Name: " + spellCardForShow.getName());
        System.out.println("Spell");
        System.out.println("Type: " + spellCardForShow.getSpellMode());
        System.out.println("Description: " + spellCardForShow.getDescription());
    }

    public void printMonsterAttributes(MonsterCard monsterCardForShow) {
        System.out.println("Name: " + monsterCardForShow.getName());
        System.out.println("Level: " + monsterCardForShow.getLevel());
        System.out.println("Type: " + monsterCardForShow.getMonsterMode());
        System.out.println("ATK: " + monsterCardForShow.getNormalAttack());
        System.out.println("DEF: " + monsterCardForShow.getNormalDefence());
        System.out.println("Description: " + monsterCardForShow.getDescription());
    }

    public void attack(Matcher matcher) {
        System.out.println("you can’t do this action in this phase");
    }

    public void flipSummon(Matcher matcher) {
        try {
            PhaseControl.getInstance().flipSummon(matcher);
            System.out.println("flip summoned successfully");
        } catch (CantDoInThisPhase e) {
            System.out.println(e.getMessage());
        }
    }

    public void setPosition(String input, Matcher matcher) {
        try {
            PhaseControl.getInstance().setPosition(input, matcher);
            System.out.println("monster card position changed successfully");
        } catch (ThisCardIsInWantedPosition | YouAlreadyChangedThisCardPosition e) {
            System.out.println(e.getMessage());
        }
    }

    public void showFieldZoneCard() {
        Board.showFieldZoneCard(true);
    }

    public void showOpponentFieldZoneCard() {
        Board.showFieldZoneCard(false);
    }

    public void directAttack(Matcher matcher) {
        System.out.println("you can’t do this action in this phase");
    }

    public void specialSummon(Matcher matcher) {
        //TODO inshaallah
    }

    public void activeSpell(Matcher matcher) {
        try {
            PhaseControl.getInstance().activeSpell(matcher);
            System.out.println("spell activated");
        } catch (YouAlreadyActivatedThisCard | PreperationsAreNotDoneYet | SpellZoneFull e) {
            System.out.println(e.getMessage());
        }
    }

    public void increaseHowManyHeraldOfCreationDidWeUseEffect() {
        howManyHeraldOfCreationDidWeUseEffect++;
    }

    private void setHowManyHeraldOfCreationDidWeUseEffect(int i) {
        howManyHeraldOfCreationDidWeUseEffect = i;
    }

    public void setWhatMainIsPhase(int i) {
        whatMainIsPhase = i;
    }

    public void summonCyberse() {
        if(permission()){
            System.out.println("Please select one Cyberse type monster from your hand or deck or graveyard to be summoned.");
            System.out.println("which card do you want to special summon?\n1.Texchanger\n2.Leotron");
            String monsterName = Main.scanner.nextLine();
            Game.whoseTurnPlayer().specialSummonThisKindOfCardFromHandOrDeckOrGraveyard(monsterName);
        }
    }

    public boolean permission(){
        System.out.println("Do you want do the effect?(yes/no)");
        return Main.scanner.nextLine().equals("yes");
    }
}
