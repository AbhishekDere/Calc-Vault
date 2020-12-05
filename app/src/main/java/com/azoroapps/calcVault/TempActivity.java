package com.azoroapps.calcVault;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import es.dmoral.toasty.Toasty;
import ir.mahdi.mzip.zip.ZipArchive;
import static com.azoroapps.calcVault.view.NewUser.SHARED_PREFS;

public class TempActivity extends AppCompatActivity{
    String loc = ".Vault";
    File file = new File(Environment.getExternalStorageDirectory() + "/" + loc+"/");
    File destination = new File(Environment.getExternalStorageDirectory().getPath()+"/AndroidFiles.zip");
    Button button;

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
        /*button.setOnClickListener(v -> {
            Toast.makeText(TempActivity.this, "clicked on button", Toast.LENGTH_LONG).show();
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("password", "5555");
            editor.apply();
        });
        */
        Toasty.success(this,"Password Saved, Re-Login",Toasty.LENGTH_SHORT).show();
    }
}