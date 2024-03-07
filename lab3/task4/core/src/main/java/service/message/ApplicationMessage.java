package service.message;

import java.io.Serializable;
import service.core.ClientInfo;

public class ApplicationMessage implements Serializable {
    public long id;
    public ClientInfo info;

    public ApplicationMessage(long id, ClientInfo info) {
        this.id = id;
        this.info = info;
    }
}
