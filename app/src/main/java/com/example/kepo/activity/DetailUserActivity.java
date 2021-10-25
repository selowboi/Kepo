package com.example.kepo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.kepo.adapter.DetailUserAdapter;
import com.example.kepo.R;
import com.example.kepo.UserSharedPreferences;
import com.example.kepo.databinding.ActivityDetailUserBinding;
import com.example.kepo.model.DetailUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DetailUserActivity extends AppCompatActivity implements DetailUserAdapter.OnItemClickListener {

    private ImageButton ibBack;
    private TextView tvUsername;
    private TextView tvName;
    private TextView tvTodos;
    private TextView tvNoData;
    private RecyclerView rvListTodo;
    private ProgressBar progressBar;
    private ActivityDetailUserBinding binding;
    private DetailUserAdapter detailUserAdapter;
    private ArrayList<DetailUser> lists = new ArrayList<>();
    private UserSharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_user);
        getSupportActionBar().hide();

        ibBack = (ImageButton) findViewById(R.id.btn_back);
        tvUsername = (TextView) findViewById(R.id.tv_search_name);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvTodos = (TextView) findViewById(R.id.tv_todos);
        tvNoData = (TextView) findViewById(R.id.tv_no_data);
        rvListTodo = (RecyclerView) findViewById(R.id.rv_list_todo);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        progressBar.setVisibility(View.GONE);
        tvNoData.setVisibility(View.GONE);

        SharedPreferences shared = getSharedPreferences("UserSharedPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();

        String user_id = shared.getString("search_user_id", "");
        String username = shared.getString("search_username", "");
        String name = shared.getString("search_name", "");

        tvUsername.setText(username);
        tvName.setText(name);

        initAdapter();

        loadJSON(user_id);
        pref = new UserSharedPreferences(this);

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailUserActivity.this, SearchUserActivity.class);
                editor.remove("search_user_id");
                editor.remove("search_usernaem");
                editor.remove("search_name");
                editor.apply();
                startActivity(intent);
            }
        });

    }

    private void loadJSON(String user_id){
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.tvNoData.setVisibility(View.GONE);
        String BASE_URL = "https://it-division-kepo.herokuapp.com/user/"+user_id+"/todo";
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                BASE_URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");
                            String message = response.getString("message");
                            String data = response.getString("data");

                            JSONObject jsonObjectData = response.getJSONObject("data");

                            String id = jsonObjectData.getString("userId");
                            String name = jsonObjectData.getString("name");
                            String username = jsonObjectData.getString("username");
                            String listTodo = jsonObjectData.getString("listTodo");

                            JSONArray jsonArray = jsonObjectData.getJSONArray("listTodo");

                            if(jsonArray.length() <= 0) {
                                binding.progressBar.setVisibility(View.GONE);
                                binding.tvNoData.setVisibility(View.VISIBLE);
                            }
                            else{
                                binding.progressBar.setVisibility(View.GONE);
                                binding.tvNoData.setVisibility(View.GONE);
                                for(int i = 0; i < jsonArray.length(); i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    String todo_id = jsonObject.getString("todo_id");
                                    String title = jsonObject.getString("title");
                                    String last_edited = jsonObject.getString("last_edited");

                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                    try {
                                        Date date = format.parse(last_edited);
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm");
                                        last_edited = dateFormat.format(date);
                                    } catch (ParseException ex) {
                                        ex.printStackTrace();
                                    }

                                    detailUserAdapter.setOnItemClickListener(DetailUserActivity.this);

                                    DetailUser detailUser = new DetailUser();
                                    detailUser.setUsername(username);
                                    detailUser.setTodo_id(todo_id);
                                    detailUser.setTitle(title);
                                    detailUser.setLast_edited(" " + last_edited);

                                    lists.add(detailUser);
                                }
                                tvTodos.setText("Todos: "+jsonArray.length());
                            }
                            detailUserAdapter.loadData(lists);
                        } catch (JSONException e) {
                            binding.progressBar.setVisibility(View.GONE);
                            binding.tvNoData.setVisibility(View.VISIBLE);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.tvNoData.setVisibility(View.VISIBLE);
                        error.printStackTrace();
                    }
                }
        );
        queue.add(jsonObjectRequest);
    }

    private void initAdapter() {
        detailUserAdapter = new DetailUserAdapter(this);
        binding.rvListTodo.setLayoutManager(new LinearLayoutManager(this));
        binding.rvListTodo.setAdapter(detailUserAdapter);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(DetailUserActivity.this, DetailTodoActivity.class);
        DetailUser clickedItem = lists.get(position);
        pref.getTodoID(clickedItem.getTodo_id());
        intent.putExtra("EXTRA_TODO", "SEARCH_USER");
        startActivity(intent);
    }
}