package CI346.tracker.in;

import CI346.tracker.VehicleTracker;


public abstract class Receiver extends Thread {
    protected final VehicleTracker tracker;
    protected final String trackedID;

    public Receiver(VehicleTracker tracker, String id) {
        this.tracker = tracker;
        this.trackedID = id;
    }
}
