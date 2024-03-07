package service.message;

import service.core.ClientInfo;

public class ClientMessage implements MySerializable {

    private static long counter = 1000;
    private long token;
    private ClientInfo info;

    public ClientMessage(ClientInfo info) {
        this.token = counter++;
        this.info = info;
    }
    public ClientMessage(long token, ClientInfo info) {
        this.token = token;
        this.info = info;
    }
    public long getToken() {
        return token;
    }
    public ClientInfo getInfo() {
        return info;
    }
}
