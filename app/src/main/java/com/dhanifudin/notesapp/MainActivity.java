package com.dhanifudin.notesapp;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.dhanifudin.notesapp.fragments.LoginFragment;
import com.dhanifudin.notesapp.fragments.NoteFragment;
import com.dhanifudin.notesapp.fragments.SettingFragment;
import com.dhanifudin.notesapp.models.User;

public class MainActivity extends AppCompatActivity
	implements LoginFragment.OnLoginFragmentListener,
	NoteFragment.OnNoteFragmentListener {

	private static final String TAG = MainActivity.class.getSimpleName();

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
		Fragment fragment = null;
		if (session.isLogin()) {
			fragment = new NoteFragment();
			((NoteFragment) fragment).setListener(this);
			Log.i(TAG, "Create NoteFragment");
		} else {
			fragment = new LoginFragment();
			((LoginFragment) fragment).setListener(this);
			Log.i(TAG, "Create LoginFragment");
		}

		getSupportFragmentManager().beginTransaction()
			.replace(R.id.fragment_container, fragment)
			.commit();
	}

	private void createSettingFragment() {
		Fragment settingFragment = new SettingFragment();
		getSupportFragmentManager().beginTransaction()
			.replace(R.id.fragment_container, settingFragment)
			.addToBackStack(null)
			.commit();
	}

	@Override
	public void onLogoutClick() {
        session.doLogout();
        addFragment();
	}

	@Override
	public void onLoginClicked(View view, String username, String password) {
        User user = session.doLogin(username, password);
        String message = "Authentication failed";
		if (user != null) {
			message = "Welcome " + username;
			session.setUser(username);
		}
		Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
		addFragment();
	}
}
