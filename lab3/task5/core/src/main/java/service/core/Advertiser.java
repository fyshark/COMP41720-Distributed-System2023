package service.core;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import java.net.InetAddress;

public class Advertiser {
    public static void jmdnsQuotationAdvertise(String host, String port, String name) {
        try {
            String config = "path=http://"+host+":"+port+"/quotations?wsdl";
            JmDNS jmdns = JmDNS.create(InetAddress.getLocalHost());
            // Register a service
            ServiceInfo serviceInfo = ServiceInfo.create("_quote._tcp.local.", name, 1234, config);
            jmdns.registerService(serviceInfo);
            // Wait a bit
            Thread.sleep(2000);
            // Unregister all services
//            jmdns.unregisterAllServices();
        } catch (Exception e) {
            System.out.println("Problem Advertising Service: " + e.getMessage());
            e.printStackTrace();
        }
    }


}
