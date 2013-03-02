package zen.stress.twister;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.provider.Settings.Secure;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {
	private static final String REGISTER_ID_URL = "http://%s/api/push-notification/%s";

	@Override
	protected void onMessage(Context context, Intent intent) {
		String content = intent.getExtras().getString("0");
		Log.i("MSG", content);
	}

	@Override
	protected void onError(Context context, String errorId) {
		// nothing to do
	}

	@Override
	protected void onRegistered(Context context, String registrationId) {
		Resources res = getResources();

		String deviceId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
		String url = String.format(REGISTER_ID_URL, res.getString(R.string.api_domain), deviceId);

		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPut method = new HttpPut(url);
		method.setHeader("Accept", "application/json");

		try {
			JSONObject object = new JSONObject();
			object.put("registration_id", registrationId);

			StringEntity input = new StringEntity(object.toString());
			input.setContentType("application/json");

			method.setEntity(input);
			httpClient.execute(method);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onUnregistered(Context context, String registrationId) {
		Resources res = getResources();

		String deviceId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
		String url = String.format(REGISTER_ID_URL, res.getString(R.string.api_domain), deviceId);

		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpDelete method = new HttpDelete(url);
		method.setHeader("Accept", "application/json");

		try {
			httpClient.execute(method);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
