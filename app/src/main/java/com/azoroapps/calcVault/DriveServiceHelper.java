package com.azoroapps.calcVault;

import android.content.SharedPreferences;
import android.os.Environment;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import ir.mahdi.mzip.zip.ZipArchive;

import static com.azoroapps.calcVault.view.NewUser.SHARED_PREFS;

public class DriveServiceHelper {
    private final Executor mExecutor= Executors.newSingleThreadExecutor();
    private final Drive mDriveService;

    public DriveServiceHelper(Drive mDriveService){
        this.mDriveService=mDriveService;
    }

    public Task<String> createFilePDF(String filePath){
        return Tasks.call(mExecutor,()->{
            File  fileMetaData = new File();
            fileMetaData.setName("MyPDFFile");
            java.io.File file = new java.io.File(filePath);
            FileContent mediaContent=new FileContent("application/zip", file);

            File myFile= null;
            try{
                myFile=mDriveService.files().create(fileMetaData,mediaContent).execute();
            }
            catch (Exception e){
                e.printStackTrace();
            }
            if(myFile==null){
                throw new IOException("Null Result Requesting Filee Creation");
            }
            return myFile.getId();

        });
    }



}
