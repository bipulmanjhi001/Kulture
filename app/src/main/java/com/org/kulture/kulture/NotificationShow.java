package com.org.kulture.kulture;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.org.kulture.kulture.model.JSONParse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;


public class NotificationShow extends AppCompatActivity {

    ImageView back_from_cart,order_image_cart;
    String qty;
    TextView cart_title,order_cart_count,emptyElement;
    ListView notificationlist;
    String CART_COUNT_USER_LOGIN_URL="http://kulture.biz/webservice/count_cart_via_user_id.php?user_id=";
    String CART_COUNT_USER_LOGOFF_URL="http://kulture.biz/webservice/count_cart_via_order_id.php?order_id=";

    SharedPreferences preferences2,preferences3;
    String getids,getIMIE,cart_counts;
    ProgressBar progressBar;

    private Handler handler = new Handler();
    private static int SPLASH_TIME_OUT = 4000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        changeStatusBarColor();
        back_from_cart=(ImageView)findViewById(R.id.back_from_cart);
        back_from_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(NotificationShow.this,Homescreen.class);
                startActivity(intent);
                NotificationShow.this.finish();
            }
        });
        order_image_cart=(ImageView)findViewById(R.id.order_image_cart);
        order_image_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(NotificationShow.this,CartActivity.class);
                startActivity(intent);
                NotificationShow.this.finish();
            }
        });
        notificationlist=(ListView)findViewById(R.id.notificationlist);
        cart_title=(TextView)findViewById(R.id.cart_title);
        cart_title.setText("Notifications");
        order_cart_count=(TextView)findViewById(R.id.order_cart_count);

        emptyElement=(TextView)findViewById(R.id.emptyElement);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        preferences2=getApplicationContext().getSharedPreferences("register_save", Context.MODE_PRIVATE);
        getids=preferences2.getString("adminid","");
        Log.d("getid",getids);

        preferences3=getApplicationContext().getSharedPreferences("mobimei_save", Context.MODE_PRIVATE);
        getIMIE=preferences3.getString("imei","");

        if(!getids.isEmpty()&& !getIMIE.isEmpty()) {
            try {
                new Login_Cart_Count().execute();
            }catch (Exception e){
                e.printStackTrace();
            }
        }else if(getids.isEmpty()&& !getIMIE.isEmpty()){
            try {
                new Logoff_Cart_Count().execute();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                emptyElement.setVisibility(View.VISIBLE);
            }
        }, SPLASH_TIME_OUT);
    }
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent=new Intent(NotificationShow.this,Homescreen.class);
        startActivity(intent);
        NotificationShow.this.finish();
    }

    //----------------------------------------------LogIn Cart Count------------------------------------------------
    private class Login_Cart_Count extends AsyncTask<String, String, Boolean> {
        JSONParse jsonParser = new JSONParse();
        @Override
        protected Boolean doInBackground(String... para) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                String json = jsonParser.makeHttpRequest(CART_COUNT_USER_LOGIN_URL+getids, "POST", params);
                try {
                    JSONObject c = new JSONObject(json);
                    cart_counts=c.getString("cart_count");

                } catch (JSONException e) {
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
            try {
                order_cart_count.setText(cart_counts);
            }catch (NullPointerException e){
                e.printStackTrace();
            }

        }
    }
    //---------------------------------------------- Logoff Cart Count------------------------------------------------
    private class Logoff_Cart_Count extends AsyncTask<String, String, Boolean> {
        JSONParse jsonParser = new JSONParse();
        @Override
        protected Boolean doInBackground(String... para) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                String json = jsonParser.makeHttpRequest(CART_COUNT_USER_LOGOFF_URL+getIMIE, "POST", params);
                try {
                    JSONObject c = new JSONObject(json);
                    cart_counts=c.getString("cart_count");
                } catch (JSONException e) {
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
            try {
                order_cart_count.setText(cart_counts);
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }
    }

}
