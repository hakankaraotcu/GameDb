package com.hakankaraotcu.gamedb;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

public class CreateListFragment extends Fragment {
    private EditText editListName, editListDescription;
    private String txtListName, txtListDescription;
    private Button cancelButton, confirmButton;
    private LinearLayout linearLayout;
    private GridView mGridView;
    private GameAdapter adapter;
    private ArrayList<Game> selectedGames = new ArrayList<>();
    private String id;
    private CollectionReference listsReference, gamesInListsReference;
    private DocumentReference gameReference, userReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_list, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editListName = view.findViewById(R.id.create_list_listName);
        editListDescription = view.findViewById(R.id.create_list_listDescription);
        cancelButton = view.findViewById(R.id.create_list_cancel);
        confirmButton = view.findViewById(R.id.create_list_confirm);

        mGridView = view.findViewById(R.id.create_list_gridView);
        linearLayout = view.findViewById(R.id.create_list_linearLayout);

        adapter = new GameAdapter(selectedGames, getActivity());
        mGridView.setAdapter(adapter);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddGameToListFragment addGameToListFragment = new AddGameToListFragment();
                getParentFragmentManager().beginTransaction().replace(R.id.user_main_RelativeLayout, addGameToListFragment, null).addToBackStack(null).commit();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStack();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createList();
            }
        });
    }

    public void createList() {
        txtListName = editListName.getText().toString();
        txtListDescription = editListDescription.getText().toString();

        if (txtListName.isEmpty()) {
            editListName.setError("List name is required");
            editListName.requestFocus();
            return;
        }
        if (txtListDescription.isEmpty()) {
            editListDescription.setError("List description is required");
            editListDescription.requestFocus();
            return;
        }

        List list = new List(txtListName, txtListDescription, selectedGames.size(), AppGlobals.currentUser.getId());

        listsReference = AppGlobals.db.collection("Lists");
        listsReference.add(list).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    id = task.getResult().getId();
                }
                for (Game game : selectedGames) {
                    gamesInListsReference = AppGlobals.db.collection("GamesInLists");

                    Map<String, Object> data = new HashMap<>();
                    data.put("listID", id);
                    data.put("gameID", game.getId());

                    gamesInListsReference.document(id + "_" + game.getId()).set(data);

                    gameReference = AppGlobals.db.collection("Games").document(game.getId());
                    gameReference.update("numberOfLists", FieldValue.increment(1));
                }
                userReference = AppGlobals.db.collection("Users").document(AppGlobals.mUser.getUid());
                userReference.update("listsCount", FieldValue.increment(1));

                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    public ArrayList<Game> getGames() {
        return this.selectedGames;
    }

    public void setGames(Game game) {
        this.selectedGames.add(game);
    }
}