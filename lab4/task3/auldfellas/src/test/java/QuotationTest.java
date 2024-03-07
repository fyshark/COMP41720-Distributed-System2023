import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;
import service.core.ClientInfo;
import service.message.ClientMessage;
import service.message.QuotationMessage;

import javax.jms.*;

import static org.junit.Assert.assertEquals;

public class QuotationTest {
    @Test
    public void testService() throws Exception {
        Main.main(new String[0]);
        ConnectionFactory factory =
                new ActiveMQConnectionFactory("failover://tcp://localhost:61616");
        Connection connection = factory.createConnection();
        connection.setClientID("auldfellastest");
        Session session =
                connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        Queue queue = session.createQueue("QUOTATIONS");
        Topic topic = session.createTopic("APPLICATIONS");
        MessageConsumer consumer = session.createConsumer(queue);
        MessageProducer producer = session.createProducer(topic);
        connection.start();
        producer.send(
                session.createObjectMessage(
                        new ClientMessage(1L, new ClientInfo("Niki Collier",
                                ClientInfo.FEMALE, 49, 1.5494, 80, false,
                                false))));
        Message message = consumer.receive();
        QuotationMessage quotationMessage =
                (QuotationMessage) ((ObjectMessage) message).getObject();
        System.out.println("token: " + quotationMessage.getToken());
        System.out.println("quotation: " + quotationMessage.getQuotation());
        message.acknowledge();
        assertEquals(1L, quotationMessage.getToken());
    }
}