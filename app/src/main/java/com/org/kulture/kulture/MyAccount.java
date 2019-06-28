package com.org.kulture.kulture;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.org.kulture.kulture.database.BillingDataSet;
import com.org.kulture.kulture.database.BillingDatabase;
import com.org.kulture.kulture.model.JSONParse;
import com.org.kulture.kulture.model.MyTextView;
import com.org.kulture.kulture.model.Terms_of_Sale;
import com.org.kulture.kulture.model.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import de.hdodenhof.circleimageview.CircleImageView;

public class MyAccount extends AppCompatActivity {
    ImageView back_from_cart,prescription_cart,edit_know;
    CircleImageView profile_image;
    String qty,id,email,username,getname,getemail;
    LinearLayout terms_conditions_layout,privacy_policy_layout,log_out_layout;
    TextView getconditiontext,getpolicytext,cart_title,cart_count;
    SharedPreferences preferences2,preferences3,preferences7;
    LinearLayout help_center_layout,click_for_upload;
    ImageView edit_profile;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    Integer myaccountCall;
    private String PROFILE_URL="http://kulture.biz/webservice/user_details_by_id.php?customer_id=";
    MyTextView profile_name,profile_email;

    TextView full_address;
    String CART_COUNT_USER_LOGIN_URL="http://kulture.biz/webservice/count_cart_via_user_id.php?user_id=";
    String CART_COUNT_USER_LOGOFF_URL="http://kulture.biz/webservice/count_cart_via_order_id.php?order_id=";

