package com.example.smartmeter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ControlFragment extends Fragment {

    SwitchCompat switchButton1,switchButton2,switchButton3,switchButton4;
    ImageView imgv;
    public String D1,D2,D3,D4;
    FirebaseDatabase database;
    private FirebaseAuth auth;
    String uid;
    DatabaseReference reference,referencebtn1,referencebtn2,referencebtn3,referencebtn4;



    public ControlFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_control, container, false);


        auth=FirebaseAuth.getInstance();
        FirebaseUser user=auth.getCurrentUser();
        if(user!=null){
            uid=user.getUid();
            reference=FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Devices");
        }
        switchButton1=view.findViewById(R.id.switchbtn1);
        switchButton2=view.findViewById(R.id.switchbtn2);
        switchButton3=view.findViewById(R.id.switchbtn3);
        switchButton4=view.findViewById(R.id.switchbtn4);

        switchButton1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sendSwitchValue("Device 1",b);
            }
        });
        switchButton2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sendSwitchValue("Device 2",b);
            }
        });
        switchButton3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sendSwitchValue("Device 3",b);
            }
        });
        switchButton4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sendSwitchValue("Device 4",b);
            }
        });
        referencebtn1=FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Devices").child("Device 1");
        referencebtn2=FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Devices").child("Device 2");
        referencebtn3=FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Devices").child("Device 3");
        referencebtn4=FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Devices").child("Device 4");

        referencebtn1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String switchValue=snapshot.getValue(String.class);

                    boolean isChecked=switchValue.equals("ON");
                    switchButton1.setChecked(isChecked);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        referencebtn2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String switchValue=snapshot.getValue(String.class);

                    boolean isChecked=switchValue.equals("ON");
                    switchButton2.setChecked(isChecked);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        referencebtn3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String switchValue=snapshot.getValue(String.class);

                    boolean isChecked=switchValue.equals("ON");
                    switchButton3.setChecked(isChecked);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        referencebtn4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String switchValue=snapshot.getValue(String.class);

                    boolean isChecked=switchValue.equals("ON");
                    switchButton4.setChecked(isChecked);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    private void sendSwitchValue(String device1, boolean b) {
        if(reference!=null){
            if(b==true){
                reference.child(device1).setValue("ON");
            }
            if(b==false){
                reference.child(device1).setValue("OFF");
            }

        }
    }
}