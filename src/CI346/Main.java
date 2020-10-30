package CI346;

import CI346.tracker.Vehicle;
import CI346.tracker.VehicleFactory;
import CI346.tracker.VehicleTracker;
import CI346.tracker.in.GPSReceiver;
import CI346.tracker.in.GuardedGPSReceiver;
import CI346.tracker.in.TargetReceiver;
import CI346.tracker.out.VehicleGUI;
import CI346.tracker.in.Receiver;

import java.awt.*;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class Main {

    public static void main(String[] args) {
        VehicleTracker tracker = new VehicleTracker(VehicleFactory.getVehicles(10));
        Map<String, Vehicle> locs = tracker.getLocations();
        for(String id: locs.keySet()) {
            System.out.println(tracker.getLocation(id));
        }
        VehicleGUI gui = new VehicleGUI(tracker);
        Semaphore s = new Semaphore(3);                   //max 3 vehicles in the critical area
        Rectangle rect = new Rectangle(10, 10, 200, 200); // the critical area
        Receiver r;
        for(int i=0; i<10; i++) {
            r = new GuardedGPSReceiver(tracker, "VEHICLE"+i, rect, s);
            r.start();
        }
        new TargetReceiver(tracker).start();
    }
}
