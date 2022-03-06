import java.awt.*;
import java.util.TimerTask;

public class Car extends Vehicle  {

    /**
     * Constructor for Car realizing type Vehicle.
     * @param color Color of the Vehicle.
     * @param drive Whether the Vehicle is drivable by a player.
     */
    public Car(Color color, boolean drive, Segment s ) {
        super(color, drive, s );
        size = 14.7;
        weight = 1624.0;
        maxSpeed = 160.0;
        length = 1;
    }

    @Override
    public void move() {

        while (vehicleLocation.x != currentSegment.segmentLength() -1 && !getDamageStatus().isDestroyed()){
            if(currentSegment.getLane().isEmpty(new Point(vehicleLocation.x+1,vehicleLocation.y))){

                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        currentSegment.moveVehicle(vehicleLocation);

                    }
                };

                timer.schedule(task, 10000);


            }

        }





    }





}
