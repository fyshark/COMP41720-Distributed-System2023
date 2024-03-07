package service.message;

import service.core.Quotation;

import java.io.Serializable;

public class QuotationMessage implements Serializable {
    public long id;
    public Quotation quotation;

    public QuotationMessage(long id, Quotation quotation) {
        this.id = id;
        this.quotation = quotation;
    }
}
