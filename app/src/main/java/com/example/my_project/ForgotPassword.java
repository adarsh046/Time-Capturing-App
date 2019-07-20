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
import com.google.firebase.auth.FirebaseAuth;

import java.util.MissingFormatArgumentException;

public class ForgotPassword extends AppCompatActivity {

    private EditText email;
    private Button reset;
    private FirebaseAuth firebaseAuth;
    private TextView goback;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        email=(EditText)findViewById(R.id.passemail);
        reset=(Button)findViewById(R.id.btnpassreset);
        firebaseAuth=FirebaseAuth.getInstance();
        goback=(TextView) findViewById(R.id.tvgo);

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                startActivity(new Intent(ForgotPassword.this, MainActivity.class));

            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            String usere= email.getText().toString().trim();

            if(usere.isEmpty()){

                Toast.makeText(ForgotPassword.this, "Enter Registered E-Mail",Toast.LENGTH_SHORT).show();
            }
            else{

                firebaseAuth.sendPasswordResetEmail(usere).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){

                            Toast.makeText(ForgotPassword.this, "Password E-Mail sent", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(ForgotPassword.this, MainActivity.class));

                        }
                        else{

                            Toast.makeText(ForgotPassword.this, "Something gone bad", Toast.LENGTH_SHORT).show();

                        }

                    }
                });

            }
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ForgotPassword.this, MainActivity.class));
    }
}
