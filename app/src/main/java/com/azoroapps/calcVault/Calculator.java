package com.azoroapps.calcVault;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.io.File;

public class Calculator extends AppCompatActivity {

    TextView button0,button1, button2, button3, button4, button5, button6, button7, button8, button9, tvDot, buttonAdd,
            buttonSub, buttonMul, buttonDiv, tvOpen, tvClose, tvEqual, buttonClear, tvResult, tvExpression;
    ImageView tvBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

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
        //Operators
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendOnExpression("+",false);
            }
        });
        buttonSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendOnExpression("-",false);
            }
        });
        buttonMul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendOnExpression("*",false);
            }
        });
        buttonDiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendOnExpression("/",false);
            }
        });
        tvOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendOnExpression("(",false);
            }
        });
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendOnExpression(")",false);
            }
        });

        //Equal Function
        tvEqual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = tvExpression.getText().toString();
                if(txt.equals("0/123")){
                    Intent intent= new Intent (getApplicationContext(),Vault.class);
                    startActivity(intent);
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