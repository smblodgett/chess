package client;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerFacade {

    private int port;

    ServerFacade(int port){
        this.port=port;
    }

    public void register(String username, String password, String email){
        try {
            URL url = new URL("http://localhost:8080/user");
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestMethod("POST");
            httpConnection.setDoInput(true);
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

            }

        }
        catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

}
