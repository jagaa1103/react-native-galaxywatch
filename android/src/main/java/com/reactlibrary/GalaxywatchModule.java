package com.reactlibrary;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

public class GalaxywatchModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;
    private ConnectionService watchService;

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
            watchService = new ConnectionService("MyConnectionService", this.reactContext, agentID);
            if(watchService != null) {
                watchService.startConnection();
                callback.invoke();
            }else{
                errorCallback.invoke();
            }
        }catch(Exception e){
            errorCallback.invoke();
        }
    }

}
