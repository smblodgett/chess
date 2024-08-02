package client;

import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import message.*;
import model.AuthData;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static ui.EscapeSequences.RESET_TEXT_COLOR;
import static ui.EscapeSequences.SET_TEXT_COLOR_BLUE;

public class ServerFacade {

    private int port;

    ServerFacade(int port){
        this.port=port;
    }

    public AuthData register(String username, String password, String email) {
        try {
            URL url = new URL("http://localhost:"+port+"/user");
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestMethod("POST");
            httpConnection.setDoOutput(true);
            httpConnection.connect();
            String jsonToSend = "{"
                    + "\"username\":\"" + username + "\","
                    + "\"password\":\"" + password + "\","
                    + "\"email\":\"" + email + "\""
                    + "}";
            try (OutputStream os = httpConnection.getOutputStream()) {
                os.write((jsonToSend).getBytes());
            }
            if (httpConnection.getResponseCode()==HttpURLConnection.HTTP_OK){
                InputStream response = httpConnection.getInputStream();
                Gson gson = new GsonBuilder().create();
                return gson.fromJson(new InputStreamReader(response), AuthData.class);
            }
            else {
                System.out.println("ERROR: " + httpConnection.getResponseMessage());
                // Get the error stream containing the HTTP response body (if any)
                InputStream respBody = httpConnection.getErrorStream();

                // Display the data returned from the server
                System.out.println(respBody.toString());
                return null;
            }

        }
        catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public AuthData login(String username, String password){
        try {
            URL url = new URL("http://localhost:"+port+"/session");
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestMethod("POST");
            httpConnection.setDoOutput(true);
            httpConnection.connect();
            String jsonToSend = "{"
                    + "\"username\":\"" + username + "\","
                    + "\"password\":\"" + password + "\""
                    + "}";
            try (OutputStream os = httpConnection.getOutputStream()) {
                os.write((jsonToSend).getBytes());
            }
            if (httpConnection.getResponseCode()==HttpURLConnection.HTTP_OK){
                //
                InputStream response = httpConnection.getInputStream();
                Gson gson = new GsonBuilder().create();
                return gson.fromJson(new InputStreamReader(response), AuthData.class);
            }
            else {
                System.out.println("ERROR: " + httpConnection.getResponseMessage());
                InputStream respBody = httpConnection.getErrorStream();
                System.out.println(respBody.toString());
                return null;
            }
        }
        catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public int createGame(String gameName,String authToken){
        try {
            URL url = new URL("http://localhost:"+port+"/game");
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestMethod("POST");
            httpConnection.setDoOutput(true);
            httpConnection.addRequestProperty("authorization",authToken);
            httpConnection.connect();
            String jsonToSend = "{"
                    + "\"gameName\":\"" + gameName + "\""
                    + "}";
            try (OutputStream os = httpConnection.getOutputStream()) {
                os.write((jsonToSend).getBytes());
            }
            if (httpConnection.getResponseCode()==HttpURLConnection.HTTP_OK){
                return 0;
            }
            else {
                System.out.println("ERROR: " + httpConnection.getResponseMessage());
                // Get the error stream containing the HTTP response body (if any)
                InputStream respBody = httpConnection.getErrorStream();
                // Display the data returned from the server
                System.out.println(respBody.toString());
                return -1;
            }
        }
        catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public void joinGame(ChessGame.TeamColor color, int gameID,String authToken){
        try {
            String playerColor=null;
            if (color== ChessGame.TeamColor.WHITE){playerColor="WHITE";}
            if (color== ChessGame.TeamColor.BLACK){playerColor="BLACK";}
            URL url = new URL("http://localhost:"+port+"/game");
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestMethod("PUT");
            httpConnection.setDoOutput(true);
            httpConnection.addRequestProperty("authorization",authToken);
            httpConnection.connect();
            String jsonToSend = "{"
                    + "\"playerColor\":\"" + playerColor + "\","
                    + "\"gameID\":\"" + gameID +"\""
                    + "}";
            try (OutputStream os = httpConnection.getOutputStream()) {
                os.write((jsonToSend).getBytes());
            }
            if (httpConnection.getResponseCode()==HttpURLConnection.HTTP_OK){
                System.out.println(SET_TEXT_COLOR_BLUE+"You've joined game no."+gameID+RESET_TEXT_COLOR);
            }
            else {
                System.out.println("ERROR: " + httpConnection.getResponseMessage());
                // Get the error stream containing the HTTP response body (if any)
                InputStream respBody = httpConnection.getErrorStream();
                // Display the data returned from the server
                System.out.println(respBody.toString());
            }
        }
        catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public ListGameReturn listGames(String authToken){
        try {
            URL url = new URL("http://localhost:"+port+"/game");
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestMethod("GET");
            httpConnection.setDoOutput(false);
            httpConnection.addRequestProperty("authorization",authToken);
            httpConnection.connect();

            if (httpConnection.getResponseCode()==HttpURLConnection.HTTP_OK){
                InputStream response = httpConnection.getInputStream();
                Gson gson = new GsonBuilder().create();
                return gson.fromJson(new InputStreamReader(response), ListGameReturn.class);
            }
            else {
                System.out.println("ERROR: " + httpConnection.getResponseMessage());
                // Get the error stream containing the HTTP response body (if any)
                InputStream respBody = httpConnection.getErrorStream();
                // Display the data returned from the server
                System.out.println(respBody.toString());
            }
        }
        catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
        return null;
    }

    public void logout(String authToken) {
        try {
            URL url = new URL("http://localhost:"+port+"/session");
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestMethod("DELETE");
            httpConnection.setDoOutput(false);
            httpConnection.addRequestProperty("authorization",authToken);
            httpConnection.connect();

            if (httpConnection.getResponseCode()==HttpURLConnection.HTTP_OK){
            }
            else {
                System.out.println("ERROR: " + httpConnection.getResponseMessage());
                // Get the error stream containing the HTTP response body (if any)
                InputStream respBody = httpConnection.getErrorStream();
                // Display the data returned from the server
                System.out.println(respBody.toString());
            }
        }
        catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public void clearData() {
        try {
            URL url = new URL("http://localhost:"+port+"/db");
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestMethod("DELETE");
            httpConnection.setDoOutput(false);
            httpConnection.connect();

            if (httpConnection.getResponseCode()==HttpURLConnection.HTTP_OK){
                return;
            }
            else {
                System.out.println("ERROR: " + httpConnection.getResponseMessage());
                InputStream respBody = httpConnection.getErrorStream();
                System.out.println(respBody.toString());
            }
        }
        catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
}
