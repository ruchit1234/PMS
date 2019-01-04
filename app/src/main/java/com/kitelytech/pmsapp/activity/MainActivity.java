package com.kitelytech.pmsapp.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.kitelytech.pmsapp.R;

import com.kitelytech.pmsapp.adapter.SimpleAdapter;
import com.kitelytech.pmsapp.fragment.ChangePasswordFragment;
import com.kitelytech.pmsapp.fragment.ManagerTaskdet;
import com.kitelytech.pmsapp.fragment.MenuListFragment;
import com.kitelytech.pmsapp.fragment.ProfileFragment;
import com.kitelytech.pmsapp.fragment.ProjectListFragment;
import com.kitelytech.pmsapp.fragment.Search_fragment;
import com.kitelytech.pmsapp.fragment.Settingsfragment;
import com.kitelytech.pmsapp.fragment.TakeLeaveFragment;

import com.kitelytech.pmsapp.pojo.Fetchempproj;
import com.kitelytech.pmsapp.pojo.Message;
import com.kitelytech.pmsapp.pojo.MessageItem;
import com.kitelytech.pmsapp.pojo.ResponseData;
import com.kitelytech.pmsapp.rest.APIInterface;
import com.kitelytech.pmsapp.rest.ApiClient;
import com.kitelytech.pmsapp.rest.Employeeprojlist;
import com.kitelytech.pmsapp.rest.Fetchemployeedetailsinterface;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ListHolder;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.squareup.picasso.Picasso;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.supercharge.shimmerlayout.ShimmerLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity{



    TextView tvname,tvlog;
    CircleImageView img;
    SimpleAdapter adapter;
    FlowingDrawer mDrawer;
    String name;


    // flag to load home fragment when user presses back key
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvname=findViewById(R.id.tvuname);
        tvlog=findViewById(R.id.tvlog);
        mDrawer=findViewById(R.id.drawerlayout);
        mDrawer.setTouchMode(ElasticDrawer.TOUCH_MODE_BEZEL);
        setupToolbar();
        //loadprofilepic();
        fetchDepartmentName();
        adapter = new SimpleAdapter(MainActivity.this);
        setupMenu();
        Retrofit rfit = new Retrofit.Builder()
                .baseUrl(ApiClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        SharedPreferences set = getSharedPreferences("PREFS_NAME", 0);
        String email = set.getString("email", "").toString();
        Fetchemployeedetailsinterface serv = rfit.create(Fetchemployeedetailsinterface.class);
        Call<ResponseData> icall = serv.Bind_Employee_Details_Based_On_Id(email);
        icall.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> icall, Response<ResponseData> response) {
                //.getMessage is POJO method to listen for final json output
                List<MessageItem> listResponse = response.body().getMessage();
                String id = String.valueOf(listResponse.get(0).getEmpid());
                SharedPreferences settings = getSharedPreferences("PrefEID", MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("eid", id);
                editor.commit();
            }

            @Override
            public void onFailure(Call<ResponseData> icall, Throwable t) {

            }

        });
