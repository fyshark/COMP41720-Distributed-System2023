import service.core.Advertiser;
import service.girlsallowed.GAQService;

import javax.xml.ws.Endpoint;

public class Main {
    public static void main(String[] args) {
        Endpoint.publish("http://0.0.0.0:9003/quotations", new GAQService());
        Advertiser.jmdnsQuotationAdvertise("girlsallowed", "9003", "girlsallowed");
    }
}
