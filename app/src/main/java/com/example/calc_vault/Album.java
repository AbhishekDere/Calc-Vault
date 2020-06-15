package com.example.calc_vault;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;


public class Album extends AppCompatActivity {
    File path = new File(Environment.getExternalStorageDirectory().toString()+File.separator+"Abhi");
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        try{
            if (path.exists()){
                ArrayList<String> albums = new ArrayList<>(Arrays.asList(Objects.requireNonNull(path.list())));
                RecyclerView recyclerView= findViewById(R.id.recycler_id);
                AlbumAdapter albumAdapter = new AlbumAdapter(albums);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(albumAdapter);
            }
            else if(!path.exists()){
                if(path.mkdirs())
                {
                    Toast.makeText(Album.this,"Secret Vault Created",Toast.LENGTH_SHORT).show();
                }
            }
        }catch(NullPointerException e){
            Toast toast=Toast.makeText(this,"BLA BLA BLA",Toast.LENGTH_SHORT);
        }


    }
    //Option Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_album,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==(R.id.AddNew)){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Create New Album");
            alert.setMessage("Enter Your Album Name");
            final EditText input = new EditText(this);
            alert.setView(input);

            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String value = String.valueOf(input.getText());
                    // Do something with value!
                    File file= new File(path+File.separator+value);
                    try{
                        if(!file.exists())
                        {
                            if(file.mkdirs())
                            {
                                Toast.makeText(Album.this,"Folder Album Created Successfully",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(Album.this,"Not Created",Toast.LENGTH_LONG).show();
                            }
                        }
                        else if(file.exists()){
                            Toast.makeText(Album.this,"Folder Exists",Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (NullPointerException e) {
                        // Unable to create file, likely because external storage is
                        // not currently mounted.
                        Log.w("ExternalStorage", "Error writing " + file, e);
                    }
                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Canceled.
                }
            });

            alert.show();
          /*  Intent intent = new Intent();
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_MULTIPLE);*/
        }
        return super.onOptionsItemSelected(item);
    }

}
