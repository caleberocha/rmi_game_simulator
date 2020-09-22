import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IClient extends Remote {
    public void play() throws RemoteException;

    public void drop() throws RemoteException;

    public boolean isAlive() throws RemoteException;
}
