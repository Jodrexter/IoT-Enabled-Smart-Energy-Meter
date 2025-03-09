package com.example.smartmeteradmin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.Patterns;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminHomeFragment extends Fragment implements userAdapter.OnItemClickListener {
    FloatingActionButton actionButton,fabLogout;

    String pass="";

    EditText edtUName,edtUMobile,edtUEmail,edtUDeviceN;

    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth auth;

    UploadData upload;

    RecyclerView recyclerView;
    List<User> userList;
    private userAdapter userAdapter;

    User user;
    public AdminHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_admin_home, container, false);

        database=FirebaseDatabase.getInstance();
        reference=FirebaseDatabase.getInstance().getReference("Users");

        actionButton=(FloatingActionButton) view.findViewById(R.id.fabAddUser);
        fabLogout=(FloatingActionButton) view.findViewById(R.id.fabLogout);


        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopUp();
            }
        });
        fabLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent i=new Intent(view.getContext(), AdminLogin.class);
                startActivity(i);
                getActivity().finishAffinity();
                Toast.makeText(view.getContext(), "User Logged out", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView=(RecyclerView) view.findViewById(R.id.recyleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        loadUsers();

        userList=new ArrayList<>();
        userAdapter=new userAdapter(userList,this);
        recyclerView.setAdapter(userAdapter);

        return view;
    }

    private void loadUsers() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    userList.clear();
                    for(DataSnapshot usrSnap: snapshot.getChildren()){
                        String name=usrSnap.child("name").getValue(String.class);
                        String email=usrSnap.child("email").getValue(String.class);
                        String mobile=usrSnap.child("mobile").getValue(String.class);
                        String pass=usrSnap.child("pass").getValue(String.class);

                        user=new User(name,email,mobile,pass);
                        userList.add(user);
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        database=FirebaseDatabase.getInstance();
        reference= database.getReference("Users");

        auth=FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
    }

    private void showPopUp() {
        final Dialog dialog=new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_layout);

        edtUName=(EditText) dialog.findViewById(R.id.edtUName);
        edtUEmail=(EditText) dialog.findViewById(R.id.edtUEmail);
        edtUMobile=(EditText) dialog.findViewById(R.id.edtUMobile);
        edtUDeviceN=(EditText) dialog.findViewById(R.id.edtUDeviceNo);



        Button btnSubmit=dialog.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=edtUName.getText().toString();
                String mobile=edtUMobile.getText().toString();
                String email=edtUEmail.getText().toString();
                String dNo=edtUDeviceN.getText().toString();
                pass="asdf@123";
                Boolean flag=false;

                if(name.isEmpty()){
                    edtUName.setError("Please Enter Name");
                }
                if(mobile.isEmpty()){
                    edtUMobile.setError("Please enter Mobile number");
                }
                if(dNo.isEmpty()){
                    edtUDeviceN.setError("Please enter Device Number");
                }
                if(email.isEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    edtUEmail.setError("Please enter a valid Email");
                }else {
                    auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                FirebaseUser user=auth.getCurrentUser();
                                if(user!=null){
                                    String uid=user.getUid();

                                    upload=new UploadData(name,email,mobile,pass,dNo,flag);
                                    reference.child(uid).setValue(upload);
                                }
                                Toast.makeText(getContext(), "Signup Successful", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }else {
                                Toast.makeText(getContext(), "Signup Failed", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }

            }
        });

        dialog.show();
    }

    @Override
    public void onItemClick(User user) {
        showPromptDialog(user);
    }

    private void showPromptDialog(User user) {
        AlertDialog.Builder builder=new AlertDialog.Builder(requireContext());
        builder.setTitle("User Details")
                .setMessage("Name: "+user.getName()+"\nMobile No.: "+user.getMobile()+"\nEmail: "+user.getMail()+"\nPassword: "+user.getPass())
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteUser(user);
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

    private void deleteUser(User user) {
        AlertDialog.Builder builder=new AlertDialog.Builder(requireContext());
        builder.setTitle("Confirm User Deletion")
                .setMessage("Are you want to permanently delete "+user.getName()+" ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseAuth.getInstance().signInWithEmailAndPassword(user.getMail(),user.getPass()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    FirebaseUser currentUser=FirebaseAuth.getInstance().getCurrentUser();
                                    if(currentUser!=null){
                                        String uid= currentUser.getUid();
                                        currentUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    removeFromDataset(user.getMail());
                                                    DatabaseReference dbRef=FirebaseDatabase.getInstance().getReference("Users").child(uid);
                                                    dbRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()){
                                                                Toast.makeText(requireContext(), "User Deleted successfully", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    }
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

    private void removeFromDataset(String mailT) {
        for(int i=0;i< userList.size();i++){
            if(userList.get(i).getMail().equals(mailT)){
                userList.remove(i);
                userAdapter.notifyItemRemoved(i);
                break;
            }
        }
    }
}