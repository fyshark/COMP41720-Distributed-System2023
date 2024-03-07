package service.message;

import service.core.ClientInfo;
import service.core.Quotation;

import java.util.LinkedList;
import java.util.List;


public class OfferMessage implements java.io.Serializable {
    private ClientInfo info;
    private LinkedList<Quotation> quotations;

    public OfferMessage(ClientInfo info,
                        LinkedList<Quotation> quotations) {
        this.info = info;
        this.quotations = quotations;
    }

    public OfferMessage(ClientInfo info) {
        this.info = info;
        this.quotations = new LinkedList<>();
    }

    public ClientInfo getInfo() {
        return info;
    }

    public LinkedList<Quotation> getQuotations() {
        return quotations;
    }

    public OfferMessage addQuotation(Quotation newQuotation) {
        this.quotations.add(newQuotation);
        return this;
    }
}
