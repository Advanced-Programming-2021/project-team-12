package view;

import Exceptions.MyException;
import Utility.CommandMatcher;
import controllers.ImportOrExportControl;

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
            new ImportOrExportControl().doImport(address, flag);
            System.out.println("Card Import Successfully");
        } catch (MyException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void exportCard(String input) {
        Matcher matcher = CommandMatcher.getCommandMatcher(input, "export Card [\\w-]+ (--address|-a) ([\\S]+.json)");
        matcher.find();
        String address = matcher.group(3);
        String cardName = matcher.group(1);
        try {
            new ImportOrExportControl().doExport(address, cardName);
            System.out.println("Card Import Successfully");
        } catch (MyException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
