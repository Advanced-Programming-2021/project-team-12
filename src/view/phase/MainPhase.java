package view.phase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;

import controllers.PhaseControl;
import models.Address;
import models.Board;
import models.Card;
import models.Player;
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
            if (Game.isAITurn())
                aiRun();
            else {
                if (whatMainIsPhase == 1) {
                    Game.getMainPhase2().setHowManyHeraldOfCreationDidWeUseEffect(0);
                    PhaseControl.getInstance().doEffectMainPhase();
                }
                System.out.println("phase: main phase " + whatMainIsPhase);
                Board.showBoard();
                getSelectedCard();
            }
        }
    }

    private void playNextPhase() {
        if (whatMainIsPhase == 1)
            Game.playTurn("BattlePhase");
        else if (whatMainIsPhase == 2)
            Game.playTurn("EndPhase");
    }

    private void aiRun() {
        if (whatMainIsPhase == 1) {
            Game.getMainPhase2().setHowManyHeraldOfCreationDidWeUseEffect(0);
            PhaseControl.getInstance().doEffectMainPhase();
        }
        System.out.println("phase: main phase " + whatMainIsPhase);
        Board.showBoard();
        getSelectedCard();
    }

    public void getSelectedCard() {
        if (Game.isAITurn())
            getAISelectedCard();
        else {
            String input;
            while (true) {
                input = Main.scanner.nextLine().trim();
                try {
                    PhaseControl.getInstance().checkInputNonCardSelected(input);
                } catch (InvalidCommandException | InvalidCardSelection | NoSelectedCardException e) {
                    System.out.println(e.getMessage());
                } catch (BreakException e) {
                    playNextPhase();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void getAISelectedCard() {
        while (true) {
            String input = "";
            int place = 0;
            int maxAttack = 0;
            Player player = Game.whoseTurnPlayer();
            for (int i = 1; i <= 7; i++) {
                if (i == 7 && place != 0) {
                    input = "select --hand " + place;
                    try {
                        PhaseControl.getInstance().checkInputNonCardSelected(input);
                    } catch (InvalidCommandException | InvalidCardSelection | NoSelectedCardException e) {
                        System.out.println(e.getMessage());
                    } catch (BreakException e) {
                        playNextPhase();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (player.getHandCard().containsKey(i) && !player.getCardHand(i).getKind().equals("monster")) {
                    input = "select --hand " + i;
                    try {
                        PhaseControl.getInstance().checkInputNonCardSelected(input);
                    } catch (InvalidCommandException | InvalidCardSelection | NoSelectedCardException e) {
                        System.out.println(e.getMessage());
                    } catch (BreakException e) {
                        playNextPhase();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    if (player.getCardHand(i).getAttack() > maxAttack) {
                        place = i;
                        maxAttack = player.getCardHand(i).getAttack();
                    }
                }
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
                    playNextPhase();
                } catch (Exception e) {
                    e.printStackTrace();
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
                    playNextPhase();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void selectSpell(Matcher matcher) {
        if (Game.isAITurn())
            selectAISpell(matcher);
        else {
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
                        playNextPhase();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void selectAISpell(Matcher matcher) {
        if (matcher.find()) {
            String input = "activate effect";
            String selectedCard = matcher.group(1);
            int rand = new Random().nextInt();
            if (rand % 3 != 1) {
                while (true) {
                    try {
                        PhaseControl.getInstance().spellSelected(input, selectedCard);
                    } catch (CantAttack | CantChangeCardPosition | CantSummonThisCard | InvalidCommandException | CantSetThisCard e) {
                        System.out.println(e.getMessage());
                    } catch (BreakException e) {
                        playNextPhase();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
                    playNextPhase();
                } catch (Exception e) {
                    e.printStackTrace();
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
                playNextPhase();
            } catch (Exception e) {
                e.printStackTrace();
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
                playNextPhase();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void selectHand(Matcher matcher) {
        if (Game.isAITurn())
            selectAIHand(matcher);
        else {
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
                        playNextPhase();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void selectAIHand(Matcher matcher) {
        if (matcher.find()) {
            String input = "set";
            String selectedCard = matcher.group(1);
            int place = Integer.parseInt(matcher.group(2));
            Card card = Game.whoseTurnPlayer().getCardHand(place);
            if (card.getKind().equals("Monster"))
                input = "summon";
            while (true) {
                try {
                    PhaseControl.getInstance().handSelected(input, selectedCard);
                } catch (CantAttack | CantChangeCardPosition | CantActivateEffect | InvalidCommandException e) {

                } catch (BreakException e) {
                    playNextPhase();
                }
            }
        }
    }

    public void summon(Matcher matcher) {
        try {
            PhaseControl.getInstance().summonControl(matcher);
            if (!Game.isAITurn())
                System.out.println("summoned successfully");
        } catch (AlreadySummonedOrSet | MonsterZoneFull | CancelException | NotEnoughTribute | NoMonsterInThisAddress e) {
            if (!Game.isAITurn())
                System.out.println(e.getMessage());
        }
    }


    public void summonForTribute(int numberOfTributes, String address) throws CancelException, NotEnoughTribute, NoMonsterInThisAddress {
        if (!Game.isAITurn())
            System.out.println("select" + numberOfTributes + "monsters for tribute(write in different lines.)");
        if (numberOfTributes == 1) PhaseControl.getInstance().summonAMediumLevelMonster(address);
        else if (numberOfTributes == 2) PhaseControl.getInstance().summonAHighLevelMonster(address);
        else if (numberOfTributes == 3) PhaseControl.getInstance().summonASuperHighLevelMonster(address);
    }

    public void ritualSummon(String monsterCardAddress, int monsterLevel) {
        Address ritualSpellCardAddress = Game.whoseTurnPlayer().getOneOfRitualSpellCardAddress();
        if (ritualSpellCardAddress != null) {
            System.out.println("Please choose some monsters from your hand or on the board for tribute!" +
                    "(sum of the chosen monsters' level should be equal to level the monster you want to summon ritually)" +
                    "\nplease type them in different lines!");
            int sumOfLevel = 0;
            List<Address> monsterCardsAddress = new ArrayList<>();
            while (sumOfLevel < monsterLevel && Game.whoseTurnPlayer().canIContinueTribute(monsterLevel - sumOfLevel, monsterCardsAddress)) {
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
        for (Address cardsAddress : monsterCardsAddress) {
            Game.whoseTurnPlayer().removeCard(cardsAddress);
        }
    }

    public String scanForTribute(int i) {
        if (Game.isAITurn())
            return scanForAITribute(i);
        else {
            System.out.println("Please select a monsters for tribute!(type monster address or cancel)");
            String tributeCard = Main.scanner.nextLine();
            while (!(tributeCard.matches("[12345]{1}"))) {
                System.out.println("invalid command!");
                tributeCard = Main.scanner.nextLine();
            }
            return tributeCard;
        }
    }

    private String scanForAITribute(int number) {
        Player player = Game.whoseTurnPlayer();
        int minAttack = 1000000;
        int place1 = 0;
        int place2 = 0;
        int place3 = 0;
        for (int i = 1; i < 6; i++) {
            if (player.getMonsterZoneCard().containsKey(i) && player.getCardMonster(i).getAttack() < minAttack) {
                minAttack = player.getCardMonster(i).getAttack();
                place1 = i;
            }
        }
        minAttack = 1000000;
        for (int i = 1; i < 6; i++) {
            if (player.getMonsterZoneCard().containsKey(i) && player.getCardMonster(i).getAttack() < minAttack && i != place1) {
                minAttack = player.getCardMonster(i).getAttack();
                place2 = i;
            }
        }
        minAttack = 1000000;
        for (int i = 1; i < 6; i++) {
            if (player.getMonsterZoneCard().containsKey(i) && player.getCardMonster(i).getAttack() < minAttack && i != place1 && i != place2) {
                minAttack = player.getCardMonster(i).getAttack();
                place3 = i;
            }
        }
        if (number == 1)
            return String.valueOf(place1);
        if (number == 2)
            return String.valueOf(place2);
        else
            return String.valueOf(place3);
    }


    public boolean isCancelled(String input) {
        return input.equals("cancel");
    }

    public String getTributeCard() {
        if (Game.isAITurn())
            return getAITributeCard();
        else {
            System.out.println("Please select a monster for tribute!(type monster address or cancel)");
            String tributeCard = Main.scanner.nextLine();
            while (!(tributeCard.matches("[12345]") || tributeCard.matches("cancel"))) {
                System.out.println("invalid command!");
                tributeCard = Main.scanner.nextLine();
            }
            return tributeCard;
        }
    }

    private String getAITributeCard() {
        Player player = Game.whoseTurnPlayer();
        int minAttack = 1000;
        int place = 0;
        for (int i = 1; i < 6; i++) {
            if (player.getMonsterZoneCard().containsKey(i) && player.getCardMonster(i).getAttack() < minAttack) {
                minAttack = player.getCardMonster(i).getAttack();
                place = i;
            }
        }
        return String.valueOf(place);
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
            if (!Game.isAITurn())
                System.out.println("set successfully");
        } catch (SpellZoneFull e) {
            if (!Game.isAITurn())
                System.out.println(e.getMessage());
        }
    }

    public void setSpell(Matcher matcher) {
        try {
            PhaseControl.getInstance().spellSet(matcher);
            if (!Game.isAITurn())
                System.out.println("set successfully");
        } catch (SpellZoneFull e) {
            if (!Game.isAITurn())
                System.out.println(e.getMessage());
        }
    }

    public void showSelectedCard(Matcher matcher) {
        matcher.find();
        Address address = new Address(matcher.group(1));
        if (address.checkIsMine())
            PhaseControl.getInstance().showSelectedCard(address);
        else {
            try {
                PhaseControl.getInstance().showOpponentCard(address);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
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

    public void activeSpell(Matcher matcher) {
        try {
            PhaseControl.getInstance().activeSpell(matcher);
            if (!Game.isAITurn())
                System.out.println("spell activated");
        } catch (YouAlreadyActivatedThisCard | PreperationsAreNotDoneYet | SpellZoneFull e) {
            if (!Game.isAITurn())
                System.out.println(e.getMessage());
        }
    }

    public void increaseHowManyHeraldOfCreationDidWeUseEffect() {
        howManyHeraldOfCreationDidWeUseEffect++;
    }

    public void setHowManyHeraldOfCreationDidWeUseEffect(int i) {
        howManyHeraldOfCreationDidWeUseEffect = i;
    }

    public void setWhatMainIsPhase(int i) {
        whatMainIsPhase = i;
    }

    public void summonCyberse() {
        if (permission()) {
            System.out.println("Please select one Cyberse type monster from your hand or deck or graveyard to be summoned.");
            System.out.println("which card do you want to special summon?\n1.Texchanger\n2.Leotron");
            String monsterName = Main.scanner.nextLine();
            Game.whoseTurnPlayer().specialSummonThisKindOfCardFromHandOrDeckOrGraveyard(monsterName);
        }
    }

    public boolean permission() {
        System.out.println("Do you want do the effect?(yes/no)");
        return Main.scanner.nextLine().equals("yes");
    }

    public static void doMindCrushEffect() {
        Player currentPlayer = Game.whoseTurnPlayer();
        Player rivalPlayer = Game.whoseRivalPlayer();
        System.out.println("type a card name so if rival has this kind of card all of them will be removed else one of your card will be removed randomly.");
        String cardName = Main.scanner.nextLine();
        if (rivalPlayer.doIHaveCardWithThisNameInMyHand(cardName)) {
            rivalPlayer.removeAllCardWithThisNameInMyHand(cardName);
        } else currentPlayer.removeOneOfHandCard();
    }

    public static void summonAMonsterCardFromGraveyard() {
        System.out.println("whose graveyard you want to summon from?(yours/rival's)");
        Board.showGraveyard();
        doCallOfTheHauntedEffect(Main.scanner.nextLine().equals("yours"));
    }


    public static void doCallOfTheHauntedEffect(boolean isMine) {
        if (isMine) System.out.println("choose a monster from your graveyard to be summoned!(only type number)");
        else System.out.println("choose a monster from your rival's graveyard to be summoned!(only type number)");
        Board.showGraveyard();
        Address address = new Address(Integer.parseInt(Main.scanner.nextLine()), "graveyard", isMine);
        if (Game.whoseTurnPlayer().getMonsterCardByAddress(address) != null)
            Game.whoseTurnPlayer().summonThisCardFromGraveYardToMonsterZone(address);
    }

    public static boolean removeCardFromMyHand() {
        System.out.println("choose card from your to be remove!(only type number)");
        Address address = new Address(Integer.parseInt(Main.scanner.nextLine()), "hand", true);
        if (Game.whoseTurnPlayer().getCardByAddress(address) != null) {
            Game.whoseTurnPlayer().removeCard(address);
            return true;
        }
        return false;
    }

    public static boolean doSolemnWarningEffect(Address address) {
        if (!Game.isAITurn()) {
            if (BattlePhase.getInstance().getPermissionForTrap("Solemn Warning", false)) {
                Game.whoseRivalPlayer().decreaseLP(2000);
                Game.whoseTurnPlayer().removeCard(address);
                return true;
            }
            return false;
        } else {
            Player currentPlayer = Game.whoseTurnPlayer();
            MonsterCard monsterCard = currentPlayer.getMonsterCardByAddress(address);
            return ((monsterCard.getNormalAttack() >= 2000) && (currentPlayer.getLP() > 5000));
        }
    }


}
