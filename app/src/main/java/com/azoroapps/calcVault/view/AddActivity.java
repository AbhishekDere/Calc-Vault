package com.azoroapps.calcVault.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.InflateException;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.azoroapps.calcVault.R;

import java.io.FileOutputStream;
import java.io.IOException;

public class AddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add);
            save();
            back();
        } catch (InflateException e) {
            e.printStackTrace();
        }
    }

    private void save() {
        findViewById(R.id.imgSave).setOnClickListener(v -> {
            //Displaying alert dialog if user has not provided any file name
            Animation animation = AnimationUtils.loadAnimation(AddActivity.this,R.anim.bubble);
            findViewById(R.id.imgSave).startAnimation(animation);
            String filename = ((EditText)findViewById(R.id.edtFileName)).getText().toString();
            if(filename.equals("")){
                AlertDialog.Builder builder = new AlertDialog.Builder((AddActivity.this));
                builder.setIcon(R.drawable.ic_error_48px);
                builder.setTitle("Title of Note Missing");
                builder.setMessage("Please enter Title of the note and then save !");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.create().show();
            }else{
                //Saving notes, displaying toast msg and disabling filename textbox
                String flName = ((EditText)findViewById(R.id.edtFileName)).getText().toString();
                String txtOfNotepad = ((EditText)findViewById(R.id.edtNotepad)).getText().toString();
                try {
                    FileOutputStream fos = openFileOutput(flName+".txt",MODE_APPEND);
                    fos.write(txtOfNotepad.getBytes());
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                findViewById(R.id.edtFileName).setEnabled(false);
                Toast.makeText(AddActivity.this, "Note Saved", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void back() {
        findViewById(R.id.imgBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to previous screen only if the filename textbox is disabled i,e file is saved
                // of filename textbox and notepad text are empty i.e, user has not typed anything
                Animation animation = AnimationUtils.loadAnimation(AddActivity.this,R.anim.bubble);
                findViewById(R.id.imgSave).startAnimation(animation);
                EditText fileName = findViewById(R.id.edtFileName);
                String textNote = ((EditText)findViewById(R.id.edtNotepad)).getText().toString();
                String textFilename = ((EditText)findViewById(R.id.edtFileName)).getText().toString();
                if(  ( (textNote.equals("") ) && (textFilename.equals("")) ) || (!fileName.isEnabled())){
                    Intent intent=new Intent(AddActivity.this, NotesActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}