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
import com.hakankaraotcu.gamedb.Model.Review;
import com.hakankaraotcu.gamedb.R;

import java.util.ArrayList;
import java.util.HashMap;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ListViewHolder> {
    private ArrayList<List> lists;
    private HashMap<String, ArrayList<Game>> gamesInList;
    private Context context;
    private ImageAdapter imageAdapter;
    private OnItemClickListener listener;

    public UserListAdapter(ArrayList<List> lists, HashMap<String, ArrayList<Game>> gamesInList, Context context) {
        this.lists = lists;
        this.gamesInList = gamesInList;
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

    class ListViewHolder extends RecyclerView.ViewHolder {
        TextView listName, gameCount, content;
        RecyclerView recyclerView;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            listName = itemView.findViewById(R.id.user_lists_item_listName);
            gameCount = itemView.findViewById(R.id.user_lists_item_gameCount);
            content = itemView.findViewById(R.id.user_lists_item_content);
            recyclerView = itemView.findViewById(R.id.user_lists_item_recyclerView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(lists.get(position), gamesInList.get(lists.get(position).getId()), position);
                    }
                }
            });
        }

        public void setData(List list) {
            this.listName.setText(list.getName());
            this.gameCount.setText(String.valueOf(list.getNumberOfGames()));
            this.content.setText(list.getDescription());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(List list, ArrayList<Game> gamesInList, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
