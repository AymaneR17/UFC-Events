package com.example.ufcproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder>{

   private Context context;
    private List<Message> messageList;


    public MessageAdapter(Context context){
        this.context = context;
        messageList = new ArrayList<>();
    }

    public void add(Message message){
        messageList.add(message);
        notifyDataSetChanged();
    }

    public void clear(){
        messageList.clear();
        notifyDataSetChanged();
    }
    @Override
    @NonNull

    public MessageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MessageAdapter.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.message_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Message message = messageList.get(position);
        holder.msg.setText(message.getMessage());
        if(message.getSenderId().equals(FirebaseAuth.getInstance().getUid())){
            holder.main.setBackgroundColor(context.getResources().getColor(R.color.red));
            holder.msg.setTextColor(context.getResources().getColor(R.color.white));
        }else{
            holder.main.setBackgroundColor(context.getResources().getColor(R.color.blue));
            holder.msg.setTextColor(context.getResources().getColor(R.color.white));

        }

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView msg;
        private LinearLayout main;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            msg = itemView.findViewById(R.id.message);
            main = itemView.findViewById(R.id.mainMessageLayout);
        }
    }
}
