import java.awt.*;
import java.io.Serializable;


public abstract class Vehicle implements Serializable {
    private Color c;
    private boolean drivable; //distinguishes humans from AI
    protected int size;
    protected Double weight;
    protected Double maxSpeed;
    private Reputation reputation;
    private DamageStatus damageStatus;
    protected Segment currentSegment;
    protected Segment target;
    protected Point vehicleLocation; //lane location on segment s

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
        reputation = new Reputation();
        damageStatus = new DamageStatus(reputation, Vehicle.this);



    }






    public void move(){
        currentSegment.moveVehicle(vehicleLocation);
    }






    /**
     * Declares a new segment location
     * @param s
     */
    public void setSegment(Segment s){
        currentSegment = s;
    }


    /**
     * removes a segment from the occupied arraylist after it has been used by the vehicle
     */
    public void removeSegment(){
        currentSegment = null;

    }


    /**
     * Returns the segment the vehicle object is currently on
     * @return
     */
    public Segment getSegment(){
        return currentSegment;
    }


    public Reputation getReputation(){return reputation;}

    /**
     * Returns the size of the vehicle.
     * @return Double size
     */
    public int getSize(){
        return  size;
    }

    /**
     * Returns the weight of the vehicle.
     * @return Double weight
     */
    public Double getWeight(){
        return weight;
    }

    /**
     * Returns the Max Speed of the vehicle.
     * @return Double max speed
     */
    public Double getMaxSpeed(){
        return maxSpeed;
    }





    /**
     * Returns damage status object
     * @return damage status
     */
    public DamageStatus getDamageStatus() {
        return damageStatus;
    }

    /**
     * Changes vehicle's location on segment s
     * @param p new location
     */
    public void setVehicleLocation(Point p){
            vehicleLocation = p;
    }


    /**
     * Returns the current segment location of vehicle object
     * @return vehicle location
     */
    public Point getVehicleLocation(){
        return vehicleLocation;

    }


    public boolean isDrivable() {
        return drivable;
    }
}