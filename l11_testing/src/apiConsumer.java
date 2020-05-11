import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class apiConsumer {

    public static void playerGet() throws IOException {
        URL urlGet = new URL("http://localhost:8080/getPlayers");

        String readLine = null;

        HttpURLConnection conection = (HttpURLConnection) urlGet.openConnection();

        conection.setRequestMethod("GET");

        BufferedReader in = new BufferedReader( //apparently api responses are given as readable strings, so it's treated as a full server interaction

        new InputStreamReader(conection.getInputStream()));
        StringBuffer response = new StringBuffer();

        while ((readLine = in.readLine()) != null) {
            response.append(readLine);
        }
        in.close();

        System.out.println("Result: " + response.toString());
    }


    public static void playersPost() throws IOException {

        final String postString =
                "{\n" + "\"playerId\": 1000,\r\n" +
                "\"playerName\": \"TESTNAME\"" + "\n}"; //json format string, really finnicky to get to work, even hardcoded

        System.out.println(postString); //making sure the string is actually correct

        URL obj = new URL("http://localhost:8080/createPlayer");

        HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();

        postConnection.setRequestMethod("POST");

        postConnection.setRequestProperty("Content-Type", "application/json"); //the content type header, otherwise the app won't know we're trying to send it a json
        postConnection.setDoOutput(true);

        OutputStream os = postConnection.getOutputStream(); //same as before, apparently it works as a normal connection

        os.write(postString.getBytes());
        os.flush();
        os.close();

        int responseCode = postConnection.getResponseCode(); //and getting the response

        System.out.println("POST Response Code :  " + responseCode);
        System.out.println("POST Response Message : " + postConnection.getResponseMessage());

        if (responseCode == HttpURLConnection.HTTP_OK) { //success, getting the full response

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    postConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {

                response.append(inputLine);

            }
            in.close();

            System.out.println(response.toString());
        }
        else {
            System.out.println("POST NOT WORKED");
        }
    }
}
