package com.org.kulture.kulture;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.org.kulture.kulture.model.JSONParse;
import com.org.kulture.kulture.model.MyBounceInterpolator;
import com.org.kulture.kulture.model.MyTextView;
import com.org.kulture.kulture.model.ProductDetailspinner;
import com.org.kulture.kulture.model.SizeLoading;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;


public class ProductDetail extends AppCompatActivity  {

    ImageView back_from_productdetails,product_cart;
    ImageView whishlist_off,whishlist_on;
    Spinner spinner;
    String slug,desc,price,item;
    ImageView show_image;
    String prename,size=null;
    Double prices;
    ImageView select_image1,select_image2,select_image3,select_image4;

    MyTextView product_details,product_title,product_sizelist,product_weight;
    MyTextView product_upc_text,product_upc,product_size_text,product_weight_text;
    TextView cart_item,updated_price,chargesvalue,qty_value,instockvalue2;
    LinearLayout add_to_cart;
    String totalprice,id,image,session,title;
    TextView instockvalue;
    DecimalFormat two = new DecimalFormat("0.00");
    String pricess,titles,description,img_url,cart_counts;
    ProductDetail activity = null;
    CardView img_a2,img_a1,img_a3,img_a4;

    String PRODUCT_DETAILS_URL= "http://kulture.biz/webservice/product_details_by_id.php?product_id=";
    String PRODUCT_SIZE_URL= "http://kulture.biz/webservice/get_product_variations_by_product_id.php?product_id=";
    String ADD_TO_CART="http://www.kulture.biz/webservice/cart.php?order_id=";

    ArrayList<String> showImages= new ArrayList<String>();
    ArrayList<Integer> showRelatedIds=new ArrayList<Integer>();
    ArrayList<String> stocklist=new ArrayList<String>();
    ArrayList<String> sizelist=new ArrayList<String>();
    ArrayList<String> idlist=new ArrayList<String>();
    public  ArrayList<SizeLoading> CustomListViewValuesArr = new ArrayList<SizeLoading>();

    int num,counter;
    LinearLayout choose_size,teamwear_active,product_details_show;
    String stock,weight,upc,novalue,qty;

    ImageView addition,subtraction,related_image1,related_image2,related_image3,related_image4,related_image5;
    TextView related_product_name1,related_product_name2,related_product_name3,related_product_name4,related_product_name5;
    TextView get_related_product_price1,get_related_product_price2,get_related_product_price3,get_related_product_price4,get_related_product_price5;
    String img1,name1,price1,img2,name2,price2,img3,name3,price3,img4,name4,price4,img5,name5,price5;
    CardView related_card1,related_card2,related_card3,related_card4,related_card5;

    TextView get_related_product_image1,get_related_product_image2,get_related_product_image3,get_related_product_image4,get_related_product_image5;
    TextView get_related_product_desc5,get_related_product_desc4,get_related_product_desc3,get_related_product_desc2,get_related_product_desc1;
    ProductDetailspinner adapter;
    ProgressBar progress_dailog,progress_dailog2;
    String desc1,desc2,desc3,desc4,desc5,getIMIE,getuid,message,status;
    RadioButton radioButton1,radioButton2,radioButton3,radioButton4;
    MaterialEditText form_number,form_name;

    SharedPreferences preferences,preferences2;
    int stock_quantity;

    String CART_COUNT_USER_LOGIN_URL="http://kulture.biz/webservice/count_cart_via_user_id.php?user_id=";
    String CART_COUNT_USER_LOGOFF_URL="http://kulture.biz/webservice/count_cart_via_order_id.php?order_id=";
    ArrayList<String> addedstockqty=new ArrayList<String>();
    ArrayList<String> addedstocksize=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        id= getIntent().getStringExtra("id");
        desc= getIntent().getStringExtra("desc");
        title= getIntent().getStringExtra("title");
        price= getIntent().getStringExtra("price");
        image=getIntent().getStringExtra("image");
        slug= getIntent().getStringExtra("slug");
        prename=getIntent().getStringExtra("prename");
        session=getIntent().getStringExtra("session");

        num=Integer.parseInt(id);
        changeStatusBarColor();
        cart_item=(TextView)findViewById(R.id.product_cart_count);
        updated_price=(TextView)findViewById(R.id.updated_price);
        instockvalue=(TextView)findViewById(R.id.instockvalue);

