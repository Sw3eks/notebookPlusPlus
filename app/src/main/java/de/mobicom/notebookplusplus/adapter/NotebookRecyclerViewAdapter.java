package de.mobicom.notebookplusplus.adapter;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import de.mobicom.notebookplusplus.R;
import de.mobicom.notebookplusplus.data.Notebook;
import de.mobicom.notebookplusplus.databinding.RecyclerviewNotebookItemBinding;

public class NotebookRecyclerViewAdapter extends ListAdapter<Notebook, NotebookRecyclerViewAdapter.NotebookViewHolder> implements Filterable {

    private List<Notebook> notebookListAll;
    private ItemClickListener mClickListener;
    private ItemClickListener mContextMenuClickListener;

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

    // convenience method for getting data at click position
    public Notebook getNotebookAt(int id) {
        return getItem(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public void setContextMenuItemClickListener(ItemClickListener itemClickListener) {
        this.mContextMenuClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);

        void onContextMenuItemClick(MenuItem item, int position);
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (notebookListAll == null) {
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
    public class NotebookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        private RecyclerviewNotebookItemBinding recyclerviewNotebookItemBinding;

        NotebookViewHolder(RecyclerviewNotebookItemBinding recyclerviewNotebookItemBinding) {
            super(recyclerviewNotebookItemBinding.getRoot());
            this.recyclerviewNotebookItemBinding = recyclerviewNotebookItemBinding;
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            Animation bounce = AnimationUtils.loadAnimation(v.getContext(), R.anim.shake);
            recyclerviewNotebookItemBinding.notebookIcon.startAnimation(bounce);
            menu.setHeaderTitle(R.string.edit_notebook);
            menu.add(0, R.string.notebook_change_name, 0, R.string.notebook_change_name).setOnMenuItemClickListener(this);
            menu.add(0, R.string.notebook_change_color, 1, R.string.notebook_change_color).setOnMenuItemClickListener(this);
            menu.add(0, R.string.notebook_delete, 2, R.string.notebook_delete).setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mContextMenuClickListener != null) {
                mContextMenuClickListener.onContextMenuItemClick(item, getAdapterPosition());
            }
            return true;
        }
    }
}