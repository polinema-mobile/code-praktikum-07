package com.dhanifudin.notesapp.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Entity(tableName = "notes")
public class Note implements Parcelable {

	@PrimaryKey(autoGenerate = true)
	@ColumnInfo(name = "id")
	private Long id;

	private String title;
	private Date date;
    private String content;

    @Ignore
    public Note() { }

    @Ignore
	public Note(String title, Date date, String content) {
		this(null, title, date, content);
	}

	public Note(Long id, String title, Date date, String content) {
		this.id = id;
		this.title = title;
		this.date = date;
		this.content = content;
	}

	protected Note(Parcel in) {
		if (in.readByte() == 0) {
			id = null;
		} else {
			id = in.readLong();
		}
		date = new Date(in.readLong());
		title = in.readString();
		content = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		if (id == null) {
			dest.writeByte((byte) 0);
		} else {
			dest.writeByte((byte) 1);
			dest.writeLong(id);
		}
		dest.writeLong(date.getTime());
		dest.writeString(title);
		dest.writeString(content);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<Note> CREATOR = new Creator<Note>() {
		@Override
		public Note createFromParcel(Parcel in) {
			return new Note(in);
		}

		@Override
		public Note[] newArray(int size) {
			return new Note[size];
		}
	};

	@NonNull
	public Long getId() {
		return id;
	}

	public void setId(@NonNull Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getDate() {
		return date;
	}

    public String getFormattedDate() {
		DateFormat formatter = new SimpleDateFormat("dd MMM yyyy", Locale.US);
		return formatter.format(date);
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
