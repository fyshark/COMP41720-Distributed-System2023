package service.dodgygeezers;

import org.apache.activemq.ActiveMQConnectionFactory;

import service.core.Quotation;
import service.dodgygeezers.DGQService;
import service.message.ClientMessage;
import service.message.QuotationMessage;

import javax.jms.*;

public class Main {
    private static DGQService service = new DGQService();
    public static void main(String[] args) {
        String host = "localhost";

        if (args.length != 0) {
            host = args[0];
        }

        ConnectionFactory factory =
                new ActiveMQConnectionFactory("failover://tcp://" + host + ":61616");
        try {
            Connection connection = factory.createConnection();
            connection.setClientID("dodgygeezers");
            Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
            Queue queue = session.createQueue("QUOTATIONS");
            Topic topic = session.createTopic("APPLICATIONS");
            MessageConsumer consumer = session.createConsumer(topic);
            MessageProducer producer = session.createProducer(queue);

            connection.start();
            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    try {
                        if (message instanceof ObjectMessage) {
                            ObjectMessage objectMessage = (ObjectMessage) message;
                            if (objectMessage.getObject() instanceof ClientMessage) {
                                ClientMessage request = (ClientMessage) objectMessage.getObject();
                                System.out.println(request.getInfo().name);
                                Quotation quotation = service.generateQuotation(request.getInfo());
                                Message response = session.createObjectMessage(new QuotationMessage(request.getToken(), quotation));
                                producer.send(response);
                                message.acknowledge();
                            } else {
                                // Handle the case where the ObjectMessage does not contain a ClientMessage
                                System.out.println("ObjectMessage of dodgygeezers is not instance of ClientMessage.");
                            }
                        } else {
                            // Handle the case where the message is not an ObjectMessage
                            System.out.println("Message is not instance of ObjectMessage dodgygeezers.");
                        }
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
