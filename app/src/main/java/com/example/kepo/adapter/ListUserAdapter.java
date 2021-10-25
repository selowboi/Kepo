package com.example.kepo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kepo.databinding.ListUserItemLayoutBinding;
import com.example.kepo.model.ListUser;

import java.util.ArrayList;

public class ListUserAdapter extends RecyclerView.Adapter<ListUserAdapter.ListUserViewHolder>{

    private ArrayList<ListUser> listUsers;
    private Context context;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public ListUserAdapter(Context context) {
        this.listUsers = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public ListUserAdapter.ListUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ListUserItemLayoutBinding itemLayoutBinding = ListUserItemLayoutBinding.inflate(layoutInflater, parent, false);
        return new ListUserAdapter.ListUserViewHolder(itemLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ListUserViewHolder holder, int position) {
        ListUser listUser = listUsers.get(position);
        holder.itemLayoutBinding.setListUser(listUser);
    }

    @Override
    public int getItemCount() {
        return listUsers.size();
    }

    public void loadData (ArrayList<ListUser> newListUsers) {
        listUsers.clear();
        listUsers.addAll(newListUsers);
        notifyDataSetChanged();
    }

    class ListUserViewHolder extends RecyclerView.ViewHolder {

        private ListUserItemLayoutBinding itemLayoutBinding;

        public ListUserViewHolder(@NonNull ListUserItemLayoutBinding itemLayoutBinding) {
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
