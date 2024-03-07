package service.broker;

import org.apache.activemq.ActiveMQConnectionFactory;
import service.message.ClientMessage;
import service.message.OfferMessage;
import service.message.QuotationMessage;

import javax.jms.*;
import java.util.concurrent.ConcurrentHashMap;

// 1 consume application message
// 1.1 create offer message partially and add to cache
// 2 create a new thread
// 2.1 wait 3 seconds
// 2.2 consume and combine
// 2.3 send

public class Server {

    // Static instance variable declaration and assignment
    static ConcurrentHashMap<Long, OfferMessage> cache = new ConcurrentHashMap<>();

    public static void main(String[] args) {

        ConnectionFactory factory;
        Connection connection;
        final Session session;

        String host = "localhost";

        if (args.length != 0) {
            host = args[0];
        }

        try {
            int port = 61616;

            factory = new ActiveMQConnectionFactory("failover://tcp://" + host + ":" + port);
            connection = factory.createConnection();
            connection.setClientID("broker");
            session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);

            //------------------APP_QUEUE-------------------
            // Creating the quotation requests queue
            Topic recevAppQueue = session.createTopic("APPLICATIONS");
            // Creating the quotation requests consumer
            MessageConsumer appConsumer = session.createConsumer(recevAppQueue);

            //------------------QUO_QUEUE-------------------
            // create consume quotation topic
            Queue recevQuoQueue = session.createQueue("QUOTATIONS");
            // create quotation consumer
            MessageConsumer quoConsumer = session.createConsumer(recevQuoQueue);


            //------------------OFFER_QUEUE-------------------
            // Creating sending offer queue
            Queue sendQueue = session.createQueue("OFFER");
            // Creating offer message producer
            MessageProducer offerMessageProducer = session.createProducer(sendQueue);


            //------------------ALL_QUEUE - START-------------------
            connection.start();

            // non-stop consuming application topic
            appConsumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message recevAppmessage) {
                    if (recevAppmessage instanceof ObjectMessage) { // check if it is child
                        // It is an Object Message
                        Object content;
                        try {
                            content = ((ObjectMessage) recevAppmessage).getObject();
                        } catch (JMSException e) {
                            throw new RuntimeException(e);
                        }

                        // Check if the content is of type QuotationRequestMessage
                        if (content instanceof ClientMessage) {

                            // create a new thread to consume clientMessage
                            new Thread(() -> {
                                // It is a Quotation Request Message
                                ClientMessage applicationMessage = (ClientMessage) content;
                                long clientId = applicationMessage.getToken();

                                System.out.println("consumed one ClientMessage, id: " + clientId);
                                // 1.1 create offer message partially
                                OfferMessage offerMessage = new OfferMessage(applicationMessage.getInfo());
                                // add to cache
                                cache.put(clientId, offerMessage);

                                // 2.2 consume and combine
                                //------------------Quotation queue processor-------------------

                                try {
                                    quoConsumer.setMessageListener(new MessageListener() {
                                        @Override
                                        public void onMessage(Message recevQuomessage) {
                                            try {
                                                if (recevQuomessage instanceof ObjectMessage) { // check if it is child
                                                    // It is an Object Message
                                                    Object content1 = ((ObjectMessage) recevQuomessage).getObject();

                                                    // Check if the content is of type QuotationRequestMessage
                                                    if (content1 instanceof QuotationMessage) {
                                                        // It is a Quotation Request Message
                                                        QuotationMessage quotationMessage = (QuotationMessage) content1;

                                                        long quotationClientId = quotationMessage.getToken();
                                                        System.out.println("consumed one QuotationMessage, id: " + clientId);
                                                        if (cache.containsKey(quotationClientId)) {
                                                            // combine quotation message with client message
                                                            cache.get(quotationClientId).addQuotation(quotationMessage.getQuotation());
                                                        }
                                                        recevQuomessage.acknowledge();
                                                    }
                                                }
                                            } catch (JMSException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
                                    });
                                } catch (JMSException e) {
                                    throw new RuntimeException(e);
                                }

                                try {
                                    Thread.sleep(3000);
                                    recevAppmessage.acknowledge();
                                    Thread.currentThread().interrupted();
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
                            }).start();
                        }
                    }
                }
            });
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

}

