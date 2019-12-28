package de.mobicom.notebookplusplus.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import de.mobicom.notebookplusplus.R;
import de.mobicom.notebookplusplus.data.Note;
import de.mobicom.notebookplusplus.data.Notebook;
import de.mobicom.notebookplusplus.databinding.RecyclerviewNotebookItemBinding;
import de.mobicom.notebookplusplus.utils.ItemTouchHelperAdapter;
import de.mobicom.notebookplusplus.utils.ItemTouchHelperViewHolder;

import static android.graphics.Color.parseColor;

public class NotebookRecyclerViewAdapter extends RecyclerView.Adapter<NotebookRecyclerViewAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    private List<Notebook> notebookList;
    private List<Notebook> notebookListFiltered = new ArrayList<>();
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private ItemClickListener mLongClickListener;

    // data is passed into the constructor
    public NotebookRecyclerViewAdapter(Context context, List<Notebook> notebookList) {
        this.mInflater = LayoutInflater.from(context);
        this.notebookList = notebookList;
        this.notebookListFiltered.addAll(notebookList);
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerviewNotebookItemBinding recyclerviewNotebookItemBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.recyclerview_notebook_item, parent, false);
        //View view = mInflater.inflate(R.layout.recyclerview_notebook_item, parent, false);
        return new ViewHolder(recyclerviewNotebookItemBinding);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notebook notebook = notebookList.get(position);
        holder.recyclerviewNotebookItemBinding.setNotebook(notebook);
        //holder.notebookTitle.setText(notebook.getName());

        //holder.notebookIcon.setColorFilter(parseColor(notebook.getColor()), PorterDuff.Mode.MULTIPLY);
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return notebookList.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(notebookList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(notebookList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    public void setNotebookList(List<Notebook> notebookList) {
        this.notebookList = notebookList;
        notifyDataSetChanged();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, ItemTouchHelperViewHolder {
        private RecyclerviewNotebookItemBinding recyclerviewNotebookItemBinding;
        //TextView notebookTitle;
        //ImageView notebookIcon;

        ViewHolder(RecyclerviewNotebookItemBinding recyclerviewNotebookItemBinding) {
            super(recyclerviewNotebookItemBinding.getRoot());
            this.recyclerviewNotebookItemBinding = recyclerviewNotebookItemBinding;
            //notebookTitle = itemView.findViewById(R.id.notebookTitle);
            //notebookIcon = itemView.findViewById(R.id.notebookIcon);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            if (mLongClickListener != null)
                mLongClickListener.onLongItemClick(view, getAdapterPosition());
            return true;
        }

        @Override
        public void onItemSelected() {
            itemView.setElevation(5);
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

    public void setLongClickListener(ItemClickListener itemClickListener) {
        this.mLongClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);

        void onLongItemClick(View view, int position);
    }

    public void filter(String text) {
        notebookList.clear();
        if (text.isEmpty()) {
            notebookList.addAll(notebookListFiltered);
        } else {
            text = text.toLowerCase();
            for (Notebook item : notebookListFiltered) {
                if (item.getName().toLowerCase().contains(text)) {
                    notebookList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
}