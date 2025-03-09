package com.example.smartmeteradmin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class PlanFragment extends Fragment implements planAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    FirebaseDatabase database;
    DatabaseReference reference;
    List<Plan> planList;
    private planAdapter planAdapter;

    FloatingActionButton fabAddPlan;
    private EditText edtPlanName,edtUnits,edtAmount;
    private Button btnAddPlan;
    Plan plan;

    public PlanFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_plan, container, false);

        database=FirebaseDatabase.getInstance();
        reference=database.getReference("Plans");

        fabAddPlan=(FloatingActionButton)view.findViewById(R.id.fabAddPlan);

        recyclerView=(RecyclerView) view.findViewById(R.id.recyclerViewPlan);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        planList=new ArrayList<>();
        planAdapter=new planAdapter(planList,this);
        recyclerView.setAdapter(planAdapter);

        loadPlans();

        fabAddPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopUp();
            }
        });

        return view;
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

    private void showPopUp() {
        final Dialog dialog=new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_plan_layout);

        edtPlanName=(EditText) dialog.findViewById(R.id.edtPlanName);
        edtUnits=(EditText) dialog.findViewById(R.id.edtUnits);
        edtAmount=(EditText) dialog.findViewById(R.id.edtAmount);

        btnAddPlan=(Button) dialog.findViewById(R.id.btnAddPlan);
        btnAddPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float units=0.0f,amount=0.0f;
                String planName=edtPlanName.getText().toString();
                units=Float.parseFloat(edtUnits.getText().toString());
                amount=Float.parseFloat(edtAmount.getText().toString());

                if(planName.isEmpty()){
                    edtPlanName.setError("Please enter plan name");
                }
                if(units==0.0f){
                    edtUnits.setError("Enter energy units");
                }
                if(amount==0.0f){
                    edtAmount.setError("Enter amount");
                }else {
                    plan=new Plan(planName,units,amount);
                    reference.child(planName).setValue(plan);
                    Toast.makeText(requireContext(), "Plan added", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();


    }

    @Override
    public void onItemClick(Plan plan) {
        showPromptDialog(plan);

    }

    private void showPromptDialog(Plan plan) {
        AlertDialog.Builder builder=new AlertDialog.Builder(requireContext());
        builder.setTitle("Plan Details")
                .setMessage("Plan Name: "+plan.getPlanName()+"\nUnits: "+plan.getUnits()+"\tAmount: "+plan.getAmount())
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deletePlan(plan);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    private void deletePlan(Plan plan) {
        AlertDialog.Builder builder=new AlertDialog.Builder(requireContext());
        builder.setTitle("Confirm Plan Delete")
                .setMessage("Are you want to permanently delete "+plan.getPlanName()+" ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        removeFromDataset(plan.getPlanName());
                        reference.child(plan.getPlanName()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(requireContext(), "Plan Deleted Successfully", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    private void removeFromDataset(String planNameT) {
        for(int i=0;i<planList.size();i++){
            if(planList.get(i).getPlanName().equals(planNameT)){
                planList.remove(i);
                planAdapter.notifyItemRemoved(i);
                break;
            }
        }
    }
}