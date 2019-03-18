package com.dhanifudin.notesapp.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.Update;

import com.dhanifudin.notesapp.db.DateConverter;
import com.dhanifudin.notesapp.models.Note;

import java.util.List;

@Dao
public interface NoteDao {

	@Insert
	void insert(Note... note);

	@Update
	void update(Note... note);

	@Delete
	void delete(Note... note);

	@Query("SELECT * FROM notes")
	LiveData<List<Note>> getAll();

	@Query("SELECT * FROM notes WHERE id = :id")
	LiveData<Note> getById(Long id);
}
