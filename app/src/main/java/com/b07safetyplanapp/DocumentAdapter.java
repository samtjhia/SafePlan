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

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.ViewHolder> {

    private List<Document> documents;
    private OnItemClickListener clickListener;
    private OnItemClickListener editListener;
    private OnItemClickListener deleteListener;

    public interface OnItemClickListener {
        void onClick(Document document);
    }

    public DocumentAdapter(List<Document> documents,
                           OnItemClickListener clickListener,
                           OnItemClickListener editListener,
                           OnItemClickListener deleteListener) {
        this.documents = documents;
        this.clickListener = clickListener;
        this.editListener = editListener;
        this.deleteListener = deleteListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleText;
        private final TextView descriptionText;
        private final TextView dateText;
        private final ImageButton editButton;
        private final ImageButton deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.tvDocumentTitle);
            descriptionText = itemView.findViewById(R.id.tvDocumentDescription);
            dateText = itemView.findViewById(R.id.tvDocumentDate);
            editButton = itemView.findViewById(R.id.btnEditDocument);
            deleteButton = itemView.findViewById(R.id.btnDeleteDocument);
        }

        public TextView getTitleText() {
            return titleText;
        }

        public TextView getDescriptionText() {
            return descriptionText;
        }

        public TextView getDateText() {
            return dateText;
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
                .inflate(R.layout.item_document, parent, false);
        return new ViewHolder(view);
    }

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

    @Override
    public int getItemCount() {
        return documents.size();
    }
}