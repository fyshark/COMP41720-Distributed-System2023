package service.broker;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.BeforeClass;
import org.junit.Test;
import service.core.ClientInfo;
import service.message.ClientMessage;

import javax.jms.*;

import static org.junit.Assert.assertEquals;
import static service.broker.Server.cache;

public class BrokerServiceUnitTest {
    static Session session;
    static Connection connection;

    @BeforeClass
    public static void setup() {
        ConnectionFactory factory = new ActiveMQConnectionFactory("failover://tcp://localhost:61616");
        try {
            connection = factory.createConnection();
            connection.setClientID("test");
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }

        String[] args = {};

        service.girlsallowed.Main.main(args);
        service.auldfellas.Main.main(args);
        service.dodgygeezers.Main.main(args);
        Server.main(args);
    }

    @Test
    public void testCache() {
        try {
            Topic recevAppQueue = session.createTopic("APPLICATIONS");
            MessageProducer applicationProducer = session.createProducer(recevAppQueue);

            connection.start();

            Message applicationMessage = session.createObjectMessage(new ClientMessage(1, new ClientInfo()));
            applicationProducer.send(applicationMessage);

            Thread.sleep(5000);

            System.out.println("the size is " + cache.size());
            assertEquals(3, cache.get(1L).getQuotations().size());
        } catch (JMSException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}