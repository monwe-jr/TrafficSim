import java.awt.*;
import java.util.TimerTask;

public class Bus extends Vehicle {



    public Bus(Color color, boolean drive, Segment s ) {
        super(color, drive, s );
        size = 32.0;
        weight = 11062.0;
        maxSpeed = 90.0;
        length = 2;

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

                timer.schedule(task, 2000);


            }

        }

    }






}
