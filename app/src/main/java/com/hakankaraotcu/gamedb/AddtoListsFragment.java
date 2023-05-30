package com.hakankaraotcu.gamedb;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.hakankaraotcu.gamedb.General.AppGlobals;
import com.hakankaraotcu.gamedb.Model.List;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddtoListsFragment extends Fragment {
    private ListView listView;
    private String gameID;
    private Button cancelButton, confirmButton;
    private Query mQuery;
    private CollectionReference gamesInListsReference;
    private DocumentReference gameReference, listReference;
    private ArrayList<List> lists;
    private ArrayList<String> listsNames;
    private ArrayList<List> selectedLists;
    private int index = 0;

    public AddtoListsFragment(String gameID) {
        this.gameID = gameID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addto_lists, container, false);

        cancelButton = view.findViewById(R.id.add_to_lists_cancel);
        confirmButton = view.findViewById(R.id.add_to_lists_confirm);
        listView = view.findViewById(R.id.add_to_lists_listView);

        lists = new ArrayList<>();
        listsNames = new ArrayList<>();
        selectedLists = new ArrayList<>();

        mQuery = AppGlobals.db.collection("Lists");
        mQuery.whereEqualTo("userID", AppGlobals.mUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                    List list = documentSnapshot.toObject(List.class);

                    assert list != null;
                    list.setId(documentSnapshot.getId());
                    lists.add(list);
                    listsNames.add(list.getName());
                }
                //adapter = new AddToListsAdapter(lists, getContext());
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_multiple_choice, listsNames);
                listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                listView.setAdapter(arrayAdapter);
            }
        });
        /*addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    return;
                }
                if(value != null){
                    lists.clear();

                    for(DocumentSnapshot documentSnapshot : value.getDocuments()){
                        List list = documentSnapshot.toObject(List.class);

                        assert list != null;
                        list.setId(documentSnapshot.getId());
                        lists.add(list);
                    }
                    adapter = new AddToListsAdapter(lists, getContext());
                    listView.setAdapter(adapter);
                }
            }
        });
        */
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CheckedTextView checkedTextView = (CheckedTextView) view;
                if (checkedTextView.isChecked()) {
                    selectedLists.add(lists.get(i));
                } else {
                    selectedLists.remove(lists.get(i));
                }
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
                for (List selectedList : selectedLists) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("gameID", gameID);
                    data.put("listID", selectedList.getId());

                    gamesInListsReference = AppGlobals.db.collection("GamesInLists");
                    gamesInListsReference.whereEqualTo("gameID", gameID).whereEqualTo("listID", selectedList.getId()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (queryDocumentSnapshots.isEmpty()) {
                                gamesInListsReference.document(selectedList.getId() + "_" + gameID).set(data);

                                gameReference = AppGlobals.db.collection("Games").document(gameID);
                                gameReference.update("numberOfLists", FieldValue.increment(1));

                                listReference = AppGlobals.db.collection("Lists").document(selectedList.getId());
                                listReference.update("numberOfGames", FieldValue.increment(1));
                                index++;
                            } else {
                                Toast.makeText(getActivity(), "Already on the " + selectedList.getName(), Toast.LENGTH_SHORT).show();
                                index++;
                            }
                            if (selectedLists.size() == index) {
                                getActivity().getSupportFragmentManager().popBackStack();
                            }
                        }
                    });
                }
            }
        });
    }
}