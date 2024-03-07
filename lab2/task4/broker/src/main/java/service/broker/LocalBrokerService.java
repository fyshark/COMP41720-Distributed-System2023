package service.broker;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.LinkedList;
import java.util.List;

import service.core.*;

/**
 * Implementation of the broker service that uses the Service Registry.
 * 
 * @author Rem
 *
 */
public class LocalBrokerService implements BrokerService, Serializable {

	// Declaring the registry variable
	Registry serviceRegistry;

	// Constructor for the LocalBrokerService
	public LocalBrokerService(Registry registry) {
		this.serviceRegistry = registry;
	}


	public List<Quotation> getQuotations(ClientInfo info) throws RemoteException {


		List<Quotation> quotations = new LinkedList<Quotation>();
		
		for (String name : serviceRegistry.list()) {
			if (name.startsWith("qs-")) {
				try {
					QuotationService service = (QuotationService) serviceRegistry.lookup(name);
					quotations.add(service.generateQuotation(info));
				} catch(Exception e) {
					System.out.println("Errors happened in LocalBroker: " + e);
				}
			}
		}

		return quotations;
	}

}
