package com.example.karisong.frags;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.karisong.CourseAdapter;
import com.example.karisong.LandingScreen;
import com.example.karisong.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Objects;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        super(R.layout.fragment_profile);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        Button settings = view.findViewById(R.id.settings);

        TextView posts_txt = view.findViewById(R.id.posts);
        posts_txt.setText(((LandingScreen) getActivity()).getPostsCount());
        Log.d("reset: " , (((LandingScreen) getActivity()).getPostsCount()));
        settings.setOnClickListener(v -> {
            SettingsFragment settingsFragment = new SettingsFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(false)
                    .replace(R.id.host_container, settingsFragment)
                    .addToBackStack("")
                    .commit();
        });

        loadProfileScreen();
        return view;

    }



    private void loadProfileScreen() {

        ((LandingScreen) getActivity()).getUserInfo(new LandingScreen.OnGetDataListener() {
            @Override
            public void onStart() {

            Log.d("listenerStarted", "started");

            }
            @Override
            public void onSuccess() {
                Log.d("listenerStarted", "success");

                LinearLayout post_section = getView().findViewById(R.id.post_section);
                LinearLayout dash_section = getView().findViewById(R.id.dash_section);
                ImageView loading_screen = getView().findViewById(R.id.loading_screen);

                post_section.setVisibility(View.VISIBLE);
                dash_section.setVisibility(View.VISIBLE);
                loading_screen.setVisibility(View.GONE);
                ProgressBar progressBar = getView().findViewById(R.id.progressBar);
                progressBar.setVisibility(View.GONE);

                if (getView() !=  null) {
                    ImageView dashboardImage = getView().findViewById(R.id.imageView);
                    loadDashboardImage(dashboardImage);
                    loadGalleryImages(new OnGetDataListener() {
                        @Override
                        public void onStart() {
                            ProgressBar progressBar = getView().findViewById(R.id.progressBar);
                            progressBar.setVisibility(View.GONE);

                        }

                        @Override
                        public void onSuccess() {
                            Log.d("pattern", "dswe3");

//                            post_section.setVisibility(View.VISIBLE);
//                            dash_section.setVisibility(View.VISIBLE);
//
//                            ProgressBar progressBar = getView().findViewById(R.id.progressBar);
//                            progressBar.setVisibility(View.GONE);
//                            loading_screen.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailed() {

                        }
                    });




                    TextView username_txt = getView().findViewById(R.id.username);
                    TextView displayname_txt = getView().findViewById(R.id.displayname);
                    TextView posts_txt = getView().findViewById(R.id.posts);
                    TextView followers_txt = getView().findViewById(R.id.followers);
                    TextView following_txt = getView().findViewById(R.id.following);

                    username_txt.setText(((LandingScreen) getActivity()).getUsername());
                    displayname_txt.setText(((LandingScreen) getActivity()).getDisplayName());
                    followers_txt.setText(((LandingScreen) getActivity()).getFollowers());
                    following_txt.setText(((LandingScreen) getActivity()).getFollowing());
                    Log.d("pattern", "dswe4");

                }
            }
            @Override
            public void onFailed() {

                Log.d("listenerStarted", "failed" );

            }
        });
    }


    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.bottom_nav_menu, menu);
    }

    // TODO caching is unreliable with glide and to date i have no way to fix it. So, each time, we are skipping the memory
    // cache. The best option seems to download the image to the user's device and overwrite it every time it changes, and
    // if the overwriting is disturbed, catch the error. Perhaps checking if the values of the storage and downloaded images
    // match or something.
    // Alternatively, we can have the dash image only refreshed on the first startup of the app.

    // TODO PICASSO IS BETTER!! WE CAN SWITCH AND OPTIMIZE
    // store bitmaps as ints in the savedinstancestatebundle and then use the ints to make placeholder images :000

    public void loadDashboardImage(ImageView dashImageView) {
        StorageReference dashboardStorageReference = ((LandingScreen) getActivity()).getDashboardImageStorageLocation();
        SharedPreferences preferences = getContext().getSharedPreferences("dashImages_preferences", 0);
        String dashchanged = preferences.getString("isDashChanged", "no");
        Log.d("dashChanged", "               "+dashchanged);
        Task<Uri> uri = dashboardStorageReference.getDownloadUrl();
        uri.addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri1) {
                Picasso.get()
                        .load(uri1)
                        .into(dashImageView);
            }
        });



