package com.ataw.cordova.plugin;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException; 
import com.ataw.cordova.plugin.AndroidProgressHUD;

import java.lang.Override;

public class LoadingPlugin extends CordovaPlugin {

	private AndroidProgressHUD activityIndicator = null;
	private String text = null;
	private boolean enableLoadingWhenChangePage = false;

	@Override
	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		text = this.preferences.getString("defaultLoadingTitle", "loading");
		enableLoadingWhenChangePage = this.preferences.getBoolean("enableLoadingWhenChangePage", true);
	}

	@Override
	public Object onMessage(String id, Object data) {
		if(enableLoadingWhenChangePage) {
			if (id == "onPageStarted")
				show(text);
			else if (id == "onPageFinished" || id == "onReceivedError")
				hide();
		}
		return null;
	}

	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		if (action.equals("show")) {
			String text = args.getString(0);
			show(text);
			callbackContext.success();
			return true;
		} else if (action.equals("hide")) {
			hide();
			callbackContext.success();
			return true;
		}

		return false;
	}

	/**
	 * This show the ProgressDialog
	 * @param text - Message to display in the Progress Dialog
	 */
	public void show(String text) {
		this.text = text;

		cordova.getActivity().runOnUiThread(new Runnable() {
			public void run() {
				activityIndicator = AndroidProgressHUD.show(ActivityIndicator.this.cordova.getActivity(), ActivityIndicator.this.text, true,true,null);
			}
		});
	}

	/**
	 * This hide the ProgressDialog
	 */
	public void hide() {
		if (activityIndicator != null) {
			activityIndicator.dismiss();
			activityIndicator = null;
		}
	}
}