package space.spulsar.pbridge;

import space.spulsar.pbridge.aidl.IResult;

/**
 * @author: SunYuxing
 * @date: 2020/4/20
 */
public interface IFetchListener {

    /**
     * 接收到回复
     * @param contentType
     * @param contentBody
     */
    void fetch(String contentType, String contentBody, IResultCallback callback);

    /**
     * 接收到同步回复请求
     * @param contentType
     * @param contentBody
     * @return
     */
    IResult fetchSync(String contentType, String contentBody);
}
