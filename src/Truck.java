import java.awt.*;

public class Truck extends Vehicle {


    public Truck(Color color, boolean drive, Segment s) {
        super(color, drive);
        size = 72.0;
        weight = 36287.0;
        maxSpeed = 100.0;

        addSegment(s);

    }

    @Override
    public void move() {

    }
}
