package CI346.tracker;

import static CI346.tracker.VehicleFactory.MAX_X;
import static CI346.tracker.VehicleFactory.MAX_Y;

public class Vehicle {
    public final int x, y;
    public final DIR xdir, ydir;
    private static final int STEP = 1;

    public enum DIR {
        POS(1),
        NEG(-1);
        private final int value;

        private DIR(int value) {
            this.value = value;
        }
    }

    public Vehicle(int x, int y, DIR xdir, DIR ydir) {
        this.x = x;
        this.y = y;
        this.xdir = xdir;
        this.ydir = ydir;
    }

    public Vehicle move() {
        DIR newXDir = transformDir(x, MAX_X, xdir);
        DIR newYDir = transformDir(y, MAX_Y, ydir);
        int newX = x + (STEP * newXDir.value);
        int newY = y + (STEP * newYDir.value);
        return new Vehicle(newX, newY, newXDir, newYDir);

    }

    private DIR transformDir(int val, int max, DIR d) {
        if (val <= 0 || val > max) {
            return reverse(d);
        }
        return d;
    }

    private static DIR reverse(DIR d) {
        return d == DIR.POS ? DIR.NEG : DIR.POS;
    }

    @Override
    public String toString() {
        return String.format("[%d,%d]", x, y);
    }
}
