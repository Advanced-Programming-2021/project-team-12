package view;

import models.Address;
import models.Player;

public class ShowBoard {
    public ShowBoard(Player currentPlayer, Player opponentPlayer) {
        opponentPlayerData(opponentPlayer);
        System.out.println("--------------------------------");
        currentPlayerData(currentPlayer);
    }

    public static void showGraveyard(Player currentPlayer, Player opponentPlayer) {
        System.out.println("current player graveyard cards:");
        if (currentPlayer.getGraveyardCard().size() == 0)
            System.out.println("there is no card in current player graveyard");
        for (int i = 1; i <= currentPlayer.getGraveyardCard().size(); i++)
            System.out.println(i + ": " + currentPlayer.getGraveyardCard().get(i).getCardName());
        System.out.println("_________________________________");
        System.out.println("opponent player graveyard cards:");
        if (opponentPlayer.getGraveyardCard().size() == 0)
            System.out.println("there is no card in opponent player graveyard");
        for (int i = 1; i <= opponentPlayer.getGraveyardCard().size(); i++)
            System.out.println(i + ": " + opponentPlayer.getGraveyardCard().get(i).getCardName());
    }

    public static void showFieldZone(Player player) {
        if (player.getFieldCard().containsKey(1))
            System.out.println(player.getFieldCard().get(1).getCardName());
        else System.out.println("there is no card in field zone");
    }

    private void opponentPlayerData(Player player) {
        System.out.println(player.getNickName() + ": " + player.getLP());
        showHandCard(player, "o");
        System.out.println(player.getNUmberOfUnusedCard());
        showSpells(player, "o");
        showMonsters(player, "o");
        showGYF(player, "o");
    }

    private void showHandCard(Player player, String flag) {
        int c = player.getNumberOFHandCard();
        if (flag.equals("c")) {
            for (int i = 0; i < 6 - c; i++)
                System.out.printf(" \t");
        }
        else System.out.printf("\t");
        for (int i = 0; i < c; i++)
            System.out.printf("c\t");
        System.out.println();
    }

    private void showGYF(Player player, String flag) {
        String A;
        String B;
        String Field;
        int graveCard = player.getNumberOfGraveyardCard();
        if (player.isFieldEmpty())
            Field = "E";
        else
            Field = "O";
        if (flag.equals("o")) {
            A = String.valueOf(graveCard);
            B = Field;
        }
        else {
            A = Field;
            B = String.valueOf(graveCard);
        }
        System.out.printf(A);
        for(int i = 0; i < 5; i++)
            System.out.printf("\t ");
        System.out.println("\t" + B);
    }

    private void showMonsters(Player player, String flag) {
        if (flag.equals("o")) {
            monster(player, 5);
            monster(player, 3);
            monster(player, 1);
            monster(player, 2);
            monster(player, 4);
        }
        else if (flag.equals("c")) {
            monster(player, 4);
            monster(player, 2);
            monster(player, 1);
            monster(player, 3);
            monster(player, 5);
        }
        System.out.println();
    }

    private void monster(Player player, int i) {
        if (player.getCardByAddress(new Address(i, "monster", true)) == null)
            System.out.printf("\t" + "E");
        else
            System.out.printf("\t" + player.getMonsterPosition(i).toString());
    }

    private void showSpells(Player player, String flag) {
        if (flag.equals("o")) {
            spell(player, 5);
            spell(player, 3);
            spell(player, 1);
            spell(player, 2);
            spell(player, 4);
        }
        else if (flag.equals("c")) {
            spell(player, 4);
            spell(player, 2);
            spell(player, 1);
            spell(player, 3);
            spell(player, 5);
        }
        System.out.println();
    }

    private void spell(Player player, int i) {
        if (player.getCardByAddress(new Address(i, "spell", true)) == null)
            System.out.printf("\t" + "E");
        else if (player.getSpellPosition(i))
            System.out.printf("\t" + "O");
        else
            System.out.printf("\t" + "H");
    }

    private void currentPlayerData(Player player) {
        showGYF(player, "c");
        showMonsters(player, "c");
        showSpells(player, "c");
        System.out.println("\t\t\t\t\t\t     " + player.getNUmberOfUnusedCard());
        showHandCard(player, "c");
        System.out.println(player.getNickName() + ": " + player.getLP());
    }

}