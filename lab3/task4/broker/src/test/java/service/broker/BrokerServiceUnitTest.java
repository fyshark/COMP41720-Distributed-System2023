package service.broker;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.BeforeClass;
import org.junit.Test;
import service.auldfellas.Main;
import service.core.ClientInfo;
import service.message.ApplicationMessage;

import javax.jms.*;

import static org.junit.Assert.assertEquals;
import static service.broker.Server.cache;

public class BrokerServiceUnitTest {
    static Session session;

    @BeforeClass
    public static void setup() {
        ConnectionFactory factory = new ActiveMQConnectionFactory("failover://tcp://localhost:61616");
        try {
            Connection connection = factory.createConnection();
            connection.setClientID("test");
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }

        String[] args = {};

        Main.main(args);
        System.out.println("2222");
        Server.main(args);
    }

    @Test
    public void testCache() {
        try {
            Queue recevAppQueue = session.createQueue("APP_QUEUE");
            MessageProducer applicationProducer = session.createProducer(recevAppQueue);

            Message applicationMessage = session.createObjectMessage(new ApplicationMessage(1, new ClientInfo()));
            applicationProducer.send(applicationMessage);

            Thread.sleep(3000);

            assertEquals(3, cache.get(1).quotations.size());
        } catch (JMSException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}