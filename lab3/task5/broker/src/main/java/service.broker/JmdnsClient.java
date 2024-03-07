package service.broker;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class JmdnsClient implements ServiceListener {
    List<String> urls = new ArrayList<>();

    @Override
    public void serviceAdded(ServiceEvent serviceEvent) {
        System.out.println("Service added: " + serviceEvent.getInfo());
    }

    @Override
    public void serviceRemoved(ServiceEvent serviceEvent) {
        System.out.println("Service removed: " + serviceEvent.getInfo());
    }

    @Override
    public void serviceResolved(ServiceEvent serviceEvent) {
        System.out.println("Service resolved: " + serviceEvent.getInfo());

        String path = serviceEvent.getInfo().getPropertyString("path");
        if (path != null) {
            urls.add(path);
        }
//        if (path != null) {
//            try {
//                invokeQuotationService(path);
//            } catch (Exception e) {
//                System.out.println("Problem with service: " + e.getMessage());
//                e.printStackTrace();
//            }
//        }

    }

//    private void invokeQuotationService(String url) throws Exception {
//        URL wsdlUrl = new URL(url);
//        QName serviceName = new QName("http://quote/", "StockPriceService");
//        Service service = Service.create(wsdlUrl, serviceName);
//        QName portName = new QName("http://quote/", "StockPricePort");
//        QuotationService quotationService = service.getPort(portName, QuotationService.class);
//        System.out.println(quotationService.generateQuotation());
//    }

}
