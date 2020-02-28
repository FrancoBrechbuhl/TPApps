package com.B3B.farmbros.firebase;

public class NotificacionJSON {
    private String to;
    private CuerpoNotificacionJSON notification;

    public NotificacionJSON(){

    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public CuerpoNotificacionJSON getNotification() {
        return notification;
    }

    public void setNotification(CuerpoNotificacionJSON notification) {
        this.notification = notification;
    }
}
