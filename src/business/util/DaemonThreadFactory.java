package business.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Created by Robert on 23.04.2017.
 */
public class DaemonThreadFactory implements ThreadFactory {
    @Override
    public Thread newThread(Runnable r) {
        Thread t = Executors.defaultThreadFactory().newThread(r);
        t.setDaemon(true);
        return t;
    }
}
