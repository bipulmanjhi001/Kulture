package com.org.kulture.kulture;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.org.kulture.kulture.model.PayPalConfig;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

public class PaymentScreen extends AppCompatActivity {

    ImageView back_from_cart;
    TextView cart_title,payment_grand_total,payment_price,payment_delivery,payment_total,bounce_payment_price_detail;
    String totalprice,qty;
    LinearLayout payment_cart_payment,check_payment_price;
    Animation shake;
    RadioButton payment_paypal;

    // PayPal configuration
    private static PayPalConfiguration paypalConfig = new PayPalConfiguration()
            .environment(PayPalConfig.PAYPAL_ENVIRONMENT).clientId(
                    PayPalConfig.PAYPAL_CLIENT_ID);

    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final String TAG = PaymentScreen.class.getSimpleName();
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        changeStatusBarColor();

        totalprice= getIntent().getStringExtra("grand_total");
      //  subtotals= getIntent().getStringExtra("subtotal");

        payment_grand_total=(TextView)findViewById(R.id.payment_grand_total);
        payment_price=(TextView)findViewById(R.id.payment_price);
        payment_delivery=(TextView)findViewById(R.id.payment_delivery);
        payment_total=(TextView)findViewById(R.id.payment_total);
        cart_title=(TextView)findViewById(R.id.cart_title);
     try {
         cart_title.setText("Payments");
         payment_grand_total.setText(totalprice);
         payment_total.setText(totalprice);
         payment_price.setText(totalprice);
     }catch (NullPointerException e){
         e.printStackTrace();
     }
        payment_cart_payment=(LinearLayout)findViewById(R.id.payment_cart_payment);
        payment_cart_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(payment_grand_total != null && payment_grand_total.getText().toString().length()>=2 && payment_paypal.isChecked()){
                    launchPayPalPayment();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Check one gateway.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        payment_paypal=(RadioButton)findViewById(R.id.payment_paypal);

       payment_paypal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if(payment_paypal.isChecked()){
                   Toast.makeText(getApplicationContext(),"Paypal",Toast.LENGTH_SHORT).show();
               }
           }
       });
        bounce_payment_price_detail=(TextView)findViewById(R.id.bounce_payment_price_detail);
        check_payment_price=(LinearLayout)findViewById(R.id.check_payment_price);
        check_payment_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                bounce_payment_price_detail.startAnimation(shake);
            }
        });
        back_from_cart=(ImageView)findViewById(R.id.back_from_cart);
        back_from_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PaymentScreen.this,Delivery_Details.class);
                intent.putExtra("grand_total", totalprice);
                startActivity(intent);
                PaymentScreen.this.finish();
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
    /**
     * Preparing final cart amount that needs to be sent to PayPal for payment
     * */
    private PayPalPayment prepareFinalCart() {
        String total_amt=payment_grand_total.getText().toString();
        BigDecimal total=new BigDecimal(total_amt);

        PayPalPayment payment = new PayPalPayment(
                total,
                PayPalConfig.DEFAULT_CURRENCY,
                "Description about transaction. This will be displayed to the user.",
                PayPalConfig.PAYMENT_INTENT);

        // Custom field like invoice_number etc.,
        payment.custom("This is text that will be associated with the payment that the app can use.");

        return payment;
    }

    /**
     * Launching PalPay payment activity to complete the payment
     * */
    private void launchPayPalPayment() {

        PayPalPayment thingsToBuy = prepareFinalCart();
        Intent intent = new Intent(PaymentScreen.this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfig);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingsToBuy);
        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }
    /**
     * Receiving the PalPay payment response
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        Log.e(TAG, confirm.toJSONObject().toString(4));
                        Log.e(TAG, confirm.getPayment().toJSONObject().toString(4));
                        String paymentId = confirm.toJSONObject().getJSONObject("response").getString("id");

                        String payment_client = confirm.getPayment().toJSONObject().toString();

                        Log.e(TAG, "paymentId: " + paymentId + ", payment_json: " + payment_client);

                        if(paymentId.length()>0) {
                          Intent intent=new Intent(PaymentScreen.this,PaymentMsg.class);
                            intent.putExtra("paymentid",paymentId);
                            intent.putExtra("payby","PaymentScreen");
                            startActivity(intent);
                            PaymentScreen.this.finish();
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.e(TAG, "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.e(TAG, "An invalid Payment or PayPalConfiguration was submitted.");
            }
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent=new Intent(PaymentScreen.this,Delivery_Details.class);
        intent.putExtra("grand_total", totalprice);
        startActivity(intent);
        PaymentScreen.this.finish();
    }
}
