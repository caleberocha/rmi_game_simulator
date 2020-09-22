import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IGame extends Remote {
    public int register(int port) throws RemoteException;

    public int play(int id) throws RemoteException;

    public int stop(int id) throws RemoteException;
}
