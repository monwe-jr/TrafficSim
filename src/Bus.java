import java.awt.*;

public class Bus extends Vehicle {



    public Bus(Color color, boolean drive, Segment s) {
        super(color, drive);
        size = 32.0;
        weight = 11062.0;
        maxSpeed = 90.0;

        addSegment(s);
    }



    @Override
    public void move() {

    }






}
