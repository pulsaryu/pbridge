package space.spulsar.pbridge;

/**
 * @author: SunYuxing
 * @date: 2020/4/22
 */
public interface IBridgeConnection {
    void onServiceConnected();
    void onServiceDisconnected();
}
