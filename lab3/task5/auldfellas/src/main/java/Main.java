import service.auldfellas.AFQService;

import javax.xml.ws.Endpoint;
import service.core.Advertiser;

public class Main {
    public static void main(String[] args) {
        Endpoint.publish("http://0.0.0.0:9001/quotations", new AFQService());
        Advertiser.jmdnsQuotationAdvertise("auldfellas", "9001", "auldfellas");
    }
}

