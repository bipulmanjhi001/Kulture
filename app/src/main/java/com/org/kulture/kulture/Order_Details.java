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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.org.kulture.kulture.model.JSONParse;
import com.org.kulture.kulture.model.OrderItems;
import com.org.kulture.kulture.model.OrderList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;

public class Order_Details extends AppCompatActivity {
    ImageView back_from_cart;
    ListView list;
    TextView main_list_title,cart_count;
    String qty,cust_id,stock_quantity;
    ImageView order_image_cart;
    String category="Order Details";
    String ORDER_URL="http://kulture.biz/webservice/get_all_order.php";
    String PRODUCT_DETAILS_URL= "http://kulture.biz/webservice/product_details_by_id.php?product_id=";

    LinearLayout check_login;
    OrderList adapter;
    Integer convertID;
    SharedPreferences preferences;
    ArrayList<OrderItems> orderItemses;
    ProgressBar progressBar;

    String image_url,pricess,titles,weight,size_options;
    Integer getproduct_id;

    String CART_COUNT_USER_LOGIN_URL="http://kulture.biz/webservice/count_cart_via_user_id.php?user_id=";
    String CART_COUNT_USER_LOGOFF_URL="http://kulture.biz/webservice/count_cart_via_order_id.php?order_id=";

    SharedPreferences preferences2,preferences3;
    String getids,getIMIE,cart_counts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        changeStatusBarColor();
        back_from_cart = (ImageView) findViewById(R.id.back_from_cart);
        back_from_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Order_Details.this, Homescreen.class);
                startActivity(intent);
                Order_Details.this.finish();
            }
        });
        order_image_cart = (ImageView) findViewById(R.id.order_image_cart);
        order_image_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Order_Details.this, CartActivity.class);
                startActivity(intent);
                Order_Details.this.finish();
            }
        });
        check_login=(LinearLayout)findViewById(R.id.check_login);
        main_list_title=(TextView)findViewById(R.id.cart_title);
        main_list_title.setText(category);
        orderItemses=new ArrayList<OrderItems>();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        //making the progressbar visible
        progressBar.setVisibility(View.VISIBLE);
        adapter = new OrderList(Order_Details.this, R.layout.order_list_row, orderItemses);
        list = (ListView) findViewById(R.id.orderlist);
        list.setAdapter(adapter);
        list.setEmptyView(findViewById(R.id.emptyElement));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),"Loading..",Toast.LENGTH_LONG).show();
                OrderItems item = (OrderItems) parent.getItemAtPosition(position);
                getproduct_id=item.getProduct_Id();
                try {
                    new Order_Product_Details_Data().execute();
                }catch (IndexOutOfBoundsException e){
                    e.printStackTrace();
                }
            }
        });
        cart_count=(TextView)findViewById(R.id.order_cart_count);

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
        //---------------------------------------Get Register user ID---------------------------------------------------
        preferences=getApplicationContext().getSharedPreferences("register_save", Context.MODE_PRIVATE);
        cust_id=preferences.getString("adminid","");
        convertID=Integer.parseInt(cust_id);
        try {
            new Orders_Detail().execute();
        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();
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
        Intent intent = new Intent(Order_Details.this, Homescreen.class);
        startActivity(intent);
        Order_Details.this.finish();
    }
    //----------------------------------------------Order Details------------------------------------------------
    private class Orders_Detail extends AsyncTask<String, String, Boolean> {
        JSONParse jsonParser = new JSONParse();
        @Override
        protected Boolean doInBackground(String... para) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                String json = jsonParser.makeHttpRequest(ORDER_URL, "POST", params);
                try {
                    JSONObject c = new JSONObject(json);
                    JSONArray arrayobj=c.getJSONArray("orders");
                    for(int i=0; i < arrayobj.length(); i++ ) {
                        JSONObject obj = arrayobj.getJSONObject(i);

                        if(obj.getString("customer_id").equals(""+convertID)) {
                            JSONArray getcust_id = obj.getJSONArray("line_items");

                             for(int k=0; k < getcust_id.length(); k++) {
                                 JSONObject linesobj=getcust_id.getJSONObject(k);
                                 Integer id = linesobj.getInt("id");
                                 String name = linesobj.getString("name");
                                 String total = linesobj.getString("total");
                                 String quantity = linesobj.getString("quantity");
                                 Integer product_id = linesobj.getInt("product_id");

                                 OrderItems orderItems=new OrderItems();
                                 orderItems.setId(id);
                                 orderItems.setName(name);
                                 orderItems.setTotal(total);
                                 orderItems.setQty(quantity);
                                 orderItems.setProduct_Id(product_id);
                                 orderItemses.add(orderItems);
                             }
                        }
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Boolean aBoolean)
        {
            adapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);

        }
    }
    //--------------------------------------Order Product Details List ------------------------------
    private class Order_Product_Details_Data extends AsyncTask<String, String, Boolean> {
        JSONParse jsonParser = new JSONParse();
        @Override
        protected Boolean doInBackground(String... para) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();

                String json = jsonParser.makeHttpRequest(PRODUCT_DETAILS_URL+getproduct_id, "POST", params);
                try {
                    JSONObject c = new JSONObject(json);
                    JSONObject obj = c.getJSONObject("product");
                    pricess = obj.getString("price");
                    titles = Html.fromHtml(obj.getString("title")).toString();
                    weight=obj.getString("weight");
                    stock_quantity=obj.getString("stock_quantity");

                    JSONArray corporate = obj.getJSONArray("images");
                    for (int j = 0; j < corporate.length(); j++) {
                        JSONObject corporateobject = corporate.getJSONObject(j);
                        image_url = corporateobject.getString("src");
                    }
                    JSONArray attributes=obj.getJSONArray("attributes");
                    if(attributes != null && attributes.length() > 0) {
                        for (int l = 0; l < attributes.length(); l++) {
                            JSONObject attributeobj = attributes.getJSONObject(0);
                            size_options=attributeobj.getString("option");
                        }
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
            Intent intent=new Intent(Order_Details.this,Order_Details_View.class);
            intent.putExtra("price",pricess);
            intent.putExtra("title",titles);
            intent.putExtra("weight",weight);
            intent.putExtra("size_options",size_options);
            intent.putExtra("image_url",image_url);
            intent.putExtra("stock_quantity",stock_quantity);
            startActivity(intent);
            Order_Details.this.finish();
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
                cart_count.setText(cart_counts);
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
                cart_count.setText(cart_counts);
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }
    }
}
