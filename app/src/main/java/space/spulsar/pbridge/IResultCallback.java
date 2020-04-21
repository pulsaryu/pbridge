package space.spulsar.pbridge;

/**
 * @author: SunYuxing
 * @date: 2020/4/20
 */
public interface IResultCallback {
    void onSuccess(String contentType, String contentBody);

    void onFailure(Exception e);
}
