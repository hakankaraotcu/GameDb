package com.hakankaraotcu.gamedb.General;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hakankaraotcu.gamedb.Model.User;

public class AppGlobals {
    public static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public static FirebaseUser mUser;
    public static FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static User currentUser;
    public static final String[] mainTitles = {"GAMES", "REVIEWS", "LISTS", "NEWS"};
    public static final String[] profileTitles = {"Games", "Diary", "Lists", "Reviews", "To-Play List", "Likes", "Following", "Followers"};
}
