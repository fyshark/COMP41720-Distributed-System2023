package service.message;

public class TimeoutMessage {
    private long token;
    public TimeoutMessage(long token) {
        this.token = token;
    }
    public long getToken() {
        return token;
    }
}