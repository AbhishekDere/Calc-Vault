package com.azoroapps.calcVault;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.io.File;

import es.dmoral.toasty.Toasty;

public class NewUser extends AppCompatActivity {

    TextView button0,button1, button2, button3, button4, button5, button6, button7, button8, button9, tvDot,
             tvEqual, buttonClear, tvResult, tvExpression;
    ImageView tvBack;
    PrefManager prefManager;
    private int STORAGE_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        prefManager = new PrefManager(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);

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
        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendOnExpression("0",true);
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendOnExpression("1",true);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendOnExpression("2",true);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendOnExpression("3",true);
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendOnExpression("4",true);
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendOnExpression("5",true);
            }
        });
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendOnExpression("6",true);
            }
        });
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendOnExpression("7",true);
            }
        });
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendOnExpression("8",true);
            }
        });
        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendOnExpression("9",true);
            }
        });
        tvDot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendOnExpression(".",true);
            }
        });

        //Equal Function
        tvEqual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                                        prefManager.setFirstTimeLaunch(false);
                                        Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
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

                                        // If user click no
                                        // then dialog box is canceled.
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alertDialog = builder.create();

                // Show the Alert Dialog box
                alertDialog.show();
                /*
                Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();
                    */

            }
        });

        //Additional
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvExpression.setText("");
                tvResult.setText("");
            }
        });
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String val =tvExpression.getText().toString();
                int length=tvExpression.length();
                if(length>0){
                    tvExpression.setText(val.substring(0,length-1));}
                tvResult.setText("");
            }
        });
    }

    private void appendOnExpression(String value, Boolean canClear){
        if (tvResult.getText()!=null){
            tvResult.setText("");
        }
        if(tvExpression.getText().equals("Set Your Password")){
            tvExpression.setText("");

        }
        if(canClear){
            tvResult.setText("");
            tvExpression.append(value);
        }
        else{
            tvExpression.append(tvResult.getText());
            tvExpression.append(value);
            tvResult.setText("");
        }
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
}