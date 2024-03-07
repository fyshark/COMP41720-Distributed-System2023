package service.core;

import java.util.ArrayList;


public class Application {
    private static int COUNTER = 1000;
    private int id;
    private ClientInfo info;
    private ArrayList<Quotation> quotations;

    public Application(ClientInfo info) {
        this.id = COUNTER++;
        this.info = info;
        this.quotations = new ArrayList<>();
    }

    public void setInfo (ClientInfo info) {
        this.info = info;
    }

    public int getId () {
        return this.id;
    }

    public void setId (int id) {
        this.id = id;
    }

    public ClientInfo getInfo() {
        return this.info;
    }

    public ArrayList<Quotation> getQuotations() {
        return this.quotations;
    }

    public void setQuotations(ArrayList<Quotation> quotations) {
        this.quotations = quotations;
    }

    public void addQuotation(Quotation quotation) {
        this.quotations.add(quotation);
    }

}