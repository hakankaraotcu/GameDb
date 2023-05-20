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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.hakankaraotcu.gamedb.Adapter.GameAdapter;
import com.hakankaraotcu.gamedb.Model.Game;
import com.hakankaraotcu.gamedb.Model.List;
import com.hakankaraotcu.gamedb.Model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateListFragment extends Fragment {

    private EditText editListName, editListDescription;
    private String txtListName, txtListDescription;
    private Button confirmButton;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore mFirestore;
    private LinearLayout linearLayout;

    private GridView mGridView;
    private GameAdapter adapter;
    private ArrayList<Game> selectedGames = new ArrayList<>();
    private String id;

    private CollectionReference listsReference, usersReference, gamesInListsReference;
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

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mFirestore = FirebaseFirestore.getInstance();

        editListName = view.findViewById(R.id.create_list_listName);
        editListDescription = view.findViewById(R.id.create_list_description);
        confirmButton = view.findViewById(R.id.create_list_confirm);

        mGridView = (GridView) view.findViewById(R.id.create_list_gridView);
        linearLayout = (LinearLayout) view.findViewById(R.id.create_list_linearLayout);

        adapter = new GameAdapter(selectedGames, getActivity());
        mGridView.setAdapter(adapter);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddGameToListFragment addGameToListFragment = new AddGameToListFragment();
                getParentFragmentManager().beginTransaction().replace(R.id.user_popular_RelativeLayout, addGameToListFragment, null).addToBackStack(null).commit();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createList();
            }
        });
    }

    public void createList(){
        txtListName = editListName.getText().toString();
        txtListDescription = editListDescription.getText().toString();

        if(txtListName.isEmpty()){
            editListName.setError("List name is required");
            editListName.requestFocus();
            return;
        }
        if(txtListDescription.isEmpty()){
            editListDescription.setError("List description is required");
            editListDescription.requestFocus();
            return;
        }

        listsReference = mFirestore.collection("Lists");
        usersReference = mFirestore.collection("Users");
        usersReference.whereEqualTo("id", mUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    User user = documentSnapshot.toObject(User.class);

                    assert user != null;
                    user.setId(documentSnapshot.getId());
                    List list = new List(txtListName, txtListDescription, selectedGames.size(), user.getId(), user.getUsername());

                    listsReference.add(list).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if(task.isSuccessful()){
                                id = task.getResult().getId();
                            }
                            for(Game game : selectedGames){
                                gamesInListsReference = mFirestore.collection("GamesInLists");

                                Map<String, Object> data = new HashMap<>();
                                data.put("listID", id);
                                data.put("gameID", game.getId());

                                gamesInListsReference.document(id + "_" + game.getId()).set(data);

                                userReference = mFirestore.collection("Users").document(mUser.getUid());
                                userReference.update("listsCount", FieldValue.increment(1));

                                gameReference = mFirestore.collection("Games").document(game.getId());
                                gameReference.update("numberOfLists", FieldValue.increment(1));
                            }
                            getActivity().getSupportFragmentManager().popBackStack();
                        }
                    });
                }
            }
        });
        /*listsReference.getParent().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    User user = documentSnapshot.toObject(User.class);
                    list = new List(txtListName, txtListDescription, games.size(), mUser.getUid());
                    listsReference.add(list).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            getActivity().getSupportFragmentManager().popBackStack();
                        }
                    });
                }
            }
        });

         */
    }

    public ArrayList<Game> getGames(){
        return this.selectedGames;
    }

    public void setGames(Game game){
        this.selectedGames.add(game);
    }
}