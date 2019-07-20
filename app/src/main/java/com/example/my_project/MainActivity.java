package com.example.my_project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.util.MissingFormatArgumentException;

public class MainActivity extends AppCompatActivity {

    private EditText id;
    private EditText password;
    private TextView info;
    private Button button;
    private int counter=5;
    private TextView registerbtn;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private TextView resetpass;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        id=(EditText)findViewById(R.id.user);
        password=(EditText)findViewById(R.id.pass);
        info=(TextView)findViewById(R.id.incorrect);
        button=(Button)findViewById(R.id.btn);
        registerbtn=(TextView)findViewById(R.id.register);
        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        resetpass= (TextView)findViewById(R.id.tvfgp);


        FirebaseUser user=firebaseAuth.getCurrentUser();

        info.setText("No. of attempts remmaining:- 5");

        if(user!=null)
        {finish();
         startActivity(new Intent(MainActivity.this,SecondActivity.class));}




        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Registration.class));
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(id.getText().toString().trim(), password.getText().toString().trim());

            }
        });

        resetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, ForgotPassword.class));
            }
        });
    }

    private void checkemailverification(){

        FirebaseUser firebaseUser=firebaseAuth.getInstance().getCurrentUser();
        Boolean flag=firebaseUser.isEmailVerified();

        if(flag==true){
            progressDialog.dismiss();
            Toast.makeText(MainActivity.this, "Login Successful",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, SecondActivity.class));
        }
        else {
            progressDialog.dismiss();
            Toast.makeText(this, "Verify your E-Mail", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(this, "Back Pressed", Toast.LENGTH_SHORT).show();
    }

    private boolean check(){

        boolean flag1=false;
        if(id.getText().toString().isEmpty() || password.getText().toString().isEmpty())
            flag1=false;
        else
            flag1=true;

        return flag1;
    }

    private void validate(String username, String userpassword) {


            check();
            if(check()==true){


        progressDialog.setMessage("Wait until verified");
        progressDialog.show();



        firebaseAuth.signInWithEmailAndPassword(username, userpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                        checkemailverification();

                    }
                else {Toast.makeText(MainActivity.this, "Login Failed Wrong Credentials",Toast.LENGTH_SHORT).show();
                   counter--;
                    progressDialog.dismiss();
                    info.setText(("No. of attempts remmaining:- "+counter));
                    if(counter==0)
                    {
                        button.setEnabled(false);}}}
        });
    }
            else Toast.makeText(MainActivity.this,"Enter Details",Toast.LENGTH_SHORT).show();
    }
}
