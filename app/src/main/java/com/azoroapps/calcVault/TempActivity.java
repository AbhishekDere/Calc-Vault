package com.azoroapps.calcVault;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.api.client.http.FileContent;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import es.dmoral.toasty.Toasty;
import ir.mahdi.mzip.zip.ZipArchive;
import static com.azoroapps.calcVault.view.NewUser.SHARED_PREFS;

public class TempActivity extends AppCompatActivity{
    String loc = ".Vault";
    File file = new File(Environment.getExternalStorageDirectory() + "/" + loc+"/");
    File destination = new File(Environment.getExternalStorageDirectory().getPath()+"/AndroidFiles.zip");
    Button button;

    private final Executor mExecutor= Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);
        button = findViewById(R.id.temp_button);

        button.setOnClickListener(new View.OnClickListener() {
            final SharedPreferences sharedPreferences= getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            @Override
            public void onClick(View v) {
                ZipArchive zipArchive = new ZipArchive();
                ZipArchive.zip(file.getPath(),destination.getPath(),sharedPreferences.getString("password", ""));
            }
        });

        Toasty.success(this,"Password Saved, Re-Login",Toasty.LENGTH_SHORT).show();
    }







}