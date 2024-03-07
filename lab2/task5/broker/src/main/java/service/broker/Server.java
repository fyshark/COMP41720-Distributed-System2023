package service.broker;

import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import service.core.*;

public class Server {
    public static void main(String[] args) {
        Registry registry = null;
        LocalBrokerService localService = new LocalBrokerService(registry);
        try {
            // Connect to the RMI Registry - creating the registry will be the
            // responsibility of the broker.
            // Creating the RMI registry

            registry = LocateRegistry.createRegistry(1099);

            // Instantiating a new broker service and passing in the RMI registry
            BrokerService broker = new LocalBrokerService(registry);

            // Exporting the stub for the broker service object
            BrokerService brokerService = (BrokerService) UnicastRemoteObject.exportObject(broker,0);

            registry.bind(Constants.BROKER_SERVICE, brokerService);

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
