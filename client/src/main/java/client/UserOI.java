package client;

import chess.ChessGame;
import model.AuthData;
import model.GameData;

import java.util.ArrayList;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class UserOI {

    public static ServerFacade facade;
    public static AuthData userAuth;

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
            userAuth = facade.register(username,password,email);
            if (userAuth == null){
                System.out.println(SET_BG_COLOR_RED+"there was an error registering you. try again?"+RESET_TEXT_COLOR);
            }
            else {
                loggedInMenu();
            }
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
            userAuth = facade.login(username,password);
            if (userAuth == null){
                System.out.println(SET_BG_COLOR_RED+"there was an error logging you in. try again?"+RESET_TEXT_COLOR);
            }
            else {
                loggedInMenu();
            }
        }
    }

    private void loggedInMenu() {
        System.out.println(SET_TEXT_COLOR_BLUE+"Logged in! It's time to chess! (Type help if you're lost.)"+RESET_TEXT_COLOR);
        boolean isLoggedInMenuGoing = true;
        Scanner scanner = new Scanner(System.in);
        while (isLoggedInMenuGoing) {
            String command = scanner.nextLine();

            Scanner lineScanner = new Scanner(command);  // Create a new scanner for the line
            ArrayList<String> commandInputs = new ArrayList<>();
            while (lineScanner.hasNext()) {
                String word = lineScanner.next();  // Read one word at a time
                commandInputs.add(word);  // Process the word
            }
            lineScanner.close();

            switch (commandInputs.get(0)){
                case "help":
                    helpLoggedIn();
                    break;
                case "create":
                    createGame(commandInputs);
                    break;
                case "join":
                    joinGame(commandInputs);
                    break;
                case "list":
                    listGames();
                    break;
                case "logout":
                    logout();
                    break;
            }
        }
    }

    private void helpLoggedIn() {
        System.out.println(SET_TEXT_COLOR_GREEN+"create <gameName> "+RESET_TEXT_COLOR+"- create a new game with name gameName");
        System.out.println(SET_TEXT_COLOR_GREEN+"join <white/black> <gameID> "+RESET_TEXT_COLOR+"- join a game, pick your side");
        System.out.println(SET_TEXT_COLOR_GREEN+"list "+RESET_TEXT_COLOR+"- list all current games");
        System.out.println(SET_TEXT_COLOR_GREEN+"logout "+RESET_TEXT_COLOR+"- logout of the application");
        System.out.println(SET_TEXT_COLOR_GREEN+"help "+RESET_TEXT_COLOR+"- you just selected this!");
    }

    private void createGame(ArrayList<String> commandInputs){
        if (commandInputs.size()!=2){
            System.out.println(SET_TEXT_COLOR_BLUE+"wrong number of inputs!"+RESET_TEXT_COLOR);
            helpLoggedIn();
        }
        else {
            String gameName = commandInputs.get(1);
            String authToken = userAuth.authToken();
            int gameID = facade.createGame(gameName,authToken);
            if (gameID == -1) {
                System.out.println(SET_BG_COLOR_RED+"there was an error creating a game. try again?"+RESET_TEXT_COLOR);
            }
            else {
                System.out.println(SET_BG_COLOR_BLUE+"your game "+gameName+ "was created with id="+gameID+"."+RESET_TEXT_COLOR);
            }
        }
    }

    private void joinGame(ArrayList<String> commandInputs) {
        if (commandInputs.size()!=3){
            System.out.println(SET_TEXT_COLOR_BLUE+"wrong number of inputs!"+RESET_TEXT_COLOR);
            helpLoggedIn();
        }
        else {
            ChessGame.TeamColor color;
            switch (commandInputs.get(1)){
                case "white":
                case "WHITE":
                case "White":
                case "w":
                case "W":
                    color = ChessGame.TeamColor.WHITE;
                    break;
                case "black":
                case "BLACK":
                case "Black":
                case "b":
                case "B":
                    color = ChessGame.TeamColor.BLACK;
                    break;
                default:
                    System.out.println(SET_TEXT_COLOR_BLUE+"Thou fool! Choose black or white!"+RESET_TEXT_COLOR);
                    color = null;
                    helpLoggedIn();
                    break;
            }
            int gameID = Integer.parseInt(commandInputs.get(2));
            String authToken = userAuth.authToken();
            facade.joinGame(color,gameID,authToken);
        }
    }

    private void listGames() {
        String authToken = userAuth.authToken();
        ArrayList<GameData> gameList = facade.listGames(authToken);

    }


}
