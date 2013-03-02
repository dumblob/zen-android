package zen.stress.twister.fragments.tabs;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import zen.stress.twister.R;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class Tab1Fragment extends Fragment {
	private static final String PRANK_REQUEST_URL = "http://%s/api/device/%s/prank";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}

		View view = inflater.inflate(R.layout.tab_frag1_layout, container, false);

		OnClickListener listener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				Tab1Fragment.this.requestPrank(v.getTag().toString());
			}
		};

		view.findViewById(R.id.button1).setOnClickListener(listener);
		view.findViewById(R.id.button2).setOnClickListener(listener);
		view.findViewById(R.id.button3).setOnClickListener(listener);
		view.findViewById(R.id.button4).setOnClickListener(listener);

		return view;
	}

	private void requestPrank(String distance) {
		View inflated = this.getActivity().getLayoutInflater().inflate(R.layout.tab_frag3_layout, null);
		RadioGroup group = (RadioGroup) inflated.findViewById(R.id.radioGroupPranksBuiltin);
		RadioButton btn = (RadioButton) group.findViewById(group.getCheckedRadioButtonId());
		String prank = btn.getText().toString();

		if (prank.equalsIgnoreCase("random")) {
			prank = Tab3Fragment.pranks[new Random().nextInt(Tab3Fragment.pranks.length)];
		}

		JSONObject object = new JSONObject();
		try {
			object.put("distance", distance);

			if (prank.equalsIgnoreCase("fart")) {
				object.put("type", "audio");
				object.put("data", "fart");
			} else if (prank.equalsIgnoreCase("falsealarm")) {
				object.put("type", "text");
				object.put("data", "Your car is being stolen ;)");
			} else if (prank.equalsIgnoreCase("vibration")) {
				object.put("type", "vibration");

				int dot = 200; // Length of a Morse Code "dot" in milliseconds
				int dash = 500; // Length of a Morse Code "dash" in milliseconds
				int short_gap = 200; // Length of Gap Between dots/dashes
				int medium_gap = 500; // Length of Gap Between Letters
				int long_gap = 1000; // Length of Gap Between Words

				int[] pattern = { 0, // Start immediately
						dot, short_gap, dot, short_gap, dot, // s
						medium_gap, dash, short_gap, dash, short_gap, dash, // o
						medium_gap, dot, short_gap, dot, short_gap, dot, // s
						long_gap };

				ArrayList<Integer> list = new ArrayList<Integer>();
				for (int part : pattern) {
					list.add(part);
				}

				object.put("data", new JSONArray(list));
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return;
		}

		String domain = this.getResources().getString(R.string.api_domain);
		String url = String.format(PRANK_REQUEST_URL, domain, this.getDeviceId());

		new RequestPrankTask(url, object).execute(null, null, null);
	}

	private String getDeviceId() {
		return Secure.getString(this.getView().getContext().getContentResolver(), Secure.ANDROID_ID);
	}

	private static class RequestPrankTask extends AsyncTask<Void, Void, Void> {
		private String url;
		private JSONObject data;

		public RequestPrankTask(String url, JSONObject data) {
			this.url = url;
			this.data = data;
		}

		@Override
		protected Void doInBackground(Void... params) {

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost method = new HttpPost(this.url);
			method.setHeader("Accept", "application/json");

			try {
				StringEntity input = new StringEntity(this.data.toString());
				input.setContentType("application/json");

				method.setEntity(input);
				HttpResponse resp = httpClient.execute(method);

				Log.i("PRANK_STATUS", resp.getStatusLine().toString());
				Log.i("PRANK_CONTENT", this.data.toString());
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return null;
		}
	}
}
