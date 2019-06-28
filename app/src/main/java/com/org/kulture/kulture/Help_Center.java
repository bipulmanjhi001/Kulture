package com.org.kulture.kulture;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.org.kulture.kulture.model.JSONParse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;

import static android.Manifest.permission.CALL_PHONE;


public class Help_Center extends AppCompatActivity {

    LinearLayout expend_related_queries,expend_payment_queries,expend_shipping_queries,expend_error_queries,log_out_layout;
    ImageView account_related_queries,account_related_queries_collepse;
    ImageView payment_related_queries,payment_related_queries_collepse;
    ImageView shipping_related_queries,shipping_related_queries_collepse;
    ImageView shipping_error_queries,shipping_error_queries_collepse,back_from_productdetails;
    TextView product_cart_count;
    String qty;
    TextView help_a1,help_a2,help_a3,help_a4,help_a5,help_a6,help_a7,help_a8;
    private static final int PERMISSION_REQUEST_CODE = 200;
    CoordinatorLayout app_coordinatorlayout;

    String CART_COUNT_USER_LOGIN_URL="http://kulture.biz/webservice/count_cart_via_user_id.php?user_id=";
    String CART_COUNT_USER_LOGOFF_URL="http://kulture.biz/webservice/count_cart_via_order_id.php?order_id=";

