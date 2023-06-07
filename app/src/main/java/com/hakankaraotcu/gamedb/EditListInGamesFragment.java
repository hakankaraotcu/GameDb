package com.hakankaraotcu.gamedb;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.hakankaraotcu.gamedb.Adapter.SearchAdapter;
import com.hakankaraotcu.gamedb.Model.Game;
import com.hakankaraotcu.gamedb.Model.List;

import java.util.ArrayList;

public class EditListInGamesFragment extends Fragment {
    private RecyclerView recyclerView;
    private SearchAdapter adapter;
    private List list;
    private ArrayList<Game> games;
    private ImageButton addButton;
    private Button backButton;

    public EditListInGamesFragment(List list, ArrayList<Game> games) {
        this.list = list;
        this.games = games;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_list_in_games, container, false);

        backButton = view.findViewById(R.id.edit_list_in_games_backButton);
        addButton = view.findViewById(R.id.edit_list_in_games_add);

        recyclerView = view.findViewById(R.id.edit_list_in_games_recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new SearchAdapter(games, getContext());
        ItemTouchHelper itemTouchHelperCallback = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                games.remove(viewHolder.getAdapterPosition());
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        });
        itemTouchHelperCallback.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStack();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddGameToListFragment addGameToListFragment = new AddGameToListFragment(games);
                getParentFragmentManager().beginTransaction().replace(R.id.user_main_RelativeLayout, addGameToListFragment, null).addToBackStack(null).commit();
            }
        });
    }

    public ArrayList<Game> getGames() {
        return this.games;
    }

    public void setGames(Game game) {
        this.games.add(game);
    }
}