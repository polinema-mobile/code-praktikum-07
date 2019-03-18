package com.dhanifudin.notesapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.dhanifudin.notesapp.adapters.NoteAdapter;
import com.dhanifudin.notesapp.fragments.LoginFragment;
import com.dhanifudin.notesapp.fragments.NewNoteFragment;
import com.dhanifudin.notesapp.fragments.NoteFragment;
import com.dhanifudin.notesapp.fragments.RegisterFragment;
import com.dhanifudin.notesapp.fragments.SettingFragment;
import com.dhanifudin.notesapp.models.Note;
import com.dhanifudin.notesapp.models.User;
import com.dhanifudin.notesapp.viewmodels.NoteViewModel;
import com.dhanifudin.notesapp.viewmodels.UserViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements
    LoginFragment.OnLoginFragmentListener,
	RegisterFragment.OnRegisterFragmentListener,
	NoteFragment.OnNoteFragmentListener,
	NewNoteFragment.OnNewNoteFragmentListener,
	NoteAdapter.OnNoteAdapterListener {

	private static final String TAG = MainActivity.class.getSimpleName();


	private UserViewModel userViewModel;
	private NoteViewModel noteViewModel;

	private Settings settings;
	private Session session;

	public Settings getSettings() {
		return settings;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		settings = new Settings(this);
		session = new Session(settings);

		userViewModel = ViewModelProviders.of(this)
				.get(UserViewModel.class);
		noteViewModel = ViewModelProviders.of(this)
				.get(NoteViewModel.class);
		addFragment();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
        	createSettingFragment();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void addFragment() {
        Fragment fragment = (session.isLogin()) ? new NoteFragment() : new LoginFragment();
		changeFragment(fragment, false);
	}

	private void changeFragment(Fragment fragment, boolean addToBackStack) {
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.fragment_container, fragment);
		if (addToBackStack) {
			transaction.addToBackStack(null);
		}
		transaction.commit();
	}

	private void createSettingFragment() {
		Fragment settingFragment = new SettingFragment();
		changeFragment(settingFragment, true);
	}

	@Override
	public void onLoginButtonClicked(final View view, final String username, final String password) {
		userViewModel
				.getUser(username)
				.observe(this, new Observer<User>() {
					@Override
					public void onChanged(@Nullable User user) {
					    String message = "Authentication failed";
                        if (user != null && password.equals(user.getPassword())) {
                            message = "Welcome " + username;
                            session.setUser(username);
                            addFragment();
                        }
                        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
					}
				});
	}

	@Override
	public void onRegisterLinkClicked() {
		Fragment registerFragment = new RegisterFragment();
		changeFragment(registerFragment, false);
	}

	@Override
	public void onRegisterButtonClicked(View view, String username, String password) {
		User user = new User(username, password);
		userViewModel.insert(user);
		Snackbar.make(view, "Registration has been successfull", Snackbar.LENGTH_SHORT)
				.show();
		LoginFragment fragment = new LoginFragment();
		changeFragment(fragment, false);
	}

	@Override
	public void onLoginLinkClicked() {
		Fragment loginFragment = new LoginFragment();
		changeFragment(loginFragment, false);
	}

    @Override
    public void onLogoutMenuClicked() {
        session.doLogout();
        addFragment();
    }

	@Override
	public void onNotesLoad(final NoteAdapter adapter) {
		noteViewModel.getNotes()
				.observe(this, new Observer<List<Note>>() {
					@Override
					public void onChanged(@Nullable List<Note> notes) {
						adapter.setNotes(notes);
					}
				});
	}

	@Override
    public void onAddButtonClicked() {
        Fragment newNoteFragment = new NewNoteFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.NOTE_TAG, Constant.INSERT_NOTE);
        newNoteFragment.setArguments(bundle);
        changeFragment(newNoteFragment, true);
    }

	@Override
	public void onSaveButtonClicked(View view, Note note, int tag) {
	    if (tag == Constant.INSERT_NOTE)
            noteViewModel.insert(note);
	    else
	        noteViewModel.update(note);
		Fragment fragment = new NoteFragment();
		changeFragment(fragment, false);
		Snackbar.make(view, "Saving note....", Snackbar.LENGTH_SHORT)
				.show();
		// return to previous fragment
        getSupportFragmentManager()
                .popBackStack();
	}

	@Override
	public void onNoteClicked(Note note) {
		Fragment fragment = new NewNoteFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constant.NOTE, note);
        bundle.putInt(Constant.NOTE_TAG, Constant.UPDATE_NOTE);
        fragment.setArguments(bundle);
		changeFragment(fragment, true);
	}
}
