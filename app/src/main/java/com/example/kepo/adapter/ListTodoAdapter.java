package com.example.kepo.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.kepo.activity.MyTodoActivity;
import com.example.kepo.databinding.ListTodoItemLayoutBinding;
import com.example.kepo.model.Delete;
import com.example.kepo.model.ListTodo;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListTodoAdapter extends RecyclerView.Adapter<ListTodoAdapter.ListTodoViewHolder>{

    private ArrayList<ListTodo> lists;
    private ArrayList<Delete> todos;
    private ArrayList<Integer> index;
    private Context context;
    private OnItemClickListener mListener, nListener;
    private CoordinatorLayout coordinatorLayout;
    private CheckBox checkBox;
    private Integer count = 0;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onItemCheck(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public void setOnItemCheckListener(OnItemClickListener listener){
        nListener = listener;
    }

    public void loadDelete (ArrayList<Delete> newTodos){
        todos.clear();
        todos.addAll(newTodos);
        notifyDataSetChanged();
    }

    public void loadData (ArrayList<ListTodo> newLists){
        lists.clear();
        lists.addAll(newLists);
        notifyDataSetChanged();
    }

    public ListTodoAdapter(Context context){
        this.lists = new ArrayList<>();
        this.todos = new ArrayList<>();
        this.index = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public ListTodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ListTodoItemLayoutBinding itemLayoutBinding = ListTodoItemLayoutBinding.inflate(layoutInflater, parent, false);
        return new ListTodoViewHolder(itemLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ListTodoViewHolder holder, int position) {
        ListTodo listTodo = lists.get(position);
        holder.itemLayoutBinding.setListTodo(listTodo);
        holder.itemLayoutBinding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nListener != null){
                    int position = holder.getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        nListener.onItemCheck(position);

                        if(holder.itemLayoutBinding.delete.isChecked()){
                            count++;
                            index.add(position);
                        }
                        else {
                            count--;
                            for(int i = 0; i < index.size(); i++){
                                if (index.get(i) == position){
                                    index.remove(i);
                                }
                            }
                        }
                        Snackbar snackbar = Snackbar
                                .make(holder.itemLayoutBinding.delete, count+" item(s)", Snackbar.LENGTH_INDEFINITE)
                                .setAction("Delete", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(holder.itemLayoutBinding.getRoot().getContext());

                                        alertDialogBuilder.setTitle("Delete Todo");

                                        alertDialogBuilder
                                                .setMessage("Are you sure want to delete all this todos?")
                                                .setCancelable(true)
                                                .setPositiveButton("YES",new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog,int id) {
                                                        todos.clear();
                                                        int idx = 0;
                                                        for(int i = 0; i < index.size(); i++){
                                                            for(int j = 0; j < lists.size(); j++){
                                                                if (index.get(i) == j){
                                                                    idx = index.get(i);
                                                                    Delete delete = new Delete();
                                                                    delete.setTodo_id(lists.get(idx).getTodo_id());
                                                                    todos.add(delete);
                                                                    lists.remove(index.get(i));
                                                                }
                                                            }
                                                        }
                                                        loadJSON(holder);
                                                    }
                                                })
                                                .setNegativeButton("NO",new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                    }
                                                });
                                        AlertDialog alertDialog = alertDialogBuilder.create();
                                        alertDialog.show();
                                        Button nbutton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                                        nbutton.setTextColor(Color.rgb(17, 81, 178));
                                        Button pbutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                                        pbutton.setTextColor(Color.rgb(17, 81, 178));
                                    }
                                })
                                .setActionTextColor(Color.RED);

                       Snackbar snackbar1 = Snackbar.make(holder.itemLayoutBinding.delete, "", 1)
                               .setAction("", new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       snackbar.dismiss();
                                   }
                               });
                       if(count == 0) snackbar1.show();
                       else snackbar.show();

                    }
                }
            }
        });
    }

    private void loadJSON(@NonNull ListTodoViewHolder holder) {
        SharedPreferences sharedPreferences = holder.itemLayoutBinding.getRoot().getContext().getSharedPreferences("UserSharedPref", Context.MODE_PRIVATE);
        String user_id = sharedPreferences.getString("user_id", "");
        String BASE_URL = "https://it-division-kepo.herokuapp.com/user/"+user_id+"/deleteTodo";
        RequestQueue queue = Volley.newRequestQueue(holder.itemLayoutBinding.getRoot().getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                BASE_URL,
                jsonRequest(todos),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");
                            String message = response.getString("message");
                            Toast.makeText(holder.itemLayoutBinding.getRoot().getContext().getApplicationContext(), "Delete todo success", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(context, MyTodoActivity.class);
                            context.startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );
        queue.add(jsonObjectRequest);
    }

    private JSONObject jsonRequest(ArrayList<Delete> todos) {
        try {
            ArrayList<String> lists = new ArrayList<>();
            for (int i = 0; i < todos.size(); i++){
                lists.add(todos.get(i).getTodo_id());
            }
            JSONArray jsonArray = new JSONArray();
            for(int i = 0; i < lists.size(); i++){
                jsonArray.put(lists.get(i));
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("todos", jsonArray);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    class ListTodoViewHolder extends RecyclerView.ViewHolder {

        private ListTodoItemLayoutBinding itemLayoutBinding;

        public ListTodoViewHolder (@NonNull ListTodoItemLayoutBinding itemLayoutBinding) {
            super(itemLayoutBinding.getRoot());
            this.itemLayoutBinding = itemLayoutBinding;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);
                        }
                    }
                }
            });


        }

    }

}
