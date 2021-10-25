package com.example.kepo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kepo.databinding.ListDetailTodoUserBinding;
import com.example.kepo.model.DetailUser;

import java.util.ArrayList;

public class DetailUserAdapter extends RecyclerView.Adapter<DetailUserAdapter.DetailUserViewHolder>{

    private ArrayList<DetailUser> lists;
    private Context context;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public DetailUserAdapter(Context context){
        this.lists = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public DetailUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ListDetailTodoUserBinding itemLayoutBinding = ListDetailTodoUserBinding.inflate(layoutInflater, parent, false);
        return new DetailUserViewHolder(itemLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailUserViewHolder holder, int position) {
        DetailUser detailUser = lists.get(position);
        holder.itemLayoutBinding.setDetailTodo(detailUser);
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public void loadData (ArrayList<DetailUser> newLists){
        lists.clear();
        lists.addAll(newLists);
        notifyDataSetChanged();
    }

    class DetailUserViewHolder extends RecyclerView.ViewHolder {

        private ListDetailTodoUserBinding itemLayoutBinding;

        public DetailUserViewHolder (@NonNull ListDetailTodoUserBinding itemLayoutBinding) {
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
