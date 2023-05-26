package com.hakankaraotcu.gamedb.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hakankaraotcu.gamedb.Model.List;
import com.hakankaraotcu.gamedb.R;

import java.util.ArrayList;

public class AddToListsAdapter extends BaseAdapter {
    private TextView listName;
    private Context context;
    private ArrayList<List> lists;

    public AddToListsAdapter(ArrayList<List> lists, Context context){
        this.lists = lists;
        this.context = context;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.add_to_lists_item, null);

        if(v != null){
            listName = v.findViewById(R.id.add_to_lists_listName);
            listName.setText(lists.get(position).getName());
        }
        return v;
    }
}
