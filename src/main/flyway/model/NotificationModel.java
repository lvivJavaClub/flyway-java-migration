package model;

import lombok.Data;
import lombok.ToString;

@Data
public class NotificationModel {
    private Integer version;
    private String body;

    @Override
    public String toString() {
        return "NotificationModel{" +
                "version=" + version +
                ", body='" + body + '\'' +
                '}';
    }
}
