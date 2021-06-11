package view;

import Exceptions.CardNotExistException;
import Exceptions.EmptyAddressException;
import Exceptions.WrongAddressException;
import Utility.CommandMatcher;
import controllers.ExportCard;
import controllers.ImportCard;

import java.util.regex.Matcher;

public class ImportOrExport {
    public void run() {
        String input;
        while (true) {
            input = Main.scanner.nextLine().trim();
            if (input.compareTo("menu exit") == 0)
                return;
            else if (input.matches("import -(s|m|t) (--address|-a) [\\S]+.json"))
                importCard(input);
            else if (input.matches("export Card [\\w-]+ (--address|-a) [\\S]+.json"))
                exportCard(input);
            else if (input.matches("menu show-current"))
                System.out.println("Import Or Export");
            else if (input.matches("menu enter [\\w]+"))
                System.out.println("menu navigation is not possible");
            else
                System.out.println("invalid command!");
        }
    }

    private void importCard(String input) {
        Matcher matcher = CommandMatcher.getCommandMatcher(input, "import -(s|m|t) (--address|-a) ([\\S]+.json)");
        matcher.find();
        String address = matcher.group(3);
        String flag = matcher.group(1);
        try {
            new ImportCard().run(address, flag);
            System.out.println("Card Import Successfully");
        }catch (WrongAddressException e) {
            System.out.println("Wrong Address!");
        }catch (EmptyAddressException e) {
            System.out.println("Empty Address!");
        }
    }

    private void exportCard(String input) {
        Matcher matcher = CommandMatcher.getCommandMatcher(input, "export Card [\\w-]+ (--address|-a) ([\\S]+.json)");
        matcher.find();
        String address = matcher.group(3);
        String cardName = matcher.group(1);
        try {
            new ExportCard().run(address, cardName);
            System.out.println("Card Import Successfully");
        }catch (WrongAddressException e) {
            System.out.println("Wrong Address!");
        }catch (EmptyAddressException e) {
            System.out.println("Empty Address!");
        }catch (CardNotExistException e) {
            System.out.println("card with this name does not exist");
        }
    }
}
