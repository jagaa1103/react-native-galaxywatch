package com.reactlibrary;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.accessory.SA;
import com.samsung.android.sdk.accessory.SAAgentV2;
import com.samsung.android.sdk.accessory.SAPeerAgent;
import com.samsung.android.sdk.accessory.SASocket;

public class WatchService extends SAAgentV2 {
    static String TAG = "WatchService";
    private static final Class<ServiceConnection> SASOCKET_CLASS = ServiceConnection.class;
    ServiceConnection socket;
    Context mContext = null;

    public WatchService(Context context) {
        super("NaviNote", context, SASOCKET_CLASS);
//        super("NaviNote", context);
        mContext = context;
    }


    public void startConnection(){
        findPeerAgents();
    }
    @Override
    protected void onFindPeerAgentsResponse(SAPeerAgent[] saPeerAgents, int i) {
        switch (i) {
            case PEER_AGENT_FOUND:
                Log.d(TAG, "================== onFindPeerAgentsResponse =================");
                for(SAPeerAgent peerAgent : saPeerAgents) {
                    if(peerAgent.getAppName().contains("GolfNavi")) {
                        Log.d(TAG, "======================= agent found ====================");
                        requestServiceConnection(peerAgent);
                    }
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
        if(productId.contains("GolfNavi")) {
            acceptServiceConnectionRequest(saPeerAgent);
        }else{
            rejectServiceConnectionRequest(saPeerAgent);
        }
    }

    @Override
    protected void onServiceConnectionResponse(SAPeerAgent saPeerAgent, SASocket saSocket, int result) {
        if(result == SAAgentV2.CONNECTION_SUCCESS || result == SAAgentV2.CONNECTION_ALREADY_EXIST) {
            Log.d(TAG, "=========== Connected ===========");
            socket = (ServiceConnection) saSocket;
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

    class ServiceConnection extends SASocket {
        public ServiceConnection() {
            super(ServiceConnection.class.getName());
        }
        @Override
        public void onError(int i, String s, int i1) {

        }
        @Override
        public void onReceive(int channelID, final byte[] bytes) {
            try{
                String message = new String(bytes, "UTF-8");
                Intent intent = new Intent();
                intent.setAction("fromWatch");
                intent.putExtra("message", message);
                if(mContext != null) mContext.sendBroadcast(intent);
            }catch(Exception err){
                err.printStackTrace();
            }

        }
        @Override
        protected void onServiceConnectionLost(int result) {
            this.close();
        }
    }
}
