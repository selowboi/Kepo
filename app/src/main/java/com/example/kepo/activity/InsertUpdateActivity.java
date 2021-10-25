package com.example.kepo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.kepo.R;
import com.example.kepo.UserSharedPreferences;
import com.example.kepo.databinding.ActivityInsertUpdateBinding;
import com.example.kepo.model.DetailListTodo;
import com.example.kepo.model.User;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class InsertUpdateActivity extends AppCompatActivity {

    private ImageButton ibBack;
    private ImageButton ibCheck;
    private TextView tvTitle;
    private TextView tvTagline;
    private TextView tvLength;
    private TextView tvMaxLength;
    private TextView tvErrorMessage;
    private EditText etTitle, etDescription;
    private ProgressBar progressBar;
    private String user_id;
    private String todo_id;
    private UserSharedPreferences pref;
    private ActivityInsertUpdateBinding binding;
    private DetailListTodo detailListTodo;
    private boolean isEmpty = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_insert_update);
        getSupportActionBar().hide();

        ibBack = (ImageButton) findViewById(R.id.btn_back);
        ibCheck = (ImageButton) findViewById(R.id.ib_check);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTagline = (TextView) findViewById(R.id.tv_tagline);
        tvLength = (TextView) findViewById(R.id.tv_str_length);
        tvMaxLength = (TextView) findViewById(R.id.tv_max_length);
        tvErrorMessage = (TextView) findViewById(R.id.tv_error_message);
        etTitle = (EditText) findViewById(R.id.et_title);
        etDescription = (EditText) findViewById(R.id.et_description);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        detailListTodo = new DetailListTodo();
        binding.setDetail(detailListTodo);

        progressBar.setVisibility(View.GONE);
        tvErrorMessage.setVisibility(View.GONE);
        tvLength.setVisibility(View.GONE);
        tvMaxLength.setVisibility(View.GONE);

        process();
    }

    private void process () {

        SharedPreferences sharedPreferences = getSharedPreferences("UserSharedPref", MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", "");
        todo_id = sharedPreferences.getString("todo_id", "");
        pref = new UserSharedPreferences(this);

        String status = getIntent().getStringExtra("EXTRA_STATUS");

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvLength.setText(String.valueOf(etDescription.length()));
                if (etDescription.length() <= 0 || etTitle.length() <= 0){
                    isEmpty = true;
                }
                else {
                    isEmpty = false;
                }
                tvLength.setVisibility(View.VISIBLE);
                tvMaxLength.setVisibility(View.VISIBLE);
                if(etDescription.length() > 100) {
                    tvErrorMessage.setText("Your description exceeded the maximum words");
                    tvErrorMessage.setVisibility(View.VISIBLE);
                }
                else  {
                    tvErrorMessage.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        etDescription.addTextChangedListener(textWatcher);
        etTitle.addTextChangedListener(textWatcher);

        if(status.equals("INSERT")){

            tvTitle.setText("Create Todo");

            ibCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(etTitle.length() <= 0 || etTitle.length() <= 0){
                        tvErrorMessage.setText("Text field must not be empty");
                        tvErrorMessage.setVisibility(View.VISIBLE);
                    }
                    else {
                        tvErrorMessage.setVisibility(View.GONE);
                        String createTitle = etTitle.getText().toString();
                        String createDescription = etDescription.getText().toString();

                        loadJSON(user_id, createTitle, createDescription);
                        goToMyTodoActivity();
                    }
                }
            });

            ibBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToMyTodoActivity();
                }
            });

        }
        else if(status.equals("UPDATE")){

            tvTitle.setText("Update Todo");

//            String last_edited = getIntent().getStringExtra("EXTRA_LAST_EDITED");
            String title = getIntent().getStringExtra("EXTRA_TITLE");
            String description = getIntent().getStringExtra("EXTRA_DESCRIPTION");

            ibCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(etTitle.length() <= 0 || etTitle.length() <= 0){
                        tvErrorMessage.setText("Text field must not be empty");
                        tvErrorMessage.setVisibility(View.VISIBLE);
                    }
                    else {
                        String updateTitle = etTitle.getText().toString();
                        String updateDescription =  etDescription.getText().toString();

                        loadJSON(user_id, todo_id, updateTitle, updateDescription);
                        Intent intent = new Intent(InsertUpdateActivity.this, DetailTodoActivity.class);
                        intent.putExtra("EXTRA_TODO", "MY_TODO");
                        startActivity(intent);
                        finish();
//                        goToDetaiTodoActivity();
                    }
                }
            });

            ibBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(InsertUpdateActivity.this, DetailTodoActivity.class);
                    intent.putExtra("EXTRA_TODO", "MY_TODO");
                    startActivity(intent);
                    finish();
//                    goToDetaiTodoActivity();
                }
            });

        }
        else {

            tvTitle.setText("Update Todo");

//            String last_edited = getIntent().getStringExtra("EXTRA_LAST_EDITED");
            String title = getIntent().getStringExtra("EXTRA_TITLE");
            String description = getIntent().getStringExtra("EXTRA_DESCRIPTION");

            ibCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(etTitle.length() <= 0 || etTitle.length() <= 0){
                        tvErrorMessage.setText("Text field must not be empty");
                        tvErrorMessage.setVisibility(View.VISIBLE);
                    }
                    else {
                        String updateTitle = etTitle.getText().toString();
                        String updateDescription =  etDescription.getText().toString();

                        loadJSON(user_id, todo_id, updateTitle, updateDescription);
                        goToDetaiTodoActivity();
                    }
                }
            });

            ibBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(InsertUpdateActivity.this, SearchTodoActivity.class);
//                    intent.putExtra("EXTRA_TODO", "");
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    private void loadJSON (String user_id, String title, String description){
        Log.d("RESULT", "jsonRequest(todos) " + jsonRequest(title, description));
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(this);
        String BASE_URL = "https://it-division-kepo.herokuapp.com/user/"+user_id+"/todo";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                BASE_URL,
                jsonRequest(title, description),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Todo created successfully", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        error.printStackTrace();
                    }
                }
        );
        Log.d("RESULT", "jsonObjectRequest " +jsonObjectRequest);
        queue.add(jsonObjectRequest);
    }

    private void loadJSON (String user_id, String todo_id, String title, String description){
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(this);
        String BASE_URL = "https://it-division-kepo.herokuapp.com/user/"+user_id+"/todo/"+todo_id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT,
                BASE_URL,
                jsonRequest(title, description),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Update todo success", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        error.printStackTrace();
                    }
                }
        );
        queue.add(jsonObjectRequest);
    }

    private JSONObject jsonRequest(String title, String description){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("title", title);
            jsonObject.put("description", description);
            return jsonObject;
        } catch (JSONException ex){
            ex.printStackTrace();
            return null;
        }
    }

    private void goToDetaiTodoActivity () {
        Intent intent = new Intent(InsertUpdateActivity.this, DetailTodoActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToMyTodoActivity () {
        Intent intent = new Intent(InsertUpdateActivity.this, MyTodoActivity.class);
        startActivity(intent);
        finish();
    }

}