import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Game extends UnicastRemoteObject implements IGame {
    private volatile List<Player> players;
    private int nextId;
    private int maxPlayers;

    protected Game(int maxPlayers) throws RemoteException {
        this.players = new ArrayList<>();
        this.nextId = 0;
        this.maxPlayers = maxPlayers;
    }

    private static final long serialVersionUID = 1287910455759693773L;

    @Override
    public int register() throws RemoteException {
        if (this.nextId >= this.maxPlayers) {
            return -1;
        }

        try {
            String address = getClientHost();
            Player p = new Player(++this.nextId, address);
            this.players.add(p);
            System.out.printf("Player added: %s\n", p);
            return p.getId();
        } catch (ServerNotActiveException e) {
            System.out.println("Client not active");
            return -2;
        }
    }

    @Override
    public int play(int id) throws RemoteException {
        Player p;

        try {
            p = this.getPlayerById(id);
        } catch (PlayerNotFoundException e) {
            System.err.println(e);
            return 0;
        }

        System.out.printf("Move from player %d\n", p.getId());

        Random rand = new Random();

        int stopPlayer = rand.nextInt(100);
        if (stopPlayer == 1) {
            p.stop();
            System.out.printf("Player %d dropped\n", p.getId());
            this.players.forEach(pl -> System.out.printf("Player %d, status %s\n", pl.getId(), pl.getStatus()));
            return 0;
        }

        int ms = rand.nextInt(1001) + 500;
        try {
            Thread.sleep(ms);
            return 1;
        } catch (InterruptedException e) {
            return 0;
        }
    }

    @Override
    public int stop(int id) throws RemoteException {
        try {
            Player p = this.getPlayerById(id);
            p.setFinished();
            return 0;
        } catch(PlayerNotFoundException e) {
            System.err.println(e);
            return -1;
        }
    }

    public int players() {
        return this.players.stream().filter(p -> p.getStatus() != PlayerStatus.FINISHED).collect(Collectors.counting())
                .intValue();
    }

    public Player getPlayerById(int id) throws PlayerNotFoundException {
        try {
            return this.players.stream().filter(pl -> pl.getId() == id).collect(Collectors.toList()).get(0);
        } catch(IndexOutOfBoundsException e) {
            throw new PlayerNotFoundException(String.format("Player %d não encontrado", id));
        }
    }

    public void start() {
        this.players.forEach(p -> {
            try {
                p.start();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    public void cutucador() {
        while (true) {
            List<Player> players = this.players.stream().filter(p -> p.getStatus() == PlayerStatus.PLAYING)
                    .collect(Collectors.toList());
            

            players.forEach(p -> {
                if (System.currentTimeMillis() - p.getLastPooledTime() >= 3000) {
                    try {
                        System.out.printf("Cutuca player %d\n", p.getId());
                        p.cutuca();
                    } catch (RemoteException e) {
                        System.out.printf("Player %d offline =(\n", p.getId());
                        p.setFinished();
                    }
                }
            });
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }

}
