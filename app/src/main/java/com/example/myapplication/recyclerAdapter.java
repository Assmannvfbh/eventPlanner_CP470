package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.MyViewHolder> {
    private ArrayList<Party> partyList;

    public recyclerAdapter(ArrayList<Party> partyList){
        this.partyList = partyList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView  party_name;

        public MyViewHolder(final View view){
            super(view);
            party_name = view.findViewById(R.id.party_row1);
        }


    }

    @NonNull
    @Override
    public recyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.party_row,parent,false);
        return new MyViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerAdapter.MyViewHolder holder, int position) {
        String name = partyList.get(position).getParty_name();
        holder.party_name.setText(name);
    }

    @Override
    public int getItemCount() {
        return partyList.size();
    }
}
