package com.example.smartmeteradmin;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class AdminHistoryFragment extends Fragment implements userAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private userAdapter userAdapter;
    private User user;
    List<User> userList;

    FirebaseDatabase database;
    DatabaseReference reference;

    String totalUnit;

    Double TotalUnit=0.0;

    private TextView tAmount,tWatt;

    public AdminHistoryFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_admin_history, container, false);

        tAmount=(TextView)view.findViewById(R.id.totalAmount);
        tWatt=(TextView)view.findViewById(R.id.totalWatts);

        database=FirebaseDatabase.getInstance();
        reference=FirebaseDatabase.getInstance().getReference("Users");
        
        recyclerView=(RecyclerView) view.findViewById(R.id.recyclerViewHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));



        loadUsers();

        tAmount.setText(String.valueOf(TotalUnit*12));
        tWatt.setText(String.valueOf(TotalUnit));

        userList=new ArrayList<>();
        userAdapter=new userAdapter(userList,this);
        recyclerView.setAdapter(userAdapter);


        return view;
    }

    private void loadUsers() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Double TootalUnit=0.0;
                if(snapshot.exists()){
                    userList.clear();
                    for(DataSnapshot usrSnap: snapshot.getChildren()){
                        if(usrSnap.exists()&&usrSnap.hasChild("Readings")){
                            DataSnapshot readingsSnap=usrSnap.child("Readings");
                            String name=usrSnap.child("name").getValue(String.class);
                            if(readingsSnap.exists()&&readingsSnap.hasChild("TotalUnitsConsumed")){
                                totalUnit=readingsSnap.child("TotalUnitsConsumed").getValue(String.class);
                            }

                            if (name != null && totalUnit != null && !name.equals("") && !totalUnit.equals("")) {
                                user = new User(name, totalUnit, Double.parseDouble(totalUnit));
                                TootalUnit+=Double.parseDouble(totalUnit);

                                TotalUnit=TootalUnit;
                                userList.add(user);

                            }
                        }

                    }
                }
                userAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onItemClick(User user) {
        showPrompDialog(user);
    }

    private void showPrompDialog(User user) {
        AlertDialog.Builder builder=new AlertDialog.Builder(requireContext(), com.google.android.material.R.style.Theme_Material3_DayNight_Dialog_Alert);
        builder.setTitle("User Information")
                .setIcon(R.drawable.person)
                .setMessage("Name: "+user.getName()+"\nTotal Unit: "+user.getTotalUnit()+"\nCost: "+user.gettUnit()*12)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }
}