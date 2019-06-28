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

public class ImageFullModeSub extends AppCompatActivity {
    private static ViewPager mPager;
    ImageView back_from_cart;
    String preid,pretitle,predesc,preslug,preprice,preimage;
    String cur_id,cur_image,cur_desc,cur_price,cur_name;
    private ArrayList<String> XMENArray = new ArrayList<String>();
    String image,id,price,extrasession,prename,session,name;
    ImageView show_logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_full_mode);

        cur_image = getIntent().getStringExtra("cur_image");
        cur_id = getIntent().getStringExtra("cur_id");
        cur_desc=getIntent().getStringExtra("cur_desc");

        cur_price=getIntent().getStringExtra("cur_price");
        cur_name=getIntent().getStringExtra("cur_name");
        session = getIntent().getStringExtra("session");

        pretitle=getIntent().getStringExtra("pretitle");
        preid = getIntent().getStringExtra("preid");
        predesc=getIntent().getStringExtra("predesc");

        preslug=getIntent().getStringExtra("preslug");
        prename = getIntent().getStringExtra("prename");
        preprice = getIntent().getStringExtra("preprice");

        preimage = getIntent().getStringExtra("preimage");
        extrasession=getIntent().getStringExtra("extrasession");

        back_from_cart=(ImageView)findViewById(R.id.back_from_cart);
        back_from_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(ImageFullModeSub.this, ProductDetailSub.class);
                    intent.putExtra("cur_image", cur_image);
                    intent.putExtra("cur_price", cur_price);
                    intent.putExtra("cur_id", cur_id);

                    intent.putExtra("cur_desc", cur_desc);
                    intent.putExtra("cur_name",cur_name);
                    intent.putExtra("session", session);

                    intent.putExtra("pretitle", pretitle);
                    intent.putExtra("predesc",predesc);
                    intent.putExtra("preid",preid);

                    intent.putExtra("preslug", preslug);
                    intent.putExtra("prename",prename);
                    intent.putExtra("preprice",preprice);

                    intent.putExtra("preimage",preimage);

                    startActivity(intent);
                    ImageFullModeSub.this.finish();

            }
        });
        XMENArray=getIntent().getStringArrayListExtra("imaglist");
        changeStatusBarColor();
        init();
        show_logo=(ImageView)findViewById(R.id.show_logo);
        show_logo.setVisibility(View.VISIBLE);
    }
    private void init() {
        for(int i=0;i<XMENArray.size();i++)
            mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new SliderAdapter(ImageFullModeSub.this,XMENArray));
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
        Intent intent = new Intent(ImageFullModeSub.this, ProductDetailSub.class);
        intent.putExtra("cur_image", cur_image);
        intent.putExtra("cur_price", cur_price);
        intent.putExtra("cur_id", cur_id);

        intent.putExtra("cur_desc", cur_desc);
        intent.putExtra("cur_name",cur_name);
        intent.putExtra("session", session);

        intent.putExtra("pretitle", pretitle);
        intent.putExtra("predesc",predesc);
        intent.putExtra("preid",preid);

        intent.putExtra("preslug", preslug);
        intent.putExtra("prename",prename);
        intent.putExtra("preprice",preprice);

        intent.putExtra("preimage",preimage);

        startActivity(intent);
        ImageFullModeSub.this.finish();

    }
}
