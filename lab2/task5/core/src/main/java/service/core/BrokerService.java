package service.core;

import java.rmi.RemoteException;
import java.util.List;
import java.rmi.Remote;

/**
 * Interface for defining the behaviours of the broker service
 * @author Rem
 *
 */
public interface BrokerService extends Remote
{
	public List<Quotation> getQuotations(ClientInfo info) throws java.rmi.RemoteException;
	public void registerService(String name, Remote service) throws RemoteException;
}
