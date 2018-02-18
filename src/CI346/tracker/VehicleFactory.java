package CI346.tracker;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ThreadLocalRandom;
import CI346.tracker.Vehicle.DIR;

public class VehicleFactory {

    public static final int MAX_X = 300;
    public static final int MAX_Y = 300;

    public static ConcurrentMap<String, Vehicle> getVehicles(int numVehicles) {
        ConcurrentMap<String, Vehicle> result = new ConcurrentHashMap<String, Vehicle>();
        String prefix = "VEHICLE";
        String name;
        for(int i = 0; i < numVehicles; i++) {
            name = prefix + i;
            Vehicle p = randomPoint();
            result.put(name, p);
        }
        return result;
    }

    private static Vehicle randomPoint() {
        int randX = ThreadLocalRandom.current().nextInt(0, MAX_X);
        int randY = ThreadLocalRandom.current().nextInt(0, MAX_Y);
        return new Vehicle(randX, randY, randomDir(), randomDir());
    }

    private static DIR randomDir() {
        int toss = ThreadLocalRandom.current().nextInt(2);
        return toss == 0 ? DIR.NEG : DIR.POS;
    }
}
