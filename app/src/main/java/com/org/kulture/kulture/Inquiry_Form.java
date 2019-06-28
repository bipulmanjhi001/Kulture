package com.org.kulture.kulture;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.org.kulture.kulture.model.JSONParse;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.NameValuePair;


public class Inquiry_Form extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    ImageView back_from_productdetails;
    TextView cart_item,category_title;
    String title,item,findsession,name,id,image,ids,slug;
    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;
    MaterialEditText desired_date_quote;
    Spinner spinner;
    ImageView show_corporate_image;
    LinearLayout submit;

    String[] size = {"Please choose a design","Style 001","Style 017","Style 018","Style 023","Style 035","Style 037","Style 044","Style 046","Style 047","Style 048","Style 050","Style 051","Style 052","Style 053","Style 054","Style 055"};
    String[] co_size={"Please choose a design","Corporate 001","Corporate 002","Corporate 003","Corporate 004","Corporate 005","Corporate 006","Corporate 007","Corporate 008","Corporate 009","Corporate 010"};

    String array_img;
    ImageView select_image1,select_image2,select_image3,select_image4;
    CardView img1,img2,img3,img4;

    String CART_COUNT_USER_LOGIN_URL="http://kulture.biz/webservice/count_cart_via_user_id.php?user_id=";
    String CART_COUNT_USER_LOGOFF_URL="http://kulture.biz/webservice/count_cart_via_order_id.php?order_id=";

    SharedPreferences preferences2,preferences3;
    String getids,getIMIE,cart_counts;

    MaterialEditText f_name_quote,l_name_quote,email_quote,organization_quote,
            phone_quote,est_qty_quote,delivery_quote;

    EditText additional_comments;
    RadioButton promotional_yes,promotional_no;
    CheckBox styles_adult,styles_women,style_kids;

    String f_name_quote_s,l_name_quote_s,email_quote_s,additional_comments_s,
            organization_quote_s,phone_quote_s,est_qty_quote_s,delivery_quote_s;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry__form);
        changeStatusBarColor();

        title= getIntent().getStringExtra("title");
        name=getIntent().getStringExtra("name");
        id=getIntent().getStringExtra("id");
        ids=getIntent().getStringExtra("ids");
        slug=getIntent().getStringExtra("slug");
        image=getIntent().getStringExtra("image");
        findsession=getIntent().getStringExtra("findsession");
        array_img=getIntent().getStringExtra("imgarray");

        category_title=(TextView)findViewById(R.id.category_name);
        show_corporate_image=(ImageView)findViewById(R.id.show_corporate_image);
        select_image1=(ImageView)findViewById(R.id.select_image1);
        select_image2=(ImageView)findViewById(R.id.select_image2);
        select_image3=(ImageView)findViewById(R.id.select_image3);
        select_image4=(ImageView)findViewById(R.id.select_image4);
        img1=(CardView)findViewById(R.id.img_a1);

        promotional_no=(RadioButton)findViewById(R.id.promotional_no);
        promotional_yes=(RadioButton)findViewById(R.id.promotional_yes);
        styles_adult=(CheckBox) findViewById(R.id.styles_adult);
        styles_women=(CheckBox) findViewById(R.id.styles_women);
        style_kids=(CheckBox)findViewById(R.id.style_kids);

        f_name_quote=(MaterialEditText)findViewById(R.id.f_name_quote);
        l_name_quote=(MaterialEditText)findViewById(R.id.l_name_quote);
        email_quote=(MaterialEditText)findViewById(R.id.email_quote);
        organization_quote=(MaterialEditText)findViewById(R.id.organization_quote);
        phone_quote=(MaterialEditText)findViewById(R.id.phone_quote);
        est_qty_quote=(MaterialEditText)findViewById(R.id.est_qty_quote);
        delivery_quote=(MaterialEditText)findViewById(R.id.delivery_quote);
        additional_comments=(EditText)findViewById(R.id.additional_comments);

        submit=(LinearLayout)findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(getApplicationContext())
                        .load(image)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .thumbnail(0.5f)
                        .crossFade()
                        .skipMemoryCache(true)
                        .animate(R.anim.bounce)
                        .into(show_corporate_image);
            }
        });
        img2=(CardView)findViewById(R.id.img_a2);
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(getApplicationContext())
                        .load(array_img)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .thumbnail(0.5f)
                        .crossFade()
                        .skipMemoryCache(true)
                        .animate(R.anim.bounce)
                        .into(show_corporate_image);
            }
        });
        img3=(CardView)findViewById(R.id.img_a3);
        img4=(CardView)findViewById(R.id.img_a4);
        try{
            category_title.setText(title);
            Glide.with(getApplicationContext())
                    .load(image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(0.5f)
                    .crossFade()
                    .skipMemoryCache(true)
                    .animate(R.anim.bounce)
                    .into(show_corporate_image);
            Glide.with(getApplicationContext())
                    .load(image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(0.5f)
                    .crossFade()
                    .skipMemoryCache(true)
                    .animate(R.anim.bounce)
                    .into(select_image1);

            select_image1.setVisibility(View.VISIBLE);
            img1.setVisibility(View.VISIBLE);

            Glide.with(getApplicationContext())
                    .load(array_img)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(0.5f)
                    .crossFade()
                    .skipMemoryCache(true)
                    .animate(R.anim.bounce)
                    .into(select_image2);

            select_image2.setVisibility(View.VISIBLE);
            img2.setVisibility(View.VISIBLE);

        }catch (NullPointerException e){
            e.printStackTrace();
        }

        desired_date_quote=(MaterialEditText)findViewById(R.id.desired_date_quote);
        desired_date_quote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(Inquiry_Form.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        back_from_productdetails=(ImageView)findViewById(R.id.back_from_productdetails);

        back_from_productdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(findsession.equals("SportItemsList")){

                    Intent intent = new Intent(Inquiry_Form.this, SportItemsList.class);
                    intent.putExtra("id", ids);
                    intent.putExtra("slug", slug);
                    intent.putExtra("name",name);
                    startActivity(intent);
                    Inquiry_Form.this.finish();

                }else if(findsession.equals("Corporate_Wear")) {

                    Intent intent = new Intent(Inquiry_Form.this, Corporate_Wear.class);
                    intent.putExtra("id", ids);
                    intent.putExtra("slug", slug);
                    intent.putExtra("name", name);
                    startActivity(intent);
                    Inquiry_Form.this.finish();

                }
            }
        });
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        cart_item=(TextView)findViewById(R.id.product_cart_count);

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
        spinner = (Spinner) findViewById(R.id.spinner_choose_design);
        if(findsession.equals("SportItemsList")) {
            ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, size);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(aa);
            spinner.setOnItemSelectedListener(this);
        }else if(findsession.equals("Corporate_Wear")) {
            ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, co_size);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(aa);
            spinner.setOnItemSelectedListener(this);
        }
    }
    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        desired_date_quote.setText(sdf.format(myCalendar.getTime()));
    }
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        item = parent.getItemAtPosition(position).toString();
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    @Override
    public void onBackPressed() {
        if(findsession.equals("SportItemsList")){
            Intent intent = new Intent(Inquiry_Form.this, SportItemsList.class);
            intent.putExtra("id", ids);
            intent.putExtra("slug", slug);
            intent.putExtra("name",name);
            startActivity(intent);
            Inquiry_Form.this.finish();
        }else if(findsession.equals("Corporate_Wear")) {
            Intent intent = new Intent(Inquiry_Form.this, Corporate_Wear.class);
            intent.putExtra("id", ids);
            intent.putExtra("slug", slug);
            intent.putExtra("name", name);
            startActivity(intent);
            Inquiry_Form.this.finish();
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

    private void validateData() {
        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(f_name_quote.getText().toString())) {
            f_name_quote.setError("Required field!");
            focusView = f_name_quote;
            cancel = true;
        }else if(TextUtils.isEmpty(l_name_quote.getText().toString())) {
            l_name_quote.setError("Required field!");
            focusView = l_name_quote;
            cancel = true;
        }  else if(TextUtils.isEmpty(email_quote.getText().toString())) {
            email_quote.setError("Required field!");
            focusView = email_quote;
            cancel = true;
        }else if(TextUtils.isEmpty(organization_quote.getText().toString())) {
            organization_quote.setError("Required field!");
            focusView = organization_quote;
            cancel = true;
        }else if(TextUtils.isEmpty(phone_quote.getText().toString())) {
            phone_quote.setError("Required field!");
            focusView = phone_quote;
            cancel = true;
        }else if(TextUtils.isEmpty(est_qty_quote.getText().toString())) {
            est_qty_quote.setError("Required field!");
            focusView = est_qty_quote;
            cancel = true;
        }
        else if(TextUtils.isEmpty(delivery_quote.getText().toString())) {
            delivery_quote.setError("Required field!");
            focusView = delivery_quote;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {

            f_name_quote_s=f_name_quote.getText().toString();
            l_name_quote_s=l_name_quote.getText().toString();
            email_quote_s=f_name_quote.getText().toString();
            organization_quote_s=l_name_quote.getText().toString();
            phone_quote_s=f_name_quote.getText().toString();
            est_qty_quote_s=l_name_quote.getText().toString();
            delivery_quote_s=f_name_quote.getText().toString();
            additional_comments_s=l_name_quote.getText().toString();

            getTextValues();
        }
    }
    private void getTextValues() {
        Toast.makeText(getApplicationContext(), "Submitted", Toast.LENGTH_LONG).show();
        try{
            new Inquiry_User().execute();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //----------------------------------------------User Inquiry------------------------------------------------
    private class Inquiry_User extends AsyncTask<String, String, Boolean> {
        JSONParse jsonParser = new JSONParse();
        @Override
        protected Boolean doInBackground(String... para) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();

              /*String json = jsonParser.makeHttpRequest(REGISTER_URL+"?email="+
                email_addresss+"&password="+passwords, "POST", params);

                try {
                    JSONObject c = new JSONObject(json);
                    JSONObject obj = c.getJSONObject("customer");
                    id=obj.getInt("id");
                    email=obj.getString("email");
                    username=obj.getString("username");

                }catch (JSONException e){
                    e.printStackTrace();
                }*/

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Boolean aBoolean) {
                Toast.makeText(getApplicationContext(), "Try again.", Toast.LENGTH_SHORT).show();
        }
    }
}
