package service.message;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;
import service.core.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Broker extends AbstractActor {

    ArrayList<ActorRef> serviceActors = new ArrayList<>();
    HashMap<Long, ActorRef> clientActorMap = new HashMap<>();
    HashMap<Long, OfferMessage> offerMsgMap = new HashMap<>();

    @Override
    public Receive createReceive() {
        return new ReceiveBuilder()
                .match(ClientInfo.class,
                        msg -> {
                            System.out.println("broker receive a ClientInfo msg: " + msg);
                            ClientMessage clientMessage = new ClientMessage(msg);
                            OfferMessage offerMessage = new OfferMessage(clientMessage.getInfo(), new LinkedList<>());
                            offerMsgMap.put(clientMessage.getToken(), offerMessage);
                            clientActorMap.put(clientMessage.getToken(), sender());

                            for (ActorRef actorRef: serviceActors) {
                                // broker send message to service
                                System.out.println(actorRef.path().address());
                                actorRef.tell(clientMessage, getSelf());
                                new Thread(() -> {
                                    try {
                                        Thread.sleep(2000);
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                    getSelf().tell(new TimeoutMessage(clientMessage.getToken()), getSelf());
                                    Thread.currentThread().interrupt();
                                }).start();
                            }
                        })
                .match(RegisterMessage.class,
                        msg -> {
                            System.out.println("broker receive a register msg: " + msg);
                            serviceActors.add(getSender());
                        })
                .match(TimeoutMessage.class,
                        msg -> {
                            System.out.println("timeout: " + msg.getToken());
                            clientActorMap.get(msg.getToken()).tell(offerMsgMap.get(msg.getToken()), getSelf());
                        })
                .match(QuotationMessage.class,
                        msg-> {
                            System.out.println("Quotation: " + msg.getToken());
                            offerMsgMap.get(msg.getToken()).addQuotation(msg.getQuotation());
                        })
                .build();
    }
}