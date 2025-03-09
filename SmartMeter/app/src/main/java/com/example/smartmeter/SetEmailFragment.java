package com.example.smartmeter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultListener;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
public class SetEmailFragment extends Fragment implements planAdapter.OnItemClickListener,PaymentResultWithDataListener {

    private RecyclerView recyclerView;
    FirebaseDatabase database;
    DatabaseReference reference,usrRef;
    List<Plan> planList;
    private planAdapter planAdapter;
    Plan plan;
    FirebaseAuth auth;
    FirebaseUser user;
    String uid="";
    Double balance,rechargeUnits=0.0;
    String rechrgeStatus="";
    Plan planG;
    String bal="";

    private Payment pay;
    private RazorPay rPay;
    TextView txvBalance;



    public SetEmailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(isAdded()&&getContext()!=null){
            // Inflate the layout for this fragment
            View view= inflater.inflate(R.layout.fragment_set_email, container, false);


//        Checkout.preload(requireContext());

            database=FirebaseDatabase.getInstance();
            reference=database.getReference("Plans");
            usrRef=database.getReference("Users");

            txvBalance=(TextView)view.findViewById(R.id.txvBalance);

            auth=FirebaseAuth.getInstance();
            if (auth != null) {
                user= auth.getCurrentUser();
            }
            if(user!=null){
                uid= user.getUid();
            }
            usrRef.child(uid).child("Readings").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        Log.d("TAG", "onDataChange: Direct Balance: "+String.valueOf(snapshot.child("Balance").getValue(Double.class)));
                        Log.d("TAG", "loadPlans: String balance: "+bal);
                        bal=String.valueOf(snapshot.child("Balance").getValue(Double.class));
                        txvBalance.setText(bal+" Units");
                        balance=snapshot.child("Balance").getValue(Double.class);
                        rechargeUnits=snapshot.child("rechargeUnits").getValue(Double.class);
                        rechrgeStatus=snapshot.child("Status").getValue(String.class);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            Log.d("TAG", "onCreateView: Double balance: "+balance);



            recyclerView=(RecyclerView) view.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            planList=new ArrayList<>();
            planAdapter=new planAdapter(planList,this);
            recyclerView.setAdapter(planAdapter);

            loadPlans();


            return view;
        }else{
            return super.onCreateView(inflater, container, savedInstanceState);
        }

    }

    private void loadPlans() {

        planList.clear();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    planList.clear();
                    for(DataSnapshot planSnap: snapshot.getChildren()){
                        String planName=planSnap.child("planName").getValue(String.class);
                        float units=planSnap.child("units").getValue(Float.class);
                        float amount=planSnap.child("amount").getValue(Float.class);

                        plan=new Plan(planName,units,amount);
                        planList.add(plan);
                    }
                }
                planAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onItemClick(Plan plan) {

        Float samount= plan.getAmount();
        int amount=Math.round(samount*100);
//        rPay=new RazorPay(amount,plan.getPlanName(),plan.getUnits());
//        Intent i=new Intent(requireActivity(),RazorPay.class);
//        startActivity(i);
//        pay=new Payment(requireActivity(),amount,plan.getUnits(),plan.getPlanName(),uid,rechargeUnits);
//        pay.startPayment();

        Checkout checkout=new Checkout();

//        checkout.setPaymentResultWithDataListener(this);
        checkout.setKeyID("rzp_test_sSWPKQfU3LiEDi");
        checkout.setImage(R.drawable.email);


        JSONObject object=new JSONObject();
        try{
            object.put("name","IoT based Smart Electricity Meter");
            object.put("description","Plan Name: "+plan.getPlanName()+" & Total units: "+plan.getUnits());
            object.put("theme.color","");
            object.put("currency","INR");
            object.put("amount",amount);

            checkout.open(requireActivity(),object);
            planG=plan;
        }catch (Exception e){
            e.printStackTrace();
        }

    }



    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        Log.d("TAG", "onPaymentSuccess: Inside Payment Success");
        int rechargeAmt= (int) (rechargeUnits+planG.getUnits());
        Log.d("TAG", "onPaymentSuccess: Total Units:="+rechargeAmt);
        usrRef.child(uid+"/Readings/rechargeUnits").setValue(rechargeAmt);
        usrRef.child(uid+"/Readings/Status").setValue("true");
        Toast.makeText(requireContext(), "Payment Success", Toast.LENGTH_SHORT).show();
        Toast.makeText(requireContext(), "Payment ID: "+s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        Log.d("TAG", "onPaymentError: Payment Error");
        Toast.makeText(requireContext(), "Payment failed", Toast.LENGTH_SHORT).show();
        Toast.makeText(requireContext(), "Reason: "+s, Toast.LENGTH_SHORT).show();
    }



}