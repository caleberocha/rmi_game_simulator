import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class Client extends UnicastRemoteObject implements IClient {
    private static final long serialVersionUID = -1937155418084943936L;

    private static volatile int moves = 10;
    private static volatile int currentMove = 0;
    private static volatile IGame game;
    private static volatile int id;
    private static volatile boolean dropped = false;
    private static volatile boolean gameOver = false;

    public Client() throws RemoteException {

    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java Client <server>");
            System.exit(1);
        }
        int port = Constants.PORT.value;
        if (args.length > 1) {
            port = Integer.parseInt(args[1]);
        }

        String client = String.format("rmi://%s:%s/gameclient", "localhost", port);
        String server = String.format("rmi://%s:%s/game", args[0], Constants.PORT.value);

        try {
            LocateRegistry.createRegistry(port);
            System.out.println("RMI registry created.");
        } catch (RemoteException e) {
            System.out.println("RMI registry already exists!");
        }

        try {
            System.setProperty("java.rmi.server.hostname", "localhost");
            System.out.println("Creating callback server");
            Naming.rebind(client, new Client());
            System.out.println("Callback server started");
        } catch (RemoteException | MalformedURLException e) {
            System.err.printf("Error starting callback server! %s", e.getMessage());
            System.exit(1);
        }

        try {
            System.out.println("Entering server");
            game = (IGame) Naming.lookup(server);

            System.out.println("Entering game");
            id = game.register(port);
            if (id < 0) {
                throw new GameStartFailedException(id);
            }

            System.out.printf("Player ID: %d\n", id);
        } catch (Exception e) {
            System.err.printf("Failed to start client. %s: %s\n", e.getClass().getName(), e.getMessage());
            System.exit(2);
        }

        while (true) {
            if (dropped) {
                System.exit(2);
            }
            if (gameOver) {
                System.exit(0);
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void play() throws RemoteException {
        System.out.println("Game started");
        new Thread(() -> {
            while (++currentMove <= moves) {
                try {
                    if (dropped) {
                        System.out.println("Dropped from server! Too bad =(");
                        return;
                    }
                    int played = game.play(id);
                    System.out.printf("Played: %d. Move %d of %d\n", played, currentMove, moves);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Game over! Congrats!");
            try {
                game.stop(id);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            gameOver = true;
        }).start();
    }

    public void drop() throws RemoteException {
        dropped = true;
    }

    public boolean isAlive() throws RemoteException {
        return true;
    }
}
