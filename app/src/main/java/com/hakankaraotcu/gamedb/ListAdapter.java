package com.hakankaraotcu.gamedb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {
    private ArrayList<Lists> lists;
    private HashMap<String, ArrayList<Games>> gamesInList;
    private Context context;
    private ImageAdapter imageAdapter;

    public ListAdapter(ArrayList<Lists> lists, HashMap<String, ArrayList<Games>> gamesInList, Context context) {
        this.lists = lists;
        this.gamesInList = gamesInList;
        this.context = context;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.lists_item, parent, false);
        return new ListAdapter.ListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        Lists list = lists.get(position);
        holder.setData(list);
        imageAdapter = new ImageAdapter(gamesInList.get(list.getId()), context);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerView.setAdapter(imageAdapter);
        holder.recyclerView.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    class ListViewHolder extends RecyclerView.ViewHolder{
        TextView listName, userName, content;
        RecyclerView recyclerView;

        public ListViewHolder(@NonNull View itemView){
            super(itemView);
            listName = itemView.findViewById(R.id.lists_item_textViewListName);
            userName = itemView.findViewById(R.id.lists_userName);
            content = itemView.findViewById(R.id.lists_item_textViewContent);
            recyclerView = itemView.findViewById(R.id.list_recyclerView);
        }

        public void setData(Lists list){
            this.listName.setText(list.getName());
            this.content.setText(list.getDescription());
            this.userName.setText(list.getUsername());
        }
    }
}
