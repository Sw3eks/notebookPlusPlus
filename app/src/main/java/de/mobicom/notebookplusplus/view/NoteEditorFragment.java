package de.mobicom.notebookplusplus.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.mobicom.notebookplusplus.R;
import de.mobicom.notebookplusplus.adapter.NoteListItemRecyclerViewAdapter;
import de.mobicom.notebookplusplus.data.model.NoteListItem;
import de.mobicom.notebookplusplus.databinding.FragmentNoteEditorBinding;
import de.mobicom.notebookplusplus.viewmodel.NotebookViewModel;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.graphics.Color.parseColor;
import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * Note editor to change content and title of notes
 */
public class NoteEditorFragment extends Fragment implements NoteListItemRecyclerViewAdapter.ItemClickListener {

    private NotebookViewModel notebookViewModel;
    private FragmentNoteEditorBinding fragmentNoteEditorBinding;
    private NoteListItemRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;

    private List<NoteListItem> currentList;
    private String currentTitle;
    private boolean currentNotificationStatus;
    private LocalDate selectedDate;
    private boolean isChanged;
    private String message;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentNoteEditorBinding = FragmentNoteEditorBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true);
        isChanged = false;
        message = getResources().getString(R.string.changes_saved);

        notebookViewModel = ViewModelProviders.of(requireActivity()).get(NotebookViewModel.class);
        if (notebookViewModel.getNotebook() != null) {
            fragmentNoteEditorBinding.colorBar.setBackgroundColor(parseColor(notebookViewModel.getNotebook().getColor()));
        } else {
            fragmentNoteEditorBinding.colorBar.setBackgroundColor(parseColor(notebookViewModel.selectNotebookColor(notebookViewModel.getNote().getNotebookParentId())));
        }
        fragmentNoteEditorBinding.setNote(notebookViewModel.getNote());
        currentTitle = notebookViewModel.getNote().getName();
        currentNotificationStatus = notebookViewModel.getNote().isNotificationEnabled();

        if (notebookViewModel.getNote().getType() == R.drawable.ic_note_type_text) {
            setupTextLayout();
        }

        if (notebookViewModel.getNote().getType() == R.drawable.ic_note_type_todo) {
            setupListLayout();
        }

        if (notebookViewModel.getNote().getType() == R.drawable.ic_note_type_voice) {
            setupVoiceLayout();
        }
        setupDatePicker();

        return fragmentNoteEditorBinding.getRoot();
    }

    /**
     * inits the setup for a note with type "text"
     */
    private void setupTextLayout() {
        fragmentNoteEditorBinding.editNote.requestFocus();
        fragmentNoteEditorBinding.editNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().equals(notebookViewModel.getNote().getDescription())) {
                    isChanged = false;
                } else {
                    notebookViewModel.getNote().setDescription(s.toString().trim());
                    isChanged = true;
                }
            }
        });
    }

    /**
     * inits the setup for a note with type "list"
     */
    private void setupListLayout() {
        fragmentNoteEditorBinding.editNote.setVisibility(View.GONE);
        fragmentNoteEditorBinding.rvNoteListItem.setVisibility(View.VISIBLE);

        recyclerView = fragmentNoteEditorBinding.rvNoteListItem;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new NoteListItemRecyclerViewAdapter();
        adapter.setCheckBoxListener(this);
        adapter.setEnterKeyListener(this);
        adapter.setTextChangeListener(this);
        adapter.setBackSpaceListener(this);
        recyclerView.setAdapter(adapter);

        NoteListItem defaultItem = new NoteListItem(notebookViewModel.getNote().getNoteId(), "", false);
        if (notebookViewModel.getAllNoteListItemsAsync(notebookViewModel.getNote().getNoteId()) != null) {
            currentList = new ArrayList<>(notebookViewModel.getAllNoteListItemsAsync(notebookViewModel.getNote().getNoteId()));
        } else {
            currentList = new ArrayList<>();
        }
        currentList.add(defaultItem);
        adapter.submitList(currentList);
    }

    private static final String AUDIO_RECORDER_FILE_EXT_3GP = ".3gp";
    private static final String AUDIO_RECORDER_FILE_EXT_MP4 = ".mp4";
    private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";
    public static final int REQUEST_AUDIO_PERMISSION_CODE = 1;

    private MediaRecorder recorder = null;
    private int currentFormat = 0;
    private int output_formats[] = {MediaRecorder.OutputFormat.MPEG_4, MediaRecorder.OutputFormat.THREE_GPP};
    private String file_exts[] = {AUDIO_RECORDER_FILE_EXT_MP4, AUDIO_RECORDER_FILE_EXT_3GP};

    /**
     * inits the setup for a note with type "voice"
     */
    @SuppressLint("ClickableViewAccessibility")
    private void setupVoiceLayout() {
        fragmentNoteEditorBinding.editNote.setVisibility(View.GONE);
        //fragmentNoteEditorBinding.setHandler(this);
        fragmentNoteEditorBinding.startRecording.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    System.out.println("start");
                    startRecording();
                    return true;
                case MotionEvent.ACTION_UP:
                    System.out.println("stop");
                    stopRecording();
                    break;
            }
            return false;
        });
    }

    private String getFilename() {
        String mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/AudioRecording.3gp";
        return mFileName;
    }

    public void startRecording() {
        if (CheckPermissions()) {
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setOutputFile(getFilename());

            try {
                recorder.prepare();
                recorder.start();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            RequestPermissions();
        }
    }

    public void stopRecording() {
        if (ActivityCompat.checkSelfPermission(getActivity(), RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{RECORD_AUDIO},
                    0);

        } else {
            if (null != recorder) {
                recorder.stop();
                recorder.reset();
                recorder.release();

                recorder = null;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_AUDIO_PERMISSION_CODE:
                if (grantResults.length > 0) {
                    boolean permissionToRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean permissionToStore = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (permissionToRecord && permissionToStore) {
                        Toast.makeText(getActivity(), "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean CheckPermissions() {
        int result = ContextCompat.checkSelfPermission(getContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getContext(), RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void RequestPermissions() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE}, REQUEST_AUDIO_PERMISSION_CODE);
    }

    /**
     * datepicker to set and enable/disable a date to send notifications
     */
    private void setupDatePicker() {
        // ClickListener instead of checkChangeListener to prevent firing message when view is opened
        fragmentNoteEditorBinding.enableNotification.setOnClickListener(v -> {
            notebookViewModel.getNote().setNotificationEnabled(!notebookViewModel.getNote().isNotificationEnabled());
            Toast.makeText(getContext(),
                    notebookViewModel.getNote().isNotificationEnabled() ? getResources().getString(R.string.notification_enabled)
                            : getResources().getString(R.string.notification_disabled), Toast.LENGTH_LONG)
                    .show();
        });

        // set min Date to Today (cause notification has to be in future
        fragmentNoteEditorBinding.datePicker.setMinDate(System.currentTimeMillis() - 1000);
        fragmentNoteEditorBinding.datePicker.setOnDateChangedListener((view, year, monthOfYear, dayOfMonth) -> selectedDate = LocalDate.of(year, monthOfYear + 1, dayOfMonth));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(notebookViewModel.getNote().getName());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_note_editor, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case android.R.id.home:
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).popBackStack();
                break;
            case R.id.create_notification:
                if (fragmentNoteEditorBinding.cardView.getVisibility() == View.GONE) {
                    fragmentNoteEditorBinding.cardView.setVisibility(View.VISIBLE);
                } else {
                    fragmentNoteEditorBinding.cardView.setVisibility(View.GONE);
                }
                break;
            case R.id.editNoteTitle:
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(NoteEditorFragmentDirections.actionNoteEditorFragmentToCreateNotebookDialogFragment().setDialogType("Note Editor"));
                break;
            case R.id.moveToArchive:
                notebookViewModel.getNote().setArchived(true);
                isChanged = true;
                message = getResources().getString(R.string.moved_note_to_archive);
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).popBackStack();
                break;
            case R.id.deleteNote:
                notebookViewModel.getNote().setMarkedForDelete(true);
                isChanged = true;
                message = getResources().getString(R.string.moved_note_to_deleted);
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).popBackStack();
                break;
        }

        return true;
    }

    @Override
    public void onPause() {
        if (notebookViewModel.getNote().getType() == R.drawable.ic_note_type_todo) {
            saveList();
        }
        shouldSave();

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        super.onPause();
    }

    private void saveList() {
        for (int i = 0; i < currentList.size(); i++) {
            if (i == 0) {
                // save first line to note for preview
                notebookViewModel.getNote().setDescription(currentList.get(0).getContent());
                notebookViewModel.update(notebookViewModel.getNote());
            }
            // save only entries with content
            if (!currentList.get(i).getContent().equals("")) {
                System.out.println("SAVED");
                notebookViewModel.insert(currentList.get(i));
            }
        }

        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    private void shouldSave() {
        if (!notebookViewModel.getNote().getName().equals(currentTitle) ||
                notebookViewModel.getNote().isNotificationEnabled() != currentNotificationStatus) {
            isChanged = true;
        }
        if (selectedDate != null && !notebookViewModel.getNote().getNotificationDate().isEqual(selectedDate)) {
            notebookViewModel.getNote().setNotificationDate(selectedDate);
            isChanged = true;
        }
        if (isChanged) {
            notebookViewModel.getNote().setLastModifiedAt(LocalDateTime.now());
            notebookViewModel.update(notebookViewModel.getNote());
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCheckBoxClick(View view, boolean isChecked, int position) {
        currentList.get(position).setChecked(isChecked);
    }

    /**
     * inserts a new row if user clicks "done" in soft keyboard
     *
     * @param view     current view
     * @param actionId id for the keyboard action
     * @param event    event fired by user input
     * @param position position in the list, clicked by user
     * @return boolean value whether action got handled
     */
    @Override
    public boolean onEnterClicked(TextView view, int actionId, KeyEvent event, int position) {
        if (event == null) {
            if (actionId == EditorInfo.IME_ACTION_DONE &&
                    view.getText().toString().trim().length() > 0) {
                NoteListItem item = new NoteListItem(notebookViewModel.getNote().getNoteId(), "", false);
                currentList.add(item);
                adapter.notifyItemInserted(position + 1);
                recyclerView.smoothScrollToPosition(position + 1);
                return true;
            }
        } else if (actionId == EditorInfo.IME_ACTION_NEXT) ;
        return false;

    }

    /**
     * deletes a row if its empty and the user clicked backspace
     *
     * @param s        current value of the edit text
     * @param start    position of current string in the sequence
     * @param before   old position
     * @param count    steps from start
     * @param position current position in the list, clicked by user
     */
    //TODO: delete only if row is empty.. not when it gets empty
    @Override
    public void onBackSpace(CharSequence s, int start, int before, int count, int position) {
        System.out.println("START " + start + " BEFORE " + before + " COUNT " + count);
        if (s.toString().trim().equals("") && currentList.size() > 1 && count < before) {
            notebookViewModel.delete(adapter.getNoteItemAt(position).getNoteListItemId());
            currentList.remove(adapter.getNoteItemAt(position));
            adapter.notifyItemRemoved(position);
        }
    }

    @Override
    public void onTextChange(CharSequence s, int position) {
        currentList.get(position).setContent(s.toString().trim());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentNoteEditorBinding = null;
    }
}
