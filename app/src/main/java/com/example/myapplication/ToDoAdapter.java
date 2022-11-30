package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.icu.text.ConstrainedFieldPosition;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ToDoAdapter extends RecyclerView.Adapter <ToDoAdapter.ViewHolder> {

    private Context context;
    ArrayList<ToDoModel> taskList;
    int duration = 10;

    public ToDoAdapter (EventToDoList context,  ArrayList<ToDoModel> taskList){
        this.context = context;
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public ToDoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        @SuppressLint("ResourceType") View view = layoutInflater.inflate(R.id.task_row, parent, false);
       ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoAdapter.ViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textView ;
        private CheckBox checkBox ;
        private ConstraintLayout taskItem;

        public ViewHolder (@NonNull View itemView){
            super(itemView);
            this.checkBox = itemView.findViewById(R.id.todoCheckBox);
            this.textView = itemView.findViewById(R.id.taskText);
            this.taskItem = itemView.findViewById(R.id.task_row);

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean isDone = ((CheckBox) view).isChecked();
                    if (isDone){
                        taskList.get(getAdapterPosition()).setDone(true);
                    }else{
                        taskList.get(getAdapterPosition()).setDone(false);
                    }
                    notifyDataSetChanged();
                    for (int i =0; i< taskList.size(); i++){
                        Log.d("TAG", taskList.toString());
                    }
                }
            });

            taskItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, String.valueOf(taskList.get(getAdapterPosition())),
                    Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}
