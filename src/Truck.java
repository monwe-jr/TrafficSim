import java.awt.*;
import java.util.TimerTask;

public class Truck extends Vehicle {


    public Truck(Color color, boolean drive, Segment s ) {
        super(color, drive, s );
        size = 72.0;
        weight = 36287.0;
        maxSpeed = 100.0;
        length = 3;

    }

    @Override
    public void move() {
                        currentSegment.moveVehicle(vehicleLocation);

    }
}
