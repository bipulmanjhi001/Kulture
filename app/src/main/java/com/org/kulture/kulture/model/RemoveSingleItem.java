package com.org.kulture.kulture.model;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.org.kulture.kulture.CartActivity;
import com.org.kulture.kulture.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;

/**
 * Created by tkru on 11/23/2017.
 */

public class RemoveSingleItem extends DialogFragment {
    LinearLayout comment_cancels,comment_done;
    private String SINGLE_PRODUCT_DELETE = "http://kulture.biz/webservice/delete_single_product_by_id.php?id=";
    String result,id;

    public void addSomeString(String temp){
        id = temp;
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
        View root = inflater.inflate(R.layout.remove_single_item, container, false);

        comment_done=(LinearLayout)root.findViewById(R.id.comment_done);
        comment_cancels=(LinearLayout)root.findViewById(R.id.comment_cancels);
        comment_cancels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        comment_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new Delete_Single_Product().execute();
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }
        });
        return root;
    }
    //----------------------------------------------Delete Single Product------------------------------------------------
    private class Delete_Single_Product extends AsyncTask<String, String, Boolean> {
        JSONParse jsonParser = new JSONParse();

        @Override
        protected Boolean doInBackground(String... para) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                String json = jsonParser.makeHttpRequest(SINGLE_PRODUCT_DELETE+id, "POST", params);
                try {
                    JSONObject c = new JSONObject(json);
                    result = c.getString("result");

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
            if(result.equals("Successful")) {
                Intent intent = new Intent(getActivity(), CartActivity.class);
                startActivity(intent);
                dismiss();
            }else {
                dismiss();
            }
        }
    }
}
