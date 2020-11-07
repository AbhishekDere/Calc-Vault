package com.azoroapps.calcVault;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.azoroapps.calcVault.utilities.DriveServiceHelper;
import com.azoroapps.calcVault.view.LoginActivity;
import com.azoroapps.calcVault.view.VaultScreen;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.gson.Gson;

import java.util.Collections;

import es.dmoral.toasty.Toasty;

public class DriveActivity extends AppCompatActivity {
    GoogleSignInClient mGoogleSignInClient;
    ImageView avatar;
    TextView name,email;
    Button logoutButton;
    GoogleSignInAccount acct;
    DriveServiceHelper driveServiceHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive);
        requestSignIn();

        /*logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ...
                if (v.getId() == R.id.button_logout) {
                    LogOut();
                    // ...
                }
            }

        });*/
    }
    private void requestSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope(DriveScopes.DRIVE_FILE))
                .build();
        mGoogleSignInClient  = GoogleSignIn.getClient(this, gso);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        logoutButton = findViewById(R.id.button_logout);
        avatar=findViewById(R.id.avatar);
        acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            //Existing User
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            name.setText(personName);
            email.setText(personEmail);
            Glide.with(this).load(personPhoto).into(avatar);
        }
        else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        startActivityForResult(mGoogleSignInClient.getSignInIntent(),400);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 400:
                if(resultCode==RESULT_OK){
                    handleSignInIntent(data);
                }
        }
    }

    private void handleSignInIntent(Intent data) {
        GoogleSignIn.getSignedInAccountFromIntent(data)
                .addOnSuccessListener(new OnSuccessListener<GoogleSignInAccount>() {
                    @Override
                    public void onSuccess(GoogleSignInAccount googleSignInAccount) {
                        GoogleAccountCredential credential=GoogleAccountCredential
                                .usingOAuth2(DriveActivity.this, Collections.singleton(DriveScopes.DRIVE_FILE));
                        credential.setSelectedAccountName(String.valueOf(googleSignInAccount.getAccount()));
                        Drive googleDriveService=new Drive.Builder(
                                AndroidHttp.newCompatibleTransport(),
                                new GsonFactory(),
                                credential).setApplicationName("My Drive Tutorial")
                                .build();
                        driveServiceHelper=new DriveServiceHelper(googleDriveService);
                    }
                });
    }

    public void uploadPdfFile(View v){
        Context context;
        ProgressDialog progressDialog=new ProgressDialog(DriveActivity.this);
        progressDialog.setTitle("Uploading to Gdrive");
        progressDialog.setMessage("Please Wait");
        progressDialog.show();
        //driveServiceHelper.createFilePDF(/*Your Intent File*/);
    }

    private void LogOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        acct=null;
                        Toasty.success(getApplicationContext(),"Signed Out",Toasty.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, VaultScreen.class);
        startActivity(intent);
        finish();
    }
}