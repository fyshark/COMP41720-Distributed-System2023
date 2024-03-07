package service;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;
import service.message.ClientMessage;
import service.core.Quotation;
import service.message.QuotationMessage;
import service.girlsallowed.GAQService;

public class Quoter extends AbstractActor {
    private GAQService service = new GAQService();
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