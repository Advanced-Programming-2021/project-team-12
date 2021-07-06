package view.phase;

import java.util.*;
import java.util.regex.Matcher;

import controllers.PhaseControl;
import models.*;
import models.card.monster.MonsterCard;
import models.card.spell.SpellCard;
import models.card.trap.TrapCard;
import controllers.Game;
import Exceptions.*;
import view.GameView;
import view.Main;

public class MainPhase {
    public Boolean goToNextPhase = false;
    public int whatMainIsPhase;

    public void run() throws Exception {
        if (!goToNextPhase) {
            if (Game.isAITurn())
                aiRun();
            else {
                if (whatMainIsPhase == 1) {
                    Game.getGameView().setHowManyHeraldOfCreationDidWeUseEffect(0);
                    PhaseControl.getInstance().doEffectMainPhase();
                }
                System.out.println("phase: main phase " + whatMainIsPhase);
                Board.showBoard();
                getSelectedCard();
            }
            playNextPhase();
        }
    }

    private void playNextPhase() throws Exception {
        if (whatMainIsPhase == 1)
            Game.playTurn("BattlePhase");
        else if (whatMainIsPhase == 2)
            Game.playTurn("EndPhase");
    }

    private void aiRun() throws Exception {
        System.out.println("phase: main phase " + whatMainIsPhase);
        Board.showBoard();
        if (whatMainIsPhase == 1) {
            Game.getGameView().setHowManyHeraldOfCreationDidWeUseEffect(0);
            PhaseControl.getInstance().doEffectMainPhase();
            getSelectedCard();
        } else if (whatMainIsPhase == 2)
            getAISelectedCard2();
    }

