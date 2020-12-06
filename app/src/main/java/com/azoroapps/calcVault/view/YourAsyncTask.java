package com.azoroapps.calcVault.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;

import com.azoroapps.calcVault.MainActivity;

import ir.mahdi.mzip.zip.ZipArchive;

import static com.azoroapps.calcVault.view.NewUser.SHARED_PREFS;

public class YourAsyncTask extends AsyncTask<Void, Void, Void> {
    private final ProgressDialog dialog;
    Context mainActivity;

    public YourAsyncTask(MainActivity activity) {
        dialog = new ProgressDialog(activity);
        this.mainActivity= activity;
    }

    @Override
    protected void onPreExecute() {
        dialog.setMessage("Creating a Zip File...");
        dialog.show();
    }
    @Override
    protected Void doInBackground(Void... args) {
        final SharedPreferences sharedPreferences = mainActivity.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        java.io.File file = new java.io.File(Environment.getExternalStorageDirectory() + "/.Vault/");
        java.io.File destination = new java.io.File(Environment.getExternalStorageDirectory().getPath()+"/AndroidFiles.zip");
        ZipArchive.zip(file.getPath(),destination.getPath(),sharedPreferences.getString("password", ""));
        return null;
    }
    @Override
    protected void onPostExecute(Void result) {
        // do UI work here
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}