package space.spulsar.pbridge;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

/**
 * @author: SunYuxing
 * @date: 2020/4/20
 */
public class ServerService extends Service {

    private static final String TAG = "ServerService";
    private static ServerService sInstance;
    private IServerInterface.Stub server;
    private IClientInterface mClient;

    public static ServerService getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public IBinder onBind(Intent intent) {
        server = new IServerInterface.Stub() {

            @Override
            public void registerClient(IClientInterface client) throws RemoteException {
                mClient = client;
            }

            @Override
            public void send() throws RemoteException {

            }

            @Override
            public AidlResult sendSync(String contentType, String contentBody) throws RemoteException {
                switch (contentType) {
                    case ContentType.COMMAND:
                        return handleCommand(contentBody);
                }

                return null;
            }
        };
        return server;
    }

    public AidlResult sendCommand(String command, String content) throws RemoteException, NotClientException {
        if (mClient != null) {
            return mClient.sendSync(ContentType.COMMAND, content);
        } else {
            throw new NotClientException();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sInstance = null;
        Log.d(TAG, "onDestroy: ");
    }

    private AidlResult handleCommand(String command) {
        if ("ping".equals(command)) {
            return ping();
        }

        return null;
    }

    private AidlResult ping() {
        AidlResult aidlResult = new AidlResult();
        aidlResult.code = 0;
        aidlResult.cotentType = ContentType.STRING;
        aidlResult.contentBody = "success";
        return aidlResult;
    }
}
