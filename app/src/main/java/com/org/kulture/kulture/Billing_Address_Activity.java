package com.org.kulture.kulture;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.org.kulture.kulture.database.BillingDataSet;
import com.org.kulture.kulture.database.BillingDatabase;
import com.org.kulture.kulture.model.JSONParse;
import com.org.kulture.kulture.model.JsonUtil;
import com.org.kulture.kulture.model.Payment_Json;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.NameValuePair;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;


@SuppressWarnings("WrongConstant")
public class Billing_Address_Activity extends AppCompatActivity {
    ImageView back_from_billing_address, billing_cart;
    String findactivity;
    TextView billing_cart_count, cart_title;
    String qty,address,totalprice;
    int pin,ids,convertid;
    String f_name_s_billing_s, l_name_billing_s,
             email_billing_s, mob_billing_s, address_billing_s,
            pin_billing_s, city_billing_s, state_billing_s,country_billing_s,organization_billing_s;
    MaterialEditText f_name_billing, l_name_billing, email_billing, mob_billing, address_billing, country_billing;
    MaterialEditText pin_billing, city_billing, state_billing,organization_quote;
    LinearLayout billing_update;

    public static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 124;
    public static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 125;

    String CART_COUNT_USER_LOGIN_URL="http://kulture.biz/webservice/count_cart_via_user_id.php?user_id=";
    String CART_COUNT_USER_LOGOFF_URL="http://kulture.biz/webservice/count_cart_via_order_id.php?order_id=";
    String UPDATE_BILLING_ADDRESS_URL="http://kulture.biz/webservice/update_user.php?customer_id=";

    SharedPreferences preferences2,preferences3,preferences;
    String getids,getIMIE,cart_counts,getid,message,status;
    CheckBox checkbox_ship;
    String Jsonresults;
    Boolean checkcheckbox=false;
    LinearLayout add_shiping_address;
    MaterialEditText f_name_shipping, l_name_shipping, organization_quote_shipping,
            address_shipping, pin_shipping, city_shipping,state_shipping,country_shipping;
    String f_name_shipping_s, l_name_shipping_s, organization_quote_shipping_s,
            address_shipping_s, pin_shipping_s, city_shipping_s,state_shipping_s,country_shipping_s;

