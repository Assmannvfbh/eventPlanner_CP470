package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class partyList extends AppCompatActivity {

    private ArrayList<Party> partylist;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_list);

        recyclerView = findViewById(R.id.recycler_id);

        partylist = new ArrayList<>();

        setPartyInfo();
        setAdapter();
    }


    private void setAdapter() {
        recyclerAdapter adapter = new recyclerAdapter(partylist);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void setPartyInfo() {

        partylist.add(new Party("Adian's 24th birthday"));
        partylist.add(new Party("Amy's 27th birthday"));
        partylist.add(new Party("Adriana's 26th birthday"));
        partylist.add(new Party("Alex's 52th birthday"));
        partylist.add(new Party("Ryan's 23th birthday"));
        partylist.add(new Party("Max's 53th birthday"));
    }
}