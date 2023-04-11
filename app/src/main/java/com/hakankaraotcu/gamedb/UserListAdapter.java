package com.hakankaraotcu.gamedb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ListViewHolder>{
    private ArrayList<Lists> lists;
    private Context context;

    public UserListAdapter(ArrayList<Lists> lists, Context context) {
        this.lists = lists;
        this.context = context;
    }

    @NonNull
    @Override
    public UserListAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.user_lists_item, parent, false);
        return new UserListAdapter.ListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListAdapter.ListViewHolder holder, int position) {
        Lists list = lists.get(position);
        holder.setData(list);
        ImageAdapter imageAdapter;
        imageAdapter = new ImageAdapter(Games.getData(), context);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerView.setAdapter(imageAdapter);
        holder.recyclerView.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    class ListViewHolder extends RecyclerView.ViewHolder{
        TextView listName, count, content;
        RecyclerView recyclerView;

        public ListViewHolder(@NonNull View itemView){
            super(itemView);
            listName = itemView.findViewById(R.id.userLists_item_textViewListName);
            count = itemView.findViewById(R.id.userLists_count);
            content = itemView.findViewById(R.id.userLists_item_textViewContent);
            recyclerView = itemView.findViewById(R.id.userLists_recyclerView);
        }

        public void setData(Lists list){
            this.listName.setText(list.getName());
            this.count.setText(String.valueOf(list.getCount()));
            this.content.setText(list.getDescription());
        }
    }
}
