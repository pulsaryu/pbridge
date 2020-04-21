package space.spulsar.pbridge;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import space.spulsar.pbridge.aidl.IResult;
import space.spulsar.pbridge.aidl.ISendListener;
import space.spulsar.pbridge.aidl.IServerInterface;

/**
 * @author: SunYuxing
 * @date: 2020/4/20
 */
public class ServerService extends Service {

    private static final String TAG = "ServerService";
    private static ServerService sInstance;
    private IServerInterface.Stub server;

    public static ServerService getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onCreate: ");
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        server = new IServerInterface.Stub() {

            @Override
            public void send(String contentType, String contentBody, final ISendListener listener) throws RemoteException {
                IFetchListener fetchListener = BridgeManager.getInstance().getFetchListener();
                if (fetchListener != null) {
                    fetchListener.fetch(contentType, contentBody, new IResultCallback() {
                        @Override
                        public void onSuccess(String contentType, String contentBody) {
                            if (listener != null) {
                                try {
                                    listener.onComplete(contentType, contentBody);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Exception e) {
                            if (listener != null) {
                                try {
                                    listener.onComplete(ContentType.ERROR, e.getMessage());
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    });
                } else {
                    if (listener != null) {
                        listener.onComplete(ContentType.ERROR, "[empty]");
                    }
                }
            }

            @Override
            public IResult sendSync(String contentType, String contentBody) throws RemoteException {
                IFetchListener fetchListener = BridgeManager.getInstance().getFetchListener();
                if (fetchListener != null) {
                    return fetchListener.fetchSync(contentType, contentBody);
                } else {
                    IResult result = new IResult();
                    result.cotentType = ContentType.ERROR;
                    result.contentBody = "[empty]";
                    return result;
                }
            }
        };
        return server;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sInstance = null;
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onDestroy: ");
        }
    }

    private IResult handleCommand(String command) {
        if ("ping".equals(command)) {
            return ping();
        }

        return null;
    }

    private IResult ping() {
        IResult result = new IResult();
        result.cotentType = ContentType.STRING;
        result.contentBody = "success";
        return result;
    }
}
