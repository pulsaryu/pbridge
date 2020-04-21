package space.spulsar.pbridge;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import space.spulsar.pbridge.aidl.IResult;

/**
 * @author: SunYuxing
 * @date: 2020/4/21
 */
@RunWith(AndroidJUnit4.class)
public class BridgeManagerTest {

    @Test
    public void setFetchListener() {
        IFetchListener listener = new IFetchListener() {
            @Override
            public void fetch(String contentType, String contentBody, IResultCallback callback) {

            }

            @Override
            public IResult fetchSync(String contentType, String contentBody) {
                return null;
            }
        };
        BridgeManager.getInstance().setFetchListener(listener);
        Assert.assertEquals(listener, BridgeManager.getInstance().getFetchListener());
    }
}
