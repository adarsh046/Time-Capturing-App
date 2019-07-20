package com.example.my_project;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class SecondActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private TextView gomap;
    private TextView urlocation;
    private TextView submit;

    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        progressDialog=new ProgressDialog(this);

        firebaseAuth=FirebaseAuth.getInstance();
        urlocation=(TextView)findViewById(R.id.tvurloc);
        urlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                startActivity(new Intent(SecondActivity.this, showinglocation.class));

            }
        });

        gomap=(TextView)findViewById(R.id.gotomap);
        gomap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                startActivity(new Intent(SecondActivity.this, MapsActivity.class));

            }
        });
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,new String[]{ Manifest.permission.ACCESS_FINE_LOCATION},1);
        }



    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SecondActivity.this, MainActivity.class));
        Toast.makeText(SecondActivity.this, "Logout first",Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){
            case R.id.logoutMenu:{
                progressDialog.setMessage("signing out");
                progressDialog.show();
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(SecondActivity.this, MainActivity.class));
            }
        }
        return true;
    }

}
