import java.awt.*;
import java.util.ArrayList;

public abstract class Vehicle {
    private Color c;
    private boolean drivable;
    protected Double size;
    protected Double weight;
    protected Double maxSpeed;
    private DamageStatus damageStatus;
    private double reputation;

    public Vehicle(Color color, boolean drive) {
        this.c = color;
        this.drivable = drive;

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
