import org.apache.activemq.ActiveMQConnectionFactory;
import service.core.ClientInfo;
import service.core.Quotation;
import service.message.ClientMessage;
import service.message.OfferMessage;

import javax.jms.*;
import java.text.NumberFormat;

public class Client {

    public static void main(String[] args) {

        ConnectionFactory factory;
        Connection connection;
        final Session session;

        try {
            String host = "localhost";
            int port = 61616;

            factory = new ActiveMQConnectionFactory("failover://tcp://" + host + ":" + port);
            connection = factory.createConnection();
            connection.setClientID("client");
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            //------------------OFFER_QUEUE-------------------
            // Creating consuming offer queue
            Queue recevQueue = session.createQueue("OFFER");
            // Creating offer message consumer
            MessageConsumer offerMessageConsumer = session.createConsumer(recevQueue);

            //------------------APP_QUEUE-------------------
            // Creating the quotation produce queue
            Topic sendAppQueue = session.createTopic("APPLICATIONS");
            // Creating the quotation requests consumer
            MessageProducer appProducer = session.createProducer(sendAppQueue);

            //------------------ALL_QUEUE - START-------------------
            connection.start();

            offerMessageConsumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    if (message instanceof ObjectMessage) {
                        Object content;
                        try {
                            content = ((ObjectMessage) message).getObject();
                        } catch (JMSException e) {
                            throw new RuntimeException(e);
                        }

                        if (content instanceof OfferMessage) {
                            displayProfile(((OfferMessage) content).getInfo());
                            for (Quotation quotation: ((OfferMessage) content).getQuotations()) {
                                displayQuotation(quotation);
                            }
                        }

                    }
                }
            });

            long token = 0;
            for (ClientInfo clientInfo: clients) {
                ClientMessage cm = new ClientMessage(token++, clientInfo);
                Message clientMessage = session.createObjectMessage(cm);
                appProducer.send(clientMessage);
            }



        } catch (JMSException e) {
            e.printStackTrace();
        }

    }

    /**
     * Display the client info nicely.
     */
    public static void displayProfile(ClientInfo info) {
        System.out.println("|=================================================================================================================|");
        System.out.println("|                                     |                                     |                                     |");
        System.out.println(
                "| Name: " + String.format("%1$-29s", info.name) +
                        " | Gender: " + String.format("%1$-27s", (info.gender == ClientInfo.MALE ? "Male" : "Female")) +
                        " | Age: " + String.format("%1$-30s", info.age) + " |");
        System.out.println(
                "| Weight/Height: " + String.format("%1$-20s", info.weight + "kg/" + info.height + "m") +
                        " | Smoker: " + String.format("%1$-27s", info.smoker ? "YES" : "NO") +
                        " | Medical Problems: " + String.format("%1$-17s", info.medicalIssues ? "YES" : "NO") + " |");
        System.out.println("|                                     |                                     |                                     |");
        System.out.println("|=================================================================================================================|");
    }

    /**
     * Display a quotation nicely - note that the assumption is that the quotation will follow
     * immediately after the profile (so the top of the quotation box is missing).
     */
    public static void displayQuotation(Quotation quotation) {
        System.out.println(
                "| Company: " + String.format("%1$-26s", quotation.company) +
                        " | Reference: " + String.format("%1$-24s", quotation.reference) +
                        " | Price: " + String.format("%1$-28s", NumberFormat.getCurrencyInstance().format(quotation.price)) + " |");
        System.out.println("|=================================================================================================================|");
    }

    /**
     * Test Data
     */
    public static final ClientInfo[] clients = {
            new ClientInfo("Niki Collier", ClientInfo.FEMALE, 49, 1.5494, 80, false, false),
            new ClientInfo("Old Geeza", ClientInfo.MALE, 65, 1.6, 100, true, true),
            new ClientInfo("Hannah Montana", ClientInfo.FEMALE, 21, 1.78, 65, false, false),
            new ClientInfo("Rem Collier", ClientInfo.MALE, 49, 1.8, 120, false, true),
            new ClientInfo("Jim Quinn", ClientInfo.MALE, 55, 1.9, 75, true, false),
            new ClientInfo("Donald Duck", ClientInfo.MALE, 35, 0.45, 1.6, false, false)
    };


}
