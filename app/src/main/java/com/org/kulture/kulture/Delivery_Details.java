package com.org.kulture.kulture;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.org.kulture.kulture.database.BillingDataSet;
import com.org.kulture.kulture.database.BillingDatabase;

import java.text.DecimalFormat;
import java.util.List;


public class Delivery_Details extends AppCompatActivity {

    ImageView back_from_cart;
    TextView cart,grand_total,bounce_price_detail;
    Button edit;
    LinearLayout place_payment,add_to_cart;
    TextView price,order_total,ship_charges,subtotal,terms_accept;
    String totalprice;
    CheckBox check_terms;
    Animation shake;
    EditText activate_coupon_box;
    TextView full_name,Address,Mobile_Number,check_coupon_validity;
    DecimalFormat two = new DecimalFormat("0.00");
    List<BillingDataSet> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_details);
        changeStatusBarColor();
        totalprice= getIntent().getStringExtra("grand_total");
        back_from_cart=(ImageView)findViewById(R.id.back_from_cart);

        cart=(TextView)findViewById(R.id.cart_title);
        order_total=(TextView)findViewById(R.id.order_total);
        ship_charges=(TextView)findViewById(R.id.ship_charges);
        subtotal=(TextView)findViewById(R.id.subtotal);
        grand_total=(TextView)findViewById(R.id.grand_total);

        check_coupon_validity=(TextView)findViewById(R.id.check_coupon_validity);
        activate_coupon_box=(EditText)findViewById(R.id.activate_coupon_box);
        try {
            cart.setText("Delivery");
            subtotal.setText(totalprice);

            order_total.setText(totalprice);
            grand_total.setText(totalprice);
           double done=Double.parseDouble(totalprice);

            if(done >= 65.00){
                ship_charges.setText("$ 0.00");
            }else if(done < 65.00){
                ship_charges.setText("$ 11.35");
                done=done+11.35;
                String calc=String.valueOf(two.format(done));
                grand_total.setText(calc);
                order_total.setText(calc);
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        back_from_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Delivery_Details.this,CartActivity.class);
                startActivity(intent);
                Delivery_Details.this.finish();
            }
        });
        edit=(Button) findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Delivery_Details.this,Billing_Address_Activity.class);
                intent.putExtra("findactivity","Delivery_Details");
                intent.putExtra("grand_total", grand_total.getText().toString());
                startActivity(intent);
                Delivery_Details.this.finish();
            }
        });
        place_payment=(LinearLayout)findViewById(R.id.place_payment);
        place_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check_terms.isChecked() && contacts.size()!= 0){
                    Intent intent=new Intent(Delivery_Details.this,PaymentScreen.class);
                    intent.putExtra("grand_total", grand_total.getText().toString());
                    startActivity(intent);
                    Delivery_Details.this.finish();
                }else if(check_terms.isChecked() && contacts.size()== 0) {
                    Toast.makeText(getApplicationContext(),"Add Billing address",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"plz accept our terms of sale.",Toast.LENGTH_SHORT).show();
                }
            }
        });
        add_to_cart=(LinearLayout)findViewById(R.id.add_to_cart);
        bounce_price_detail=(TextView)findViewById(R.id.bounce_price_detail);
        add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                bounce_price_detail.startAnimation(shake);
            }
        });
        terms_accept=(TextView)findViewById(R.id.terms_accept);
        check_terms=(CheckBox)findViewById(R.id.check_terms);

        full_name=(TextView)findViewById(R.id.full_name);
        Address=(TextView)findViewById(R.id.Address);
        Mobile_Number=(TextView)findViewById(R.id.Phone);

        // Reading all contacts
        BillingDatabase db= new BillingDatabase(Delivery_Details.this);
        contacts = db.getAllBillingContacts();
        if(contacts.size()>=1){
            for (BillingDataSet cn : contacts) {
                String f_name = cn.getFirst_name();
                String l_name=cn.getLast_name();
                String address=cn.getAddress();
                String phone=cn.getMob_number();
                String full_names=f_name+" "+l_name;
                try {
                    full_name.setText(full_names);
                    Address.setText(address);
                    Mobile_Number.setText(phone);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }else if(contacts.size()== 0){
          Toast.makeText(getApplicationContext(),"Add Billing address",Toast.LENGTH_SHORT).show();
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
        Intent intent=new Intent(Delivery_Details.this,CartActivity.class);
        startActivity(intent);
        Delivery_Details.this.finish();
    }
}
