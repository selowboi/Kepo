package com.example.kepo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
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
import com.example.kepo.adapter.ListTodoAdapter;
import com.example.kepo.R;
import com.example.kepo.UserSharedPreferences;
import com.example.kepo.databinding.ActivityMyTodoBinding;
import com.example.kepo.model.Delete;
import com.example.kepo.model.ListTodo;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyTodoActivity extends AppCompatActivity implements ListTodoAdapter.OnItemClickListener {

    private ActivityMyTodoBinding binding;
    private ListTodoAdapter listTodoAdapter;
    private CoordinatorLayout coordinatorLayout;
    private ImageButton ibBack;
    private ImageButton ibCreate;
    private TextView tvTitle;
    private TextView tvTagline;
    private TextView tvNoData;
    private RecyclerView rvLists;
    private ProgressBar progressBar;
    private ArrayList<ListTodo> result = new ArrayList<>();
    private ArrayList<Delete> todos = new ArrayList<>();
    private UserSharedPreferences pref;
    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_todo);
        getSupportActionBar().hide();

        ibBack = (ImageButton) findViewById(R.id.btn_back);
        ibCreate = (ImageButton) findViewById(R.id.ib_create_todo);
        tvTitle = (TextView) findViewById(R.id.tv_mytodo);
        tvTagline = (TextView) findViewById(R.id.tv_mytodo_tagline);
        tvNoData = (TextView) findViewById(R.id.tv_no_data);
        rvLists = (RecyclerView) findViewById(R.id.rv_list_todo);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        initAdapter();
        SharedPreferences shared = getSharedPreferences("UserSharedPref", MODE_PRIVATE);
        String user_id = shared.getString("user_id", "");

        loadJSON(user_id);

        pref = new UserSharedPreferences(this);

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyTodoActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        ibCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyTodoActivity.this, InsertUpdateActivity.class);
                intent.putExtra("EXTRA_STATUS", "INSERT");
                startActivity(intent);
            }
        });

    }

    private void loadJSON(String user_id){
        binding.tvNoData.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);
        String BASE_URL = "https://it-division-kepo.herokuapp.com/user/"+user_id+"/todo";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                BASE_URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            binding.progressBar.setVisibility(View.GONE);
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
                                binding.tvNoData.setVisibility(View.VISIBLE);
                            }
                            else{
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

                                    listTodoAdapter.setOnItemClickListener(MyTodoActivity.this);
                                    listTodoAdapter.setOnItemCheckListener(MyTodoActivity.this);

                                    Delete delete = new Delete();
                                    delete.setTodo_id(todo_id);
                                    todos.add(delete);

                                    ListTodo list = new ListTodo();
                                    list.setTodo_id(todo_id);
                                    list.setTitle(title);
                                    list.setLast_edited(" " + last_edited);

                                    result.add(list);
                                }

                            }
                            listTodoAdapter.loadDelete(todos);
                            listTodoAdapter.loadData(result);
                        } catch (JSONException e) {
                            binding.progressBar.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        binding.progressBar.setVisibility(View.GONE);
                        error.printStackTrace();
                    }
                }
        );
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }

    private void initAdapter(){
        listTodoAdapter = new ListTodoAdapter(this);
        binding.rvListTodo.setLayoutManager(new LinearLayoutManager(this));
        binding.rvListTodo.setAdapter(listTodoAdapter);
    }



    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(MyTodoActivity.this, DetailTodoActivity.class);
        ListTodo clickedItem = result.get(position);
        pref.save(clickedItem.getTodo_id());
        intent.putExtra("EXTRA_TODO", "MY_TODO");
        startActivity(intent);
    }

    @Override
    public void onItemCheck(int position) {
//        ListTodo checkedItem = todos.get(position);
        Delete checkedItem = todos.get(position);

//        Log.d("RESULT", "Res" + checkedItem);
    }

}