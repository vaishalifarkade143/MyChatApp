package com.example.mychatapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mychatapp.Activity.ChatActivity;
import com.example.mychatapp.R;
import com.example.mychatapp.databinding.RawConversationBinding;
import com.example.mychatapp.model.User;

import java.util.ArrayList;

//24.
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewholder>
{
    Context context;
    ArrayList<User> userslist;
    //constructor to pass values
    public UserAdapter(Context context,ArrayList<User> userslist)
    {
        this.context = context;
        this.userslist = userslist;
    }
    @NonNull
    @Override
    public UserViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //getting the view of raw_conversation into the MainActivity
        View view = LayoutInflater.from(context).inflate(R.layout.raw_conversation,parent,false);
        return new UserViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewholder holder, int position) {
        User user = userslist.get(position);
        holder.binding.username.setText(user.getName());
        //27.
        Glide.with(context).load(user.getProfileImage())
                .placeholder(R.drawable.avatar)
                .into(holder.binding.profile);

        //28.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("name",user.getName());//passing data
                intent.putExtra("uid",user.getUid());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return userslist.size();
    }

    public class UserViewholder extends RecyclerView.ViewHolder
    {
        RawConversationBinding binding;

        public UserViewholder(@NonNull View itemView) {
            super(itemView);
            binding = RawConversationBinding.bind(itemView);
        }
    }//now go to main_activity.xml
}
