package com.example.smartmeter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class HomeFragment extends Fragment {

    AppCompatTextView txvTemp,txvHumid,txvVoltage,txvCurrent,txvWatt,txvUnits,txvtotalExpense,txvCurrentWatt;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser user;
    String uid;
    double unitPrice=0.0;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);

//        txvHumid=(AppCompatTextView) view.findViewById(R.id.txvHumid);
//        txvTemp=(AppCompatTextView) view.findViewById(R.id.txvTemp);
        txvVoltage=(AppCompatTextView) view.findViewById(R.id.txvVoltage);
        txvCurrent=(AppCompatTextView) view.findViewById(R.id.txvCurrent);
        txvWatt=(AppCompatTextView) view.findViewById(R.id.txvWatt);
        txvUnits=(AppCompatTextView) view.findViewById(R.id.txvUnits);
        txvtotalExpense=(AppCompatTextView) view.findViewById(R.id.totalExpense);
        txvCurrentWatt=(AppCompatTextView) view.findViewById(R.id.txvCurrentWatt);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        database=FirebaseDatabase.getInstance();
        reference=database.getReference("Users");
        auth=FirebaseAuth.getInstance();
        if(auth!=null){
            user= auth.getCurrentUser();
        }
        if(user!=null){
            uid=user.getUid();
        }
        reference.child(uid+"/Readings").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
//                    String temp=snapshot.child("Temp").getValue(String.class);
//                    String humid=snapshot.child("Humid").getValue(String.class);
                    String voltage=snapshot.child("Voltage").getValue(String.class);
                    String current=snapshot.child("Current").getValue(String.class);
                    String watt=snapshot.child("TotalEnergy").getValue(String.class);
                    String units=snapshot.child("TotalUnitsConsumed").getValue(String.class);
                    String currentWatts=snapshot.child("CurrentPower").getValue(String.class);
                    if(units!=null){
                        unitPrice=Double.parseDouble(units)*12;// Here per unit price is assumed as 12RS
                    }


//                    txvTemp.setText(temp);
//                    txvHumid.setText(humid);
                    txvCurrent.setText(current);
                    txvVoltage.setText(voltage);
                    txvWatt.setText(watt);
                    txvUnits.setText(units);
                    txvCurrentWatt.setText(currentWatts);
                    txvtotalExpense.setText(Double.toString(unitPrice));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        super.onCreate(savedInstanceState);
    }
}