package com.reactlibrary;

import android.content.Intent;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

public class GalaxywatchModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    Callback errorCallback;

    public GalaxywatchModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "Galaxywatch";
    }

    @ReactMethod
    public void sampleMethod(String stringArgument, int numberArgument, Callback callback) {
        // TODO: Implement some actually useful functionality
        callback.invoke("Received numberArgument: " + numberArgument + " stringArgument: " + stringArgument);
    }

    @ReactMethod
    public void startService(int agentID, Callback callback){
        try{
            Intent intent = new Intent(reactContext, ConnectionService.class);
            this.reactContext.startService(intent);
            callback.invoke("samsung conneciton service started");
        }catch(Exception e){
            e.printStackTrace();
//            errorCallback.invoke();
        }
    }

    @ReactMethod
    public void startConnection(){
        try{
            ConnectionService.instance.startConnection();
        }catch(Exception e){
            e.printStackTrace();
//            errorCallback.invoke();
        }
    }

}
