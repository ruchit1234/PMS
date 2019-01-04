package com.kitelytech.pmsapp.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

 import com.kitelytech.pmsapp.R;
import com.kitelytech.pmsapp.activity.LoginActivity;
import com.kitelytech.pmsapp.activity.MainActivity;
import com.kitelytech.pmsapp.pojo.MessageItem;
import com.kitelytech.pmsapp.pojo.ResponseData;
import com.kitelytech.pmsapp.rest.ApiClient;
import com.kitelytech.pmsapp.rest.Fetchemployeedetailsinterface;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

        /**
  * Created by mxn on 2016/12/13.
  * MenuListFragment
  */
        
        public class MenuListFragment extends Fragment {

            private int navItemIndex = 0;
    // tags used to attach the fragments
            private static final String TAG_PROJECT_LIST = "Project List";
    private static final String TAG_CHANGE_PASSWORD = "Change Password";
    private static final String TAG_TAKE_LEAVE = "Take Leave";
    public static String CURRENT_TAG = TAG_PROJECT_LIST;
    private boolean shouldLoadHomeFragOnBackPress = true;
     FlowingDrawer mDrawer;

             CircleImageView ivMenuUserProfilePhoto;

            @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
            }

        
            @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                View view = inflater.inflate(R.layout.fragment_menu, container,
                                false);
                mDrawer = (FlowingDrawer) getActivity().findViewById(R.id.drawerlayout);
                ivMenuUserProfilePhoto=view.findViewById(R.id.ivprof);
                NavigationView vNavigation = (NavigationView) view.findViewById(R.id.vNavi);
                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Fetching Data...");
                progressDialog.show();
                SharedPreferences emp = getActivity().getSharedPreferences("employee", 0);
                String em=emp.getString("employee","").toString();
              if (em.equals("employee")){
                  Menu nav_Menu = vNavigation.getMenu();
                  nav_Menu.findItem(R.id.nav_task_Details).setVisible(false);
                }
                Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(ApiClient.BASE_URL)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
        
                        Fetchemployeedetailsinterface service = retrofit.create(Fetchemployeedetailsinterface.class);
        
                //Result is our pojo class
                                SharedPreferences settings = getActivity().getSharedPreferences("PREFS_NAME", 0);
                String emailtoken= settings.getString("email", "").toString();
                Call<ResponseData> call = service.Bind_Employee_Details_Based_On_Id(emailtoken);
                call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                                progressDialog.dismiss();
                                //.getMessage is POJO method to listen for final json output
                                        List<MessageItem> listResponse =response.body().getMessage();
                                String fname=listResponse.get(0).getEmpFirstName();
                                String lname=listResponse.get(0).getEmpLastName();
                                String email=listResponse.get(0).getEmpEmail();
                                String imgaddress=listResponse.get(0).getEmpPhoto();
                                String url="http://pms.cloudester.com"+imgaddress;
                                ivMenuUserProfilePhoto=getActivity().findViewById(R.id.ivprof);
                                TextView navhead = getActivity().findViewById(R.id.navhead);
                                TextView navsubt = getActivity().findViewById(R.id.navsubt);
                                navhead.setText(fname+" "+lname);
                                navsubt.setText(email);
                                Picasso.get().load(url).into(ivMenuUserProfilePhoto);
                
                                    }

                    @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), "Check Your Internet Connection", Toast.LENGTH_LONG).show();
                            }
        });
                vNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                
                                        int id = menuItem.getItemId();
                
                                        if (id == R.id.nav_project_list) {
                                        FragmentManager fm = getActivity().getSupportFragmentManager();
                                        ProjectListFragment mprjlist = (ProjectListFragment) fm.findFragmentById(R.id.frame);
                                        if (mprjlist == null) {
                                                mprjlist = new ProjectListFragment();
                                                fm.beginTransaction().replace(R.id.id_main_frame, mprjlist).commit();
                                            }
                                    } else if (id == R.id.nav_change_password) {
                                        ChangePasswordFragment mPasswordFragment  = new ChangePasswordFragment();
                                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                        transaction.replace(R.id.id_main_frame, mPasswordFragment).commit();
                                    } else if (id == R.id.nav_notification) {
                    
                                            }
                                else if (id==R.id.menu_settings){
                                        Settingsfragment mPasswordFragment  = new Settingsfragment();
                                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                        transaction.replace(R.id.id_main_frame, mPasswordFragment).commit();
                    
                                            }
                                            else if(id==R.id.nav_task_Details){
                                            ManagerTaskdet mPasswordFragment  = new ManagerTaskdet();
                                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                            transaction.replace(R.id.id_main_frame, mPasswordFragment).commit();
                                        }
                                else if (id == R.id.nav_logout) {
                                        SharedPreferences settings = getActivity().getSharedPreferences("PREFS_NAME", 0);
                                                    SharedPreferences.Editor editor = settings.edit();
                                                    editor.remove("logged");
                                                    editor.remove("email");
                                                    editor.remove("old");
                                                    editor.remove("proname");
                                                    editor.remove("id");
                                                    editor.remove("tid");
                                                    editor.remove("eid");
                                                    editor.commit();
                                                    SharedPreferences set = getActivity().getSharedPreferences("myPrefs", 0);
                                                    SharedPreferences.Editor ed = set.edit();
                                                    ed.remove("empid");
                                                    ed.remove("tstatus");
                                                    ed.remove("qaid");
                                                    ed.commit();
                                        SharedPreferences man = getActivity().getSharedPreferences("manager", 0);
                                        SharedPreferences.Editor edman = man.edit();
                                        edman.remove("manager");
                                        edman.commit();
                                        SharedPreferences emp = getActivity().getSharedPreferences("employee", 0);
                                        SharedPreferences.Editor edemp = emp.edit();
                                        edemp.remove("employee");
                                        edemp.commit();
                                        SharedPreferences state = getActivity().getSharedPreferences("State", 0);
                                        SharedPreferences.Editor edstate = state.edit();
                                        edstate.remove("state");
                                        edstate.commit();
                                        getActivity().finish();
                                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                                        startActivity(intent);
                                                    getActivity().finish();
                                    }
                                        if (mDrawer.isMenuVisible()) {
                                        mDrawer.closeMenu();
                                    }
                                return true;
                            }
        }) ;
                return  view ;
            }

        
        }