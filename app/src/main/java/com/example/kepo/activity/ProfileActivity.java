package com.example.kepo.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kepo.R;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvTitle;
    private TextView tvName;
    private TextView tvUsername;
    private TextView tvLogout;
    private ImageButton ibBack;
    private ImageView ivPicture;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().hide();

        tvTitle = (TextView) findViewById(R.id.tv_profile);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvUsername = (TextView) findViewById(R.id.tv_username);
        tvLogout = (TextView) findViewById(R.id.tv_logout);
        ibBack = (ImageButton) findViewById(R.id.btn_back);
        ivPicture = (ImageView) findViewById(R.id.iv_profile_picture);

        SharedPreferences shared = getSharedPreferences("UserSharedPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        String name = shared.getString("name", "");
        String username = shared.getString("username", "");

        tvName.setText(name);
        tvUsername.setText(username);

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ProfileActivity.this);

                alertDialogBuilder.setTitle("Logout");

                alertDialogBuilder
                        .setMessage("Are you sure want to logout?")
                        .setCancelable(true)
                        .setPositiveButton("YES",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                Intent intent = new Intent(ProfileActivity.this, SplashActivity.class);
                                editor.clear();
                                editor.apply();
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("NO",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();

                alertDialog.show();

                Button nbutton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                nbutton.setTextColor(Color.rgb(17, 81, 178));
                Button pbutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                pbutton.setTextColor(Color.rgb(17, 81, 178));
            }
        });
    }
}