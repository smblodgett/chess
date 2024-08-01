package client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.AuthData;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerFacade {

    private int port;

    ServerFacade(int port){
        this.port=port;
    }

    public AuthData register(String username, String password, String email) {
        try {
            URL url = new URL("http://localhost:"+port +"/user");
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

}
