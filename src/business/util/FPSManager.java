package business.util;

/**
 * Created by Robert on 07.04.2017.
 */
public class FPSManager {

    private double FPS = 60.0;
    private long millis;
    private int nanos;
    private long time1;
    private long time2;

    public FPSManager(double FPS) {
        this.FPS = FPS;
    }

    public void setFPS(double FPS) {
        this.FPS = FPS;
    }

    public void start() {
        time1 = System.nanoTime();
    }

    public void waitForNextFrame() {
        time2 = System.nanoTime();
        millis = (long)(1000.0/FPS-(time2-time1)/1000000);
        nanos = (int)(((1000.0/FPS-(time2-time1)/1000000.0)-((double)millis))*1000000.0);
        try {
            Thread.sleep(millis, nanos);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        time1 = System.nanoTime();
    }
}
