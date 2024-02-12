package com.example.karisong;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ResponseTest extends AppCompatActivity {
    private EditText nameedt, usernameedt, useridedt;
    private Button send;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    UserInfo userInfo;
    FirebaseUser thisuser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_responsetest);

        nameedt = findViewById(R.id.name);
        usernameedt = findViewById(R.id.username);
        useridedt = findViewById(R.id.userid);
        send = findViewById(R.id.send);
        String uid = thisuser.getUid();
        firebaseDatabase = FirebaseDatabase.getInstance("https://karisong-default-rtdb.firebaseio.com/");
        databaseReference = firebaseDatabase.getReference().child("users").child(uid);


        userInfo = new UserInfo();
        send.setOnClickListener(v -> {
            String name = nameedt.getText().toString();
            String username = usernameedt.getText().toString();
            String userid = useridedt.getText().toString();

            if (TextUtils.isEmpty(name) && TextUtils.isEmpty(username) && TextUtils.isEmpty(userid)) {
                // if the text fields are empty
                // then show the below message.
                Toast.makeText(ResponseTest.this, "Please add some data.", Toast.LENGTH_SHORT).show();
            } else {
                // else call the method to add
                // data to our database.
                addDatatoFirebase(name, username, userid);
                Toast.makeText(ResponseTest.this, "success!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void addDatatoFirebase(String name,String username,String userid ) {
        userInfo.setUsername(username);
        userInfo.setCountry(name);
        userInfo.setUserid(userid);
        userInfo.setFollowing(0);
        userInfo.setFollowers(0);
        userInfo.setPosts(0);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                databaseReference.child(userInfo.getUsername()).setValue(userInfo);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ResponseTest.this, "Error. Please try again later. " + error, Toast.LENGTH_SHORT).show();

            }
        });
    }
}

// verification email
// change "enter first and last name" to something else
// make ways to reset password and change email (including password reset email)
// delete user
// check if user is new
// reauthentication requirement
// sign out