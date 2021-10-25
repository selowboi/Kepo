package com.example.kepo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.kepo.R;

public class HomeActivity extends AppCompatActivity {

    private TextView tvWelcome;
    private Button btnMyTodo;
    private Button btnSearchTodo;
    private Button btnSearchUser;
    private Button btnProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();

        tvWelcome = (TextView) findViewById(R.id.tv_welcome);
        btnMyTodo = (Button) findViewById(R.id.btn_myTodo);
        btnSearchTodo = (Button) findViewById(R.id.btn_searchTodo);
        btnSearchUser = (Button) findViewById(R.id.btn_searchUser);
        btnProfile = (Button) findViewById(R.id.btn_profile);

        SharedPreferences shared = getSharedPreferences("UserSharedPref", MODE_PRIVATE);
        String name = shared.getString("name", "");

        tvWelcome.setText("Welcome, " + name);

        btnMyTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MyTodoActivity.class);
                startActivity(intent);
            }
        });

        btnSearchTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, SearchTodoActivity.class);
                startActivity(intent);
            }
        });

        btnSearchUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, SearchUserActivity.class);
                startActivity(intent);
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }

}