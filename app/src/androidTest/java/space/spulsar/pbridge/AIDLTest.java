package space.spulsar.pbridge;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ServiceTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author: SunYuxing
 * @date: 2020/4/20
 */
@RunWith(AndroidJUnit4.class)
public class AIDLTest {

    private static final String TAG = "AIDLTest";

    @Rule
    public final ServiceTestRule serviceTestRule = new ServiceTestRule() {
        @Override
        protected void beforeService() {
            super.beforeService();
            Log.d(TAG, "beforeService: ");
        }
    };

    @Test
    public void connect() throws TimeoutException, RemoteException {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        IBinder iBinder = serviceTestRule.bindService(new Intent(appContext, ServerService.class));
        assertNotNull(iBinder);

        IServerInterface serverInterface = IServerInterface.Stub.asInterface(iBinder);
        assertNotNull(serverInterface);

        serviceTestRule.unbindService();
    }

    @Test
    public void ping() throws TimeoutException, RemoteException {
        IServerInterface service = connectService();
        AidlResult aidlResult = service.sendSync(ContentType.COMMAND, "ping");
        assertNotNull(aidlResult);
        assertEquals("success", aidlResult.contentBody);
    }

    @Test
    public void callback() throws TimeoutException, RemoteException, NotClientException {
        final int code = 123;
        IServerInterface service = connectService();
        service.registerClient(new IClientInterface.Stub() {
            @Override
            public void send() throws RemoteException {

            }

            @Override
            public AidlResult sendSync(String contentType, String contentBody) throws RemoteException {
                AidlResult aidlResult = new AidlResult();
                aidlResult.code = code;
                return aidlResult;
            }
        });

        AidlResult aidlResult = ServerService.getInstance().sendCommand(ContentType.COMMAND, "ping");
        assertEquals(code, aidlResult.code);
    }

    private IServerInterface connectService() throws TimeoutException {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        IBinder iBinder = serviceTestRule.bindService(new Intent(appContext, ServerService.class));
        assertNotNull(iBinder);

        return IServerInterface.Stub.asInterface(iBinder);
    }
}
