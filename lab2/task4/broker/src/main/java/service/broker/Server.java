package service.broker;

import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import service.core.*;

public class Server {
    public static void main(String[] args) {
        Registry registry = null;
        try {
            // Connect to the RMI Registry - creating the registry will be the
            // responsibility of the broker.
            // Creating the RMI registry

            registry = LocateRegistry.createRegistry(1099);

            // Instantiating a new broker service and passing in the RMI registry
            BrokerService broker = new LocalBrokerService(registry);

            // Exporting the stub for the broker service object
            BrokerService brokerService = (BrokerService) UnicastRemoteObject.exportObject(broker,0);

//            if (args.length == 0) {
//                registry = LocateRegistry.createRegistry(1099);
//            } else {
//                registry = LocateRegistry.getRegistry(args[0], 1099);
//            }
            // Create the Remote Object
//            QuotationService quotationService = (QuotationService) UnicastRemoteObject.exportObject(localService, 0);
            // Register the object with the RMI Registry
            registry.bind(Constants.BROKER_SERVICE, brokerService);

//            registry.bind(Constants.AULD_FELLAS_SERVICE, new AFQService());
//            registry.bind(Constants.GIRLS_ALLOWED_SERVICE, new GAQService());

//            BrokerService ls = (LocalBrokerService) quotationBroker.lookup(Constants.BROKER_SERVICE);
//            ClientInfo client1 = new ClientInfo("Donald Duck", ClientInfo.MALE, 35, 0.45, 1.6, false, false);
//            List<Quotation> quotations = ls.getQuotations(client1);
//            for (Quotation quotation: quotations) {
//                System.out.println(quotation.company);
//                System.out.println(quotation.price);
//                System.out.println(quotation.reference);
//                System.out.println("-----");
//            }
            System.out.println("STOPPING SERVER SHUTDOWN");
            while (true) {
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            System.out.println("Trouble: " + e);
        }
    }
}
