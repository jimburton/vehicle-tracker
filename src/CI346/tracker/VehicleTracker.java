package CI346.tracker;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class VehicleTracker {
    private final ConcurrentMap<String, Vehicle>
            locations;
    private final Map<String, Vehicle> unmodifiableMap;

    public VehicleTracker
            (Map<String, Vehicle> points) {
        locations = new ConcurrentHashMap<String, Vehicle>(points);
        unmodifiableMap =
                Collections.unmodifiableMap(locations);
    }

    public Map<String, Vehicle> getLocations() {
        return unmodifiableMap;
    }

    public Vehicle getLocation(String id) {
        return locations.get(id);
    }

    public void setLocation(String id, Vehicle p) {
        //System.out.println("Setting location for "+id);
        if(locations.replace(id, p) == null)
            throw new IllegalArgumentException("No such vehicle: "+id);
    }
}
