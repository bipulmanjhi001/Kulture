package com.org.kulture.kulture;

/**
 * Created by tkru on 10/24/2017.
 */

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.org.kulture.kulture.model.SliderAdapter;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

public class ImageFullMode extends AppCompatActivity {
    private static ViewPager mPager;
    ImageView back_from_cart;
    String preid,pretitle,predesc,preslug,preprice,preimage;

    private ArrayList<String> XMENArray = new ArrayList<String>();
    String prename,session;
    ImageView show_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_full_mode);
        pretitle=getIntent().getStringExtra("title");
        preid = getIntent().getStringExtra("id");
        predesc=getIntent().getStringExtra("desc");

        preslug=getIntent().getStringExtra("slug");
        prename = getIntent().getStringExtra("prename");
        preprice = getIntent().getStringExtra("price");

        preimage = getIntent().getStringExtra("image");
        session = getIntent().getStringExtra("session");
        back_from_cart=(ImageView)findViewById(R.id.back_from_cart);

        back_from_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(ImageFullMode.this, ProductDetail.class);
                    intent.putExtra("image", preimage);
                    intent.putExtra("price", preprice);
                    intent.putExtra("title", pretitle);

                    intent.putExtra("desc", predesc);
                    intent.putExtra("slug", preslug);
                    intent.putExtra("id", preid);

                    intent.putExtra("prename", prename);
                    intent.putExtra("session", session);
                    startActivity(intent);
                    ImageFullMode.this.finish();
            }
        });
        XMENArray=getIntent().getStringArrayListExtra("imaglist");
        show_logo=(ImageView)findViewById(R.id.show_logo);
        show_logo.setVisibility(View.VISIBLE);
        changeStatusBarColor();
        init();
    }
    private void init() {
        for(int i=0;i<XMENArray.size();i++)
            mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new SliderAdapter(ImageFullMode.this,XMENArray));
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
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
        Intent intent = new Intent(ImageFullMode.this, ProductDetail.class);
        intent.putExtra("image", preimage);
        intent.putExtra("price", preprice);
        intent.putExtra("title", pretitle);

        intent.putExtra("desc", predesc);
        intent.putExtra("slug", preslug);
        intent.putExtra("id", preid);

        intent.putExtra("prename", prename);
        intent.putExtra("session", session);
        startActivity(intent);
        ImageFullMode.this.finish();

    }
}