    private void getAISelectedCard2() throws Exception {
        Player player = Game.whoseTurnPlayer();
        for (int i = 1; i <= 5; i++) {
            if (player.getSpellZoneCard().containsKey(i)) {
                Address address = new Address(i, "spell", true);
                try {
                    PhaseControl.getInstance().activeSpell(address);
                } catch (MyException e) {
                    System.out.println(e.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        playNextPhase();
    }

    public void getSelectedCard() throws Exception {
        if (Game.isAITurn())
            getAISelectedCard();
        else {
            String input;
            while (true) {
                input = Main.scanner.nextLine().trim();
//                try {
//                    PhaseControl.getInstance().checkInputNonCardSelected(input);
//                } catch (MyException e) {
//                    System.out.println(e.getMessage());
//                } catch (BreakException e) {
//                    playNextPhase();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        }
    }

    private void getAISelectedCard() throws Exception {
        String input = "";
        ArrayList<Integer> monsters = new ArrayList<>();
        Player player = Game.whoseTurnPlayer();
        for (int i = 1; i <= 6; i++) {
            if (player.getHandCard().containsKey(i) && !player.getCardHand(i).getKind().equals("Monster")) {
                Address address = new Address(i, "hand", true);
                try {
                    PhaseControl.getInstance().whatIsSet(address);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (player.getHandCard().containsKey(i))
                monsters.add(i);
        }
        if (monsters.size() >= 2) {
            Collections.sort(monsters, new Comparator<Integer>() {
                public int compare(Integer i1, Integer i2) {
                    Card c1 = player.getCardHand(i1);
                    Card c2 = player.getCardHand(i2);
                    if (c1.getAttack() != c2.getAttack())
                        return (c1.getAttack() > c2.getAttack()) ? -1 : 1;
                    else
                        return c1.getCardName().compareTo(c2.getCardName());
                }
            });
            for (int i = 0; i < monsters.size(); i++) {
                Address address = new Address(monsters.get(i), "hand", true);
                try {
                    PhaseControl.getInstance().summonControl(address);
                } catch (MyException e) {
                    System.out.println(e.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        playNextPhase();
    }

    public void selectMonster(Matcher matcher) {
        if (matcher.find()) {
            Board.showBoard();
            String selectedCard = matcher.group(1);
            String input;
            while (true) {
                input = Main.scanner.nextLine().trim();
//                try {
//                    PhaseControl.getInstance().monsterCardSelected(input, selectedCard);
//                } catch (MyException e) {
//                    System.out.println(e.getMessage());
//                } catch (BreakException e) {
//                    playNextPhase();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
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
//                try {
//                    PhaseControl.getInstance().OpponentMonsterCardSelected(input, selectedCard);
//                } catch (MyException e) {
//                    System.out.println(e.getMessage());
//                } catch (BreakException e) {
//                    playNextPhase();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
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
//                    input = Main.scanner.nextLine().trim();
//                    try {
//                        PhaseControl.getInstance().spellSelected(input, selectedCard);
//                    } catch (MyException e) {
//                        System.out.println(e.getMessage());
//                    } catch (BreakException e) {
//                        playNextPhase();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                }
            }
        }
    }

    private void selectAISpell(Matcher matcher) {
        if (matcher.find()) {
            String input = "activate effect";
            String selectedCard = matcher.group(1);
            int rand = new Random().nextInt(4);
            if (rand != 1) {
//                try {
//                    PhaseControl.getInstance().spellSelected(input, selectedCard);
//                } catch (MyException e) {
//                    System.out.println(e.getMessage());
//                } catch (BreakException e) {
//                    playNextPhase();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
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
//                try {
//                    PhaseControl.getInstance().OpponentSpellCardSelected(input, selectedCard);
//                } catch (MyException e) {
//                    System.out.println(e.getMessage());
//                } catch (BreakException e) {
//                    playNextPhase();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        }
    }

    public void selectField() {
        String input;
        while (true) {
            Board.showBoard();
            input = Main.scanner.nextLine().trim();
//            try {
//                PhaseControl.getInstance().fieldCardSelected(input);
//            } catch (MyException e) {
//                System.out.println(e.getMessage());
//            } catch (BreakException e) {
//                playNextPhase();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }
    }

    public void selectOpponentField() {
        String input;
        while (true) {
            Board.showBoard();
            input = Main.scanner.nextLine().trim();
//            try {
//                PhaseControl.getInstance().opponentFieldCardSelected(input);
//            } catch (MyException e) {
//                System.out.println(e.getMessage());
//            } catch (BreakException e) {
//                playNextPhase();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
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
//                    try {
//                        PhaseControl.getInstance().handSelected(input, selectedCard);
//                    } catch (MyException e) {
//                        System.out.println(e.getMessage());
//                    } catch (BreakException e) {
//                        playNextPhase();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
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
//            try {
//                PhaseControl.getInstance().handSelected(input, selectedCard);
//            } catch (MyException e) {
//                System.out.println(e.getMessage());
//            } catch (BreakException e) {
//                playNextPhase();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }
    }

    public void summon(Address address) {
        try {
            PhaseControl.getInstance().summonControl(address);
            if (!Game.isAITurn())
                System.out.println("summoned successfully");
        } catch (MyException e) {
            if (!Game.isAITurn())
                System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ritualSummon(Address monsterCardAddress, int monsterLevel) {
        Address ritualSpellCardAddress = Game.whoseTurnPlayer().getOneOfRitualSpellCardAddress();
        if (ritualSpellCardAddress != null) {
            if (!Game.isAITurn())
                System.out.println("Please choose some monsters from your hand or on the board for tribute!" +
                    "(sum of the chosen monsters' level should be equal to level the monster you want to summon ritually)" +
                    "\nplease type them in different lines!");
            int sumOfLevel = 0;
            List<Address> monsterCardsAddress = new ArrayList<>();
            if (Game.isAITurn())
                monsterCardsAddress = selectAIRitual(monsterCardsAddress,sumOfLevel, monsterLevel);
            else while (sumOfLevel < monsterLevel && Game.whoseTurnPlayer().canIContinueTribute(monsterLevel - sumOfLevel, monsterCardsAddress)) {
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
            } else if (!Game.isAITurn()) System.out.println("You chose the wrong cards.");
        }
    }

    private List<Address> selectAIRitual(List<Address> monsterCardsAddress, int sumOfLevel, int monsterLevel) {
        for (int i = 1; i <= 5; i++) {
            Address address = new Address(i, "monster", true);
            if (Game.whoseTurnPlayer().getCardByAddress(address) == null)
                continue;
            monsterCardsAddress.add(address);
            MonsterCard monsterCard1 = Board.whatKindaMonsterIsHere(address);
            sumOfLevel += monsterCard1.getLevel();
            if (!(sumOfLevel < monsterLevel && Game.whoseTurnPlayer().canIContinueTribute(monsterLevel - sumOfLevel, monsterCardsAddress))) {
                monsterCardsAddress.remove(address);
                sumOfLevel -= monsterCard1.getLevel();
            }
        }
        return monsterCardsAddress;
    }

    private void tributeThisCards(List<Address> monsterCardsAddress) {
        for (Address cardsAddress : monsterCardsAddress) {
            Game.whoseTurnPlayer().removeCard(cardsAddress);
        }
    }

    public boolean isCancelled(String input) {
        return input.equals("cancel");
    }

//    public String getTributeCard() {
//        if (Game.isAITurn())
//            return getAITributeCard();
//        else {
//            System.out.println("Please select a monster for tribute!(type monster address or cancel)");
//            String tributeCard = Main.scanner.nextLine();
//            while (!(tributeCard.matches("[12345]") || tributeCard.matches("cancel"))) {
//                System.out.println("invalid command!");
//                tributeCard = Main.scanner.nextLine();
//            }
//            return tributeCard;
//        }
//    }
//
//    private String getAITributeCard() {
//        Player player = Game.whoseTurnPlayer();
//        int minAttack = 10000;
//        int place = 0;
//        for (int i = 1; i < 6; i++) {
//            if (player.getMonsterZoneCard().containsKey(i) && player.getCardMonster(i).getAttack() < minAttack) {
//                minAttack = player.getCardMonster(i).getAttack();
//                place = i;
//            }
//        }
//        return String.valueOf(place);
//    }

    public void setMonster(Address address) {
        try {
            PhaseControl.getInstance().monsterSet(address);
            System.out.println("set successfully");
        } catch (MyException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTrap(Address address) {
        try {
            PhaseControl.getInstance().trapSet(address);
            if (!Game.isAITurn())
                System.out.println("set successfully");
        } catch (MyException e) {
            if (!Game.isAITurn())
                System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSpell(Address address) {
        try {
            PhaseControl.getInstance().spellSet(address);
            if (!Game.isAITurn())
                System.out.println("set successfully");
        } catch (MyException e) {
            if (!Game.isAITurn())
                System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
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
        System.out.println("Name: " + trapCardForShow.getRealName());
        System.out.println("Trap");
        System.out.println("Type: Normal");
        System.out.println("Description: " + trapCardForShow.getDescription());
    }

    public void printSpellAttributes(SpellCard spellCardForShow) {
        System.out.println("Name: " + spellCardForShow.getRealName());
        System.out.println("Spell");
        System.out.println("Type: " + spellCardForShow.getSpellMode());
        System.out.println("Description: " + spellCardForShow.getDescription());
    }

    public void printMonsterAttributes(MonsterCard monsterCardForShow) {
        System.out.println("Name: " + monsterCardForShow.getRealName());
        System.out.println("Level: " + monsterCardForShow.getLevel());
        System.out.println("Type: " + monsterCardForShow.getMonsterMode());
        System.out.println("ATK: " + monsterCardForShow.getNormalAttack());
        System.out.println("DEF: " + monsterCardForShow.getNormalDefence());
        System.out.println("Description: " + monsterCardForShow.getDescription());
    }

    public void attack(Matcher matcher) {
        System.out.println("you can’t do this action in this phase");
    }

    public void flipSummon(Address address) {
        try {
            PhaseControl.getInstance().flipSummon(address);
            System.out.println("flip summoned successfully");
        } catch (MyException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPosition(String input, Address address) {
        try {
            PhaseControl.getInstance().setPosition(input, address);
            System.out.println("monster card position changed successfully");
        } catch (MyException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showFieldZoneCard() {
//        Board.showFieldZoneCard(true);
    }

    public void showOpponentFieldZoneCard() {
//        Board.showFieldZoneCard(false);
    }

    public void directAttack(Matcher matcher) {
        System.out.println("you can’t do this action in this phase");
    }

    public void activeSpell(Address address) {
        try {
            PhaseControl.getInstance().activeSpell(address);
            if (!Game.isAITurn())
                System.out.println("spell activated");
        } catch (MyException e) {
            if (!Game.isAITurn())
                System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setWhatMainIsPhase(int i) {
        whatMainIsPhase = i;
    }
}
