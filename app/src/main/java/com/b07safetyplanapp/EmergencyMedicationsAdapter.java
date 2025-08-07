
package com.b07safetyplanapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.b07safetyplanapp.models.emergencyinfo.Medication;

import java.util.List;

/**
 * RecyclerView adapter for displaying a list of emergency medications.
 * <p>
 * Supports editing and deleting medications using listener callbacks.
 */
public class EmergencyMedicationsAdapter extends RecyclerView.Adapter<EmergencyMedicationsAdapter.ViewHolder> {

    private List<Medication> medications;
    private OnItemClickListener editListener;
    private OnItemClickListener deleteListener;

    /**
     * Interface for handling click events on medication items.
     */
    public interface OnItemClickListener {
        /**
         * Called when a medication item is clicked.
         *
         * @param contact the selected medication item
         */
        void onClick(Medication contact);
    }

    /**
     * Constructs a new EmergencyMedicationsAdapter.
     *
     * @param contacts       the list of medications to display
     * @param editListener   listener for editing a medication
     * @param deleteListener listener for deleting a medication
     */
    public EmergencyMedicationsAdapter(List<Medication> contacts,
                                       OnItemClickListener editListener,
                                       OnItemClickListener deleteListener) {
        this.medications = contacts;
        this.editListener = editListener;
        this.deleteListener = deleteListener;
    }

    /**
     * ViewHolder for a medication item layout.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameText;
        private final TextView dosageText;
        private final ImageButton editButton;
        private final ImageButton deleteButton;

        /**
         * Constructs a ViewHolder and initializes view references.
         *
         * @param itemView the inflated item view
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.tvMedicationName);
            dosageText = itemView.findViewById(R.id.tvMedicationDosage);
            editButton = itemView.findViewById(R.id.btnEditMedication);
            deleteButton = itemView.findViewById(R.id.btnDeleteMedication);
        }

        /** @return the TextView displaying the medication name */
        public TextView getNameText() {
            return nameText;
        }

        /** @return the TextView displaying the medication dosage */
        public TextView getDosageText() {
            return dosageText;
        }

        /** @return the ImageButton for editing the medication */
        public ImageButton getEditButton() {
            return editButton;
        }

        /** @return the ImageButton for deleting the medication */
        public ImageButton getDeleteButton() {
            return deleteButton;
        }
    }

    /**
     * Inflates the medication item layout and returns a ViewHolder.
     *
     * @param parent   the parent ViewGroup
     * @param viewType the type of the new view
     * @return a new ViewHolder instance
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_emergency_medication, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Binds a medication item to the ViewHolder at the specified position.
     *
     * @param holder   the ViewHolder to bind data to
     * @param position the position of the item in the list
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Medication medication = medications.get(position);

        holder.getNameText().setText(medication.getName());
        holder.getDosageText().setText(medication.getDosage());

        holder.getEditButton().setOnClickListener(v -> {
            if (editListener != null) {
                editListener.onClick(medication);
            }
        });

        holder.getDeleteButton().setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onClick(medication);
            }
        });
    }

    /**
     * Returns the total number of medication items.
     *
     * @return the item count
     */
    @Override
    public int getItemCount() {
        return medications.size();
    }
}
