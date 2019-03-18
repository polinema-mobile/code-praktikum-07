package com.dhanifudin.notesapp.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.dhanifudin.notesapp.dao.NoteDao;
import com.dhanifudin.notesapp.dao.UserDao;
import com.dhanifudin.notesapp.models.Note;
import com.dhanifudin.notesapp.models.User;

@Database(entities = {User.class, Note.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {

	private static final String DB_NAME = "notes";

	private static volatile  AppDatabase INSTANCE = null;

	public abstract UserDao userDao();

	public abstract NoteDao noteDao();

	public static AppDatabase getInstance(final Context context) {
		synchronized (AppDatabase.class) {
			if (INSTANCE == null) {
				INSTANCE = Room
					.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DB_NAME)
					.fallbackToDestructiveMigration()
					.build();
			}
		}

		return INSTANCE;
	}

    public static void destroyInstance() {
        INSTANCE = null;
	}
}
