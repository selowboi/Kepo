package com.example.kepo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.kepo.R;
import com.example.kepo.UserSharedPreferences;
import com.example.kepo.model.DetailListTodo;
import com.example.kepo.model.DetailUser;
import com.example.kepo.model.ListTodo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailTodoActivity extends AppCompatActivity {

    private TextView tvLastEdited;
    private TextView tvTitle;
    private TextView tvDescription;
    private ImageButton ibBack;
    private ImageButton ibUpdate;
    private ProgressBar progressBar;
    private String username;
    private String todo_id;
    private String title;
    private String description;
    private String last_edited;
    private UserSharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_todo);
        getSupportActionBar().hide();

        tvLastEdited = (TextView) findViewById(R.id.tv_last_edited_value);
        tvTitle = (TextView) findViewById(R.id.tv_detail_title);
        tvDescription = (TextView) findViewById(R.id.tv_detail_description);
        ibBack = (ImageButton) findViewById(R.id.btn_back);
        ibUpdate = (ImageButton) findViewById(R.id.ib_update_todo);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        SharedPreferences preferences = getSharedPreferences("UserSharedPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        String status = getIntent().getStringExtra("EXTRA_TODO");

        Log.d("RESULT", "STATUS " + status);

        if(status.equals("SEARCH_USER")){

            String search_user_id = preferences.getString("search_user_id", "");
//            String search_username = preferences.getString("search_username", "");
            String search_todo_id = preferences.getString("search_todo_id", "");

            String BASE_URL = "https://it-division-kepo.herokuapp.com/user/"+search_user_id+"/todo/"+search_todo_id;
            loadJSON(BASE_URL);
            ibUpdate.setVisibility(View.GONE);
            ibBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DetailTodoActivity.this, DetailUserActivity.class);
                    editor.remove("search_todo_id");
                    editor.apply();
                    startActivity(intent);
                }
            });
        }
        else if(status.equals("MY_TODO")){

            String user_id = preferences.getString("user_id", "");
//            String username = preferences.getString("username", "");
            String todo_id = preferences.getString("todo_id", "");

            String BASE_URL = "https://it-division-kepo.herokuapp.com/user/"+user_id+"/todo/"+todo_id;
            loadJSON(BASE_URL);
            ibUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DetailTodoActivity.this, InsertUpdateActivity.class);
                    intent.putExtra("EXTRA_STATUS", "UPDATE");
                    intent.putExtra("EXTRA_LAST_EDITED", last_edited);
                    intent.putExtra("EXTRA_TITLE", title);
                    intent.putExtra("EXTRA_DESCRIPTION", description);
                    startActivity(intent);
                }
            });

            ibBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DetailTodoActivity.this, MyTodoActivity.class);
                    startActivity(intent);
                }
            });
        }
        else {

            String username = preferences.getString("username", "");
            String todo_user_id = preferences.getString("todo_user_id", "");
            String todo_username = preferences.getString("todo_username", "");
            String todo_todo_id = preferences.getString("todo_todo_id", "");

            String BASE_URL = "https://it-division-kepo.herokuapp.com/user/"+todo_user_id+"/todo/"+todo_todo_id;
            loadJSON(BASE_URL);
            if(username.equals(todo_username)){
                ibUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(DetailTodoActivity.this, InsertUpdateActivity.class);
                        intent.putExtra("EXTRA_STATUS", "SEARCH");
                        intent.putExtra("EXTRA_LAST_EDITED", last_edited);
                        intent.putExtra("EXTRA_TITLE", title);
                        intent.putExtra("EXTRA_DESCRIPTION", description);
                        startActivity(intent);
                    }
                });
            }
            else {
                ibUpdate.setVisibility(View.GONE);
            }
            ibBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DetailTodoActivity.this, SearchTodoActivity.class);
                    editor.remove("todo_user_id");
                    editor.remove("todo_username");
                    editor.remove("todo_todo_id");
                    editor.apply();
                    startActivity(intent);
                }
            });
        }

    }

    private void loadJSON(String BASE_URL){
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                BASE_URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            JSONObject jsonObjectData = response.getJSONObject("data");

                            username = jsonObjectData.getString("username");
                            String todo_id = jsonObjectData.getString("todo_id");
                            title = jsonObjectData.getString("title");
                            description = jsonObjectData.getString("description");
                            last_edited = jsonObjectData.getString("last_edited");

                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                            try {
                                Date date = format.parse(last_edited);
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm");
                                last_edited = dateFormat.format(date);
                            } catch (ParseException ex) {
                                ex.printStackTrace();
                            }

                            tvTitle.setText(title);
                            tvLastEdited.setText(" "+last_edited);
                            tvDescription.setText(description);

                        } catch (JSONException e) {
                            progressBar.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
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

}