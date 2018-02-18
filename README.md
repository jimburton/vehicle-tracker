# Vehicle Tracker

An exercise for CI346 adapted from Goetz et al., *Java Concurrency in Practice*. See the lecture
slides for Week 3 for an explanation.

The application consists of a `VehicleTracker` that keeps track of a set of `Vehicle` objects
and manages access to them with a `ConcurrentMap`. `Vehicle` is an immutable class that encapsulates 
an *(x, y)* coordinate and a direction in which the vehicle is moving. These classes represent the
*model* of the application. 

The *controllers* are those classes in `CI346.tracker.in`, and are subclasses of `Receiver`. 
`Receiver` is a subclass of `Thread` that simulates data about vehicle locations coming in to the 
application. For instance, a `GPSReceiver` is initialised with a copy of the `VehicleTracker` and
the ID of the vehicle for which it will simulate GPS data. It does this by sleeping for a short 
time then getting a new location for the vehicle by calling its `move` method, then sending the 
new location to the tracker.

The *view* is provided by classes in the package `CI346.tracker.in`. Currently there is only one of 
these, `VehicleGUI` but you could easily write more. `VehicleGUI` repeatedly fetches the collection
of vehicles from the tracker then draws their location in a `JPanel`.

Clone the repository and read the code to get an idea of how it works. Run the `main` method in the 
class `CI346.Main`. You will see ten vehicles moving about the screen, each of which is controlled
by a `GPSReceiver`.

## Exercise

The rectangle drawn on the screen by `VehicleGUI` represents an area in which only a fixed number 
of vehicles, *n*, should be allowed at any one time. You are create a new subclass of `Receiver` that
moves vehicles normally when outside of the critical area. However, when the vehicle is about to
enter the critical area the receiver will stop moving the vehicle and wait for permission to
enter. Permission is given when there are fewer than *n* vehicles in the area.

You will achieve this using an instance of `java.util.concurrent.Semaphore`. This is a bounded 
semaphore (use the constructor that takes an int) that will keep track of how many vehicles are
in the critical area. Make a subclass of `Receiver` called `GuardedGPSReceiver` with a constructor
that takes the `id` of the vehicle to track, a copy of the vehicle tracker, a `java.awt.Rectangle`
that represents the critical area, and a semaphore:

    public GuardedGPSReceiver(VehicleTracker tracker,
                              String id,
                              Rectangle criticalArea,
                              Semaphore semaphore) 
                              
The `run` method of the new receiver will be very similar to that of `GPSReceiver` except that
if moving the vehicle would mean it enters the critical area, the receiver will ask for permission 
by calling `semaphore.acquire`. If more than *n* vehicles are in the critical area, this will put 
the receiver to sleep until a vehicle leaves. When moving the vehicle would mean leaving the critical
area, your receiver should release the semaphore. In pseudocode, the logic that determines when to
move a vehicle looks like this:

    let future := where the vehicle will be if we move it
    let hasPermission := false
    if future is in critical area:
        if hasPermission is false:
            wait for permission
            hasPermission := true
        move the vehicle
    else:
        if hasPermission is true:
            release the permission
            hasPermission := false
        move the vehicle

Your receiver will need a method that tells you whether a vehicle is in the critical area:

    private boolean inCriticalArea(Vehicle v)
    
The simplest way to write this is by using `Rectangle.contains`.

Change the `main` method so that it creates a `Rectangle` with the right dimensions and 
a bounded semaphore before creating the receivers:

    Semaphore s = new Semaphore(3);
    Rectangle rect = new Rectangle(10, 10, 200, 200);
    Receiver r;
    for(int i=0; i<10; i++) {
      r = new GuardedGPSReceiver(tracker, "VEHICLE"+i, rect, s);
      r.start();
    }
    
Once you ge this working, you should see the vehicles waiting on the edge of the area before
receiving permission to enter. Change the semaphore to one that uses a *fair policy* (i.e. the next
thread to be woken up will be the one that has been waiting longest). What difference does this 
make to the way the application runs?

    