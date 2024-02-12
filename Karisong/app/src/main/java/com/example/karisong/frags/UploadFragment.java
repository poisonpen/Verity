package com.example.karisong.frags;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karisong.LandingScreen;
import com.example.karisong.NewUserScreen;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.fragment.app.Fragment;

import com.example.karisong.MainActivity;
import com.example.karisong.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class UploadFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        openfile();
        View view = inflater.inflate(R.layout.fragment_upload, container, false);
        Button button = view.findViewById(R.id.uploading);
        ImageView imageView = view.findViewById(R.id.preview);
        CardView cardView = view.findViewById(R.id.cardView);
        EditText editText = view.findViewById(R.id.editText);

        button.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.VISIBLE);
        cardView.setVisibility(View.VISIBLE);
        editText.setVisibility(View.VISIBLE);



        return view;
    }

    private static final int CHOOSE_FILE = 1;
    private void openfile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        intent.getData();
        startActivityForResult(intent, CHOOSE_FILE);

    }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            switch (requestCode) {
                case CHOOSE_FILE:
                    if (resultCode == RESULT_OK) {
                        Uri file = data.getData();

                        ImageView imageView = getView().findViewById(R.id.preview);
                        imageView.setImageURI(file);
                        Button button = getView().findViewById(R.id.uploading);
                        EditText editText = getView().findViewById(R.id.editText);

                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                StorageReference storere = ((LandingScreen) getActivity()).getPostsStorageLocation();
                                UploadTask uploadTask;
                                Log.d("suggested", storere.getPath());
                                String content = editText.getText().toString();

                                Pattern MY_PATTERN = Pattern.compile("#(\\S+)");
                                Matcher mat = MY_PATTERN.matcher(content);
                                List<String> strs = new ArrayList<String>();
                                while (mat.find()) {
                                    strs.add(mat.group(1));
                                }

                                Log.d("inforafsmdfas", String.valueOf(strs));

                                uploadTask = storere.putFile(file);
                                uploadTask.addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        Log.d("suggested", "failure");


                                    }
                                }).addOnSuccessListener(taskSnapshot -> {
                                    if (getActivity() == null) {
                                        uploadTask.cancel();
                                    } else {
                                        ((LandingScreen) getActivity()).addPostCount();
                                        
                                        Toast.makeText(getContext(), "Post Created!", Toast.LENGTH_SHORT).show();
                                        ProfileFragment profileFragment = new ProfileFragment();
                                        getActivity().getSupportFragmentManager().beginTransaction()
                                                .setReorderingAllowed(false)
                                                .replace(R.id.host_container, profileFragment)
                                                .addToBackStack("")
                                                .commit();
                                    }
                                });

                            }
                        });


                    }
                    break;
            }
        }



    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.bottom_nav_menu, menu);
    }

    public UploadFragment(){
        super(R.layout.fragment_upload);
    }
}

