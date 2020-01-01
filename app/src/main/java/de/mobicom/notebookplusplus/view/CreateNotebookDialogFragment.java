package de.mobicom.notebookplusplus.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import androidx.lifecycle.ViewModelProviders;
import de.mobicom.notebookplusplus.R;
import de.mobicom.notebookplusplus.data.Notebook;
import de.mobicom.notebookplusplus.viewmodel.NotebookViewModel;

public class CreateNotebookDialogFragment extends DialogFragment {

    private NotebookViewModel notebookViewModel;
    private EditText mEditTextNotebookName;
    private Spinner colorSpinner;

    public CreateNotebookDialogFragment() {
    }

    static CreateNotebookDialogFragment newInstance(String title) {
        CreateNotebookDialogFragment frag = new CreateNotebookDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_create_notebook, container);
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        AlertDialog d = (AlertDialog) getDialog();
//        if (d != null) {
//            positiveButton = d.getButton(Dialog.BUTTON_POSITIVE);
//            positiveButton.setEnabled(false);
//        }
//    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        notebookViewModel = ViewModelProviders.of(requireActivity()).get(NotebookViewModel.class);

        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);

        mEditTextNotebookName.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final AlertDialog.Builder b = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme)
                .setView(inflater.inflate(R.layout.dialog_create_notebook, null))
                .setTitle(R.string.create_a_new_notebook)
                .setPositiveButton(R.string.create_button,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String color;
                                switch (colorSpinner.getSelectedItem().toString()) {
                                    case "Blue":
                                        color = "#3498db";
                                        break;
                                    case "Red":
                                        color = "#e74c3c";
                                        break;
                                    case "Purple":
                                        color = "#9b59b6";
                                        break;
                                    case "Green":
                                        color = "#2ecc71";
                                        break;
                                    case "Dark Grey":
                                        color = "#34495e";
                                        break;
                                    case "Yellow":
                                        color = "#f1c40f";
                                        break;
                                    default:
                                        color = "#f39c12";
                                }
                                notebookViewModel.insert(
                                        new Notebook(mEditTextNotebookName.getText().toString(), 1, color));
                            }
                        }
                )
                .setNegativeButton(R.string.cancel_button,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }
                );

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_create_notebook, null);

        mEditTextNotebookName = view.findViewById(R.id.edit_text_new_notebook);
        colorSpinner = view.findViewById(R.id.colorDropdown);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.color_Array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colorSpinner.setAdapter(adapter);

        b.setView(view);
        return b.create();
    }

}
