package com.org.kulture.kulture;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by tkru on 9/21/2017.
 */

public class PaymentMsg extends AppCompatActivity {
    LinearLayout payment_ok;
    TextView output;
    private static String bodyText;
    String payment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_sucessfull);
        changeStatusBarColor();
        bodyText = getIntent().getStringExtra("paymentid");
        payment=getIntent().getStringExtra("payby");
        payment_ok = (LinearLayout)findViewById(R.id.payment_ok);

        output = (TextView) findViewById(R.id.output);
        output.setText(bodyText);

        payment_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((output.getText().toString().length() > 0) && payment.equals("PaymentScreen")) {
                    Intent intent = new Intent(PaymentMsg.this, Homescreen.class);
                    startActivity(intent);
                    PaymentMsg.this.finish();
                }
            }
        });
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
        Intent intent = new Intent(PaymentMsg.this, Homescreen.class);
        startActivity(intent);
        PaymentMsg.this.finish();
    }
    @SuppressLint("NewApi")
    private void sendNotification() {
        Intent intent = new Intent(this, MachineActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 100, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
         Notification notif = new Notification.Builder(this)
                .setContentIntent(pendingIntent)
                .setContentTitle("Thank you for using Kulture.")
                .setContentText("Your Trnsaction id :"+bodyText)
                .setSmallIcon(R.mipmap.small_logo)
                .build();
        notif.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(1, notif);
    }
}