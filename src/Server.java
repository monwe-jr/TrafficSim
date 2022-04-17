import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server{

    public static List<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(9797);
            while(true) {
                Socket client = server.accept();
                ClientHandler clientHandler = new ClientHandler(client);
                clients.add(clientHandler);
                clientHandler.run();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
