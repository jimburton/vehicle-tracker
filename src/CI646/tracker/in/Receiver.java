package CI646.tracker.in;

import CI646.tracker.VehicleTracker;


public abstract class Receiver extends Thread {
    protected final VehicleTracker tracker;
    protected String trackedID;

    public Receiver(VehicleTracker tracker, String id) {
        this.tracker = tracker;
        this.trackedID = id;
    }
}
