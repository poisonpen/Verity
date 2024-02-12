package com.example.karisong;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

//import android.app.FragmentTransaction;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.*;

import com.example.karisong.frags.ContestFragment;
import com.example.karisong.frags.SettingsFragment;
import com.example.karisong.frags.UploadFragment;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.example.karisong.frags.DashboardFragment;
import com.example.karisong.frags.HomeFragment;
import com.example.karisong.frags.ProfileFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class LandingScreen extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Fragment fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_screen);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        isUserExisting(); // checks if the user's account has a name and such -- if not, prompts to redo that

        openFragment(new HomeFragment());
        bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragments = new HomeFragment();
                    openFragment(fragments);
                    return true;
                case R.id.navigation_dashboard:
                    fragments = new DashboardFragment();
                    openFragment(fragments);
                    return true;
                case R.id.navigation_upload:
                    fragments = new UploadFragment();
                    openFragment(fragments);
                    return true;
                case R.id.navigation_contest:
                    fragments = new ContestFragment();
                    openFragment(fragments);
                    return true;
                case R.id.navigation_profile:
                    fragments = new ProfileFragment();
                    openFragment(fragments);
                    return true;
            }
            return true;
        });
    }


    private void openFragment(Fragment fragment) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
               transaction
                       .setReorderingAllowed(true)
                       .add(fragment, "a")
                       .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                       .replace(R.id.host_container, fragment)
                       .commit();
    }




    public interface OnGetDataListener {
        public void onStart();
        public void onSuccess();
        public void onFailed();
    }


    public String[] getUserInfo(OnGetDataListener onGetDataListener) {
        FirebaseUser thisuser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://karisong-default-rtdb.firebaseio.com/");
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("users").child(thisuser.getUid());

        onGetDataListener.onStart();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot2 : snapshot.getChildren()) {
                    for (DataSnapshot snapshot3 : snapshot2.getChildren()) {

                        final String username = snapshot2.child("username").getValue().toString();
                        final String displayname = snapshot2.child("displayname").getValue().toString();
                        final String MYPREFS = "Userinfo_preferences";
                        final int mode = Activity.MODE_PRIVATE;

                        SharedPreferences mySharedPreferences;
                        mySharedPreferences = getSharedPreferences(MYPREFS, mode);

                        SharedPreferences.Editor myEditor;
                        myEditor = mySharedPreferences.edit();
                        myEditor.putString("username", username);
                        myEditor.putString("displayname", displayname);

                        final String posts = snapshot2.child("posts").getValue().toString();
                        final String following = snapshot2.child("following").getValue().toString();
                        final String followers = snapshot2.child("followers").getValue().toString();

                        myEditor.putString("posts", posts);
                        myEditor.putString("following", following);
                        myEditor.putString("followers", followers);

                        myEditor.commit();
                    }
                }
                onGetDataListener.onSuccess();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // read failed
            }
        });

        SharedPreferences preferences = getSharedPreferences("Userinfo_preferences", 0);

        String username = preferences.getString("username", null);
        String displayname = preferences.getString("displayname", null);
        String posts = preferences.getString("posts", null);
        String following = preferences.getString("following", null);
        String followers = preferences.getString("following", null);

        String[] userInfoArray = {username, displayname, posts, following, followers};
        return userInfoArray;
    }

    // The following looks a lot like it should perhaps be contained within one single object, the way
    // the userinfo class is... perhaps TODO
    // just to kill the boilerplate code.

    public String getUsername() {
        SharedPreferences preferences = getSharedPreferences("Userinfo_preferences", 0);
        String username = preferences.getString("username", null);
        return username;
    }

    public String getDisplayName() {
        SharedPreferences preferences = getSharedPreferences("Userinfo_preferences", 0);
        String displayname = preferences.getString("displayname", null);
        return displayname;
    }

    public String getPostsCount() {

        SharedPreferences preferences = getSharedPreferences("Userinfo_preferences", 0);
        String posts = preferences.getString("posts", null);
        return posts;
    }
    public String getFollowing() {
        SharedPreferences preferences = getSharedPreferences("Userinfo_preferences", 0);
        String following = preferences.getString("following", null);
        return following;
    }

    public String getFollowers() {
        SharedPreferences preferences = getSharedPreferences("Userinfo_preferences", 0);
        String followers = preferences.getString("followers", null);
        return followers;
    }

        // TODO keeps returning empty
    public StorageReference getPostsStorageLocation() {
        SharedPreferences preferences = getSharedPreferences("Userinfo_preferences", 0);
        String username = preferences.getString("username", null);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        long long_date = new Date().getTime();
        String date = String.valueOf(long_date);
        Log.d("datecurrent", date);

        StorageReference storageRef2 = storageRef.child(username).child("posts").child(date+""+UUID.randomUUID().toString());
        return storageRef2;
    }


    public StorageReference getDashboardImageStorageLocation() {
        String username = getUsername();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference dashboardRef = storageRef.child(username).child("profile").child("dashboardImage");

        return dashboardRef;
    }


    public StorageReference getGalleryImageStorageLocation() {
        String username = getUsername();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference dashboardRef = storageRef.child(username).child("posts");
        return dashboardRef;
    }

    public void setDisplayName(String new_display_name) {
        FirebaseUser thisuser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = thisuser.getUid();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://karisong-default-rtdb.firebaseio.com/");
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("users").child(uid).child(getUsername()).child("displayname");
        databaseReference.setValue(new_display_name);
    }

    public void addPostCount() {
        FirebaseUser thisuser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = thisuser.getUid();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://karisong-default-rtdb.firebaseio.com/");
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("users").child(uid).child(getUsername()).child("posts");

        int currentPostCount = Integer.parseInt(getPostsCount());
        databaseReference.setValue(1);
        //Log.d("dbinfo", databaseReference.toString());
    }

    public void addPostTags(String tags) {
        String tag = tags;

        FirebaseUser thisuser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = thisuser.getUid();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://karisong-default-rtdb.firebaseio.com/");
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("tags").child(tag);
        databaseReference.setValue(tag);
    }

    public void subtractPostCount() {
        FirebaseUser thisuser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = thisuser.getUid();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://karisong-default-rtdb.firebaseio.com/");
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("users").child(uid).child(getUsername()).child("posts");
        int currentPostCount = Integer.parseInt(getPostsCount());
        databaseReference.setValue(currentPostCount - 1);
    }

    public void deletePost(StorageReference storageReference) {
        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                subtractPostCount();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
            }
        });

    }
    public void deleteAccount() {
        SharedPreferences preferences = getSharedPreferences("Userinfo_preferences", 0);
        String username = preferences.getString("username", null);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference ref = storageRef.child(username);

        FirebaseUser thisuser = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://karisong-default-rtdb.firebaseio.com/");
        DatabaseReference databaseReference = firebaseDatabase.getReference()
                .child("users").child(thisuser.getUid());

        databaseReference.removeValue();
        ref.delete();
        thisuser.delete();
        Log.d("dones", "done");
    }

    public void resetPostCount(int posts) {
        FirebaseUser thisuser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://karisong-default-rtdb.firebaseio.com/");
        DatabaseReference databaseReference = firebaseDatabase.getReference()
                .child("users").child(thisuser.getUid()).child(getUsername()).child("posts");

        final String MYPREFS = "Userinfo_preferences";
        final int mode = Activity.MODE_PRIVATE;

        SharedPreferences mySharedPreferences;
        mySharedPreferences = getSharedPreferences(MYPREFS, mode);
        SharedPreferences.Editor myEditor;
        myEditor = mySharedPreferences.edit();
        myEditor.putString("posts", String.valueOf(posts));
        myEditor.commit();

        databaseReference.setValue(posts);
        Log.d("scasdasdas", "reset");
    }

    public void setDashChangedStatus (Boolean isChanged) {
        final String MYPREFS = "dashImages_preferences";
        final int mode = Activity.MODE_PRIVATE;
        SharedPreferences mySharedPreferences;
        mySharedPreferences = getSharedPreferences(MYPREFS, mode);
        SharedPreferences.Editor myEditor;
        myEditor = mySharedPreferences.edit();
        if (isChanged == true) {
            myEditor.putString("isDashChanged", "yes");
        } else {
            myEditor.putString("isDashChanged", "no");
        }
        myEditor.commit();
    }

    public void isUserExisting() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            FirebaseUser thisuser = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://karisong-default-rtdb.firebaseio.com/");
            DatabaseReference databaseReference = firebaseDatabase.getReference().child("users").child(thisuser.getUid());
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.getValue() == null) {
                        Intent intent = new Intent(getApplicationContext(), NewUserScreen.class);
                        startActivity(intent);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }
}