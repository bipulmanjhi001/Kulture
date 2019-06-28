package com.org.kulture.kulture.model;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.org.kulture.kulture.CartActivity;
import com.org.kulture.kulture.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by tkru on 11/23/2017.
 */

public class EmptyCart extends DialogFragment {
    LinearLayout comment_cancels,comment_done;
    Integer id;
    String getid,getIMIE,success;

    String All_Delete_Using_User_Id = "http://kulture.biz/webservice/delete_all_cart_via_user_id.php?user_id=";
    String All_Delete_Using_Order_Id = "http://kulture.biz/webservice/delete_all_cart_via_order_id.php?order_id=";

    SharedPreferences pref,preferences3;

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
        View root = inflater.inflate(R.layout.delete_confirmation_box, container, false);

        preferences3=getActivity().getApplicationContext().getSharedPreferences("mobimei_save", MODE_PRIVATE);
        getIMIE=preferences3.getString("imei","");

        pref = getActivity().getApplicationContext().getSharedPreferences("register_save", Context.MODE_PRIVATE);
        getid = pref.getString("adminid", "");

        comment_cancels = (LinearLayout)root.findViewById(R.id.comment_cancels );
        comment_done = (LinearLayout)root.findViewById(R.id.comment_done);
        comment_cancels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    dismiss();
            }
        });
        comment_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!getid.isEmpty() && !getIMIE.isEmpty()) {
                    try {
                        new Delete_ALL_Login_Cart().execute();
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                } else if (getid.isEmpty() && !getIMIE.isEmpty()) {
                    try {
                        new Delete_ALL_Logoff_Cart().execute();
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        return root;
    }
    //----------------------------------------------Login Delete All------------------------------------------------
    private class Delete_ALL_Login_Cart extends AsyncTask<String, String, Boolean> {
        JSONParse jsonParser = new JSONParse();
        @Override
        protected Boolean doInBackground(String... para) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                String json = jsonParser.makeHttpRequest(All_Delete_Using_User_Id + getid, "POST", params);
                try {
                    JSONObject c = new JSONObject(json);
                    success=c.getString("result");

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
            if(success.equals("Successful")) {
                Intent intent=new Intent(getActivity(), CartActivity.class);
                startActivity(intent);
                 dismiss();
                Toast.makeText(getActivity().getApplicationContext(), "Your Cart is empty!!.", Toast.LENGTH_SHORT).show();

            }else {
                dismiss();
                Toast.makeText(getActivity().getApplicationContext(), "No data Found", Toast.LENGTH_SHORT).show();
            }
        }
    }
    //----------------------------------------------Logoff Delete All------------------------------------------------
    private class Delete_ALL_Logoff_Cart extends AsyncTask<String, String, Boolean> {
        JSONParse jsonParser = new JSONParse();
        @Override
        protected Boolean doInBackground(String... para) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                String json = jsonParser.makeHttpRequest(All_Delete_Using_Order_Id + getIMIE, "POST", params);

                try {
                    JSONObject c = new JSONObject(json);
                    success=c.getString("result");

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
            if(success.equals("Successful")) {
                Intent intent=new Intent(getActivity(), CartActivity.class);
                startActivity(intent);
                dismiss();
                Toast.makeText(getActivity().getApplicationContext(), "Your Cart is empty!!.", Toast.LENGTH_SHORT).show();
            }else {
                dismiss();
                Toast.makeText(getActivity().getApplicationContext(), "No data Found", Toast.LENGTH_SHORT).show();
            }
        }
    }

}