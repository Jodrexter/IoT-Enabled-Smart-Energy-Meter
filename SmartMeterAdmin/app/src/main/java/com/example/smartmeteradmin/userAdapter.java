package com.example.smartmeteradmin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class userAdapter extends RecyclerView.Adapter<userAdapter.UserViewHolder> {

    private List<User> userList;
    private OnItemClickListener onItemClickListener;

    public userAdapter(List<User> userList, OnItemClickListener onItemClickListener) {
        this.userList = userList;
        this.onItemClickListener = onItemClickListener;
    }
    public interface OnItemClickListener{
        void onItemClick(User user);
    }

    @NonNull
    @Override
    public userAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user,parent,false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull userAdapter.UserViewHolder holder, int position) {

        User user=userList.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txvUserName,txvUserMail;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            txvUserName=(TextView) itemView.findViewById(R.id.txvUserName);
            txvUserMail=(TextView) itemView.findViewById(R.id.txvUserMail);

            itemView.setOnClickListener(this);
        }

        public void bind(User user) {
            txvUserName.setText(user.getName());
            txvUserMail.setText(user.getMail());
        }

        @Override
        public void onClick(View view) {
            int position=getAdapterPosition();
            if(position!=RecyclerView.NO_POSITION){
                onItemClickListener.onItemClick(userList.get(position));
            }
        }
    }
}
