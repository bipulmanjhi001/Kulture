package com.org.kulture.kulture;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.org.kulture.kulture.model.CartModel;
import com.org.kulture.kulture.model.CartRecyclerAdapter;
import com.org.kulture.kulture.model.EmptyCart;
import com.org.kulture.kulture.model.FooterBarLayout;
import com.org.kulture.kulture.model.JSONParse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;


public class CartActivity extends AppCompatActivity {
    ImageView back_from_cart,delete_all;
    TextView cart;
    LinearLayout cart_payment, add_cart_layout, add_to_cart, progresslayout;
    String qty, totalprice, grand_totals;
    int count;
    SharedPreferences preferences, preferences2;
    TextView grand_total;

    public String prices, img, names, getqty, getsize,getpid;
    String getid, getIMIE, message, message2, status, status2;
    DecimalFormat two = new DecimalFormat("0.00");

    private Handler handler = new Handler();
    private static int SPLASH_TIME_OUT = 9000;

    String PRODUCT_DETAILS_URL = "http://kulture.biz/webservice/product_details_by_id.php?product_id=";
    String LOGIN_DATA = "http://www.kulture.biz/webservice/cart.php?user_id="; //(When User Logged in)
    String LOG_OFF_DATA = "http://www.kulture.biz/webservice/cart.php?order_id="; //(When User Not Login)

    ArrayList<String> ids = new ArrayList<String>();
    ArrayList<String> order_ids = new ArrayList<String>();
    ArrayList<String> prduct_ids = new ArrayList<String>();
    ArrayList<String> sizes = new ArrayList<String>();
    ArrayList<String> quantitys = new ArrayList<String>();
    ArrayList<String> price=new ArrayList<String>();
    ArrayList<String> name=new ArrayList<String>();
    ArrayList<String> images=new ArrayList<String>();

    RecyclerView recyclerView;
    ArrayList<CartModel> cartModels;
    CartRecyclerAdapter adapter;
    FooterBarLayout cart_payment_proceed;

    ArrayList<Double> calculateprices = new ArrayList<Double>();
    double doubarray,doubarray2;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        back_from_cart = (ImageView) findViewById(R.id.back_from_cart);
        cart = (TextView) findViewById(R.id.cart_title);

        changeStatusBarColor();
        preferences = getApplicationContext().getSharedPreferences("register_save", Context.MODE_PRIVATE);
        getid = preferences.getString("adminid", "");

        preferences2 = getApplicationContext().getSharedPreferences("mobimei_save", Context.MODE_PRIVATE);
        getIMIE = preferences2.getString("imei", "");

        cart_payment_proceed = (FooterBarLayout) findViewById(R.id.cart_payment_proceed);
        add_to_cart = (LinearLayout) findViewById(R.id.add_to_cart);
        progresslayout = (LinearLayout) findViewById(R.id.progresslayout);
        progressBar = (ProgressBar) findViewById(R.id.probar);
        delete_all=(ImageView)findViewById(R.id.delete_all);

        if (!getid.isEmpty() && !getIMIE.isEmpty()) {
            try {
                recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                cartModels = new ArrayList<CartModel>();
                adapter = new CartRecyclerAdapter(CartActivity.this,cartModels,ids);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setAdapter(adapter);
                new Login_View_Cart_Details().execute();

            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        } else if (getid.isEmpty() && !getIMIE.isEmpty()) {
            try {
                recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                cartModels = new ArrayList<CartModel>();
                adapter = new CartRecyclerAdapter(CartActivity.this,cartModels,ids);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setAdapter(adapter);
                new Logof_View_Cart_Details().execute();

            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        delete_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!getid.isEmpty()) {
                    try {
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        EmptyCart frag = new EmptyCart();
                        frag.show(ft, "txn_tag");
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        grand_total = (TextView) findViewById(R.id.grand_total);
        back_from_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, Homescreen.class);
                startActivity(intent);
                CartActivity.this.finish();
            }
        });

