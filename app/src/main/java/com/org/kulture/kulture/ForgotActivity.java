package com.org.kulture.kulture;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.org.kulture.kulture.model.ResetPassword;
import com.rengwuxian.materialedittext.MaterialEditText;

public class ForgotActivity extends AppCompatActivity {
   FloatingActionButton forgot_fab;
   String getforgot_password_edit;
    MaterialEditText forgot_password_edit;
    LinearLayout forgot_submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        forgot_fab=(FloatingActionButton)findViewById(R.id.forgot_fab);
        forgot_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ForgotActivity.this,Loginscreen.class);
                startActivity(intent);
                ForgotActivity.this.finish();
            }
        });
        forgot_password_edit=(MaterialEditText)findViewById(R.id.forgot_password_edit);
        forgot_submit=(LinearLayout)findViewById(R.id.forgot_submit);
        forgot_submit.setOnClickListener(new View.OnClickListener() {
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
        if (TextUtils.isEmpty(forgot_password_edit.getText().toString())) {
            forgot_password_edit.setError("Required field!");
            focusView = forgot_password_edit;
            cancel = true;
        }if (cancel) {
            focusView.requestFocus();
        } else {
            getTextValues();
        }
    }
    //Get the values from EditText
    private void getTextValues() {
        getforgot_password_edit=forgot_password_edit.getText().toString();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ResetPassword frag = ResetPassword.addSomeString(getforgot_password_edit);
        frag.show(ft, "txn_tag");

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ForgotActivity.this, Loginscreen.class);
        startActivity(intent);
        ForgotActivity.this.finish();
    }
}
