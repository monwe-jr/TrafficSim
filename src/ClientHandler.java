import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable{

    private Socket client;
    private BufferedReader in;
    private PrintWriter out;

    public ClientHandler(Socket client) {
        this.client = client;
        try {
            in = new BufferedReader(new InputStreamReader(
                    client.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(
                    client.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        if (in != null && out != null) {
            try{
                while(true) {
                    String str = in.readLine();
                    if (str == null || str.trim().equalsIgnoreCase("exit")) {
                        break;
                    }
                    broadcastMessage(str);
                }
                client.close();
                Server.clients.remove(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void broadcastMessage(String str) {

    }
}
