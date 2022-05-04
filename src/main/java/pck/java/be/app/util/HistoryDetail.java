package pck.java.be.app.util;

public class HistoryDetail {
    private String username, time, content, type;

    public HistoryDetail(String username, String time, String content, String type) {
        this.username = username;
        this.time = time;
        this.content = content;
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public String getTime() {
        return time;
    }

    public String getContent() {
        return content;
    }

    public String getType() {
        return type;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setType(String type) {
        this.type = type;
    }
}
