package com.ataw.cordova.plugin;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

import java.lang.Override;

public class LoadingPlugin extends CordovaPlugin {

	private AndroidProgressHUD fActivityIndicator = null;
	private String fText = null;
	private boolean enableLoadingWhenChangePage = false;

	@Override
	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		fText = this.preferences.getString("defaultLoadingTitle", "loading");
		enableLoadingWhenChangePage = this.preferences.getBoolean("enableLoadingWhenChangePage", true);
	}

	@Override
	public Object onMessage(String id, Object data) {
		if(enableLoadingWhenChangePage) {
			if (id == "onPageStarted")
				show(fText);
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

	public void show(String text) {
		this.fText = text;

		cordova.getActivity().runOnUiThread(new Runnable() {
			public void run() {
				fActivityIndicator = AndroidProgressHUD.show(LoadingPlugin.this.cordova.getActivity(), LoadingPlugin.this.fText, true,true,null);
			}
		});
	}

	public void hide() {
		if (fActivityIndicator != null) {
			fActivityIndicator.dismiss();
			fActivityIndicator = null;
		}
	}
}