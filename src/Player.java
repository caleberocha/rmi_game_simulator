import java.rmi.RemoteException;

public class Player implements IPlayer {
    private int id;

    public Player(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }    

    @Override
    public void start() throws RemoteException {
        // TODO Auto-generated method stub

    }

    @Override
    public void stop() throws RemoteException {
        // TODO Auto-generated method stub

    }

    @Override
    public void cutuca() throws RemoteException {
        // TODO Auto-generated method stub

    }

    @Override
    public String toString() {
        return String.format("Player %d", this.getId());
    }

}
