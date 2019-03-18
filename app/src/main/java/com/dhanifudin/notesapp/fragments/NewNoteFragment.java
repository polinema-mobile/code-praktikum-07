package com.dhanifudin.notesapp.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.dhanifudin.notesapp.Constant;
import com.dhanifudin.notesapp.R;
import com.dhanifudin.notesapp.models.Note;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewNoteFragment extends Fragment {

	private OnNewNoteFragmentListener listener;

	public NewNoteFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view =  inflater.inflate(R.layout.fragment_new_note, container, false);
		final EditText titleText = view.findViewById(R.id.input_title);
		final EditText contentText = view.findViewById(R.id.input_content);

		final Bundle bundle = getArguments();
		Note note = null;
		if (bundle != null) {
			note = bundle.getParcelable(Constant.NOTE);
            titleText.setText(note.getTitle());
            contentText.setText(note.getContent());
		}

		Button saveButton = view.findViewById(R.id.button_save);
		final Note finalNote = note;
		saveButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (listener != null) {
				    int tag = (bundle != null)
							? bundle.getInt(Constant.NOTE_TAG) : Constant.INSERT_NOTE;
                    String title = titleText.getText().toString();
                    String content = contentText.getText().toString();

                    Note newNote = (tag == Constant.INSERT_NOTE) ? new Note() : finalNote;
                    newNote.setTitle(title);
                    newNote.setContent(content);

					listener.onSaveButtonClicked(view, newNote, tag);
				}
			}
		});
		return view;
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof OnNewNoteFragmentListener) {
			listener = (OnNewNoteFragmentListener) context;
		} else {
			throw new RuntimeException(context.toString()
				+ "must implement OnNewNoteFragmentListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		listener = null;
	}

	public interface OnNewNoteFragmentListener {
		void onSaveButtonClicked(View view, Note note, int Tag);
	}

}
