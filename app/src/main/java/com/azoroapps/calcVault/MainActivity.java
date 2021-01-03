package com.azoroapps.calcVault;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

import java.lang.ref.WeakReference;
import java.util.Collections;

import es.dmoral.toasty.Toasty;
import ir.mahdi.mzip.zip.ZipArchive;

import static com.azoroapps.calcVault.view.NewUser.SHARED_PREFS;

public class MainActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    DriveServiceHelper driveServiceHelper;
    Button syncButton,restoreButton,logout;
    View myView;
    Context context;
    ImageView photo;
    TextView userName,userID;
    GoogleSignInClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        syncButton = findViewById(R.id.SyncWithDrive);
        restoreButton=findViewById(R.id.RestoreDrive);
        photo=findViewById(R.id.googleUserPhoto);
        userName=findViewById(R.id.googleUserName);
        userID=findViewById(R.id.UserID);
        logout=findViewById(R.id.logout);
        progressDialog = new ProgressDialog(MainActivity.this);
        context=getApplicationContext();
        requestSignIn();
        GoogleSignInAccount acct=GoogleSignIn.getLastSignedInAccount(this);
        if(acct==null){
            userName.setText("");
            userID.setText("Please Log in First");
            logout.setEnabled(false);
            restoreButton.setEnabled(false);
            syncButton.setEnabled(false);

        }
        else{
            userName.setText(acct.getDisplayName());
            userID.append(acct.getId());
            Glide.with(this).load(acct.getPhotoUrl()).into(photo);
            syncButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myView=v;
                    new MyTask(MainActivity.this).execute();
                }
            });
            restoreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // final SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
                    //ZipArchive.unzip(Environment.getExternalStorageDirectory().getPath()+"/AndroidFiles.zip"
                    //       ,Environment.getExternalStorageDirectory().getPath(),sharedPreferences.getString("password", ""));
                    myView=v;
                    new restoreTask(MainActivity.this).execute();
                }
            });
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    client.signOut().addOnCompleteListener(MainActivity.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toasty.success(MainActivity.this,"Logged Out Successfully",Toasty.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
            });
        }

    }

    private void requestSignIn() {
        GoogleSignInOptions signInOptions= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .requestScopes(new Scope(DriveScopes.DRIVE_FILE))
                .build();

        client = GoogleSignIn.getClient(this,signInOptions);
        startActivityForResult(client.getSignInIntent(),400);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 400) {
            if (resultCode == RESULT_OK) {
                finish();
                startActivity(getIntent());
                handleSignInIntent(data);

            }
        }
    }

    private void handleSignInIntent(Intent data) {
        GoogleSignIn.getSignedInAccountFromIntent(data)
                .addOnSuccessListener(googleSignInAccount -> {
                    GoogleAccountCredential credential=GoogleAccountCredential
                            .usingOAuth2(MainActivity.this, Collections.singleton(DriveScopes.DRIVE_FILE));
                    credential.setSelectedAccount(googleSignInAccount.getAccount());
                    Drive googleDriveService = new Drive.Builder(
                            AndroidHttp.newCompatibleTransport(),
                            new GsonFactory(),
                            credential)
                            .setApplicationName("Upload to Drive")
                            .build();
                    driveServiceHelper = new DriveServiceHelper(googleDriveService);
                })
                .addOnFailureListener(e -> {
                });
    }

    public void uploadPdfFile(View v){
        /*
        progressDialog.setTitle("Uploading to Google Drive");
        progressDialog.setMessage("Zipping done. Uploading it to Drive.");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String filePath = Environment.getExternalStorageDirectory()+"/AndroidFiles.zip";

        driveServiceHelper.createFilePDF(filePath).addOnSuccessListener(s -> {

            progressDialog.dismiss();
            Toasty.success(MainActivity.this,"Uploaded Successfully",Toasty.LENGTH_SHORT).show();
        })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toasty.error(MainActivity.this,"Check Your Google Drive API Key",Toasty.LENGTH_SHORT).show();
                });*/
    }

    static class MyTask extends AsyncTask<Integer, Integer, String> {

        WeakReference<MainActivity> activityReference;
        MyTask(MainActivity context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            activityReference.get().progressDialog.setMessage("Creating a Protected Zip File");
            activityReference.get().progressDialog.show();
        }

        @Override
        protected String doInBackground(Integer... params) {
            final SharedPreferences sharedPreferences = activityReference.get().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
            java.io.File file = new java.io.File(Environment.getExternalStorageDirectory() + "/.Vault/");
            java.io.File destination = new java.io.File(Environment.getExternalStorageDirectory().getPath()+"/AndroidFiles.zip");
            ZipArchive.zip(file.getPath(),destination.getPath(),sharedPreferences.getString("password", ""));
            return "File Zipped";
        }
        @Override
        protected void onPostExecute(String result) {
            if (activityReference.get().progressDialog.isShowing()) {
                activityReference.get().progressDialog.dismiss();
                //activityReference.get().uploadPdfFile(activityReference.get().myView);
            }
        }
    }

    static class restoreTask extends AsyncTask<Integer, Integer, String> {

        WeakReference<MainActivity> activityReference;
        restoreTask(MainActivity context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            activityReference.get().progressDialog.setMessage("Un-Zipping");
            activityReference.get().progressDialog.show();
        }

        @Override
        protected String doInBackground(Integer... params) {
            final SharedPreferences sharedPreferences = activityReference.get().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

            ZipArchive.unzip(Environment.getExternalStorageDirectory().getPath()+"/AndroidFiles.zip"
                    ,Environment.getExternalStorageDirectory().getPath(),sharedPreferences.getString("password", ""));
            return "File Zipped";
        }
        @Override
        protected void onPostExecute(String result) {
            if (activityReference.get().progressDialog.isShowing()) {
                activityReference.get().progressDialog.dismiss();
                Toast.makeText(activityReference.get().context, "Success", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(activityReference.get().context, "Failed", Toast.LENGTH_SHORT).show();
            }


        }

    }

}