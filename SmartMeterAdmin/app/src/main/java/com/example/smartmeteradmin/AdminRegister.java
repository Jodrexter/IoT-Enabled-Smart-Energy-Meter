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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminRegister extends AppCompatActivity {

    private EditText edtName,edtMail,edtMobile,edtPass;
    private Button btnSignup;
    private TextView txvalreadyRegistered;

    String name,mail,mobile,Pass;

    UploadToFirebase upload;

    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_register);

        edtName=findViewById(R.id.edtName);
        edtMail=findViewById(R.id.edtEmail);
        edtMobile=findViewById(R.id.edtPhone);
        edtPass=findViewById(R.id.edtPass);

        database=FirebaseDatabase.getInstance();
        reference=database.getReference("Admins");

        auth=FirebaseAuth.getInstance();

        btnSignup=findViewById(R.id.btnSignup);

        txvalreadyRegistered=findViewById(R.id.txvalreadyAccount);



        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name=edtName.getText().toString().trim();
                mail=edtMail.getText().toString().trim();
                mobile=edtMobile.getText().toString().trim();
                Pass=edtPass.getText().toString().trim();



                if(name.isEmpty()){
                    edtName.setError("Enter valid Name");
                }
                if(mail.isEmpty() && !Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
                    edtMail.setError("Enter valid Mail");
                }
                if(mobile.isEmpty()){
                    edtMobile.setError("Enter a valid Username");
                }
                if(Pass.isEmpty()){
                    edtPass.setError("Please enter Password");
                }
                else {
                    auth.createUserWithEmailAndPassword(mail,Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                FirebaseUser user=auth.getCurrentUser();
                                if(user!=null){
                                    String uid=user.getUid();
                                    upload=new UploadToFirebase(name,mobile,mail,Pass);
                                    reference.child(uid).setValue(upload);
                                }
                                Toast.makeText(getApplicationContext(), "Signup Successful", Toast.LENGTH_SHORT).show();
                                Intent i=new Intent(getApplicationContext(), AdminLogin.class);
                                startActivity(i);
                            }else {
                                Toast.makeText(getApplicationContext(), "Signup Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });

        txvalreadyRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(), AdminLogin.class);
                startActivity(i);
            }
        });
    }
}