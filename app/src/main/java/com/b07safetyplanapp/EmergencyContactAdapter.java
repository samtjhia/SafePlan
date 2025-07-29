package com.b07safetyplanapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.b07safetyplanapp.models.emergencyinfo.EmergencyContact;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EmergencyContactAdapter extends RecyclerView.Adapter<EmergencyContactAdapter.ViewHolder> {

    private List<EmergencyContact> contacts;
    private OnItemClickListener clickListener;
    private OnItemClickListener editListener;
    private OnItemClickListener deleteListener;

    public interface OnItemClickListener {
        void onClick(EmergencyContact contact);
    }

    public EmergencyContactAdapter(List<EmergencyContact> contacts,
                                   OnItemClickListener clickListener,
                                   OnItemClickListener editListener,
                                   OnItemClickListener deleteListener) {
        this.contacts = contacts;
        this.clickListener = clickListener;
        this.editListener = editListener;
        this.deleteListener = deleteListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameText;
        private final TextView relationshipText;
        private final TextView phoneText;
//        private final TextView dateText;
        private final ImageButton editButton;
        private final ImageButton deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.tvContactName);
            relationshipText = itemView.findViewById(R.id.tvContactRelationship);
            phoneText = itemView.findViewById(R.id.tvContactPhone);
            editButton = itemView.findViewById(R.id.btnEditContact);
            deleteButton = itemView.findViewById(R.id.btnDeleteContact);
        }

        public TextView getNameText() {
            return nameText;
        }

        public TextView getRelationshipText() {
            return relationshipText;
        }

        public TextView getPhoneText() {
            return phoneText;
        }

        public ImageButton getEditButton() {
            return editButton;
        }

        public ImageButton getDeleteButton() {
            return deleteButton;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_emergency_contact, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EmergencyContact contact = contacts.get(position);

        holder.getNameText().setText(contact.getName());
        holder.getPhoneText().setText(contact.getPhone());

        String relationship = contact.getRelationship();
        if (relationship != null && !relationship.trim().isEmpty()) {
            holder.getRelationshipText().setText(relationship);
            holder.getRelationshipText().setVisibility(View.VISIBLE);
        } else {
            holder.getRelationshipText().setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onClick(contact);
            }
        });

        holder.getEditButton().setOnClickListener(v -> {
            if (editListener != null) {
                editListener.onClick(contact);
            }
        });

        holder.getDeleteButton().setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onClick(contact);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }
}