package de.mobicom.notebookplusplus.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import de.mobicom.notebookplusplus.R;
import de.mobicom.notebookplusplus.data.Note;
import de.mobicom.notebookplusplus.databinding.RecyclerviewNoteItemBinding;
import de.mobicom.notebookplusplus.utils.ItemTouchHelperAdapter;
import de.mobicom.notebookplusplus.utils.ItemTouchHelperViewHolder;
import de.mobicom.notebookplusplus.view.ArchiveFragment;
import de.mobicom.notebookplusplus.view.DeletedNotesFragment;

public class NoteRecyclerViewAdapter extends ListAdapter<Note, NoteRecyclerViewAdapter.NoteViewHolder> implements ItemTouchHelperAdapter, Filterable {

    private RecyclerviewNoteItemBinding recyclerviewNoteItemBinding;
    private List<Note> noteListAll;
    private ItemClickListener mClickListener;
    private ItemClickListener mPopupClickListener;
    private ItemClickListener mBookmarkClickListener;
    private String type;

    public NoteRecyclerViewAdapter(String type) {
        super(DIFF_CALLBACK);
        this.type = type;
    }

    private static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK = new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getNoteId() == newItem.getNoteId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getName().equals(newItem.getName()) &&
                    oldItem.getDescription().equals(newItem.getDescription()) &&
                    oldItem.getLastModifiedAt().isEqual(newItem.getLastModifiedAt());
        }
    };

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            ((LinearLayoutManager) recyclerView.getLayoutManager()).setRecycleChildrenOnDetach(true);
        }
    }

    @Override
    @NonNull
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        recyclerviewNoteItemBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.recyclerview_note_item, parent, false);
        return new NoteViewHolder(recyclerviewNoteItemBinding, this.type);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = getNoteAt(position);
        holder.recyclerviewNoteItemBinding.setNote(note);
        if (this.type.equals(DeletedNotesFragment.DELETED_NOTES_FRAGMENT) || this.type.equals(ArchiveFragment.ARCHIVE_FRAGMENT)) {
            holder.recyclerviewNoteItemBinding.noteCalendarIcon.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
    }

    @Override
    public void onItemDismiss(int position) {

    }

    public Note getNoteAt(int id) {
        return getItem(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public void setPopupMenuItemClickListener(ItemClickListener itemClickListener) {
        this.mPopupClickListener = itemClickListener;
    }

    public void setBookmarkClickListener(ItemClickListener itemClickListener) {
        this.mBookmarkClickListener = itemClickListener;
    }

    public interface ItemClickListener {

        void onItemClick(View view, int position);

        void onPopupMenuItemClick(MenuItem item, int position);

        void onBookmarkClick(View view, int position);

    }

    public interface ButtonClickListeners {

        void onBookmark(View view);

        void onOpenPopupMenu(View view);
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (noteListAll == null) {
                noteListAll = new ArrayList<>(getCurrentList());
            }
            List<Note> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(noteListAll);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Note item : noteListAll) {
                    if (item.getName().toLowerCase().contains(filterPattern) ||
                            item.getDescription().toLowerCase().contains(filterPattern)) {
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

    public class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, ItemTouchHelperViewHolder, PopupMenu.OnMenuItemClickListener, NoteRecyclerViewAdapter.ButtonClickListeners {
        private RecyclerviewNoteItemBinding recyclerviewNoteItemBinding;
        private String type;

        NoteViewHolder(RecyclerviewNoteItemBinding recyclerviewNoteItemBinding, String type) {
            super(recyclerviewNoteItemBinding.getRoot());
            this.recyclerviewNoteItemBinding = recyclerviewNoteItemBinding;
            this.type = type;
            this.recyclerviewNoteItemBinding.setHandler(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }

        @Override
        public void onBookmark(View view) {
            if (mBookmarkClickListener != null) {
                mBookmarkClickListener.onBookmarkClick(view, getAdapterPosition());
            }
        }

        @Override
        public void onOpenPopupMenu(View view) {
            System.out.println("Context");
            androidx.appcompat.widget.PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            if (this.type.equals(ArchiveFragment.ARCHIVE_FRAGMENT)) {
                popupMenu.inflate(R.menu.popup_menu_archive);
            } else if (this.type.equals(DeletedNotesFragment.DELETED_NOTES_FRAGMENT)) {
                popupMenu.inflate(R.menu.popup_menu_deleted_notes);
            } else {
                popupMenu.inflate(R.menu.popup_menu_note);
            }
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mPopupClickListener != null) {
                mPopupClickListener.onPopupMenuItemClick(item, getAdapterPosition());
            }
            return true;
        }
    }
}