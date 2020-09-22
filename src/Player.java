import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Player implements IPlayer {
    private int id;
    private PlayerStatus status;
    private String address;
    private int port;
    private long lastPooledTime;

    public Player(int id, String address, int port) {
        this.id = id;
        this.address = address;
        this.port = port;
        this.status = PlayerStatus.JOINED;
        this.lastPooledTime = System.currentTimeMillis();
    }

    public int getId() {
        return this.id;
    }

    public String getAddress() {
        return String.format("%s:%d", this.address, this.port);
    }

    @Override
    public void start() throws RemoteException {
        try {
            System.out.printf("Starting player %s, address %s:%s\n", this.id, this.address, this.port);
            IClient c = (IClient) Naming.lookup(String.format("rmi://%s:%s/gameclient", this.address, this.port));
            c.play();
            this.status = PlayerStatus.PLAYING;
            this.lastPooledTime = System.currentTimeMillis();
        } catch (MalformedURLException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() throws RemoteException {
        try {
            IClient c = (IClient) Naming.lookup(String.format("rmi://%s:%s/gameclient", this.address, this.port));
            c.drop();
            this.setFinished();
        } catch (MalformedURLException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cutuca() throws RemoteException {
        try {
            IClient c = (IClient) Naming.lookup(String.format("rmi://%s:%s/gameclient", this.address, this.port));
            c.isAlive();
            this.lastPooledTime = System.currentTimeMillis();
        } catch (MalformedURLException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    public PlayerStatus getStatus() {
        return this.status;
    }

    public long getLastPooledTime() {
        return lastPooledTime;
    }

    public void setFinished() {
        this.status = PlayerStatus.FINISHED;
        System.out.printf("Player %s finished playing\n", this.id);
    }

    @Override
    public String toString() {
        return String.format("Player %d (%s:%d) is %s", this.getId(), this.address, this.port, this.status);
    }

}
