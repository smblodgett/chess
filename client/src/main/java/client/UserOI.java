package client;

import chess.ChessGame;
import model.AuthData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static ui.EscapeSequences.*;
import ui.ChessBoardPrinter;

public class UserOI {

    public static ServerFacade facade;
    public static AuthData userAuth;
    public static final String DIVIDERS = "■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□■□";
    public static Map<Integer,Integer> gameKey = new HashMap<>();

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
                    help();
                    break;
                case "login":
                    login(commandInputs);
                    help();
                    break;
                default:
                    badInputAction();
                    break;
            }
        }
        scanner.close();
    }

    private void help() {
        System.out.println(SET_BG_COLOR_LIGHT_GREY+DIVIDERS);
        System.out.println(SET_TEXT_COLOR_BLUE+"♝ options ♝"+RESET_TEXT_COLOR);
        System.out.println(SET_TEXT_COLOR_GREEN+"register <username> <password> <email> "+RESET_TEXT_COLOR+"- register a chess account");
        System.out.println(SET_TEXT_COLOR_GREEN+"login <username> <password> "+RESET_TEXT_COLOR+"- login to play chess");
        System.out.println(SET_TEXT_COLOR_GREEN+"quit "+RESET_TEXT_COLOR+"- exit the application");
        System.out.println(SET_TEXT_COLOR_GREEN+"help "+RESET_TEXT_COLOR+"- if you need more help");
        System.out.println(DIVIDERS);
    }

    private void quit(){
        System.out.println(DIVIDERS);
        System.out.println(SET_TEXT_COLOR_BLUE+"see you next time!"+RESET_TEXT_COLOR);
        System.out.println(DIVIDERS);
        try{
            Thread.sleep(3000);
        }
        catch (InterruptedException ex) {
            System.out.println("bye bye!");
        }
    }

    private void badInputAction() {
        System.out.println(DIVIDERS);
        System.out.println(SET_TEXT_COLOR_BLUE+"umm....that's an invalid input, mate. Try again!"+RESET_TEXT_COLOR);
        System.out.println(DIVIDERS);
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
                System.out.println(SET_BG_COLOR_RED+SET_TEXT_COLOR_BLACK+"there was an error registering you. maybe try again?\n"+RESET_TEXT_COLOR);
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
                System.out.println(SET_TEXT_COLOR_BLACK+"there was an error logging you in. maybe try again?"+RESET_TEXT_COLOR);
            }
            else {
                loggedInMenu();
            }
        }
    }

    private void loggedInMenu() {
        System.out.println(DIVIDERS);
        System.out.println(SET_TEXT_COLOR_BLUE+"You're in! It's time to chess! (Type help if you're lost.)"+RESET_TEXT_COLOR);
        System.out.println(DIVIDERS);
        listGames(false);
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
                case "play":
                    joinGame(commandInputs);
                    break;
                case "watch":
                case "observe":
                    watchGame(commandInputs);
                    break;
                case "list":
                    listGames(true);
                    break;
                case "logout":
                    logout();
                    isLoggedInMenuGoing=false;
                    help();
                    break;
                case "clear":
                    clearData();
                    isLoggedInMenuGoing=false;
                    break;
                default:
                    badInputAction();
                    break;
            }
        }
    }

    private void helpLoggedIn() {
        System.out.println(SET_BG_COLOR_LIGHT_GREY+DIVIDERS);
        System.out.println(SET_TEXT_COLOR_BLUE+"♖ options ♖"+RESET_TEXT_COLOR);
        System.out.println(SET_TEXT_COLOR_GREEN+"create <gameName> "+RESET_TEXT_COLOR+"- create a new game with name gameName");
        System.out.println(SET_TEXT_COLOR_GREEN+"play <white/black> <gameListNumber> "+RESET_TEXT_COLOR+"- join a game, pick your side");
        System.out.println(SET_TEXT_COLOR_GREEN+"observe <gameListNumber> "+RESET_TEXT_COLOR+"- join a game, pick your side");
        System.out.println(SET_TEXT_COLOR_GREEN+"list "+RESET_TEXT_COLOR+"- list all current games");
        System.out.println(SET_TEXT_COLOR_GREEN+"logout "+RESET_TEXT_COLOR+"- logout of the application");
        System.out.println(SET_TEXT_COLOR_GREEN+"clear "+RESET_TEXT_COLOR+"- clear all data");
        System.out.println(SET_TEXT_COLOR_GREEN+"help "+RESET_TEXT_COLOR+"- if you need more help");
        System.out.println(DIVIDERS);
    }

    private void createGame(ArrayList<String> commandInputs){
        if (commandInputs.size()!=2){
            System.out.println(SET_TEXT_COLOR_BLUE+"wrong number of inputs!"+RESET_TEXT_COLOR);
            helpLoggedIn();
        }
        else {
            String gameName = commandInputs.get(1);
            String authToken = userAuth.authToken();
            int gameAdded = facade.createGame(gameName,authToken);
            if (gameAdded == -1) {
                System.out.println(SET_TEXT_COLOR_RED+"there was an error creating a game. try again?"+RESET_TEXT_COLOR);
            }
            else {
                System.out.println(SET_TEXT_COLOR_BLUE+"your game, \""+gameName+ "\", was created."+RESET_TEXT_COLOR);
                gameKey.put(gameKey.size()+1,gameKey.size()+1);
            }
        }
    }

    private void joinGame(ArrayList<String> commandInputs) {
        int gameListNumber;
        try {
            gameListNumber = Integer.parseInt(commandInputs.get(2));
        }
        catch (Exception ex) {
            System.out.println(SET_TEXT_COLOR_BLUE+"second input must be game list number!"+RESET_TEXT_COLOR);
            return;
        }
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
            try {
                int gameID = gameKey.get(gameListNumber);
                String authToken = userAuth.authToken();
                facade.joinGame(color,gameID,authToken);
                ChessGame chessGame = new ChessGame(); // this will  probably be replaced?
                var printer = new ChessBoardPrinter(chessGame);
                printer.drawEverything();
            }
            catch (NullPointerException ex) {
                System.out.println(SET_TEXT_COLOR_BLUE+"null error for some reason :/"+RESET_TEXT_COLOR);
                helpLoggedIn();
            }

        }
    }

    private void watchGame(ArrayList<String> commandInputs){
        int gameListNumber;
        try {
            gameListNumber = Integer.parseInt(commandInputs.get(1));
        }
        catch (Exception ex) {
            System.out.println(SET_TEXT_COLOR_BLUE+"second input must be game list number!"+RESET_TEXT_COLOR);
            return;
        }
        if (commandInputs.size()!=2){
            System.out.println(SET_TEXT_COLOR_BLUE+"wrong number of inputs!"+RESET_TEXT_COLOR);
            helpLoggedIn();
        }
        try {
            ChessGame chessGame = new ChessGame(); // this will  probably be replaced with real game data
            var printer = new ChessBoardPrinter(chessGame);
            printer.drawEverything();
        }
        catch (NullPointerException ex) {
            System.out.println(SET_TEXT_COLOR_BLUE+"null error for some reason :/"+RESET_TEXT_COLOR);
            helpLoggedIn();
        }
    }

    private void listGames(boolean isDisplayed) {
        String authToken = userAuth.authToken();
        var impureGameList = facade.listGames(authToken);
        var gameList = impureGameList.removeChessBoards();
        gameKey.clear();
        int count = 1;
        for (var game : gameList){
            var name = game.gameName();
            var whiteName = game.whiteUsername();
            var blackName = game.blackUsername();
            var gameID = game.gameID();
            if (isDisplayed) {
                System.out.println(DIVIDERS);
                System.out.println(count + ":");
                System.out.println(SET_TEXT_COLOR_BLUE + "game name: " + name + "\nwhite is " + whiteName
                        + "\nblack is " + blackName + "\ngameid=" + gameID + RESET_TEXT_COLOR);
            }
            gameKey.put(count,gameID);
            count++;
        }
        if (gameList.isEmpty()){
            System.out.println(DIVIDERS);
            System.out.println(SET_TEXT_COLOR_BLUE+"There aren't any games made, yet. Maybe you should make one..."+RESET_TEXT_COLOR);
            System.out.println(DIVIDERS);
            try{
                Thread.sleep(3000);
            }
            catch (InterruptedException ex) {
                System.out.println("!!!!!!!!");
            }
            helpLoggedIn();
        }
    }

    private void logout() {
        String authToken = userAuth.authToken();
        facade.logout(authToken);
        System.out.println(SET_TEXT_COLOR_BLUE+"you've now been logged out"+RESET_TEXT_COLOR);
    }

    private void clearData(){
        facade.clearData();
        System.out.println(SET_BG_COLOR_RED+SET_TEXT_COLOR_BLACK+DIVIDERS);
        System.out.println("everything. has. been. destroyed.");
        System.out.println(DIVIDERS+RESET_TEXT_COLOR);
        try{
            Thread.sleep(3000);
        }
        catch (InterruptedException ex) {
            System.out.println("!!!!!!!!");
        }
    }

}
