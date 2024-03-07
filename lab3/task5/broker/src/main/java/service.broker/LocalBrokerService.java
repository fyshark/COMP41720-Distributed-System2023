package service.broker;

import service.core.BrokerService;
import service.core.ClientInfo;
import service.core.Quotation;
import service.core.QuotationService;

import javax.jmdns.JmDNS;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Implementation of the broker service that uses the Service Registry.
 *
 * @author Rem
 */
@WebService(targetNamespace = "http://core.service/",
        name = "QuotationService",
        serviceName = "QuotationService")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
public class LocalBrokerService implements BrokerService {

    private JmdnsClient jmdnsClient = new JmdnsClient();

    public LocalBrokerService() {
        try {
// Create a JmDNS instance
            JmDNS jmdns = JmDNS.create(InetAddress.getLocalHost());
// Add a service listener
            jmdns.addServiceListener("_quote._tcp.local.", jmdnsClient);
// Wait a bit
            Thread.sleep(1000);
        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @WebMethod
    public LinkedList<Quotation> getQuotations(ClientInfo info) {

        LinkedList<Quotation> quotations = new LinkedList<Quotation>();

//        String auldfellasUrl = "http://auldfellas:9001/quotations";
//        String dodgygeezersUrl = "http://dodgygeezers:9002/quotations";
//        String girlsallowedUrl = "http://girlsallowed:9003/quotations";

//        String[] urls = {
//                auldfellasUrl,
//                dodgygeezersUrl,
//                girlsallowedUrl
//        };

        for (String url : jmdnsClient.urls) {
            URL wsdlUrl = null;
            try {
                wsdlUrl = new URL(url);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            QName serviceName =
                    new QName("http://core.service/", "QuotationService");
            Service service = Service.create(wsdlUrl, serviceName);
            QName portName =
                    new QName("http://core.service/", "QuotationServicePort");
            QuotationService quotationService =
                    service.getPort(portName, QuotationService.class);
            Quotation quotation = quotationService
                    .generateQuotation(info);

            quotations.add(quotation);
        }


//		for (String name : serviceRegistry.list()) {
//			if (name.startsWith("qs-")) {
//				try {
//					QuotationService service = (QuotationService) serviceRegistry.lookup(name);
//					quotations.add(service.generateQuotation(info));
//				} catch(Exception e) {
//					System.out.println("Errors happened in LocalBroker: " + e);
//				}
//			}
//		}

        return quotations;
    }
}
