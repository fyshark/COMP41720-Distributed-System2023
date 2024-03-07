import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import service.core.Constants;
import service.core.Quotation;
import service.core.QuotationService;
import service.core.ClientInfo;

import service.dodgygeezers.DGQService;
import org.junit.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DodgygeezersUnitTest {
    private static Registry registry;
    private static QuotationService dgqService;

    @BeforeClass
    public static void setup() {
        dgqService = new DGQService();
        
        try {
            registry = LocateRegistry.createRegistry(1099);
            QuotationService quotationService = (QuotationService) UnicastRemoteObject.exportObject(dgqService, 0);
            registry.bind(Constants.DODGY_GEEZERS_SERVICE, quotationService);
        } catch (Exception e) {
            System.out.println("Trouble: " + e);
        }
    }

    @Test
    public void connectionTest() throws Exception {
        try {
            QuotationService service = (QuotationService) registry.lookup(Constants.DODGY_GEEZERS_SERVICE);
            assertNotNull(service);
        } catch (Exception e) {
            System.out.println("Trouble in dodgygeezers: " + e);
        }
    }

    @Test
    public void generateQuotationTest() throws Exception {
        ClientInfo clientInfo = new ClientInfo();
        Object result = dgqService.generateQuotation(clientInfo);
        assertNotNull(dgqService.generateQuotation(clientInfo));
        assertTrue(result instanceof Quotation);
    }
}
