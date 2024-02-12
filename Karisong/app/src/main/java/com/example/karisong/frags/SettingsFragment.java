package com.example.karisong.frags;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.example.karisong.LandingScreen;
import com.example.karisong.MainActivity;
import com.example.karisong.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SettingsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        Button logout = view.findViewById(R.id.logout);
        Button deleteacc = view.findViewById(R.id.deleteacc);

        Button changeDash = view.findViewById(R.id.changeDash);
        EditText new_displayname_picker = view.findViewById(R.id.new_displayname_picker);
        TextView save = view.findViewById(R.id.save);
        Switch modeswitch = view.findViewById(R.id.switch1);
        androidx.appcompat.widget.Toolbar toolbar = view.findViewById(R.id.toolbarId);

        new_displayname_picker.setText(((LandingScreen) getActivity()).getDisplayName());
        final String[] isDashChanged = {"no", ""};

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_baseline_arrow_back_24));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });

        AuthUI.getInstance();

        logout.setOnClickListener(v -> {
            AuthUI.getInstance().signOut(this.getActivity());

            final String MYPREFS = "Userinfo_preferences";
            final String MYPREFS2 = "dashImages_preferences";

            SharedPreferences mySharedPreferences = getActivity().getSharedPreferences(MYPREFS, 0);
            SharedPreferences mySharedPreferences2 = getActivity().getSharedPreferences(MYPREFS2, 0);
            mySharedPreferences.edit().clear().commit();
            mySharedPreferences2.edit().clear().commit();

            Intent intent = new Intent(this.getContext(), MainActivity.class);
            intent.putExtra("Resign", true);

            startActivity(intent);
            getActivity().finish();

        });

        deleteacc.setOnClickListener(v -> {
            ((LandingScreen) getActivity()).deleteAccount();
            Intent intent = new Intent(this.getContext(), MainActivity.class);
            intent.putExtra("Resign", true);

            startActivity(intent);
            getActivity().finish();
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new_displayname_picker.getText().toString() != ((LandingScreen) getActivity()).getDisplayName()) {
                    ((LandingScreen) getActivity()).setDisplayName(new_displayname_picker.getText().toString());
                }

                ProfileFragment profileFragment= new ProfileFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(false)
                        .add(profileFragment, "added??")
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)

                        .replace(R.id.host_container, profileFragment)
                        .commitNow();

            }
        });


        if (modeswitch.isChecked()) {
            // TODO set default mode
        }

        changeDash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getContext().getSharedPreferences("dashImages_preferences", 0);
                SharedPreferences.Editor myEditor;
                myEditor = preferences.edit();
                myEditor.putString("isDashChanged", "yes");
                myEditor.commit();
                openfile();
            }
        });

        return view;

    }

    private static final int CHOOSE_FILE = 1;
    private void openfile() {
        // TODO validate image (dimensions/size), type
        ((LandingScreen) getActivity()).setDashChangedStatus(true);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        intent.getData();
        startActivityForResult(intent, CHOOSE_FILE);
    }

    public interface onSuccessListener {
        public void onStart();
        public void onSuccess();
        public void onFailed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CHOOSE_FILE:
                if (resultCode == RESULT_OK) {
                    Uri file = data.getData();

                    StorageReference dashboardRef = ((LandingScreen) getActivity()).getDashboardImageStorageLocation();

                    UploadTask uploadTask;
                    uploadTask = dashboardRef.putFile(file);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        }
                    });
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Log.d("suggested", "failure");
                        }
                    }).addOnSuccessListener(taskSnapshot -> {

                    });
                }
                break;
        }
    }

    public SettingsFragment() {
        super(R.layout.fragment_settings);
    }
}
