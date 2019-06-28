package com.org.kulture.kulture;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.org.kulture.kulture.model.ExpandableHeightGridView;
import com.org.kulture.kulture.model.JSONParse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;

public class DesignYourOwn extends AppCompatActivity {
    ProgressBar design_own_progressBar;
    TextView design_own_title;
    ExpandableHeightGridView load_design_own_list;

    private Handler handler = new Handler();
    private static int SPLASH_TIME_OUT = 9000;
    ImageView back_from_productdetails,product_cart;

    SharedPreferences preferences2,preferences3;
    String getids,getIMIE,cart_counts;

    String CART_COUNT_USER_LOGIN_URL="http://kulture.biz/webservice/count_cart_via_user_id.php?user_id=";
    String CART_COUNT_USER_LOGOFF_URL="http://kulture.biz/webservice/count_cart_via_order_id.php?order_id=";

    TextView product_cart_count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design_your_own);

        design_own_progressBar=(ProgressBar)findViewById(R.id.design_own_progressBar);
        design_own_progressBar.setVisibility(View.VISIBLE);

        design_own_title=(TextView)findViewById(R.id.design_own_title);
        load_design_own_list=(ExpandableHeightGridView)findViewById(R.id.load_design_own_list);

        product_cart=(ImageView)findViewById(R.id.product_cart);
        product_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DesignYourOwn.this, CartActivity.class);
                startActivity(intent);
                DesignYourOwn.this.finish();
            }
        });
        back_from_productdetails=(ImageView)findViewById(R.id.back_from_productdetails);
        back_from_productdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DesignYourOwn.this, Homescreen.class);
                startActivity(intent);
                DesignYourOwn.this.finish();
            }
        });
        product_cart_count=(TextView)findViewById(R.id.product_cart_count);
        preferences2=getApplicationContext().getSharedPreferences("register_save", Context.MODE_PRIVATE);
        getids=preferences2.getString("adminid","");

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
                design_own_progressBar.setVisibility(View.GONE);
            }
        }, SPLASH_TIME_OUT);
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
                product_cart_count.setText(cart_counts);
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
                product_cart_count.setText(cart_counts);
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }
    }
}
