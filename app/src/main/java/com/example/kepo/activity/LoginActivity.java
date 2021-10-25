package com.example.kepo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.kepo.databinding.ActivityLoginBinding;
import com.example.kepo.error.Error_1;
import com.example.kepo.error.Error_2;
import com.example.kepo.error.Error_3;
import com.example.kepo.model.User;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private ImageView ivLogo;
    private EditText etUsername;
    private EditText etPassword;
    private TextView tvTagline;
    private Button btnLogin;
    private ProgressBar progressBar;
    private LinearLayout llError;
    private TextView tvErrorTitle;
    private TextView tvErrorMessage;
    private Button btnError;

    private String user_id;
    private String username;
    private String name;

    private ActivityLoginBinding binding;
    private UserSharedPreferences pref;
    private static final String BASE_URL = "https://it-division-kepo.herokuapp.com/login";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        ivLogo = (ImageView) findViewById(R.id.iv_logo_sm);
        etUsername =  (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);
        tvTagline = (TextView) findViewById(R.id.tv_tagline);
        btnLogin = (Button) findViewById(R.id.btn_login);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        progressBar.setVisibility(View.GONE);

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        binding.setUser(new User());
        pref = new UserSharedPreferences(this);
        if(!pref.getUserID().equals("")){
            goToHomeActivity();
        }

    }

    private void login(){
        User user = binding.getUser();
        if(etUsername.getText().toString().equals("") || etPassword.getText().toString().equals("")){
//            ErrorLogin errorLogin = new ErrorLogin();
//            errorLogin.setTvErrorMessage("Please input username and password");
//            errorLogin.show(getSupportFragmentManager(), "USER NOT FOUND");
            Error_2 errorLogin  = new Error_2();
            errorLogin.show(getSupportFragmentManager(), "ERROR_LOGIN");
        }
        else{
            binding.progressBar.setVisibility(View.VISIBLE);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    BASE_URL,
                    jsonRequest(user),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                binding.progressBar.setVisibility(View.GONE);

                                String status = response.getString("status");
                                String message = response.getString("message");

                                if(status.equals("200") && message.equals("Username or password is incorrect")){
//                                    ErrorLogin errorLogin = new ErrorLogin();
//                                    errorLogin.setTvErrorMessage("Please input username and password");
//                                    errorLogin.show(getSupportFragmentManager(), "INCORRECT USERNAME OR PASSWORD");
                                    Error_1 incorectLogin = new Error_1();
                                    incorectLogin.show(getSupportFragmentManager(), "INCORRECT");
                                }
                                else if (status.equals("200") && message.equals("Login success")){
                                    JSONObject jsonObjectData = response.getJSONObject("data");
                                    user_id = jsonObjectData.getString("user_id");
                                    username = jsonObjectData.getString("username");
                                    name = jsonObjectData.getString("name");
                                    pref.save(user_id, username, name);
                                    goToHomeActivity();
                                }
                                else {
//                                    ErrorLogin errorLogin = new ErrorLogin();
//                                    errorLogin.setTvErrorMessage("User not found");
//                                    errorLogin.show(getSupportFragmentManager(), "USER NOT FOUND");

                                    Error_3 notFound = new Error_3();
                                    notFound.show(getSupportFragmentManager(), "NOTFOUND");
                                }

                            } catch (Exception ex) {
                                binding.progressBar.setVisibility(View.GONE);

                                ex.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            binding.progressBar.setVisibility(View.GONE);
//                            ErrorNotFound notFound = new ErrorNotFound();
//                            notFound.show(getSupportFragmentManager(), "NOTFOUND");
                            error.printStackTrace();
                        }
                    }
            );
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(jsonObjectRequest);
        }
    }

    private JSONObject jsonRequest(User user){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", user.getUsername());
            jsonObject.put("password", user.getPassword());
            return jsonObject;
        } catch (Exception ex){
            return null;
        }
    }

    private void goToHomeActivity(){
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

}