# Vehicle Tracker

An exercise for CI646 adapted from Goetz et al., *Java Concurrency in Practice*. See the lecture
slides for Week 3 for an explanation.

The application consists of a `VehicleTracker` that keeps track of a set of `Vehicle` objects
and manages access to them with a `ConcurrentMap`. `Vehicle` is an immutable class that encapsulates 
an *(x, y)* coordinate and a direction in which the vehicle is moving. These classes represent the
*model* of the application. 

The *controllers* are those classes in `CI646.tracker.in`, and are subclasses of `Receiver`. 
`Receiver` is a subclass of `Thread` that simulates data about vehicle locations coming in to the 
application. For instance, a `GPSReceiver` is initialised with a copy of the `VehicleTracker` and
the ID of the vehicle for which it will simulate GPS data. It does this by sleeping for a short 
time then getting a new location for the vehicle by calling its `move` method, then sending the 
new location to the tracker.

The *view* is provided by classes in the package `CI646.tracker.out`. Currently there is only one of 
these, `VehicleGUI` but you could easily write more. `VehicleGUI` repeatedly fetches the collection
of vehicles from the tracker then draws their location in a `JPanel`.

Clone the repository and read the code to get an idea of how it works. Run the `main` method in the 
class `CI646.Main`. You will see ten vehicles moving about the screen, each of which is controlled
by a `GPSReceiver`.

## Exercise 1

The rectangle drawn on the screen by `VehicleGUI` represents an area in which only a fixed number 
of vehicles, *n*, should be allowed at any one time. You will make a new subclass of `Receiver` that
moves vehicles in the same way as `GPSReceiver` when outside of the critical area. However, when the vehicle is about to
enter the critical area the receiver will stop moving the vehicle and wait for permission to
enter. Permission is given when there are fewer than *n* vehicles in the area.

You will achieve this using an instance of `java.util.concurrent.Semaphore`. This is a bounded 
semaphore (use the constructor that takes an int) that will keep track of how many vehicles are
in the critical area. Make a subclass of `Receiver` called `GuardedGPSReceiver` with a constructor
that takes a copy of the vehicle tracker, the `id` of the vehicle to track, a `java.awt.Rectangle`
that represents the critical area, and a semaphore:

    public GuardedGPSReceiver(VehicleTracker tracker,
                              String id,
                              Rectangle criticalArea,
                              Semaphore semaphore) 
                              
The `run` method of the new receiver will begin in a similar way to that of `GPSReceiver` by
sleeping for 25ms. However, if moving the vehicle would mean it enters the critical area, the 
receiver will ask for permission by calling `semaphore.acquire`. If more than *n* vehicles are 
in the critical area, this will put the receiver to sleep until a vehicle leaves. When moving 
the vehicle would mean leaving the critical area, your receiver should release the semaphore. 
In pseudocode, the logic that determines when to move a vehicle looks like this:

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
    
One way to write this is by using `Rectangle.contains`.

Change the `main` method so that it creates a `Rectangle` with the right dimensions and 
a bounded semaphore before creating the receivers:

    Semaphore s = new Semaphore(3);                   //max 3 vehicles in the critical area
    Rectangle rect = new Rectangle(10, 10, 200, 200); // the critical area
    Receiver r;
    for(int i=0; i<10; i++) {
      r = new GuardedGPSReceiver(tracker, "VEHICLE"+i, rect, s);
      r.start();
    }
    
Once you get this working you should see the vehicles waiting on the edge of the area before
receiving permission to enter. You may notice that some vehicles continue to wait while other
vehicles are given permission to enter. Change the semaphore to one that uses a *fair policy* 
(i.e. the next thread to be woken up will be the one that has been waiting longest). What 
difference does this make to the way the application runs?

## Exercise 2

Add a new type of `Receiver` called `TargetReceiver`. This receiver will launch a terminal allowing
an operator to enter the ID of a vehicle of special interest. Change the `VehicleGUI` class so that
it queries the tracker to see if there is a targetted vehicle. If there is one, the `VehicleGUI` will
call attention top it by drawing it on screen in a different colour.

Create a `String` field in `VehicleTracker` called `targetID` with accompanying getters and setters.
The `run` method of `TargetReceiver` will consist mainly of a `while` loop that reads lines of input
from the user until an empty line is entered, at which point the loop and the `run` methodf should 
exit. Since we don't know which vehicle the `TargetReceiver` is tracking when the instance is created,
the constructor should call the superclass constructor passing in `null` for the `trackedID` 
parameter. When an ID is entered, call `tracker.setTargetID`.

In the `paint` method of `VehicleGUI`, add a call to `tracker.getTargetID`. When drawing the vehicles,
if the ID matches `targetID` you should draw it in a different colour, not forgetting to change the 
colour back again afterwards.

    
