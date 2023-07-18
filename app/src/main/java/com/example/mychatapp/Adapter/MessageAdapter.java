package com.example.mychatapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mychatapp.R;
import com.example.mychatapp.databinding.ItemReceiveBinding;
import com.example.mychatapp.databinding.ItemSentBinding;
import com.example.mychatapp.model.Message;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

//32.
public class MessageAdapter extends RecyclerView.Adapter{
    Context context;
    ArrayList<Message> messagelist;
    //34*
    final int ITEM_SENT = 1;
    final  int ITEM_REC = 2;

    //constructor
    public MessageAdapter(Context context,ArrayList<Message> messagelist)
    {
        this.context = context;
        this.messagelist = messagelist;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //35.
        if (viewType == ITEM_SENT)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.item_sent,parent,false);
            return new SentViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_receive,parent,false);
            return new ReceiveViewHolder(view);
        }
    }

    //34. two viewHolder hai esiliye method overide karenge viewType ki
    @Override
    public int getItemViewType(int position) {

        // Message class ka object lete hai
        Message message =  messagelist.get(position);
        if (FirebaseAuth.getInstance().getUid().equals(message.getSenderId()))
        {
            return ITEM_SENT;
        }
        else {
            return ITEM_REC;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //36.
        Message message =  messagelist.get(position);

        if (holder.getClass() == SentViewHolder.class)
        {
            SentViewHolder viewHolder = (SentViewHolder) holder;
            viewHolder.binding.message.setText(message.getMessage());
        }
        else {
            ReceiveViewHolder viewHolder = (ReceiveViewHolder) holder;
            viewHolder.binding.message.setText(message.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messagelist.size();
    }

    //33.we have to bind item_sent and item_receive so we have to create two viewHolder
    //33.1
    public class SentViewHolder extends RecyclerView.ViewHolder
    {
        ItemSentBinding binding;
        public SentViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemSentBinding.bind(itemView);
        }
    }
    //33.2
    public class ReceiveViewHolder extends RecyclerView.ViewHolder
    {
        ItemReceiveBinding binding;
        public ReceiveViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemReceiveBinding.bind(itemView);
        }
    }


}
