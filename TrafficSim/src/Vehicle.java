import java.awt.*;

public abstract class Vehicle{
    Color c;
    boolean drivable;

    Vehicle(Color c, boolean driveable){
        this.c = c;
        this.drivable = driveable;
    }

    public abstract void remove();

    public abstract void add();

    public abstract void stop();

    public abstract void acceleration();


}
