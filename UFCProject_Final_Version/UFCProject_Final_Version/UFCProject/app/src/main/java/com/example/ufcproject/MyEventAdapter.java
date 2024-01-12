package com.example.ufcproject;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MyEventAdapter extends RecyclerView.Adapter<MyEventAdapter.MyViewHolder> {
     List<Event> eventList;
     Context context;

     static MyViewHolder newHolder;

    MyEventAdapter(List<Event>eventList, Context context){
        this.eventList = eventList;
        this.context = context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.mDate.setText("Date : "+ eventList.get(position).getDate());
        holder.mMainOrPrelim.setText( "Main ou Préliminaire : "+ eventList.get(position).getMainOrPrelim());
        holder.mCardPlacement.setText( "Numéro du combat : "+ eventList.get(position).getCardPlacement());
        holder.mFighterOne.setText(eventList.get(position).getFighterOne());
        holder.mFighterTwo.setText(eventList.get(position).getFighterTwo());
        holder.mWinner.setText(eventList.get(position).getWinner());
        holder.mWinner.setTextColor(Color.WHITE);


        holder.mFighterOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeFragment.setFighterChoice(holder.mFighterOne.getText().toString(),
                                                holder.mWinner.getText().toString());

                holder.mFighterTwo.setEnabled(false);
                holder.mFighterOne.setEnabled(false);
                holder.mFighterOne.setBackgroundColor(Color.GREEN);
                holder.mFighterTwo.setBackgroundColor(Color.GRAY);
                newHolder = holder;
            }
        });
        holder.mFighterTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeFragment.setFighterChoice(holder.mFighterTwo.getText().toString(),
                        holder.mWinner.getText().toString());
                holder.mFighterOne.setEnabled(false);
                holder.mFighterTwo.setEnabled(false);
                holder.mFighterTwo.setBackgroundColor(Color.GREEN);
                holder.mFighterOne.setBackgroundColor(Color.GRAY);
                newHolder = holder;
            }
        });

    }

    public static void setWinner(){
        System.out.println("je change la couleur");
        System.out.println(newHolder.mWinner.getText().toString());
        newHolder.mWinner.setTextColor(Color.RED);
    }
    @Override
    public int getItemCount() {
        return eventList.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mDate;
        public TextView mCardPlacement;
        public Button mFighterOne;
        public Button mFighterTwo;
        public TextView mWinner;
        public TextView mMainOrPrelim;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mDate = (TextView)itemView.findViewById(R.id.date);
            mCardPlacement =  (TextView)itemView.findViewById(R.id.card_placement);
            mFighterOne =  (Button)itemView.findViewById(R.id.fighter_1);
            mFighterTwo =  (Button)itemView.findViewById(R.id.fighter_2);
            mWinner =  (TextView)itemView.findViewById(R.id.winner);
            mMainOrPrelim =  (TextView)itemView.findViewById(R.id.main_or_prelim);
        }
    }
}

