package com.kitelytech.pmsapp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kitelytech.pmsapp.R;
import com.kitelytech.pmsapp.fragment.ChangePasswordFragment;
import com.kitelytech.pmsapp.fragment.ProfileFragment;
import com.kitelytech.pmsapp.fragment.ProjectListFragment;
import com.kitelytech.pmsapp.fragment.TakeLeaveFragment;
import com.kitelytech.pmsapp.rest.APIInterface;
import com.kitelytech.pmsapp.rest.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    // TODO - insert your themoviedb.org API KEY here
    private final static String API_KEY = "f48c1bc7b6689608da5805235fc20f5c";
    private static final String TAG = MainActivity.class.getSimpleName();

    // index to identify current nav menu item
    private int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_PROJECT_LIST = "Project List";
    private static final String TAG_CHANGE_PASSWORD = "Change Password";
    private static final String TAG_TAKE_LEAVE = "Take Leave";
    public static String CURRENT_TAG = TAG_PROJECT_LIST;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;

    private ImageView ivProfileImg;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (API_KEY.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please obtain your API KEY first from themoviedb.org", Toast.LENGTH_LONG).show();
            return;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);
        if (savedInstanceState == null) {
            CURRENT_TAG = TAG_PROJECT_LIST;
            navItemIndex = 0;
            loadHomeFragment();
        }

        ivProfileImg = (ImageView)headerView.findViewById(R.id.ivProfileImg);
        ivProfileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProfileFragment mProfileFragment = new ProfileFragment();
                Fragment fragment = mProfileFragment;
//                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
//                        android.R.anim.fade_out);
//                fragmentTransaction.replace(R.id.frame, fragment, "Profile");
//                fragmentTransaction.commitAllowingStateLoss();
//                setTitle("Profile");

                switchContent(R.id.frame, fragment, "Profile");

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

    }

    public void switchContent(int id, Fragment fragment, String title){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out);
        fragmentTransaction.replace(id, fragment, title);
        fragmentTransaction.commitAllowingStateLoss();
        setTitle(title);

    }

    public void loadHomeFragment(){
        Fragment fragment = getHomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
        fragmentTransaction.commitAllowingStateLoss();
        setTitle(CURRENT_TAG);
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // project list
                ProjectListFragment mProjectListFragment = new ProjectListFragment();
                return mProjectListFragment;
            case 1:
                //change password
                ChangePasswordFragment mPasswordFragment  = new ChangePasswordFragment();
                return mPasswordFragment;
            case 2:
                // Take Leave
                TakeLeaveFragment mLeaveFragment = new TakeLeaveFragment();
                return mLeaveFragment;
            case 3:
                // logout

            default:
                return new ProjectListFragment();
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if (shouldLoadHomeFragOnBackPress){
            if (navItemIndex != 0){
                navItemIndex = 0;
                CURRENT_TAG = TAG_PROJECT_LIST;
                loadHomeFragment();
                return;
            }
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_project_list) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_PROJECT_LIST;
        } else if (id == R.id.nav_change_password) {
            navItemIndex = 1;
            CURRENT_TAG = TAG_CHANGE_PASSWORD;
        } else if (id == R.id.nav_notification) {

        } else if (id == R.id.nav_logout) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_take_leave) {
            navItemIndex = 2;
            CURRENT_TAG = TAG_TAKE_LEAVE;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        loadHomeFragment();
        return true;
    }
}
