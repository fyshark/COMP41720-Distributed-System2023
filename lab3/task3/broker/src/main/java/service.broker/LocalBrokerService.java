package service.broker;

import service.core.BrokerService;
import service.core.ClientInfo;
import service.core.Quotation;
import service.core.QuotationService;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Implementation of the broker service that uses the Service Registry.
 *
 * @author Rem
 */
@WebService(targetNamespace = "http://core.service/",
        name = "BrokerService",
        serviceName = "BrokerService")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
public class LocalBrokerService implements BrokerService {

//    // Declaring the registry variable
//    Registry serviceRegistry;
//x
//    // Constructor for the LocalBrokerService
//    public LocalBrokerService(Registry registry) {
//        this.serviceRegistry = registry;
//    }

    public LocalBrokerService() {
    }

    @WebMethod
    public LinkedList<Quotation> getQuotations(ClientInfo info) {

        LinkedList<Quotation> quotations = new LinkedList<Quotation>();

        String auldfellasUrl = "http://0.0.0.0:9001/quotations";
        String dodgygeezersUrl = "http://0.0.0.0:9002/quotations";
        String girlsallowedUrl = "http://0.0.0.0:9003/quotations";

        String[] urls = {
                auldfellasUrl,
                dodgygeezersUrl,
                girlsallowedUrl
        };

        try{
            for (String url : urls) {
                URL wsdlUrl = null;

                wsdlUrl = new URL(url + "?wsdl");

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
        } catch(Exception e) {
            System.out.println("Error in broker: " + e);
        }

        return quotations;
    }
}
