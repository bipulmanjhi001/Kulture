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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.org.kulture.kulture.model.Category_GridView_ImageAdapter;
import com.org.kulture.kulture.model.Category_Grid_Model;
import com.org.kulture.kulture.model.ConnectionDetector;
import com.org.kulture.kulture.model.ExpandableHeightGridView;
import com.org.kulture.kulture.model.JSONParse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;

public class CategoryListActivity extends AppCompatActivity {

    ImageView back_from_productdetails,product_cart;
    String slug,qty;
    String id,name;
    TextView category_title,product_cart_count;

    String CATEGORY_URL= "http://kulture.biz/webservice/get_product_by_category.php?catgory=";
    ProgressBar progressBar;
    private Boolean isInternetPresent = false;
    private ConnectionDetector cd;
    ExpandableHeightGridView load_sub_category_list;
    private Category_GridView_ImageAdapter category_gridView_imageAdapter;
    private ArrayList<Category_Grid_Model> mGridViewData;
    LinearLayout price_tag;
    String price;
    String img_url;

    String CART_COUNT_USER_LOGIN_URL="http://kulture.biz/webservice/count_cart_via_user_id.php?user_id=";
    String CART_COUNT_USER_LOGOFF_URL="http://kulture.biz/webservice/count_cart_via_order_id.php?order_id=";

    SharedPreferences preferences2,preferences3;
    String getids,getIMIE,cart_counts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        changeStatusBarColor();
        slug= getIntent().getStringExtra("slug");
        id=getIntent().getStringExtra("id");
        name=getIntent().getStringExtra("prename");
        category_title=(TextView)findViewById(R.id.category_title);
        try{
            category_title.setText(name);
        }catch(NullPointerException e){
            e.printStackTrace();
        }
        cd = new ConnectionDetector(getApplicationContext());

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
        price_tag=(LinearLayout)findViewById(R.id.price_tag);
        isInternetPresent = cd.isConnectingToInternet();
        load_sub_category_list=(ExpandableHeightGridView)findViewById(R.id.load_sub_category_list);
        load_sub_category_list.setExpanded(true);

        if (isInternetPresent) {
            mGridViewData = new ArrayList<Category_Grid_Model>();
            category_gridView_imageAdapter = new Category_GridView_ImageAdapter(this, R.layout.category_list, mGridViewData);
            load_sub_category_list.setAdapter(category_gridView_imageAdapter);
            progressBar = (ProgressBar) findViewById(R.id.progressBar);
            //making the progressbar visible
            progressBar.setVisibility(View.VISIBLE);
            new Category_Data().execute();
            load_sub_category_list.setVisibility(View.VISIBLE);
            load_sub_category_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                      Category_Grid_Model item = (Category_Grid_Model) parent.getItemAtPosition(position);
                        Intent intent = new Intent(CategoryListActivity.this, ProductDetail.class);
                        intent.putExtra("id", String.valueOf(item.getCat_sub_id()));
                        intent.putExtra("desc", item.getCat_sub_desc());
                        intent.putExtra("title", item.getCat_sub_name());
                        intent.putExtra("price", item.getCat_sub_price());
                        intent.putExtra("image", item.getCat_sub_image());
                        intent.putExtra("slug",slug);
                        intent.putExtra("prename",name);
                        intent.putExtra("session","CategoryListActivity");
                        startActivity(intent);
                        CategoryListActivity.this.finish();
                }
            });
        }
        back_from_productdetails=(ImageView)findViewById(R.id.back_from_productdetails);
        back_from_productdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CategoryListActivity.this,Homescreen.class);
                startActivity(intent);
                CategoryListActivity.this.finish();
            }
        });
        product_cart=(ImageView)findViewById(R.id.product_cart);
        product_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CategoryListActivity.this,CartActivity.class);
                startActivity(intent);
                CategoryListActivity.this.finish();
            }
        });
        product_cart_count=(TextView)findViewById(R.id.product_cart_count);
    }
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }
    }
 //--------------------------------------Active List ----------------------------
    private class Category_Data extends AsyncTask<String, String, Boolean> {
        JSONParse jsonParser = new JSONParse();
        @Override
        protected Boolean doInBackground(String... para) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();

                String json = jsonParser.makeHttpRequest(CATEGORY_URL+slug, "POST", params);
                try {
                    JSONObject c = new JSONObject(json);
                    JSONArray array = c.getJSONArray("products");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            price = object.getString("price");
                            String title = Html.fromHtml(object.getString("title")).toString();
                            String description = Html.fromHtml(object.getString("description")).toString();
                            Integer id = object.getInt("id");

                            JSONArray corporate = object.getJSONArray("images");
                           for (int j = 0; j < corporate.length(); j++) {
                                JSONObject corporateobject = corporate.getJSONObject(0);
                                   img_url = corporateobject.getString("src");
                            }
                            Category_Grid_Model category_grid_model = new Category_Grid_Model();
                            category_grid_model.setCat_sub_id(id);
                            category_grid_model.setCat_sub_price(price);
                            category_grid_model.setCat_sub_desc(description);
                            category_grid_model.setCat_sub_name(title);
                            category_grid_model.setCat_sub_image(img_url);
                            mGridViewData.add(category_grid_model);
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
         category_gridView_imageAdapter.notifyDataSetChanged();
         progressBar.setVisibility(View.GONE);
         load_sub_category_list.setVisibility(View.VISIBLE);
     }
 }
    @Override
    public void onBackPressed() {
        Intent intent=new Intent(CategoryListActivity.this,Homescreen.class);
        startActivity(intent);
        CategoryListActivity.this.finish();
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
