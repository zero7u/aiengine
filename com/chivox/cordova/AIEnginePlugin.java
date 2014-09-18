package com.chivox.cordova;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.chivox.Kpad;
import com.sx.base.AIEngineHelper;
import com.sx.base.AIRecorder;


public class AIEnginePlugin extends CordovaPlugin {
	
	private static String tag = "AIEnginePlugin";
	
	public Kpad kpad = null;

	@Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
//		this.cordova.getActivity()
        if (action.equals("demo")) {
            String message = args.getString(0);
            this.demo(message, callbackContext);
            return true;
        } else if (action.equals("start")) {
        	this.start(args, callbackContext);
        } else if (action.equals("stop")) {
        	this.stop();
        }
        return super.execute(action, args, callbackContext);
    }
	
	private Kpad getKpad() {
		if (kpad == null) {
			kpad = (Kpad) webView.getContext();
		}
		return kpad;
	}

    private void demo(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
            callbackContext.success(message);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }
    
    private void start(JSONArray args, final CallbackContext callbackContext) {
    	Log.d(tag, "start=================");
    	Kpad.recorder.start(AIEngineHelper.getFilesDir(webView.getContext()).getPath(), new AIRecorder.Callback() {

			@Override
			public void onStarted() {
				// TODO Auto-generated method stub
//				AIEngine.aiengine_start(engine, param, id, callback)
				callbackContext.success();
			}

			@Override
			public void onData(byte[] data, int size) {
				// TODO Auto-generated method stub
//				AIEngine.aiengine_feed(size, data, size)
				JSONObject json = new JSONObject();
				try {
					json.put("data", data);
					json.put("size", size);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				callbackContext.success(json);
			}

			@Override
			public void onStopped() {
				// TODO Auto-generated method stub
//				AIEngine.aiengine_stop(engine);
				callbackContext.success();
			}
    	});
    }
    
    private void stop() {
    	System.out.println("stopping..............");
    	Kpad.recorder.stop();
    	Log.d(tag, "stop-----------------");
    }
    
}
