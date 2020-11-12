package CI646.tracker.in;

import CI646.tracker.Vehicle;
import CI646.tracker.VehicleTracker;

public class GPSReceiver extends Receiver {
    public GPSReceiver(VehicleTracker tracker, String id) {
        super(tracker, id);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Vehicle v = tracker.getLocation(trackedID);
            tracker.setLocation(trackedID, v.move());
        }
    }
}