    SharedPreferences preferences2,preferences3;
    String getids,getIMIE,cart_counts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_center);
        changeStatusBarColor();

        back_from_productdetails=(ImageView)findViewById(R.id.back_from_productdetails);
        back_from_productdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Help_Center.this,Homescreen.class);
                startActivity(intent);
                Help_Center.this.finish();
            }
        });
        app_coordinatorlayout=(CoordinatorLayout)findViewById(R.id.help_center_main);
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
        expend_related_queries=(LinearLayout)findViewById(R.id.expend_related_queries);
        account_related_queries_collepse=(ImageView)findViewById(R.id.account_related_queries_collepse);
        account_related_queries_collepse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account_related_queries_collepse.setVisibility(View.GONE);
                account_related_queries.setVisibility(View.VISIBLE);
                expend_related_queries.setVisibility(View.GONE);
            }
        });
        account_related_queries=(ImageView)findViewById(R.id.account_related_queries);
        account_related_queries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account_related_queries.setVisibility(View.GONE);
                account_related_queries_collepse.setVisibility(View.VISIBLE);
                expend_related_queries.setVisibility(View.VISIBLE);
            }
        });
        //second
        expend_payment_queries=(LinearLayout)findViewById(R.id.expend_payment_queries);
        payment_related_queries_collepse=(ImageView)findViewById(R.id.payment_related_queries_collepse);
        payment_related_queries_collepse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payment_related_queries_collepse.setVisibility(View.GONE);
                payment_related_queries.setVisibility(View.VISIBLE);
                expend_payment_queries.setVisibility(View.GONE);
            }
        });
        payment_related_queries=(ImageView)findViewById(R.id.payment_related_queries);
        payment_related_queries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payment_related_queries.setVisibility(View.GONE);
                payment_related_queries_collepse.setVisibility(View.VISIBLE);
                expend_payment_queries.setVisibility(View.VISIBLE);
            }
        });
        //third
        expend_shipping_queries=(LinearLayout)findViewById(R.id.expend_shipping_queries);
        shipping_related_queries_collepse=(ImageView)findViewById(R.id.shipping_related_queries_collepse);
        shipping_related_queries_collepse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shipping_related_queries_collepse.setVisibility(View.GONE);
                shipping_related_queries.setVisibility(View.VISIBLE);
                expend_shipping_queries.setVisibility(View.GONE);
            }
        });
        shipping_related_queries=(ImageView)findViewById(R.id.shipping_related_queries);
        shipping_related_queries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shipping_related_queries.setVisibility(View.GONE);
                shipping_related_queries_collepse.setVisibility(View.VISIBLE);
                expend_shipping_queries.setVisibility(View.VISIBLE);
            }
        });
        //fourth
        expend_error_queries=(LinearLayout)findViewById(R.id.expend_error_queries);
        shipping_error_queries_collepse=(ImageView)findViewById(R.id.shipping_error_queries_collepse);
        shipping_error_queries_collepse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shipping_error_queries_collepse.setVisibility(View.GONE);
                shipping_error_queries.setVisibility(View.VISIBLE);
                expend_error_queries.setVisibility(View.GONE);
            }
        });
        shipping_error_queries=(ImageView)findViewById(R.id.shipping_error_queries);
        shipping_error_queries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shipping_error_queries.setVisibility(View.GONE);
                shipping_error_queries_collepse.setVisibility(View.VISIBLE);
                expend_error_queries.setVisibility(View.VISIBLE);
            }
        });

        help_a1=(TextView)findViewById(R.id.help_a1);
        help_a1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkPermission()) {
                    requestPermission();
                }
                String mobileNo = "0428 000 078";
                String uri = "tel:" + mobileNo.trim();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(uri));
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                        int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                }
                try {
                    startActivity(intent);
                }catch (SecurityException e){
                    e.printStackTrace();
                }

            }
        });
        help_a2=(TextView)findViewById(R.id.help_a2);
        help_a2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkPermission()) {
                    requestPermission();
                }
                String mobileNo = "0428 000 078";
                String uri = "tel:" + mobileNo.trim();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(uri));
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                        int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                }
                try {
                    startActivity(intent);
                }catch (SecurityException e){
                    e.printStackTrace();
                }
            }
        });
        help_a3=(TextView)findViewById(R.id.help_a3);
        help_a3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkPermission()) {
                    requestPermission();
                }
                String mobileNo = "0428 000 078";
                String uri = "tel:" + mobileNo.trim();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(uri));
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                        int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                }
                try {
                    startActivity(intent);
                }catch (SecurityException e){
                    e.printStackTrace();
                }
            }
        });
        help_a4=(TextView)findViewById(R.id.help_a4);
        help_a4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkPermission()) {
                    requestPermission();
                }
                String mobileNo = "0428 000 078";
                String uri = "tel:" + mobileNo.trim();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(uri));
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                        int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                }
                try {
                    startActivity(intent);
                }catch (SecurityException e){
                    e.printStackTrace();
                }
            }
        });
        help_a5=(TextView)findViewById(R.id.help_a5);
        help_a5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkPermission()) {
                    requestPermission();
                }
                String mobileNo = "0428 000 078";
                String uri = "tel:" + mobileNo.trim();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(uri));
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                        int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                }
                try {
                    startActivity(intent);
                }catch (SecurityException e){
                    e.printStackTrace();
                }
            }
        });
        help_a6=(TextView)findViewById(R.id.help_a6);
        help_a6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkPermission()) {
                    requestPermission();
                }
                String mobileNo = "0428 000 078";
                String uri = "tel:" + mobileNo.trim();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(uri));
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                        int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                }
                try {
                    startActivity(intent);
                }catch (SecurityException e){
                    e.printStackTrace();
                }
            }
        });
        help_a7=(TextView)findViewById(R.id.help_a7);
        help_a7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkPermission()) {
                    requestPermission();
                }
                String mobileNo = "0428 000 078";
                String uri = "tel:" + mobileNo.trim();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(uri));
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                        int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                }
                try {
                    startActivity(intent);
                }catch (SecurityException e){
                    e.printStackTrace();
                }
            }
        });
        help_a8=(TextView)findViewById(R.id.help_a8);
        help_a8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkPermission()) {
                    requestPermission();
                }
                String mobileNo = "0428 000 078";
                String uri = "tel:" + mobileNo.trim();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(uri));
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                        int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                }
                try {
                    startActivity(intent);
                }catch (SecurityException e){
                    e.printStackTrace();
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
        Intent intent=new Intent(Help_Center.this,Homescreen.class);
        startActivity(intent);
        Help_Center.this.finish();
    }
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), CALL_PHONE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{CALL_PHONE}, PERMISSION_REQUEST_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted){
                        String mobileNo = "0428 000 078";
                        String uri = "tel:" + mobileNo.trim();
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse(uri));
                        if (ActivityCompat.checkSelfPermission(Help_Center.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                        }
                        try {
                            startActivity(intent);
                        }catch (SecurityException e){
                            e.printStackTrace();
                        }
                    }
                    else {
                        Snackbar.make(app_coordinatorlayout, "Permission denied for phone call.", Snackbar.LENGTH_SHORT).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(CALL_PHONE)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{CALL_PHONE},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }
                break;
        }
    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(Help_Center.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
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
