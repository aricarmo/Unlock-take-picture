package com.akarmo.pega;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

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
    private LocationManager locationManager;
    private Location location;
    private double latitude;
    private double longitude;
    String msgLocation = null;

    @Override
    protected void onPreExecute() {

    }

    public SendEmail(Context context){
        mContext = context;
         prefs = mContext.getSharedPreferences(
                 "com.akarmo.pega", Context.MODE_PRIVATE);

        locationManager = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);

        for (String provider : providers) {
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (location == null || l.getAccuracy() < location.getAccuracy()) {
                // Found best last known location: %s", l);
                location = l;
            }
        }

    }

    @Override
    protected Void doInBackground(String... params) {
        // TODO Auto-generated method stub
        //get gps location
        if (location != null) {
            msgLocation = "Someone tried to unlock your phone.\n The location is: https://www.google.com.br/maps/place/"+location.getLatitude() +","+ location.getLongitude();
        }else{
            msgLocation = "We can't get location of your cellphone, maybe the gps is offline";
        }
        if (prefs.getBoolean("SendEmail", false)) {
            GMailSender sender = new GMailSender(cf.email, cf.emailPassword);

            try {
                if (sender.sendMail("Someone is trying to use your phone!",
                        msgLocation,
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

