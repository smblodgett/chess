package client;

import java.util.ArrayList;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class UserOI {

    public static ServerFacade facade;

    public UserOI(){
        facade = new ServerFacade(8080);
    }

    public void run(){
        baseLevelMenu();
    }

    public void baseLevelMenu(){
        boolean isBaseGoing = true;
        Scanner scanner = new Scanner(System.in);
        while (isBaseGoing) {
            String command = scanner.nextLine();

            Scanner lineScanner = new Scanner(command);  // Create a new scanner for the line
            ArrayList<String> commandInputs = new ArrayList<>();
            while (lineScanner.hasNext()) {
                String word = lineScanner.next();  // Read one word at a time
                commandInputs.add(word);  // Process the word
            }
            lineScanner.close();

            switch (commandInputs.getFirst()) {
                case "help":
                    help();
                    break;
                case "quit":
                    isBaseGoing=false;
                    quit();
                    break;
                case "register":
                    register(commandInputs);
                    break;
                case "login":
                    login(commandInputs);
                    break;
                default:
                    badInputAction();
                    break;
            }
        }

        scanner.close();

    }

    private void help() {
        System.out.println(SET_TEXT_COLOR_BLUE+"These are the things this app is programmed to do:"+RESET_TEXT_COLOR);
        System.out.println(SET_TEXT_COLOR_GREEN+"register <username> <password> <email> "+RESET_TEXT_COLOR+"- register a chess account");
        System.out.println(SET_TEXT_COLOR_GREEN+"login <username> <password> "+RESET_TEXT_COLOR+"- login to play chess");
        System.out.println(SET_TEXT_COLOR_GREEN+"quit "+RESET_TEXT_COLOR+"- exit the application");
        System.out.println(SET_TEXT_COLOR_GREEN+"help "+RESET_TEXT_COLOR+"- you just selected this!");
    }

    private void quit(){
        System.out.println(SET_TEXT_COLOR_BLUE+"see you next time!");
        try{
            Thread.sleep(3000);
        }
        catch (InterruptedException ex) {
            System.out.println("bye bye!");
        }
    }

    private void badInputAction() {
        System.out.println(SET_TEXT_COLOR_BLUE+"umm....that's an invalid input, mate. Try again!"+RESET_TEXT_COLOR);
    }

    private void register(ArrayList<String> commandInputs) {
        if (commandInputs.size()!=4){
            System.out.println(SET_TEXT_COLOR_BLUE+"wrong number of inputs!"+RESET_TEXT_COLOR);
            help();
        }
        else {
            String username = commandInputs.get(1);
            String password = commandInputs.get(2);
            String email = commandInputs.get(3);
            ServerFacade.register(username,password,email);
        }

    }

    private void login(ArrayList<String> commandInputs) {
        if (commandInputs.size()!=3){
            System.out.println(SET_TEXT_COLOR_BLUE+"wrong number of inputs!"+RESET_TEXT_COLOR);
            help();
        }
        else {
            String username = commandInputs.get(1);
            String password = commandInputs.get(2);
        }
    }

}
