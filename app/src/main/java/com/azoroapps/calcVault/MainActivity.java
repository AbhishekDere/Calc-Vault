package com.azoroapps.calcVault;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.azoroapps.calcVault.view.YourAsyncTask;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.zip.ZipFile;

import es.dmoral.toasty.Toasty;
import ir.mahdi.mzip.zip.ZipArchive;

import static com.azoroapps.calcVault.view.NewUser.SHARED_PREFS;

public class MainActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    DriveServiceHelper driveServiceHelper;
    Button syncButton;
    View myView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        syncButton = findViewById(R.id.SyncWithDrive);
        progressDialog = new ProgressDialog(MainActivity.this);
        requestSignIn();
        syncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myView=v;
                new MyTask(MainActivity.this).execute();
            }
        });

    }

    /*private void zipFile() {
        pd = new ProgressDialog(MainActivity.this);
        pd.setTitle("Creating a Zip File to Upload");
        pd.setMessage("Please Wait");
        pd.setCancelable(false);
        pd.show();

        myZipFile().addOnSuccessListener(s -> {
            pd.dismiss();
            Toasty.success(MainActivity.this,"Zip Created",Toasty.LENGTH_SHORT).show();

        })
                .addOnFailureListener(e -> {
                    Toasty.error(MainActivity.this,"Something Went Wrong",Toasty.LENGTH_SHORT).show();
                    pd.dismiss();
                });

    }*/



    private void requestSignIn() {
        GoogleSignInOptions signInOptions= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope(DriveScopes.DRIVE_FILE))
                .build();
        GoogleSignInClient client = GoogleSignIn.getClient(this,signInOptions);
        startActivityForResult(client.getSignInIntent(),400);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 400:
                if(resultCode==RESULT_OK){
                    handleSignInIntent(data);
                }
                break;
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
                });
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
                activityReference.get().uploadPdfFile(activityReference.get().myView);
            }


        }

    }

}