    SharedPreferences preferences5,preferences6;
    String getids,getIMIE,cart_counts,getUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_forgot);
        setSupportActionBar(toolbar);
        changeStatusBarColor();
        edit_profile = (ImageView) findViewById(R.id.edit_profile);
        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyAccount.this, Billing_Address_Activity.class);
                intent.putExtra("findactivity", "MyAccount");
                startActivity(intent);
                MyAccount.this.finish();
            }
        });
        click_for_upload = (LinearLayout) findViewById(R.id.click_for_upload);
        click_for_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        back_from_cart = (ImageView) findViewById(R.id.back_from_cart);
        profile_email = (MyTextView) findViewById(R.id.profile_email);
        profile_name = (MyTextView) findViewById(R.id.profile_name);
        profile_name.setVisibility(View.GONE);
        full_address = (TextView) findViewById(R.id.full_address);
        back_from_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyAccount.this, Homescreen.class);
                startActivity(intent);
                MyAccount.this.finish();
            }
        });
        profile_image = (CircleImageView) findViewById(R.id.profile_image);
        help_center_layout = (LinearLayout) findViewById(R.id.help_center_layout);
        edit_know=(ImageView)findViewById(R.id.edit_know);
        edit_know.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyAccount.this, Billing_Address_Activity.class);
                intent.putExtra("findactivity", "MyAccount");
                startActivity(intent);
                MyAccount.this.finish();
            }
        });
        help_center_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyAccount.this, Help_Center.class);
                startActivity(intent);
                MyAccount.this.finish();
            }
        });
        cart_title = (TextView) findViewById(R.id.cart_title);
        cart_title.setText("My Account");
        terms_conditions_layout = (LinearLayout) findViewById(R.id.terms_conditions_layout);
        getconditiontext = (TextView) findViewById(R.id.getconditiontext);
        terms_conditions_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Terms_of_Sale frag = Terms_of_Sale.addSomeString(getconditiontext.getText().toString());
                frag.show(ft, "txn_tag");
            }
        });
        BillingDatabase billdb = new BillingDatabase(MyAccount.this);
        List<BillingDataSet> billcontacts = billdb.getAllBillingContacts();
        if (billcontacts.size() >= 1) {
            for (BillingDataSet cn : billcontacts) {
                String f_name = cn.getFirst_name();
                String l_name = cn.getLast_name();
                String address = cn.getAddress();
                String phone = cn.getMob_number();
                String full_names = f_name + " " + l_name;
                try {
                    full_address.setText(full_names + "\n"
                            + address + "\n" + phone);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        } else if (billcontacts.size() == 0) {
            Toast.makeText(getApplicationContext(), "Add Billing address", Toast.LENGTH_SHORT).show();
        }
        privacy_policy_layout=(LinearLayout)findViewById(R.id.privacy_policy_layout);
        getpolicytext=(TextView)findViewById(R.id.getpolicytext);
        privacy_policy_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Terms_of_Sale frag = Terms_of_Sale.addSomeString(getpolicytext.getText().toString());
                frag.show(ft, "txn_tag");
            }
        });
        log_out_layout=(LinearLayout)findViewById(R.id.log_out_layout);
        log_out_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences7=getApplicationContext().getSharedPreferences("checkglogin", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor7 = preferences7.edit();
                editor7.clear();
                editor7.apply();
                preferences3=getApplicationContext().getSharedPreferences("register_save", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor3 = preferences3.edit();
                editor3.clear();
                editor3.apply();
                Intent intent=new Intent(MyAccount.this,Loginscreen.class);
                startActivity(intent);
                MyAccount.this.finish();
            }
        });
        prescription_cart=(ImageView)findViewById(R.id.prescription_cart);
        prescription_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MyAccount.this,CartActivity.class);
                startActivity(intent);
                MyAccount.this.finish();
            }
        });
        cart_count=(TextView)findViewById(R.id.cart_count);
        preferences5=getApplicationContext().getSharedPreferences("register_save", Context.MODE_PRIVATE);
        getids=preferences5.getString("adminid","");
        getname=preferences5.getString("adminname","");
        getemail=preferences5.getString("adminemail","");
        getUrl=preferences5.getString("adminurl","");

        try{
            profile_name.setText(getname);
            profile_email.setText(getemail);
            if(!getUrl.isEmpty()) {
                Glide.with(getApplicationContext())
                        .load(getUrl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .thumbnail(0.5f)
                        .crossFade()
                        .skipMemoryCache(true)
                        .animate(R.anim.bounce)
                        .into(profile_image);
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        preferences6=getApplicationContext().getSharedPreferences("mobimei_save", Context.MODE_PRIVATE);
        getIMIE=preferences6.getString("imei","");

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
        myaccountCall=Integer.parseInt(getids);
        try {
            new Profile_Details().execute();
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Gallery"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }
    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Gallery", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(MyAccount.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    userChoosenTask ="Take Photo";
                    boolean result= Utility.checkPermission(MyAccount.this);
                    if(result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Gallery")) {
                    userChoosenTask ="Choose from Gallery";
                    boolean result= Utility.checkPermission(MyAccount.this);
                    if(result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }
    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }
    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        profile_image.setImageBitmap(thumbnail);
        String savedestination=destination.getAbsolutePath();
        Log.d("savedestination",savedestination);
        profile_image.setVisibility(View.VISIBLE);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        profile_image.setImageBitmap(bm);
        profile_image.setVisibility(View.VISIBLE);
    }
    @Override
    public void onBackPressed() {
        Intent intent=new Intent(MyAccount.this,Homescreen.class);
        startActivity(intent);
        MyAccount.this.finish();
    }
    //----------------------------------------------Profile Details------------------------------------------------
    private class Profile_Details extends AsyncTask<String, String, Boolean> {
        JSONParse jsonParser = new JSONParse();
        @Override
        protected Boolean doInBackground(String... para) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                String json = jsonParser.makeHttpRequest(PROFILE_URL+myaccountCall, "POST", params);
                try {
                    JSONObject c = new JSONObject(json);
                    JSONObject obj = c.getJSONObject("customer");
                    id=obj.getString("id");
                    email=obj.getString("email");
                    username=obj.getString("username");

                }catch (JSONException e){
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            try{
            if(profile_email.getText().toString().isEmpty()
               && profile_name.getText().toString().isEmpty()) {

                profile_name.setText(username);
                profile_email.setText(email);

            }
            }catch (NullPointerException e){
                e.printStackTrace();
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
