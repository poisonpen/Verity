package com.example.karisong;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.karisong.frags.CourseModel;
import com.example.karisong.frags.SettingsFragment;
import com.example.karisong.frags.specialfrags.PostFragment;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class CourseAdapter extends ArrayAdapter<CourseModel> {

    public CourseAdapter(@NonNull Context context, ArrayList<CourseModel> courseModelArrayList) {
        super(context, 0, courseModelArrayList);
    }

    @NonNull
    @Override
    public View getView( int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.card_item, parent, false);
        }

        CourseModel courseModel = getItem(position);
        ImageView image = listItemView.findViewById(R.id.imageexample);
        image.setImageBitmap(courseModel.getBitmap());

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostFragment fragment = new PostFragment();
                FragmentActivity activity = (FragmentActivity) getContext();

                Bundle args = new Bundle();
                args.putString("Storagereference", courseModel.getStorageReference()+"");
                fragment.setArguments(args);

                activity.getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(false)
                        .replace(R.id.host_container, fragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                        .addToBackStack("")
                        .commit();
            }

        });

        return listItemView;
    }
}