package com.example.kepo;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class UserSharedPreferences {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public UserSharedPreferences(Context context){
        pref = context.getSharedPreferences("UserSharedPref", Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void save (String user_id, String username, String name){
        editor.putString("user_id", user_id);
        editor.putString("username", username);
        editor.putString("name", name);
        editor.apply();
    }

    public void save (String todo_id){
        editor.putString("todo_id", todo_id);
        editor.apply();
    }

    public void search (String user_id, String username, String name){
        editor.putString("search_user_id", user_id);
        editor.putString("search_username", username);
        editor.putString("search_name", name);
        editor.apply();
    }

    public void searchTodo (String user_id, String username, String todo_id){
        editor.putString("todo_user_id", user_id);
        editor.putString("todo_username", username);
        editor.putString("todo_todo_id", todo_id);
        editor.apply();
    }

    public void getTodoID (String todo_id){
        editor.putString("search_todo_id", todo_id);
        editor.apply();
    }

    public String getUserID() {return pref.getString("user_id", "");}

//    public String getName() {
//        return pref.getString("name", "");
//    }

}


