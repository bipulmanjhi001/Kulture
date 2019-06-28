package com.org.kulture.kulture;

import android.app.FragmentTransaction;
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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.org.kulture.kulture.model.ConnectionDetector;
import com.org.kulture.kulture.model.ExpandableHeightGridView;
import com.org.kulture.kulture.model.JSONParse;
import com.org.kulture.kulture.model.SlideShow_Vending_Machine;
import com.org.kulture.kulture.model.Vending_Machine_GridView_ImageAdapter;
import com.org.kulture.kulture.model.Vending_Machine_Grid_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;

public class MachineActivity extends AppCompatActivity {
    ImageView back_from_productdetail,product_cart;
    TextView product_cart_count,category_title;
    String qty,message,status;

    String MACHINE_URL= "http://kulture.biz/webservice/vending-machine.php";
    ProgressBar progressBar;
    private Boolean isInternetPresent = false;
    private ConnectionDetector cd;
    ExpandableHeightGridView load_vending_machine_list;
    private Vending_Machine_GridView_ImageAdapter vending_machine_gridView_imageAdapter;
    private ArrayList<Vending_Machine_Grid_Model> mGridViewData;
    String large_img,thumb_img;
    ArrayList<String> largeImageList=new ArrayList<String>();

    String CART_COUNT_USER_LOGIN_URL="http://kulture.biz/webservice/count_cart_via_user_id.php?user_id=";
    String CART_COUNT_USER_LOGOFF_URL="http://kulture.biz/webservice/count_cart_via_order_id.php?order_id=";

    SharedPreferences preferences2,preferences3;
    String getids,getIMIE,cart_counts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine);

        changeStatusBarColor();
        category_title=(TextView)findViewById(R.id.category_title);
        category_title.setText("Vending Machine");
        back_from_productdetail=(ImageView)findViewById(R.id.back_from_productdetails);
        back_from_productdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MachineActivity.this,Homescreen.class);
                startActivity(intent);
                MachineActivity.this.finish();
            }
        });

        product_cart=(ImageView)findViewById(R.id.product_cart);
        product_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MachineActivity.this,CartActivity.class);
                startActivity(intent);
                MachineActivity.this.finish();
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
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        load_vending_machine_list=(ExpandableHeightGridView)findViewById(R.id.load_vending_machine_list);
        load_vending_machine_list.setExpanded(true);

        if (isInternetPresent) {
            mGridViewData = new ArrayList<Vending_Machine_Grid_Model>();
            vending_machine_gridView_imageAdapter = new Vending_Machine_GridView_ImageAdapter(this, R.layout.vending_machine_list, mGridViewData);
            load_vending_machine_list.setAdapter(vending_machine_gridView_imageAdapter);
            //getting the progressbar
            progressBar=(ProgressBar)findViewById(R.id.machine_progressbar);
            progressBar.setVisibility(View.VISIBLE);

            new Vending_Machine_Data().execute();
            load_vending_machine_list.setVisibility(View.VISIBLE);

            load_vending_machine_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    Vending_Machine_Grid_Model item = (Vending_Machine_Grid_Model) parent.getItemAtPosition(position);
                    if (largeImageList.size() >= 0) {
                        try {
                            Bundle data = new Bundle();
                            data.putStringArrayList("imagearray", largeImageList);
                            data.putString("clickimg", item.getLarge_image());
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            SlideShow_Vending_Machine frag = new SlideShow_Vending_Machine();
                            frag.setArguments(data);
                            frag.show(ft, "txn_tag");
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
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
        Intent intent=new Intent(MachineActivity.this,Homescreen.class);
        startActivity(intent);
        MachineActivity.this.finish();
    }
    //--------------------------------------Vending Machine List -----------------------------------
    private class Vending_Machine_Data extends AsyncTask<String, String, Boolean> {
        JSONParse jsonParser = new JSONParse();
        @Override
        protected Boolean doInBackground(String... para) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();

                String json = jsonParser.makeHttpRequest(MACHINE_URL, "POST", params);
                try {
                    JSONObject c = new JSONObject(json);
                    message=c.getString("message");
                    status=c.getString("status");
                    JSONArray array = c.getJSONArray("vending_machine");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        large_img = object.getString("image");
                        largeImageList.add(large_img);
                        thumb_img = object.getString("thumb_image");

                        Vending_Machine_Grid_Model vending_machine_grid_model = new Vending_Machine_Grid_Model();
                        vending_machine_grid_model.setLarge_image(large_img);
                        vending_machine_grid_model.setThumb_image(thumb_img);
                        mGridViewData.add(vending_machine_grid_model);
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
            vending_machine_gridView_imageAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
            category_title.setVisibility(View.VISIBLE);
            load_vending_machine_list.setVisibility(View.VISIBLE);
            if(largeImageList.size() >= 0){
                for(int i=0; i<largeImageList.size(); i++){
                    Log.d("ArrayValue",largeImageList.get(i));
                }
            }
        }
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
