package com.hakankaraotcu.gamedb.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hakankaraotcu.gamedb.R;

import java.util.ArrayList;

public class ProfileListAdapter extends ArrayAdapter<String> {
    private String[] titlesList;
    private ArrayList<Integer> countList;
    private Context context;
    private TextView title, count;

    public ProfileListAdapter(String[] titlesList, ArrayList<Integer> countList, Context context) {
        super(context, R.layout.titles, titlesList);
        this.titlesList = titlesList;
        this.countList = countList;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.titles, null);

        if (view != null) {
            title = view.findViewById(R.id.titles_title);
            count = view.findViewById(R.id.titles_count);

            title.setText(titlesList[position]);
            count.setText(String.valueOf(countList.get(position)));
        }
        return view;
    }
}
