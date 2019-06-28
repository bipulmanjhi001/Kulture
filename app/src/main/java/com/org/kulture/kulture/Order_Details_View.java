package com.org.kulture.kulture;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.org.kulture.kulture.model.JSONParse;
import com.org.kulture.kulture.model.MyBounceInterpolator;
import com.org.kulture.kulture.model.MyTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;

/**
 * Created by tkru on 10/31/2017.
 */

public class Order_Details_View extends AppCompatActivity {

    String title,price,image,weight,size_options,stock_quantity,qty;
    ImageView show_image,whishlist_on,whishlist_off,back_from_productdetails,product_cart;
    MyTextView product_name,product_weight_text,product_weight,product_size_text,product_sizelist;
    TextView updated_price,product_cart_count;
    TextView spinner_size,qty_value;

    String CART_COUNT_USER_LOGIN_URL="http://kulture.biz/webservice/count_cart_via_user_id.php?user_id=";
    String CART_COUNT_USER_LOGOFF_URL="http://kulture.biz/webservice/count_cart_via_order_id.php?order_id=";

    SharedPreferences preferences2,preferences3;
    String getids,getIMIE,cart_counts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details_view);
        changeStatusBarColor();

        title= getIntent().getStringExtra("title");
        price= getIntent().getStringExtra("price");
        image=getIntent().getStringExtra("image_url");
        weight= getIntent().getStringExtra("weight");
        size_options=getIntent().getStringExtra("size_options");
        stock_quantity=getIntent().getStringExtra("stock_quantity");

        show_image=(ImageView)findViewById(R.id.show_image);
        product_name=(MyTextView)findViewById(R.id.product_name);
        updated_price=(TextView)findViewById(R.id.updated_price);
        spinner_size=(TextView)findViewById(R.id.spinner_size);
        qty_value=(TextView)findViewById(R.id.qty_value);

        product_cart_count=(TextView)findViewById(R.id.product_cart_count);
        product_sizelist=(MyTextView)findViewById(R.id.product_sizelist);
        product_weight_text=(MyTextView)findViewById(R.id.product_weight_text);
        product_weight=(MyTextView)findViewById(R.id.product_weight);
        product_size_text=(MyTextView)findViewById(R.id.product_size_text);
        product_cart=(ImageView)findViewById(R.id.product_cart);
        product_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Order_Details_View.this, CartActivity.class);
                startActivity(intent);
                Order_Details_View.this.finish();
            }
        });

        back_from_productdetails=(ImageView)findViewById(R.id.back_from_productdetails);
        back_from_productdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Order_Details_View.this, Order_Details.class);
                startActivity(intent);
                Order_Details_View.this.finish();
            }
        });
        try {
            product_name.setText(title);
            updated_price.setText(price);
            if(stock_quantity.equals("-1") || stock_quantity.equals("null") || stock_quantity.equals("0") ) {
                qty_value.setText("1");
            }else {
                qty_value.setText(stock_quantity);
            }
            if(!weight.isEmpty()){
                product_weight_text.setVisibility(View.VISIBLE);
                product_weight.setVisibility(View.VISIBLE);
                product_weight.setText(weight);
            }
            if(!size_options.isEmpty()){
                    product_size_text.setVisibility(View.VISIBLE);
                    product_sizelist.setVisibility(View.VISIBLE);
                    product_sizelist.setText(size_options);
            }
            Glide.with(getApplicationContext())
                    .load(image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(0.5f)
                    .crossFade()
                    .skipMemoryCache(true)
                    .animate(R.anim.bounce)
                    .into(show_image);
            spinner_size.setText(size_options);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        whishlist_on=(ImageView)findViewById(R.id.whishlist_on);
        whishlist_off=(ImageView)findViewById(R.id.whishlist_off);
        whishlist_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whishlist_on.setVisibility(View.GONE);
                whishlist_off.setVisibility(View.VISIBLE);
                final Animation myAnim = AnimationUtils.loadAnimation(Order_Details_View.this, R.anim.bounce);
                // Use bounce interpolator with amplitude 0.2 and frequency 20
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.1, 10);
                myAnim.setInterpolator(interpolator);
                whishlist_off.startAnimation(myAnim);
            }
        });
        whishlist_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whishlist_off.setVisibility(View.GONE);
                whishlist_on.setVisibility(View.VISIBLE);
                final Animation myAnim = AnimationUtils.loadAnimation(Order_Details_View.this, R.anim.bounce);
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.1, 10);
                myAnim.setInterpolator(interpolator);
                whishlist_on.startAnimation(myAnim);
            }
        });

        product_cart_count=(TextView)findViewById(R.id.product_cart_count);

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
        Intent intent = new Intent(Order_Details_View.this, Order_Details.class);
        startActivity(intent);
        Order_Details_View.this.finish();
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
