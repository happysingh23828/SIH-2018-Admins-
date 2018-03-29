package dynamicdrillers.sih2018admins;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.interfaces.DownloadProgressListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import dmax.dialog.SpotsDialog;

public class DashboardActivity extends AppCompatActivity {

    SharedpreferenceHelper sharedpreferenceHelper = SharedpreferenceHelper.getInstance(this);
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    TextView Admins, ViewComplaints;
    CardView CardAdmin, CardComplaint, sendNotification;
    public static final String SharedprefenceName = "USER_DATA";
    private String Type;
    ImageView imageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

         findViewById(R.id.click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String s[]={android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
                if(ContextCompat.checkSelfPermission(DashboardActivity.this, android.Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(DashboardActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                           checkPermissions();
                    }
                    else{
                        ActivityCompat.requestPermissions(DashboardActivity.this,s,123);
                    }
                }
                else{
                    ActivityCompat.requestPermissions(DashboardActivity.this,s,123);
                }




            }
        });


        init();
        SharedPreferences sharedPreferences = getSharedPreferences(SharedprefenceName,Context.MODE_PRIVATE);


        Type = getIntent().getStringExtra("type");

//        if(Type.equals("authority_admin"))
  //          startActivity(new Intent(this,AuthorityDashboardActivity.class));

        if(Type.equals("admin"))
            Admins.setText("State Admins");
        else  if(Type.equals("state_admin"))
            Admins.setText("District Admins");
        else if(Type.equals("district_admin"))
            Admins.setText("Regions Admins");
        else
            Admins.setText("Authority Admins");




        CardComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Type.equals("region_admin")){
                    Intent intent = new Intent(DashboardActivity.this,ComplaintsActivity.class);
                    intent.putExtra("type","complainer_region");
                    intent.putExtra("data",SharedpreferenceHelper.getInstance(getBaseContext()).getRegion());
                    startActivity(intent);
                }
                else {
                    final Dialog dialog = new Dialog(DashboardActivity.this);
                    dialog.setContentView(R.layout.complaint_dialog_layout);
                    dialog.setTitle("Choose  Action ");

                    final TextView State = (TextView) dialog.findViewById(R.id.state);
                    final TextView District = (TextView) dialog.findViewById(R.id.district);
                    final TextView Region = (TextView) dialog.findViewById(R.id.region);

                    if(Type.equals("admin")) {
                        State.setVisibility(View.VISIBLE);
                        District.setVisibility(View.VISIBLE);
                        Region.setVisibility(View.VISIBLE);
                    }
                    else  if(Type.equals("state_admin")){
                        State.setVisibility(View.VISIBLE);
                        District.setVisibility(View.VISIBLE);
                        Region.setVisibility(View.VISIBLE);

                    }
                    else if(Type.equals("district_admin")){
                        State.setVisibility(View.GONE);
                        District.setVisibility(View.VISIBLE);
                        Region.setVisibility(View.VISIBLE);
                    }


                    State.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(Type.equals("admin")){
                                dialog.dismiss();
                                Intent intent = new Intent(DashboardActivity.this,FilterActivity.class);
                                intent.putExtra("type","state");
                                startActivity(intent);
                            }
                            else {
                                dialog.dismiss();
                                Intent intent = new Intent(DashboardActivity.this,ComplaintsActivity.class);
                                intent.putExtra("type","complainer_state");
                                intent.putExtra("data",SharedpreferenceHelper.getInstance(getBaseContext()).getState());
                                startActivity(intent);
                            }

                        }
                    });

                    District.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(Type.equals("district_admin")){
                                Toast.makeText(DashboardActivity.this, SharedpreferenceHelper.getInstance(getBaseContext()).getDistrict(), Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                Intent intent = new Intent(DashboardActivity.this,ComplaintsActivity.class);
                                intent.putExtra("type","complaint_district");
                                intent.putExtra("data",SharedpreferenceHelper.getInstance(getBaseContext()).getDistrict());
                                startActivity(intent);
                            }
                            else {
                                dialog.dismiss();
                                Intent intent = new Intent(DashboardActivity.this,FilterActivity.class);
                                intent.putExtra("type","district");
                                startActivity(intent);
                            }

                        }
                    });

                    Region.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(Type.equals("region_admin")){
                                Toast.makeText(DashboardActivity.this, SharedpreferenceHelper.getInstance(getBaseContext()).getDistrict(), Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                Intent intent = new Intent(DashboardActivity.this,ComplaintsActivity.class);
                                intent.putExtra("type","complainer_region");
                                intent.putExtra("data",SharedpreferenceHelper.getInstance(getBaseContext()).getRegion());
                                startActivity(intent);
                            }
                            else {
                                dialog.dismiss();
                                Intent intent = new Intent(DashboardActivity.this,FilterActivity.class);
                                intent.putExtra("type","region");
                                startActivity(intent);
                            }

                        }
                    });



                    dialog.show();


                }


            }
        });


        CardAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(DashboardActivity.this);
                dialog.setContentView(R.layout.admins_dialog_layout);
                dialog.setTitle("Choose  Action ");



                final Button AddAdmin = (Button) dialog.findViewById(R.id.add_admin);
                final Button AdminList = (Button) dialog.findViewById(R.id.admin_list);

                if(Type.equals("admin")){
                    AddAdmin.setText("Add State");
                    AdminList.setText("State Admins List");
                }

                else  if(Type.equals("state_admin")){
                    AddAdmin.setText("Add District");
                    AdminList.setText("District Admins List");
                }
                else if(Type.equals("district_admin")){
                    AddAdmin.setText("Add Region");
                    AdminList.setText("Region Admins");
                }
                else {
                    AddAdmin.setText("Add Authority");
                    AdminList.setText("Authority Admins List");
                }


                AddAdmin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(DashboardActivity.this,AdminsRegistrationActivity.class));
                        dialog.dismiss();
                    }
                });

                AdminList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent ;
                        if(Type.equals("admin")){
                            intent = new Intent(DashboardActivity.this,StateAdminsActivity.class);
                        }
                        else  if(Type.equals("state_admin"))
                            intent = new Intent(DashboardActivity.this,DistrictAdminsActivity.class);
                        else if(Type.equals("district_admin"))
                            intent = new  Intent(DashboardActivity.this,RegionsAdminActivity.class);
                        else
                            intent = new  Intent(DashboardActivity.this,AuthorityAdminActivity.class);



                        intent.putExtra("type",Type);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });


                dialog.show();
                //startActivity(new Intent(DashboardActivity.this,StateAdminsActivity.class));
            }
        });





        Toast.makeText(this, sharedPreferences.getString("email",null)+" "+sharedPreferences.getString("password",null), Toast.LENGTH_SHORT).show();

    }

    /**
     *   Initialization of views
     */
    private void init(){
        Admins = findViewById(R.id.admin_dashboard_text);
        ViewComplaints = findViewById(R.id.viewComplaints_dashboard_txt);
        CardAdmin = findViewById(R.id.CardAdmins);
        CardComplaint =  findViewById(R.id.CardComplaints);
    }

    void checkPermissions() {
        final SpotsDialog spotsDialog  = new SpotsDialog(this);
        spotsDialog.show();
        AndroidNetworking.download("http://api.pdflayer.com/api/convert?access_key=d31e4dc5234b89ae4c645efcd97002c0&document_url=http://apilayer.com"
                , Environment.getExternalStorageDirectory().getPath()+"/Pictures","Mypdf.pdf")
                .setTag("downloadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setDownloadProgressListener(new DownloadProgressListener() {
                    @Override
                    public void onProgress(long bytesDownloaded, long totalBytes) {
                        // do anything with progress

                    }
                })
                .startDownload(new DownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        spotsDialog.dismiss();
                        Toast.makeText(DashboardActivity.this, "Download complete", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onError(ANError error) {
                        spotsDialog.dismiss();
                        Toast.makeText(DashboardActivity.this, "error :"+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

}
