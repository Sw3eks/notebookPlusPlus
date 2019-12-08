package de.mobicom.notebookplusplus.notebook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.mobicom.notebookplusplus.R;
import de.mobicom.notebookplusplus.notebook.model.Notebook;

public class NotebookRecyclerViewAdapter extends RecyclerView.Adapter<NotebookRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Notebook> notebookList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public NotebookRecyclerViewAdapter(Context context, ArrayList<Notebook> notebookList) {
        this.mInflater = LayoutInflater.from(context);
        this.notebookList = notebookList;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_notebook_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notebook notebook = notebookList.get(position);
        holder.notebookTitle.setText(notebook.getName());
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return notebookList.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView notebookTitle;

        ViewHolder(View itemView) {
            super(itemView);
            notebookTitle = itemView.findViewById(R.id.notebookTitle);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public Notebook getItem(int id) {
        return notebookList.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}