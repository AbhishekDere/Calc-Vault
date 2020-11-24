package com.azoroapps.calcVault;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.azoroapps.calcVault.utilities.PrefManager;

import es.dmoral.toasty.Toasty;

public class NewUser extends AppCompatActivity {

    TextView button0,button1, button2, button3, button4, button5, button6, button7, button8, button9, tvDot,
             tvEqual, buttonClear, tvResult, tvExpression;
    ImageView tvBack;
    PrefManager prefManager;
    public static final String SHARED_PREFS = "sharedPrefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        prefManager = new PrefManager(getApplicationContext());
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            int STORAGE_PERMISSION_CODE = 1;
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

        }
        Toasty.Config.reset();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        //Assigning the number buttons to layout
        initControl();

        //Setting onClickListeners
        initControlListener();
    }

    private void initControlListener() {
        //Numbers
        button0.setOnClickListener(v -> appendOnExpression("0"));
        button1.setOnClickListener(v -> appendOnExpression("1"));
        button2.setOnClickListener(v -> appendOnExpression("2"));
        button3.setOnClickListener(v -> appendOnExpression("3"));
        button4.setOnClickListener(v -> appendOnExpression("4"));
        button5.setOnClickListener(v -> appendOnExpression("5"));
        button6.setOnClickListener(v -> appendOnExpression("6"));
        button7.setOnClickListener(v -> appendOnExpression("7"));
        button8.setOnClickListener(v -> appendOnExpression("8"));
        button9.setOnClickListener(v -> appendOnExpression("9"));
        tvDot.setOnClickListener(v -> appendOnExpression("."));

        //Equal Function
        tvEqual.setOnClickListener(v -> showDialogue());
        //Additional
        buttonClear.setOnClickListener(v -> {
            tvExpression.setText("");
            tvResult.setText("");
        });
        tvBack.setOnClickListener(v -> {
            String val =tvExpression.getText().toString();
            int length=tvExpression.length();
            if(length>0){
                tvExpression.setText(val.substring(0,length-1));}
            tvResult.setText("");
        });
    }

    private void appendOnExpression(String value){
        if (tvResult.getText()!=null){
            tvResult.setText("");
        }
        if(tvExpression.getText().equals("Set Your Password")){
            tvExpression.setText("");

            int maxLength = 8;
            InputFilter[] fArray = new InputFilter[1];
            fArray[0] = new InputFilter.LengthFilter(maxLength);
            tvExpression.setFilters(fArray);
        }
        tvResult.setText("");
        tvExpression.append(value);
    }

    private void initControl() {
        button0 = findViewById(R.id.tvZero);
        button1 = findViewById(R.id.tvOne);
        button2 = findViewById(R.id.tvTwo);
        button3 = findViewById(R.id.tvThree);
        button4 = findViewById(R.id.tvFour);
        button5 = findViewById(R.id.tvFive);
        button6 = findViewById(R.id.tvSix);
        button7 = findViewById(R.id.tvSeven);
        button8 = findViewById(R.id.tvEight);
        button9 = findViewById(R.id.tvNine);
        tvDot= findViewById(R.id.tvDot);
        tvEqual = findViewById(R.id.tvEquals);
        buttonClear = findViewById(R.id.tvClear);
        tvExpression = findViewById(R.id.tvExpression);
        tvResult = findViewById(R.id.tvResult);
        tvBack =  findViewById(R.id.tvBack);
    }

    private void showDialogue(){
        String txt = tvExpression.getText().toString();
        String bla ="To Unlock the vault, press 0/"+txt+" and press the Equal Sign to Login. Press OK again to Re-Login";
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(NewUser.this);
        builder.setMessage(bla);
        builder.setCancelable(false);

        // Set Alert Title
        builder.setTitle("Great");
        builder
                .setPositiveButton(
                        "Yes",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {
                                savePassword();
                                prefManager.setFirstTimeLaunch(false);
                                Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                                assert i != null;
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                                finish();
                            }
                        });
        builder
                .setNegativeButton(
                        "No",
                        new DialogInterface
                                .OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void savePassword() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("password", tvExpression.getText().toString());
        editor.apply();
        Toasty.success(this,"Password Saved, Re-Login",Toasty.LENGTH_SHORT).show();
    }

}