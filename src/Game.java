import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class Game extends UnicastRemoteObject implements IGame {
    private List<Player> players;
    private int nextId;
    
    protected Game() throws RemoteException {
        this.players = new ArrayList<>();
        this.nextId = 0;
    }

    private static final long serialVersionUID = 1287910455759693773L;

    @Override
    public int register() throws RemoteException {
        Player p = new Player(++this.nextId);
        this.players.add(p);
        System.out.printf("Player added: %s\n", p);
        return p.getId();
    }

    @Override
    public int play(int id) throws RemoteException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int stop(int id) throws RemoteException {
        // TODO Auto-generated method stub
        return 0;
    }
    
}
