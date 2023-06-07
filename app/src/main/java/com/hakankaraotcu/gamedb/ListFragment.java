package com.hakankaraotcu.gamedb;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.Query;
import com.hakankaraotcu.gamedb.Adapter.BottomSheetAdapter;
import com.hakankaraotcu.gamedb.Adapter.GameAdapter;
import com.hakankaraotcu.gamedb.General.AppGlobals;
import com.hakankaraotcu.gamedb.Model.Game;
import com.hakankaraotcu.gamedb.Model.List;
import com.hakankaraotcu.gamedb.Model.User;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListFragment extends Fragment {
    private GameFragment gameFragment;
    private CircularImageView list_userImage;
    private TextView list_username, list_name;
    private ExpandableTextView list_description;
    private List list;
    private ListView listView;
    private GridView mGridView;
    private GameAdapter adapter;
    private ArrayList<Game> games;
    private DocumentReference userReference, listReference, gameReference;
    private CollectionReference gamesInListsReference;
    private BottomSheetAdapter bottomSheetAdapter;
    private User user;
    private Button backButton, editButton;
    private TextView profile_username;
    private String[] titles = {"Delete", "Edit"};
    private int[] images = {R.drawable.ic_delete, R.drawable.ic_edit};

    public ListFragment(List list, ArrayList<Game> games) {
        this.list = list;
        this.games = games;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        backButton = view.findViewById(R.id.list_backButton);
        editButton = view.findViewById(R.id.list_editButton);
        mGridView = view.findViewById(R.id.list_gridView);
        list_userImage = view.findViewById(R.id.list_userImage);
        list_username = view.findViewById(R.id.list_username);
        list_name = view.findViewById(R.id.list_name);
        list_description = view.findViewById(R.id.expand_text_view);
        profile_username = view.findViewById(R.id.list_usernameToolbar);

        adapter = new GameAdapter(games, getContext());
        mGridView.setAdapter(adapter);
        mGridView.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        gamesInListsReference = AppGlobals.db.collection("GamesInLists");
        userReference = AppGlobals.db.collection("Users").document(list.getUserID());
        listReference = AppGlobals.db.collection("Lists").document(list.getId());

        Fragment fragment = getParentFragmentManager().findFragmentByTag("userListsFragment");

        if (fragment == null) {
            editButton.setVisibility(View.GONE);
        } else {
            editButton.setVisibility(View.VISIBLE);
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStack();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialogTheme);
                View bottomSheetView = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.edit_bottom_sheet, view.findViewById(R.id.editBottomSheetContainer));
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();

                listView = bottomSheetView.findViewById(R.id.edit_bottom_sheet_listView);

                bottomSheetAdapter = new BottomSheetAdapter(titles, images, getContext());
                listView.setAdapter(bottomSheetAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        switch (titles[i]) {
                            case "Delete":
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setTitle("Delete list");
                                builder.setMessage("Are you sure you want to delete this list");
                                builder.setNegativeButton("CANCEL", null);
                                builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        deleteList();
                                        getParentFragmentManager().popBackStack();
                                        bottomSheetDialog.hide();
                                    }
                                });
                                builder.show();
                                break;
                            case "Edit":
                                EditListFragment editListFragment = new EditListFragment(list, games);
                                getParentFragmentManager().beginTransaction().replace(R.id.user_main_RelativeLayout, editListFragment, null).addToBackStack(null).commit();
                                bottomSheetDialog.hide();
                                break;
                        }
                    }
                });
            }
        });

        userReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    user = documentSnapshot.toObject(User.class);

                    assert user != null;
                    profile_username.setText(user.getUsername() + "'s List");
                    list_username.setText(user.getUsername());
                    list_name.setText(list.getName());
                    list_description.setText(list.getDescription());

                    if (user.getAvatar().equals("default")) {
                        list_userImage.setImageResource(R.mipmap.ic_launcher);
                    } else {
                        Picasso.get().load(user.getAvatar()).resize(24, 24).into(list_userImage);
                    }
                }
            }
        });

        list_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileFragment profileFragment = new ProfileFragment(list.getUserID());
                // for guest
                if (getActivity().getLocalClassName().equals("GuestMainActivity")) {
                    getParentFragmentManager().beginTransaction().replace(R.id.guest_main_RelativeLayout, profileFragment, null).addToBackStack(null).commit();
                }
                // for user
                if (getActivity().getLocalClassName().equals("UserMainActivity")) {
                    getParentFragmentManager().beginTransaction().replace(R.id.user_main_RelativeLayout, profileFragment, null).addToBackStack(null).commit();
                }
            }
        });

        list_userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileFragment profileFragment = new ProfileFragment(list.getUserID());
                // for guest
                if (getActivity().getLocalClassName().equals("GuestMainActivity")) {
                    getParentFragmentManager().beginTransaction().replace(R.id.guest_main_RelativeLayout, profileFragment, null).addToBackStack(null).commit();
                }
                // for user
                if (getActivity().getLocalClassName().equals("UserMainActivity")) {
                    getParentFragmentManager().beginTransaction().replace(R.id.user_main_RelativeLayout, profileFragment, null).addToBackStack(null).commit();
                }
            }
        });

        gameFragment = new GameFragment();

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle args = new Bundle();
                args.putString("id", games.get(i).getId());
                gameFragment.setArguments(args);
                // for guest
                if (getActivity().getLocalClassName().equals("GuestMainActivity")) {
                    getParentFragmentManager().beginTransaction().replace(R.id.guest_main_RelativeLayout, gameFragment, null).addToBackStack(null).commit();
                }
                // for user
                if (getActivity().getLocalClassName().equals("UserMainActivity")) {
                    getParentFragmentManager().beginTransaction().replace(R.id.user_main_RelativeLayout, gameFragment, null).addToBackStack(null).commit();
                }
            }
        });
    }

    private void deleteList(){
        listReference.delete();
        userReference.update("listsCount", FieldValue.increment(-1));
        for(Game game : games){
            gamesInListsReference.document(list.getId() + "_" + game.getId()).delete();
            gameReference = AppGlobals.db.collection("Games").document(game.getId());
            gameReference.update("numberOfLists", FieldValue.increment(-1));
        }
    }

    public void setGames(ArrayList<Game> games) {
        this.games = games;
    }
}