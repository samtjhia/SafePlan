package com.b07safetyplanapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.b07safetyplanapp.models.emergencyinfo.EmergencyContact;

import java.util.List;

/**
 * RecyclerView adapter for displaying a list of emergency contacts.
 * <p>
 * Supports editing and deleting contacts using listener callbacks.
 */
public class EmergencyContactAdapter extends RecyclerView.Adapter<EmergencyContactAdapter.ViewHolder> {

    private List<EmergencyContact> contacts;
    private OnItemClickListener editListener;
    private OnItemClickListener deleteListener;

    /**
     * Interface for handling click events on contact items.
     */
    public interface OnItemClickListener {
        /**
         * Called when a contact item is clicked.
         *
         * @param contact the clicked emergency contact
         */
        void onClick(EmergencyContact contact);
    }

    /**
     * Constructs a new EmergencyContactAdapter.
     *
     * @param contacts       the list of emergency contacts to display
     * @param editListener   listener for editing a contact
     * @param deleteListener listener for deleting a contact
     */
    public EmergencyContactAdapter(List<EmergencyContact> contacts,
                                   OnItemClickListener editListener,
                                   OnItemClickListener deleteListener) {
        this.contacts = contacts;
        this.editListener = editListener;
        this.deleteListener = deleteListener;
    }

    /**
     * ViewHolder for emergency contact item layout.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameText;
        private final TextView relationshipText;
        private final TextView phoneText;
        private final ImageButton editButton;
        private final ImageButton deleteButton;


        /**
         * Initializes the view holder and binds views.
         *
         * @param itemView the inflated item view
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.tvContactName);
            relationshipText = itemView.findViewById(R.id.tvContactRelationship);
            phoneText = itemView.findViewById(R.id.tvContactPhone);
            editButton = itemView.findViewById(R.id.btnEditContact);
            deleteButton = itemView.findViewById(R.id.btnDeleteContact);
        }

        /** @return the TextView displaying the contact's name */
        public TextView getNameText() {
            return nameText;
        }

        /** @return the TextView displaying the contact's relationship */
        public TextView getRelationshipText() {
            return relationshipText;
        }

        /** @return the TextView displaying the contact's phone number */
        public TextView getPhoneText() {
            return phoneText;
        }

        /** @return the ImageButton for editing the contact */
        public ImageButton getEditButton() {
            return editButton;
        }

        /** @return the ImageButton for deleting the contact */
        public ImageButton getDeleteButton() {
            return deleteButton;
        }
    }


    /**
     * Inflates the contact item layout and creates a new ViewHolder.
     *
     * @param parent   the parent ViewGroup
     * @param viewType the type of view
     * @return a new ViewHolder instance
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_emergency_contact, parent, false);
        return new ViewHolder(view);
    }


    /**
     * Binds a contact item to the ViewHolder.
     *
     * @param holder   the ViewHolder to bind data to
     * @param position the position of the contact in the list
     */
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

    /**
     * Returns the total number of contacts in the list.
     *
     * @return the item count
     */
    @Override
    public int getItemCount() {
        return contacts.size();
    }
}