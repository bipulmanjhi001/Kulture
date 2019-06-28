package com.org.kulture.kulture;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.org.kulture.kulture.model.ConnectionDetector;

import static android.Manifest.permission.READ_PHONE_STATE;


public class Splashscreen extends AppCompatActivity {
    // splash screen timer
    private Handler handler = new Handler();
    ImageView animate;
    private static int SPLASH_TIME_OUT = 4000;
    CoordinatorLayout coordinatorlayout;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    Boolean checkcondition=false;
    Boolean handle_setting=false;
    String IMEINumber;
    SharedPreferences pref;

    private static final int PERMISSION_REQUEST_CODE = 205;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splashscreen);
        changeStatusBarColor();
        Animation bottomUp = AnimationUtils.loadAnimation(this,
                R.anim.zoomin);
        final Animation zoomout = AnimationUtils.loadAnimation(this,
                R.anim.zoomout);
        animate = (ImageView) findViewById(R.id.imgLogo);
        animate.startAnimation(bottomUp);
        animate.startAnimation(zoomout);
        coordinatorlayout = (CoordinatorLayout) findViewById(R.id.checkconnection);
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        try {
            if (!checkPermission()) {
                requestPermission();
                if (isInternetPresent) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(checkcondition){
                                onBackPressed();
                            }else if(!checkcondition) {
                                Intent i = new Intent(Splashscreen.this, Homescreen.class);
                                startActivity(i);
                                Splashscreen.this.finish();
                            }else if(!handle_setting){
                                refresh(savedInstanceState);
                            }
                        }
                    }, SPLASH_TIME_OUT);
                }else {
                    // Ask user to connect to Internet
                    Snackbar snackbar = Snackbar
                            .make(coordinatorlayout, "No internet connection ", Snackbar.LENGTH_LONG)
                            .setAction("RETRY", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startActivity(new Intent(Settings.ACTION_SETTINGS));
                                    handle_setting=true;
                                }
                            });
                    TextView snackbarActionTextView = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_action);
                    snackbarActionTextView.setTextSize(14);

                    snackbarActionTextView.setTextColor(Color.RED);
                    snackbarActionTextView.setTypeface(snackbarActionTextView.getTypeface(), Typeface.BOLD);

                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    textView.setMaxLines(1);
                    textView.setTextSize(14);
                    textView.setSingleLine(true);
                    textView.setTypeface(null, Typeface.BOLD);
                    snackbar.show();
                }
            } else if (checkPermission()) {
                TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                IMEINumber = tm.getDeviceId();
                Log.d("IMEI",IMEINumber);
                pref = getApplicationContext().getSharedPreferences("mobimei_save", MODE_PRIVATE);
                SharedPreferences.Editor logineditor;
                logineditor = pref.edit();
                logineditor.putString("imei",IMEINumber);
                logineditor.apply();
                if (isInternetPresent) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(checkcondition){
                                onBackPressed();
                            }else if(!checkcondition) {
                                Intent i = new Intent(Splashscreen.this, Homescreen.class);
                                startActivity(i);
                                Splashscreen.this.finish();
                            }else if(!handle_setting){
                                refresh(savedInstanceState);
                            }
                        }
                    }, SPLASH_TIME_OUT);
                }else {
                    // Ask user to connect to Internet
                    Snackbar snackbar = Snackbar
                            .make(coordinatorlayout, "No internet connection ", Snackbar.LENGTH_LONG)
                            .setAction("RETRY", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startActivity(new Intent(Settings.ACTION_SETTINGS));
                                    handle_setting=true;
                                }
                            });
                    TextView snackbarActionTextView = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_action);
                    snackbarActionTextView.setTextSize(14);

                    snackbarActionTextView.setTextColor(Color.RED);
                    snackbarActionTextView.setTypeface(snackbarActionTextView.getTypeface(), Typeface.BOLD);

                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    textView.setMaxLines(1);
                    textView.setTextSize(14);
                    textView.setSingleLine(true);
                    textView.setTypeface(null, Typeface.BOLD);
                    snackbar.show();
                }
            }
        }catch (NullPointerException e){
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
    public void onBackPressed() {
        checkcondition=true;
        Splashscreen.this.finish();
    }
    public void refresh(Bundle view){          //refresh is onClick name given to the button
        onRestart();
    }
    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
        Intent i = new Intent(Splashscreen.this, Splashscreen.class);
        startActivity(i);
        finish();
    }

    //----------------------------------------------Get IMEI Number----------------------------------------------------
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), READ_PHONE_STATE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{READ_PHONE_STATE}, PERMISSION_REQUEST_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted){
                        doPermissionGrantedStuffs();
                        if (ActivityCompat.checkSelfPermission(Splashscreen.this, READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                        }
                    }
                    else {
                        Snackbar.make(coordinatorlayout, "Permission denied for phone call.", Snackbar.LENGTH_SHORT).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(READ_PHONE_STATE)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{READ_PHONE_STATE},
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
        new AlertDialog.Builder(Splashscreen.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    public void doPermissionGrantedStuffs() {
        //Have an  object of TelephonyManager
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        IMEINumber = tm.getDeviceId();
        Log.d("IMEI",IMEINumber);
        pref = getApplicationContext().getSharedPreferences("mobimei_save", MODE_PRIVATE);
        SharedPreferences.Editor logineditor;
        logineditor = pref.edit();
        logineditor.putString("imei",IMEINumber);
        logineditor.apply();
    }

}
