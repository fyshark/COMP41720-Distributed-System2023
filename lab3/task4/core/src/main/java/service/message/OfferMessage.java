package service.message;

import java.io.Serializable;
import java.util.ArrayList;

import service.core.Quotation;
import service.core.ClientInfo;

public class OfferMessage implements Serializable {

    public ClientInfo clientInfo;

    public ArrayList<Quotation> quotations;

    public long id;

    public OfferMessage(long id, ClientInfo receivedClientInfo) {
        // Assigning a new ArrayList to the quotations variable
        this.quotations = new ArrayList<>();

        // Assigning the received client info to the client info variable
        this.clientInfo = receivedClientInfo;

        // Assigning the received client application message id to the token variable
        this.id = id;
    }

    public void addQuotation(Quotation newQuotation) {
        this.quotations.add(newQuotation);
    }
 }