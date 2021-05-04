package view;


import java.util.Scanner;

public class Effect {
    public static String run(String string){
        if(string.equals("Suijin")){
            System.out.println("Do you want to use its effect?(please type \"yes\" or \"no\"!)");
            Main.scanner = new Scanner(System.in);
            return (Main.scanner.nextLine());
        }
    }
}
