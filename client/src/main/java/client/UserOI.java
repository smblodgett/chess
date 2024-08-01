package client;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class UserOI {

    public void run(){
        baseLevelMenu();
    }

    public void baseLevelMenu(){
        boolean isBaseGoing = true;
        Scanner scanner = new Scanner(System.in);
        while (isBaseGoing) {
            String command = scanner.nextLine();
            switch (command) {
                case "help":
                    help();
                    break;
                case "quit":
                    isBaseGoing=false;
                    quit();
                    break;
                case "register":
                    break;
                case "login":
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

}
