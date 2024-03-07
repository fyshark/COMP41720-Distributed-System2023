package service;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;
import service.dodgygeezers.DGQService;
import service.core.ClientMessage;
import service.core.Quotation;
import service.core.QuotationMessage;

public class Quoter extends AbstractActor {
    private DGQService service = new DGQService();
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