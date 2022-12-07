package com.example.myapplication;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    private ArrayList<Event> partyList;

    public RecyclerAdapter(ArrayList<Event> partyList){
        this.partyList = partyList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView  party_name;
        private TextView subtitle;

        public MyViewHolder(final View view){
            super(view);
            party_name = view.findViewById(R.id.event_row_title);
            subtitle = view.findViewById(R.id.event_row_subtitle);
        }


    }

    @NonNull
    @Override
    public RecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.party_row,parent,false);
        return new MyViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.MyViewHolder holder, int position) {
        String name = partyList.get(position).getParty_name();
        int id = partyList.get(position).getID();
        String organizer = partyList.get(position).getOrganizer();
        holder.party_name.setText(name);
        holder.subtitle.setText(organizer);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EventDetailsActivity.class);
                intent.putExtra("id", id);
                v.getContext().startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return partyList.size();
    }
}
