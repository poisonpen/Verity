package com.example.karisong;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NewUserScreen extends AppCompatActivity {
    private EditText userpicker_edt;
    private EditText display_edt;
    private Button goButton;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser thisuser = FirebaseAuth.getInstance().getCurrentUser();

    private Spinner country_spinner;

    public NewUserScreen() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_screen);

        userpicker_edt = (findViewById(R.id.new_username_picker));
        display_edt = (findViewById(R.id.new_displayname_picker));
        goButton = findViewById(R.id.letsgo);
        country_spinner = (findViewById(R.id.country_spinner));
        String uid = thisuser.getUid();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.countries_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        country_spinner.setAdapter(adapter);

        firebaseDatabase = FirebaseDatabase.getInstance("https://karisong-default-rtdb.firebaseio.com/");
        databaseReference = firebaseDatabase.getReference().child("users").child(uid);


        goButton.setOnClickListener(v -> {
            String username = userpicker_edt.getText().toString();
            String displayname = display_edt.getText().toString();
            String country;

            if (country_spinner.getSelectedItem() == null ){
                country = "United States of America";
            } else {
                country = country_spinner.getSelectedItem().toString();
            }

            if (TextUtils.isEmpty(username))  {
                Toast.makeText(NewUserScreen.this, "Please enter a username.", Toast.LENGTH_SHORT).show();
            } else {
                if (TextUtils.isEmpty(displayname)) {
                    displayname = username;
                }
                addDatatoFirebase(username, displayname, country);
                Toast.makeText(NewUserScreen.this, "success!", Toast.LENGTH_SHORT).show();
            }

        });

    }


    private void addDatatoFirebase(String username , String displayname , String country ) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(username);
        userInfo.setDisplayname(displayname);
        userInfo.setCountry(country);
        userInfo.setFollowers(0);
        userInfo.setFollowing(0);
        userInfo.setPosts(0);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                final String MYPREFS = "Userinfo_preferences";
                final int mode = Activity.MODE_PRIVATE;

                SharedPreferences mySharedPreferences;
                mySharedPreferences = getSharedPreferences(MYPREFS, mode);

                SharedPreferences.Editor myEditor;
                myEditor = mySharedPreferences.edit();

                myEditor.putString("username", username);
                myEditor.putString("displayname", displayname);
                myEditor.putString("posts", "0");
                myEditor.putString("following", "0");
                myEditor.putString("followers", "0");

                myEditor.commit();

                databaseReference.child(userInfo.getUsername()).setValue(userInfo);
                Intent intent = new Intent(getApplicationContext(), LandingScreen.class);
                startActivity(intent);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(NewUserScreen.this, "Fail to add data " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

}

