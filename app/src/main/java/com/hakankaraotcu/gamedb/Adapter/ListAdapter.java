package com.hakankaraotcu.gamedb.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hakankaraotcu.gamedb.Model.Game;
import com.hakankaraotcu.gamedb.Model.List;
import com.hakankaraotcu.gamedb.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {
    private ArrayList<List> lists;
    private HashMap<String, ArrayList<Game>> gamesInList;
    private Context context;
    private ImageAdapter imageAdapter;
    private OnItemClickListener listener;

    public ListAdapter(ArrayList<List> lists, HashMap<String, ArrayList<Game>> gamesInList, Context context) {
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
        List list = lists.get(position);
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if(listener != null && position != RecyclerView.NO_POSITION){
                        listener.onItemClick(lists.get(position), position);
                    }
                }
            });
        }

        public void setData(List list){
            this.listName.setText(list.getName());
            this.content.setText(list.getDescription());
            this.userName.setText(list.getUsername());
        }
    }

    public interface OnItemClickListener{
        void onItemClick(List list, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
