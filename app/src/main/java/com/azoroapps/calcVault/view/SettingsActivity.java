package com.azoroapps.calcVault.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.azoroapps.calcVault.R;

import java.io.File;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;

import static com.azoroapps.calcVault.view.NewUser.SHARED_PREFS;

public class SettingsActivity extends AppCompatActivity {

    LinearLayout changePassword,eraseVault;
    String newPass;
    File file = new File(Environment.getExternalStorageDirectory() + "/.Vault");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        changePassword = findViewById(R.id.view_change_password);
        eraseVault=findViewById(R.id.eraseVault);
        changePassword.setOnClickListener(v -> changePassword());
        eraseVault.setOnClickListener(v -> {
            new SweetAlertDialog(SettingsActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Are you sure?")
                    .setContentText("Won't be able to recover this file!")
                    .setConfirmText("Yes,delete it!")
                    .setConfirmClickListener(sDialog -> {
                        deleteAllFiles(file);
                        sDialog.dismissWithAnimation();
                        finish();
                        System.exit(0);
                    })
                    .setCancelButton("Cancel", sDialog -> {
                    })
                    .show();
        });
    }
    void deleteAllFiles(File dir) {
        Log.d("DeleteRecursive", "DELETEPREVIOUS TOP" + dir.getPath());
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            for (String child : children) {
                File temp = new File(dir, child);
                if (temp.isDirectory()) {
                    Log.d("DeleteRecursive", "Recursive Call" + temp.getPath());
                    deleteAllFiles(temp);
                } else {
                    Log.d("DeleteRecursive", "Delete File" + temp.getPath());
                    boolean b = temp.delete();
                    if (!b) {
                        Log.d("DeleteRecursive", "DELETE FAIL");
                    }
                }
            }
        }
        boolean b =dir.delete();
        if(b){
            Toasty.success(this,"All Files Deleted, Re-Open The App.",Toasty.LENGTH_SHORT).show();
        }
    }

    private void changePassword() {

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.change_password_popup, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setView(promptsView);

        final EditText userInput =  promptsView.findViewById(R.id.editTextDialogUserInput);
        userInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        userInput.setFilters(new InputFilter[] {new InputFilter.LengthFilter(8)});

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        (dialog, id) -> {
                            // get user input and set it to result
                            // edit text
                            newPass=userInput.getText().toString();
                            if(newPass.length()>3&&newPass.length()<9){
                                editor.putString("password", newPass);
                                editor.apply();
                                Toasty.success(this,"Password Changed, Please Re-Login to see the Effect",Toasty.LENGTH_SHORT).show();
                            }
                            if(newPass.equals("0000")){
                                Toasty.error(this,"Password cannot be 0000",Toasty.LENGTH_SHORT).show();
                            }
                            else{
                                Toasty.error(this,"Password should be a number between\n 4-8 Digits",Toasty.LENGTH_LONG).show();
                            }

                        })
                .setNegativeButton("Cancel",
                        (dialog, id) -> dialog.cancel());
        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();

    }

}

