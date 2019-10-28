package com.reactlibrary;

import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.accessory.SA;
import com.samsung.android.sdk.accessory.SAAgent;
import com.samsung.android.sdk.accessory.SAPeerAgent;
import com.samsung.android.sdk.accessory.SASocket;

public class ConnectionService extends SAAgent {
    public static ConnectionService instance;
    static String TAG = "ConnectionService";
    SA sa = null;
    SASocket socket = null;
    public int agentID = 104;

    public ConnectionService() {
        super("ConnectionService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int i, int i1) {
        instance = this;
        return super.onStartCommand(intent, i, i1);
    }

    public void startService(){
        try {
            sa = new SA();
            sa.initialize(getApplicationContext());
        }catch (final SsdkUnsupportedException e){
            if (e.getType() == SsdkUnsupportedException.LIBRARY_NOT_INSTALLED) { // You should install service application first.
                Log.e(TAG, "Error -> LIBRARY_NOT_INSTALLED");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void startConnection(){
        findPeerAgents();
    }

    @Override
    protected void onFindPeerAgentsResponse(SAPeerAgent[] saPeerAgents, int i) {
//        super.onFindPeerAgentsResponse(saPeerAgents, i);
        switch (i) {
            case PEER_AGENT_FOUND:
                for(SAPeerAgent peerAgent : saPeerAgents) {
                    if(peerAgent.getAccessoryId() == agentID) requestServiceConnection(peerAgent);
                }
                break;
            case FINDPEER_DEVICE_NOT_CONNECTED:
                break;
            case FINDPEER_SERVICE_NOT_FOUND:
                break;
        }
    }



    @Override
    protected void onServiceConnectionRequested(SAPeerAgent saPeerAgent) {
        super.onServiceConnectionRequested(saPeerAgent);
        String productId = saPeerAgent.getAccessory().getProductId();
        if(productId.equals("GolfNavi")) {
            acceptServiceConnectionRequest(saPeerAgent);
        }else{
            rejectServiceConnectionRequest(saPeerAgent);
        }
    }

    @Override
    protected void onServiceConnectionResponse(SAPeerAgent saPeerAgent, SASocket saSocket, int result) {
        super.onServiceConnectionResponse(saPeerAgent, saSocket, result);
        if(result == CONNECTION_SUCCESS || result == CONNECTION_ALREADY_EXIST) {
            if(saSocket != null) socket = (ServiceConnection)saSocket;
        }else{
            Log.d(TAG, "Connection failed");
        }
    }

    public void sendToWatch(String message){
        int channelID = 123;
        if(socket != null) {
            try{
                socket.send(channelID, message.getBytes());
            }catch(Exception e){
                e.printStackTrace();
            }
        }else {
            findPeerAgents();
        }
    }

    public void receiveFromWatch(String message){
        Log.d(TAG, "message: " + message);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        startService();
        return null;
    }

    class ServiceConnection extends SASocket {
        public ServiceConnection() {
            super(ServiceConnection.class.getName());
        }
        @Override
        public void onError(int i, String s, int i1) {

        }
        @Override
        public void onReceive(int i, final byte[] bytes) {
//            Thread thread = new Thread(new Runnable() {
//                @Override
//                public void run() {
                    receiveFromWatch(bytes.toString());
//                }
//            });
//            thread.start();
        }
        @Override
        protected void onServiceConnectionLost(int result) {
            socket.close();
        }
    }
}
