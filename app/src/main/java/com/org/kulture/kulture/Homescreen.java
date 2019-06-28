package com.org.kulture.kulture;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.firebase.messaging.FirebaseMessaging;
import com.org.kulture.kulture.gcm.GCMRegistrationIntentService;
import com.org.kulture.kulture.model.Config;
import com.org.kulture.kulture.model.ConnectionDetector;
import com.org.kulture.kulture.model.ExpandableHeightGridView;
import com.org.kulture.kulture.model.JSONParse;
import com.org.kulture.kulture.model.MyCustomPagerAdapter;
import com.org.kulture.kulture.model.NotificationUtils;
import com.org.kulture.kulture.model.Product_GridView_ImageAdapter;
import com.org.kulture.kulture.model.Product_Grid_Model;
import com.org.kulture.kulture.model.Terms_of_Sale;
import com.org.kulture.kulture.search.SearchAdapter;
import com.org.kulture.kulture.search.SharedPreference;
import com.org.kulture.kulture.search.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;

import static android.Manifest.permission.CALL_PHONE;

public class Homescreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int PERMISSION_REQUEST_CODE = 200;
    CoordinatorLayout app_coordinatorlayout;
    String getsearchtext,qty;
    Integer feature_ids;

    ArrayList<String> categoryname= new ArrayList<>();
    //Search list
    private ArrayList<String> mCategory;
    Toolbar toolbar;
    ViewPager viewPager;
    private LinearLayout dotsLayout;
    MyCustomPagerAdapter myCustomPagerAdapter;
    private TextView[] dots;
   // private int[] layouts;
    int check=1;
    ProgressBar progressBar;
    ImageView check_notifications,home_cart;
    Animation shake;
    TextView cart_count;
    Toast toast;
    CoordinatorLayout coordinatorlayout;
    Boolean handle_setting=false;
    /****************************************************************/
    private Boolean isInternetPresent = false;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private ConnectionDetector cd;
    private String token,cart_counts;
    LinearLayout feature_visible,show_feature;

   //$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
    private static final String TAG = Homescreen.class.getSimpleName();
    String BANNER_URL= "http://www.kulture.biz/webservice/get_slider_images.php";

    String CATEGORY_URL= "http://kulture.biz/webservice/category.php";
    String FEATURE_URL="http://kulture.biz/webservice/get_all_featured_product.php";
    String PRODUCT_URL="http://kulture.biz/webservice/product_details_by_id.php?product_id=";

    String CART_COUNT_USER_LOGIN_URL="http://kulture.biz/webservice/count_cart_via_user_id.php?user_id=";
    String CART_COUNT_USER_LOGOFF_URL="http://kulture.biz/webservice/count_cart_via_order_id.php?order_id=";

    String id,name,slug,image,count,category;
    ExpandableHeightGridView display_category;
    private Product_GridView_ImageAdapter product_gridView_imageAdapter;
    private ArrayList<Product_Grid_Model> mGridData;
    CardView img_ex1,img_ex2,img_ex3;
    ArrayList<Integer> collectfetureids=new ArrayList<Integer>();

    //***************************************************************************************//
    CardView new_arrival_product1,new_arrival_product2,
            new_arrival_product3,new_arrival_product4,new_arrival_product5;
    ImageView img_feature1,img_feature2,img_feature3,img_feature4,img_feature5;

    TextView feature_desc1,feature_desc2,feature_desc3,feature_desc4,feature_desc5;
    String desc1,desc2,desc3,desc4,desc5;
    ProgressBar new_arrival_progress;
    TextView gettitle1,gettitle2,gettitle3,gettitle4,gettitle5;
    TextView getprice1,getprice2,getprice3,getprice4,getprice5;
    String img1,name1,price1,img2,name2,price2,img3,name3,price3,img4,name4,price4,img5,name5,price5;

    SharedPreferences preferences,preferences2,preferences3;
    String getid,getids,getIMIE;
    ArrayList<String> bannerimage= new ArrayList<>();
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);

        toolBarData();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);

        coordinatorlayout=(CoordinatorLayout)findViewById(R.id.home_main);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        preferences2=getApplicationContext().getSharedPreferences("register_save", Context.MODE_PRIVATE);
        getids=preferences2.getString("adminid","");

        preferences3=getApplicationContext().getSharedPreferences("mobimei_save", Context.MODE_PRIVATE);
        getIMIE=preferences3.getString("imei","");

        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        feature_visible=(LinearLayout)findViewById(R.id.feature_visible);

        show_feature=(LinearLayout)findViewById(R.id.show_feature);
        display_category=(ExpandableHeightGridView)findViewById(R.id.display_category);
        display_category.setExpanded(true);

        if (isInternetPresent) {
            mGridData = new ArrayList<Product_Grid_Model>();
            product_gridView_imageAdapter = new Product_GridView_ImageAdapter(this, R.layout.product_list, mGridData);
            display_category.setAdapter(product_gridView_imageAdapter);

            //getting the progressbar
            progressBar = (ProgressBar) findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);

            display_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    Product_Grid_Model item = (Product_Grid_Model) parent.getItemAtPosition(position);

                    if(item.getCat_name().equals("Corporate Wear")){
                        Intent intent = new Intent(Homescreen.this, Corporate_Wear.class);
                        intent.putExtra("id", item.getCat_id());
                        intent.putExtra("slug", item.getCat_slug());
                        intent.putExtra("name",item.getCat_name());
                        startActivity(intent);
                        Homescreen.this.finish();
                    }else if(item.getCat_name().equals("Sports Wear")) {
                        Intent intent = new Intent(Homescreen.this, SportItemsList.class);
                        intent.putExtra("id", item.getCat_id());
                        intent.putExtra("slug", item.getCat_slug());
                        intent.putExtra("name", item.getCat_name());
                        startActivity(intent);
                        Homescreen.this.finish();
                    }else {
                        Intent intent = new Intent(Homescreen.this, CategoryListActivity.class);
                        intent.putExtra("id", item.getCat_id());
                        intent.putExtra("slug", item.getCat_slug());
                        intent.putExtra("prename", item.getCat_name());
                        startActivity(intent);
                        Homescreen.this.finish();
                    }
                }
            });
        }else{
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
        home_cart=(ImageView)findViewById(R.id.home_prescription_cart);
        home_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Homescreen.this,CartActivity.class);
                startActivity(intent);
                Homescreen.this.finish();
            }
        });
        img_ex1=(CardView)findViewById(R.id.img_ex1);
        img_ex1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homescreen.this, CategoryListActivity.class);
                intent.putExtra("id", "54");
                intent.putExtra("slug", "kids");
                intent.putExtra("prename", "Kids");
                startActivity(intent);
                Homescreen.this.finish();
            }
        });
        img_ex2=(CardView)findViewById(R.id.img_ex2);
        img_ex2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homescreen.this, DesignYourOwn.class);
                startActivity(intent);
                Homescreen.this.finish();
            }
        });

        img_ex3=(CardView)findViewById(R.id.img_ex3);
        img_ex3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homescreen.this, Corporate_Wear.class);
                intent.putExtra("id", "69");
                intent.putExtra("slug", "corporate-wear");
                intent.putExtra("name", "Corporate Wear");
                startActivity(intent);
                Homescreen.this.finish();
            }
        });
        feature_desc1=(TextView)findViewById(R.id.feature_desc1);
        feature_desc2=(TextView)findViewById(R.id.feature_desc2);
        feature_desc3=(TextView)findViewById(R.id.feature_desc3);
        feature_desc4=(TextView)findViewById(R.id.feature_desc4);
        feature_desc5=(TextView)findViewById(R.id.feature_desc5);


        new_arrival_product1=(CardView)findViewById(R.id.new_arrival_product1);
        new_arrival_product1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homescreen.this, ProductDetail.class);
                intent.putExtra("id", String.valueOf(collectfetureids.get(0)));
                intent.putExtra("desc",feature_desc1.getText().toString());
                intent.putExtra("title", name1);
                intent.putExtra("price",price1);
                intent.putExtra("image",img1);
                intent.putExtra("session","Homescreen");
                startActivity(intent);
                Homescreen.this.finish();
            }
        });
        new_arrival_product2=(CardView)findViewById(R.id.new_arrival_product2);
        new_arrival_product2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homescreen.this, ProductDetail.class);
                intent.putExtra("id", String.valueOf(collectfetureids.get(1)));
                intent.putExtra("desc",feature_desc2.getText().toString() );
                intent.putExtra("title",name2);
                intent.putExtra("price",price2);
                intent.putExtra("image",img2);
                intent.putExtra("session","Homescreen");
                startActivity(intent);
                Homescreen.this.finish();
            }
        });
        new_arrival_product3=(CardView)findViewById(R.id.new_arrival_product3);
        new_arrival_product3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homescreen.this, ProductDetail.class);
                intent.putExtra("id", String.valueOf(collectfetureids.get(2)));
                intent.putExtra("desc",feature_desc3.getText().toString());
                intent.putExtra("title",name3);
                intent.putExtra("price",price3);
                intent.putExtra("image",img3);
                intent.putExtra("session","Homescreen");
                startActivity(intent);
                Homescreen.this.finish();
            }
        });
        new_arrival_product4=(CardView)findViewById(R.id.new_arrival_product4);
        new_arrival_product4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homescreen.this, ProductDetail.class);
                intent.putExtra("id", String.valueOf(collectfetureids.get(3)));
                intent.putExtra("desc",feature_desc4.getText().toString());
                intent.putExtra("title",name4);
                intent.putExtra("price",price4);
                intent.putExtra("image",img4);
                intent.putExtra("session","Homescreen");
                startActivity(intent);
                Homescreen.this.finish();
            }
        });
        new_arrival_product5=(CardView)findViewById(R.id.new_arrival_product5);
        new_arrival_product5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homescreen.this, ProductDetail.class);
                intent.putExtra("id",String.valueOf(collectfetureids.get(4)));
                intent.putExtra("desc",feature_desc5.getText().toString());
                intent.putExtra("title",name5);
                intent.putExtra("price",price5);
                intent.putExtra("image",img5);
                intent.putExtra("session","Homescreen");
                startActivity(intent);
                Homescreen.this.finish();
            }
        });
        img_feature1=(ImageView)findViewById(R.id.img_feature1);
        img_feature2=(ImageView)findViewById(R.id.img_feature2);
        img_feature3=(ImageView)findViewById(R.id.img_feature3);
        img_feature4=(ImageView)findViewById(R.id.img_feature4);
        img_feature5=(ImageView)findViewById(R.id.img_feature5);

        gettitle1=(TextView)findViewById(R.id.gettitle1);
        gettitle2=(TextView)findViewById(R.id.gettitle2);
        gettitle3=(TextView)findViewById(R.id.gettitle3);
        gettitle4=(TextView)findViewById(R.id.gettitle4);
        gettitle5=(TextView)findViewById(R.id.gettitle5);
        getprice1=(TextView)findViewById(R.id.getprice1);
        getprice2=(TextView)findViewById(R.id.getprice2);
        getprice3=(TextView)findViewById(R.id.getprice3);
        getprice4=(TextView)findViewById(R.id.getprice4);
        getprice5=(TextView)findViewById(R.id.getprice5);


        new_arrival_progress=(ProgressBar)findViewById(R.id.new_arrival_progress);

        check_notifications=(ImageView)findViewById(R.id.check_notifications);
        check_notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_notifications.setBackgroundResource(R.drawable.ic_turn_notifications_on_button);
                shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                check_notifications.startAnimation(shake);
            }
        });
        cart_count=(TextView)findViewById(R.id.cart_counts);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // making notification bar transparent
        changeStatusBarColor();
        try{
            new Banner_images_call().execute();
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // changing the next button text 'NEXT' / 'GOT IT'
                if (position == bannerimage.size() - 1) {
                    // last page. make button text to GOT IT
                    addBottomDots(position);
                } else {
                    addBottomDots(position);
                }
            }
            @Override
            public void onPageSelected(int position) {

            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        // adding bottom dots
        addBottomDots(0);

        if(!getids.isEmpty()&& !getIMIE.isEmpty()) {
            try {
                new Login_Cart_Count().execute();
            }catch (Exception e){
                e.printStackTrace();
            }
        }else if(getids.isEmpty()&& !getIMIE.isEmpty()){
            try {
                new Logoff_Cart_Count().execute();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        //Initializing our broadcast receiver
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            //When the broadcast received
            //We are sending the broadcast from GCMRegistrationIntentService
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    token = intent.getStringExtra("token");
                    Log.d("Token", token);
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                    displayFirebaseRegId();
                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                        // new push notification is received
                        String message = intent.getStringExtra("message");
                        Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                } else if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_ERROR)) {
                    Toast.makeText(getApplicationContext(), "GCM registration error!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_SHORT).show();
                }
            }
        };

        displayFirebaseRegId();
        //Checking play service is available or not
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

        //if play service is not available
        if(ConnectionResult.SUCCESS != resultCode) {
            //If play service is supported but not installed
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                //Displaying message that play service is not installed
                Toast.makeText(getApplicationContext(), "Google Play Service is not install/enabled in this device!", Toast.LENGTH_LONG).show();
                GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());
                //If play service is not supported
                //Displaying an error message
            } else {
                Toast.makeText(getApplicationContext(), "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
            }
            //If play service is available
        } else {
            //Starting intent to register device
            Intent itent = new Intent(this, GCMRegistrationIntentService.class);
            startService(itent);
        }
    }
    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", token);

        Log.e(TAG, "Firebase reg id: " + regId);
    }
    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START) || check == 1) {
            drawer.closeDrawer(GravityCompat.START);
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
            check++;
        } else if (check == 2) {
            Homescreen.this.finish();
        }
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view banner_item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_account) {
            preferences=getApplicationContext().getSharedPreferences("register_save", Context.MODE_PRIVATE);
            getid=preferences.getString("adminid","");

            if(!getid.isEmpty()) {
                Intent intent = new Intent(Homescreen.this, MyAccount.class);
                startActivity(intent);
                Homescreen.this.finish();
            }else if(getid.isEmpty()){
                Intent intent = new Intent(Homescreen.this, Loginscreen.class);
                startActivity(intent);
                Homescreen.this.finish();
            }
        } else if (id == R.id.nav_orders) {

            preferences=getApplicationContext().getSharedPreferences("register_save", Context.MODE_PRIVATE);
            getid=preferences.getString("adminid","");

            if(!getid.isEmpty()) {
                Intent intent=new Intent(Homescreen.this,Order_Details.class);
                startActivity(intent);
                Homescreen.this.finish();
            }else if(getid.isEmpty()){
                Intent intent = new Intent(Homescreen.this, Loginscreen.class);
                startActivity(intent);
                Homescreen.this.finish();
            }

        }else if(id == R.id.nav_about){
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            Terms_of_Sale frag = Terms_of_Sale.addSomeString("ABOUT US");
            frag.show(ft, "txn_tag");
        }
        else if (id == R.id.rate_us) {
            Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
            }

        } else if (id == R.id.nav_share) {
            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Kulture");
                String sAux = "\nLet me recommend you our web site." +
                        "Our fashion wear and indigenous clothing comprises of daily wear, " +
                        "sports wear, corporate wear, fashion wear for all ages and for both boys and girls. " +
                        "We are the top traditional fashion clothing company in Australia, " +
                        "selling beautiful Aboriginal designer wear for men & women.\n\n";
                sAux = sAux + "http://www.kulture.biz/ \n\n";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "choose one"));
            } catch (Exception e) {
                //e.toString();
            }
        } else if (id == R.id.nav_call) {
            if (!checkPermission()) {
                requestPermission();
            }
            String mobileNo = "0428 000 078";
            String uri = "tel:" + mobileNo.trim();
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse(uri));
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                        int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
            }
            try {
                startActivity(intent);
            }catch (SecurityException e){
                e.printStackTrace();
            }
        }else if (id == R.id.nav_email) {

            final Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("text/plain");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"admin@kulture.biz"});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Kulture App Testing");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Android App Test Message");

            emailIntent.setType("message/rfc822");
            try {
                startActivity(Intent.createChooser(emailIntent,
                        "Send email using..."));
            } catch (ActivityNotFoundException ex) {
                Toast.makeText(getApplicationContext(), "No email clients installed.", Toast.LENGTH_SHORT).show();
            }
        }else if(id == R.id.nav_return){
            Intent intent = new Intent(Homescreen.this, Help_Center.class);
            startActivity(intent);
            Homescreen.this.finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), CALL_PHONE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{CALL_PHONE}, PERMISSION_REQUEST_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted){
                        String mobileNo = "0428 000 078";
                        String uri = "tel:" + mobileNo.trim();
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse(uri));
                        if (ActivityCompat.checkSelfPermission(Homescreen.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                        }
                        try {
                            startActivity(intent);
                        }catch (SecurityException e){
                            e.printStackTrace();
                        }
                    }
                    else {
                        Snackbar.make(app_coordinatorlayout, "Permission denied for phone call.", Snackbar.LENGTH_SHORT).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(CALL_PHONE)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{CALL_PHONE},
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
        new AlertDialog.Builder(Homescreen.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    private void toolBarData() {
        toolbar = (Toolbar) findViewById(R.id.search_toolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadToolBarSearch();
            }
        });
        toolbar.setTitle("Search");
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        setSupportActionBar(toolbar);
    }
    public void loadToolBarSearch() {
        ArrayList<String> categoryStored = SharedPreference.loadList(Homescreen.this, Utils.PREFS_NAME, Utils.KEY_COUNTRIES);

        View view = Homescreen.this.getLayoutInflater().inflate(R.layout.view_toolbar_search, null);
        LinearLayout parentToolbarSearch = (LinearLayout) view.findViewById(R.id.parent_toolbar_search);
        ImageView imgToolBack = (ImageView) view.findViewById(R.id.img_tool_back);
        final EditText edtToolSearch = (EditText) view.findViewById(R.id.edt_tool_search);
        ImageView imgToolMic = (ImageView) view.findViewById(R.id.img_tool_mic);
        final ListView listSearch = (ListView) view.findViewById(R.id.list_search);
        final TextView txtEmpty = (TextView) view.findViewById(R.id.txt_empty);

        Utils.setListViewHeightBasedOnChildren(listSearch);
        edtToolSearch.setHint("Search for Products");
        final Dialog toolbarSearchDialog = new Dialog(Homescreen.this, R.style.MaterialSearch);
        toolbarSearchDialog.setContentView(view);
        toolbarSearchDialog.setCancelable(false);
        toolbarSearchDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        toolbarSearchDialog.getWindow().setGravity(Gravity.BOTTOM);
        toolbarSearchDialog.show();

        toolbarSearchDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        categoryStored = (categoryStored != null && categoryStored.size() > 0) ? categoryStored : new ArrayList<String>();
        final SearchAdapter searchAdapter = new SearchAdapter(Homescreen.this, categoryStored, false);

        listSearch.setVisibility(View.VISIBLE);
        listSearch.setAdapter(searchAdapter);
        listSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                category = String.valueOf(adapterView.getItemAtPosition(position));
                SharedPreference.addList(Homescreen.this, Utils.PREFS_NAME, Utils.KEY_COUNTRIES, category);
                edtToolSearch.setText(category);
                getsearchtext=edtToolSearch.getText().toString();
                listSearch.setVisibility(View.GONE);
                toolbarSearchDialog.dismiss();
                showCustomAlert();
            }
        });
        edtToolSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mCategory = new ArrayList<String>(categoryname);
                listSearch.setVisibility(View.VISIBLE);
                searchAdapter.updateList(mCategory, true);
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<String> filterList = new ArrayList<String>();
                boolean isNodata = false;
                if (s.length() > 0) {
                    for (int i = 0; i < mCategory.size(); i++) {
                        if (mCategory.get(i).toLowerCase().startsWith(s.toString().trim().toLowerCase())) {
                            filterList.add(mCategory.get(i));

                            listSearch.setVisibility(View.VISIBLE);
                            searchAdapter.updateList(filterList, true);
                            isNodata = true;
                        }
                    }
                    if (!isNodata) {
                        listSearch.setVisibility(View.GONE);
                        txtEmpty.setVisibility(View.VISIBLE);
                        txtEmpty.setText("No data found");
                    }
                } else {
                    listSearch.setVisibility(View.GONE);
                    txtEmpty.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        imgToolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toolbarSearchDialog.dismiss();
            }
        });
        imgToolMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtToolSearch.setText("");

            }
        });
    }
    //Registering receiver on activity resume
    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));
        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));
        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }
    //Unregistering receiver on activity paused
    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }
    //--------------------------------------BANNER IMAGES CALL    ----------------------------
    private class Banner_images_call extends AsyncTask<String, String, Boolean> {
        JSONParse jsonParser = new JSONParse();
        @Override
        protected Boolean doInBackground(String... para) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();

                String json = jsonParser.makeHttpRequest(BANNER_URL, "POST", params);
                try {
                    JSONObject c = new JSONObject(json);
                    JSONArray array = c.getJSONArray("slider");

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        String img_url=object.getString("image");
                        //Log.d("banner_image",img_url);
                        bannerimage.add(img_url);
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

            myCustomPagerAdapter = new MyCustomPagerAdapter(Homescreen.this, bannerimage);
            viewPager.setAdapter(myCustomPagerAdapter);
        }
    }

    private void addBottomDots(int currentPage) {
        try {
            dots = new TextView[bannerimage.size()];
            int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
            int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

            dotsLayout.removeAllViews();
            for (int i = 0; i < dots.length; i++) {
                dots[i] = new TextView(this);
                dots[i].setText(Html.fromHtml("&#8226;"));
                dots[i].setTextSize(35);
                dots[i].setTextColor(colorsInactive[currentPage]);
                dotsLayout.addView(dots[i]);
            }
            if (dots.length > 0)
                dots[currentPage].setTextColor(colorsActive[currentPage]);
        }catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
        }
    }
    //----------------------------------------------LogIn Cart Count------------------------------------------------
    private class Login_Cart_Count extends AsyncTask<String, String, Boolean> {
        JSONParse jsonParser = new JSONParse();
        @Override
        protected Boolean doInBackground(String... para) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                String json = jsonParser.makeHttpRequest(CART_COUNT_USER_LOGIN_URL+getids, "POST", params);

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
                cart_count.setText(cart_counts);
            }catch (NullPointerException e){
                e.printStackTrace();
            }
            try {
                new Category_Data().execute();
            }catch (IndexOutOfBoundsException e){
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
                cart_count.setText(cart_counts);
            }catch (NullPointerException e){
                e.printStackTrace();
            }
            try {
                new Category_Data().execute();
            }catch (IndexOutOfBoundsException e){
                e.printStackTrace();
            }
        }
    }
    //--------------------------------------Active Gallery ----------------------------
    private class Category_Data extends AsyncTask<String, String, Boolean> {
        JSONParse jsonParser = new JSONParse();

        @Override
        protected Boolean doInBackground(String... para) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();

                String json = jsonParser.makeHttpRequest(CATEGORY_URL, "POST", params);
                try {
                    JSONObject c = new JSONObject(json);
                    JSONArray array = c.getJSONArray("product_categories");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);

                 if(!object.getString("name").equals("Wholesalers")) {
                     if(!object.getString("name").equals("Kids")) {

                         id = object.getString("id");
                         name = object.getString("name");
                         slug = object.getString("slug");
                         image = object.getString("image");
                         count = object.getString("count");

                         Product_Grid_Model product_grid_model = new Product_Grid_Model();
                         product_grid_model.setCat_id(id);
                         product_grid_model.setCat_name(name);
                         product_grid_model.setCat_slug(slug);
                         product_grid_model.setCat_image(image);
                         product_grid_model.setCat_count(count);
                         mGridData.add(product_grid_model);
                     }
                      }
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
            product_gridView_imageAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
            display_category.setVisibility(View.VISIBLE);
            feature_visible.setVisibility(View.VISIBLE);
            new_arrival_progress.setVisibility(View.VISIBLE);
            try{
            new Feature_Urls_Data().execute();
          }catch (IndexOutOfBoundsException e){
                e.printStackTrace();
            }
        }
    }
    //------------------------------------------------Feature urls----------------------------------------------
    private class Feature_Urls_Data extends AsyncTask<String, String, Boolean> {
        JSONParse jsonParser = new JSONParse();
        @Override
        protected Boolean doInBackground(String... para) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                String json = jsonParser.makeHttpRequest(FEATURE_URL, "POST", params);
                try {
                    JSONObject c = new JSONObject(json);
                    JSONArray arrobj = c.getJSONArray("product");
                    for(int x=0; x < arrobj.length(); x++) {
                        JSONObject object=arrobj.getJSONObject(x);
                        feature_ids = object.getInt("id");
                        collectfetureids.add(feature_ids);
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
            show_feature.setVisibility(View.VISIBLE);
            try{
                if(collectfetureids.get(0)!= null) {
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
                String json = jsonParser.makeHttpRequest(PRODUCT_URL+collectfetureids.get(0), "POST", params);
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
            try {
                if (!name1.isEmpty() && !price1.isEmpty() && !img1.isEmpty() && !desc1.isEmpty()) {
                    gettitle1.setText(name1);
                    getprice1.setText(price1);
                    feature_desc1.setText(desc1);
                    try {
                        Glide.with(getApplicationContext())
                                .load(img1)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .thumbnail(0.5f)
                                .crossFade()
                                .skipMemoryCache(true)
                                .animate(R.anim.bounce)
                                .into(img_feature1);
                    } catch (IndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                    new_arrival_product1.setVisibility(View.VISIBLE);
                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }
            new_arrival_progress.setVisibility(View.GONE);
            try {
                if (collectfetureids.get(1) != null) {
                    new Variant_Details_Data2().execute();
                }
            }catch (IndexOutOfBoundsException e){
                e.printStackTrace();
            }
        }
    }
    //------------------------------------------------Variant 2----------------------------------------------
    private class Variant_Details_Data2 extends AsyncTask<String, String, Boolean> {
        JSONParse jsonParser = new JSONParse();
        @Override
        protected Boolean doInBackground(String... para) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                String json = jsonParser.makeHttpRequest(PRODUCT_URL+collectfetureids.get(1), "POST", params);
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
            try {
                if (!name2.isEmpty() && !price2.isEmpty() && !img2.isEmpty() && !desc2.isEmpty()) {
                    gettitle2.setText(name2);
                    getprice2.setText(price2);
                    feature_desc2.setText(desc2);
                    try {

                        Glide.with(getApplicationContext())
                                .load(img2)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .thumbnail(0.5f)
                                .crossFade()
                                .skipMemoryCache(true)
                                .animate(R.anim.bounce)
                                .into(img_feature2);

                    } catch (IndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                    new_arrival_product2.setVisibility(View.VISIBLE);
                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }
            try{
                if(collectfetureids.get(2)!= null) {
                    new Variant_Details_Data3().execute();
                }
            }catch (IndexOutOfBoundsException e){
                e.printStackTrace();
            }
        }
    }
    //--------------------------------------------------Variant 3--------------------------------------------
    private class Variant_Details_Data3 extends AsyncTask<String, String, Boolean> {
        JSONParse jsonParser = new JSONParse();
        @Override
        protected Boolean doInBackground(String... para) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                String json = jsonParser.makeHttpRequest(PRODUCT_URL+collectfetureids.get(2), "POST", params);
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
            try {
                if (!name3.isEmpty() && !price3.isEmpty() && !img3.isEmpty() && !desc3.isEmpty()) {
                    gettitle3.setText(name3);
                    getprice3.setText(price3);
                    feature_desc3.setText(desc3);
                    try {
                        Glide.with(getApplicationContext())
                                .load(img3)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .thumbnail(0.5f)
                                .crossFade()
                                .skipMemoryCache(true)
                                .animate(R.anim.bounce)
                                .into(img_feature3);
                    } catch (IndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                    new_arrival_product3.setVisibility(View.VISIBLE);
                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }
                try {
                    if (collectfetureids.get(3) != null) {
                        new Variant_Details_Data4().execute();
                    }
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
    }
    //----------------------------------------------Variant 4------------------------------------------------
    private class Variant_Details_Data4 extends AsyncTask<String, String, Boolean> {
        JSONParse jsonParser = new JSONParse();
        @Override
        protected Boolean doInBackground(String... para) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                String json = jsonParser.makeHttpRequest(PRODUCT_URL+collectfetureids.get(3), "POST", params);
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
            try {
                if (!name4.isEmpty() && !price4.isEmpty() && !img4.isEmpty() && !desc4.isEmpty()) {
                    gettitle4.setText(name4);
                    getprice4.setText(price4);
                    feature_desc4.setText(desc4);
                    try {
                        Glide.with(getApplicationContext())
                                .load(img4)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .thumbnail(0.5f)
                                .crossFade()
                                .skipMemoryCache(true)
                                .animate(R.anim.bounce)
                                .into(img_feature4);
                    } catch (IndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                    new_arrival_product4.setVisibility(View.VISIBLE);
                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }
            try{
                if(collectfetureids.get(4)!= null) {
                    new Variant_Details_Data5().execute();
                }
            }catch (IndexOutOfBoundsException e){
                e.printStackTrace();
            }
        }
    }
    //----------------------------------------------Variant 5------------------------------------------------
    private class Variant_Details_Data5 extends AsyncTask<String, String, Boolean> {
        JSONParse jsonParser = new JSONParse();
        @Override
        protected Boolean doInBackground(String... para) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                String json = jsonParser.makeHttpRequest(PRODUCT_URL+collectfetureids.get(4), "POST", params);
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
                if (!name5.isEmpty() && !price5.isEmpty() && !img5.isEmpty() && !desc5.isEmpty()) {
                    gettitle5.setText(name5);
                    getprice5.setText(price5);
                    feature_desc5.setText(desc5);
                    try {
                        Glide.with(getApplicationContext())
                                .load(img5)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .thumbnail(0.5f)
                                .crossFade()
                                .skipMemoryCache(true)
                                .animate(R.anim.bounce)
                                .into(img_feature5);
                    } catch (IndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                    new_arrival_product5.setVisibility(View.VISIBLE);
                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }
            try{
                new Category_Search().execute();
            }catch (IndexOutOfBoundsException e){
                e.printStackTrace();
            }
        }
    }
    //--------------------------------------Active Search ----------------------------
    private class Category_Search extends AsyncTask<String, String, Boolean> {
        JSONParse jsonParser = new JSONParse();

        @Override
        protected Boolean doInBackground(String... para) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();

                String json = jsonParser.makeHttpRequest(CATEGORY_URL, "POST", params);
                try {
                    JSONObject c = new JSONObject(json);
                    JSONArray array = c.getJSONArray("product_categories");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                         categoryname.add(object.getString("name"));
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
    }
    public void showCustomAlert()
    {
        Context context = getApplicationContext();
        // Create layout inflator object to inflate toast.xml file
        LayoutInflater inflater = getLayoutInflater();
        // Call toast.xml file for toast layout
        View toastRoot = inflater.inflate(R.layout.toast, null);
        toast = new Toast(context);
        // Set layout to toast
        toast.setView(toastRoot);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM,
                4, 4);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();

    }


}
