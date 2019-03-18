package com.dhanifudin.notesapp.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.dhanifudin.notesapp.models.Note;
import com.dhanifudin.notesapp.repositories.NoteRepository;

import java.util.List;

public class NoteViewModel extends AndroidViewModel  {
	private NoteRepository repository;
	private LiveData<List<Note>> notes;

	public NoteViewModel(@NonNull Application application) {
		super(application);
		this.repository = new NoteRepository(application);
		this.notes = repository.getNotes();
	}

	public LiveData<List<Note>> getNotes() {
		return notes;
	}

	public void insert(Note note) {
		repository.insert(note);
	}

	public void update(Note note) {
		repository.update(note);
	}
}
