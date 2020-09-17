import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class Server {
    public static int port = 54321;
    public static String serverHost = "localhost";
    public static void main (String[] args) {
        try {
            LocateRegistry.createRegistry(port);
            System.out.println("RMI registry created.");
        } catch (RemoteException e) {
            System.out.println("RMI registry already exists!");
        }

        try {
            String server = String.format("rmi://%s:%s/game", serverHost, port);
            Game game = new Game();
            System.setProperty("java.rmi.server.hostname", "localhost");
            Naming.rebind(server, game);
            System.out.println("Server started");
        } catch (Exception e) {
            System.out.printf("Error starting server! %s", e.getMessage());
            System.exit(1);
        }
    }
}
