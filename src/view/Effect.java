package view;


import java.util.Scanner;

public class Effect {
    public static String run(String cardName){
        if(cardName.equals("Suijin"))
            System.out.println("Do you want to use its effect?(please type \"yes\" or \"no\"!)");
        if(cardName.equals("ManEaterBug"))
            System.out.println("Which rival's monster card you want to be destroyed for effect of ManEaterBug?(Please just type monster zone number!)");
        if(cardName.equals("Scanner"))
            System.out.println("Which card do you want to be scanned from rival's graveyard?(Please just type graveyard number!)");
        if(cardName.equals("BeastKingBarbaros"))
            System.out.println("How many monsters do you want to tribute?(type 2 or 3)");
        if(cardName.equals("HeraldOfCreation1"))
            System.out.println("How many HeraldOfCreation card do you want to use?");
        if(cardName.equals("HeraldOfCreation2"))
            System.out.println("Choose a card for tribute from monster zone!(type a number from 1 to 5)");
        if(cardName.equals("HeraldOfCreation3"))
            System.out.println("Choose a monster card with level 7 or more for your graveyard!(type number)");
        if(cardName.equals("Terratiger"))
            System.out.println("Choose a monster card with level 4 or less for your hand!(type number if you don't type 0)");
        if(cardName.equals("Terraforming"))
            System.out.println("Type a name of field spell card from your deck to come to your hand.(Please type in CamelCase!)");
        if(cardName.equals("TwinTwisters")) {
            System.out.println("Choose a card from your hand to remove and choose two of rival's trap or trap to be destroyed!(type 3 number in 3 lines)");
            return Main.scanner.nextLine()+","+Main.scanner.nextLine()+","+Main.scanner.nextLine();
        }
        if(cardName.equals("MysticalSpaceTyphoon"))
            System.out.println("Choose two of rival's trap or trap to be destroyed!(type a number)");
        return (Main.scanner.nextLine());
    }
}
