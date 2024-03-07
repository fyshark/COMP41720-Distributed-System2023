package service;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;
import service.auldfellas.AFQService;
import service.core.ClientMessage;
import service.core.Quotation;
import service.core.QuotationMessage;

public class Quoter extends AbstractActor {
    private AFQService service = new AFQService();
    @Override
    public Receive createReceive() {
        return new ReceiveBuilder()
                .match(ClientMessage.class,
                        msg -> {
                            Quotation quotation = service.generateQuotation(msg.getInfo());
                            getSender().tell(new QuotationMessage(msg.getToken(), quotation), getSelf());
                        })
                .build();
    }
}