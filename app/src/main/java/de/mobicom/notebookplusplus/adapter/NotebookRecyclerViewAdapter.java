package de.mobicom.notebookplusplus.adapter;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import de.mobicom.notebookplusplus.R;
import de.mobicom.notebookplusplus.data.Notebook;
import de.mobicom.notebookplusplus.databinding.RecyclerviewNotebookItemBinding;
import de.mobicom.notebookplusplus.utils.ItemTouchHelperAdapter;
import de.mobicom.notebookplusplus.utils.ItemTouchHelperViewHolder;

public class NotebookRecyclerViewAdapter extends ListAdapter<Notebook, NotebookRecyclerViewAdapter.NotebookViewHolder> implements ItemTouchHelperAdapter, Filterable {

    private List<Notebook> notebookListAll;
    private ItemClickListener mClickListener;
    private ItemClickListener mLongClickListener;

    public NotebookRecyclerViewAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Notebook> DIFF_CALLBACK = new DiffUtil.ItemCallback<Notebook>() {
        @Override
        public boolean areItemsTheSame(@NonNull Notebook oldItem, @NonNull Notebook newItem) {
            return oldItem.getNotebookId() == newItem.getNotebookId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Notebook oldItem, @NonNull Notebook newItem) {
            return oldItem.getName().equals(newItem.getName()) && oldItem.getColor().equals(newItem.getColor());
        }
    };

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public NotebookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerviewNotebookItemBinding recyclerviewNotebookItemBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.recyclerview_notebook_item, parent, false);
        return new NotebookViewHolder(recyclerviewNotebookItemBinding);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull NotebookViewHolder holder, int position) {
        Notebook notebook = getNotebookAt(position);
        holder.recyclerviewNotebookItemBinding.setNotebook(notebook);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(getCurrentList(), i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(getCurrentList(), i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
    }

    // convenience method for getting data at click position
    public Notebook getNotebookAt(int id) {
        return getItem(id);
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

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if(notebookListAll == null) {
                notebookListAll = new ArrayList<>(getCurrentList());
            }
            List<Notebook> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(notebookListAll);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Notebook item : notebookListAll) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            submitList((List) results.values);
        }
    };

    // stores and recycles views as they are scrolled off screen
    public class NotebookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, ItemTouchHelperViewHolder, View.OnCreateContextMenuListener {
        private RecyclerviewNotebookItemBinding recyclerviewNotebookItemBinding;

        NotebookViewHolder(RecyclerviewNotebookItemBinding recyclerviewNotebookItemBinding) {
            super(recyclerviewNotebookItemBinding.getRoot());
            this.recyclerviewNotebookItemBinding = recyclerviewNotebookItemBinding;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            //itemView.setOnCreateContextMenuListener(this);
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

        @Override
        public void onItemClear() {
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select The Action");
            menu.add(0, v.getId(), 0, "Call");//groupId, itemId, order, title
            menu.add(0, v.getId(), 0, "SMS");
        }
    }
}