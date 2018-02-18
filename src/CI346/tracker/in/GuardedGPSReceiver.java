package CI346.tracker.in;

import CI346.tracker.Vehicle;
import CI346.tracker.VehicleTracker;

import java.awt.*;
import java.util.concurrent.Semaphore;

public class GuardedGPSReceiver extends Receiver {

    private final Rectangle criticalArea;
    private boolean hasPermission = false;
    private final Semaphore semaphore;

    public GuardedGPSReceiver(VehicleTracker tracker,
                              String id,
                              Rectangle criticalArea,
                              Semaphore semaphore) {
        super(tracker, id);
        this.criticalArea = criticalArea;
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Vehicle now = tracker.getLocation(trackedID);
            Vehicle future = now.move();
            if(inCriticalArea(future)) {
                if(!hasPermission) {
                    System.out.println(trackedID + " entering critical area!");
                    try {
                        semaphore.acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    hasPermission = true;
                }
                tracker.setLocation(trackedID, future);
            } else {
                if (hasPermission) {
                    semaphore.release();
                    hasPermission = false;
                }
                tracker.setLocation(trackedID, future);
            }
        }
    }

    private boolean inCriticalArea(Vehicle v) {
        return criticalArea.contains(new Point(v.x, v.y));
    }
}
