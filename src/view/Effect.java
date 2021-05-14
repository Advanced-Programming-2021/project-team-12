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
            System.out.println("Which card do you want to be scanned from rival's graveyard?(Please just type graveyard number!)");
            Main.scanner=new Scanner(System.in);
            return (Main.scanner.nextLine());
        }
    }
}
