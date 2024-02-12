package com.example.karisong.frags.specialfrags;

import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.karisong.LandingScreen;
import com.example.karisong.R;
import com.example.karisong.frags.CourseModel;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PostFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_post, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        androidx.appcompat.widget.Toolbar toolbar = view.findViewById(R.id.toolbarId);
        ImageView postImage = view.findViewById(R.id.postImage);
        Button delete = view.findViewById(R.id.delete);
        StorageReference dashboardStorageReference = ((LandingScreen) getActivity()).getDashboardImageStorageLocation();
        Bundle bundle = this.getArguments();
        String storageReferenceString = bundle.getString("Storagereference");
        Log.d("storageref", storageReferenceString);



        FirebaseStorage postFirebaseStorageLocation = FirebaseStorage.getInstance();
        StorageReference postStorageLocation = postFirebaseStorageLocation.getReferenceFromUrl(storageReferenceString);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO the delete isn't actully deleting haha but only sometimes
                ((LandingScreen) getActivity()).deletePost(postStorageLocation);
                getActivity().getSupportFragmentManager().popBackStack();

            }
        });

        Glide.with(getActivity())
                .asBitmap()
                .load(postStorageLocation)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.empty)
                .into(postImage);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_baseline_arrow_back_24));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }        });

    }

    // TODO add save and like animation, populate imageview with the image that was tapped into this fragment, set a bio somehow

    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.bottom_nav_menu, menu);
    }

    public PostFragment(){
        super(R.layout.fragment_post);
    }
}