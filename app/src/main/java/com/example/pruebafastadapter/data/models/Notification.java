package com.example.pruebafastadapter.data.models;

public class Notification {
    public String title;
    public String description;
    public String deviceToken;

    public Notification(String title, String description, String deviceToken) {
        this.title = title;
        this.description = description;
        this.deviceToken = deviceToken;
    }

    public Notification uploadDataToApiMessage(String deviceToken){
        return new Notification("Nuevos datos","Se cargaron y/o actualizaron nuevos datos en el servidor.", deviceToken);
    }

    @Override
    public String toString() {
        return "Notification{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", deviceToken='" + deviceToken + '\'' +
                '}';
    }
}
