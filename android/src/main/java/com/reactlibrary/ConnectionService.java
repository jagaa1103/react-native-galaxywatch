package com.reactlibrary;

import android.content.Context;
import android.content.ServiceConnection;
import android.util.Log;

import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.accessory.SA;
import com.samsung.android.sdk.accessory.SAAgentV2;
import com.samsung.android.sdk.accessory.SAPeerAgent;
import com.samsung.android.sdk.accessory.SASocket;

public class ConnectionService extends SAAgentV2 {

    static String TAG = "ConnectionService";
    SA sa = new SA();
    SASocket socket = null;

    protected ConnectionService(String s, Context context) {
        super(s, context);
        try {
            sa.initialize(context);
        }catch (final SsdkUnsupportedException e){
            if (e.getType() == SsdkUnsupportedException.LIBRARY_NOT_INSTALLED) { // You should install service application first.
                Log.d(TAG, "Error -> LIBRARY_NOT_INSTALLED");
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
        super.onFindPeerAgentsResponse(saPeerAgents, i);
        switch (i) {
            case PEER_AGENT_FOUND:
                requestServiceConnection(saPeerAgents[0]);
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

    class ServiceConnection extends SASocket {
        public ServiceConnection() {
            super(ServiceConnection.class.getName());
        }
        @Override
        public void onError(int i, String s, int i1) {

        }
        @Override
        public void onReceive(int i, final byte[] bytes) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    receiveFromWatch(bytes.toString());
                }
            });
            thread.start();
        }
        @Override
        protected void onServiceConnectionLost(int result) {
            socket.close();
        }
    }
}
