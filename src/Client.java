import java.rmi.Naming;

public class Client {
    public static int port = 54321;
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Use: java Client <server>");
            System.exit(1);
        }

        try {
            String server = String.format("rmi://%s:%s/game", args[0], port);
            System.out.println("Entering server");
            IGame game = (IGame) Naming.lookup(server);
            System.out.println("Entering game");
            int id = game.register();
            System.out.printf("Player ID: %d\n", id);

        } catch (Exception e) {
            System.out.printf("Failed to start client: %s\n", e.getMessage());
        }
    }
}
