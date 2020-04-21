package space.spulsar.pbridge;

/**
 * @author: SunYuxing
 * @date: 2020/4/21
 */
public class BridgeManager extends BridgeAdapter {

    private static class InstanceHolder {
        private static final BridgeManager sInstance = new BridgeManager();
    }

    public static BridgeManager getInstance() {
        return InstanceHolder.sInstance;
    }

    /**
     * 不许外部初始化
     */
    private BridgeManager() {
    }
}
