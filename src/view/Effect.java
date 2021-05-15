package view;


import java.util.Scanner;

public class Effect {
    public static String run(String string){
        if(string.equals("Suijin")){
            System.out.println("Do you want to use its effect?(please type \"yes\" or \"no\"!)");
            Main.scanner = new Scanner(System.in);
            return (Main.scanner.nextLine());
        }
        if(string.equals("ManEaterBug")){
            System.out.println("Which rival's monster card you want to be destroyed for effect of ManEaterBug?(Please just type monster zone number!)");
            Main.scanner=new Scanner(System.in);
            return (Main.scanner.nextLine());
        }
        if(string.equals("Scanner")){
            System.out.println("Which card do you want to be scanned from rival's graveyard?(Please just type graveyard number!)");
            Main.scanner=new Scanner(System.in);
            return (Main.scanner.nextLine());
        }
        if(string.equals("BeastKingBarbaros")){
            System.out.println("How many monsters do you want to tribute?(type 2 or 3)");
            Main.scanner=new Scanner(System.in);
            return (Main.scanner.nextLine());
        }
        if(string.equals("HeraldOfCreation1")){
            System.out.println("How many HeraldOfCreation card do you want to use?");
            Main.scanner=new Scanner(System.in);
            return (Main.scanner.nextLine());
        }
        if(string.equals("HeraldOfCreation2")){
            System.out.println("Choose a card for tribute from monster zone!(type a number from 1 to 5)");
            Main.scanner=new Scanner(System.in);
            return (Main.scanner.nextLine());
        }
        if(string.equals("HeraldOfCreation3")){
            System.out.println("Choose a monster card with level 7 or more for your graveyard!(type number)");
            Main.scanner=new Scanner(System.in);
            return (Main.scanner.nextLine());
        }
        if(string.equals("Terratiger")){
            System.out.println("Choose a monster card with level 4 or less for your hand!(type number if you don't type 0)");
            Main.scanner=new Scanner(System.in);
            return (Main.scanner.nextLine());
        }


    }
}
