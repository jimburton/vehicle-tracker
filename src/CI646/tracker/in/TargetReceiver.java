package CI646.tracker.in;

import CI646.tracker.VehicleTracker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TargetReceiver extends Receiver {
    public TargetReceiver(VehicleTracker tracker) {
        super(tracker, null);
    }

    private void setTrackedID(String id) {
        this.trackedID = id;
    }

    public void run() {
        System.out.println("Vehicle Target Receiver");
        System.out.println("Enter the ID of the vehicle to track, or newline to end");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            String input = br.readLine();
            while (!input.trim().isEmpty()) {
                tracker.setTargetID("VEHICLE"+input);
                System.out.println("Enter the ID of the vehicle to track, or newline to end");
                input = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
