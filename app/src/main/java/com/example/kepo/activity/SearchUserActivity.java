package com.example.kepo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
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
import com.example.kepo.adapter.ListUserAdapter;
import com.example.kepo.R;
import com.example.kepo.UserSharedPreferences;
import com.example.kepo.databinding.ActivitySearchUserBinding;
import com.example.kepo.model.ListUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchUserActivity extends AppCompatActivity implements ListUserAdapter.OnItemClickListener {

    public static final String EXTRA_USER_ID = "user_id";
    public static final String EXTRA_USERNAME = "username";
    public static final String EXTRA_NAME = "name";

    private static final String BASE_URL = "https://it-division-kepo.herokuapp.com/searchUser";

    private ActivitySearchUserBinding binding;
    private ListUserAdapter listUserAdapter;
    private ArrayList<ListUser> listUsers = new ArrayList<>();
    private ImageButton ibBack;
    private ImageButton ibSearch;
    private EditText etSearch;
    private TextView tvResultTitle;
    private TextView tvResult;
    private TextView tvNoData;
    private ProgressBar progressBar;
    private RecyclerView rvListUser;
    private UserSharedPreferences pref;
    private String user_id, username, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_user);
        getSupportActionBar().hide();

        ibBack = (ImageButton) findViewById(R.id.btn_back);
        ibSearch = (ImageButton) findViewById(R.id.btn_search);
        etSearch = (EditText) findViewById(R.id.et_search_user);
        tvResultTitle = (TextView) findViewById(R.id.tv_result);
        tvResult = (TextView) findViewById(R.id.tv_result_value);
        tvNoData = (TextView) findViewById(R.id.tv_no_data);
        rvListUser = (RecyclerView) findViewById(R.id.rv_list_user);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        tvResultTitle.setVisibility(View.GONE);
        binding.tvResult.setVisibility(View.GONE);
        binding.tvNoData.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.GONE);

        initAdapter();
        pref = new UserSharedPreferences(this);

        ibSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etSearch.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Text field cannot be empty", Toast.LENGTH_LONG).show();
                }
                else{
                    listUsers.clear();
                    tvResultTitle.setVisibility(View.VISIBLE);
                    binding.tvResult.setVisibility(View.VISIBLE);
                    tvResult.setText(" \"" + etSearch.getText().toString() +"\"");
                    loadJSON();
                    etSearch.setText("");
                }
            }
        });

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchUserActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadJSON(){
        SharedPreferences shared = getSharedPreferences("UserSharedPref", MODE_PRIVATE);
        String id = shared.getString("user_id", "");
        binding.progressBar.setVisibility(View.VISIBLE);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                BASE_URL,
                jsonRequest(id, etSearch.getText().toString()),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
//                            String status = response.getString("status");
//                            String message = response.getString("message");
//                            String data = response.getString("data");

                            JSONArray jsonArray = response.getJSONArray("data");

                            if(jsonArray.length() <= 0){
                                binding.progressBar.setVisibility(View.GONE);
                                binding.tvNoData.setVisibility(View.VISIBLE);
                            }
                            else {
                                binding.progressBar.setVisibility(View.GONE);
                                binding.tvNoData.setVisibility(View.GONE);
                                for (int i = 0; i < jsonArray.length(); i++){
                                    JSONObject jsonObjectData = jsonArray.getJSONObject(i);

                                    user_id = jsonObjectData.getString("user_id");
                                    username = jsonObjectData.getString("username");
                                     name = jsonObjectData.getString("name");

                                    if(username.contains(etSearch.getText().toString())){
                                        listUserAdapter.setOnItemClickListener(SearchUserActivity.this);
                                        ListUser listUser = new ListUser();
                                        listUser.setSearched_user_id(user_id);
                                        listUser.setSearched_username(username);
                                        listUser.setSearched_name(name);
                                        listUsers.add(listUser);
                                        listUserAdapter.loadData(listUsers);
                                    }
                                }
                            }
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

    private JSONObject jsonRequest(String id, String searchQuery){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", id);
            jsonObject.put("searchQuery", searchQuery);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void initAdapter(){
        listUserAdapter = new ListUserAdapter(this);
        binding.rvListUser.setLayoutManager(new LinearLayoutManager(this));
        binding.rvListUser.setAdapter(listUserAdapter);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(SearchUserActivity.this, DetailUserActivity.class);
        ListUser clickedItem = listUsers.get(position);
        pref.search(clickedItem.getSearched_user_id(), clickedItem.getSearched_username(), clickedItem.getSearched_name());
//        intent.putExtra("EXTRA_SEARCH", "SEARCH");

//        intent.putExtra(EXTRA_USER_ID, clickedItem.getSearched_user_id());
//        intent.putExtra(EXTRA_USERNAME, clickedItem.getSearched_username());
//        intent.putExtra(EXTRA_NAME, clickedItem.getSearched_name());

        startActivity(intent);
    }
}