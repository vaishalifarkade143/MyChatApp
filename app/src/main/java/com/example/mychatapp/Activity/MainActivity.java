package com.example.mychatapp.Activity;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.mychatapp.Adapter.UserAdapter;
import com.example.mychatapp.R;
import com.example.mychatapp.databinding.ActivityMainBinding;
import com.example.mychatapp.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    //25. hume firebase se jo user hai unka data recycler view par show karvana hai
    FirebaseDatabase database;
    UserAdapter userAdapter;
    ArrayList<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //25*
        database = FirebaseDatabase.getInstance();
        users = new ArrayList<>();

        userAdapter = new UserAdapter(this, users);
        //we have set LinearLayout in main_activity.xml
        //binding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerview.setAdapter(userAdapter);

        //26. hum data get kar rahe firebase se
        database.getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    User user = snapshot1.getValue(User.class);
                    users.add(user);
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    //23.if we click on search or setting
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        int id = item.getItemId();
        {
            if (id == R.id.search) {
                Toast.makeText(this, "search clicked", Toast.LENGTH_SHORT).show();
                return true;
            }
            if (id == R.id.setting) {
                Toast.makeText(this, "Setting clicked", Toast.LENGTH_SHORT).show();
                return true;
            }

            return false;//now create raw_conversation.xml
        }
    }

    //22. to set topmenu on MainActivity
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.topmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}