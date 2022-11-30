package com.example.myapplication;

public class Party {
    private String party_name;
    private int ID;

    public Party(String party_name, int id) {
        this.party_name = party_name;
        this.ID = id;
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
}
