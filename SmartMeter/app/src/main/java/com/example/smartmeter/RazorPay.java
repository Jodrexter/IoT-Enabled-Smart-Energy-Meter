package com.example.smartmeter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class RazorPay extends AppCompatActivity implements PaymentResultListener {
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser user;
    String uid;
    Float rechageUnits=0.0f;

    int amount;
    String planName;
    float planUnits;

    public RazorPay(int amount, String planName, float planUnits) {
        this.amount = amount;
        this.planName = planName;
        this.planUnits = planUnits;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                if(snapshot.exists()&&snapshot.hasChild("rechargeUnits")){
                    rechageUnits=snapshot.child("rechargeUnits").getValue(Float.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Checkout.preload(getApplicationContext());
        makePayment();
    }

    private void makePayment() {
        Checkout checkout=new Checkout();

//        checkout.setPaymentResultWithDataListener(this);
        checkout.setKeyID("rzp_test_sSWPKQfU3LiEDi");
        checkout.setImage(R.drawable.email);


        JSONObject object=new JSONObject();
        try{
            object.put("name","IoT based Smart Electricity Meter");
            object.put("description","Plan Name: "+planName+" & Total units: "+planUnits);
            object.put("theme.color","");
            object.put("currency","INR");
            object.put("amount",amount);

            checkout.open(this,object);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        int rechargeAmt= (int) (rechageUnits+planUnits);
        Log.d("TAG", "onPaymentSuccess: Total Units:="+rechargeAmt);
        reference.child(uid+"/Readings/rechargeUnits").setValue(rechargeAmt);
        reference.child(uid+"/Readings/Status").setValue("true");
        Toast.makeText(getApplicationContext(), "Payment Success", Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(), "Payment ID: "+s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
    }
}