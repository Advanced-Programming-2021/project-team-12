package controllers.phase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import controllers.PhaseControl;
import controllers.move.Attack;
import models.Address;
import models.Board;
import models.Player;
import models.card.monster.MonsterCard;
import models.card.monster.monster_effect.DestroyOneMonsterWhenBecameFacedUp;
import models.card.spell.SpellCard;
import models.card.spell.SpellMode;
import models.card.trap.TrapCard;
import view.Effect;
import controllers.Game;
import Exceptions.*;
import view.Main;

public class MainPhase {
    public Boolean goToNextPhase = false;
    private int howManyHeraldOfCreationDidWeUseEffect = 0;
    private int whatMainIsPhase;

    public void run() {
        if (!goToNextPhase) {
            if (whatMainIsPhase == 1) {
                Game.getMainPhase2().setHowManyHeraldOfCreationDidWeUseEffect(0);
                doEffect();
            }
            System.out.println("phase: draw phase");
            Board.showBoeard();
            getSelectedCard();
        }
    }

    public void getSelectedCard() {
        String input;
        while (true) {
            input = Main.scanner.nextLine().trim();
            try {
                try {
                    try {
                        try {
                            PhaseControl.getInstance().checkInputNonCardSelected(input);
                        } catch (InvalidCardSelection e) {
                            System.out.println(e.getMessage());
                        }
                    } catch (BreakException e) {
                        break;
                    }
                } catch (NoSelectedCardException e) {
                    System.out.println(e.getMessage());
                }
            } catch (InvalidCommandException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void selectMonster(Matcher matcher) {
        if (matcher.find()) {
            Board.showBoeard();
            String selectedCard = matcher.group(1);
            String input;
            while (true) {
                input = Main.scanner.nextLine().trim();
                try {
                    try {
                        try {
                            try {
                                try {
                                    PhaseControl.getInstance().monsterCardSelected(input, selectedCard);
                                } catch (CantActivateEffect e) {
                                    System.out.println(e.getMessage());
                                }
                            } catch (CantSetThisCard e) {
                                System.out.println(e.getMessage());
                            }
                        } catch (CantSummonThisCard e) {
                            System.out.println(e.getMessage());
                        }
                    } catch (BreakException e) {
                        break;
                    }
                } catch (InvalidCommandException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public void selectOpponentMonster(Matcher matcher) {
        if (matcher.find()) {
            Board.showBoeard();
            String selectedCard = matcher.group(1);
            String input;
            while (true) {
                input = Main.scanner.nextLine().trim();
                try {
                    try {
                        try {
                            try {
                                try {
                                    try {
                                        try {
                                            PhaseControl.getInstance().OpponentMonstercardSelected(input, selectedCard);
                                        } catch (CantChangeCardPosition e) {
                                            System.out.println(e.getMessage());
                                        }
                                    } catch (CantAttack e) {
                                        System.out.println(e.getMessage());
                                    }
                                } catch (CantActivateEffect e) {
                                    System.out.println(e.getMessage());
                                }
                            } catch (CantSetThisCard e) {
                                System.out.println(e.getMessage());
                            }
                        } catch (CantSummonThisCard e) {
                            System.out.println(e.getMessage());
                        }
                    } catch (BreakException e) {
                        break;
                    }
                } catch (InvalidCommandException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public void selectSpell(Matcher matcher) {
        if (matcher.find()) {
            Board.showBoeard();
            String selectedCard = matcher.group(1);
            String input;
            while (true) {
                input = Main.scanner.nextLine().trim();
                try {
                    try {
                        try {
                            try {
                                try {
                                    try {
                                        PhaseControl.getInstance().spellSelected(input, selectedCard);
                                    } catch (CantChangeCardPosition e) {
                                        System.out.println(e.getMessage());
                                    }
                                } catch (CantAttack e) {
                                    System.out.println(e.getMessage());
                                }
                            } catch (CantSetThisCard e) {
                                System.out.println(e.getMessage());
                            }
                        } catch (CantSummonThisCard e) {
                            System.out.println(e.getMessage());
                        }
                    } catch (BreakException e) {
                        break;
                    }
                } catch (InvalidCommandException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public void selectOpponentSpell(Matcher matcher) {
        if (matcher.find()) {
            String selectedCard = matcher.group(1);
            String input;
            while (true) {
                Board.showBoeard();
                input = Main.scanner.nextLine().trim();
                try {
                    try {
                        try {
                            try {
                                try {
                                    try {
                                        try {
                                            PhaseControl.getInstance().OpponentSpellCardSelected(input, selectedCard);
                                        } catch (CantChangeCardPosition e) {
                                            System.out.println(e.getMessage());
                                        }
                                    } catch (CantAttack e) {
                                        System.out.println(e.getMessage());
                                    }
                                } catch (CantActivateEffect e) {
                                    System.out.println(e.getMessage());
                                }
                            } catch (CantSetThisCard e) {
                                System.out.println(e.getMessage());
                            }
                        } catch (CantSummonThisCard e) {
                            System.out.println(e.getMessage());
                        }
                    } catch (BreakException e) {
                        break;
                    }
                } catch (InvalidCommandException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public void selectField() {
        String input;
        while (true) {
            Board.showBoeard();
            input = Main.scanner.nextLine().trim();
            try {
                try {
                    try {
                        try {
                            try {
                                try {
                                    try {
                                        PhaseControl.getInstance().fieldCardSelected(input);
                                    } catch (CantChangeCardPosition e) {
                                        System.out.println(e.getMessage());
                                    }
                                } catch (CantAttack e) {
                                    System.out.println(e.getMessage());
                                }
                            } catch (CantActivateEffect e) {
                                System.out.println(e.getMessage());
                            }
                        } catch (CantSetThisCard e) {
                            System.out.println(e.getMessage());
                        }
                    } catch (CantSummonThisCard e) {
                        System.out.println(e.getMessage());
                    }
                } catch (BreakException e) {
                    break;
                }
            } catch (InvalidCommandException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void selectOpponentField() {
        String input;
        while (true) {
            Board.showBoeard();
            input = Main.scanner.nextLine().trim();
            try {
                try {
                    try {
                        try {
                            try {
                                try {
                                    try {
                                        PhaseControl.getInstance().opponentFieldCardSelected(input);
                                    } catch (CantChangeCardPosition e) {
                                        System.out.println(e.getMessage());
                                    }
                                } catch (CantAttack e) {
                                    System.out.println(e.getMessage());
                                }
                            } catch (CantActivateEffect e) {
                                System.out.println(e.getMessage());
                            }
                        } catch (CantSetThisCard e) {
                            System.out.println(e.getMessage());
                        }
                    } catch (CantSummonThisCard e) {
                        System.out.println(e.getMessage());
                    }
                } catch (BreakException e) {
                    break;
                }
            } catch (InvalidCommandException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void selectHand(Matcher matcher) {
        if (matcher.find()) {
            Board.showBoeard();
            String selectedCard = matcher.group(1);
            String input;
            while (true) {
                input = Main.scanner.nextLine().trim();
                try {
                    try {
                        try {
                            try {
                                try {
                                    PhaseControl.getInstance().handSelected(input, selectedCard);
                                } catch (CantChangeCardPosition e) {
                                    System.out.println(e.getMessage());
                                }
                            } catch (CantAttack e) {
                                System.out.println(e.getMessage());
                            }
                        } catch (CantActivateEffect e) {
                            System.out.println(e.getMessage());
                        }
                    } catch (BreakException e) {
                        break;
                    }
                } catch (InvalidCommandException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public void summon(Matcher matcher) {
        if (matcher.find()) {
            Player currentPlayer = Game.whoseTurnPlayer();
            if (!currentPlayer.isMonsterZoneFull()) {
                if (!currentPlayer.isHeSummonedOrSet()) {
                    if (!currentPlayer.getMonsterCardByStringAddress(matcher.group(1)).isRitual()) {
                        Address address = new Address(matcher.group(1));
                        int level = Game.whoseTurnPlayer().getMonsterCardByStringAddress(matcher.group(1)).getLevel();
                        if (level <= 4) {
                            currentPlayer.setHeSummonedOrSet(true);
                            if (currentPlayer.getMonsterCardByAddress(address).getName().equals("Scanner")) {
                                currentPlayer.summonCardFromHandToMonsterZone(matcher.group(1)).setIsScanner(true);
                            } else currentPlayer.summonCardFromHandToMonsterZone(matcher.group(1));
                            if (currentPlayer.getMonsterCardByAddress(address).getName().equals("Terratiger")) {
                                if (Integer.parseInt(Effect.run("Terratiger")) != 0) {
                                    Address address1 = new Address(Integer.parseInt(Effect.run("Terratiger")), "hand", true);
                                    if ((currentPlayer.getMonsterCardByAddress(address1) != null)
                                            && (currentPlayer.getMonsterCardByAddress(address1).getLevel() <= 4)
                                            && (!currentPlayer.isMonsterZoneFull()))
                                        currentPlayer.setCardFromHandToMonsterZone(address1);
                                }
                            }
                            System.out.println("summoned successfully");
                            if (Game.whoseRivalPlayer().doIHaveTrapHoleTrapOnTheBoard()) {
                                currentPlayer.removeCard(address);
                                Game.whoseRivalPlayer().removeOneOfMyTrapHoleTrapOnTheBoard();
                                System.out.println("The summmoned card got destroyed by effect of Trap Hole efffect.");
                            }
                            if (Game.whoseRivalPlayer().doIHaveTorrentialTributeTrapOnTheBoard()) {
                                Attack.destroyAllMonstersInTheBoard();
                                Game.whoseRivalPlayer().removeOneOfMyTorrentialTributeTrapOnTheBoard();
                                System.out.println("All monster card got destroyed by effect of TorrentialTribute efffect.");
                            }
                        } else if (level <= 6) {
                            summonAMediumLevelMonster(matcher.group(1));
                        } else {
                            int index = Game.whoseTurnPlayer().getIndexOfThisCardByAddress(address);
                            if (Game.whoseTurnPlayer().getMonsterCardByAddress(address).getName().equals("BeastKingBarbaros")
                                    && (Integer.parseInt(Effect.run("BeastKingBarbaros")) == 3)) {
                                Game.whoseTurnPlayer().setDidBeastKingBarbarosSummonedSuperHighLevel(true, index);
                                summonASuperHighLevelMonster(matcher.group(1));
                            } else summonAHighLevelMonster(matcher.group(1));
                        }
                    } else ritualSummon(matcher);
                } else System.out.println("you already summoned/set on this turn");
            } else System.out.println("monster card zone is full");
        }
    }

    public void ritualSummon(Matcher matcher) {

    }

    public void summonAHighLevelMonster(String address) {
        Address address1 = new Address(address);
        Player currentPlayer = Game.whoseTurnPlayer();
        if (Game.whoseTurnPlayer().isThereTwoCardInMonsterZone()) {
            System.out.println("Please select two monsters for tribute!(type monster address or cancel and type monsters in different lines)");
            String tributeCard1 = scanForTribute();
            if (tributeCard1.equals("cancel")) {
                System.out.println("canceled successfully");
            } else {
                String tributeCard2 = scanForTribute();
                if (Game.whoseTurnPlayer().isMonsterInThisMonsterZoneTypeAddress(Integer.parseInt(tributeCard1))
                        && Game.whoseTurnPlayer().isMonsterInThisMonsterZoneTypeAddress(Integer.parseInt(tributeCard2))) {
                    System.out.println("summoned successfully");
                    Game.whoseTurnPlayer().setHeSummonedOrSet(true);
                    Game.whoseTurnPlayer().removeThisMonsterZoneTypeAddressForTribute(Integer.parseInt(tributeCard1));
                    Game.whoseTurnPlayer().removeThisMonsterZoneTypeAddressForTribute(Integer.parseInt(tributeCard2));
                    Game.whoseTurnPlayer().summonCardFromHandToMonsterZone(address);
                    if (Game.whoseRivalPlayer().doIHaveTrapHoleTrapOnTheBoard()) {
                        currentPlayer.removeCard(address1);
                        Game.whoseRivalPlayer().removeOneOfMyTrapHoleTrapOnTheBoard();
                        System.out.println("The summmoned card got destroyed by effect of Trap Hole efffect.");
                    }
                    if (Game.whoseRivalPlayer().doIHaveTorrentialTributeTrapOnTheBoard()) {
                        Attack.destroyAllMonstersInTheBoard();
                        Game.whoseRivalPlayer().removeOneOfMyTorrentialTributeTrapOnTheBoard();
                        System.out.println("All monster card got destroyed by effect of TorrentialTribute efffect.");
                    }
                } else System.out.println("there no monsters on one of this addresses");
            }
        } else System.out.println("there are not enough cards for tribute");
    }

    public void summonASuperHighLevelMonster(String address) {
        Address address1 = new Address(address);
        Player currentPlayer = Game.whoseTurnPlayer();
        if (Game.whoseTurnPlayer().isThereThreeCardInMonsterZone()) {
            System.out.println("Please select three monsters for tribute!(type monster address or cancel and type monsters in different lines)");
            String tributeCard1 = scanForTribute();
            if (isCancelled(tributeCard1)) {
                System.out.println("canceled successfully");
            } else {
                String tributeCard2 = scanForTribute();
                if (isCancelled(tributeCard2)) {
                    System.out.println("canceled successfully");
                } else {
                    String tributeCard3 = scanForTribute();
                    if (Game.whoseTurnPlayer().isMonsterInThisMonsterZoneTypeAddress(Integer.parseInt(tributeCard1))
                            && Game.whoseTurnPlayer().isMonsterInThisMonsterZoneTypeAddress(Integer.parseInt(tributeCard2))
                            && Game.whoseTurnPlayer().isMonsterInThisMonsterZoneTypeAddress(Integer.parseInt(tributeCard3))) {
                        System.out.println("summoned successfully");
                        Game.whoseTurnPlayer().setHeSummonedOrSet(true);
                        Game.whoseTurnPlayer().removeThisMonsterZoneTypeAddressForTribute(Integer.parseInt(tributeCard1));
                        Game.whoseTurnPlayer().removeThisMonsterZoneTypeAddressForTribute(Integer.parseInt(tributeCard2));
                        Game.whoseTurnPlayer().removeThisMonsterZoneTypeAddressForTribute(Integer.parseInt(tributeCard3));
                        Game.whoseTurnPlayer().summonCardFromHandToMonsterZone(address);
                        if (Game.whoseRivalPlayer().doIHaveTrapHoleTrapOnTheBoard()) {
                            currentPlayer.removeCard(address1);
                            Game.whoseRivalPlayer().removeOneOfMyTrapHoleTrapOnTheBoard();
                            System.out.println("The summmoned card got destroyed by effect of Trap Hole efffect.");
                        }
                        if (Game.whoseRivalPlayer().doIHaveTorrentialTributeTrapOnTheBoard()) {
                            Attack.destroyAllMonstersInTheBoard();
                            Game.whoseRivalPlayer().removeOneOfMyTorrentialTributeTrapOnTheBoard();
                            System.out.println("All monster card got destroyed by effect of TorrentialTribute efffect.");
                        }
                    } else System.out.println("there no monsters on one of this addresses");
                }
            }
        } else System.out.println("there are not enough cards for tribute");
    }

    public String scanForTribute() {
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

    public void summonAMediumLevelMonster(String address) {
        Address address1 = new Address(address);
        Player currentPlayer = Game.whoseTurnPlayer();
        if (currentPlayer.isThereAnyCardInMonsterZone()) {
            System.out.println("Please select two monster for tribute!(type monster address or cancel)");
            String tributeCard = Main.scanner.nextLine();
            while (!(tributeCard.matches("[\\d+]") || tributeCard.matches("cancel"))) {
                System.out.println("invalid command!");
                tributeCard = Main.scanner.nextLine();
            }
            if (tributeCard.equals("cancel")) {
                System.out.println("canceled successfully");
            } else {
                if (currentPlayer.isMonsterInThisMonsterZoneTypeAddress(Integer.parseInt(tributeCard))) {
                    System.out.println("summoned successfully");
                    currentPlayer.setHeSummonedOrSet(true);
                    currentPlayer.removeThisMonsterZoneTypeAddressForTribute(Integer.parseInt(tributeCard));
                    currentPlayer.summonCardFromHandToMonsterZone(address);
                    if (Game.whoseRivalPlayer().doIHaveTrapHoleTrapOnTheBoard()) {
                        currentPlayer.removeCard(address1);
                        Game.whoseRivalPlayer().removeOneOfMyTrapHoleTrapOnTheBoard();
                        System.out.println("The summmoned card got destroyed by effect of Trap Hole efffect.");
                    }
                    if (Game.whoseRivalPlayer().doIHaveTorrentialTributeTrapOnTheBoard()) {
                        Attack.destroyAllMonstersInTheBoard();
                        Game.whoseRivalPlayer().removeOneOfMyTorrentialTributeTrapOnTheBoard();
                        System.out.println("All monster card got destroyed by effect of TorrentialTribute efffect.");
                    }
                } else System.out.println("there no monsters one this address");
            }
        } else System.out.println("there are not enough cards for tribute");
    }

    public void setMonster(Matcher matcher) {
        if (matcher.find()) {
            if (!Game.whoseTurnPlayer().isMonsterZoneFull()) {
                if (!Game.whoseTurnPlayer().isHeSummonedOrSet()) {
                    Address address = new Address(matcher.group(1));
                    if (Game.whoseTurnPlayer().getMonsterCardByAddress(address).getName().equals("Scanner")) {
                        Game.whoseTurnPlayer().setCardFromHandToMonsterZone(address).setIsScanner(true);
                    } else Game.whoseTurnPlayer().setCardFromHandToMonsterZone(address);
                    Game.whoseTurnPlayer().setHeSummonedOrSet(true);
                    System.out.println("set successfully");
                } else {
                    System.out.println("you already summoned/set on this turn");
                }
            } else {
                System.out.println("monster card zone is full");
            }
        }
    }

    public void setTrap(Matcher matcher) {
        if (matcher.find()) {
            if (Game.whoseTurnPlayer().isSpellZoneFull()) {
                Game.whoseTurnPlayer().setCardFromHandToSpellZone(matcher.group(1));
                Game.whoseTurnPlayer().setHeSummonedOrSet(true);
                System.out.println("set successfully");
            } else System.out.println("spell card zone is full");
        }
    }

    public void setSpell(Matcher matcher) {
        if (matcher.find()) {
            Player currentPlayer = Game.whoseTurnPlayer();
            if (currentPlayer.isSpellZoneFull()) {
                if (currentPlayer.getSpellCardByStringAddress(matcher.group(1)).getSpellMode() != SpellMode.RITUAL) {
                    currentPlayer.setCardFromHandToSpellZone(matcher.group(1));
                    currentPlayer.setHeSummonedOrSet(true);
                    System.out.println("set successfully");
                } else {
                    if (currentPlayer.isThereAnyRitualTypeMonsterInOurHand()
                            && currentPlayer.isOneOfLevelOfRitualMonstersInTheHandIsEqualToSumOfLevelOfSubsetOfMonsterZone()) {
                        System.out.println("Choose a ritual card from hand which has the condition!");
                        String input = Main.scanner.nextLine();
                        while (!(input.matches("^[ ]*select --hand [\\d]+[ ]*$"))) {
                            System.out.println("invalid command!");
                            input = Main.scanner.nextLine();
                        }
                        setTheRitualSpell(input, matcher.group(1));
                    } else System.out.println("there is no way you could ritual summon a monster");
                }
            } else System.out.println("spell card zone is full");
        }
    }

    public void setTheRitualSpell(String input, String address) {

    }

    public void showSelectedCard(Matcher matcher) {
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

    public void attack(Matcher matcher) {
        System.out.println("you can’t do this action in this phase");
    }

    public void flipSummon(Matcher matcher) {
        if (matcher.find()) {
            Player currentPlayer = Game.whoseTurnPlayer();
            if (currentPlayer.isThisMonsterOnDHPosition(matcher.group(1))) {
                currentPlayer.convertThisMonsterFromDHToOO(matcher.group(1));
                Address address = new Address(matcher.group(1));
                if (currentPlayer.getMonsterCardByAddress(address).getName().equals("ManEaterBug")) {
                    doManEaterBugEffect();
                }
                System.out.println("flip summoned successfully");
                if (Game.whoseRivalPlayer().doIHaveTrapHoleTrapOnTheBoard()) {
                    currentPlayer.removeCard(address);
                    Game.whoseRivalPlayer().removeOneOfMyTrapHoleTrapOnTheBoard();
                    System.out.println("The summmoned card got destroyed by effect of Trap Hole efffect.");
                }
            } else System.out.println("you can’t do this action in this phase");
        }
    }

    public void doManEaterBugEffect() {
        int monsterZoneNumber = Integer.parseInt(Effect.run("ManEaterBug"));
        if (monsterZoneNumber <= 5 && monsterZoneNumber >= 1) {
            Address address1 = new Address(monsterZoneNumber, "monster", false);
            if (!Board.isAddressEmpty(address1)) {
                Attack.destroyThisAddress(address1);
            }
        }
    }

    public void setPosition(String input, Matcher matcher) {
        Player currentPlayer = Game.whoseTurnPlayer();
        if (matcher.find()) {
            int index = currentPlayer.getIndexOfThisCardByAddress(new Address(matcher.group(1)));
            Matcher matcher1 = getCommandMatcher(input, "^[ ]*set -- position (attack|defense)[ ]*$");
            if (matcher1.find()) {
                if (matcher1.group(1).equals("attack")) {
                    if (currentPlayer.isThisMonsterOnAttackPosition(matcher.group(1))) {
                        if (currentPlayer.didWeChangePositionThisCardInThisTurn(index)) {
                            currentPlayer.setDidWeChangePositionThisCardInThisTurn(index);
                            currentPlayer.convertThisMonsterFromAttackToDefence(matcher.group(1));
                            System.out.println("monster card position changed successfully");
                        } else System.out.println("you already changed this card position in this turn");
                    } else System.out.println("this card is already in the wanted position");
                } else {
                    if (!currentPlayer.isThisMonsterOnAttackPosition(matcher.group(1))) {
                        if (currentPlayer.didWeChangePositionThisCardInThisTurn(index)) {
                            currentPlayer.setDidWeChangePositionThisCardInThisTurn(index);
                            currentPlayer.convertThisMonsterFromDefenceToAttack(matcher.group(1));
                            System.out.println("monster card position changed successfully");
                        } else System.out.println("you already changed this card position in this turn");
                    } else System.out.println("this card is already in the wanted position");
                }
            }
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

    }

    public void activeSpell(Matcher matcher) {
        if (matcher.find()) {
            Address address = new Address(matcher.group(1));
            Player currentPlayer = Game.whoseTurnPlayer();
            int index = currentPlayer.getIndexOfThisCardByAddress(new Address(matcher.group(1)));
            if (currentPlayer.didWeActivateThisSpell(index)) {
                if (currentPlayer.getSpellCardByStringAddress(matcher.group(1)).getSpellMode() == SpellMode.FIELD) {
                    if (SpellCard.canWeActivateThisSpell(address)) {
                        currentPlayer.setDidWeActivateThisSpell(index);
                        currentPlayer.setIsThisSpellActivated(true, index);
                        SpellCard.doSpellAbsorptionEffect();
                        System.out.println("spell activated");
                        currentPlayer.getSpellCardByStringAddress(matcher.group(1)).doEffect(currentPlayer.addCardToAddress(Board.getCardByAddress(address), "field", index));
                    } else System.out.println("preparations of this spell are not done yet");
                } else if (!(currentPlayer.isSpellZoneFull())) {
                    if (SpellCard.canWeActivateThisSpell(address)) {
                        currentPlayer.setDidWeActivateThisSpell(index);
                        currentPlayer.setIsThisSpellActivated(true, index);
                        SpellCard.doSpellAbsorptionEffect();
                        System.out.println("spell activated");
                        currentPlayer.getSpellCardByStringAddress(matcher.group(1)).doEffect(currentPlayer.addCardToAddress(Board.getCardByAddress(address), "spell", index));
                    } else System.out.println("preparations of this spell are not done yet");
                } else System.out.println("spell card zone is full");
            } else System.out.println("you have already activated this card");
        }
    }

    public void activeTrap(Matcher matcher) {

    }

    public void doEffect() {
        Player currentPlayer = Game.whoseTurnPlayer();
        if (currentPlayer.doWeHaveThisCardInBoard("Scanner")) {
            Address address = new Address(Integer.parseInt(Effect.run("Scanner")), "graveyard", false);
            if (Board.getCardByAddress(address).getKind().equals("monster")) {
                MonsterCard monsterCard = Game.whoseTurnPlayer().getMonsterCardByAddress(address);
                //address.setIfItIsScannerThenWhat(monsterCard);
            }
        }
        int count = Integer.parseInt(Effect.run("HeraldOfCreation1"));
        int numberOfHeraldOfCreation = Game.whoseTurnPlayer().howManyHeraldOfCreationDoWeHave("HeraldOfCreation2");
        for (int i = 0; i < minOfTwoNumber(numberOfHeraldOfCreation, count) - howManyHeraldOfCreationDidWeUseEffect; i++) {
            Address shouldBeRemoved = new Address(Integer.parseInt(Effect.run("HeraldOfCreation2")), "monster", true);
            if (!Board.isAddressEmpty(shouldBeRemoved)) {
                Address comeBackFromGraveyard = new Address(Integer.parseInt(Effect.run("HeraldOfCreation3")), "graveyard", true);
                MonsterCard monsterCardForHeraldOfCreation = Board.whatKindaMonsterIsHere(comeBackFromGraveyard);
                if (monsterCardForHeraldOfCreation != null) {
                    if (monsterCardForHeraldOfCreation.getLevel() >= 7) {
                        currentPlayer.removeCard(shouldBeRemoved);
                        currentPlayer.setCardFromGraveyardToMonsterZone(comeBackFromGraveyard);
                        if (whatMainIsPhase == 1) Game.getMainPhase2().increaseHowManyHeraldOfCreationDidWeUseEffect();
                    }
                }
            }
        }
    }

    public int minOfTwoNumber(int num1, int num2) {
        if (num1 <= num2) return num1;
        return num2;
    }

    public static Matcher getCommandMatcher(String input, String regex) {
        input = input.trim();
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(input);
    }

    public int getHowManyHeraldOfCreationDidWeUseEffect() {
        return howManyHeraldOfCreationDidWeUseEffect;
    }

    public void setHowManyHeraldOfCreationDidWeUseEffect(int howManyHeraldOfCreationDidWeUseEffect) {
        this.howManyHeraldOfCreationDidWeUseEffect = howManyHeraldOfCreationDidWeUseEffect;
    }

    public void increaseHowManyHeraldOfCreationDidWeUseEffect() {
        howManyHeraldOfCreationDidWeUseEffect++;
    }

    public int getWhatMainIsPhase() {
        return whatMainIsPhase;
    }

    public void setWhatMainIsPhase(int whatMainIsPhase) {
        this.whatMainIsPhase = whatMainIsPhase;
    }
}
