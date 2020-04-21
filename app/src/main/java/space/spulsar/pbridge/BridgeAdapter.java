package space.spulsar.pbridge;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import space.spulsar.pbridge.aidl.IResult;
import space.spulsar.pbridge.aidl.ISendListener;
import space.spulsar.pbridge.aidl.IServerInterface;

/**
 * @author: SunYuxing
 * @date: 2020/4/21
 */
class BridgeAdapter {

    private IServerInterface mServerInterface;
    private IFetchListener mFetchListener;

    public void connect(Context context) {
        context.bindService(new Intent(context, ServerService.class), new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mServerInterface = IServerInterface.Stub.asInterface(service);
                onConnected(mServerInterface);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mServerInterface = null;
            }
        }, 0);
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
}
