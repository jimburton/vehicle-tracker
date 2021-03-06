package CI646;

import CI646.tracker.Vehicle;
import CI646.tracker.VehicleFactory;
import CI646.tracker.VehicleTracker;
import CI646.tracker.in.GPSReceiver;
import CI646.tracker.out.VehicleGUI;
import CI646.tracker.in.Receiver;

import java.util.Map;

public class Main {

    public static void main(String[] args) {
        VehicleTracker tracker = new VehicleTracker(VehicleFactory.getVehicles(10));
        Map<String, Vehicle> locs = tracker.getLocations();
        for(String id: locs.keySet()) {
            System.out.println(tracker.getLocation(id));
        }
        VehicleGUI gui = new VehicleGUI(tracker);
        Receiver r;
        for(int i=0; i<10; i++) {
            r = new GPSReceiver(tracker, "VEHICLE"+i);
            r.start();
        }
    }
}
