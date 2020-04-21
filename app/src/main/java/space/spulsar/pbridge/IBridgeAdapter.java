package space.spulsar.pbridge;

import space.spulsar.pbridge.aidl.IResult;

/**
 * @author: SunYuxing
 * @date: 2020/4/21
 */
public interface IBridgeAdapter {
    void registerReceiveListener(IFetchListener listener);

    void unregisterReceiveListener(IFetchListener listener);

    void send(String contentType, String contentBody, IResultCallback sendListener) throws Exception;

    IResult sendSync(String contentType, String contentBody) throws Exception;
}
