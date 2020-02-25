package com.B3B.farmbros.firebase;

public class JSONFirebase {
    private NotificacionFirebase notificacionFirebase;
    private String to;

    public JSONFirebase(){

    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public NotificacionFirebase getNotificacionFirebase() {
        return notificacionFirebase;
    }

    public void setNotificacionFirebase(NotificacionFirebase notificacionFirebase) {
        this.notificacionFirebase = notificacionFirebase;
    }
}
