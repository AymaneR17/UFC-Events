package com.example.ufcproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MyFighterAdapter extends RecyclerView.Adapter<MyFighterAdapter.MyViewHolder> {

    List<Event> eventList;
    Context context;

    MyFighterAdapter(List<Event>eventList, Context context){

        this.eventList = eventList;
        this.context = context;
    }
   public void setFilterdList(List<Event> filteredList){
        this.eventList = filteredList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fighter_item, parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.mFighterOne.setText(eventList.get(position).getFighterOne());
        holder.mFighterTwo.setText(eventList.get(position).getFighterTwo());
        holder.mWinner.setText("Gagnant : "+eventList.get(position).getWinner());

    }
    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mFighterOne;
        public TextView mFighterTwo;
        public TextView mWinner;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mFighterOne =  (TextView)itemView.findViewById(R.id.fighter_1);
            mFighterTwo =  (TextView)itemView.findViewById(R.id.fighter_2);
            mWinner =  (TextView)itemView.findViewById(R.id.winner);
        }
    }
}
