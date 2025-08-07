
package com.b07safetyplanapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.b07safetyplanapp.models.emergencyinfo.EmergencyContact;
import com.b07safetyplanapp.models.emergencyinfo.SafeLocation;

import java.util.List;

/**
 * Adapter class for displaying a list of emergency safe locations in a RecyclerView.
 * <p>
 * Supports editing and deleting each item via callback listeners.
 */
public class EmergencySafeLocationsAdapter extends RecyclerView.Adapter<EmergencySafeLocationsAdapter.ViewHolder> {

    private List<SafeLocation> safe_locations;
    private OnItemClickListener editListener;
    private OnItemClickListener deleteListener;


    public interface OnItemClickListener {
        void onClick(SafeLocation safe_location);
    }

    /**
     * Constructor for the adapter.
     *
     * @param safe_locations List of SafeLocation items to display.
     * @param editListener   Listener for handling edit clicks.
     * @param deleteListener Listener for handling delete clicks.
     */
    public EmergencySafeLocationsAdapter(List<SafeLocation> safe_locations,
                                         OnItemClickListener editListener,
                                         OnItemClickListener deleteListener) {
        this.safe_locations = safe_locations;
        this.editListener = editListener;
        this.deleteListener = deleteListener;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameText;
        private final TextView addressText;
        private final TextView notesText;
        private final ImageButton editButton;
        private final ImageButton deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.tvLocationName);
            addressText = itemView.findViewById(R.id.tvLocationAddress);
            notesText = itemView.findViewById(R.id.tvLocationNotes);
            editButton = itemView.findViewById(R.id.btnEditSafeLocation);
            deleteButton = itemView.findViewById(R.id.btnDeleteSafeLocation);
        }


        public TextView getNameText() {
            return nameText;
        }

        public TextView getAddressText() {
            return addressText;
        }

        public TextView getNotesText() {
            return notesText;
        }

        public ImageButton getEditButton() {
            return editButton;
        }

        public ImageButton getDeleteButton() {
            return deleteButton;
        }
    }

    /**
     * Inflates the view for each list item.
     *
     * @param parent   The parent view group.
     * @param viewType The type of the view (unused).
     * @return A new ViewHolder instance.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_emergency_safe_location, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Binds data to the item view at the given position.
     *
     * @param holder   The ViewHolder for the current item.
     * @param position The position in the dataset.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SafeLocation contact = safe_locations.get(position);

        holder.getNameText().setText(contact.getName());
        holder.getAddressText().setText(contact.getAddress());
        holder.getNotesText().setText(contact.getNotes());

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
        return safe_locations.size();
    }
}
