package com.example.kepo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kepo.databinding.ListSearchTodoLayoutBinding;
import com.example.kepo.model.ListSearchTodo;

import java.util.ArrayList;

public class ListSearchTodoAdapter extends RecyclerView.Adapter<ListSearchTodoAdapter.ListSearchTodoViewHolder>{

    private ArrayList<ListSearchTodo> lists;
    private Context context;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public ListSearchTodoAdapter(Context context){
        this.lists = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public ListSearchTodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ListSearchTodoLayoutBinding itemLayoutBinding = ListSearchTodoLayoutBinding.inflate(layoutInflater, parent, false);
        return new ListSearchTodoViewHolder(itemLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ListSearchTodoViewHolder holder, int position) {
        ListSearchTodo listTodo = lists.get(position);
        holder.itemLayoutBinding.setListSearchTodo(listTodo);
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public void loadData (ArrayList<ListSearchTodo> newLists){
        lists.clear();
        lists.addAll(newLists);
        notifyDataSetChanged();
    }

    class ListSearchTodoViewHolder extends RecyclerView.ViewHolder {

        private ListSearchTodoLayoutBinding itemLayoutBinding;

        public ListSearchTodoViewHolder (@NonNull ListSearchTodoLayoutBinding itemLayoutBinding) {
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
