import java.awt.*;

public class Car extends Vehicle  {

    /**
     * Constructor for Car realizing type Vehicle.
     * @param color Color of the Vehicle.
     * @param drive Whether the Vehicle is drivable by a player.
     */
    public Car(Color color, boolean drive) {
        super(color, drive);
        size = 14.7;
        weight = 1624.0;
        maxSpeed = 160.0;
    }

    @Override
    public void move() {


    }





}
