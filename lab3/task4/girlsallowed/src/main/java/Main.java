import org.apache.activemq.ActiveMQConnectionFactory;
import service.girlsallowed.GAQService;
import service.core.Quotation;
import service.message.ApplicationMessage;
import service.message.QuotationMessage;

import javax.jms.*;

public class Main {
    static GAQService service = new GAQService();

    public static void main(String[] args) {
        // Initializing and assigning the host variable
        String host = "localhost";
        if (args.length > 0) {
            host = args[0];
        }
        try {
            ConnectionFactory factory = new ActiveMQConnectionFactory("failover://tcp://"+host+":61616");
            Connection connection = factory.createConnection();
            connection.setClientID("girlsallowed");
            Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
            Topic topic = session.createTopic("APPLICATIONS");
            Queue queue = session.createQueue("APPQUEUE");
            MessageConsumer consumer = session.createConsumer(topic);
            MessageProducer producer = session.createProducer(queue);

            connection.start();

            // Begin an infinite loop to always listen to the applications topic
            while(true) {

                try {
                    // Get the next message from the APPLICATION topic
                    Message message = consumer.receive();

                    // Check it is the right type of message
                    if (message instanceof ObjectMessage) {

                        // It’s an Object Message
                        Object content = ((ObjectMessage) message).getObject();

                        if (content instanceof ApplicationMessage) {

                            // It’s a Quotation Request Message
                            ApplicationMessage request = (ApplicationMessage) content;

                            // Generate a quotation and send a quotation response message
                            Quotation quotation = service.generateQuotation(request.info);
                            Message response = session.createObjectMessage(new QuotationMessage(request.id, quotation));
                            producer.send(response);
                        }

                    } else {
                        // Not the right type of message
                        System.out.println("Unknown message type in girlsallowed: " + message.getClass().getCanonicalName());
                    }

                } catch (Exception e) {
                    // Catching the exception
                    System.out.println("Caught error inside girlsallowed while loop: " + e);
                }
            }

        } catch (JMSException e) {
            // Catching the exception
            System.out.println("Caught error for girlsallowed JMS processing messages: " + e);
            e.printStackTrace();
        }


    }
}

