import javax.swing.*;
import java.awt.*;

public class TrafficLight {
    private Timer timer;
    private boolean isRed;
    private Point x; //location of the traffic light


    TrafficLight(Point p, JPanel panel) {
        add(p, panel);
        functionality();
        this.x = p;
    }

    //adds a traffic light at point p
    public void add(Point p, JPanel panel) {

    }

    //adds functionality using timer
    private void functionality() {

    }

    //changes light color
    private void changeLight() {

    }

    //returns a list of traffic lights adjacent to this traffic light based on  data  in Map class
    private void adjLights() {
    }

    //returns a list of traffic lights opposite to this traffic light based on data in Map class
    private void oppLights() {

    }


}
