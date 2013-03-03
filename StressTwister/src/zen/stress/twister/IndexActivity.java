package zen.stress.twister;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import zen.stress.twister.fragments.tabs.TabsViewPagerFragmentActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gcm.GCMRegistrar;

public class IndexActivity extends FragmentActivity {
	private static final String REPORT_LOCATION_URL = "http://%s/api/device/%s/location";

	MyLocationListener mlocListener;
	LocationManager mlocManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.index_list_view);

		mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		mlocListener = new MyLocationListener();
		mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);

		this.reportLocation();
		this.registerPushNotificationsId();

		this.startActivity(new Intent(this, TabsViewPagerFragmentActivity.class));
	}

	private void reportLocation() {
		Resources res = getResources();

		String deviceId = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);
		String url = String.format(REPORT_LOCATION_URL, res.getString(R.string.api_domain), deviceId);

		JSONObject object;
		try {
			JSONObject location = new JSONObject();
			if (mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				if (MyLocationListener.latitude > 0) {
					location.put("lat", MyLocationListener.latitude);
					location.put("lng", MyLocationListener.longitude);
				}
			} else {
				location.put("lat", 0);
				location.put("lng", 0);
			}

			object = new JSONObject();
			object.put("location", location);
		} catch (JSONException e) {
			e.printStackTrace();

			return;
		}

		new ReportLocationTask(url, object).execute(null, null, null);
	}

	private void registerPushNotificationsId() {
		Resources res = getResources();
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
		final String regId = GCMRegistrar.getRegistrationId(this);
		if (regId.equals("")) {
			GCMRegistrar.register(this, res.getString(R.string.push_notification_sender_id));
			Log.i("PUSH_NOTIFICATION", "ID registered");
		} else {
			Log.i("PUSH_NOTIFICATION", "ID already registered");
		}
	}

	private static class ReportLocationTask extends AsyncTask<Void, Void, Void> {
		private String url;
		private JSONObject data;

		public ReportLocationTask(String url, JSONObject data) {
			this.url = url;
			this.data = data;
		}

		@Override
		protected Void doInBackground(Void... params) {

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPut method = new HttpPut(this.url);
			method.setHeader("Accept", "application/json");

			try {
				StringEntity input = new StringEntity(this.data.toString());
				input.setContentType("application/json");

				method.setEntity(input);
				httpClient.execute(method);
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

	public static class MyLocationListener implements LocationListener {

		private static double latitude;
		private static double longitude;

		@Override
		public void onLocationChanged(Location loc) {
			MyLocationListener.latitude = loc.getLatitude();
			MyLocationListener.longitude = loc.getLongitude();
		}

		@Override
		public void onProviderDisabled(String provider) {

		}

		@Override
		public void onProviderEnabled(String provider) {
			Log.i("GPS", "provider enabled");

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extra) {
			// TODO Auto-generated method stub

		}

		public void setLatitude(double latitude) {
			MyLocationListener.latitude = latitude;
		}

		public double getLatitude() {
			return latitude;
		}

		public void setLongitude(double longitude) {
			MyLocationListener.longitude = longitude;
		}

		public double getLongitude() {
			return longitude;
		}

	}

}
