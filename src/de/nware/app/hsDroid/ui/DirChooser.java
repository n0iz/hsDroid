package de.nware.app.hsDroid.ui;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import de.nware.app.hsDroid.R;

public class DirChooser extends nListActivity {
	private static final String TAG = "hsDroid-DirChooser";
	private SharedPreferences mPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dir_chooser_list);
		customTitle("Downloadverzeichnis");
		if (savedInstanceState != null) {

		}

		mPreferences = PreferenceManager.getDefaultSharedPreferences(this);

		final ArrayList<File> sdDirs = new ArrayList<File>();
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			File extDir = Environment.getExternalStorageDirectory();
			Log.d(TAG, "absolutePath:" + extDir.getAbsolutePath());
			Log.d(TAG, "canonicalPath:" + extDir.getAbsolutePath());
			FileFilter dirFilter = new FileFilter() {

				@Override
				public boolean accept(File pathname) {
					// TODO Auto-generated method stub
					return pathname.isDirectory();
				}
			};
			for (File file : extDir.listFiles(dirFilter)) {
				// Log.d(TAG, "SD Dir:" + file.getName());
				if (!file.isHidden()) {
					sdDirs.add(file);
				}

			}
			// Sortiere Liste
			Collections.sort(sdDirs);
			// Setze Liste in ListView
			getListView()
					.setAdapter(new ArrayAdapter<File>(getApplicationContext(), R.layout.dir_chooser_item, sdDirs));
			getListView().setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
					showToast("pos:" + pos + " name: " + sdDirs.get(pos).getAbsolutePath());

					Editor ed = mPreferences.edit();
					ed.putString("downloadPathPref", sdDirs.get(pos).getAbsolutePath());
					ed.commit();
					finish();
					// if (hasSubDirs(sdDirs.get(pos))) {
					//
					// }
				}
			});
		} else {
			showToast("SD Karte nicht verfügbar.");
		}

	}

	private boolean hasSubDirs(File file) {
		for (File childDir : file.listFiles()) {
			if (childDir.isDirectory()) {
				return true;
			}
		}
		return false;
	}
}