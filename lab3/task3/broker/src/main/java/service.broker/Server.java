package service.broker;

import javax.xml.ws.Endpoint;

public class Server {
    public static void main(String[] args) {
        // Publish the LocalBrokerService as a web service
        String address = "http://0.0.0.0:9000/broker"; // Using /broker as context
        Endpoint.publish(address, new LocalBrokerService());
        System.out.println("LocalBrokerService is published at: " + address);
    }
}
