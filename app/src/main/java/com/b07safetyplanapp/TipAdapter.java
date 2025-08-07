package com.b07safetyplanapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.b07safetyplanapp.models.questionnaire.Tip;

/**
 * TipAdapter is a RecyclerView.Adapter used to bind a list of Tip objects
 * to the views defined in item_tip.xml layout. It displays safety tips
 * with a title and body text for each item.
 */
public class TipAdapter extends RecyclerView.Adapter<TipAdapter.TipViewHolder> {

    private List<Tip> tipList;

    /**
     * Constructs a new TipAdapter with a list of tips to display.
     *
     * @param tipList List of Tip objects to be rendered in the RecyclerView.
     */
    public TipAdapter(List<Tip> tipList) {
        this.tipList = tipList;
    }

    /**
     * Inflates the item layout (item_tip) and returns a new TipViewHolder.
     *
     * @param parent   The parent ViewGroup.
     * @param viewType The view type of the new View.
     * @return A new TipViewHolder that holds the inflated layout.
     */
    @NonNull
    @Override
    public TipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tip, parent, false);
        return new TipViewHolder(view);
    }

    /**
     * Binds the Tip data (title and body) to the corresponding views in the ViewHolder.
     *
     * @param holder   The TipViewHolder containing the views.
     * @param position The position of the item in the tipList.
     */
    @Override
    public void onBindViewHolder(@NonNull TipViewHolder holder, int position) {
        Tip tip = tipList.get(position);
        holder.tipTitle.setText(tip.getTitle());
        holder.tipBody.setText(tip.getBody());
    }

    /**
     * Returns the total number of items in the tip list.
     *
     * @return Size of the tip list.
     */
    @Override
    public int getItemCount() {
        return tipList.size();
    }

    /**
     * TipViewHolder holds the views (TextViews) for each tip item.
     * It represents each individual list item in the RecyclerView.
     */
    public static class TipViewHolder extends RecyclerView.ViewHolder {
        TextView tipTitle;
        TextView tipBody;

        /**
         * Initializes the views from the layout.
         *
         * @param itemView The view representing a single tip item.
         */
        public TipViewHolder(@NonNull View itemView) {
            super(itemView);
            tipTitle = itemView.findViewById(R.id.tipTitle);
            tipBody = itemView.findViewById(R.id.tipBody);
        }
    }
}
