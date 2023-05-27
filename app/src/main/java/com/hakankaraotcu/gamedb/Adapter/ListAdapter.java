package com.hakankaraotcu.gamedb.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.hakankaraotcu.gamedb.General.AppGlobals;
import com.hakankaraotcu.gamedb.Model.Game;
import com.hakankaraotcu.gamedb.Model.List;
import com.hakankaraotcu.gamedb.Model.User;
import com.hakankaraotcu.gamedb.R;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {
    private ArrayList<List> lists;
    private HashMap<String, ArrayList<Game>> gamesInList;
    private Context context;
    private ImageAdapter imageAdapter;
    private OnItemClickListener listener;
    private DocumentReference documentReference;
    private User user;

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
        documentReference = AppGlobals.db.collection("Users").document(list.getUserID());
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    user = documentSnapshot.toObject(User.class);

                    assert user != null;
                    holder.setData(list);
                }
            }
        });
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
        TextView listName, userName, content;
        RecyclerView recyclerView;
        CircularImageView userImage;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            listName = itemView.findViewById(R.id.lists_item_listName);
            userName = itemView.findViewById(R.id.lists_username);
            userImage = itemView.findViewById(R.id.lists_userImage);
            content = itemView.findViewById(R.id.lists_item_content);
            recyclerView = itemView.findViewById(R.id.lists_item_recyclerView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(lists.get(position), position);
                    }
                }
            });
        }

        public void setData(List list) {
            this.listName.setText(list.getName());
            this.content.setText(list.getDescription());
            this.userName.setText(user.getUsername());

            if (user.getAvatar().equals("default")) {
                userImage.setImageResource(R.mipmap.ic_launcher);
            } else {
                Picasso.get().load(user.getAvatar()).resize(24, 24).into(userImage);
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(List list, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
