package de.mobicom.notebookplusplus.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import de.mobicom.notebookplusplus.R;
import de.mobicom.notebookplusplus.data.Note;
import de.mobicom.notebookplusplus.databinding.RecyclerviewNoteItemBinding;
import de.mobicom.notebookplusplus.utils.ItemTouchHelperAdapter;
import de.mobicom.notebookplusplus.utils.ItemTouchHelperViewHolder;

public class NoteRecyclerViewAdapter extends RecyclerView.Adapter<NoteRecyclerViewAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    private List<Note> noteList;
    private List<Note> noteListFiltered = new ArrayList<>();
    private ItemClickListener mClickListener;

    public NoteRecyclerViewAdapter() {
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerviewNoteItemBinding recyclerviewNoteItemBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.recyclerview_note_item, parent, false);
        return new ViewHolder(recyclerviewNoteItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note note = noteList.get(position);
        holder.recyclerviewNoteItemBinding.setNote(note);
//
//        switch (note.getType()) {
//            case "todo":
//                //holder.noteType.setBackgroundResource(R.drawable.ic_note_type_todo);
//                break;
//            case "speech":
//                //holder.noteType.setBackgroundResource(R.drawable.ic_note_type_speech);
//                break;
//            default:
//                //holder.noteType.setBackgroundResource(R.drawable.ic_note_type_text);
//        }

    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public void setNoteList(List<Note> noteList) {
        this.noteList = noteList;
        this.noteListFiltered = noteList;
        notifyDataSetChanged();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(noteList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, ItemTouchHelperViewHolder {
        private RecyclerviewNoteItemBinding recyclerviewNoteItemBinding;

        ViewHolder(RecyclerviewNoteItemBinding recyclerviewNoteItemBinding) {
            super(recyclerviewNoteItemBinding.getRoot());
            this.recyclerviewNoteItemBinding = recyclerviewNoteItemBinding;
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

        }
    }

    public Note getItem(int id) {
        return noteList.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public void filter(String text) {
        noteList.clear();
        if (text.isEmpty()) {
            noteList.addAll(noteListFiltered);
        } else {
            text = text.toLowerCase();
            for (Note item : noteListFiltered) {
                if (item.getName().toLowerCase().contains(text) || item.getDescription().toLowerCase().contains(text)) {
                    noteList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
}