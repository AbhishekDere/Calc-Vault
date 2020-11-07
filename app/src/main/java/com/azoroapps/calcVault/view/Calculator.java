package com.azoroapps.calcVault.view;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.azoroapps.calcVault.R;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import es.dmoral.toasty.Toasty;

import static com.azoroapps.calcVault.NewUser.SHARED_PREFS;

public class Calculator extends AppCompatActivity {
    TextView button0,button1, button2, button3, button4, button5, button6, button7, button8, button9, tvDot, buttonAdd,
            buttonSub, buttonMul, buttonDiv, tvOpen, tvClose, tvEqual, buttonClear, tvResult, tvExpression;
    ImageView tvBack;
    int STORAGE_PERMISSION_CODE;
    private String text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            STORAGE_PERMISSION_CODE = 1;
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

        }
        Toasty.Config.reset();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);


        //Assigning the number buttons to layout
        initControl();

        //Setting onClickListeners
        initControlListener();
        //tvExpression.setText("");
    }

    private void initControlListener() {
        //Numbers
        button0.setOnClickListener(v -> appendOnExpression("0",true));
        button1.setOnClickListener(v -> appendOnExpression("1",true));
        button2.setOnClickListener(v -> appendOnExpression("2",true));
        button3.setOnClickListener(v -> appendOnExpression("3",true));
        button4.setOnClickListener(v -> appendOnExpression("4",true));
        button5.setOnClickListener(v -> appendOnExpression("5",true));
        button6.setOnClickListener(v -> appendOnExpression("6",true));
        button7.setOnClickListener(v -> appendOnExpression("7",true));
        button8.setOnClickListener(v -> appendOnExpression("8",true));
        button9.setOnClickListener(v -> appendOnExpression("9",true));
        tvDot.setOnClickListener(v -> appendOnExpression(".",true));
        //Operators
        buttonAdd.setOnClickListener(v -> appendOnExpression("+",false));
        buttonSub.setOnClickListener(v -> appendOnExpression("-",false));
        buttonMul.setOnClickListener(v -> appendOnExpression("*",false));
        buttonDiv.setOnClickListener(v -> appendOnExpression("/",false));
        tvOpen.setOnClickListener(v -> appendOnExpression("(",false));
        tvClose.setOnClickListener(v -> appendOnExpression(")",false));

        //Equal Function
        tvEqual.setOnClickListener(v -> {
            String txt = tvExpression.getText().toString();
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            text = sharedPreferences.getString("password", "");
            if(txt.equals("0/"+text)){
                Intent intent= new Intent (this, VaultScreen.class);
                startActivity(intent);
                finish();
            }
            else{
                try {
                    Expression expression = new ExpressionBuilder(txt).build();
                    double result = expression.evaluate();
                    tvResult.setText(String.valueOf(result));
                } catch (Exception e){
                    Log.d("Exception","Error");
                }
            }
        });

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

    private void appendOnExpression(String value, Boolean canClear){
        if (tvResult.getText()!=null){
            tvResult.setText("");
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
        buttonAdd = findViewById(R.id.tvPlus);
        buttonSub = findViewById(R.id.tvMinus);
        buttonMul = findViewById(R.id.tvMul);
        buttonDiv = findViewById(R.id.tvDivide);
        tvOpen = findViewById(R.id.tvOpen);
        tvClose = findViewById(R.id.tvClose);
        tvEqual = findViewById(R.id.tvEquals);
        buttonClear = findViewById(R.id.tvClear);
        tvExpression = findViewById(R.id.tvExpression);
        tvResult = findViewById(R.id.tvResult);
        tvBack =  findViewById(R.id.tvBack);
    }
}