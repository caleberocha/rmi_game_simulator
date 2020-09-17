import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IGame extends Remote {
 public int register() throws RemoteException;
 public int play(int id) throws RemoteException;
 public int stop(int id) throws RemoteException;
}
