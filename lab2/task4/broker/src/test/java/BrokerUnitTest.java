import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import service.broker.LocalBrokerService;
import service.core.*;

import org.junit.*;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BrokerUnitTest {
    private static BrokerService brokerService;
    private static Registry registry;
    private static LocalBrokerService localBrokerService;

    @BeforeClass
    public static void setup() throws RemoteException {
        try {
            registry = null;

            // Creating a RMI registry
            registry = LocateRegistry.createRegistry(1099);

            localBrokerService = new LocalBrokerService(registry);

            // Export the stub for the broker service object
            brokerService = (BrokerService) UnicastRemoteObject.exportObject(localBrokerService, 0);

            // Register and label the quotation service with the RMI Registry
            registry.bind(Constants.BROKER_SERVICE, brokerService);

        } catch (Exception e) {
            //Print error message of any problem
            System.out.println("Trouble: " + e);
        }
    }
        @Test
        public void connectionTest() throws Exception {
            try {
                BrokerService service = (BrokerService) registry.lookup(Constants.BROKER_SERVICE);
                assertNotNull(service);
            } catch (Exception e) {
                System.out.println("Trouble in Broker: " + e);
            }
        }

        @Test
        public void getQuotationsTest() throws RemoteException {
            ClientInfo clientInfo = new ClientInfo("Niki Collier", ClientInfo.FEMALE, 49, 1.5494, 80, false, false);
            Registry registry = LocateRegistry.getRegistry(1099);
            try {
                // Lookup and retrieve the stub of the broker service object
                BrokerService brokerService = (BrokerService) registry.lookup(Constants.BROKER_SERVICE);

                // Add quotations to the quotationList
                List<Quotation> quotations = brokerService.getQuotations(clientInfo);

                // Assert that the quotationList is empty
                assertTrue(quotations.isEmpty());

            }catch (Exception e) {
                // Print error message if there is a problem
                System.out.println("Trouble: " + e);
            }
        }

    }