        chargesvalue=(TextView)findViewById(R.id.chargesvalue);
        product_cart=(ImageView)findViewById(R.id.product_cart);
        product_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProductDetail.this,CartActivity.class);
                startActivity(intent);
                ProductDetail.this.finish();
            }
        });
        activity = this;
        product_title=(MyTextView)findViewById(R.id.product_name);
        product_weight=(MyTextView)findViewById(R.id.product_weight);
        product_details=(MyTextView)findViewById(R.id.product_details);
        product_upc=(MyTextView)findViewById(R.id.product_upc);
        product_upc_text=(MyTextView)findViewById(R.id.product_upc_text);
        product_details_show=(LinearLayout)findViewById(R.id.product_details_show);
        novalue="";
        product_size_text=(MyTextView)findViewById(R.id.product_size_text);
        product_weight_text=(MyTextView)findViewById(R.id.product_weight_text);
        teamwear_active=(LinearLayout)findViewById(R.id.teamwear_active);
        progress_dailog=(ProgressBar)findViewById(R.id.progress_dailog);
        progress_dailog2=(ProgressBar)findViewById(R.id.progress_dailog2);
        progress_dailog.setVisibility(View.VISIBLE);
        instockvalue2=(TextView)findViewById(R.id.instockvalue2);
        try {
            if(prename.equals("Team Wear")){
                teamwear_active.setVisibility(View.VISIBLE);
                form_name=(MaterialEditText)findViewById(R.id.form_name);
                form_number=(MaterialEditText)findViewById(R.id.form_number);
                radioButton1=(RadioButton)findViewById(R.id.radioButton1);
                radioButton1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(radioButton1.isChecked()){
                            form_name.setVisibility(View.VISIBLE);
                        }
                    }
                });
                radioButton2=(RadioButton)findViewById(R.id.radioButton2);
                radioButton2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        form_name.setVisibility(View.GONE);
                    }
                });
                radioButton3=(RadioButton)findViewById(R.id.radioButton3);
                radioButton3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        form_number.setVisibility(View.VISIBLE);
                    }
                });
                radioButton4=(RadioButton)findViewById(R.id.radioButton4);
                radioButton4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        form_number.setVisibility(View.GONE);
                    }
                });
            }
            product_title.setText(title);
            product_details.setText(desc);
            updated_price.setText(price);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        img_a1=(CardView)findViewById(R.id.img_a1);
        img_a2=(CardView)findViewById(R.id.img_a2);
        img_a3=(CardView)findViewById(R.id.img_a3);
        img_a4=(CardView)findViewById(R.id.img_a4);

        preferences2=getApplicationContext().getSharedPreferences("mobimei_save", Context.MODE_PRIVATE);
        getIMIE=preferences2.getString("imei","");

        preferences=getApplicationContext().getSharedPreferences("register_save", Context.MODE_PRIVATE);
        getuid=preferences.getString("adminid","");

        if(!getuid.isEmpty()&& !getIMIE.isEmpty()) {
            try {
                new Login_Cart_Count().execute();
            }catch (Exception e){
                e.printStackTrace();
            }
        }else if(getuid.isEmpty()&& !getIMIE.isEmpty()){
            try {
                new Logoff_Cart_Count().execute();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        /*-------------------------------  Add to cart--------------------------------------------- */
        add_to_cart=(LinearLayout)findViewById(R.id.add_to_cart);
        add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    qty = qty_value.getText().toString();
                    addedstockqty.add(qty);
                    int qtys=Integer.parseInt(qty);
                    stock=instockvalue.getText().toString();
                    if (qty.equals(stock)){
                        try {
                            String getcount=cart_item.getText().toString();
                            int counts=Integer.parseInt(getcount);
                            counts++;
                            cart_item.setText(String.valueOf(counts));
                        }catch (NullPointerException e){
                            e.printStackTrace();
                        }
                        try {
                            new Add_To_Cart().execute();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        Toast.makeText(getApplicationContext(), "Added to cart", Toast.LENGTH_SHORT).show();
                    } else if(Integer.parseInt(qty) < Integer.parseInt(stock)){
                        try {
                            String getcount=cart_item.getText().toString();
                            int counts=Integer.parseInt(getcount);
                            counts++;
                            cart_item.setText(String.valueOf(counts));
                        }catch (NullPointerException e){
                            e.printStackTrace();
                        }
                        try {
                            new Add_To_Cart().execute();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        Toast.makeText(getApplicationContext(), "Added to cart", Toast.LENGTH_SHORT).show();
                    }else if(qtys < stock_quantity){
                        try {
                            String getcount=cart_item.getText().toString();
                            int counts=Integer.parseInt(getcount);
                            counts++;
                            cart_item.setText(String.valueOf(counts));
                        }catch (NullPointerException e){
                            e.printStackTrace();
                        }
                        try {
                            new Add_To_Cart().execute();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        Toast.makeText(getApplicationContext(), "Added to cart", Toast.LENGTH_SHORT).show();
                    }
                    else if(stock.isEmpty()){
                        try {
                            String getcount=cart_item.getText().toString();
                            int counts=Integer.parseInt(getcount);
                            counts++;
                            cart_item.setText(String.valueOf(counts));
                        }catch (NullPointerException e){
                            e.printStackTrace();
                        }
                        try {
                            new Add_To_Cart().execute();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        Toast.makeText(getApplicationContext(), "Added to cart", Toast.LENGTH_SHORT).show();
                    }else if(prename.equals("Team Wear")){
                        try {
                            String getcount=cart_item.getText().toString();
                            int counts=Integer.parseInt(getcount);
                            counts++;
                            cart_item.setText(String.valueOf(counts));
                        }catch (NullPointerException e){
                            e.printStackTrace();
                        }
                        try {
                            new Add_To_Cart().execute();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        Toast.makeText(getApplicationContext(), "Added to cart", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Check Size", Toast.LENGTH_SHORT).show();
                    }
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }
        });
        /*-------------------------------  End Add to cart--------------------------------------------- */
        back_from_productdetails=(ImageView)findViewById(R.id.back_from_productdetails);
        back_from_productdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(session.equals("CategoryListActivity")) {
                    Intent intent = new Intent(ProductDetail.this, CategoryListActivity.class);
                    intent.putExtra("slug",slug);
                    intent.putExtra("title",title);
                    intent.putExtra("prename",prename);
                    intent.putExtra("id",id);
                    startActivity(intent);
                    ProductDetail.this.finish();
                }else if(session.equals("Homescreen")) {
                    Intent intent = new Intent(ProductDetail.this, Homescreen.class);
                    startActivity(intent);
                    ProductDetail.this.finish();
                }
            }
        });
        whishlist_off=(ImageView)findViewById(R.id.whishlist_off);
        whishlist_on=(ImageView)findViewById(R.id.whishlist_on);
        whishlist_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whishlist_off.setVisibility(View.GONE);
                whishlist_on.setVisibility(View.VISIBLE);
                final Animation myAnim = AnimationUtils.loadAnimation(ProductDetail.this, R.anim.bounce);
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.1, 10);
                myAnim.setInterpolator(interpolator);
                whishlist_on.startAnimation(myAnim);
            }
        });
     //-------------------------------------------------------------------------------------------------------
        related_image1=(ImageView)findViewById(R.id.related_image1);
        related_image2=(ImageView)findViewById(R.id.related_image2);
        related_image3=(ImageView)findViewById(R.id.related_image3);
        related_image4=(ImageView)findViewById(R.id.related_image4);
        related_image5=(ImageView)findViewById(R.id.related_image5);

        related_product_name1=(TextView)findViewById(R.id.related_product_name1);
        related_product_name2=(TextView)findViewById(R.id.related_product_name2);
        related_product_name3=(TextView)findViewById(R.id.related_product_name3);
        related_product_name4=(TextView)findViewById(R.id.related_product_name4);
        related_product_name5=(TextView)findViewById(R.id.related_product_name5);

        get_related_product_price1=(TextView)findViewById(R.id.get_related_product_price1);
        get_related_product_price2=(TextView)findViewById(R.id.get_related_product_price2);
        get_related_product_price3=(TextView)findViewById(R.id.get_related_product_price3);
        get_related_product_price4=(TextView)findViewById(R.id.get_related_product_price4);
        get_related_product_price5=(TextView)findViewById(R.id.get_related_product_price5);

        related_card1=(CardView)findViewById(R.id.related_card1);
        related_card2=(CardView)findViewById(R.id.related_card2);
        related_card3=(CardView)findViewById(R.id.related_card3);
        related_card4=(CardView)findViewById(R.id.related_card4);
        related_card5=(CardView)findViewById(R.id.related_card5);

        get_related_product_image1=(TextView)findViewById(R.id.get_related_product_image1);
        get_related_product_image2=(TextView)findViewById(R.id.get_related_product_image2);
        get_related_product_image3=(TextView)findViewById(R.id.get_related_product_image3);
        get_related_product_image4=(TextView)findViewById(R.id.get_related_product_image4);
        get_related_product_image5=(TextView)findViewById(R.id.get_related_product_image5);

        get_related_product_desc5=(TextView)findViewById(R.id.get_related_product_desc5);
        get_related_product_desc4=(TextView)findViewById(R.id.get_related_product_desc4);
        get_related_product_desc3=(TextView)findViewById(R.id.get_related_product_desc3);
        get_related_product_desc2=(TextView)findViewById(R.id.get_related_product_desc2);
        get_related_product_desc1=(TextView)findViewById(R.id.get_related_product_desc1);

        related_card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetail.this, ProductDetailSub.class);
                intent.putExtra("cur_id", String.valueOf(showRelatedIds.get(0)));
                intent.putExtra("cur_image",get_related_product_image1.getText().toString());
                intent.putExtra("cur_desc",get_related_product_desc1.getText().toString());

                intent.putExtra("cur_price",get_related_product_price1.getText().toString());
                intent.putExtra("cur_name",related_product_name1.getText().toString());
                intent.putExtra("pretitle",title);

                intent.putExtra("preid",id);
                intent.putExtra("preslug",slug);
                intent.putExtra("prename",prename);

                intent.putExtra("predesc",desc);
                intent.putExtra("preprice",price);
                intent.putExtra("preimage",image);

                intent.putExtra("session",session);
                startActivity(intent);
                ProductDetail.this.finish();

            }
        });

        related_card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetail.this, ProductDetailSub.class);
                intent.putExtra("cur_id", String.valueOf(showRelatedIds.get(1)));
                intent.putExtra("cur_image",get_related_product_image2.getText().toString());
                intent.putExtra("cur_desc",get_related_product_desc2.getText().toString());

                intent.putExtra("cur_price",get_related_product_price2.getText().toString());
                intent.putExtra("cur_name",related_product_name2.getText().toString());
                intent.putExtra("session",session);

                intent.putExtra("pretitle",title);
                intent.putExtra("preid",id);
                intent.putExtra("predesc",desc);

                intent.putExtra("preslug",slug);
                intent.putExtra("prename",prename);
                intent.putExtra("preprice",price);

                intent.putExtra("preimage",image);
                startActivity(intent);
                ProductDetail.this.finish();
            }
        });

        related_card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetail.this, ProductDetailSub.class);
                intent.putExtra("cur_id", String.valueOf(showRelatedIds.get(2)));
                intent.putExtra("cur_image",get_related_product_image3.getText().toString());
                intent.putExtra("cur_desc",get_related_product_desc3.getText().toString());

                intent.putExtra("cur_price",get_related_product_price3.getText().toString());
                intent.putExtra("cur_name",related_product_name3.getText().toString());
                intent.putExtra("session",session);

                intent.putExtra("pretitle",title);
                intent.putExtra("preid",id);
                intent.putExtra("preslug",slug);

                intent.putExtra("predesc",desc);
                intent.putExtra("prename",prename);
                intent.putExtra("preprice",price);

                intent.putExtra("preimage",image);
                startActivity(intent);
                ProductDetail.this.finish();

            }
        });

        related_card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetail.this, ProductDetailSub.class);
                intent.putExtra("cur_id", String.valueOf(showRelatedIds.get(3)));
                intent.putExtra("cur_image",get_related_product_image4.getText().toString());
                intent.putExtra("cur_desc",get_related_product_desc4.getText().toString());

                intent.putExtra("cur_price",get_related_product_price4.getText().toString());
                intent.putExtra("cur_name",related_product_name4.getText().toString());
                intent.putExtra("session",session);

                intent.putExtra("pretitle",title);
                intent.putExtra("preid",id);
                intent.putExtra("predesc",desc);

                intent.putExtra("preslug",slug);
                intent.putExtra("prename",prename);
                intent.putExtra("preprice",price);

                intent.putExtra("preimage",image);
                startActivity(intent);
                ProductDetail.this.finish();
            }
        });
        related_card5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetail.this, ProductDetailSub.class);
                intent.putExtra("cur_id", String.valueOf(showRelatedIds.get(4)));
                intent.putExtra("cur_image",get_related_product_image5.getText().toString());
                intent.putExtra("cur_desc",get_related_product_desc5.getText().toString());

                intent.putExtra("cur_price",get_related_product_price5.getText().toString());
                intent.putExtra("cur_name",related_product_name5.getText().toString());
                intent.putExtra("session",session);

                intent.putExtra("pretitle",title);
                intent.putExtra("preid",id);
                intent.putExtra("predesc",desc);

                intent.putExtra("preslug",slug);
                intent.putExtra("prename",prename);
                intent.putExtra("preprice",price);

                intent.putExtra("preimage",image);
                startActivity(intent);
                ProductDetail.this.finish();
            }
        });
     //------------------------------------------------------------------------------------------
        show_image=(ImageView)findViewById(R.id.show_image);
        show_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(product_details_show.getVisibility() == View.VISIBLE) {
                    Intent intent = new Intent(ProductDetail.this, ImageFullMode.class);
                    intent.putExtra("image", image);
                    intent.putExtra("price", price);
                    intent.putExtra("title", title);
                    intent.putExtra("slug", slug);
                    intent.putExtra("desc", desc);
                    intent.putExtra("id", id);
                    intent.putExtra("prename", prename);
                    intent.putExtra("session", session);
                    intent.putStringArrayListExtra("imaglist", showImages);
                    startActivity(intent);
                    ProductDetail.this.finish();
                }else{
                    Toast.makeText(getApplicationContext(),"Loading..",Toast.LENGTH_SHORT).show();
                }
            }
        });
        try {
            Glide.with(getApplicationContext())
                    .load(image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(0.5f)
                    .crossFade()
                    .skipMemoryCache(true)
                    .animate(R.anim.bounce)
                    .into(show_image);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        select_image3=(ImageView)findViewById(R.id.select_image3);
        select_image4=(ImageView)findViewById(R.id.select_image4);

        select_image2=(ImageView)findViewById(R.id.select_image2);
        try {
            select_image4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if(showImages.get(3)!=null) {
                            Glide.with(getApplicationContext())
                                    .load(showImages.get(3))
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .thumbnail(0.5f)
                                    .crossFade()
                                    .skipMemoryCache(true)
                                    .animate(R.anim.bounce)
                                    .into(show_image);
                            select_image4.setVisibility(View.VISIBLE);
                            img_a4.setVisibility(View.VISIBLE);
                        }else if(showImages.get(3)==null) {
                            select_image4.setVisibility(View.GONE);
                            img_a4.setVisibility(View.GONE);
                        }
                    }catch (IndexOutOfBoundsException e){
                        e.printStackTrace();
                    }
                }
            });
            select_image3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (showImages.get(0) != null) {
                            Glide.with(getApplicationContext())
                                    .load(showImages.get(0))
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .thumbnail(0.5f)
                                    .crossFade()
                                    .skipMemoryCache(true)
                                    .animate(R.anim.bounce)
                                    .into(show_image);
                            select_image3.setVisibility(View.VISIBLE);
                            img_a3.setVisibility(View.VISIBLE);
                        } else if(showImages.get(0) == null){
                            select_image3.setVisibility(View.GONE);
                            img_a3.setVisibility(View.GONE);
                        }
                    }catch (IndexOutOfBoundsException e){
                        e.printStackTrace();
                    }
                }
            });

            select_image2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                    if(showImages.get(1)!=null) {
                        Glide.with(getApplicationContext())
                                .load(showImages.get(1))
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .thumbnail(0.5f)
                                .crossFade()
                                .skipMemoryCache(true)
                                .animate(R.anim.bounce)
                                .into(show_image);
                        select_image2.setVisibility(View.VISIBLE);
                        img_a2.setVisibility(View.VISIBLE);
                    }else if(showImages.get(1)==null) {
                        select_image2.setVisibility(View.GONE);
                        img_a2.setVisibility(View.GONE);
                    }
                }catch (IndexOutOfBoundsException e){
                    e.printStackTrace();
                }
                }
            });
            select_image1 = (ImageView) findViewById(R.id.select_image1);
            product_sizelist=(MyTextView)findViewById(R.id.product_sizelist);
            select_image1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                    if(showImages.get(2)!=null) {
                        Glide.with(getApplicationContext())
                                .load(showImages.get(2))
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .thumbnail(0.5f)
                                .crossFade()
                                .skipMemoryCache(true)
                                .animate(R.anim.bounce)
                                .into(show_image);
                        select_image1.setVisibility(View.VISIBLE);
                        img_a1.setVisibility(View.VISIBLE);
                    }else if(showImages.get(2)==null){
                        select_image1.setVisibility(View.GONE);
                        img_a1.setVisibility(View.GONE);
                    }
                }catch (IndexOutOfBoundsException e){
                    e.printStackTrace();
                }
                }
            });
        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }
        new Product_Details_Data().execute();

        whishlist_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whishlist_on.setVisibility(View.GONE);
                whishlist_off.setVisibility(View.VISIBLE);
                final Animation myAnim = AnimationUtils.loadAnimation(ProductDetail.this, R.anim.bounce);
                // Use bounce interpolator with amplitude 0.2 and frequency 20
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.1, 10);
                myAnim.setInterpolator(interpolator);
                whishlist_off.startAnimation(myAnim);
            }
        });

        spinner = (Spinner) findViewById(R.id.spinner_size);
        choose_size=(LinearLayout)findViewById(R.id.choose_size);
        adapter = new ProductDetailspinner(activity, R.layout.spinner_rows, CustomListViewValuesArr);
        // Set adapter to spinner
        spinner.setAdapter(adapter);
        // Listener called when spinner item selected
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
                // Get selected row data to show on screen
               size = ((TextView) v.findViewById(R.id.size)).getText().toString();
                addedstocksize.add(size);
                try {
                    if(!size.equals("Choose an option")) {
                        item = stocklist.get(position);
                        instockvalue.setText(item);
                        instockvalue.setVisibility(View.GONE);
                        chargesvalue.setText(item + " in stock ");
                        chargesvalue.setVisibility(View.VISIBLE);
                    }else if(size.isEmpty() && !item.isEmpty()) {
                        item = stocklist.get(position);
                        instockvalue.setText(item);
                        instockvalue.setVisibility(View.GONE);
                        chargesvalue.setText(item + " in stock ");
                        chargesvalue.setVisibility(View.VISIBLE);
                    }else {
                        chargesvalue.setVisibility(View.INVISIBLE);
                    }
                }catch (IndexOutOfBoundsException e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                chargesvalue.setVisibility(View.INVISIBLE);
            }

        });
        qty_value=(TextView)findViewById(R.id.qty_value);
        qty_value.setText("1");
        addition=(ImageView)findViewById(R.id.addition);
        qty = qty_value.getText().toString();
        addition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int stockist=Integer.parseInt(instockvalue.getText().toString());

                    stock = instockvalue.getText().toString();
                    String sizes=product_sizelist.getText().toString();
                    if (counter < stockist) {
                        counter++;
                        qty_value.setText(String.valueOf(counter));
                        qty = qty_value.getText().toString();
                        prices = Double.parseDouble(price) * Double.parseDouble(qty);
                            totalprice = String.valueOf(two.format(prices));
                            updated_price.setText(totalprice);
                    }else if (counter < stock_quantity) {
                        counter++;
                        qty_value.setText(String.valueOf(counter));
                        qty = qty_value.getText().toString();
                        prices = Double.parseDouble(price) * Double.parseDouble(qty);
                        totalprice = String.valueOf(two.format(prices));
                        updated_price.setText(totalprice);
                    }
                    else if (sizes.isEmpty()) {
                        counter++;
                        qty_value.setText(String.valueOf(counter));
                        qty = qty_value.getText().toString();
                        prices = Double.parseDouble(price) * Double.parseDouble(qty);
                        totalprice = String.valueOf(two.format(prices));
                        updated_price.setText(totalprice);
                    }else if(sizes.length() < 2 || sizes.isEmpty() || sizes.equals("[]")){
                        counter++;
                        qty_value.setText(String.valueOf(counter));
                        qty = qty_value.getText().toString();
                        prices = Double.parseDouble(price) * Double.parseDouble(qty);
                        totalprice = String.valueOf(two.format(prices));
                        updated_price.setText(totalprice);
                    }
                    else if(prename.equals("Team Wear")){
                        counter++;
                        qty_value.setText(String.valueOf(counter));
                        qty = qty_value.getText().toString();
                        prices = Double.parseDouble(price) * Double.parseDouble(qty);
                            totalprice = String.valueOf(two.format(prices));
                            updated_price.setText(totalprice);

                    }
                }catch (NumberFormatException  | NullPointerException e){
                    e.printStackTrace();
                }
            }
        });
        subtraction=(ImageView)findViewById(R.id.subtraction);
        subtraction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (counter > 1) {
                        counter--;
                        qty_value.setText(String.valueOf(counter));
                        qty = qty_value.getText().toString();
                        prices = Double.parseDouble(price) * Double.parseDouble(qty);
                            totalprice = String.valueOf(two.format(prices));
                            updated_price.setText(totalprice);
                    }
                }catch (NumberFormatException e){
                    e.printStackTrace();
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
        if(session.equals("CategoryListActivity")) {
            Intent intent = new Intent(ProductDetail.this, CategoryListActivity.class);
            intent.putExtra("slug",slug);
            intent.putExtra("name",prename);
            intent.putExtra("id",id);
            startActivity(intent);
            ProductDetail.this.finish();
        }else if(session.equals("Homescreen")) {
            Intent intent = new Intent(ProductDetail.this, Homescreen.class);
            startActivity(intent);
            ProductDetail.this.finish();
        }
    }
    //--------------------------------------Active Product List ----------------------------
    private class Product_Details_Data extends AsyncTask<String, String, Boolean> {
        JSONParse jsonParser = new JSONParse();
        @Override
        protected Boolean doInBackground(String... para) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                String json = jsonParser.makeHttpRequest(PRODUCT_DETAILS_URL+num, "POST", params);

                try {
                    JSONObject c = new JSONObject(json);
                    JSONObject obj = c.getJSONObject("product");
                         pricess = obj.getString("price");
                         titles = Html.fromHtml(obj.getString("title")).toString();
                         description = Html.fromHtml(obj.getString("description")).toString();

                    weight=obj.getString("weight");
                    JSONArray corporate = obj.getJSONArray("images");
                        for (int j = 0; j < corporate.length(); j++) {
                            JSONObject corporateobject = corporate.getJSONObject(j);
                            img_url = corporateobject.getString("src");
                            showImages.add(img_url);
                        }
                     JSONArray relatedids=obj.getJSONArray("related_ids");
                    for(int k=0; k < relatedids.length(); k++){
                        int rids = relatedids.getInt(k);
                        showRelatedIds.add(rids);
                    }
                    JSONArray attributes=obj.getJSONArray("attributes");
                    if(attributes != null && attributes.length() > 0) {
                        for (int l = 0; l < attributes.length(); l++) {
                            JSONObject attributeobj = attributes.getJSONObject(l);
                            JSONArray options = attributeobj.getJSONArray("options");
                            for (int m = 0; m < options.length(); m++) {
                                if (options.getString(m).length() > 10 && !options.getString(m).isEmpty()) {
                                    upc = options.getString(m);
                                }
                            }
                        }
                       }
                     if(obj.getInt("stock_quantity")> 0) {
                         stock_quantity = obj.getInt("stock_quantity");
                         Log.d("stock_quantity",String.valueOf(stock_quantity));
                      }
                }catch (JSONException e){
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
            try {
                product_title.setText(titles);
                product_details.setText(description);
                updated_price.setText(pricess);

                if(!weight.isEmpty() && !weight.equals("null")) {
                    product_weight.setText(weight);
                    product_weight.setVisibility(View.VISIBLE);
                    product_weight_text.setVisibility(View.VISIBLE);
                }
                if(!upc.isEmpty()&& !upc.equals("null")) {
                    product_upc.setText(upc);
                    product_upc.setVisibility(View.VISIBLE);
                    product_upc_text.setVisibility(View.VISIBLE);
                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }
            try {
                if(showImages.get(0)!=null) {
                    Glide.with(getApplicationContext())
                            .load(showImages.get(0))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .thumbnail(0.5f)
                            .crossFade()
                            .skipMemoryCache(true)
                            .animate(R.anim.bounce)
                            .into(select_image3);
                    select_image3.setVisibility(View.VISIBLE);
                    img_a3.setVisibility(View.VISIBLE);
                }else if(showImages.get(0)==null) {
                   select_image3.setVisibility(View.GONE);
                    img_a3.setVisibility(View.GONE);
                }
                if(showImages.get(1)!=null) {
                    Glide.with(getApplicationContext())
                            .load(showImages.get(1))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .thumbnail(0.5f)
                            .crossFade()
                            .skipMemoryCache(true)
                            .animate(R.anim.bounce)
                            .into(select_image2);
                    select_image2.setVisibility(View.VISIBLE);
                    img_a2.setVisibility(View.VISIBLE);
                }else if(showImages.get(1)==null) {
                    select_image2.setVisibility(View.GONE);
                    img_a2.setVisibility(View.GONE);
                }
                if(showImages.get(2)!=null) {
                Glide.with(getApplicationContext())
                        .load(showImages.get(2))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .thumbnail(0.5f)
                        .crossFade()
                        .skipMemoryCache(true)
                        .animate(R.anim.bounce)
                        .into(select_image1);
                    select_image1.setVisibility(View.VISIBLE);
                    img_a1.setVisibility(View.VISIBLE);
                }else if(showImages.get(2)==null) {
                    select_image1.setVisibility(View.GONE);
                    img_a1.setVisibility(View.GONE);
                }
                if(showImages.get(3)!=null) {
                    Glide.with(getApplicationContext())
                            .load(showImages.get(3))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .thumbnail(0.5f)
                            .crossFade()
                            .skipMemoryCache(true)
                            .animate(R.anim.bounce)
                            .into(select_image4);
                    select_image4.setVisibility(View.VISIBLE);
                    img_a4.setVisibility(View.VISIBLE);
                }else if(showImages.get(3)==null) {
                    select_image4.setVisibility(View.GONE);
                    img_a4.setVisibility(View.GONE);
                }
            }catch (IndexOutOfBoundsException e){
                e.printStackTrace();
            }
            progress_dailog.setVisibility(View.GONE);
            progress_dailog2.setVisibility(View.VISIBLE);
            try {
                new Product_Size_Tracker().execute();
            }catch (IndexOutOfBoundsException e){
                e.printStackTrace();
            }
        }
    }
    //----------------------------------------------Size Tracker-----------------------------------------
    private class Product_Size_Tracker extends AsyncTask<String, String, Boolean> {
        JSONParse jsonParser = new JSONParse();
        @Override
        protected Boolean doInBackground(String... para) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                String json = jsonParser.makeHttpRequest(PRODUCT_SIZE_URL+num, "POST", params);
                try {
                    JSONObject c = new JSONObject(json);
                    JSONArray arrayobj = c.getJSONArray("product");

                           for (int i = 0; i < arrayobj.length(); i++) {

                               JSONObject obj = arrayobj.getJSONObject(i);
                               if (!obj.getString("id").isEmpty()) {
                                   String id = obj.getString("id");
                                   idlist.add(id);
                               }
                               if (!obj.getString("stock_quantity").isEmpty()) {
                                   stock = obj.getString("stock_quantity");
                                   stocklist.add(stock);
                               }
                               JSONArray attributes = obj.getJSONArray("attributes");
                               if (attributes != null && attributes.length() > 0) {
                                   for (int l = 0; l < attributes.length(); l++) {
                                       JSONObject attributeobj = attributes.getJSONObject(l);
                                       String optionsvalue = attributeobj.getString("option");
                                       SizeLoading sizeLoading = new SizeLoading();
                                       sizeLoading.setPro_size(optionsvalue);
                                       CustomListViewValuesArr.add(sizeLoading);
                                       sizelist.add(optionsvalue);
                                   }
                           }
                       }
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
            adapter.notifyDataSetChanged();
            product_details_show.setVisibility(View.VISIBLE);
            if (sizelist.size() >= 0) {
                product_sizelist.setText(String.valueOf(sizelist));
                product_size_text.setVisibility(View.VISIBLE);
                product_sizelist.setVisibility(View.VISIBLE);
            }
            try{
                if(stocklist == null){
                    instockvalue2.setVisibility(View.VISIBLE);
                    chargesvalue.setVisibility(View.GONE);
                    instockvalue.setVisibility(View.GONE);
                    instockvalue.setText(String.valueOf(stock_quantity));
                    instockvalue2.setText(String.valueOf(stock_quantity)+ " in stock");
                }else if(stocklist.isEmpty()) {
                    instockvalue2.setVisibility(View.VISIBLE);
                    chargesvalue.setVisibility(View.GONE);
                    instockvalue.setVisibility(View.GONE);
                    instockvalue.setText(String.valueOf(stock_quantity));
                    instockvalue2.setText(String.valueOf(stock_quantity)+ " in stock");
                }else if(stocklist.size() <= 0){
                    instockvalue2.setVisibility(View.VISIBLE);
                    chargesvalue.setVisibility(View.GONE);
                    instockvalue.setVisibility(View.GONE);
                    instockvalue.setText(String.valueOf(stock_quantity));
                    instockvalue2.setText(String.valueOf(stock_quantity)+ " in stock");
                }else if(!stocklist.get(0).isEmpty()){
                    chargesvalue.setVisibility(View.VISIBLE);
                    instockvalue2.setVisibility(View.GONE);
                }
            }catch (IndexOutOfBoundsException e){
                e.printStackTrace();
            }
            progress_dailog2.setVisibility(View.GONE);
            try{
                if(showRelatedIds.get(0)!= null) {
                    new Vartiant_Details_Data1().execute();
                }
            }catch (IndexOutOfBoundsException e){
                e.printStackTrace();
            }
        }
    }
    //-------------------------------------------Variant 1---------------------------------------------------
    private class Vartiant_Details_Data1 extends AsyncTask<String, String, Boolean> {
        JSONParse jsonParser = new JSONParse();
        @Override
        protected Boolean doInBackground(String... para) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                String json = jsonParser.makeHttpRequest(PRODUCT_DETAILS_URL+showRelatedIds.get(0), "POST", params);
                try {
                    JSONObject c = new JSONObject(json);
                    JSONObject obj = c.getJSONObject("product");
                    price1 = obj.getString("price");
                    name1 = Html.fromHtml(obj.getString("title")).toString();
                    img1 = obj.getString("featured_src");
                    desc1= Html.fromHtml(obj.getString("description")).toString();
                }catch (JSONException e){
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
            related_product_name1.setText(name1);
            get_related_product_price1.setText(price1);
            get_related_product_image1.setText(img1);
            get_related_product_desc1.setText(desc1);
            try {
                    Glide.with(getApplicationContext())
                            .load(img1)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .thumbnail(0.5f)
                            .crossFade()
                            .skipMemoryCache(true)
                            .animate(R.anim.bounce)
                            .into(related_image1);
            }catch (IndexOutOfBoundsException e){
                e.printStackTrace();
            }
            related_card1.setVisibility(View.VISIBLE);
            try {
                if (showRelatedIds.get(1) != null) {
                    new Vartiant_Details_Data2().execute();
                }
            }catch (IndexOutOfBoundsException e){
                e.printStackTrace();
            }
        }
    }
    //------------------------------------------------Variant 2----------------------------------------------
    private class Vartiant_Details_Data2 extends AsyncTask<String, String, Boolean> {
        JSONParse jsonParser = new JSONParse();
        @Override
        protected Boolean doInBackground(String... para) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                String json = jsonParser.makeHttpRequest(PRODUCT_DETAILS_URL+showRelatedIds.get(1), "POST", params);
                try {
                    JSONObject c = new JSONObject(json);
                    JSONObject obj = c.getJSONObject("product");
                    price2 = obj.getString("price");
                    name2 = Html.fromHtml(obj.getString("title")).toString();
                    img2 = obj.getString("featured_src");
                    desc2= Html.fromHtml(obj.getString("description")).toString();
                }catch (JSONException e){
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
            related_product_name2.setText(name2);
            get_related_product_price2.setText(price2);
            get_related_product_image2.setText(img2);
            get_related_product_desc2.setText(desc2);
            try {
                    Glide.with(getApplicationContext())
                            .load(img2)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .thumbnail(0.5f)
                            .crossFade()
                            .skipMemoryCache(true)
                            .animate(R.anim.bounce)
                            .into(related_image2);

            }catch (IndexOutOfBoundsException e){
                e.printStackTrace();
            }
            related_card2.setVisibility(View.VISIBLE);
            try{
            if(showRelatedIds.get(2)!= null) {
                new Vartiant_Details_Data3().execute();
            }
        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }
        }
    }
  //--------------------------------------------------Variant 3--------------------------------------------
    private class Vartiant_Details_Data3 extends AsyncTask<String, String, Boolean> {
        JSONParse jsonParser = new JSONParse();
        @Override
        protected Boolean doInBackground(String... para) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                String json = jsonParser.makeHttpRequest(PRODUCT_DETAILS_URL+showRelatedIds.get(2), "POST", params);
                try {
                    JSONObject c = new JSONObject(json);
                    JSONObject obj = c.getJSONObject("product");
                    price3 = obj.getString("price");
                    name3 = Html.fromHtml(obj.getString("title")).toString();
                    img3 = obj.getString("featured_src");
                    desc3= Html.fromHtml(obj.getString("description")).toString();
                }catch (JSONException e){
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
            related_product_name3.setText(name3);
            get_related_product_price3.setText(price3);
            get_related_product_image3.setText(img3);
            get_related_product_desc3.setText(desc3);
            try {
                Glide.with(getApplicationContext())
                        .load(img3)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .thumbnail(0.5f)
                        .crossFade()
                        .skipMemoryCache(true)
                        .animate(R.anim.bounce)
                        .into(related_image3);
            }catch (IndexOutOfBoundsException e){
                e.printStackTrace();
            }
            related_card3.setVisibility(View.VISIBLE);
            try{
            if(showRelatedIds.get(3)!= null) {
                new Vartiant_Details_Data4().execute();
            }
        }catch (IndexOutOfBoundsException e){
          e.printStackTrace();
      }
        }
    }
    //----------------------------------------------Variant 4------------------------------------------------
    private class Vartiant_Details_Data4 extends AsyncTask<String, String, Boolean> {
        JSONParse jsonParser = new JSONParse();
        @Override
        protected Boolean doInBackground(String... para) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                String json = jsonParser.makeHttpRequest(PRODUCT_DETAILS_URL+showRelatedIds.get(3), "POST", params);
                try {
                    JSONObject c = new JSONObject(json);
                    JSONObject obj = c.getJSONObject("product");
                    price4 = obj.getString("price");
                    name4 = Html.fromHtml(obj.getString("title")).toString();
                    img4 = obj.getString("featured_src");
                    desc4= Html.fromHtml(obj.getString("description")).toString();
                }catch (JSONException e){
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
            related_product_name4.setText(name4);
            get_related_product_price4.setText(price4);
            get_related_product_image4.setText(img4);
            get_related_product_desc4.setText(desc4);
            try {
                Glide.with(getApplicationContext())
                        .load(img4)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .thumbnail(0.5f)
                        .crossFade()
                        .skipMemoryCache(true)
                        .animate(R.anim.bounce)
                        .into(related_image4);
            }catch (IndexOutOfBoundsException e){
                e.printStackTrace();
            }
            related_card4.setVisibility(View.VISIBLE);
            try{
            if(showRelatedIds.get(4)!= null) {
                new Vartiant_Details_Data5().execute();
            }
        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }
        }
    }
    //----------------------------------------------Variant 5------------------------------------------------
    private class Vartiant_Details_Data5 extends AsyncTask<String, String, Boolean> {
        JSONParse jsonParser = new JSONParse();
        @Override
        protected Boolean doInBackground(String... para) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                String json = jsonParser.makeHttpRequest(PRODUCT_DETAILS_URL+showRelatedIds.get(4), "POST", params);
                try {
                    JSONObject c = new JSONObject(json);
                    JSONObject obj = c.getJSONObject("product");
                    price5 = obj.getString("price");
                    name5 = Html.fromHtml(obj.getString("title")).toString();
                    img5 = obj.getString("featured_src");
                    desc5= Html.fromHtml(obj.getString("description")).toString();
                }catch (JSONException e){
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
            try {
                if (!name5.isEmpty() && !price5.isEmpty() && !img5.isEmpty()) {
                    related_product_name5.setText(name5);
                    get_related_product_price5.setText(price5);
                    get_related_product_image5.setText(img5);
                    get_related_product_desc5.setText(desc5);
                    try {
                        Glide.with(getApplicationContext())
                                .load(img5)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .thumbnail(0.5f)
                                .crossFade()
                                .skipMemoryCache(true)
                                .animate(R.anim.bounce)
                                .into(related_image5);
                    } catch (IndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                    related_card5.setVisibility(View.VISIBLE);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }
    //----------------------------------------------Add TO Cart------------------------------------------------
    private class Add_To_Cart extends AsyncTask<String, String, Boolean> {
        JSONParse jsonParser = new JSONParse();
        @Override
        protected Boolean doInBackground(String... para) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                String json = jsonParser.makeHttpRequest(ADD_TO_CART+getIMIE+"&product_id="+num+"&user_id="+getuid+"&size="+size+"&quantity="+qty, "POST", params);
                try {
                    JSONObject c = new JSONObject(json);
                    message = c.getString("message");
                    status=c.getString("status");

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

        }
    }
    //----------------------------------------------LogIn Cart Count------------------------------------------------
    private class Login_Cart_Count extends AsyncTask<String, String, Boolean> {
        JSONParse jsonParser = new JSONParse();
        @Override
        protected Boolean doInBackground(String... para) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                String json = jsonParser.makeHttpRequest(CART_COUNT_USER_LOGIN_URL+getuid, "POST", params);

                try {
                    JSONObject c = new JSONObject(json);
                    cart_counts=c.getString("cart_count");

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
            try {
                cart_item.setText(cart_counts);
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }
    }
    //---------------------------------------------- Logoff Cart Count------------------------------------------------
    private class Logoff_Cart_Count extends AsyncTask<String, String, Boolean> {
        JSONParse jsonParser = new JSONParse();
        @Override
        protected Boolean doInBackground(String... para) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                String json = jsonParser.makeHttpRequest(CART_COUNT_USER_LOGOFF_URL+getIMIE, "POST", params);
                try {
                    JSONObject c = new JSONObject(json);
                    cart_counts=c.getString("cart_count");
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
            try {
                cart_item.setText(cart_counts);
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }
    }
}
