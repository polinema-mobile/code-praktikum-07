package com.dhanifudin.notesapp.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.dhanifudin.notesapp.Constant;
import com.dhanifudin.notesapp.Data;
import com.dhanifudin.notesapp.R;
import com.dhanifudin.notesapp.Settings;
import com.dhanifudin.notesapp.adapters.NoteAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoteFragment extends Fragment {

	private static final String TAG = NoteFragment.class.getSimpleName();

	private RecyclerView recyclerView;
	private NoteAdapter adapter;
	private OnNoteFragmentListener listener;

	public interface OnNoteFragmentListener {
        void onLogoutClick();
	}

	public NoteFragment() {
	}

	public void setListener(OnNoteFragmentListener listener) {
		this.listener = listener;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
													 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_note, container, false);
		recyclerView = view.findViewById(R.id.rv_notes);

		adapter = new NoteAdapter(getContext(), Data.getNotes());
		recyclerView.setAdapter(adapter);
		displayAsList();

		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_note, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	private void displayAsList() {
		RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
		recyclerView.setLayoutManager(layoutManager);
		adapter.setLayout(Constant.LAYOUT_MODE_LIST);
	}

	private void displayAsGrid() {
		RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
		recyclerView.setLayoutManager(layoutManager);
		adapter.setLayout(Constant.LAYOUT_MODE_GRID);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_show_list:
				displayAsList();
				return true;
			case R.id.action_show_grid:
				displayAsGrid();
				return true;

			case R.id.action_logout:
				Log.i(TAG, "Logout click");
				listener.onLogoutClick();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
