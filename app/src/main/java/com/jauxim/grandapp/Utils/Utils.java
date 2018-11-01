package com.jauxim.grandapp.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.jauxim.grandapp.R;
import com.jauxim.grandapp.ui.Activity.ActivityEdit.ActivityEditActivity;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class Utils {

    public static String getPriceFormated(Long eurocents) {
        return String.format("%.2l€", eurocents);
    }

    public static String getTimeFormated(String time) {
        //TODO: TBI
        return "";
    }

    public static float getAbsoluteDistance(Double lat1, Double lon1, float lat2, float lon2) {
        Location loc1 = new Location("");
        loc1.setLatitude(lat1);
        loc1.setLongitude(lon1);

        Location loc2 = new Location("");
        loc2.setLatitude(lat2);
        loc2.setLongitude(lon2);

        return loc1.distanceTo(loc2);
    }

    public static String getWalkingDistance(final double lat1, final double lon1, final double lat2, final double lon2) {
        final String[] parsedDistance = {null};
        final String[] response = {null};

        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Log.i("Output1", String.valueOf(lat1));
                    //Log.i("Output2", String.valueOf(lon1));
                    //Log.i("Output3", String.valueOf(lat2));
                    //Log.i("Output4", String.valueOf(lon2));
                    URL url = new URL("https://maps.googleapis.com/maps/api/directions/json?origin=" + lat1 + "," + lon1 + "&destination=" + lat2 + "," + lon2 + "&units=metric&mode=walking&key=AIzaSyCNhz3l4i6fPHxw0rvNpT_YUVZtsyGXs6o");
                    //Log.i("Output4.5", url.toString());
                    final HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    InputStream in = new BufferedInputStream(conn.getInputStream());
                    response[0] = org.apache.commons.io.IOUtils.toString(in, "UTF-8");

                    JSONObject jsonObject = new JSONObject(response[0]);
                    //Log.i("Output5", jsonObject.toString());
                    JSONArray array = jsonObject.getJSONArray("routes");
                    //Log.i("Output6", array.toString());
                    JSONObject routes = array.getJSONObject(0);
                    //Log.i("Output7", routes.toString());
                    JSONArray legs = routes.getJSONArray("legs");
                    JSONObject steps = legs.getJSONObject(0);
                    JSONObject distance = steps.getJSONObject("distance");
                    parsedDistance[0] = distance.getString("text");

                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return parsedDistance[0];
    }

    public static String getCountDownTime(Long millis) {
        Long diff = System.currentTimeMillis() - millis;

        if (diff < 0)
            return "GONE!";

        Long min = diff / (60 * 1000);
        if (min < 60)
            return min + " min";

        Long hours = min / 60;
        if (hours < 24)
            return hours + " hours";

        Long days = hours / 24;
        if (days < 7)
            return days + " days";

        return days / 7 + " weeks";
    }

    public static CropImage.ActivityBuilder createCropCamera(boolean ovalMask){
        CropImage.ActivityBuilder builder = com.theartofdev.edmodo.cropper.CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setActivityTitle("Crop Image")
                //.setCropMenuCropButtonIcon(R.drawable.check)
                .setScaleType(CropImageView.ScaleType.CENTER_INSIDE)
                .setMultiTouchEnabled(false);

        if (ovalMask) {
            builder.setAspectRatio(1, 1);
            builder.setCropShape(CropImageView.CropShape.OVAL);
            builder.setFixAspectRatio(true);
        } else {
            builder.setCropShape(CropImageView.CropShape.RECTANGLE);
        }

        return builder;
    }

    public static Bitmap getBitmapFromUri(Context context, Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                context.getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    public static void changeMapFragment(AppCompatActivity activity, int layoutId, Fragment fragment, String tag) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(layoutId, fragment, tag);
        fragmentTransaction.commit();
    }

}
