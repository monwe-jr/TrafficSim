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


        while (vehicleLocation.x != currentSegment.segmentLength()-1 && !getDamageStatus().isDestroyed()){
            if(currentSegment.getLane().isEmpty(new Point(vehicleLocation.x+1,vehicleLocation.y))){

                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        currentSegment.moveVehicle(vehicleLocation);
                    }
                };

                timer.schedule(task, 3000);


            }

        }

    }
}
