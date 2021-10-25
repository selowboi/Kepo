package com.example.kepo.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.kepo.BR;

public class ListUser extends BaseObservable {

    private String searched_user_id;
    private String searched_username;
    private String searched_name;

    @Bindable
    public String getSearched_user_id() {
        return searched_user_id;
    }

    public void setSearched_user_id(String searched_user_id) {
        this.searched_user_id = searched_user_id;
        notifyPropertyChanged(BR.searched_user_id);
    }

    @Bindable
    public String getSearched_username() {
        return searched_username;
    }

    public void setSearched_username(String searched_username) {
        this.searched_username = searched_username;
        notifyPropertyChanged(BR.searched_username);
    }

    @Bindable
    public String getSearched_name() {
        return searched_name;
    }

    public void setSearched_name(String searched_name) {
        this.searched_name = searched_name;
        notifyPropertyChanged(BR.searched_name);
    }
}
