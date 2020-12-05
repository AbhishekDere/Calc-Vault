package com.azoroapps.calcVault.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.azoroapps.calcVault.adapter.FileAdapter;
import com.azoroapps.calcVault.R;
import com.azoroapps.calcVault.utilities.FileItem;

import java.io.File;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class EditActivity extends AppCompatActivity {
    String strFileName;
    public static final String KEY_NAME = "fileName";
    public static final String KEY_INFO = "noteInfo";
    public static final String KEY_POSITION = "clickedPosition";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        //loading all the files present in the internal storage directory
        loadFileList();

        //if back id pressed-MainActivity will be shown
        findViewById(R.id.imgListBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation animation = AnimationUtils.loadAnimation(EditActivity.this,R.anim.bubble);
                findViewById(R.id.imgListBack).startAnimation(animation);

                Intent intentList = new Intent(EditActivity.this, NotesActivity.class);
                startActivity(intentList);
                finish();
            }
        });
    }

    private void loadFileList() {

        final List<FileItem> fileItems = new ArrayList<>();

        File file = getFilesDir();
        File []files = file.listFiles();

        for(File fl:files){
            //or formatting date in desired way
            Date date = new Date(fl.lastModified());

            String size = convertSizeLong(fl.length());

            fileItems.add(new FileItem(R.drawable.ic_writing,fl.getName(),""+(new SimpleDateFormat("dd-MMM-yyyy HH-mm-ss").format(date)),""+size,""));
        }

        ((ListView)findViewById(R.id.lstListOfFiles)).setAdapter(new FileAdapter(this,fileItems));

        //opening individual file in new activity for editing with delete and back option
        ((ListView) findViewById(R.id.lstListOfFiles)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                FileItem clickedItem = fileItems.get(position);

                String strFileName = clickedItem.txtFlName;

                Bundle bundle=new Bundle();

                bundle.putString(KEY_NAME,strFileName);

                startActivity(new Intent(EditActivity.this,EditFileActivity.class).putExtras(bundle));
            }
        });
        ((ListView) findViewById(R.id.lstListOfFiles)).setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                //TODO use the long CLick options menu and set it up to perform operations


                AlertDialog.Builder builder = new AlertDialog.Builder((EditActivity.this));
                builder.setIcon(R.drawable.ic_error_48px);
                builder.setTitle("Warning: Delete");
                builder.setMessage("    Delete the file permanently ?");

                builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String filePath = getFilesDir().getAbsolutePath();
                        File file = new File(filePath, String.valueOf(fileItems.get(position)));
                        Boolean deletedFileStatus = file.delete();
                        if(deletedFileStatus)
                            Toast.makeText(EditActivity.this,"Note Deleted",Toast.LENGTH_SHORT).show();
                        //gotoMainActivity();

                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });


                builder.create().show();

                Toasty.success(EditActivity.this,"CLicked on"+fileItems.get(position).txtFlName,Toasty.LENGTH_SHORT).show();
                return false;
            }
        });

    }

    private String convertSizeLong(long length) {

        String size = null;

        double bt =length;
        double kb = length/1024.0;
        double mb = ((length/1024.0)/1024.0);
        double gb = (((length/1024.0)/1024.0)/1024.0);
        double tb = ((((length/1024.0)/1024.0)/1024.0)/1024.0);

        DecimalFormat decFormat = new DecimalFormat("0.00");

        if(tb>1) {
            size = decFormat.format(tb).concat(" TB");
        } else if(gb>1) {
            size = decFormat.format(gb).concat(" GB");
        } else if(mb>1) {
            size = decFormat.format(mb).concat(" MB");
        } else if(kb>1) {
            size = decFormat.format(kb).concat(" kB");
        } else {
            size = decFormat.format(bt).concat(" Bytes");
        }

        return size;
    }
}