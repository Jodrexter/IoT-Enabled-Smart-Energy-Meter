package com.example.smartmeter;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class Payment extends AppCompatActivity implements PaymentResultListener {
    private final Activity mActivity;
    int amount;
    String planName;
    float planUnits,rechargeUnits;

    FirebaseDatabase database;
    DatabaseReference usrRef;
    String uid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Checkout.preload(getApplicationContext());

        database=FirebaseDatabase.getInstance();
        usrRef=database.getReference("Users");
    }

    public Payment(Activity activity, int amount, float planUnits, String planName, String uid, Float rechargeUnits) {
        this.mActivity = activity;
        this.amount = amount;
        this.planName = planName;
        this.planUnits = planUnits;

        this.uid=uid;
        this.rechargeUnits=rechargeUnits;


    }


    public void startPayment() {

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

            checkout.open(mActivity,object);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(getApplicationContext(), "Heyyyyyy", Toast.LENGTH_SHORT).show();
        int rechargeAmt= (int) (rechargeUnits+planUnits);
        usrRef.child(uid+"/Readings/rechargeUnits").setValue(rechargeAmt);
        usrRef.child(uid+"/Readings/Status").setValue("true");
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(getApplicationContext(), "Payment Failed: "+s, Toast.LENGTH_SHORT).show();
    }
}
