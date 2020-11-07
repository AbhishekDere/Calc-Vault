package com.azoroapps.calcVault.utilities;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DriveServiceHelper {
    private final Executor mExecuter=Executors.newSingleThreadExecutor();
    private final Drive mDriveService;

    public DriveServiceHelper(Drive mDriveService){
        this.mDriveService=mDriveService;

    }
    public Task<String> createFilePDF(String filePath){
        return Tasks.call(mExecuter,()->{
             File fileMetaData = new File();
             fileMetaData.setName("MyPDFFile");
             java.io.File file=new java.io.File(filePath);
            FileContent mediaContent=new FileContent("application/pdf", file);
            File myFile=null;
            try{
                myFile=mDriveService.files().create(fileMetaData,mediaContent).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (myFile==null){
                throw new IOException("Null result while Requesting File Creation");
            }
            return myFile.getId();
        });
    }
}
