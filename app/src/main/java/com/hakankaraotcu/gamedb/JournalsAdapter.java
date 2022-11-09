package com.hakankaraotcu.gamedb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class JournalsAdapter extends RecyclerView.Adapter<JournalsAdapter.JournalsHolder> {

    private ArrayList<Journals> journalsList;
    private Context context;

    public JournalsAdapter(ArrayList<Journals> journalsList, Context context) {
        this.journalsList = journalsList;
        this.context = context;
    }

    @NonNull
    @Override
    public JournalsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.journals_item, parent, false);
        return new JournalsHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull JournalsHolder holder, int position) {
        Journals journals = journalsList.get(position);
        holder.setData(journals);
    }

    @Override
    public int getItemCount() {
        return journalsList.size();
    }

    class JournalsHolder extends RecyclerView.ViewHolder{

        TextView journalTitle, journalContent;
        ImageView journalImage;

        public JournalsHolder(@NonNull View itemView){
            super(itemView);
            journalTitle = (TextView) itemView.findViewById(R.id.journals_item_textViewTitle);
            journalContent = (TextView) itemView.findViewById(R.id.journals_item_textViewContent);
            journalImage = (ImageView) itemView.findViewById(R.id.journals_item_imageViewImage);
        }

        public void setData(Journals journals){
            this.journalTitle.setText(journals.getJournalTitle());
            this.journalContent.setText(journals.getJournalContent());
            this.journalImage.setBackgroundResource(journals.getJournalImage());
        }
    }
}
