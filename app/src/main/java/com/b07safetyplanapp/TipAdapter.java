package com.b07safetyplanapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.b07safetyplanapp.models.questionnaire.Tip;

public class TipAdapter extends RecyclerView.Adapter<TipAdapter.TipViewHolder> {

    private List<Tip> tipList;

    public TipAdapter(List<Tip> tipList) {
        this.tipList = tipList;
    }

    @NonNull
    @Override
    public TipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tip, parent, false);
        return new TipViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TipViewHolder holder, int position) {
        Tip tip = tipList.get(position);
        holder.tipTitle.setText(tip.getTitle());
        holder.tipBody.setText(tip.getBody());
    }

    @Override
    public int getItemCount() {
        return tipList.size();
    }

    public static class TipViewHolder extends RecyclerView.ViewHolder {
        TextView tipTitle;
        TextView tipBody;

        public TipViewHolder(@NonNull View itemView) {
            super(itemView);
            tipTitle = itemView.findViewById(R.id.tipTitle);
            tipBody = itemView.findViewById(R.id.tipBody);
        }
    }
}
