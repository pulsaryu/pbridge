package space.spulsar.pbridge;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;

import space.spulsar.pbridge.aidl.IResult;
import space.spulsar.pbridge.aidl.ISendListener;
import space.spulsar.pbridge.aidl.IServerInterface;

/**
 * @author: SunYuxing
 * @date: 2020/4/21
 */
class BridgeAdapter {

    private static final String TAG = "BridgeAdapter";
    private IServerInterface mServerInterface;
    private IFetchListener mFetchListener;
    private final Handler mConnectHandler = new Handler(Looper.getMainLooper());
    private ConnectPart2 mConnectPart2;

    public void connect(final Context context, final String targetPackageName, final IBridgeConnection connection) {
        if (mConnectPart2 != null) {
            mConnectPart2.stopRetry();
            mConnectHandler.removeCallbacks(mConnectPart2);
        }
        mConnectPart2 = new ConnectPart2(context, targetPackageName, connection);
        mConnectHandler.post(mConnectPart2);
    }

    public void setFetchListener(IFetchListener listener) {
        mFetchListener = listener;
    }

    public IFetchListener getFetchListener() {
        return mFetchListener;
    }

    public void send(String contentType, String contentBody, final IResultCallback resultCallback) {
        if (mServerInterface != null) {
            try {
                mServerInterface.send(contentType, contentBody, new ISendListener.Stub() {
                    @Override
                    public void onComplete(String contentType, String contentBody) throws RemoteException {
                        resultCallback.onSuccess(contentType, contentBody);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                resultCallback.onFailure(e);
            }
        }
    }

    public IResult sendSync(String contentType, String contentBody) throws Exception {
        if (mServerInterface != null) {
            return mServerInterface.sendSync(contentType, contentBody);
        } else {
            throw new NotConnectedException();
        }
    }

    private void onConnected(IServerInterface serverInterface) {

    }

    private class ConnectPart2 implements Runnable {

        private static final int RETRY_DELAY_MILLIS = 1000;
        private final Context context;
        private final String targetPackageName;
        private final IBridgeConnection connection;
        private boolean mRetry = true;

        public ConnectPart2(Context context, String targetPackageName, IBridgeConnection connection) {
            super();
            this.context = context.getApplicationContext();
            this.targetPackageName = targetPackageName;
            this.connection = connection;
        }

        void stopRetry() {
            mRetry = false;
        }

        @Override
        public void run() {
            Intent serviceIntent = new Intent();
            String serviceClassName = ServerService.class.getName();
            ComponentName componentName = new ComponentName(targetPackageName, "space.spulsar.pbridge.ServerService");
            serviceIntent.setComponent(componentName);
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "开始连接");
            }
            context.bindService(serviceIntent, new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "onServiceConnected: ");
                    }
                    mServerInterface = IServerInterface.Stub.asInterface(service);
                    onConnected(mServerInterface);
                    if (connection != null) {
                        connection.onServiceConnected();
                    }
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "onServiceDisconnected: ");
                    }
                    mServerInterface = null;
                    if (connection != null) {
                        connection.onServiceDisconnected();
                    }
                    // 重连
                    if (mRetry) {
                        mConnectHandler.postDelayed(ConnectPart2.this, RETRY_DELAY_MILLIS);
                    }
                }
            }, Context.BIND_AUTO_CREATE);
        }
    }
}
