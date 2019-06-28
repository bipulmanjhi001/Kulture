package com.org.kulture.kulture.model;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.org.kulture.kulture.R;


/**
 * Created by Bipul on 22-02-2017.
 */
public class ResetPassword extends DialogFragment {
    EditText pass1, pass2;
    String password,confirmpassword;
    SharedPreferences pref;
    LinearLayout resetbutton,cancelbutton;

    private static String bodyText;

    public static ResetPassword addSomeString(String temp){
        ResetPassword f = new ResetPassword();
        bodyText = temp;
        return f;
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
        View root = inflater.inflate(R.layout.resetpassword, container, false);
        pass1 = (EditText) root.findViewById(R.id.resetpassword);
        pass2 = (EditText) root.findViewById(R.id.resetconfirmpassword);
        resetbutton=(LinearLayout)root.findViewById(R.id.resetpasswordbutton);
        cancelbutton=(LinearLayout)root.findViewById(R.id.cancelbutton);
        cancelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        resetbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
        return root;
    }
    //Validate Data locally(Checks whether the fields are empty or not)
    private void validateData() {
        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(pass1.getText().toString())) {
            pass1.setError("Required field!");
            focusView = pass1;
            cancel = true;
        } else if (TextUtils.isEmpty(pass2.getText().toString())) {
            pass2.setError("Required field!");
            focusView = pass2;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            getTextValues();
        }
    }//validateData
    //Get the values from EditText
    private void getTextValues() {
        password = pass1.getText().toString().trim();
        confirmpassword = pass2.getText().toString().trim();

    }
 }
