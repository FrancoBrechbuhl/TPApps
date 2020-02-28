package com.B3B.farmbros.firebase;

public class CuerpoNotificacionJSON {
    private String body;
    private String title;
    private String icon;

    public CuerpoNotificacionJSON(){

    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
