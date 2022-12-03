package com.example.myapplication;

public class Event {
    private String party_name;
    private String organizer;
    private int ID;
    private String admin;

    public Event(String party_name, int id, String organizer, String admin) {
        this.party_name = party_name;
        this.ID = id;
        this.organizer = organizer;
        this.admin = admin;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getParty_name() {
        return party_name;
    }

    public void setParty_name(String party_name) {
        this.party_name = party_name;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }
}
