package com.reactlibrary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

import java.sql.Connection;

import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.samsung.android.sdk.accessory.SAAgentV2;

public class GalaxywatchModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    private WatchService watchService = null;
    static final String TAG = "GalaxywatchModule";

    Callback errorCallback;

    public GalaxywatchModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        IntentFilter filter = new IntentFilter("fromWatch");
        reactContext.registerReceiver(receiver, filter);
        SAAgentV2.requestAgent(reactContext, WatchService.class.getName(), mAgentCallback);
    }

    @Override
    public String getName() {
        return "Galaxywatch";
    }


    @ReactMethod
    public void start(){
        try{
            watchService.start();
        }catch(Exception e){
            e.printStackTrace();
//            errorCallback.invoke();
        }
    }

    @ReactMethod
    public void connect(){
        try{
            watchService.connect();
        }catch(Exception e){
            e.printStackTrace();
//            errorCallback.invoke();
        }
    }

    @ReactMethod
    public void sendToWatch(String message){
        watchService.sendToWatch(message);
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            sendMessageToReact(message);
        }
    };

    private void sendMessageToReact(String message){
        Log.d(TAG, "==================== message from watch =====================");
        Log.d(TAG, message);
        Log.d(TAG, "=============================================================");
        WritableMap params = Arguments.createMap();
        params.putString("sendEventFromGalaxyWatch", message);
        if(reactContext != null) reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit("fromGalaxyWatch", params);
    }

    private SAAgentV2.RequestAgentCallback mAgentCallback = new SAAgentV2.RequestAgentCallback() {
        @Override
        public void onAgentAvailable(SAAgentV2 agent) {
            watchService = (WatchService) agent;
        }

        @Override
        public void onError(int errorCode, String message) {
            Log.e(TAG, "Agent initialization error: " + errorCode + ". ErrorMsg: " + message);
        }
    };
}
