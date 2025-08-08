
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

public class EmergencyMedicationsAdapter extends RecyclerView.Adapter<EmergencyMedicationsAdapter .ViewHolder> {

    private List<Medication> medications;
    private OnItemClickListener editListener;
    private OnItemClickListener deleteListener;

    public interface OnItemClickListener {
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameText;
        private final TextView DosageText;

        private final ImageButton editButton;
        private final ImageButton deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.tvMedicationName);
            DosageText = itemView.findViewById(R.id.tvMedicationDosage);

            editButton = itemView.findViewById(R.id.btnEditMedication);
            deleteButton = itemView.findViewById(R.id.btnDeleteMedication);
        }

        public TextView getNameText() {
            return nameText;
        }

        public TextView getDosageText() {
            return DosageText;
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
                .inflate(R.layout.item_emergency_medication, parent, false);
        return new ViewHolder(view);
    }

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

    @Override
    public int getItemCount() {
        return medications.size();
    }
}