        cart_payment = (LinearLayout) findViewById(R.id.cart_payment);
        add_cart_layout = (LinearLayout) findViewById(R.id.add_cart_layout);
        cart_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!getid.isEmpty()) {
                    Intent intent = new Intent(CartActivity.this, Delivery_Details.class);
                    intent.putExtra("grand_total", grand_total.getText().toString());
                    startActivity(intent);
                    CartActivity.this.finish();
                } else if (getid.isEmpty()) {
                    Intent intent = new Intent(CartActivity.this, Loginscreen.class);
                    startActivity(intent);
                    CartActivity.this.finish();
                }
            }
        });
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
        Intent intent = new Intent(CartActivity.this, Homescreen.class);
        startActivity(intent);
        CartActivity.this.finish();
    }

    //----------------------------------------------Login View Cart------------------------------------------------
    private class Login_View_Cart_Details extends AsyncTask<String, String, Boolean> {
        JSONParse jsonParser = new JSONParse();

        @Override
        protected Boolean doInBackground(String... para) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                String json = jsonParser.makeHttpRequest(LOGIN_DATA + getid, "POST", params);
                try {
                    JSONObject c = new JSONObject(json);
                    message = c.getString("message");
                    status = c.getString("status");
                    JSONArray array = c.getJSONArray("cart");

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject obj = array.getJSONObject(i);
                        String id = obj.getString("id");
                        ids.add(id);
                        String order_id = obj.getString("order_id");
                        order_ids.add(order_id);
                        String prduct_id = obj.getString("prduct_id");
                        prduct_ids.add(prduct_id);
                        String user_id = obj.getString("user_id");
                        String size = obj.getString("size");
                        sizes.add(size);
                        String quantity = obj.getString("quantity");
                        quantitys.add(quantity);
                    }

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
            if(status.equals("1")) {
                try {

                    new GetCartDetails().execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if(status.equals("0"))
            {
                setContentView(R.layout.no_added_product);
                back_from_cart = (ImageView) findViewById(R.id.back_from_cart);
                back_from_cart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(CartActivity.this, Homescreen.class);
                        startActivity(intent);
                        CartActivity.this.finish();
                    }
                });
            }
        }
    }

    //----------------------------------------------Log Of View Cart------------------------------------------------
    private class Logof_View_Cart_Details extends AsyncTask<String, String, Boolean> {
        JSONParse jsonParser = new JSONParse();

        @Override
        protected Boolean doInBackground(String... para) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                String json = jsonParser.makeHttpRequest(LOG_OFF_DATA + getIMIE, "POST", params);
                try {
                    JSONObject c = new JSONObject(json);
                    message2 = c.getString("message");
                    status2 = c.getString("status");
                    JSONArray array = c.getJSONArray("cart");

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject obj = array.getJSONObject(i);
                        String id = obj.getString("id");
                        ids.add(id);
                        String order_id = obj.getString("order_id");
                        order_ids.add(order_id);
                        String prduct_id = obj.getString("prduct_id");
                        prduct_ids.add(prduct_id);
                        String user_id = obj.getString("user_id");
                        String size = obj.getString("size");
                        sizes.add(size);
                        String quantity = obj.getString("quantity");
                        quantitys.add(quantity);

                    }
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
            if (status2.equals("1")) {
                try {
                  new GetCartDetails().execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (status2.equals("0")) {
                setContentView(R.layout.no_added_product);
                back_from_cart = (ImageView) findViewById(R.id.back_from_cart);
                back_from_cart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(CartActivity.this, Homescreen.class);
                        startActivity(intent);
                        CartActivity.this.finish();
                    }
                });
            }
        }
    }
    private class GetCartDetails extends AsyncTask<String, String, Boolean> {
        JSONParse jsonParser = new JSONParse();
        @Override
        protected Boolean doInBackground(String... para) {
            try {
                for(int i=0; i< prduct_ids.size();i++) {
                    String url = PRODUCT_DETAILS_URL + prduct_ids.get(i);

                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    String json = jsonParser.makeHttpRequest(url, "POST", params);
                    try {
                        JSONObject c = new JSONObject(json);
                        JSONObject obj = c.getJSONObject("product");
                        prices = obj.getString("price");
                        price.add(prices);
                        names = Html.fromHtml(obj.getString("title")).toString();
                        name.add(names);
                        img = obj.getString("featured_src");
                        images.add(img);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;

        }
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            try{
                AddData();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void AddData(){
        for(int k=0; k< prduct_ids.size(); k++) {
            count++;
            getqty = quantitys.get(k);
            getsize = sizes.get(k);
            getpid = ids.get(k);
            calculateprices.add(Double.parseDouble(price.get(k)));
            doubarray = Double.parseDouble(quantitys.get(k)) * Double.parseDouble(price.get(k));
            totalprice = String.valueOf(two.format(doubarray));
            doubarray2 = doubarray2 + doubarray;
            grand_totals = String.valueOf(two.format(doubarray2));

            CartModel cartModele = new CartModel();
            cartModele.setCart_name(name.get(k));
            cartModele.setCart_total_price(totalprice);
            cartModele.setCart_image(images.get(k));

            cartModele.setCart_qty(quantitys.get(k));
            cartModele.setCart_size(sizes.get(k));
            cartModele.setProduct_id(ids.get(k));
            cartModels.add(cartModele);

            grand_total.setText(grand_totals);
            adapter.notifyDataSetChanged();
        }
        try {
            cart.setText("My Cart(" + String.valueOf(count) + ")");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progresslayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                if (count > 0) {
                    cart_payment_proceed.setVisibility(View.VISIBLE);
                } else {
                    cart_payment_proceed.setVisibility(View.GONE);
                }
            }
        }, SPLASH_TIME_OUT);
    }
}

