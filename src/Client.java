import javax.sound.sampled.Port;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    public static void main(String[] args) {
        String hostName = "localhost";
        int port = 9797;

        try (
                Socket echo = new Socket(hostName, port);
                PrintWriter out = new PrintWriter(echo.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(echo.getInputStream()));
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
                ) {
            String userInput;

            new Thread(() -> {
                String message;
                try{
                    while((message = in.readline()) != null){System.out.println(message);}
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
