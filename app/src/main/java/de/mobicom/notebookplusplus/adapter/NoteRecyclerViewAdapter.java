package de.mobicom.notebookplusplus.adapter;

import android.graphics.Color;
import android.graphics.PorterDuff;
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
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import de.mobicom.notebookplusplus.R;
import de.mobicom.notebookplusplus.data.Note;
import de.mobicom.notebookplusplus.databinding.RecyclerviewNoteItemBinding;
import de.mobicom.notebookplusplus.utils.ItemTouchHelperAdapter;
import de.mobicom.notebookplusplus.utils.ItemTouchHelperViewHolder;
import de.mobicom.notebookplusplus.view.ArchiveFragment;
import de.mobicom.notebookplusplus.view.DeletedNotesFragment;

public class NoteRecyclerViewAdapter extends ListAdapter<Note, NoteRecyclerViewAdapter.NoteViewHolder> implements ItemTouchHelperAdapter, androidx.appcompat.widget.PopupMenu.OnMenuItemClickListener, Filterable {

    private RecyclerviewNoteItemBinding recyclerviewNoteItemBinding;
    private List<Note> noteListAll;
    private ItemClickListener mClickListener;
    private ItemClickListener mLongClickListener;
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
    @NonNull
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        recyclerviewNoteItemBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.recyclerview_note_item, parent, false);
        return new NoteViewHolder(recyclerviewNoteItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = getNoteAt(position);
        holder.recyclerviewNoteItemBinding.setNote(note);
        holder.recyclerviewNoteItemBinding.setHandler(this);
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

    public void onBookmarkNote() {
        recyclerviewNoteItemBinding.noteBookmarkIcon.setImageResource(R.drawable.ic_note_bookmark_enabled);
        System.out.println("Bookmarked");
    }

    public void onOpenContextMenu(View view) {
        System.out.println("Context");
        androidx.appcompat.widget.PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.setOnMenuItemClickListener(this);
        if (this.type.equals(ArchiveFragment.ARCHIVE_FRAGMENT)) {
            popupMenu.inflate(R.menu.popup_menu_archive);
        } else if (this.type.equals(DeletedNotesFragment.DELETED_NOTES_FRAGMENT)) {
            popupMenu.inflate(R.menu.popup_menu_deleted_notes);
        } else {
            popupMenu.inflate(R.menu.popup_menu_note);
        }
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.moveNote:
                System.out.println("Move");
                return true;
            case R.id.archiveNote:
                return true;
            case R.id.cloneNote:
                return true;
            case R.id.deleteNote:
                return true;
            default:
                return false;
        }
    }

    public Note getNoteAt(int id) {
        return getItem(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public void setLongClickListener(ItemClickListener itemClickListener) {
        this.mLongClickListener = itemClickListener;
    }

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

    public class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, ItemTouchHelperViewHolder, View.OnLongClickListener {
        private RecyclerviewNoteItemBinding recyclerviewNoteItemBinding;

        NoteViewHolder(RecyclerviewNoteItemBinding recyclerviewNoteItemBinding) {
            super(recyclerviewNoteItemBinding.getRoot());
            this.recyclerviewNoteItemBinding = recyclerviewNoteItemBinding;
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
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }
}