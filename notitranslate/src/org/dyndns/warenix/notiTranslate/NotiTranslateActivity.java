package org.dyndns.warenix.notiTranslate;

import org.dyndns.warenix.google.translate.TranslationMaster;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;

public class NotiTranslateActivity extends Activity {
	public static final String LOG_TAG = NotiTranslateActivity.class
			.getSimpleName();

	String[] languageCodeList;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preference);

		languageCodeList = TranslationMaster
				.getLanguageCodeList(getApplicationContext());

		Spinner languageSpinner = (Spinner) findViewById(R.id.languageSpinner);
		languageSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView adapterView, View view,
					int position, long id) {
				String locale = languageCodeList[position];
				Log.d(LOG_TAG,
						String.format("selected %d %s", position, locale));
				TranslateActivity.saveLanguageCode(getApplicationContext(),
						locale);
				TranslateActivity
						.sendDefaultNotification(getApplicationContext());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		String locale = TranslateActivity
				.getSavedLanguageCode(getApplicationContext());

		int index = TranslationMaster
				.findIndexOfValue(languageCodeList, locale);

		languageSpinner.setSelection(index);

		TranslateActivity.sendDefaultNotification(getApplicationContext());

	}

}