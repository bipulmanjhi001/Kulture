package com.org.kulture.kulture.model;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.org.kulture.kulture.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by tkru on 9/19/2017.
 */

public class GmailSucess extends DialogFragment {
    LinearLayout payment_ok;
    TextView gemail_id,g_name,gmail_id;
    private static String g_id,g_names,ge_id,g_url;
    CircleImageView checked_circle;

    Integer id;
    String ids,result;
    String username,getIMIE;
    MaterialEditText reg_password;

    String email_addresss,password;
    private String REGISTER_URL="http://kulture.biz/webservice/registration.php";
    private String UPDATE_URL="http://kulture.biz/webservice/update_userid_into_cart.php?order_id=";
    SharedPreferences pref,preferences3;

    public void getGmailId(String temp1, String temp2, String temp3, String temp4){
        g_id=temp1;
        g_names=temp2;
        ge_id=temp3;
        g_url=temp4;
    };

    public void getGmailId2(String temp1, String temp2, String temp3){
        g_id=temp1;
        g_names=temp2;
        ge_id=temp3;
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MY_DIALOG2);
    }
    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(width, height);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.gmail_sucessfull, container, false);

        preferences3=getActivity().getApplicationContext().getSharedPreferences("mobimei_save", MODE_PRIVATE);
        getIMIE=preferences3.getString("imei","");

        payment_ok = (LinearLayout)root.findViewById(R.id.payment_ok);
        gmail_id=(TextView)root.findViewById(R.id.gmail_id);
        gmail_id.setText(ge_id);
        checked_circle=(CircleImageView)root.findViewById(R.id.checked_circle);
        reg_password=(MaterialEditText)root.findViewById(R.id.reg_password);

        try {
            if(!g_url.isEmpty()) {
                Glide.with(getActivity()).load(g_url)
                        .thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(checked_circle);
            }else {
                checked_circle.setVisibility(View.GONE);
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        gemail_id= (TextView)root.findViewById(R.id.gemail_id);
        gemail_id.setText(g_id);
        g_name= (TextView)root.findViewById(R.id.g_name);
        g_name.setText(g_names);
        payment_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gemail_id.getText().toString().length() > 0) {
                    SharedPreferences.Editor e = getActivity().getSharedPreferences("checkglogin", MODE_PRIVATE).edit();
                    e.putString ("key","value_exist");
                    e.apply();
                    validateData();
                    dismiss();
                }
            }
        });
        return root;
    }
    private void validateData() {
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(reg_password.getText().toString())) {
            reg_password.setError("Required field!");
            focusView = reg_password;
            cancel = true;
        }if (cancel) {
            focusView.requestFocus();
        } else {
            password=reg_password.getText().toString();
            getTextValues();
        }
    }
    private void getTextValues() {
        Toast.makeText(getActivity().getApplicationContext(), "Loading..", Toast.LENGTH_LONG).show();
        try{
            new Register_user().execute();
        }catch (Exception f){
            f.printStackTrace();
        }
    }
    //----------------------------------------------Register------------------------------------------------
    private class Register_user extends AsyncTask<String, String, Boolean> {
        JSONParse jsonParser = new JSONParse();
        @Override
        protected Boolean doInBackground(String... para) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                String json = jsonParser.makeHttpRequest(REGISTER_URL+"?email="+g_id+"&password="+password, "POST", params);
                try {
                    JSONObject c = new JSONObject(json);
                    JSONObject obj = c.getJSONObject("customer");
                    id=obj.getInt("id");
                    email_addresss=obj.getString("email");
                    username=obj.getString("username");

                }catch (JSONException e){
                    e.printStackTrace();
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            try {
                if (id > 0) {
                    ids = String.valueOf(id);
                    pref = getActivity().getApplicationContext().getSharedPreferences("register_save", Context.MODE_PRIVATE);
                    SharedPreferences.Editor logineditor;
                    logineditor = pref.edit();
                    logineditor.putString("adminid",ids);
                    logineditor.putString("adminemail",g_id);
                    logineditor.putString("adminname",g_names);
                    logineditor.putString("adminurl",g_url);
                    logineditor.apply();

                    Toast.makeText(getActivity().getApplicationContext(), "Registered successfully.Plz login", Toast.LENGTH_SHORT).show();
                    try{
                        new Update_Cart().execute();
                    }catch (IndexOutOfBoundsException e){
                        e.printStackTrace();
                    }
                }else {
                    dismiss();
                    Toast.makeText(getActivity().getApplicationContext(), "Already Registered.", Toast.LENGTH_SHORT).show();
                }
            }catch (NullPointerException e){
                e.printStackTrace();
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
                String json = jsonParser.makeHttpRequest(UPDATE_URL + getIMIE + "&user_id=" + ids, "POST", params);
                try{
                    JSONObject c = new JSONObject(json);
                    result = c.getString("result");

                }catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Boolean aBoolean) {
             dismiss();
        }
    }
}