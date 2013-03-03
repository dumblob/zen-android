package zen.stress.twister.prank;

import java.lang.reflect.Field;

import org.json.JSONArray;
import org.json.JSONException;

import zen.stress.twister.R;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PrankExecutor {
	private static PrankExecutor singleton;

	private PrankExecutor() {
	}

	public static PrankExecutor getInstance() {
		if (singleton == null) {
			singleton = new PrankExecutor();
		}

		return singleton;
	}

	public void execute(Context context, String type, Object data) {
		if (type.equals("text")) {
			this.executeTextPrank(context, (String) data);
		} else if (type.equals("audio")) {
			this.executeAudioPrank(context, (String) data);
		} else if (type.equals("vibration")) {
			try {
				JSONArray jsonArray = (JSONArray) data;
				long[] pattern = new long[jsonArray.length()];
				for (int i = 0; i < jsonArray.length(); i++) {
					pattern[i] = jsonArray.getInt(i);
				}

				this.executeVibrationPrank(context, pattern);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (type.equals("image")) {
			this.executeImagePrank(context, (String) data);
		} else {
			throw new IllegalArgumentException(String.format("unsupported type: %s", type));
		}
	}

	private void executeTextPrank(final Context context, final String text) {
		Log.i("PRANK_TEXT", text);

		Handler h = new Handler(Looper.getMainLooper());
		h.post(new Runnable() {
			public void run() {
				LayoutInflater inflater = LayoutInflater.from(context);
				View layout = inflater.inflate(R.layout.text_layout, null);
				TextView textView = (TextView) layout.findViewById(R.id.textView);
				textView.setText(text);

				WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

				DisplayMetrics metrics = new DisplayMetrics();
				wm.getDefaultDisplay().getMetrics(metrics);
				textView.setHeight(metrics.heightPixels);
				textView.setWidth(metrics.widthPixels);

				Toast toast = new Toast(context);
				toast.setView(layout);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.setDuration(Toast.LENGTH_LONG);
				toast.show();
			}
		});
	}

	private void executeAudioPrank(final Context context, final String resourceName) {
		Log.i("PRANK_AUDIO", resourceName);

		int id;
		try {
			Field field = R.raw.class.getField(resourceName);
			id = field.getInt(null);
		} catch (Exception e) {
			Log.e("MyTag", "Failure to get drawable id.", e);

			return;
		}

		final int resourceId = id;

		Handler h = new Handler(Looper.getMainLooper());
		h.post(new Runnable() {
			public void run() {
				MediaPlayer mp = MediaPlayer.create(context, resourceId);
				mp.setOnCompletionListener(new OnCompletionListener() {
					@Override
					public void onCompletion(MediaPlayer mp) {
						mp.release();
					}
				});
				mp.start();
			}
		});
	}

	private void executeVibrationPrank(Context context, long[] pattern) {
		Log.i("PRANK_VIBRATION", pattern.toString());

		Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		v.vibrate(pattern, -1);
	}

	private void executeImagePrank(final Context context, final String resourceName) {
		Log.i("PRANK_IMAGE", resourceName);

		Handler h = new Handler(Looper.getMainLooper());
		h.post(new Runnable() {
			public void run() {
				LayoutInflater inflater = LayoutInflater.from(context);
				View layout = inflater.inflate(R.layout.image_layout, null);
				ImageView imageView = (ImageView) layout.findViewById(R.id.imageView);

				WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

				Drawable drawable;
				try {
					Field field = R.drawable.class.getField(resourceName);
					int drawableId = field.getInt(null);
					Resources res = context.getResources();
					drawable = res.getDrawable(drawableId);
				} catch (Exception e) {
					Log.e("MyTag", "Failure to get drawable id.", e);

					return;
				}

				imageView.setImageDrawable(drawable);

				DisplayMetrics metrics = new DisplayMetrics();
				wm.getDefaultDisplay().getMetrics(metrics);
				imageView.setMinimumHeight(metrics.heightPixels);
				imageView.setMinimumWidth(metrics.widthPixels);

				Toast toast = new Toast(context);
				toast.setView(layout);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.setDuration(Toast.LENGTH_LONG);
				toast.show();
			}
		});
	}
}
