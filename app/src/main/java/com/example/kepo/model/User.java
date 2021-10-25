package com.example.kepo.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.kepo.BR;


public class User extends BaseObservable {

    private String user_id;
    private String username;
    private String password;
    private String name;

    @Bindable
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
        notifyPropertyChanged(BR.user_id);
    }

    @Bindable
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        notifyPropertyChanged(BR.username);
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }
}
