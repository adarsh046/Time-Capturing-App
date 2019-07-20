package com.example.my_project;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Registration extends AppCompatActivity {

    private EditText username, userpassword, usermail;
    private Button regButton;
    private TextView userlogin;
    private FirebaseAuth firebaseAuth;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Registration.this, MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        firebaseAuth=FirebaseAuth.getInstance();



        username=(EditText)findViewById(R.id.etusername);
        userpassword=(EditText)findViewById((R.id.etpassword));
        usermail=(EditText)findViewById(R.id.etemail);
        regButton=(Button) findViewById(R.id.button);
        userlogin=(TextView)findViewById((R.id.tvuserlogin));

        userlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registration.this, MainActivity.class));
            }
        });

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    //upload data
                    String user_email=usermail.getText().toString().trim();
                    String user_password=userpassword.getText().toString().trim();
                    firebaseAuth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                sendemail();


                                }
                            else Toast.makeText(Registration.this, "Registration Not Successful",Toast.LENGTH_SHORT).show();
                        }
                    });
            }}
        });

    }


    private void sendemail(){

    FirebaseUser firebaseUser= firebaseAuth.getCurrentUser();
    if(firebaseUser!=null)
    {
        firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    Toast.makeText(Registration.this,"Successfuly Registered, Verification mail has been sent",Toast.LENGTH_SHORT).show();
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(Registration.this, MainActivity.class));
                }
                else{
                    Toast.makeText(Registration.this, "Something gone bad",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    }

    private Boolean validate() {

    boolean result=false;
    String name=username.getText().toString();
    String password=userpassword.getText().toString();
    String mail=usermail.getText().toString();

    if((name.isEmpty()) || (password.isEmpty()) || (mail.isEmpty()))
        Toast.makeText(getApplicationContext(),"One of the given field is empty", Toast.LENGTH_SHORT).show();
    else
    {result=true;}

    return result;


    }}
