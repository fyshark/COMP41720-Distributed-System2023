import service.core.Advertiser;
import service.dodgygeezers.DGQService;

import javax.xml.ws.Endpoint;

public class Main {
    public static void main(String[] args) {
        Endpoint.publish("http://0.0.0.0:9002/quotations", new DGQService());
        Advertiser.jmdnsQuotationAdvertise("dodgygeezers", "9002", "dodgygeezers");
    }
}