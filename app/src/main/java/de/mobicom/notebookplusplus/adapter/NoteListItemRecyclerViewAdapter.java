package de.mobicom.notebookplusplus.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import de.mobicom.notebookplusplus.R;
import de.mobicom.notebookplusplus.data.model.NoteListItem;
import de.mobicom.notebookplusplus.databinding.RecyclerviewNoteListItemBinding;

public class NoteListItemRecyclerViewAdapter extends ListAdapter<NoteListItem, NoteListItemRecyclerViewAdapter.NoteListItemViewHolder> {

    private RecyclerviewNoteListItemBinding binding;
    private ItemClickListener mCheckBoxListener;
    private ItemClickListener mEnterListener;
    private ItemClickListener mTextChangeListener;

    public NoteListItemRecyclerViewAdapter() {
        super(DIFF_CALLBACK);

    }

    private static final DiffUtil.ItemCallback<NoteListItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<NoteListItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull NoteListItem oldItem, @NonNull NoteListItem newItem) {
            return oldItem.getNoteListItemId() == newItem.getNoteListItemId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull NoteListItem oldItem, @NonNull NoteListItem newItem) {
            return oldItem.getContent().equals(newItem.getContent()) &&
                    oldItem.isChecked() == newItem.isChecked();
        }
    };

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            ((LinearLayoutManager) recyclerView.getLayoutManager()).setRecycleChildrenOnDetach(true);
        }
    }

    @NonNull
    @Override
    public NoteListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.recyclerview_note_list_item, parent, false);
        return new NoteListItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteListItemRecyclerViewAdapter.NoteListItemViewHolder holder, int position) {
        NoteListItem item = getNoteItemAt(position);
        holder.binding.setItem(item);
        holder.binding.contentLine.requestFocus();
    }

    public NoteListItem getNoteItemAt(int id) {
        return getItem(id);
    }

    public void setCheckBoxListener(ItemClickListener itemClickListener) {
        this.mCheckBoxListener = itemClickListener;
    }

    public void setEnterKeyListener(ItemClickListener keyListener) {
        this.mEnterListener = keyListener;
    }

    public void setTextChangeListener(ItemClickListener changeListener) {
        this.mTextChangeListener = changeListener;
    }

    public interface ItemClickListener {

        void onCheckBoxClick(View view, boolean isChecked, int position);

        void onEnterClicked(int actionId, KeyEvent event, String content, int position);

        void onTextChange(CharSequence s, int position);

    }

    public class NoteListItemViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener, EditText.OnKeyListener, TextWatcher {
        private RecyclerviewNoteListItemBinding binding;

        NoteListItemViewHolder(RecyclerviewNoteListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.itemCheckbox.setOnCheckedChangeListener(this);
            this.binding.contentLine.setOnKeyListener(this);
            this.binding.contentLine.addTextChangedListener(this);
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (mCheckBoxListener != null) {
                mCheckBoxListener.onCheckBoxClick(buttonView, isChecked, getAdapterPosition());
            }
        }

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            String content = binding.contentLine.getText().toString();
            if (mEnterListener != null) {
                mEnterListener.onEnterClicked(keyCode, event, content, getAdapterPosition());
            }
            return true;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (mTextChangeListener != null) {
                mTextChangeListener.onTextChange(s, getAdapterPosition());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }
}
