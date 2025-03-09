package com.example.smartmeteradmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AdminLogin extends AppCompatActivity {

    private EditText edtUName,edtPass;
    private Button btnLogin;
    private FirebaseAuth auth=FirebaseAuth.getInstance();
    private TextView txvnotAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        edtUName=findViewById(R.id.edtUsername);
        edtPass=findViewById(R.id.edtPass);

        txvnotAccount=findViewById(R.id.txvnotLogin);

        btnLogin=findViewById(R.id.btnLogin);

        auth=FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String UName=edtUName.getText().toString().trim();
                String Pass=edtPass.getText().toString().trim();

                if(!UName.isEmpty()&& Patterns.EMAIL_ADDRESS.matcher(UName).matches()){
                    if(!Pass.isEmpty()){
                        auth.signInWithEmailAndPassword(UName,Pass)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                                        Intent i=new Intent(getApplicationContext(), AdminHome.class);
                                        startActivity(i);
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                    else {
                        edtPass.setError("Please enter Password");
                    }
                } else {
                    edtUName.setError("Please enter Username");
                }
            }
        });

        txvnotAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(), AdminRegister.class);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        FirebaseUser currentUser= auth.getCurrentUser();
        if(currentUser!=null){
            Intent i=new Intent(getApplicationContext(),AdminHome.class);
            startActivity(i);
            finish();
            Toast.makeText(getApplicationContext(), "Already logged in", Toast.LENGTH_SHORT).show();
        }
        super.onStart();
    }
}