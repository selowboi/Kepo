package com.example.kepo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.kepo.adapter.ListSearchTodoAdapter;
import com.example.kepo.R;
import com.example.kepo.UserSharedPreferences;
import com.example.kepo.databinding.ActivitySearchTodoBinding;
import com.example.kepo.model.ListSearchTodo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SearchTodoActivity extends AppCompatActivity implements ListSearchTodoAdapter.OnItemClickListener{

    private ImageButton ibBack;
    private ImageButton ibSearch;
    private EditText etSearch;
    private CheckBox checkUser;
    private CheckBox checkTodo;
    private RecyclerView rvListTodo;
    private TextView tvNoData;
    private TextView tvResult;
    private TextView tvResultValue;
    private ProgressBar progressBar;
    private ActivitySearchTodoBinding binding;
    private ListSearchTodoAdapter listSearchTodoAdapter;
    private ArrayList<ListSearchTodo> listTodos = new ArrayList<>();
    private UserSharedPreferences pref;
    private static final String BASE_URL = "https://it-division-kepo.herokuapp.com/searchTodos";
    private int filterUser = 0, filterTodo = 0;
    private String user_id, username, todo_id, title, description, last_edited;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_todo);
        getSupportActionBar().hide();

        ibBack = (ImageButton) findViewById(R.id.btn_back);
        ibSearch = (ImageButton) findViewById(R.id.btn_search);
        etSearch = (EditText) findViewById(R.id.et_search_todo);
        checkUser = (CheckBox) findViewById(R.id.check_user);
        checkTodo = (CheckBox) findViewById(R.id.check_todo);
        rvListTodo = (RecyclerView) findViewById(R.id.rv_list_todo);
        tvNoData = (TextView) findViewById(R.id.tv_no_data);
        tvResult = (TextView) findViewById(R.id.tv_result);
        tvResultValue = (TextView) findViewById(R.id.tv_result_value);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        binding.tvNoData.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.GONE);
        binding.tvResult.setVisibility(View.GONE);

        initAdapter();
        pref = new UserSharedPreferences(this);

        checkTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkTodo.isChecked()){
                    filterTodo = 1;
                }
                else {
                    filterTodo = 0;
                }
            }
        });

        checkUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkUser.isChecked()){
                    filterUser = 1;
                }
                else {
                    filterUser = 0;
                }
            }
        });

        ibSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etSearch.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Text field cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else if(filterUser == 0 && filterTodo == 0){
                    Toast.makeText(getApplicationContext(), "You must choose either to search by user, todo, or both", Toast.LENGTH_SHORT).show();
                }
                else {
                    listTodos.clear();
                    binding.tvResult.setVisibility(View.VISIBLE);
                    tvResultValue.setText(" \"" + etSearch.getText().toString() +"\"");
                    loadJSON();
                    etSearch.setText("");
                }
            }
        });

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchTodoActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadJSON(){
        binding.progressBar.setVisibility(View.VISIBLE);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                BASE_URL,
                jsonRequest(etSearch.getText().toString(), filterUser, filterTodo),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            if(jsonArray.length() <= 0){
                                binding.progressBar.setVisibility(View.GONE);
                                binding.tvNoData.setVisibility(View.VISIBLE);
                            }
                            else {
                                binding.progressBar.setVisibility(View.GONE);
                                binding.tvNoData.setVisibility(View.GONE);
                                if(filterTodo == 1 && filterUser == 0){
                                    for (int i = 0; i < jsonArray.length(); i++){

                                        JSONObject jsonObjectData = jsonArray.getJSONObject(i);
                                        String user_id = jsonObjectData.getString("user_id");
                                        String username = jsonObjectData.getString("username");
                                        String todo_id = jsonObjectData.getString("todo_id");
                                        String title = jsonObjectData.getString("title");
                                        String description = jsonObjectData.getString("description");
                                        String last_edited = jsonObjectData.getString("last_edited");

                                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                        try {
                                            Date date = format.parse(last_edited);
                                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm");
                                            last_edited = dateFormat.format(date);
                                        } catch (ParseException ex) {
                                            ex.printStackTrace();
                                        }

                                        if(title.contains(etSearch.getText().toString())){
                                            listSearchTodoAdapter.setOnItemClickListener(SearchTodoActivity.this);

                                            ListSearchTodo lists = new ListSearchTodo();
                                            lists.setUser_id(user_id);
                                            lists.setUsername(username);
                                            lists.setTodo_id(todo_id);
                                            lists.setTitle(title);
                                            lists.setDescription(description);
                                            lists.setLast_edited(last_edited);
                                            listTodos.add(lists);
                                        }

                                    }
                                }
                                else if (filterUser == 1 && filterTodo == 0) {
                                    for (int i = 0; i < jsonArray.length(); i++){

                                        JSONObject jsonObjectData = jsonArray.getJSONObject(i);
                                        String user_id = jsonObjectData.getString("user_id");
                                        String username = jsonObjectData.getString("username");
                                        String todo_id = jsonObjectData.getString("todo_id");
                                        String title = jsonObjectData.getString("title");
                                        String description = jsonObjectData.getString("description");
                                        String last_edited = jsonObjectData.getString("last_edited");

                                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                        try {
                                            Date date = format.parse(last_edited);
                                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm");
                                            last_edited = dateFormat.format(date);
                                        } catch (ParseException ex) {
                                            ex.printStackTrace();
                                        }

                                        if(username.contains(etSearch.getText().toString())){
                                            listSearchTodoAdapter.setOnItemClickListener(SearchTodoActivity.this);

                                            ListSearchTodo lists = new ListSearchTodo();
                                            lists.setUser_id(user_id);
                                            lists.setUsername(username);
                                            lists.setTodo_id(todo_id);
                                            lists.setTitle(title);
                                            lists.setDescription(description);
                                            lists.setLast_edited(last_edited);
                                            listTodos.add(lists);
                                        }

                                    }
                                }
                                else if (filterUser == 1 && filterTodo == 1){
                                    for (int i = 0; i < jsonArray.length(); i++){

                                        JSONObject jsonObjectData = jsonArray.getJSONObject(i);
                                        String user_id = jsonObjectData.getString("user_id");
                                        String username = jsonObjectData.getString("username");
                                        String todo_id = jsonObjectData.getString("todo_id");
                                        String title = jsonObjectData.getString("title");
                                        String description = jsonObjectData.getString("description");
                                        String last_edited = jsonObjectData.getString("last_edited");

                                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                        try {
                                            Date date = format.parse(last_edited);
                                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm");
                                            last_edited = dateFormat.format(date);
                                        } catch (ParseException ex) {
                                            ex.printStackTrace();
                                        }

                                        listSearchTodoAdapter.setOnItemClickListener(SearchTodoActivity.this);

                                        ListSearchTodo lists = new ListSearchTodo();
                                        lists.setUser_id(user_id);
                                        lists.setUsername(username);
                                        lists.setTodo_id(todo_id);
                                        lists.setTitle(title);
                                        lists.setDescription(description);
                                        lists.setLast_edited(last_edited);
                                        listTodos.add(lists);
                                     }
                                }
                            }
                            listSearchTodoAdapter.loadData(listTodos);
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

    private JSONObject jsonRequest(String searchQuery, int filterUser, int filterTodo){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("searchQuery", searchQuery);
            jsonObject.put("filterUser", filterUser);
            jsonObject.put("filterTodo", filterTodo);
            return jsonObject;
        } catch (JSONException e) {
           return null;
        }
    }

    private void initAdapter() {
        listSearchTodoAdapter = new ListSearchTodoAdapter(this);
        binding.rvListTodo.setLayoutManager(new LinearLayoutManager(this));
        binding.rvListTodo.setAdapter(listSearchTodoAdapter);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(SearchTodoActivity.this, DetailTodoActivity.class);
        ListSearchTodo clickedItem = listTodos.get(position);
        pref.searchTodo(clickedItem.getUser_id(), clickedItem.getUsername(), clickedItem.getTodo_id());
        intent.putExtra("EXTRA_TODO", "SEARCH_TODO");
        startActivity(intent);
    }
}