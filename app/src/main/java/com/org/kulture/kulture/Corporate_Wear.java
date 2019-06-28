package com.org.kulture.kulture;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.org.kulture.kulture.model.ConnectionDetector;
import com.org.kulture.kulture.model.Corporate_GridVierw_ImageAdapter;
import com.org.kulture.kulture.model.Corporate_Wear_Model;
import com.org.kulture.kulture.model.ExpandableHeightGridView;
import com.org.kulture.kulture.model.JSONParse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;

public class Corporate_Wear extends AppCompatActivity {
    ImageView back_from_productdetails,product_cart;
    String title;
    TextView cart_item;
    TextView category_name;
    String ids,name;
    String CATEGORY_URL= "http://kulture.biz/webservice/get_product_by_category.php?catgory=";
    ProgressBar progressBar;
    private Boolean isInternetPresent = false;
    private ConnectionDetector cd;
    ExpandableHeightGridView load_corporate_list;
    private Corporate_GridVierw_ImageAdapter corporate_gridVierw_imageAdapter;
    private ArrayList<Corporate_Wear_Model> corporate_wear_models;
    String img_url,img_;

    String CART_COUNT_USER_LOGIN_URL="http://kulture.biz/webservice/count_cart_via_user_id.php?user_id=";
    String CART_COUNT_USER_LOGOFF_URL="http://kulture.biz/webservice/count_cart_via_order_id.php?order_id=";

    SharedPreferences preferences2,preferences3;
    String getids,getIMIE,cart_counts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corporate__wear);
        changeStatusBarColor();

        title = getIntent().getStringExtra("slug");
        ids = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
        category_name = (TextView) findViewById(R.id.category_name_corporate);
        category_name.setText(name);

        cart_item = (TextView) findViewById(R.id.product_cart_count);
        back_from_productdetails = (ImageView) findViewById(R.id.back_from_productdetails);
        back_from_productdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Corporate_Wear.this, Homescreen.class);
                startActivity(intent);
                Corporate_Wear.this.finish();
            }
        });
        product_cart = (ImageView) findViewById(R.id.product_cart);
        product_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Corporate_Wear.this, CartActivity.class);
                startActivity(intent);
                Corporate_Wear.this.finish();
            }
        });
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

        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        load_corporate_list = (ExpandableHeightGridView) findViewById(R.id.load_corporate_wear_list);
        load_corporate_list.setExpanded(true);
        if (isInternetPresent) {
            corporate_wear_models = new ArrayList<Corporate_Wear_Model>();
            corporate_gridVierw_imageAdapter = new Corporate_GridVierw_ImageAdapter(this, R.layout.corporate_list, corporate_wear_models);
            load_corporate_list.setAdapter(corporate_gridVierw_imageAdapter);

            progressBar = (ProgressBar) findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
            new Corporate_Wear_Data().execute();
            load_corporate_list.setVisibility(View.VISIBLE);

            load_corporate_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    Corporate_Wear_Model item = (Corporate_Wear_Model) parent.getItemAtPosition(position);
                    Intent intent = new Intent(Corporate_Wear.this, Inquiry_Form.class);
                    intent.putExtra("id", String.valueOf(item.getCor_id()));
                    intent.putExtra("title", item.getCor_name());
                    intent.putExtra("image", item.getCor_image());
                    intent.putExtra("imgarray",item.getCor_image2());
                    intent.putExtra("name", name);
                    intent.putExtra("ids",ids);
                    intent.putExtra("slug",title);
                    intent.putExtra("findsession","Corporate_Wear");
                    startActivity(intent);
                    Corporate_Wear.this.finish();
                }
            });
        }
    }
    //--------------------------------------Active List ----------------------------
    private class Corporate_Wear_Data extends AsyncTask<String, String, Boolean> {
        JSONParse jsonParser = new JSONParse();
        @Override
        protected Boolean doInBackground(String... para) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();

                String json = jsonParser.makeHttpRequest(CATEGORY_URL+title, "POST", params);
                try {
                    JSONObject c = new JSONObject(json);
                    JSONArray array = c.getJSONArray("products");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        String title = Html.fromHtml(object.getString("title")).toString();
                        Integer id = object.getInt("id");
                        JSONArray corporate = object.getJSONArray("images");
                        for (int j = 0; j < corporate.length(); j++) {
                            JSONObject corporateobject = corporate.getJSONObject(0);
                            img_url = corporateobject.getString("src");
                        }
                        for(int k=0; k < corporate.length(); k++) {
                            JSONObject corporateobject = corporate.getJSONObject(k);
                            img_ = corporateobject.getString("src");
                        }
                        Corporate_Wear_Model corporate_wear_model = new Corporate_Wear_Model();
                        corporate_wear_model.setCor_id(id);
                        corporate_wear_model.setCor_name(title);
                        corporate_wear_model.setCor_image(img_url);
                        corporate_wear_model.setCor_image2(img_);
                        corporate_wear_models.add(corporate_wear_model);
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
            corporate_gridVierw_imageAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
            load_corporate_list.setVisibility(View.VISIBLE);
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
        Intent intent=new Intent(Corporate_Wear.this,Homescreen.class);
        startActivity(intent);
        Corporate_Wear.this.finish();
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
                cart_item.setText(cart_counts);
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
                cart_item.setText(cart_counts);
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }
    }
}
