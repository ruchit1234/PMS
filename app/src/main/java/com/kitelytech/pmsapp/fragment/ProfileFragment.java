package com.kitelytech.pmsapp.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.kitelytech.pmsapp.R;
import com.kitelytech.pmsapp.activity.MainActivity;
import com.kitelytech.pmsapp.pojo.FileUtils;
import com.kitelytech.pmsapp.pojo.MessageItem;
import com.kitelytech.pmsapp.pojo.ResponseData;
import com.kitelytech.pmsapp.pojo.Result;
import com.kitelytech.pmsapp.rest.APIInterface;
import com.kitelytech.pmsapp.rest.ApiClient;
import com.kitelytech.pmsapp.rest.Fetchemployeedetailsinterface;
import com.kitelytech.pmsapp.rest.UpdateEmployeeinterface;
import com.kitelytech.pmsapp.rest.Updatephoto;
import com.kitelytech.pmsapp.rest.UpimgClient;
import com.squareup.picasso.Picasso;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickClick;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    public static final int PICK_IMAGE = 1;
    public static final int REQUEST_CAMERA = 100;
    public ImageView pick;
    public Uri filePath;
    EditText etfname,etlname,etemail,etaddress,etjoindt;
    TextView ettvname;
    Button update;
    public ProfileFragment() {
        // Required empty public constructor
    }


    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Dexter.withActivity(getActivity())
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */}
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).check();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        pick=view.findViewById(R.id.ivEditProfileImg);
        pick.setOnClickListener(this);
        etfname=view.findViewById(R.id.etfname);
        etlname=view.findViewById(R.id.etlname);
        etemail=view.findViewById(R.id.etemail);
        etaddress=view.findViewById(R.id.etaddress);
        etjoindt=view.findViewById(R.id.etjoindate);
        update=view.findViewById(R.id.update);
        ettvname=view.findViewById(R.id.tvName);
        update.setOnClickListener(this);

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();
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
                //.getMessage is POJO method to listen for final json output
                List<MessageItem> listResponse =response.body().getMessage();
                String fname=listResponse.get(0).getEmpFirstName();
                String lname=listResponse.get(0).getEmpLastName();
                String email=listResponse.get(0).getEmpEmail();
                String address=listResponse.get(0).getEmpAddress();
                String joindt=listResponse.get(0).getJoiningDate();
                String imgaddress=listResponse.get(0).getEmpPhoto();
                String imgurl="http://pms.cloudester.com"+imgaddress;
                SharedPreferences settings = getActivity().getSharedPreferences("PREFS_NAME", MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("old",imgaddress);
                Picasso.get().load(imgurl).into(pick);
                ettvname.setText(fname+" "+lname);
                etfname.setText(fname);
                etlname.setText(lname);
               etemail.setText(email);
               etaddress.setText(address);
               etjoindt.setText(joindt);
               progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Check Your Internet Connection", Toast.LENGTH_LONG).show();
            }
        });

                return view;
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_leave, menu);
                  super.onCreateOptionsMenu(menu,inflater);
    }
    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId() /*to get clicked view id**/) {
            case R.id.ivEditProfileImg:

                // do seection when the profile image is clicked is clicked
        PickImageDialog.build(new PickSetup()).show(getActivity());
        PickSetup setup = new PickSetup();
        PickImageDialog.build(setup)
                .setOnClick(new IPickClick() {
                    @Override
                    public void onGalleryClick() {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                    }

                    @Override
                    public void onCameraClick() {
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, REQUEST_CAMERA);

                    }
                }).show(getActivity());
    break;

            case R.id.update:
                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Updating Data...");
                progressDialog.show();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(ApiClient.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                SharedPreferences settings = getActivity().getSharedPreferences("PREFS_NAME", 0);
                String emailtoken= settings.getString("email","").toString();
                Fetchemployeedetailsinterface service = retrofit.create(Fetchemployeedetailsinterface.class);
                Call<ResponseData> call = service.Bind_Employee_Details_Based_On_Id(emailtoken);
                call.enqueue(new Callback<ResponseData>() {
                    @Override
                    public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                        //.getMessage is POJO method to listen for final json output
                        List<MessageItem> listResponse =response.body().getMessage();
                        int id=listResponse.get(0).getEmpid();
                        String sfname = etfname.getText().toString();
                        String slname = etlname.getText().toString();
                        String saddress=etaddress.getText().toString();
                        UpdateEmployeeinterface updateservice = retrofit.create(UpdateEmployeeinterface.class);
                        Call<Result> upcall = updateservice.Update_Emp_Profile_Details(sfname,slname,saddress,id);
                        upcall.enqueue(new Callback<Result>() {
                            @Override
                            public void onResponse(Call<Result> call, Response<Result> response) {
                                //.getMessage is POJO method to listen for final json output
                                String msg=response.body().getMessage();
                                if (msg.equals("Profile Updated Successfully..!!")) {
                                    Toast.makeText(getActivity(),"Profile Updated",Toast.LENGTH_LONG).show();
                                }
                                progressDialog.dismiss();
                                }

                            @Override
                            public void onFailure(Call<Result> call, Throwable t) {
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), "Check Your Internet Connection", Toast.LENGTH_LONG).show();
                            }
                        });

                        }

                    @Override
                    public void onFailure(Call<ResponseData> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Check Your Internet Connection", Toast.LENGTH_LONG).show();
                    }
                });
                break;
            default:
                break;
        }}
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            Uri selectedfile = data.getData(); //The uri with the location of the file
            String fname = getFileName(selectedfile);
            java.io.File file = FileUtils.getFile(getActivity(),selectedfile);
            pick.setImageURI(selectedfile);
            Retrofit Upimg = new Retrofit.Builder()
                    .baseUrl(UpimgClient.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            SharedPreferences settings = getActivity().getSharedPreferences("PREFS_NAME", 0);
            String oldimg= settings.getString("old", "").toString();
            String emailtoken= settings.getString("email","").toString();
            Updatephoto upservice = Upimg.create(Updatephoto.class);
            RequestBody requestFile =
                    RequestBody.create(
                            MediaType.parse(getActivity().getContentResolver().getType(selectedfile)),
                            (java.io.File) file
                    );

            // MultipartBody.Part is used to send also the actual file name
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("file",fname, requestFile);

            // add another part within the multipart request
            RequestBody email =
                    RequestBody.create(
                            okhttp3.MultipartBody.FORM, emailtoken);
            RequestBody old =
                    RequestBody.create(
                            okhttp3.MultipartBody.FORM, oldimg);

            ProfileFragment tag=this;
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            // finally, execute the old
            Call<ResponseBody> call = upservice.imgStore_Path(body, email,old);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call,
                                       Response<ResponseBody> response) {
                    Toast.makeText(getContext(),"Upload Success",Toast.LENGTH_LONG).show();
                    ft.detach(tag).attach(tag).commit();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }
        else if(requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap selectedfile = (Bitmap) extras.get("data");
            Uri imageuri=getImageUri(getActivity(),selectedfile);
            String fname = getFileName(imageuri);
            java.io.File file = FileUtils.getFile(getActivity(),imageuri);
            pick.setImageURI(imageuri);
            Retrofit Upimg = new Retrofit.Builder()
                    .baseUrl(UpimgClient.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            SharedPreferences settings = getActivity().getSharedPreferences("PREFS_NAME", 0);
            String oldimg= settings.getString("old", "").toString();
            String emailtoken= settings.getString("email","").toString();
            Updatephoto upservice = Upimg.create(Updatephoto.class);
            RequestBody requestFile =
                    RequestBody.create(
                            MediaType.parse(getActivity().getContentResolver().getType(imageuri)),
                            (java.io.File) file
                    );

            // MultipartBody.Part is used to send also the actual file name
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("file",fname, requestFile);

            // add another part within the multipart request
            RequestBody email =
                    RequestBody.create(
                            okhttp3.MultipartBody.FORM, emailtoken);
            RequestBody old =
                    RequestBody.create(
                            okhttp3.MultipartBody.FORM, oldimg);

            ProfileFragment tag=this;
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            // finally, execute the old
            Call<ResponseBody> call = upservice.imgStore_Path(body, email,old);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call,
                                       Response<ResponseBody> response) {
                    Toast.makeText(getContext(),"Upload Success",Toast.LENGTH_LONG).show();
                    ft.detach(tag).attach(tag).commit();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }
    }
    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}