    String LOGIN_DATA = "http://www.kulture.biz/webservice/cart.php?user_id="; //(When User Logged in)
    Payment_Json p;
    List<Payment_Json.line_items> lnList = new ArrayList<Payment_Json.line_items>();
    DecimalFormat two = new DecimalFormat("0.00");
    Integer changeid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing_address);

        changeStatusBarColor();
        findactivity = getIntent().getStringExtra("findactivity");
        totalprice= getIntent().getStringExtra("grand_total");

        state_billing = (MaterialEditText) findViewById(R.id.state_billing);
        city_billing = (MaterialEditText) findViewById(R.id.city_billing);
        pin_billing = (MaterialEditText) findViewById(R.id.pin_billing);

        organization_quote = (MaterialEditText) findViewById(R.id.organization_quote);
        f_name_billing = (MaterialEditText) findViewById(R.id.f_name_billing);
        l_name_billing = (MaterialEditText) findViewById(R.id.l_name_billing);

        country_billing=(MaterialEditText)findViewById(R.id.country_billing);

        email_billing = (MaterialEditText) findViewById(R.id.email_billing);
        mob_billing = (MaterialEditText) findViewById(R.id.mob_billing);
        address_billing = (MaterialEditText) findViewById(R.id.address_billing);

        preferences = getApplicationContext().getSharedPreferences("register_save", Context.MODE_PRIVATE);
        getid = preferences.getString("adminid", "");

        p=new Payment_Json();
        cart_title = (TextView) findViewById(R.id.cart_title);
        cart_title.setText("Checkout");
        billing_cart = (ImageView) findViewById(R.id.billing_cart);
        billing_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Billing_Address_Activity.this, CartActivity.class);
                startActivity(intent);
                Billing_Address_Activity.this.finish();
            }
        });

        Payment_Json.PaymentDetails paymentDetails=p.new PaymentDetails();
        paymentDetails.setPayment_method("Online");
        paymentDetails.setPayment_method_title("Paypal");
        try {
            changeid = Integer.parseInt(getid);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        paymentDetails.setCustomer_id(changeid);
        paymentDetails.setSet_paid(true);

        List<Payment_Json.PaymentDetails> dnList = new ArrayList<Payment_Json.PaymentDetails>();
        dnList.add(paymentDetails);
        p.setData(dnList);

        checkbox_ship=(CheckBox)findViewById(R.id.checkbox_ship);
        add_shiping_address=(LinearLayout)findViewById(R.id.add_shiping_address);
        checkbox_ship.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(checkbox_ship.isChecked()){
                    add_shiping_address.setVisibility(View.VISIBLE);
                    checkcheckbox=true;

                }else if(!checkbox_ship.isChecked()){
                    add_shiping_address.setVisibility(View.GONE);
                    checkcheckbox=false;
                }
            }
        });
        f_name_shipping = (MaterialEditText) findViewById(R.id.f_name_shipping);
        l_name_shipping = (MaterialEditText) findViewById(R.id.l_name_shipping);
        organization_quote_shipping = (MaterialEditText) findViewById(R.id.organization_quote_shipping);

        address_shipping = (MaterialEditText) findViewById(R.id.address_shippingss);
        pin_shipping = (MaterialEditText) findViewById(R.id.pin_shipping);
        city_shipping = (MaterialEditText) findViewById(R.id.city_shipping);

        state_shipping=(MaterialEditText)findViewById(R.id.state_shipping);
        country_shipping = (MaterialEditText) findViewById(R.id.country_shipping);

        preferences2=getApplicationContext().getSharedPreferences("register_save", Context.MODE_PRIVATE);
        getids=preferences2.getString("adminid","");

        convertid=Integer.parseInt(getids);

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
        back_from_billing_address = (ImageView) findViewById(R.id.back_from_cart);
        back_from_billing_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(findactivity.equals("Delivery_Details")) {

                    Intent intent = new Intent(Billing_Address_Activity.this, Delivery_Details.class);
                    intent.putExtra("grand_total",totalprice);
                    startActivity(intent);
                    Billing_Address_Activity.this.finish();

                }else if(findactivity.equals("MyAccount")){

                    Intent intent = new Intent(Billing_Address_Activity.this, MyAccount.class);
                    startActivity(intent);
                    Billing_Address_Activity.this.finish();
                }
            }
        });
        if (!checkPermission()) {
            requestPermission();
        } else if (checkPermission()) {
            getAddress();
        }
        billing_update = (LinearLayout) findViewById(R.id.billing_update);
        billing_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
        billing_cart_count = (TextView) findViewById(R.id.cart_count);

    }
    //Validate Data locally(Checks whether the fields are empty or not)
    private void validateData() {
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(f_name_billing.getText().toString())) {
            f_name_billing.setError("Required field!");
            focusView = f_name_billing;
            cancel = true;
        } else if (TextUtils.isEmpty(l_name_billing.getText().toString())) {
            l_name_billing.setError("Required field!");
            focusView = l_name_billing;
            cancel = true;
        }else if (TextUtils.isEmpty(email_billing.getText().toString())) {
            email_billing.setError("Required field!");
            focusView = email_billing;
            cancel = true;
        }else if (TextUtils.isEmpty(organization_quote.getText().toString())) {
            organization_quote.setError("Required field!");
            focusView = organization_quote;
            cancel = true;
        } else if (TextUtils.isEmpty(mob_billing.getText().toString())) {
            mob_billing.setError("Required field!");
            focusView = mob_billing;
            cancel = true;
        } else if (TextUtils.isEmpty(address_billing.getText().toString())) {
            address_billing.setError("Required field!");
            focusView = address_billing;
            cancel = true;
        } else if (TextUtils.isEmpty(pin_billing.getText().toString())) {
            pin_billing.setError("Required field!");
            focusView = pin_billing;
            cancel = true;
        } else if (TextUtils.isEmpty(city_billing.getText().toString())) {
            city_billing.setError("Required field!");
            focusView = city_billing;
            cancel = true;
        } else if (TextUtils.isEmpty(state_billing.getText().toString())) {
            state_billing.setError("Required field!");
            focusView = state_billing;
            cancel = true;
        }else if (TextUtils.isEmpty(country_billing.getText().toString())) {
            country_billing.setError("Required field!");
            focusView = country_billing;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            getTextValues();
        }
    }
    //Get the values from EditText
    private void getTextValues() {
        f_name_s_billing_s = f_name_billing.getText().toString();
        l_name_billing_s = l_name_billing.getText().toString();
        email_billing_s = email_billing.getText().toString();
        mob_billing_s = mob_billing.getText().toString();
        address_billing_s = address_billing.getText().toString();
        pin_billing_s = pin_billing.getText().toString();
        pin = Integer.parseInt(pin_billing_s);
        city_billing_s = city_billing.getText().toString();
        state_billing_s = state_billing.getText().toString();
        country_billing_s=country_billing.getText().toString();
        organization_billing_s=organization_quote.getText().toString();

        BillingDatabase db = new  BillingDatabase(Billing_Address_Activity.this);
        db.addBillingData(f_name_s_billing_s,l_name_billing_s,organization_billing_s,country_billing_s,email_billing_s,mob_billing_s,address_billing_s,pin,city_billing_s,state_billing_s);

        // Reading all contacts
        List<BillingDataSet> billing = db.getAllBillingContacts();
        for (BillingDataSet cn : billing) {
            String f_name=cn.getFirst_name();
            String l_name=cn.getLast_name();
            String email_ids=cn.getEmail();
            String comp=cn.getCompany();
            String country=cn.getCountry();
            String Addresss=cn.getAddress();
            String Citys=cn.getCity();
            Integer Pin_codes=cn.getPincode();
            String pin=String.valueOf(Pin_codes);
            String States=cn.getState();
            String Mobile_Numbers=cn.getMob_number();
            try{
                f_name_billing.setText(f_name);
                l_name_billing.setText(l_name);
                email_billing.setText(email_ids);

                country_billing.setText(country);
                address_billing.setText(Addresss);
                organization_quote.setText(comp);

                city_billing.setText(Citys);
                pin_billing.setText(pin);
                state_billing.setText(States);

                mob_billing.setText(Mobile_Numbers);
            }catch (NullPointerException e){
                e.printStackTrace();
            }
    }
        if(checkbox_ship.isChecked()){
            Payment_Json.BillingAddress billingAddress=p.new BillingAddress();
            billingAddress.setFirst_name(f_name_s_billing_s);
            billingAddress.setLast_name(l_name_billing_s);
            billingAddress.setEmail(email_billing_s);
            billingAddress.setPhone(mob_billing_s);
            billingAddress.setAddress1(address_billing_s);
            billingAddress.setPostcode(pin_billing_s);
            billingAddress.setCity(city_billing_s);
            billingAddress.setState(state_billing_s);
            billingAddress.setCountry(country_billing_s);

            List<Payment_Json.BillingAddress> bnList = new ArrayList<Payment_Json.BillingAddress>();
            bnList.add(billingAddress);
            p.setBillingAddress(bnList);

            GetShippingDetails();

        }else if(!checkbox_ship.isChecked()) {
            f_name_shipping_s = f_name_s_billing_s;
            l_name_shipping_s = l_name_billing_s;
            organization_quote_shipping_s = organization_billing_s;
            address_shipping_s = address_billing_s;
            pin_shipping_s = pin_billing_s;
            city_shipping_s = city_billing_s;
            state_shipping_s = state_billing_s;
            country_shipping_s = country_billing_s;

            Payment_Json.BillingAddress billingAddress=p.new BillingAddress();
            billingAddress.setFirst_name(f_name_s_billing_s);
            billingAddress.setLast_name(l_name_billing_s);
            billingAddress.setEmail(email_billing_s);
            billingAddress.setPhone(mob_billing_s);
            billingAddress.setAddress1(address_billing_s);
            billingAddress.setPostcode(pin_billing_s);
            billingAddress.setCity(city_billing_s);
            billingAddress.setState(state_billing_s);
            billingAddress.setCountry(country_billing_s);

            List<Payment_Json.BillingAddress> bnList = new ArrayList<Payment_Json.BillingAddress>();
            bnList.add(billingAddress);
            p.setBillingAddress(bnList);

            Payment_Json.ShippingAddress shippingAddress=p.new ShippingAddress();
            shippingAddress.setShipping_first_name(f_name_shipping_s);
            shippingAddress.setShipping_last_name(l_name_shipping_s);
            shippingAddress.setShipping_address1(address_shipping_s);
            shippingAddress.setShipping_postcode(pin_shipping_s);
            shippingAddress.setShipping_city(city_shipping_s);
            shippingAddress.setShipping_state(state_shipping_s);
            shippingAddress.setShipping_country(country_shipping_s);

            List<Payment_Json.ShippingAddress> cnList = new ArrayList<Payment_Json.ShippingAddress>();
            cnList.add(shippingAddress);
            p.setShippingAddress(cnList);

            try {
                new Update_User_Billing_Address().execute();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }
    public void GetShippingDetails(){
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(f_name_shipping.getText().toString())) {
            f_name_shipping.setError("Required field!");
            focusView = f_name_shipping;
            cancel = true;
        } else if (TextUtils.isEmpty(l_name_shipping.getText().toString())) {
            l_name_shipping.setError("Required field!");
            focusView = l_name_shipping;
            cancel = true;
        }else if (TextUtils.isEmpty(organization_quote_shipping.getText().toString())) {
            organization_quote_shipping.setError("Required field!");
            focusView = organization_quote_shipping;
            cancel = true;
        } else if (TextUtils.isEmpty(address_shipping.getText().toString())) {
            address_shipping.setError("Required field!");
            focusView = address_shipping;
            cancel = true;
        } else if (TextUtils.isEmpty(pin_shipping.getText().toString())) {
            pin_shipping.setError("Required field!");
            focusView = pin_shipping;
            cancel = true;
        } else if (TextUtils.isEmpty(city_shipping.getText().toString())) {
            city_shipping.setError("Required field!");
            focusView = city_shipping;
            cancel = true;
        } else if (TextUtils.isEmpty(state_shipping.getText().toString())) {
            state_shipping.setError("Required field!");
            focusView = state_shipping;
            cancel = true;
        } else if (TextUtils.isEmpty(country_shipping.getText().toString())) {
            country_shipping.setError("Required field!");
            focusView = country_shipping;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            getTextShipValues();
        }
    }
    //Get the values from EditText
    private void getTextShipValues() {
        Toast.makeText(getApplicationContext(), "Loading...", Toast.LENGTH_SHORT).show();
        f_name_shipping_s = f_name_shipping.getText().toString();
        l_name_shipping_s = l_name_shipping.getText().toString();
        organization_quote_shipping_s = organization_quote_shipping.getText().toString();
        address_shipping_s = address_shipping.getText().toString();
        pin_shipping_s = pin_shipping.getText().toString();
        city_shipping_s = city_shipping.getText().toString();
        state_shipping_s = state_shipping.getText().toString();
        country_shipping_s = country_shipping.getText().toString();

        Payment_Json.ShippingAddress shippingAddress=p.new ShippingAddress();
        shippingAddress.setShipping_first_name(f_name_shipping_s);
        shippingAddress.setShipping_last_name(l_name_shipping_s);
        shippingAddress.setShipping_address1(address_shipping_s);
        shippingAddress.setShipping_postcode(pin_shipping_s);
        shippingAddress.setShipping_city(city_shipping_s);
        shippingAddress.setShipping_state(state_shipping_s);
        shippingAddress.setShipping_country(country_shipping_s);

        List<Payment_Json.ShippingAddress> cnList = new ArrayList<Payment_Json.ShippingAddress>();
        cnList.add(shippingAddress);
        p.setShippingAddress(cnList);

        try {
            new Update_User_Billing_Address().execute();
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }
    //----------------------------------------------Update Billing Address------------------------------------------------
    private class Update_User_Billing_Address extends AsyncTask<String, String, Boolean> {
        JSONParse jsonParser = new JSONParse();
        @Override
        protected Boolean doInBackground(String... para) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();

                String updateAddress=UPDATE_BILLING_ADDRESS_URL+URLEncoder.encode(getids)+"&first_name="+URLEncoder.encode(f_name_s_billing_s)+"&last_name="+URLEncoder.encode(l_name_billing_s)+"&bl_first_name="+URLEncoder.encode(f_name_s_billing_s)+"&bl_last_name="+URLEncoder.encode(l_name_billing_s)+"&bl_company="+URLEncoder.encode(organization_billing_s)+"&bl_address_1="+URLEncoder.encode(address_billing_s)+"&bl_address_2="+""+"&bl_city="+URLEncoder.encode(city_billing_s)+"&bl_state="+URLEncoder.encode(state_billing_s)+"&bl_postcode="+URLEncoder.encode(pin_billing_s)+"&bl_country="+URLEncoder.encode(country_billing_s)+"&bl_email="+URLEncoder.encode(email_billing_s)+"&bl_phone="+URLEncoder.encode(mob_billing_s)+"&sh_first_name="+URLEncoder.encode(f_name_shipping_s)+"&sh_last_name="+URLEncoder.encode(l_name_shipping_s)+"&sh_company="+URLEncoder.encode(organization_quote_shipping_s)+"&sh_address_1="+URLEncoder.encode(address_shipping_s)+"&sh_address_2="+""+"&sh_city="+URLEncoder.encode(city_shipping_s)+"&sh_state="+URLEncoder.encode(state_shipping_s)+"&sh_postcode="+URLEncoder.encode(pin_shipping_s)+"&sh_country="+URLEncoder.encode(country_shipping_s);
                String json = jsonParser.makeHttpRequest(updateAddress, "POST", params);
                try {
                    JSONObject c = new JSONObject(json);
                    JSONObject obj=c.getJSONObject("customer");
                    ids=obj.getInt("id");
                }catch (JSONException e){
                    e.printStackTrace();
                }
                }catch (IllegalArgumentException e){
                    e.printStackTrace();
                }
            return null;
        }
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(ids > 0) {
                Toast.makeText(getApplicationContext(), "Address Updated Sucessfully...", Toast.LENGTH_SHORT).show();
                try {
                    new Cart_Details().execute();
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }else {
                Toast.makeText(getApplicationContext(), "Try Again..", Toast.LENGTH_SHORT).show();
            }
        }
    }
    //----------------------------------------------Login View Cart------------------------------------------------
    private class Cart_Details extends AsyncTask<String, String, Boolean> {
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
                        String order_id = obj.getString("order_id");
                        String user_id = obj.getString("user_id");
                        String size = obj.getString("size");
                        String p_id=obj.getString("prduct_id");
                        String qantity=obj.getString("quantity");
                        int pro_id=Integer.parseInt(p_id);
                        int qty=Integer.parseInt(qantity);

                        Payment_Json.line_items line_items=p.new line_items();
                        line_items.setProduct_id(pro_id);
                        line_items.setQuantity(qty);
                        line_items.setVariation_id(size);
                        lnList.add(line_items);
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
            p.setItemsList(lnList);
            Payment_Json.shipping_lines shipping_lines=p.new shipping_lines();
            shipping_lines.setMethod_id("flat_rate");
            shipping_lines.setMethod_title("Flat_rate");
            Double convert_total=Double.parseDouble(totalprice);
            shipping_lines.setTotal(convert_total);

            List<Payment_Json.shipping_lines> slList= new ArrayList<Payment_Json.shipping_lines>();
            slList.add(shipping_lines);
            p.setShipping_lines(slList);

            try{
                Submit_Result task = new Submit_Result();
                task.execute(new Payment_Json[] {p});
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }
    }
    //----------------------------------------------Submit Result------------------------------------------------
    private class Submit_Result extends AsyncTask<Payment_Json, Void, String> {
        JSONParse jsonParser = new JSONParse();
        @Override
        protected String doInBackground(Payment_Json... params) {
            List<NameValuePair> paramss = new ArrayList<NameValuePair>();
            Jsonresults = JsonUtil.toJSon(params[0]);
            return Jsonresults;
        }
        @Override
        protected void onPostExecute(String Jsonresults) {
            Toast.makeText(getApplicationContext(), "Welcome  "+f_name_s_billing_s, Toast.LENGTH_SHORT).show();
            super.onPostExecute(Jsonresults);
           try {
               Log.d("Result", Jsonresults);
           }catch (NullPointerException e){
               e.printStackTrace();
           }
            if (findactivity.equals("Delivery_Details")) {
                Intent intent = new Intent(Billing_Address_Activity.this, Delivery_Details.class);
                intent.putExtra("grand_total", totalprice);
                startActivity(intent);
                Billing_Address_Activity.this.finish();
            } else if (findactivity.equals("MyAccount")) {
                Intent intent = new Intent(Billing_Address_Activity.this, MyAccount.class);
                startActivity(intent);
                Billing_Address_Activity.this.finish();
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
                billing_cart_count.setText(cart_counts);
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
                billing_cart_count.setText(cart_counts);
            }catch (NullPointerException e){
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

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:
                if (grantResults.length > 0) {
                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted) {
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                            showMessageOKCancel("You need to allow access to both the permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(new String[]{ACCESS_FINE_LOCATION},
                                                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }
            case MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION:
                if (grantResults.length > 0) {
                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted) {
                        getAddress();
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(ACCESS_COARSE_LOCATION)) {
                            showMessageOKCancel("You need to allow access to both the permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(new String[]{ACCESS_COARSE_LOCATION},
                                                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }

        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(Billing_Address_Activity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    public void getAddress() {
        // TODO Auto-generated method stub

        turnGPSOn(); // method to turn on the GPS if its in off state.
        getMyCurrentLocation();
    }

    /** Method to turn on GPS **/

    public void turnGPSOn() {
        try {
            String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

            if (!provider.contains("gps")) { //if gps is disabled
                final Intent poke = new Intent();
                poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
                poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
                poke.setData(Uri.parse("3"));
                sendBroadcast(poke);
            }
        } catch (Exception e) {

        }
    }
    // Method to turn off the GPS
    public void turnGPSOff() {
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (provider.contains("gps")) { //if gps is enabled

            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }
    }
    // turning off the GPS if its in on state. to avoid the battery drain.
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        turnGPSOff();
    }


    /** Check the type of GPS Provider available at that instance and  collect the location informations
     @Output Latitude and Longitude
      * */
    void getMyCurrentLocation() {

        LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener locListener = new MyLocationListener();
        try {
            gps_enabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        try {
            network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }
        //don't start listeners if no provider is enabled

        //if(!gps_enabled && !network_enabled)

        //return false;

        if (gps_enabled) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);

        }


        if (gps_enabled) {
            location = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        }

        if (network_enabled && location == null) {

            locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locListener);

        }
        if (network_enabled && location == null) {
            location = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        }

        if (location != null) {

            MyLat = location.getLatitude();
            MyLong = location.getLongitude();
        } else {
            Location loc = getLastKnownLocation(this);
            if (loc != null) {
                MyLat = loc.getLatitude();
                MyLong = loc.getLongitude();
            }
        }

        locManager.removeUpdates(locListener); // removes the periodic updates from location listener to avoid battery drainage. If you want to get location at the periodic intervals call this method using pending intent.

        try {
            // Getting address from found locations.
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());
            addresses = geocoder.getFromLocation(MyLat, MyLong, 1);

            StateName = addresses.get(0).getAdminArea();
            CityName = addresses.get(0).getLocality();
            PostalCode = addresses.get(0).getPostalCode();
            CountryName=addresses.get(0).getCountryName();
            SubAdminName=addresses.get(0).getAddressLine(0);
            address=SubAdminName+"," +CityName+"-" +PostalCode+"," +StateName+"," +CountryName;

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (CityName.equals("")) {
                city_billing.setText("");
            } else if (!CityName.equals("")) {
                city_billing.setText(CityName);
            }

            if (CityName.equals("")) {
                city_billing.setText("");
            } else if (!CityName.equals("")) {
                city_billing.setText(CityName);
            }
            if (PostalCode.equals("")) {
                pin_billing.setText("");
            } else if (!PostalCode.equals("")) {
                pin_billing.setText(PostalCode);

            }
            if(CountryName.equals("")){
                address_billing.setText("");
                country_billing.setText("");
            }else if(!CountryName.equals("")){
                address_billing.setText(address);
                country_billing.setText(CountryName);
            }

            if (StateName.equals("")) {
                state_billing.setText("");
            } else if (!StateName.equals("")) {
                state_billing.setText(StateName);
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    // Location listener class. to get location.
    public class MyLocationListener implements LocationListener {
        public void onLocationChanged(Location location) {
            if (location != null) {
            }
        }

        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }

        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub
        }
    }
    private boolean gps_enabled = false;
    private boolean network_enabled = false;
    Location location;
    Double MyLat, MyLong;
    String CityName = "";
    String StateName = "";
    String PostalCode= "";
    String CountryName = "";
    String SubAdminName = "";

// below method to get the last remembered location. because we don't get locations all the times .At some instances we are unable to get the location from GPS. so at that moment it will show us the last stored location.

    public static Location getLastKnownLocation(Context context)
    {
        Location location = null;
        LocationManager locationmanager = (LocationManager) context.getSystemService("location");
        List list = locationmanager.getAllProviders();
        boolean i = false;
        Iterator iterator = list.iterator();
        do {
            //System.out.println("---------------------------------------------------------------------");
            if (!iterator.hasNext())
                break;
            String s = (String) iterator.next();
            //if(i != 0 && !locationmanager.isProviderEnabled(s))
            if (i != false && !locationmanager.isProviderEnabled(s))
                continue;
            // System.out.println("provider ===> "+s);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

            }
            Location location1 = locationmanager.getLastKnownLocation(s);
            if(location1 == null)
                continue;
            if(location != null)
            {
                //System.out.println("location ===> "+location);
                //System.out.println("location1 ===> "+location);
                float f = location.getAccuracy();
                float f1 = location1.getAccuracy();
                if(f >= f1)
                {
                    long l = location1.getTime();
                    long l1 = location.getTime();
                    if(l - l1 <= 600000L)
                        continue;
                }
            }
            location = location1;
            // System.out.println("location  out ===> "+location);
            //System.out.println("location1 out===> "+location);
            i = locationmanager.isProviderEnabled(s);
            // System.out.println("---------------------------------------------------------------------");
        } while(true);
        return location;
    }
    @Override
    public void onBackPressed() {
        if(findactivity.equals("Delivery_Details")) {
            Intent intent = new Intent(Billing_Address_Activity.this, Delivery_Details.class);
            intent.putExtra("grand_total",totalprice);
            startActivity(intent);
            Billing_Address_Activity.this.finish();
        }else if(findactivity.equals("MyAccount")){
            Intent intent = new Intent(Billing_Address_Activity.this, MyAccount.class);
            startActivity(intent);
            Billing_Address_Activity.this.finish();
        }
    }

}


