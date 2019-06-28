package com.org.kulture.kulture;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.org.kulture.kulture.model.JSONParse;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;

public class Registration extends AppCompatActivity {
    CheckBox check_sign_up;
    String check,email,username;
    Integer id;
    MaterialEditText reg_con_password,reg_password,reg_email,reg_fname,reg_lname;
    LinearLayout footerbar_register;

    String f_names,l_names,email_addresss,passwords,con_passwords,addname;
    private String REGISTER_URL="http://kulture.biz/webservice/registration.php";
    private String UPDATE_URL="http://kulture.biz/webservice/update_userid_into_cart.php?order_id=";

    SharedPreferences pref,preferences3;
    String getIMIE,result,getids;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        preferences3=getApplicationContext().getSharedPreferences("mobimei_save", Context.MODE_PRIVATE);
        getIMIE=preferences3.getString("imei","");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Registration.this,Loginscreen.class);
                startActivity(intent);
                Registration.this.finish();
            }
        });
        check_sign_up=(CheckBox)findViewById(R.id.check_sign_up);
        reg_con_password=(MaterialEditText)findViewById(R.id.reg_con_password);
        reg_password=(MaterialEditText)findViewById(R.id.reg_password);
        reg_email=(MaterialEditText)findViewById(R.id.reg_email);
        reg_fname=(MaterialEditText)findViewById(R.id.reg_fname);
        reg_lname=(MaterialEditText)findViewById(R.id.reg_lname);
        check_sign_up.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    check="true";
                }else if(!isChecked){
                    check="false";
                }
            }
        });
        footerbar_register=(LinearLayout)findViewById(R.id.footerbar_register);
        footerbar_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
    }
    //Validate Data locally(Checks whether the fields are empty or not)
    private void validateData() {
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(reg_fname.getText().toString())) {
            reg_fname.setError("Required field!");
            focusView = reg_fname;
            cancel = true;
        }else if(TextUtils.isEmpty(reg_lname.getText().toString())) {
            reg_lname.setError("Required field!");
            focusView = reg_lname;
            cancel = true;
        }else if(TextUtils.isEmpty(reg_email.getText().toString())) {
            reg_email.setError("Required field!");
            focusView = reg_email;
            cancel = true;
        }
        else if(TextUtils.isEmpty(reg_password.getText().toString())) {
            reg_password.setError("Required field!");
            focusView = reg_password;
            cancel = true;
        } else if(TextUtils.isEmpty(reg_con_password.getText().toString())) {
            reg_con_password.setError("Required field!");
            focusView = reg_con_password;
            cancel = true;
        }if (cancel) {
            focusView.requestFocus();
        } else {
            getTextValues();
        }
    }
    //Get the values from EditText
    private void getTextValues() {
        Toast.makeText(getApplicationContext(), "Loading..", Toast.LENGTH_LONG).show();
        f_names=reg_fname.getText().toString();
        l_names=reg_lname.getText().toString();
        addname=f_names +"_"+l_names;

        email_addresss=reg_email.getText().toString();
        passwords=reg_password.getText().toString();
        con_passwords=reg_con_password.getText().toString();
        check="true";
        if(passwords.equals(con_passwords)) {
            try {
                new Register_user().execute();
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }else {
            Toast.makeText(getApplicationContext(),"Check your password",Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public void onBackPressed() {
        Intent intent=new Intent(Registration.this,Loginscreen.class);
        startActivity(intent);
        Registration.this.finish();
    }
    //----------------------------------------------Register------------------------------------------------
    private class Register_user extends AsyncTask<String, String, Boolean> {
        JSONParse jsonParser = new JSONParse();
        @Override
        protected Boolean doInBackground(String... para) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                String json = jsonParser.makeHttpRequest(REGISTER_URL+"?email="+email_addresss+"&password="+passwords, "POST", params);
                try {
                    JSONObject c = new JSONObject(json);
                    JSONObject obj = c.getJSONObject("customer");
                    id=obj.getInt("id");
                    getids=String.valueOf(id);
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
            if (id > 0) {
                pref = getApplicationContext().getSharedPreferences("register_save", MODE_PRIVATE);
                SharedPreferences.Editor logineditor;
                logineditor = pref.edit();
                logineditor.putString("adminemail",email_addresss);
                logineditor.putString("adminid",getids);
                logineditor.putString("adminname",addname);
                logineditor.putString("adminurl","");
                logineditor.apply();
                try{
                    new Update_Cart().execute();
                }catch (IndexOutOfBoundsException e){
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), "Registered successfully.", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getApplicationContext(), "Try again.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    //----------------------------------------------Update Cart------------------------------------------------
    private class Update_Cart extends AsyncTask<String, String, Boolean> {
        JSONParse jsonParser = new JSONParse();
        @Override
        protected Boolean doInBackground(String... para) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                String json = jsonParser.makeHttpRequest(UPDATE_URL+getIMIE+"&user_id="+getids, "POST", params);
                try {
                    JSONObject c = new JSONObject(json);
                    result=c.getString("result");

                }catch (JSONException e){
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }@Override
        protected void onPostExecute(Boolean aBoolean) {
            Intent intent = new Intent(Registration.this, Loginscreen.class);
            startActivity(intent);
            Registration.this.finish();
        }
    }
}
