package service.core;


import java.rmi.Remote;

/**
 * Interface to define the behaviour of a quotation service.
 * 
 * @author Rem
 *
 */
public interface QuotationService extends Remote
{
	public Quotation generateQuotation(ClientInfo info)
	throws java.rmi.RemoteException;
	
}
