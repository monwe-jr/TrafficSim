import java.awt.*;
import java.util.ArrayList;

public abstract class Vehicle {
    private Color c;
    private boolean drivable;
    protected Double size;
    protected Double weight;
    protected Double maxSpeed;
    private DamageStatus damageStatus;
    private Reputation reputation;

    /**
     * Creates a Vehicle with a specified color and indicator for player vehicle.
     * NOTE: Abstract class maybe shouldn't have a constructor?
     * @param color The color of the vehicle.
     * @param drive True if player vehicle false otherwise.
     */
    public Vehicle(Color color, boolean drive) {
        this.c = color;
        this.drivable = drive;
        reputation = new Reputation();
    }








    public abstract void move();



    public Double getSize(){
        return  size;
    }

    public Double getWeight(){
        return weight;
    }

    public Double getMaxSpeed(){
        return maxSpeed;
    }



}
