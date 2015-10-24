package com.akarmo.pega;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;

import java.io.File;

public class DetectPasswordFailed extends DeviceAdminReceiver {

    private static final String TAG = "LockScreenPolicyAdminReceiver";

    @Override
    public void onPasswordFailed(Context context, Intent intent) {
        SharedPreferences.Editor editor;
        SharedPreferences prefs;
        prefs = context.getSharedPreferences(
                "com.akarmo.pega", Context.MODE_PRIVATE);
        String fileName;
        try{
            if (prefs.getBoolean("TakeAfterWrongPass", false)) {

                DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);


                // TODO invoke after X tries
                fileName = Environment.getExternalStorageDirectory().getPath()+"/Pega/"+ TakePicture.takePicture(System.currentTimeMillis() + ".jpg");
                if(prefs.getBoolean("SendEmail", false)) {
                    new SendEmail(context).execute(fileName, prefs.getString("email", ""));
                    File file =  new File(fileName);
                    file.delete();
                }

            }
        }catch(Throwable e){
            e.printStackTrace();
        }

    }

}