package com.org.kulture.kulture.gcm;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.org.kulture.kulture.MachineActivity;
import com.org.kulture.kulture.R;
import com.org.kulture.kulture.model.Config;
import com.org.kulture.kulture.model.JSONParse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;


/**
 * Created by bipul on 01/18/2017.
 */

//Class is extending GcmListenerService
public class GCMPushReceiverService extends GcmListenerService {
    //This method will be called on every new message received
    String messages,image,title,status,thumb_img;
    ArrayList<String> thumbImageList=new ArrayList<String>();
    String MACHINE_URL= "http://kulture.biz/webservice/vending-machine.php";
    int count;
    @Override
    public void onMessageReceived(String from, Bundle data) {
        //Getting the message from the bundle
        String message = data.getString("data");
        ++count;
      try {
            JSONObject c = new JSONObject(message);
            messages = c.getString("message");
            image = c.getString("image");
            title = c.getString("title");
            }catch (JSONException e) {
                e.printStackTrace();
            }
          //Displaying a notification with the message
             new Vending_Machine_Data().execute();
            // gcm successfully registered
            // now subscribe to `global` topic to receive app wide notifications
            FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
    }
    //--------------------------------------Vending Machine List -----------------------------------
    public class Vending_Machine_Data extends AsyncTask<String, String, Boolean> {
        JSONParse jsonParser = new JSONParse();
        @Override
        protected Boolean doInBackground(String... para) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();

                String json = jsonParser.makeHttpRequest(MACHINE_URL, "POST", params);
                try {
                    JSONObject c = new JSONObject(json);
                    status=c.getString("status");
                    JSONArray array = c.getJSONArray("vending_machine");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        thumb_img = object.getString("thumb_image");
                        thumbImageList.add(thumb_img);

                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }
                return null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(thumbImageList.size() >= 0){
                    String thumsup=thumbImageList.get(count);
                    try {
                        new generatePictureStyleNotification(GCMPushReceiverService.this, title, messages, thumsup).execute();
                    }catch (IndexOutOfBoundsException e){
                        e.printStackTrace();
                    }

            }else {
                try {
                    new generatePictureStyleNotification(GCMPushReceiverService.this, title, messages, "http://www.kulture.biz/wp-content/themes/kulture/images/logo.png").execute();
                }catch (IndexOutOfBoundsException e){
                    e.printStackTrace();
                }
            }
        }
    }


    public class generatePictureStyleNotification extends AsyncTask<String, Void, Bitmap> {

        private Context mContext;
        private String title, message, imageUrl;

        public generatePictureStyleNotification(Context context, String title, String message, String imageUrl) {
            super();
            this.mContext = context;
            this.title = title;
            this.message = message;
            this.imageUrl = imageUrl;
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            InputStream in;
            try {
                URL url = new URL(this.imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                in = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                return myBitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);

            Intent intent = new Intent(mContext, MachineActivity.class);
            intent.putExtra("key", "value");
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 100, intent, PendingIntent.FLAG_ONE_SHOT);

            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notif = new Notification.Builder(mContext)
                    .setContentIntent(pendingIntent)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.mipmap.small_logo)
                    .setLargeIcon(result)
                    .setStyle(new Notification.BigPictureStyle().bigPicture(result))
                    .build();
            notif.flags |= Notification.FLAG_AUTO_CANCEL;
            notificationManager.notify(1, notif);
        }
    }
}