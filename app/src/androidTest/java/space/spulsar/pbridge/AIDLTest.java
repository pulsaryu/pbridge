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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import space.spulsar.pbridge.aidl.IResult;
import space.spulsar.pbridge.aidl.ISendListener;
import space.spulsar.pbridge.aidl.IServerInterface;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
        IResult result = service.sendSync(ContentType.COMMAND, "ping");
        assertNotNull(result);
        assertEquals("success", result.contentBody);
    }

    @Test
    public void callback() throws TimeoutException, RemoteException, NotClientException, InterruptedException {
        IServerInterface service = connectService();
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        service.send(ContentType.COMMAND, "ping", new ISendListener.Stub() {
            @Override
            public void onComplete(String contentType, String contentBody) throws RemoteException {
                countDownLatch.countDown();
            }
        });

        boolean await = countDownLatch.await(10, TimeUnit.MILLISECONDS);
        assertTrue(await);
    }

    @Test
    public void sendSync() throws TimeoutException, RemoteException {
        IServerInterface service = connectService();
        IResult result = service.sendSync(ContentType.COMMAND, "ping");
        assertNotNull(result);
    }

    private IServerInterface connectService() throws TimeoutException {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        IBinder iBinder = serviceTestRule.bindService(new Intent(appContext, ServerService.class));
        assertNotNull(iBinder);

        return IServerInterface.Stub.asInterface(iBinder);
    }
}
