package org.dyndns.warenix.notiTranslate;

import java.util.Locale;

import org.dyndns.warenix.google.translate.TranslationMaster;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.ClipboardManager;

public class TranslateActivity extends Activity {
	static final String BUNDLE_TRANSLATED_TEXT = "translated_text";
	private static final String PREFERENCE_NAME = "notiTranslate";
	private static final String PREFERENCE_KEY_LANGUAGE_CODE = "languageCode";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.translate);

		final ClipboardManager myClipBoard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

		if (myClipBoard.hasText()) {
			String clipboardText = myClipBoard.getText().toString();

			Context context = getApplicationContext();
			String languageCode = TranslateActivity
					.getSavedLanguageCode(context);
			new TranslateAsyncTask(context, clipboardText, languageCode)
					.execute();
		} else {
			Context context = getApplicationContext();
			String title = context.getString(R.string.app_name);
			String text = "Translate clipboard text to your languagee";
			sendNotification(context, title, text);
		}
		/**
		 * windowless activity
		 */
		finish();
	}

	protected void onResume() {
		super.onResume();
	}

	public static void sendNotification(Context context, String title,
			String text) {
		CharSequence contentTitle = title;
		CharSequence contentText = text;

		Intent notificationIntent = new Intent(context, TranslateActivity.class);
		notificationIntent.putExtra(BUNDLE_TRANSLATED_TEXT, text);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				notificationIntent, 0);

		Notification notification = new Notification(R.drawable.ic_launcher,
				text, System.currentTimeMillis());
		notification.setLatestEventInfo(context, contentTitle, contentText,
				contentIntent);

		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(ns);
		mNotificationManager.notify(1, notification);
	}

	public static void sendDefaultNotification(Context context) {
		String languageCode = getSavedLanguageCode(context);
		String languageName = TranslationMaster.getLocaleNameByCode(context,
				languageCode);
		String title = context.getString(R.string.app_name);
		String text = String.format("Translate clipboard text to '%s'",
				languageName);
		sendNotification(context, title, text);
	}

	class TranslateAsyncTask extends AsyncTask<Void, Void, Void> {
		Context context;
		String text;
		String languageCode;

		public TranslateAsyncTask(Context context, String text,
				String languageCode) {
			this.context = context;
			this.text = text;
			this.languageCode = languageCode;
		}

		public Void doInBackground(Void... args) {
			String translatedText = TranslationMaster.translate(text,
					languageCode);

			String title = context.getString(R.string.app_name);
			String text = translatedText;
			sendNotification(context, title, text);

			return null;
		}
	}

	public static String getSavedLanguageCode(Context context) {
		SharedPreferences settings = context.getSharedPreferences(
				PREFERENCE_NAME, 0);
		String locale = settings.getString(PREFERENCE_KEY_LANGUAGE_CODE,
				Locale.ENGLISH.toString());
		return locale;
	}

	public static void saveLanguageCode(Context context, String languageCode) {
		SharedPreferences settings = context.getSharedPreferences(
				PREFERENCE_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(PREFERENCE_KEY_LANGUAGE_CODE, languageCode);
		editor.commit();
	}

}
