package com.hakankaraotcu.gamedb;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.hakankaraotcu.gamedb.Adapter.GameAdapter;
import com.hakankaraotcu.gamedb.General.AppGlobals;
import com.hakankaraotcu.gamedb.Model.Game;
import com.hakankaraotcu.gamedb.Model.List;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditListFragment extends Fragment {
    private List list;
    private DocumentReference listReference;
    private EditText listName, listDescription;
    private String txtListName, txtListDescription;
    private Button cancelButton, confirmButton;
    private GridView gridView;
    private GameAdapter adapter;
    private ArrayList<Game> games;
    private LinearLayout linearLayout;
    private ArrayList<Game> oldGames;
    private CollectionReference gamesInListsReference;
    private DocumentReference gameReference;

    public EditListFragment(List list, ArrayList<Game> games) {
        this.list = list;
        this.games = games;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        oldGames = new ArrayList<>(games);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_list, container, false);

        listName = view.findViewById(R.id.edit_list_name);
        listDescription = view.findViewById(R.id.edit_list_description);
        cancelButton = view.findViewById(R.id.edit_list_cancel);
        confirmButton = view.findViewById(R.id.edit_list_confirm);
        gridView = view.findViewById(R.id.edit_list_gridView);
        linearLayout = view.findViewById(R.id.edit_list_linearLayout);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listName.setText(list.getName());
        listDescription.setText(list.getDescription());

        listReference = AppGlobals.db.collection("Lists").document(list.getId());

        adapter = new GameAdapter(games, getActivity());
        gridView.setAdapter(adapter);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditListInGamesFragment editListInGamesFragment = new EditListInGamesFragment(list, games);
                getParentFragmentManager().beginTransaction().replace(R.id.user_main_RelativeLayout, editListInGamesFragment, "editListInGamesFragment").addToBackStack(null).commit();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Discard Changes");
                builder.setMessage("Are you sure? Changes will be lost.");
                builder.setNegativeButton("CANCEL", null);
                builder.setPositiveButton("DISCARD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!oldGames.equals(games)) {
                            Fragment fragment = getParentFragmentManager().findFragmentByTag("listFragment");
                            ListFragment listFragment = (ListFragment) fragment;

                            listFragment.setGames(oldGames);
                        }
                        getParentFragmentManager().popBackStack();
                    }
                });
                builder.show();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editList();
            }
        });
    }

    private void editList() {
        txtListName = listName.getText().toString();
        txtListDescription = listDescription.getText().toString();

        if (txtListName.isEmpty()) {
            listName.setError("List name is required");
            listName.requestFocus();
            return;
        }
        if (txtListDescription.isEmpty()) {
            listDescription.setError("List description is required");
            listDescription.requestFocus();
            return;
        }

        gamesInListsReference = AppGlobals.db.collection("GamesInLists");
        for (Game game : games) {
            if (!oldGames.contains(game)) {
                Map<String, Object> data = new HashMap<>();
                data.put("listID", list.getId());
                data.put("gameID", game.getId());

                gamesInListsReference.document(list.getId() + "_" + game.getId()).set(data);
                gameReference = AppGlobals.db.collection("Games").document(game.getId());
                gameReference.update("numberOfLists", FieldValue.increment(1));
                listReference.update("numberOfGames", FieldValue.increment(1));
            }
        }
        for (Game game : oldGames) {
            if (!games.contains(game)) {
                Map<String, Object> data = new HashMap<>();
                data.put("listID", list.getId());
                data.put("gameID", game.getId());

                gamesInListsReference.document(list.getId() + "_" + game.getId()).delete();
                gameReference = AppGlobals.db.collection("Games").document(game.getId());
                gameReference.update("numberOfLists", FieldValue.increment(-1));
                listReference.update("numberOfGames", FieldValue.increment(-1));
            }
        }

        listReference.update("name", txtListName, "description", txtListDescription);
        getParentFragmentManager().popBackStack();
    }
}