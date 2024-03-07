package service.broker;

import org.apache.activemq.ActiveMQConnectionFactory;
import service.message.OfferMessage;
import service.message.ApplicationMessage;
import service.message.QuotationMessage;

import javax.jms.*;
import java.util.HashMap;

// 1 consume application message
// 1.1 create offer message partially and add to cache
// 2 create a new thread
// 2.1 wait 3 seconds
// 2.2 consume and combine
// 2.3 send

public class Server {

    // Static instance variable declaration and assignment
    static HashMap<Long, OfferMessage> cache = new HashMap<>();

    public static void main(String[] args) {

        ConnectionFactory factory;
        Connection connection;
        final Session session;

        try {
            String host = "localhost";
            int port = 61616;

            factory = new ActiveMQConnectionFactory("failover://tcp://" + host + ":" + port);
            connection = factory.createConnection();
            connection.setClientID("broker");
            session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);

            //------------------APP_QUEUE-------------------
            // Creating the quotation requests queue
            Topic recevAppQueue = session.createTopic("APP_TOPIC");
            // Creating the quotation requests consumer
            final MessageConsumer appConsumer = session.createConsumer(recevAppQueue);

            //------------------QUO_QUEUE-------------------
            // create consume quotation queue
            Queue recevQuoQueue = session.createQueue("QUO_QUEUE");
            // create quotation consumer
            final MessageConsumer quoConsumer = session.createConsumer(recevQuoQueue);


            //------------------OFFER_QUEUE-------------------
            // Creating sending offer queue
            Queue sendQueue = session.createQueue("OFFER_QUEUE");
            // Creating offer message producer
            final MessageProducer offerMessageProducer = session.createProducer(sendQueue);


            //------------------ALL_QUEUE - START-------------------
            connection.start();

            // non-stop consuming application topic
            while (true) {
                try {
                    final Message recevAppmessage = appConsumer.receive();
                    if (recevAppmessage instanceof ObjectMessage) { // check if it is child
                        // It is an Object Message
                        final Object content = ((ObjectMessage) recevAppmessage).getObject();

                        // Check if the content is of type QuotationRequestMessage
                        if (content instanceof ApplicationMessage) {

                            // create a new thread to consume quotations
                            new Thread() {
                                public void run() {
                                    // It is a Quotation Request Message
                                    ApplicationMessage applicationMessage = (ApplicationMessage) content;
                                    final long clientId = applicationMessage.id;
                                    System.out.println("consumed one ApplicationMessage, id: " + clientId);
                                    // 1.1 create offer message partially
                                    OfferMessage offerMessage = new OfferMessage(clientId, applicationMessage.info);
                                    // add to cache
                                    cache.put(clientId, offerMessage);

                                    // 2 create a new thread
                                    Thread quotationsThread = new Thread() {
                                        public void run() {
                                            // 2.2 consume and combine
                                            //------------------Quotation queue processor-------------------
                                            while (true) {
                                                try {
                                                    Message recevQuomessage = quoConsumer.receive();
                                                    if (recevQuomessage instanceof ObjectMessage) { // check if it is child
                                                        // It is an Object Message
                                                        Object content = ((ObjectMessage) recevQuomessage).getObject();

                                                        // Check if the content is of type QuotationRequestMessage
                                                        if (content instanceof QuotationMessage) {
                                                            // It is a Quotation Request Message
                                                            QuotationMessage quotationMessage = (QuotationMessage) content;

                                                            System.out.println("consumed one QuotationMessage, id: " + clientId);
                                                            if (quotationMessage.id == clientId) {
                                                                if (cache.containsKey(clientId)) {
                                                                    cache.get(clientId).addQuotation(quotationMessage.quotation);
                                                                }
                                                                recevQuomessage.acknowledge();
                                                            }
                                                        }
                                                    }
                                                } catch (JMSException e) {
                                                    throw new RuntimeException(e);
                                                }
                                            }
                                        }
                                    };

                                    try {
                                        quotationsThread.start();
                                        Thread.sleep(3000);
                                        quotationsThread.stop();
                                        recevAppmessage.acknowledge();
                                    } catch (InterruptedException | JMSException e) {
                                        throw new RuntimeException(e);
                                    }


                                    try {
                                        Message offer = session.createObjectMessage(cache.get(clientId));
                                        // send message to offer queue
                                        offerMessageProducer.send(offer);
                                    } catch (JMSException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            }.start();
                        }
                    }
                } catch (JMSException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

}

