package com.example.kepo.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

public class Delete extends BaseObservable {

    private String todo_id;

    @Bindable
    public String getTodo_id() {
        return todo_id;
    }

    public void setTodo_id(String todo_id) {
        this.todo_id = todo_id;
        notifyPropertyChanged(BR.todo_id);
    }
}
