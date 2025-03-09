package com.example.smartmeteradmin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class planAdapter extends RecyclerView.Adapter<planAdapter.PlanViewHolder> {

    private List<Plan> planList;
    private OnItemClickListener onItemClickListener;

    public planAdapter(List<Plan> planList, OnItemClickListener onItemClickListener) {
        this.planList = planList;
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(Plan plan);
    }


    @NonNull
    @Override
    public PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plan,parent,false);
        return new PlanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanViewHolder holder, int position) {
        Plan plan=planList.get(position);
        holder.bind(plan);

    }

    @Override
    public int getItemCount() {
        return planList.size();
    }

    public class PlanViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView txvPlanName,txvAmount,txvUnits;
        public PlanViewHolder(@NonNull View itemView) {
            super(itemView);

            txvPlanName=itemView.findViewById(R.id.txvPlanName);
            txvAmount=itemView.findViewById(R.id.txvAmount);
            txvUnits=itemView.findViewById(R.id.txvUnit);

            itemView.setOnClickListener(this);
        }

        public void bind(Plan plan) {
            txvPlanName.setText(plan.getPlanName());
            txvUnits.setText(Float.toString(plan.getUnits()));
            txvAmount.setText(Float.toString(plan.getAmount()));
        }

        @Override
        public void onClick(View view) {
            int position=getAdapterPosition();
            if(position!=RecyclerView.NO_POSITION){
                onItemClickListener.onItemClick(planList.get(position));
            }

        }
    }
}
