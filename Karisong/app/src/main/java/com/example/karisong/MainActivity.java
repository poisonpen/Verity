package com.example.karisong;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.wifi.hotspot2.pps.Credential;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.data.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.auth.OAuthCredential;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    startSignIn();
  }

  private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
          new FirebaseAuthUIActivityResultContract(),
          (result) -> {
              FirebaseAuth auth = FirebaseAuth.getInstance();
              FirebaseUserMetadata metadata = auth.getCurrentUser().getMetadata();
              if (metadata.getCreationTimestamp() == metadata.getLastSignInTimestamp()) {
                  Intent intent = new Intent(getApplicationContext(), NewUserScreen.class);
                  startActivity(intent);
              } else {
                      Intent intent = new Intent(getApplicationContext(), LandingScreen.class);
                      startActivity(intent);
              }
          });

  private void startSignIn() {
      FirebaseAuth auth = FirebaseAuth.getInstance();
      LandingScreen landingScreen = new LandingScreen();
      Intent intentOld = getIntent();
      intentOld.getExtras();
      Boolean reSignOn = intentOld.getBooleanExtra("Resign", false);
      if (auth.getCurrentUser() != null && auth.getUid() != null && reSignOn == false) {
          Intent intent = new Intent(getApplicationContext(), LandingScreen.class);
          startActivity(intent);
      } else {
          AuthMethodPickerLayout customLayout = new AuthMethodPickerLayout
                  .Builder(R.layout.auth_layout)
                  .setGoogleButtonId(R.id.google_btn)
                  .setEmailButtonId(R.id.email_btn)
                  .setTosAndPrivacyPolicyId(R.id.tos_privacy_txt)

                  .build();

          Intent signInIntent =
                  AuthUI.getInstance().createSignInIntentBuilder()
                          .setTheme(R.style.Theme_Karisong)
                          .setAuthMethodPickerLayout(customLayout)
                          .setTosAndPrivacyPolicyUrls("https://karisong.carrd.co/#privacytos", "https://karisong.carrd.co/#privacytos")
                          .setIsSmartLockEnabled(false)
                          .setAvailableProviders(Arrays.asList(
                                  new AuthUI.IdpConfig.EmailBuilder().build(),
                                  new AuthUI.IdpConfig.GoogleBuilder().build()
                          ))
                          .build();
          signInLauncher.launch(signInIntent);
      }
  }
}

