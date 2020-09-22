import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class Server {
    public static String serverHost = "localhost";

    public static void main(String[] args) {
        if (args.length < 1) {
            help();
        }
        int maxPlayers = Integer.parseInt(args[0]);

        try {
            LocateRegistry.createRegistry(Constants.PORT.value);
            System.out.println("RMI registry created.");
        } catch (RemoteException e) {
            System.out.println("RMI registry already exists!");
        }

        try {
            String server = String.format("rmi://%s:%s/game", serverHost, Constants.PORT.value);
            Game game = new Game(maxPlayers);
            System.setProperty("java.rmi.server.hostname", "localhost");
            Naming.rebind(server, game);
            System.out.println("Server started");

            while (game.getPlayersCount() < maxPlayers) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }

            new Thread(() -> {
                game.cutucador();
            }).start();

            game.start();

            while (true) {
                if (game.getPlayersCount(PlayerStatus.FINISHED) == game.getPlayersCount()) {
                    System.out.println("Game finished, stopping server");
                    System.exit(0);
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }
        } catch (Exception e) {
            System.out.printf("Error starting server! %s", e.getMessage());
            System.exit(1);
        }
    }

    public static void help() {
        System.out.println("Usage: java Server <max_players>");
        System.exit(1);
    }
}
