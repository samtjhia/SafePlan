package com.b07safetyplanapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.b07safetyplanapp.models.emergencyinfo.Document;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * RecyclerView Adapter for displaying a list of user-uploaded documents.
 * <p>
 * Supports click, edit, and delete actions via listener interfaces.
 */
public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.ViewHolder> {

    private List<Document> documents;
    private OnItemClickListener clickListener;
    private OnItemClickListener editListener;
    private OnItemClickListener deleteListener;

    /**
     * Interface for handling click actions on a document item.
     */
    public interface OnItemClickListener {
        /**
         * Called when a document is clicked.
         *
         * @param document the clicked document
         */
        void onClick(Document document);
    }

    /**
     * Constructs a new DocumentAdapter.
     *
     * @param documents      the list of documents to display
     * @param clickListener  the listener for opening documents
     * @param editListener   the listener for editing documents
     * @param deleteListener the listener for deleting documents
     */
    public DocumentAdapter(List<Document> documents,
                           OnItemClickListener clickListener,
                           OnItemClickListener editListener,
                           OnItemClickListener deleteListener) {
        this.documents = documents;
        this.clickListener = clickListener;
        this.editListener = editListener;
        this.deleteListener = deleteListener;
    }

    /**
     * ViewHolder that holds references to views in a document item layout.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleText;
        private final TextView descriptionText;
        private final TextView dateText;
        private final ImageButton editButton;
        private final ImageButton deleteButton;

        /**
         * Constructs the ViewHolder and initializes view references.
         *
         * @param itemView the item view
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.tvDocumentTitle);
            descriptionText = itemView.findViewById(R.id.tvDocumentDescription);
            dateText = itemView.findViewById(R.id.tvDocumentDate);
            editButton = itemView.findViewById(R.id.btnEditDocument);
            deleteButton = itemView.findViewById(R.id.btnDeleteDocument);
        }

        /** @return the title TextView */
        public TextView getTitleText() {
            return titleText;
        }

        /** @return the description TextView */
        public TextView getDescriptionText() {
            return descriptionText;
        }

        /** @return the date TextView */
        public TextView getDateText() {
            return dateText;
        }

        /** @return the edit ImageButton */
        public ImageButton getEditButton() {
            return editButton;
        }

        /** @return the delete ImageButton */
        public ImageButton getDeleteButton() {
            return deleteButton;
        }
    }


    /**
     * Inflates the item layout and returns a ViewHolder.
     *
     * @param parent   the parent ViewGroup
     * @param viewType the type of the new view
     * @return the new ViewHolder
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_document, parent, false);
        return new ViewHolder(view);
    }


    /**
     * Binds data to the ViewHolder's views for the given position.
     *
     * @param holder   the ViewHolder to bind data to
     * @param position the item position in the dataset
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Document document = documents.get(position);

        holder.getTitleText().setText(document.getTitle());
        holder.getDescriptionText().setText(document.getDescription());

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(new Date(document.getTimestamp()));
        holder.getDateText().setText(formattedDate);

        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onClick(document);
            }
        });

        holder.getEditButton().setOnClickListener(v -> {
            if (editListener != null) {
                editListener.onClick(document);
            }
        });

        holder.getDeleteButton().setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onClick(document);
            }
        });
    }


    /**
     * Returns the number of documents in the list.
     *
     * @return the item count
     */
    @Override
    public int getItemCount() {
        return documents.size();
    }
}