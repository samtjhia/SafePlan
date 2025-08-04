package com.b07safetyplanapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TipAdapter extends RecyclerView.Adapter<TipAdapter.TipViewHolder> {

    private List<String> tipList;

    public TipAdapter(List<String> tipList) {
        this.tipList = tipList;
    }

    @NonNull
    @Override
    public TipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tip, parent, false);
        return new TipViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TipAdapter.TipViewHolder holder, int position) {
        String tip = tipList.get(position);
        holder.tipTextView.setText(tip);
    }

    @Override
    public int getItemCount() {
        return tipList.size();
    }


    public static class TipViewHolder extends RecyclerView.ViewHolder {
        TextView tipTextView;

        public TipViewHolder(@NonNull View itemView) {
            super(itemView);
            tipTextView = itemView.findViewById(R.id.textViewTip);
        }
    }
}
