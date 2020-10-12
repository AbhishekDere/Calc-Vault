package com.azoroapps.calcVault.view;

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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.azoroapps.calcVault.R;
import com.azoroapps.calcVault.adapter.AlbumAdapter;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;

import es.dmoral.toasty.Toasty;


public class Album extends AppCompatActivity {
    ArrayList<String> mNames = new ArrayList<>();
    ArrayList<String> mImageUrls= new ArrayList<>();
    RecyclerView recyclerView;
    AlbumAdapter albumAdapter;
    boolean aBoolean;
    String[] directories;
    File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/.Vault/.Photos/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        if(!path.exists()){
            boolean k = path.mkdirs();
        }
        directories = path.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        if(!path.exists()) {
            aBoolean= path.mkdirs();
            if(aBoolean){
                Toasty.success(this, "Vault Created", Toasty.LENGTH_SHORT).show();
            }
        }
        listingAlbums();
    }

    private void listingAlbums(){
        mNames = new ArrayList<>(Arrays.asList(directories));
        if(mNames.isEmpty()){
            boolean k = path.mkdirs();
        }
        albumAdapter = new AlbumAdapter(this,mNames,mImageUrls);
        recyclerView= findViewById(R.id.recycler_id);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setAdapter(albumAdapter);

    }

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
                    boolean b;
                    String value = String.valueOf(input.getText());

                    File file= new File(path+"/."+value);
                    try{
                        if(!file.exists())
                        {
                                b = file.mkdirs();
                                if(b){
                                    Toasty.success(Album.this,"Folder Album Created Successfully",Toast.LENGTH_SHORT).show();
                                    startActivity(getIntent());
                                    finish();
                                }
                                else if(!path.exists()){
                                    Toasty.error(Album.this,"Vault not Found",Toast.LENGTH_SHORT).show();
                                }
                        }
                        else if(file.exists()){
                            Toasty.info(Album.this,"Album Exists, Try Different Name",Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toasty.info(Album.this,"Not Created",Toast.LENGTH_LONG).show();
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

        }
        return super.onOptionsItemSelected(item);
    }

}
