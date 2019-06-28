package com.org.kulture.kulture;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.org.kulture.kulture.model.GmailSucess;
import com.org.kulture.kulture.model.JSONParse;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;

public class Loginscreen extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener  {

    LinearLayout register,forgot_password;
    FloatingActionButton login_fab;
    private static final String TAG = Loginscreen.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;

    private GoogleApiClient mGoogleApiClient;
    private SignInButton btnSignIn;
    LinearLayout google,sign_in_button;
    String getid,s_email,s_pass,email,result,new_pass;
    String personName,gid,personPhotoUrl,getids,getIMIE,results;
    SharedPreferences preferences,preferences2,preferences3,pref;
    MaterialEditText sign_email,sign_password;

    private String UPDATE_URL="http://kulture.biz/webservice/update_userid_into_cart.php?order_id=";
    private String LOGIN_URL="http://kulture.biz/webservice/check_login.php?uname=";

    int user_id;
    String ids;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginscreen);

        preferences=getApplicationContext().getSharedPreferences("checkglogin", Context.MODE_PRIVATE);
        getid=preferences.getString("key","");

        btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);
        google=(LinearLayout)findViewById(R.id.google);

        preferences2=getApplicationContext().getSharedPreferences("register_save", Context.MODE_PRIVATE);
        getids=preferences2.getString("adminid","");

        preferences3=getApplicationContext().getSharedPreferences("mobimei_save", Context.MODE_PRIVATE);
        getIMIE=preferences3.getString("imei","");

        sign_email=(MaterialEditText)findViewById(R.id.sign_email);
        sign_password=(MaterialEditText)findViewById(R.id.sign_password);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        // Customizing G+ button
        btnSignIn.setSize(SignInButton.SIZE_STANDARD);

        btnSignIn.setScopes(gso.getScopeArray());
        register=(LinearLayout)findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Loginscreen.this,Registration.class);
                startActivity(intent);
                Loginscreen.this.finish();
            }
        });
        login_fab=(FloatingActionButton)findViewById(R.id.login_fab);
        login_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Loginscreen.this,Homescreen.class);
                startActivity(intent);
                Loginscreen.this.finish();
            }
        });
        sign_in_button=(LinearLayout)findViewById(R.id.sign_in_button);
        sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
        forgot_password=(LinearLayout)findViewById(R.id.forgot_password);
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Loginscreen.this,ForgotActivity.class);
                startActivity(intent);
                Loginscreen.this.finish();
            }
        });
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGoogleApiClient.isConnected()) {
                     signIn();
                    mGoogleApiClient.disconnect();
                    mGoogleApiClient.connect();
                }
            }
        });
    }
    private void validateData() {
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(sign_email.getText().toString())) {
            sign_email.setError("Required field!");
            focusView = sign_email;
            cancel = true;
        }else if(TextUtils.isEmpty(sign_password.getText().toString())) {
            sign_password.setError("Required field!");
            focusView = sign_password;
            cancel = true;
        }  if (cancel) {
            focusView.requestFocus();
        } else {
            s_email=sign_email.getText().toString();
            s_pass=sign_password.getText().toString();
            byte[] encodeValue = Base64.encode(s_pass.getBytes(), Base64.NO_WRAP);
            new_pass=new String(encodeValue);
            getTextValues();
        }
    }
    private void getTextValues() {
        Toast.makeText(getApplicationContext(), "Loading..", Toast.LENGTH_LONG).show();
        try{
           new Login_User().execute();
       }catch (Exception e){
           e.printStackTrace();
       }
    }
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            if (acct != null) {
                personName = acct.getDisplayName();
                gid=acct.getId();
                email = acct.getEmail();
                try {
                    personPhotoUrl = acct.getPhotoUrl().toString();
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }

            if(!personPhotoUrl.isEmpty()) {
                try {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    GmailSucess frag = new GmailSucess();
                    frag.getGmailId(email, personName, gid, personPhotoUrl);
                    frag.show(ft, "txn_tag");
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }else if(personPhotoUrl.isEmpty()){
                try {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    GmailSucess frag = new GmailSucess();
                    frag.getGmailId2(email, personName, gid);
                    frag.show(ft, "txn_tag");
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);

        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }
    @Override
    public void onBackPressed() {
       Loginscreen.this.finish();
    }

    //----------------------------------------------Login now------------------------------------------------
    private class Login_User extends AsyncTask<String, String, Boolean> {
        JSONParse jsonParser = new JSONParse();
        @Override
        protected Boolean doInBackground(String... para) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                String json = jsonParser.makeHttpRequest(LOGIN_URL+s_email+"&pass="+new_pass, "POST", params);
                try {
                    JSONObject c = new JSONObject(json);
                    results=c.getString("result");
                    user_id=c.getInt("user_id");

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
            if (user_id > 0 && getids.isEmpty()) {
                ids=String.valueOf(user_id);
                pref = getApplicationContext().getSharedPreferences("register_save", MODE_PRIVATE);
                SharedPreferences.Editor logineditor;
                logineditor = pref.edit();
                logineditor.putString("adminid",ids);
                logineditor.putString("adminemail",s_email);
                logineditor.putString("adminname","");
                logineditor.putString("adminurl","");
                logineditor.apply();

                try {
                    new Update_Cart2().execute();
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }else if(user_id > 0 && !getids.isEmpty()) {
                try {
                    new Update_Cart().execute();
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
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
            Intent intent = new Intent(Loginscreen.this, Homescreen.class);
            startActivity(intent);
            Loginscreen.this.finish();
            Toast.makeText(getApplicationContext(), "Login successfully.", Toast.LENGTH_SHORT).show();
        }
    }
    //----------------------------------------------Update Cart2------------------------------------------------
    private class Update_Cart2 extends AsyncTask<String, String, Boolean> {
        JSONParse jsonParser = new JSONParse();
        @Override
        protected Boolean doInBackground(String... para) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                String json = jsonParser.makeHttpRequest(UPDATE_URL+getIMIE+"&user_id="+ids, "POST", params);
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
            Intent intent = new Intent(Loginscreen.this, Homescreen.class);
            startActivity(intent);
            Loginscreen.this.finish();
            Toast.makeText(getApplicationContext(), "Login successfully.", Toast.LENGTH_SHORT).show();

        }
    }
}
