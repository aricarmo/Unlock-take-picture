package com.akarmo.pega;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by arilsoncarmo on 1/8/15.
 */
public class SendEmail extends AsyncTask<String, Void, Void> {

    ProgressDialog pd = null;
    String error = null;
    Integer result;
    private Context mContext;
    SharedPreferences.Editor editor;
    SharedPreferences prefs;
    config cf = new config();

    @Override
    protected void onPreExecute() {

    }

    public SendEmail(Context context){
        mContext = context;
         prefs = mContext.getSharedPreferences(
                "com.akarmo.pega", Context.MODE_PRIVATE);
    }

    @Override
    protected Void doInBackground(String... params) {
        // TODO Auto-generated method stub

        if (prefs.getBoolean("SendEmail", false)) {
            GMailSender sender = new GMailSender(cf.email, cf.emailPassword);

            try {
                if (sender.sendMail("Someone has trying to use your phone!",
                        "",
                        cf.email,
                        params[1], params[0])) {
                    System.out.println("Message sent");

                }
            } catch (Exception e) {
                error = e.getMessage();
                Log.e("SendMail", e.getMessage(), e);
            }
        }

        return null;
    }

    protected void onPostExecute(Integer result) {

    }
}

