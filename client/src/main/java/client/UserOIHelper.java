package client;

import static client.UserOI.DIVIDERS;
import static ui.EscapeSequences.*;

public class UserOIHelper {


    public void help() {
        System.out.println(SET_BG_COLOR_LIGHT_GREY+DIVIDERS);
        System.out.println(SET_TEXT_COLOR_BLUE+"♝ options ♝"+RESET_TEXT_COLOR);
        System.out.println(SET_TEXT_COLOR_GREEN+"register <username> <password> <email> "+RESET_TEXT_COLOR+"- register a chess account");
        System.out.println(SET_TEXT_COLOR_GREEN+"login <username> <password> "+RESET_TEXT_COLOR+"- login to play chess");
        System.out.println(SET_TEXT_COLOR_GREEN+"quit "+RESET_TEXT_COLOR+"- exit the application");
        System.out.println(SET_TEXT_COLOR_GREEN+"help "+RESET_TEXT_COLOR+"- if you need more help");
        System.out.println(DIVIDERS);
    }

    public void helpLoggedIn() {
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

    public void helpInGame() {
        System.out.println(SET_BG_COLOR_LIGHT_GREY+DIVIDERS);
        System.out.println(SET_TEXT_COLOR_BLUE+"♞ options ♞"+RESET_TEXT_COLOR);
        System.out.println(SET_TEXT_COLOR_GREEN+"draw "+RESET_TEXT_COLOR+"- redraw the current game");
        System.out.println(SET_TEXT_COLOR_GREEN+"move <initial location> <final location> "+RESET_TEXT_COLOR+"- move a chess piece");
        System.out.println(SET_TEXT_COLOR_GREEN+"highlight <location> "+RESET_TEXT_COLOR+"- highlight all legal moves from this location");
        System.out.println(SET_TEXT_COLOR_GREEN+"resign "+RESET_TEXT_COLOR+"- resign this game");
        System.out.println(SET_TEXT_COLOR_GREEN+"leave "+RESET_TEXT_COLOR+"- leave this game");
        System.out.println(DIVIDERS);
    }

}
