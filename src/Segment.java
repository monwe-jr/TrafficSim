import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Segment {

    private Point location;  // this road segment is between intersection x and intersection y
    private ArrayList<Integer> intersections = new ArrayList<>();
    private ArrayList<Integer> lanes = new ArrayList<>();
    private Random num = new Random();

    Segment(Point intersections) {
        this.location = intersections;
        addIntersections(intersections);
        addLane();
    }



    private void addLane() {
        int random = num.nextInt(3);

        for (int i = 0; i < random; i++) {
            lanes.add(0);
        }

    }


    //fix this
    public int laneLocation() {
        return lanes.indexOf(1);
    }


    private boolean canSwitchLeft() {
        if (laneLocation() != 0) {
            return true;
        }
        return false;
    }


    private boolean canSwitchRight() {
        if (laneLocation() != lanes.size() - 1) {
            return true;
        }
        return false;
    }


    public void switchLeft() {
        if (canSwitchLeft()) {
            int location = laneLocation();
            lanes.set(location, 0);
            lanes.set(location - 1, 1);
        }
    }


    public void switchRight() {
        if (canSwitchRight()) {
            int location = laneLocation();
            lanes.set(location, 0);
            lanes.set(location + 1, 1);
        }
    }


    public Point getLocation() {
        return location;
    }


    private void addIntersections(Point p) {
        intersections.add(p.x);
        intersections.add(p.y);
    }


    //segment is one way
    public boolean oneWay(Map m) {
        Point toFind = new Point(location.y, location.x);

        for (int i = 0; i < m.getMap().get(location.y).size(); i++) {
            if (m.getMap().get(location.y).get(i).getLocation().equals(toFind)) {
                return false;
            }
        }

        return true;
    }


    //Checks if one way segment has a turn at intersection 'ID'
    public boolean canTurn(Map m, int ID) {
        ArrayList<Segment> roads = m.getMap().get(ID);

        for (int i = 0; i < roads.size(); i++) {
            if(roads.get(i).getLocation().x == location.y){
                return true;
            }
        }



        return false;
    }


    public ArrayList<Integer> getIntersections() {
        return intersections;
    }


}
