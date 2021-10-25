package com.example.kepo.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.kepo.BR;

public class ListTodo extends BaseObservable {

    private String todo_id;
    private String title;
    private String last_edited;

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