isOnline();

    }
    protected void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setIcon(R.drawable.ic_search_white_24dp);
        toolbar.setNavigationIcon(R.drawable.ic_nav_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDrawer.toggleMenu();

            }
        });
    }
    /*
   public void loadprofilepic(){
       Retrofit retrofit = new Retrofit.Builder()
               .baseUrl(ApiClient.BASE_URL)
               .addConverterFactory(GsonConverterFactory.create())
               .build();

       Fetchemployeedetailsinterface service = retrofit.create(Fetchemployeedetailsinterface.class);

//Result is our pojo class
       SharedPreferences settings = getSharedPreferences("PREFS_NAME", 0);
       String emailtoken= settings.getString("email", "").toString();
       Call<ResponseData> call = service.Bind_Employee_Details_Based_On_Id(emailtoken);
       call.enqueue(new Callback<ResponseData>() {
           @Override
           public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
               //.getMessage is POJO method to listen for final json output
               List<MessageItem> listResponse =response.body().getMessage();
               String fname=listResponse.get(0).getEmpFirstName();
               String lname=listResponse.get(0).getEmpLastName();
               String email=listResponse.get(0).getEmpEmail();
               String imgaddress=listResponse.get(0).getEmpPhoto();
               String url="http://pms.cloudester.com"+imgaddress;
               Picasso.get().load(url).into(img);


           }




           @Override
           public void onFailure(Call<ResponseData> call, Throwable t) {
               Toast.makeText(MainActivity.this, "Check Your Internet Connection", Toast.LENGTH_LONG).show();
           }
       });
   }*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportActionBar().setDisplayShowTitleEnabled(false);
   //getMenuInflater().inflate(R.menu.menu_spinner, menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        //fetchDepartmentName();
        // Drop down layout style - list view with radio button
        return true;
    }

   private void setupMenu() {
       FragmentManager fm = getSupportFragmentManager();
      MenuListFragment mMenuFragment = (MenuListFragment) fm.findFragmentById(R.id.id_container_menu);
     if (mMenuFragment == null) {
       mMenuFragment = new MenuListFragment();
     fm.beginTransaction().replace(R.id.id_container_menu, mMenuFragment).commit();
      }}

//        mDrawer.setOnDrawerStateChangeListener(new ElasticDrawer.OnDrawerStateChangeListener() {
//            @Override
//            public void onDrawerStateChange(int oldState, int newState) {
//                if (newState == ElasticDrawer.STATE_CLOSED) {
//                    Log.i("MainActivity", "Drawer STATE_CLOSED");
//                }
//            }
//
//            @Override
//            public void onDrawerSlide(float openRatio, int offsetPixels) {
//                Log.i("MainActivity", "openRatio="  openRatio  " ,offsetPixels="  offsetPixels);
//            }
//        });
    //}

   private void fetchDepartmentName(){
//Creating a rest adapter
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Creating an object of our api interface
        Employeeprojlist service = retrofit.create(Employeeprojlist.class);
        //While the app fetched data we are displaying a progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "Fetching Data", "Please wait...", false, false);
        SharedPreferences settings = this.getSharedPreferences("PREFS_NAME", 0);
        String emailtoken= settings.getString("email", "").toString();
        Call<Fetchempproj> call = service.Bind_Employee_Projects(emailtoken);
        call.enqueue(new Callback<Fetchempproj>() {
            @Override
            public void onResponse(Call<Fetchempproj> call, Response<Fetchempproj> response) {
                 List<Message> listResponse =response.body().getMessage();
                 List<String> listSpinner = new ArrayList<String>();
                listSpinner.add("All");
                 for (int i = 0; i < listResponse.size(); i++){
                        String id=listResponse.get(i).getProjectId().toString();
                         name=listResponse.get(i).getProjectName();
                        String fn=id+"."+name;
                    listSpinner.add(fn);
                }

                 final ArrayAdapter a = new ArrayAdapter(MainActivity.this,R.layout.spinner_item_black,listSpinner);
                    a.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    //Setting the ArrayAdapter data on the Spinner
                    Spinner spinner = (Spinner) findViewById(R.id.spproj);
                    spinner.setAdapter(a);
              loading.dismiss();
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        String item=spinner.getSelectedItem().toString();
                        String a=item.replaceAll("([a-z])", "");
                        String b=a.replaceAll("([A-Z])", "");
                        String prid=b.replaceAll("([.])", "");
                        String prname=item.replaceAll("([.])", "");
                        String pname=prname.replaceAll("([0-9])", "");
                        tvname.setText(" "+pname);

                        if (item.contains("All")){
                        SharedPreferences bid = getSharedPreferences("PREFS_NAME", 0);
                        SharedPreferences.Editor editor = bid.edit();
                            editor.remove("id");
                            editor.commit();
                            FragmentManager fm = getSupportFragmentManager();
                            ProjectListFragment mprjlist = (ProjectListFragment) fm.findFragmentById(R.id.frame);
                            if (mprjlist == null) {
                                mprjlist = new ProjectListFragment();
                                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                                        android.R.anim.fade_out);
                                fragmentTransaction.commitAllowingStateLoss();
                                fm.beginTransaction().replace(R.id.id_main_frame, mprjlist).commit();
                                tvname.setText("   All Projects");
                            }

                        }
                            else {
                        SharedPreferences settings = getSharedPreferences("PREFS_NAME", MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                           editor.putString("id", prid);
                            editor.commit();
                            FragmentManager fm = getSupportFragmentManager();
                            ProjectListFragment mprjlist = (ProjectListFragment) fm.findFragmentById(R.id.frame);
                            if (mprjlist == null) {
                                mprjlist = new ProjectListFragment();
                                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                                        android.R.anim.fade_out);
                                fragmentTransaction.commitAllowingStateLoss();
                                fm.beginTransaction().replace(R.id.id_main_frame, mprjlist).commit();
                            }
                        }

                    }



                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // your code here

                    }

                });
                }
            @Override
            public void onFailure(Call<Fetchempproj> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(MainActivity.this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
            }
        });}


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {

        }
        else {
        }
        return false;
    }
    public MainActivity() {
        // Constructor body
    }
    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            Intent intent=new Intent(MainActivity.this,MainActivity.class);
            startActivity(intent);
            finish();

            //additional code
        } else {
            getFragmentManager().popBackStack();
            finish();
        }


    }


    public void onClick(View view) {
        FragmentManager fm = getSupportFragmentManager();
        ProfileFragment mprjlist = (ProfileFragment) fm.findFragmentById(R.id.frame);
        if (mprjlist == null) {
            mprjlist = new ProfileFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                    android.R.anim.fade_out);
            fragmentTransaction.commitAllowingStateLoss();
            fm.beginTransaction().replace(R.id.id_main_frame, mprjlist).commit();}
    }

       /* if (mDrawer.isMenuVisible()) {
            mDrawer.closeMenu();
        }*/




    public void name(View view) {
        DialogPlus dialog = DialogPlus.newDialog(this)
                .setGravity(Gravity.TOP)
                .setHeader(R.layout.dialog_view)
                .setAdapter(adapter)
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                             String v = String.valueOf(position);
                                if (v.equals("0")){
                                    tvlog.setText("   Tasks List");
                                    dialog.dismiss();
                                    FragmentManager fm = getSupportFragmentManager();
                                    ProjectListFragment mprjlist = (ProjectListFragment) fm.findFragmentById(R.id.frame);
                                    if (mprjlist == null) {
                                        mprjlist = new ProjectListFragment();
                                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                                                android.R.anim.fade_out);
                                        fragmentTransaction.commitAllowingStateLoss();
                                        fm.beginTransaction().replace(R.id.id_main_frame, mprjlist).commit();}
                                }
                                else if(v.equals("1")){
                                    tvlog.setText("   Calendar");
                                    dialog.dismiss();
                                }
                                else if(v.equals("2")){
                                    tvlog.setText(" Messaging");
                                    dialog.dismiss();
                                }
                                else if(v.equals("3")){
                                    SharedPreferences settings = getSharedPreferences("manager", 0);
                                    String man=settings.getString("manager", "").toString();
                                    if (man.equals("manager")) {
                                        tvlog.setText("   View Tasks");
                                        FragmentManager fm = getSupportFragmentManager();
                                        ManagerTaskdet mprjlist = (ManagerTaskdet) fm.findFragmentById(R.id.frame);
                                        if (mprjlist == null) {
                                            mprjlist = new ManagerTaskdet();
                                            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                                                    android.R.anim.fade_out);
                                            fragmentTransaction.commitAllowingStateLoss();
                                            fm.beginTransaction().replace(R.id.id_main_frame, mprjlist).commit();}

                                    }
                                    else {
                                    tvlog.setText("About Project");
                                    }
                                    dialog.dismiss();
                                }
                                else if(v.equals("4")){
                                    dialog.dismiss();
                                    FragmentManager fm = getSupportFragmentManager();
                                    Settingsfragment mprjlist = (Settingsfragment) fm.findFragmentById(R.id.frame);
                                    if (mprjlist == null) {
                                        mprjlist = new Settingsfragment();
                                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                                                android.R.anim.fade_out);
                                        fragmentTransaction.commitAllowingStateLoss();
                                        fm.beginTransaction().replace(R.id.id_main_frame, mprjlist).commit();}

                                }
                                else if (v.equals("5")){
                                    dialog.dismiss();
                                    SharedPreferences settings = getSharedPreferences("PREFS_NAME", 0);
                                    SharedPreferences.Editor editor = settings.edit();
                                    editor.remove("logged");
                                    editor.remove("email");
                                    editor.remove("old");
                                    editor.remove("proname");
                                    editor.remove("id");
                                    editor.remove("tid");
                                    editor.remove("eid");
                                    editor.commit();
                                    SharedPreferences set = getSharedPreferences("myPrefs", 0);
                                    SharedPreferences.Editor ed = set.edit();
                                    ed.remove("empid");
                                    ed.remove("tstatus");
                                    ed.remove("qaid");
                                    ed.commit();
                                    SharedPreferences man = getSharedPreferences("manager", 0);
                                    SharedPreferences.Editor edman = man.edit();
                                    edman.remove("manager");
                                    edman.commit();
                                    SharedPreferences emp = getSharedPreferences("employee", 0);
                                    SharedPreferences.Editor edemp = emp.edit();
                                    edemp.remove("employee");
                                    edemp.commit();
                                    SharedPreferences state = getSharedPreferences("State", 0);
                                    SharedPreferences.Editor edstate = state.edit();
                                    edstate.remove("state");
                                    edstate.commit();
                                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                    }
                })
                .setExpanded(true, 1300)  // This will enable the expand feature, (similar to android L share dialog)
                .setCancelable(true)
                .create();
        dialog.show();

    }

    public void search(View view) {
        FragmentManager fm = getSupportFragmentManager();
        Search_fragment mprjlist = (Search_fragment) fm.findFragmentById(R.id.frame);
        if (mprjlist == null) {
            mprjlist = new Search_fragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                    android.R.anim.fade_out);
            fragmentTransaction.commitAllowingStateLoss();
            fm.beginTransaction().replace(R.id.id_main_frame, mprjlist).commit();}

    }
}
