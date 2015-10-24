package com.akarmo.pega;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;

import java.io.File;


public class DetectUnlockReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent arg1) {
        SharedPreferences.Editor editor;
        SharedPreferences prefs;
        prefs = context.getSharedPreferences(
                "com.akarmo.pega", Context.MODE_PRIVATE);
        String fileName;
        try{
            if (prefs.getBoolean("AlwaysTakePicture", false)) {

                DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);


                // TODO invoke after X tries

                fileName = Environment.getExternalStorageDirectory().getPath()+"/Pega/"+ TakePicture.takePicture(System.currentTimeMillis() + ".jpg");
                if(prefs.getBoolean("SendEmail", false))
                    new SendEmail(context).execute(fileName, prefs.getString("email", ""));
                File file =  new File(fileName);
                file.delete();
            }
        }catch(Throwable e){
            e.printStackTrace();
        }
	}


}