//        if (dashchanged == "yes") {
//
//
//                        Log.d("dashChanged", "yeah");
//            Glide.with(this.getContext())
//                    .load(dashboardStorageReference)
//                    .skipMemoryCache(true)
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .into(dashImageView);
//            ((LandingScreen) getActivity()).setDashChangedStatus(false);
//        } else {
//            Log.d("dashChanged", "nah");
//            Glide.with(this.getContext())
//                    .load(dashboardStorageReference)
//                    .skipMemoryCache(false)
//                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
//                    .into(dashImageView);
//        }
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    public interface OnGetDataListener {
        public void onStart();
        public void onSuccess();
        public void onFailed();
    }

    public void loadGalleryImages (OnGetDataListener onGetDataListener) {
        StorageReference galleryImagesRef = ((LandingScreen) getActivity()).getGalleryImageStorageLocation();
        if (galleryImagesRef != null) {
            ArrayList<CourseModel> courseModelArrayList = new ArrayList<CourseModel>();
            CourseAdapter adapter = new CourseAdapter(getContext(), courseModelArrayList);

            galleryImagesRef.listAll()
                    .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                        @Override

                        public void onSuccess(ListResult listResult) {
                            int x = listResult.getItems().size();
                            ((LandingScreen) getActivity()).resetPostCount(x);

                            if (x == 1) {
                                StorageReference ref = listResult.getItems().get(0);
                                courseModelArrayList.add(new CourseModel(null, ref));
                                Glide.with(getContext())
                                        .asBitmap()
                                        .load(ref)
                                        .skipMemoryCache(false)
                                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                        .into(new CustomTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                SharedPreferences mySharedPreferences;
                                                mySharedPreferences = getActivity().getSharedPreferences("post_saved", 0);
                                                SharedPreferences.Editor myEditor;
                                                myEditor = mySharedPreferences.edit();

                                                myEditor.putString("0", BitMapToString(resource));
                                                myEditor.commit();

                                                GridView courses = getView().findViewById(R.id.gridview);
                                                courseModelArrayList.set(0, new CourseModel(resource,ref));

                                                courses.setAdapter(adapter);
                                            }

                                            @Override
                                            public void onLoadCleared(@Nullable Drawable placeholder) {

                                            }
                                        });
                            } else {
                                for (int i = 0; i < x; i++) {
                                    if (getActivity() != null) {
                                        int galleryPosition = i;
                                        StorageReference ref = listResult.getItems().get(i);
                                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("post_saved", 0);

                                        String string = sharedPreferences.getString(i + "", null);

                                        if (string != null) {
                                            Bitmap btm = StringToBitMap(string);
                                            Drawable d = new BitmapDrawable(getResources(), btm);

                                            courseModelArrayList.add(new CourseModel(btm, ref));
                                        } else {
                                            courseModelArrayList.add(new CourseModel(null, ref));

                                        }
                                        Log.d("pattern", "dswe");
                                        Glide.with(getActivity())
                                                .asBitmap()
                                                .load(ref)
                                                .skipMemoryCache(false)
                                                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                                .into(new CustomTarget<Bitmap>() {
                                                    @Override
                                                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                        if (getView() != null) {
                                                            Log.d("pattern", "dswe2");

                                                            SharedPreferences mySharedPreferences;
                                                            mySharedPreferences = getActivity().getSharedPreferences("post_saved", 0);
                                                            SharedPreferences.Editor myEditor;
                                                            myEditor = mySharedPreferences.edit();

                                                            myEditor.putString(String.valueOf(galleryPosition), BitMapToString(resource));
                                                            myEditor.commit();

                                                            GridView courses = getView().findViewById(R.id.gridview);
                                                            courseModelArrayList.set(galleryPosition, new CourseModel(resource, listResult.getItems().get(galleryPosition)));

                                                            courses.setAdapter(adapter);

                                                        }
                                                        onGetDataListener.onSuccess();
                                                    }


                                                    @Override
                                                    public void onLoadCleared(@Nullable Drawable placeholder) {

                                                    }
                                                });
                                    }
                                }
                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("pattern", "dswe3");
                        }
                    });
            Log.d("pattern", "dswe2");
        }
    }


}



