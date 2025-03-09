package com.example.smartmeter;

import static androidx.core.content.ContentProviderCompat.requireContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultListener;
import com.razorpay.PaymentResultWithDataListener;

public class HomeActivity extends AppCompatActivity {

    private static final String ROOT_FRAGMENT_TAG = "root_fragment";
    BottomNavigationView navigationView;

    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser user;
    String uid;
    Boolean flag=false;
    String oldPass="";

    EditText edtoldPass,edtnewPass;
    Button btnSet;

    UploadToFirebase upload;
    String oldPwd;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        database=FirebaseDatabase.getInstance();
        reference=database.getReference("Users");
        auth=FirebaseAuth.getInstance();

        if(auth!=null){
            user= auth.getCurrentUser();
        }
        if(user!=null){
            uid=user.getUid();
        }

        reference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    DataSnapshot flagSnap=snapshot.child("flag");
                    DataSnapshot pwdSnap=snapshot.child("pass");

                    if(flagSnap.exists()){
                        flag=flagSnap.getValue(Boolean.class);
                    }
                    if(pwdSnap.exists()){
                        oldPwd=pwdSnap.getValue(String.class);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if(flag==false){
            showPopUp();
        }

        navigationView=(BottomNavigationView) findViewById(R.id.bottomNav);

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId=item.getItemId();
                if(itemId==R.id.nav_home){
                    loadFragment(new HomeFragment(),true);
                }else if(itemId==R.id.nav_control){
                    loadFragment(new ControlFragment(),false);
                }
                //else if(itemId==R.id.nav_monitor){
                    //loadFragment(new MonitorFragment(),false);
              //  }
                else if(itemId==R.id.nav_setemail){
                    loadFragment(new SetEmailFragment(),false);
                }else if(itemId==R.id.nav_logout){
                    FirebaseAuth.getInstance().signOut();
                    Intent i=new Intent(getApplicationContext(), UserLogin.class);
                    startActivity(i);
                    finish();
                    Toast.makeText(HomeActivity.this, "User Logged out", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        navigationView.setSelectedItemId(R.id.nav_home);
    }

    private void showPopUp() {
        final Dialog dialog=new Dialog(getApplicationContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_layout_pass);

        edtoldPass=(EditText)dialog.findViewById(R.id.edtoldPass);
        edtnewPass=(EditText)dialog.findViewById(R.id.edtNewPass);
        btnSet=(Button)dialog.findViewById(R.id.btnSave);

        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPwd=edtoldPass.getText().toString();
                String newPass=edtnewPass.getText().toString();

                if(!oldPass.equals(oldPwd)){
                    Toast.makeText(HomeActivity.this, "Enter correct old Password", Toast.LENGTH_SHORT).show();
                    edtoldPass.setText("");
                    edtnewPass.setText("");
                }else{
                    Boolean status=true;
                }

            }
        });
    }

    public void loadFragment(Fragment fragment,Boolean flag){
        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        if(flag){
            transaction.add(R.id.container,fragment);
            manager.popBackStack(ROOT_FRAGMENT_TAG,FragmentManager.POP_BACK_STACK_INCLUSIVE);
            transaction.addToBackStack(ROOT_FRAGMENT_TAG);
        }

        else{
            transaction.replace(R.id.container,fragment);
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }


//    @Override
//    public void onPaymentSuccess(String s, PaymentData paymentData) {
//        try {
//            SetEmailFragment paymentFragment=(SetEmailFragment) mViewPager.getAdapter().instantiateItem(mViewPager,mViewPager.getCurrentItem());
//            paymentFragment.checkRazorResponse(paymentData,true);
//        }catch (Exception e){
//
//        }
//        Toast.makeText(this, "Payment Success"+s, Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onPaymentError(int i, String s, PaymentData paymentData) {
//        Toast.makeText(this, "Failed payment"+s, Toast.LENGTH_SHORT).show();
//    }
}