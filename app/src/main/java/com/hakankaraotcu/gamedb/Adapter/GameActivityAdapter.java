package com.hakankaraotcu.gamedb.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hakankaraotcu.gamedb.R;

public class GameActivityAdapter extends ArrayAdapter<String> {
    private String[] titles;
    private int[] images;
    private Context context;
    private TextView title;
    private ImageView image;

    public GameActivityAdapter(String[] titles, int[] images, Context context) {
        super(context, R.layout.game_activity_item, titles);
        this.titles = titles;
        this.images = images;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.game_activity_item, null);

        if (view != null) {
            image = view.findViewById(R.id.add_review_image);
            title = view.findViewById(R.id.add_review_title);

            image.setBackgroundResource(images[position]);
            title.setText(titles[position]);
        }
        return view;
    }
}
