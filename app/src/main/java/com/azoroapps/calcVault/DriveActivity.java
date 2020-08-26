package com.azoroapps.calcVault;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import es.dmoral.toasty.Toasty;

public class DriveActivity extends AppCompatActivity {
    GoogleSignInClient mGoogleSignInClient;
    ImageView avatar;
    TextView name,email;
    Button logoutButton;
    GoogleSignInAccount acct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient  = GoogleSignIn.getClient(this, gso);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        logoutButton = findViewById(R.id.button_logout);
        avatar=findViewById(R.id.avatar);

        acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            name.setText(personName);
            email.setText(personEmail);
            Glide.with(this).load(personPhoto).into(avatar);
        }
        else {
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
        }

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ...
                if (v.getId() == R.id.button_logout) {
                    LogOut();
                    // ...
                }
            }

        });
    }
    private void LogOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        acct=null;
                        Toasty.success(getApplicationContext(),"Signed Out",Toasty.LENGTH_SHORT).show();
                        /*Intent intent = new Intent(getApplicationContext(),Vault.class);
                        startActivity(intent);*/
                        finish();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this,Vault.class);
        startActivity(intent);
        finish();
    }
}