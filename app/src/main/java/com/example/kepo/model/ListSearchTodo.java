package com.example.kepo.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.kepo.BR;

public class ListSearchTodo extends BaseObservable {

    private String user_id;
    private String username;
    private String todo_id;
    private String title;
    private String description;
    private String last_edited;

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
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        notifyPropertyChanged(BR.description);
    }

    @Bindable
    public String getTodo_id() {
        return todo_id;
    }

    public void setTodo_id(String todo_id) {
        this.todo_id = todo_id;
        notifyPropertyChanged(BR.todo_id);
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    @Bindable
    public String getLast_edited() {
        return last_edited;
    }

    public void setLast_edited(String last_edited) {
        this.last_edited = last_edited;
        notifyPropertyChanged(BR.last_edited);
    }
}
