package client;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
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
    public static WSFacade wsFacade;
    public static ChessGame currentGame = null;
    public static UserOIHelper helper = new UserOIHelper();

    public UserOI(){
        facade = new ServerFacade(8080);
        wsFacade = new WSFacade("http://localhost:8080", new NotificationHandler(), new ErrorHandler(),new LoadGameMessageHandler());
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
                    helper.help();
                    break;
                case "quit":
                    isBaseGoing=false;
                    quit();
                    break;
                case "register":
                    register(commandInputs,scanner);
                    helper.help();
                    break;
                case "login":
                    login(commandInputs,scanner);
                    helper.help();
                    break;
                default:
                    badInputAction();
                    break;
            }
        }
        scanner.close();
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

    private void register(ArrayList<String> commandInputs,Scanner scanner) {
        if (commandInputs.size()!=4){
            System.out.println(SET_TEXT_COLOR_BLUE+"wrong number of inputs!"+RESET_TEXT_COLOR);
            helper.help();
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
                loggedInMenu(scanner);
            }
        }
    }

    private void login(ArrayList<String> commandInputs,Scanner scanner) {
        if (commandInputs.size()!=3){
            System.out.println(SET_TEXT_COLOR_BLUE+"wrong number of inputs!"+RESET_TEXT_COLOR);
        }
        else {
            String username = commandInputs.get(1);
            String password = commandInputs.get(2);
            userAuth = facade.login(username,password);
            if (userAuth == null){
                System.out.println(SET_TEXT_COLOR_BLACK+"there was an error logging you in. maybe try again?"+RESET_TEXT_COLOR);
            }
            else {
                loggedInMenu(scanner);
            }
        }
    }

    private void loggedInMenu(Scanner scanner) {
        System.out.println(DIVIDERS);
        System.out.println(SET_TEXT_COLOR_BLUE+"You're in! It's time to chess! (Type help if you're lost.)"+RESET_TEXT_COLOR);
        System.out.println(DIVIDERS);
        listGames(false);
        boolean isLoggedInMenuGoing = true;
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
                    helper.helpLoggedIn();
                    break;
                case "create":
                    createGame(commandInputs);
                    break;
                case "join":
                case "play":
                    joinGame(commandInputs,scanner);
                    break;
                case "watch":
                case "observe":
                    watchGame(commandInputs,scanner);
                    break;
                case "list":
                    listGames(true);
                    break;
                case "logout":
                    logout();
                    isLoggedInMenuGoing=false;
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

    private void createGame(ArrayList<String> commandInputs){
        if (commandInputs.size()!=2){
            System.out.println(SET_TEXT_COLOR_BLUE+"wrong number of inputs!"+RESET_TEXT_COLOR);
            helper.helpLoggedIn();
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

    private void joinGame(ArrayList<String> commandInputs,Scanner scanner) {
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
            helper.helpLoggedIn();
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
                    helper.helpLoggedIn();
                    break;
            }
            try {
                int gameID = gameKey.get(gameListNumber);
                String authToken = userAuth.authToken();
                facade.joinGame(color,gameID,authToken);
                wsFacade.connectToGame(authToken,gameID,color);
                inGameMenu(authToken,gameID,color,scanner);
            }
            catch (RuntimeException ex) {
                helper.helpLoggedIn();
            }
        }
    }

    private void watchGame(ArrayList<String> commandInputs,Scanner scanner){
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
            helper.helpLoggedIn();
        }
        try {
            int gameID = gameKey.get(gameListNumber);
            String authToken = userAuth.authToken();
            wsFacade.connectToGame(authToken,gameID,null);
            ChessGame chessGame = currentGame; //// this will  probably be replaced with real game data
            var printer = new ChessBoardPrinter(chessGame, ChessGame.TeamColor.WHITE);
            printer.drawEverything();
        }
        catch (NullPointerException ex) {
            System.out.println(SET_TEXT_COLOR_BLUE+"null error for some reason :/"+RESET_TEXT_COLOR);
            helper.helpLoggedIn();
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
                System.out.println(SET_TEXT_COLOR_GREEN+count+ ":");
                System.out.println(SET_TEXT_COLOR_BLUE + "game name: " + name + "\nwhite is " + whiteName
                        + "\nblack is " + blackName + "\ngameid=" + gameID + RESET_TEXT_COLOR);
            }
            gameKey.put(count,gameID);
            count++;
        }
        if (isDisplayed && !gameList.isEmpty()) {System.out.println(DIVIDERS);}
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
            helper.helpLoggedIn();
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

    private void inGameMenu(String authToken, int gameID, ChessGame.TeamColor color,Scanner scanner) {
        waitForUpdate();
        System.out.println(currentGame.toString());
        String colorString;
        if (color== ChessGame.TeamColor.WHITE) {colorString = SET_TEXT_COLOR_WHITE + "WHITE";}
        else {colorString = SET_TEXT_COLOR_BLACK + "BLACK";}
        System.out.println(DIVIDERS);
        System.out.println(SET_TEXT_COLOR_BLUE+"You've joined game no."+gameID+" as "
                +colorString+SET_TEXT_COLOR_BLUE+" (type help for more options)"+RESET_TEXT_COLOR);
        System.out.println(DIVIDERS);

        ChessGame game = currentGame;
        var printer = new ChessBoardPrinter(game,color);
        printer.drawEverything();
        wsFacade.setIsOnMessage(false);

        String username = userAuth.username();
        boolean isInGameMenuGoing = true;
        while (isInGameMenuGoing) {
            if (game!=currentGame && currentGame!=null){
                game=currentGame;
            }
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
                    helper.helpInGame();
                    break;
                case "draw":
                case "redraw":
                    redraw(game);
                    break;
                case "leave":
                    isInGameMenuGoing=false;
                    leaveGame(authToken,gameID,username);
                    helper.helpLoggedIn();
                    break;
                case "move":
                    makeMove(commandInputs,game,authToken,gameID);
                    break;
                case "highlight":
                    highlightLegalMoves(commandInputs,game,color);
                    break;
                case "resign":
                    resign(game, authToken,gameID, color,username,scanner);
                    break;
                default:
                    badInputAction();
                    break;
            }
        }
    }

    private void waitForUpdate(){
        while (!wsFacade.getIsOnMessage()) {
            try {
                Thread.sleep(100);
            }
            catch(InterruptedException ex){
                System.out.println("interrupted!");
            }
        }
        System.out.println("okay, we got something from WSFacade");
    }

    private void redraw(ChessGame game) {
        var printer = new ChessBoardPrinter(game, ChessGame.TeamColor.WHITE);
        printer.drawEverything();
    }

    private void leaveGame(String authToken, int gameID,String username){
        wsFacade.leaveGame(authToken,gameID,username);
        waitForUpdate();
        wsFacade.setIsOnMessage(false);
        currentGame=null;
    }

    private void makeMove(ArrayList<String> commandInputs,ChessGame game,String authToken,int gameID ){
        if (commandInputs.size()!=3){ // maybe need to add promotion functionality
            System.out.println(SET_TEXT_COLOR_BLUE+"wrong number of inputs!"+RESET_TEXT_COLOR);
            helper.helpInGame();
        }
        String initialPositionString = commandInputs.get(1);
        String finalPositionString = commandInputs.get(2);
        try {
            int rowInit = getRowFromString(initialPositionString.substring(1));
            int colInit = getColumnFromLetter(initialPositionString.charAt(0));
            ChessPosition initialPosition = new ChessPosition(rowInit,colInit); //
            int rowFin = getRowFromString(finalPositionString.substring(1));
            int colFin = getColumnFromLetter(finalPositionString.charAt(0));
            ChessPosition finalPosition = new ChessPosition(rowFin,colFin); //
            ChessPiece.PieceType promotionPieceType=null;
            if (game.getBoard().getPiece(initialPosition).getPieceType()== ChessPiece.PieceType.PAWN
                && finalPosition.getRow()==8){
                System.out.println("what piece do you want to promote to?");
                Scanner scanner = new Scanner(System.in);
                String promotionPieceString = scanner.nextLine();
                scanner.close();
                promotionPieceType = getPieceTypeFromString(promotionPieceString);
            }
            ChessMove move = new ChessMove(initialPosition,finalPosition,promotionPieceType);
            wsFacade.makeMove(authToken,gameID,move);
            waitForUpdate();
            wsFacade.setIsOnMessage(false);
        }
        catch (IllegalArgumentException ex) {
            System.out.println(SET_TEXT_COLOR_BLUE+"you didn't input your move right!"+RESET_TEXT_COLOR);
        }
    }

    private ChessPiece.PieceType getPieceTypeFromString(String promotionPieceString) throws IllegalArgumentException{
        return switch (promotionPieceString){
            case "knight","Knight","KNIGHT","n","N"-> ChessPiece.PieceType.KNIGHT;
            case "bishop", "Bishop","BISHOP","b","B" -> ChessPiece.PieceType.BISHOP;
            case "rook","Rook","ROOK","r","R" -> ChessPiece.PieceType.ROOK;
            case "queen","Queen","QUEEN","q","Q" -> ChessPiece.PieceType.QUEEN;
            default -> throw new IllegalArgumentException("you can't promote to that!");
        };
    }

    private int getColumnFromLetter(char l) throws IllegalArgumentException {
        return switch (l){
            case 'a', 'A' -> 1;
            case 'b', 'B' -> 2;
            case 'c', 'C' -> 3;
            case 'd', 'D' -> 4;
            case 'e', 'E' -> 5;
            case 'f', 'F' -> 6;
            case 'g', 'G' -> 7;
            case 'h', 'H' -> 8;
            default -> throw new IllegalArgumentException("Invalid column letter: " + l);
        };
    }
    private int getRowFromString(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid row number: " + s, e);
        }
    }

    private void highlightLegalMoves(ArrayList<String> commandInputs, ChessGame game, ChessGame.TeamColor myColor) {
        if (commandInputs.size()!=2){ // maybe need to add promotion functionality
            System.out.println(SET_TEXT_COLOR_BLUE+"wrong number of inputs!"+RESET_TEXT_COLOR);
            helper.helpInGame();
        }
        String highlightPositionString = commandInputs.get(1);
        try {
            int row = getRowFromString(highlightPositionString.substring(1));
            int col = getColumnFromLetter(highlightPositionString.charAt(0));
            ChessPosition highlightPosition = new ChessPosition(row,col);
            var printer = new ChessBoardPrinter(game, myColor);
            printer.highlightedBoard(highlightPosition);
        }
        catch (IllegalArgumentException ex) {
            System.out.println(SET_TEXT_COLOR_BLUE+"you didn't input your move right!"+RESET_TEXT_COLOR);
        }
    }

    private void resign (ChessGame game, String authToken, int gameID, ChessGame.TeamColor color,String username,Scanner scanner) {
        System.out.println("are you sure you want to resign? you'll lose instantly");
        String resignPrompt = scanner.nextLine();
        boolean isResign;
        switch (resignPrompt){
            case "yes","YES","y","Y"->isResign=true;
            default -> isResign=false;
        }
        if (!isResign){inGameMenu(authToken,gameID,color,scanner);}
        wsFacade.resign(authToken,gameID,username);
    }
}