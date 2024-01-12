package com.example.ufcproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MyEventNumberAdapter extends RecyclerView.Adapter<MyEventNumberAdapter.MyViewEventNumberHolder>
{
    List<String>eventNumberList;
    Context context;

     MyEventNumberAdapter( List<String>eventNumberList, Context context) {
         this.eventNumberList = eventNumberList;
         this.context = context;
    }
    public MyViewEventNumberHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewEventNumberHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.event_number_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewEventNumberHolder holder, int position) {
         int mPosition = position;

         holder.mEvent.setText("UFC "+eventNumberList.get(mPosition));
         holder.imgMap.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 context.startActivity(new Intent(context.getApplicationContext(), MapActivity.class));
             }
         });
         holder.mEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("event",eventNumberList.get(mPosition)); // Put anything what you want

                HomeFragment hm = new HomeFragment();
                hm.setArguments(bundle);

                ((FragmentActivity)context)
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout, hm)
                        .commit();
            }
        });
    }
    @Override
    public int getItemCount() {
        return eventNumberList.size();
    }
    public static class MyViewEventNumberHolder extends RecyclerView.ViewHolder{
         public TextView mEvent;
         public ImageView imgMap;
        public MyViewEventNumberHolder(@NonNull View itemView) {
            super(itemView);
            mEvent =  (TextView)itemView.findViewById(R.id.eventNumber);
            imgMap = (ImageView) itemView.findViewById(R.id.imgMap);
        }
    }
}