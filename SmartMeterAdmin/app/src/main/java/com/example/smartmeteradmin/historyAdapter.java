package com.example.smartmeteradmin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class historyAdapter extends RecyclerView.Adapter<historyAdapter.UserHistoryViewHolder> {
    private List<User> userList;
    private OnItemClickListener onItemClickListener;

    public historyAdapter(List<User> userList,OnItemClickListener onItemClickListener) {
        this.userList = userList;
        this.onItemClickListener = onItemClickListener;
    }
    public interface OnItemClickListener{
        void onItemClick(User user);
    }

    @NonNull
    @Override
    public UserHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history,parent,false);
        return new UserHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHistoryViewHolder holder, int position) {
        User user=userList.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class UserHistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView txvName,txvAmount,txvUnits;

        public UserHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            txvName=(TextView) itemView.findViewById(R.id.txvName);
            txvAmount=(TextView) itemView.findViewById(R.id.txvAmountH);
            txvUnits=(TextView) itemView.findViewById(R.id.txvUnitH);

            itemView.setOnClickListener(this);
        }

        public void bind(User user){
            Double totalUnitss= Double.parseDouble(user.getTotalUnit());
            txvName.setText(user.getName());
            txvUnits.setText(String.valueOf(user.getTotalUnit()));
            txvAmount.setText(String.valueOf(totalUnitss*12));//12RS is condifrred as price per kW
        }

        @Override
        public void onClick(View view) {
            int positin=getAdapterPosition();
            if(positin!=RecyclerView.NO_POSITION){
                onItemClickListener.onItemClick(userList.get(positin));
            }
        }
    }


}
